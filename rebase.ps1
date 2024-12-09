# Store the current branch name
$originalBranch = git rev-parse --abbrev-ref HEAD
Write-Output "Starting from branch: $originalBranch"

# List all branches following the "gsd-*" pattern
$branches = @(git branch --list "gsd-*" --format="%(refname:short)" | Where-Object { $_ -ne "" } | Sort-Object)

# Check if any branches are found
if ($branches.Count -eq 0) {
    Write-Output "No branches found matching the pattern 'gsd-*'."
    exit 1
}

# Debug output to verify branches
Write-Output "Found the following branches:"
$branches | ForEach-Object { Write-Output "  $_" }

# Check for unstaged changes and stash them if present
$status = git status --porcelain
$hasChanges = $false
if ($status) {
    Write-Output "Detected uncommitted changes. Stashing them temporarily..."
    git stash push -m "Temporary stash for rebase script"
    if ($LASTEXITCODE -ne 0) {
        Write-Output "Failed to stash changes. Please resolve any issues before running the script."
        exit 1
    }
    $hasChanges = $true
    Write-Output "Changes stashed successfully."
}

try {
    # Iterate over each branch and perform rebase
    for ($i = 0; $i -lt $branches.Count; $i++) {
        $currentBranch = $branches[$i]
        $parentBranch = if ($i -eq 0) { "master" } else { $branches[$i - 1] }
        Write-Output "Current branch: $currentBranch"

        Write-Output "Rebasing branch '$currentBranch' onto its parent '$parentBranch'..."

        # Checkout the current branch
        $checkoutResult = git checkout $currentBranch
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to checkout branch '$currentBranch'"
        }

        # Perform the rebase
        $rebaseResult = git rebase $parentBranch

        if ($LASTEXITCODE -ne 0) {
            throw "Rebase failed for branch '$currentBranch'. Resolve conflicts, then run:`n`t git rebase --continue`nOr if you want to abort:`n`t git rebase --abort"
        }

        Write-Output "Successfully rebased '$currentBranch' onto '$parentBranch'."

        # Push the rebased branch to the remote repository
        Write-Output "Pushing '$currentBranch' to the remote repository..."
        git push --force-with-lease origin $currentBranch

        if ($LASTEXITCODE -ne 0) {
            throw "Push failed for branch '$currentBranch'. Please check the error and push manually."
        }

        Write-Output "Successfully pushed '$currentBranch'."
    }

    # Switch back to the original branch
    Write-Output "Switching back to original branch: $originalBranch"
    git checkout $originalBranch
    if ($LASTEXITCODE -ne 0) {
        throw "Failed to switch back to original branch '$originalBranch'"
    }
    Write-Output "Successfully returned to '$originalBranch'."
}
catch {
    Write-Output "An error occurred: $_"

    # Try to return to original branch even if there was an error
    Write-Output "Attempting to return to original branch '$originalBranch'..."
    git checkout $originalBranch
    if ($LASTEXITCODE -ne 0) {
        Write-Output "Warning: Failed to return to original branch '$originalBranch'"
    }

    exit 1
}
finally {
    # Restore stashed changes if we stashed them
    if ($hasChanges) {
        Write-Output "Restoring your uncommitted changes..."
        git stash pop
        if ($LASTEXITCODE -ne 0) {
            Write-Output "Warning: Failed to restore stashed changes. Your changes are still in the stash."
            Write-Output "You can restore them manually with 'git stash pop' or 'git stash apply'"
            exit 1
        }
        Write-Output "Successfully restored your uncommitted changes."
    }
}