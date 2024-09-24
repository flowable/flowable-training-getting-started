# List all branches following the "gsd-*" pattern
$branches = git branch --list "gsd-*" | ForEach-Object { $_.Trim() } | Sort-Object

# Check if any branches are found
if ($branches.Count -eq 0) {
    Write-Output "No branches found matching the pattern 'gsd-*'."
    exit 1
}

# Iterate over each branch and perform rebase
for ($i = 1; $i -lt $branches.Count; $i++) {
    $currentBranch = $branches[$i]
    $parentBranch = $branches[$i - 1]

    Write-Output "Rebasing branch '$currentBranch' onto its parent '$parentBranch'..."

    # Checkout the current branch
    git checkout $currentBranch

    # Perform the rebase
    $rebaseResult = git rebase $parentBranch

    if ($LASTEXITCODE -ne 0) {
        Write-Output "Rebase failed for branch '$currentBranch'. Resolve conflicts, then run:"
        Write-Output "`t git rebase --continue"
        Write-Output "Or if you want to abort: `t git rebase --abort"
        exit 1
    }

    Write-Output "Successfully rebased '$currentBranch' onto '$parentBranch'."

    # Push the rebased branch to the remote repository
    Write-Output "Pushing '$currentBranch' to the remote repository..."
    git push --force-with-lease

    if ($LASTEXITCODE -ne 0) {
        Write-Output "Push failed for branch '$currentBranch'. Please check the error and push manually."
        exit 1
    }

    Write-Output "Successfully pushed '$currentBranch'."
}

# Switch back to the main branch
git checkout master
Write-Output "Rebasing and pushing completed. You are now back on the 'master' branch."
