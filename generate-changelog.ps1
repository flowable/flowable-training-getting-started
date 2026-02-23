# Store the current branch name
$originalBranch = git rev-parse --abbrev-ref HEAD
Write-Output "Starting from branch: $originalBranch"

# List all branches following the "gsd-*" pattern and sort them numerically
$branches = @(git branch --list "gsd-*" --format="%(refname:short)" | Where-Object { $_ -ne "" } | Sort-Object { [int]($_ -replace 'gsd-', '') })

# Check if any branches are found
if ($branches.Count -eq 0) {
    Write-Output "No branches found matching the pattern 'gsd-*'."
    exit 1
}

# Debug output to verify branches
Write-Output "Found the following branches:"
$branches | ForEach-Object { Write-Output "  $_" }

# Initialize the changelog content
$changelog = @("# Changelog", "")

try {
    # Iterate over each branch and generate changelog
    for ($i = 0; $i -lt $branches.Count; $i++) {
        $currentBranch = $branches[$i]
        $parentBranch = if ($i -eq 0) { "main" } else { $branches[$i - 1] }
        Write-Output "Current branch: $currentBranch"

        Write-Output "Generating changelog for branch '$currentBranch' compared to its parent '$parentBranch'..."

        # Get the changelog between the current branch and its parent
        $changelogEntries = git log $parentBranch..$currentBranch --oneline

        if ($LASTEXITCODE -ne 0) {
            throw "Failed to generate changelog for branch '$currentBranch'"
        }

        # Add the changelog entries to the changelog content
        $changelog += "## $currentBranch"
        $changelog += $changelogEntries
        $changelog += ""
    }

    # Write the changelog to CHANGELOG.MD
    $changelog | Out-File -FilePath "CHANGELOG.MD" -Encoding utf8
    Write-Output "Changelog has been written to CHANGELOG.MD"

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