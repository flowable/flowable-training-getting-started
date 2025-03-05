# Store the current branch name
$originalBranch = git rev-parse --abbrev-ref HEAD
Write-Output "Starting from branch: $originalBranch"

# List all branches following the "gsd-*" pattern
$branches = @(git branch --list "gsd-*" --format="%(refname:short)" | Where-Object { $_ -ne "" } | Sort-Object { [int]($_ -replace 'gsd-', '') })

# Check if any branches are found
if ($branches.Count -eq 0)
{
    Write-Output "No branches found matching the pattern 'gsd-*'."
    exit 1
}

# Debug output to verify branches
Write-Output "Found the following branches:"
$branches | ForEach-Object { Write-Output "  $_" }

# Initialize the changelog file
$changelogFile = "branch-overview.md"
Set-Content -Path $changelogFile -Value "# Branch Overview`n`n"

try
{
    # Iterate over each branch and generate changelog
    for ($i = 0; $i -lt $branches.Count; $i++) {
        $currentBranch = $branches[$i]
        $parentBranch = if ($i -eq 0)
        {
            "master"
        }
        else
        {
            $branches[$i - 1]
        }
        Write-Output "Current branch: $currentBranch"

        Write-Output "Generating changelog for branch '$currentBranch' compared to its parent '$parentBranch'..."

        # Checkout the current branch
        $checkoutResult = git checkout $currentBranch
        if ($LASTEXITCODE -ne 0)
        {
            throw "Failed to checkout branch '$currentBranch'"
        }

        # Get the changes in text format
        $changes = git diff --name-status $parentBranch $currentBranch | Out-String
        if ($LASTEXITCODE -ne 0)
        {
            throw "Failed to get changes for branch '$currentBranch'. Error: $( $changes | Out-String )"
        }

        # Split changes into categories
        $added = @()
        $modified = @()
        $deleted = @()
        $changes -split "`n" | ForEach-Object {
            if ($_ -match "^A\s+(.*)") {
                $added += $matches[1]
            } elseif ($_ -match "^M\s+(.*)") {
                $modified += $matches[1]
            } elseif ($_ -match "^D\s+(.*)") {
                $deleted += $matches[1]
            }
        }

        # Append the changelog to the file
        Add-Content -Path $changelogFile -Value "## Branch $currentBranch`r`n"
        if ($added.Count -gt 0) {
            Add-Content -Path $changelogFile -Value "### Added`r`n"
            $added | ForEach-Object { Add-Content -Path $changelogFile -Value "$_`r`n" }
        }
        if ($modified.Count -gt 0) {
            Add-Content -Path $changelogFile -Value "### Modified`r`n"
            $modified | ForEach-Object { Add-Content -Path $changelogFile -Value "$_`r`n" }
        }
        if ($deleted.Count -gt 0) {
            Add-Content -Path $changelogFile -Value "### Deleted`r`n"
            $deleted | ForEach-Object { Add-Content -Path $changelogFile -Value "$_`r`n" }
        }
        if ($added.Count -eq 0 -and $modified.Count -eq 0 -and $deleted.Count -eq 0) {
            Add-Content -Path $changelogFile -Value "_No changes_`r`n"
        }
        Add-Content -Path $changelogFile -Value "`r`n"
    }

    # Switch back to the original branch
    Write-Output "Switching back to original branch: $originalBranch"
    git checkout $originalBranch
    if ($LASTEXITCODE -ne 0)
    {
        throw "Failed to switch back to original branch '$originalBranch'"
    }
    Write-Output "Successfully returned to '$originalBranch'."
}
catch
{
    Write-Output "An error occurred: $_"

    # Try to return to original branch even if there was an error
    Write-Output "Attempting to return to original branch '$originalBranch'..."
    git checkout $originalBranch
    if ($LASTEXITCODE -ne 0)
    {
        Write-Output "Warning: Failed to return to original branch '$originalBranch'"
    }

    exit 1
}