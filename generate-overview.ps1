param(
    [string]$BaseBranch = "main",
    [string]$OutputFile = "branch-overview.md"
)

$bt = '`'  # backtick character - avoids PS parsing issues in strings

# Store the current branch name
$originalBranch = git rev-parse --abbrev-ref HEAD
Write-Output "Starting from branch: $originalBranch"

# Fetch and discover branches from remote
Write-Output "Fetching remote branches..."
git fetch --prune origin | Out-Null
$allBranches = @(git branch -r --list "origin/gsd-*" --format="%(refname:short)" |
        ForEach-Object { $_ -replace "^origin/", "" } |
        Where-Object { $_ -ne "" } |
        Sort-Object { [int]($_ -replace "gsd-", "") })

if ($allBranches.Count -eq 0)
{
    Write-Output "No branches found matching the pattern 'gsd-*'."
    exit 1
}

Write-Output "Found $( $allBranches.Count ) branch(es)."

$branchData = @()

try
{
    for ($i = 0; $i -lt $allBranches.Count; $i++) {
        $currentBranch = $allBranches[$i]
        $parentBranch = if ($i -eq 0)
        {
            $BaseBranch
        }
        else
        {
            $allBranches[$i - 1]
        }

        Write-Output "[$( $i + 1 )/$( $allBranches.Count )] Analysing '$currentBranch'..."

        git checkout $currentBranch 2>&1 | Out-Null
        if ($LASTEXITCODE -ne 0)
        {
            throw "Failed to checkout branch '$currentBranch'."
        }

        $commitMsg = git log -1 --pretty=format:"%s" $currentBranch

        $added = @()
        $modified = @()
        $deleted = @()

        git diff --name-status $parentBranch $currentBranch | ForEach-Object {
            if ($_ -match "^A\s+(.*)")
            {
                $added += $matches[1]
            }
            elseif ($_ -match "^M\s+(.*)")
            {
                $modified += $matches[1]
            }
            elseif ($_ -match "^D\s+(.*)")
            {
                $deleted += $matches[1]
            }
        }

        $totalFiles = $added.Count + $modified.Count + $deleted.Count

        $branchData += [PSCustomObject]@{
            Branch = $currentBranch
            Parent = $parentBranch
            CommitMsg = $commitMsg
            Added = $added
            Modified = $modified
            Deleted = $deleted
            TotalFiles = $totalFiles
        }
    }

    git checkout $originalBranch 2>&1 | Out-Null

    # --- Build markdown ---
    $lines = @()
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm"

    $lines += "# Branch Overview"
    $lines += ""
    $lines += ""

    # Tree summary
    $lines += "## Chain"
    $lines += ""
    $lines += $bt + $bt + $bt
    $lines += $BaseBranch
    for ($i = 0; $i -lt $branchData.Count; $i++) {
        $d = $branchData[$i]
        $indent = "    " * $i
        $fileLabel = if ($d.TotalFiles -eq 1)
        {
            "1 file"
        }
        else
        {
            "$( $d.TotalFiles ) files"
        }
        $lines += "$indent" + "+-- $($d.Branch)  ($fileLabel changed)"
    }
    $lines += $bt + $bt + $bt
    $lines += ""

    # Per-branch detail
    $lines += "## Details"
    $lines += ""

    foreach ($d in $branchData)
    {
        $fileLabel = if ($d.TotalFiles -eq 1)
        {
            "1 file changed"
        }
        else
        {
            "$( $d.TotalFiles ) files changed"
        }
        $lines += "### $( $d.Branch )"
        $lines += ""
        $lines += "> $( $d.CommitMsg )"
        $lines += ""
        $lines += "**Parent:** $bt$( $d.Parent )$bt | **Changes:** $fileLabel"
        $lines += ""

        if ($d.Added.Count -gt 0)
        {
            $lines += "#### Added"
            $lines += ""
            $d.Added | ForEach-Object { $lines += "- $bt$_$bt" }
            $lines += ""
        }
        if ($d.Modified.Count -gt 0)
        {
            $lines += "#### Modified"
            $lines += ""
            $d.Modified | ForEach-Object { $lines += "- $bt$_$bt" }
            $lines += ""
        }
        if ($d.Deleted.Count -gt 0)
        {
            $lines += "#### Deleted"
            $lines += ""
            $d.Deleted | ForEach-Object { $lines += "- $bt$_$bt" }
            $lines += ""
        }
        if ($d.TotalFiles -eq 0)
        {
            $lines += "_No changes compared to parent._"
            $lines += ""
        }

        $lines += "---"
        $lines += ""
    }

    $lines | Set-Content -Path $OutputFile -Encoding UTF8
    Write-Output ""
    Write-Output "Written to: $OutputFile"
}
catch
{
    Write-Output ""
    Write-Output "ERROR: $_"

    Write-Output "Attempting to return to original branch '$originalBranch'..."
    git checkout $originalBranch 2>&1 | Out-Null
    if ($LASTEXITCODE -ne 0)
    {
        Write-Output "Warning: Failed to return to original branch '$originalBranch'."
    }

    exit 1
}