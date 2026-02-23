param(
    [string]$RemoteUrl = "",
    [string]$OutputDir = ".",
    [string]$BaseBranch = "main",
    [string]$ZipName = ""
)

# --- Validate remote URL ---
if ($RemoteUrl -eq "") {
    $RemoteUrl = git remote get-url origin 2>$null
    if ($LASTEXITCODE -ne 0 -or $RemoteUrl -eq "") {
        Write-Output "Could not detect remote URL. Please pass it with -RemoteUrl."
        exit 1
    }
    Write-Output "Detected remote URL: $RemoteUrl"
}

# --- Resolve output dir to absolute path ---
$resolved = Resolve-Path -Path $OutputDir -ErrorAction SilentlyContinue
if (-not $resolved) {
    Write-Output "Output directory does not exist: $OutputDir"
    exit 1
}
$OutputDir = $resolved.Path
if (-not $OutputDir) {
    Write-Output "Output directory does not exist: $OutputDir"
    exit 1
}

# --- Generate zip name from timestamp if not provided ---
if ($ZipName -eq "") {
    $timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
    $ZipName = "exercises_$timestamp.zip"
}
if (-not $ZipName.EndsWith(".zip")) {
    $ZipName = "$ZipName.zip"
}
$zipPath = Join-Path $OutputDir $ZipName

# --- Temp working directory ---
$tempRoot = Join-Path $env:TEMP "gsd_export_$(Get-Random)"
New-Item -ItemType Directory -Path $tempRoot | Out-Null
Write-Output "Working in temp folder: $tempRoot"

try {
    # --- Discover gsd-* branches from remote ---
    Write-Output "Fetching branch list from remote..."
    $remoteBranches = git ls-remote --heads $RemoteUrl "refs/heads/gsd-*" |
            ForEach-Object { ($_ -split "\s+")[1] -replace "refs/heads/", "" } |
            Where-Object { $_ -ne "" } |
            Sort-Object { [int]($_ -replace "gsd-", "") }

    if ($remoteBranches.Count -eq 0) {
        Write-Output "No gsd-* branches found on remote."
        exit 1
    }

    $allBranches = @($BaseBranch) + $remoteBranches
    Write-Output "Found $($remoteBranches.Count) gsd-* branch(es) + '$BaseBranch'. Processing $($allBranches.Count) total."

    # --- Clone each branch into its own subfolder ---
    foreach ($branch in $allBranches) {
        Write-Output ""
        Write-Output "Cloning '$branch'..."
        $branchDir = Join-Path $tempRoot $branch

        git clone --branch $branch --single-branch --depth 1 $RemoteUrl $branchDir 2>&1 | Out-Null
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to clone branch '$branch' from $RemoteUrl"
        }

        # Remove .git folder
        $gitDir = Join-Path $branchDir ".git"
        if (Test-Path $gitDir) {
            Remove-Item -Recurse -Force $gitDir
        }

        Write-Output "  Cloned '$branch' -> $branch\"
    }

    # --- Zip the whole temp folder ---
    Write-Output ""
    Write-Output "Creating zip: $zipPath"
    Compress-Archive -Path "$tempRoot\*" -DestinationPath $zipPath -Force
    if (-not (Test-Path $zipPath)) {
        throw "Zip file was not created at $zipPath"
    }

    $sizeMB = [math]::Round((Get-Item $zipPath).Length / 1MB, 2)
    Write-Output "Done! Zip created: $zipPath ($sizeMB MB)"
    Write-Output ""
    Write-Output "Contents:"
    $allBranches | ForEach-Object { Write-Output "  $_\" }
}
catch {
    Write-Output ""
    Write-Output "ERROR: $_"
    exit 1
}
finally {
    # Always clean up temp folder
    if (Test-Path $tempRoot) {
        Remove-Item -Recurse -Force $tempRoot
        Write-Output ""
        Write-Output "Cleaned up temp folder."
    }
}
