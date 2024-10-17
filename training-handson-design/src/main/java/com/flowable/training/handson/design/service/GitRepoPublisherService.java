package com.flowable.training.handson.design.service;

import com.flowable.design.engine.api.history.AppRevision;
import com.flowable.design.engine.api.runtime.Model;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import java.io.File;
import java.io.IOException;

@Service
public class GitRepoPublisherService {

    @Autowired
    ModelDefinitionWriterService modelDefinitionWriterService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GitRepoPublisherService.class);
    private static final String REMOTE = "origin";

    @Value("${flowable.design.git.repo.clone-dir}")
    private String filePath;

    @Value("${flowable.design.git.repo.uri}")
    private String uri;

    @Value("${flowable.design.git.repo.remote-branch}")
    private String remoteBranch;

    @Value("${flowable.design.git.repo.username}")
    private String username;

    @Value("${flowable.design.git.repo.password}")
    private String password;

    public void commitAndPushChanges(Model appModel, AppRevision appRevision) throws IOException, NoFilepatternException, GitAPIException{
        String branchName = appRevision.getAppKey().concat(File.separator + appRevision.getKey());
        UsernamePasswordCredentialsProvider creds = new UsernamePasswordCredentialsProvider(username, password);

        Git repo = getLatestRemoteBranch(creds);

        checkoutTargetBranch(repo, branchName, creds);

        //stageFilesToTrackedBranch(repo, appRevision.getAppKey(), true);

        modelDefinitionWriterService.saveFilesFilesystem(appRevision, "/home/codespace/models-untracked");

        syncAndCommitPublishedModels(repo, new File(filePath), new File("/home/codespace/models-untracked/"));

        //stageFilesToTrackedBranch(repo, appRevision.getAppKey(), false);

        commitStagedFiles(repo, appRevision);

        pushCommit(repo, branchName, creds);

        deleteLocalBranch(repo, branchName);

    }

    private Git getLatestRemoteBranch(UsernamePasswordCredentialsProvider creds) {
        Git repo;
        try {
            repo = Git.open(new File(filePath));
            repo.checkout().setName(remoteBranch).call();
            repo.pull().setCredentialsProvider(creds).call();
        } catch (IOException e ) {
            LOGGER.debug("No existing repo found in ${filePath}. Cloning repo from remote.");
            try{
                repo = Git.cloneRepository()
                        .setURI(uri)
                        .setCloneAllBranches(true)
                        .setCredentialsProvider(creds)
                        .setDirectory(new File(filePath))
                        .call();
            } catch (GitAPIException exp) {
                throw new RuntimeException("Unable to clone remote repo to local.", exp);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to create and update local repo.", e);
        }

        return repo;
    }

    private void checkoutTargetBranch(Git repo, String branchName, UsernamePasswordCredentialsProvider creds) {
        try {
            repo.checkout().setCreateBranch(true).setName(branchName).setStartPoint(remoteBranch).call();
        } catch (RefAlreadyExistsException e) {
            LOGGER.debug("Branch: ${branchName} already exists. Checking out ${branchName}.");
            try {
                repo.checkout().setName(branchName).call();
            } catch (GitAPIException exp) {
                throw new RuntimeException("Unable to checkout branch on local repo.", exp);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unhandled exception checking out branch.", e);
        }

        try {
            repo.pull().setCredentialsProvider(creds).call();
        } catch (RefNotAdvertisedException e) {
            LOGGER.debug("No branch: ${branchName} on remote to pull from. Continuing on local branch.");
        } catch (Exception e) {
            throw new RuntimeException("Unhandled exception pulling latest branch", e);
        }
    }

    private void stageFilesToTrackedBranch(Git repo, String app, Boolean update) {
        try {
            repo.add().setUpdate(update)
                    .addFilepattern(app)
                    .call();
        } catch (Exception e) {
            throw new RuntimeException("Unable to add existing files to tracked branch", e);
        }
    }

    private void commitStagedFiles(Git repo, AppRevision appRevision) {
        String commitMessage = !appRevision.getDescription().trim().isEmpty()
                ? appRevision.getDescription()
                    .concat( ": " + appRevision.getAppKey())
                    .concat("-" + appRevision.getKey())
                : "Update Deployed Application : ".concat(appRevision.getAppKey())
                    .concat("-" + appRevision.getKey());

        try {
            if(!repo.status().call().isClean()) {
                repo.commit()
                        .setMessage(commitMessage)
                        .call();
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to commit changes on local branch", e);
        }
    }

    private void pushCommit(Git repo, String branchName, UsernamePasswordCredentialsProvider creds) {
        try {
            repo.push().setCredentialsProvider(creds).setRemote(REMOTE).add(branchName).call();
        } catch (Exception e) {
            throw new RuntimeException("Unable to push commited changes to remote", e);
        }
    }
    private void deleteLocalBranch(Git repo, String branchName) {
        try {
            repo.checkout().setName(remoteBranch).call();
            repo.branchDelete().setBranchNames(branchName).setForce(true).call();
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete local branch after pushing commited changes.", e);
        }
    }
    private void openMergeRequest(Git repo, String branchName, UsernamePasswordCredentialsProvider creds) {
    }

        // Assuming you have the Git object available, typically created as:
    // Git git = Git.open(new File(repoDirectory));

    public void syncAndCommitPublishedModels(Git git, File trackedDirectory, File newModelsDirectory) throws IOException, GitAPIException {
        // Stage all current tracked files
        git.add().addFilepattern(".").call();

        // Find files in tracked directory but not in new models directory (deleted files)
        Set<String> trackedFiles = listAllFiles(trackedDirectory);
        Set<String> newModelFiles = listAllFiles(newModelsDirectory);

        // Identify files to be removed
        trackedFiles.removeAll(newModelFiles); // Now this set contains files to delete

        // Stage deletions
        for (String fileToDelete : trackedFiles) {
            git.rm().addFilepattern(fileToDelete).call();
        }

        // Copy new model files to the tracked directory
        copyDirectory(newModelsDirectory, trackedDirectory);

        // Stage the newly added and modified files
        git.add().addFilepattern(".").call();

        // Commit the changes
        git.commit().setMessage("Sync published models with tracked files").call();
    }

    // Helper to list all files relative to the given directory
    private Set<String> listAllFiles(File directory) throws IOException {
        try (Stream<Path> paths = Files.walk(directory.toPath())) {
            return paths.filter(Files::isRegularFile)
                        .map(path -> directory.toPath().relativize(path).toString())
                        .collect(Collectors.toSet());
        }
    }

    // Helper to copy the contents of one directory to another
    private void copyDirectory(File source, File target) throws IOException {
        Files.walk(source.toPath()).forEach(sourcePath -> {
            try {
                Path targetPath = target.toPath().resolve(source.toPath().relativize(sourcePath));
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
