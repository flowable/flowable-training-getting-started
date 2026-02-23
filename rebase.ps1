param(
    [string]$StartFrom = "",
    [string]$BaseBranch = "main"
)

# Store the current branch name
$originalBranch = git rev-parse --abbrev-ref HEAD
Write-Output "Starting from branch: $originalBranch"

# Push the base branch to the remote repository
Write-Output "Pushing '$BaseBranch' branch to the remote repository..."
git push origin $BaseBranch
if ($LASTEXITCODE -ne 0)
{
    Write-Output "Failed to push '$BaseBranch' branch. Please check the error and push manually."
    exit 1
}
Write-Output "Successfully pushed '$BaseBranch' branch."

# List all branches following the "gsd-*" pattern
git fetch --prune origin | Out-Null
$allBranches = @(git branch -r --list "origin/gsd-*" --format="%(refname:short)" |
        ForEach-Object { $_ -replace "^origin/", "" } |
        Where-Object { $_ -ne "" } |
        Sort-Object { [int]($_ -replace "gsd-", "") })

# Check if any branches are found
if ($allBranches.Count -eq 0)
{
    Write-Output "No branches found matching the pattern 'gsd-*'."
    exit 1
}

# Apply --StartFrom filter if provided
$branches = $allBranches
if ($StartFrom -ne "")
{
    $startIndex = [Array]::IndexOf($allBranches, $StartFrom)
    if ($startIndex -eq -1)
    {
        Write-Output "Branch '$StartFrom' not found in gsd-* branches. Available branches:"
        $allBranches | ForEach-Object { Write-Output "  $_" }
        exit 1
    }
    $branches = $allBranches[$startIndex..($allBranches.Count - 1)]
    Write-Output "Resuming from branch: $StartFrom"
}

# Debug output to verify branches
Write-Output "Processing the following branches:"
$branches | ForEach-Object { Write-Output "  $_" }

# Check for unstaged changes and stash them if present
$status = git status --porcelain
$hasChanges = $false
if ($status)
{
    Write-Output "Detected uncommitted changes. Stashing them temporarily..."
    git stash push -m "Temporary stash for rebase script"
    if ($LASTEXITCODE -ne 0)
    {
        Write-Output "Failed to stash changes. Please resolve any issues before running the script."
        exit 1
    }
    $hasChanges = $true
    Write-Output "Changes stashed successfully."
}

try
{
    for ($i = 0; $i -lt $branches.Count; $i++) {
        $currentBranch = $branches[$i]

        # Determine parent: if resuming mid-chain, gsd-N's parent is the previous branch in the full list
        if ($i -eq 0)
        {
            if ($StartFrom -ne "")
            {
                $fullIndex = [Array]::IndexOf($allBranches, $currentBranch)
                $parentBranch = if ($fullIndex -eq 0)
                {
                    $BaseBranch
                }
                else
                {
                    $allBranches[$fullIndex - 1]
                }
            }
            else
            {
                $parentBranch = $BaseBranch
            }
        }
        else
        {
            $parentBranch = $branches[$i - 1]
        }

        Write-Output ""
        Write-Output "[$( $i + 1 )/$( $branches.Count )] Rebasing '$currentBranch' onto '$parentBranch'..."

        git checkout $currentBranch
        if ($LASTEXITCODE -ne 0)
        {
            throw "Failed to checkout branch '$currentBranch'."
        }

        git rebase $parentBranch
        if ($LASTEXITCODE -ne 0)
        {
            throw @"
Rebase failed for branch '$currentBranch'.

Resolve the conflicts, then run:
    git rebase --continue

Once resolved, resume this script from where it left off with:
    .\rebase-chain.ps1 -StartFrom $currentBranch

Or to abort the rebase entirely:
    git rebase --abort
"@
        }

        Write-Output "Successfully rebased '$currentBranch' onto '$parentBranch'."

        git push --force-with-lease origin $currentBranch
        if ($LASTEXITCODE -ne 0)
        {
            throw "Push failed for branch '$currentBranch'. Please check the error and push manually."
        }

        Write-Output "Successfully pushed '$currentBranch'."
    }

    Write-Output ""
    Write-Output "All branches processed successfully."
    Write-Output "Switching back to original branch: $originalBranch"
    git checkout $originalBranch
    if ($LASTEXITCODE -ne 0)
    {
        throw "Failed to switch back to original branch '$originalBranch'."
    }
    Write-Output "Done."
}
catch
{
    Write-Output ""
    Write-Output "ERROR: $_"

    Write-Output "Attempting to return to original branch '$originalBranch'..."
    git checkout $originalBranch
    if ($LASTEXITCODE -ne 0)
    {
        Write-Output "Warning: Failed to return to original branch '$originalBranch'."
    }

    exit 1
}
finally
{
    if ($hasChanges)
    {
        Write-Output "Restoring your uncommitted changes..."
        git stash pop
        if ($LASTEXITCODE -ne 0)
        {
            Write-Output "Warning: Failed to restore stashed changes."
            Write-Output "Restore manually with: git stash pop"
        }
        else
        {
            Write-Output "Successfully restored your uncommitted changes."
        }
    }
}