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
import java.util.Collections;
import java.util.Comparator;

@Service
public class GitRepoPublisherService {

    @Autowired
    ModelDefinitionWriterService modelDefinitionWriterService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GitRepoPublisherService.class);
    private static final String REMOTE = "origin";

    @Value("${flowable.design.git.repo.clone-dir}")
    private String filePath;

    @Value("${flowable.design.git.repo.untracked-models-dir}")
    private String untrackedPath;

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

        deleteDirectoryContents(new File(untrackedPath)); //ensure untracked path is empty

        modelDefinitionWriterService.saveFilesFilesystem(appRevision, untrackedPath);

        syncAndCommitPublishedModels(repo, appRevision.getAppKey(), new File(filePath + "/" + appRevision.getAppKey()), new File(untrackedPath + appRevision.getAppKey()));

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

    // ##TODO
    private void openMergeRequest(Git repo, String branchName, UsernamePasswordCredentialsProvider creds) {
    }

    public void syncAndCommitPublishedModels(Git git, String appKey, File trackedDirectory, File newModelsDirectory) throws IOException, GitAPIException {
        // Stage all current tracked files
        git.add().addFilepattern(".").call();

        // Find files in tracked directory but not in new models directory (deleted files)
        Set<String> trackedFiles = listAllFiles(trackedDirectory);
        Set<String> newModelFiles = listAllFiles(newModelsDirectory);

        // Identify files to be removed
        trackedFiles.removeAll(newModelFiles); // Now this set contains files to delete

        // Stage deletions
        for (String fileToDelete : trackedFiles) {
            git.rm().addFilepattern(appKey + "/" + fileToDelete).call();
        }

        // Copy new model files to the tracked directory
        copyDirectory(newModelsDirectory, trackedDirectory);

        deleteDirectoryContents(newModelsDirectory);

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
        } catch ( NoSuchFileException e) {
            return Collections.emptySet();
        }
    }

    // Helper to copy the contents of one directory to another
    private void copyDirectory(File source, File target) throws IOException {
        // Delete files in the target directory that do not exist in the source
        deleteExtraFiles(target, source);
    
        // Walk through the source directory and copy files
        Files.walk(source.toPath()).forEach(sourcePath -> {
            try {
                Path targetPath = target.toPath().resolve(source.toPath().relativize(sourcePath));
    
                if (Files.isDirectory(sourcePath)) {
                    // Create directories as needed
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                } else {
                    // Copy files, replacing existing ones
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private void deleteExtraFiles(File target, File source) throws IOException {
        try {
            Files.walk(target.toPath())
                .sorted(Comparator.reverseOrder()) // Ensure we delete files before directories
                .forEach(targetPath -> {
                    Path relativePath = target.toPath().relativize(targetPath);
                    Path sourceEquivalent = source.toPath().resolve(relativePath);

                    try {
                        if (Files.notExists(sourceEquivalent)) {
                            Files.delete(targetPath); // Delete if it doesn't exist in the source
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        } catch( NoSuchFileException e ) {
            LOGGER.debug("No files present for ${target.toPath().toString()}. Doing nothing, because no files to detele.");
        }
    }
    private void deleteDirectoryContents(File directory) throws IOException {
        if (directory.exists()) {
            // Walk through the directory, deleting files and directories
            Files.walk(directory.toPath())
                .sorted(Comparator.reverseOrder()) // Delete files before directories
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        }
    }
}
