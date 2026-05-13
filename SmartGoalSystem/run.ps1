# ====================================================================

# Auto-kill any existing Java/Tomcat processes to free up port 8080
Get-Process -Name java -ErrorAction SilentlyContinue | Stop-Process -Force

$ProjectDir = "d:\SVAC MAIN 2\SmartGoalSystem"
$ServerDir = "$ProjectDir\server"
$TomcatVersion = "9.0.84"
$TomcatUrl = "https://archive.apache.org/dist/tomcat/tomcat-9/v$TomcatVersion/bin/apache-tomcat-$TomcatVersion-windows-x64.zip"
$ZipFile = "$ServerDir\tomcat.zip"
$TomcatExtractDir = "$ServerDir\apache-tomcat-$TomcatVersion"

# 1. Prepare Server Directory
if (!(Test-Path $ServerDir)) {
    New-Item -ItemType Directory -Force -Path $ServerDir | Out-Null
}

# 2. Download Tomcat if not present
if (!(Test-Path $TomcatExtractDir)) {
    Write-Host "Downloading Apache Tomcat $TomcatVersion (This might take a minute)..." -ForegroundColor Cyan
    Invoke-WebRequest -Uri $TomcatUrl -OutFile $ZipFile
    
    Write-Host "Extracting Tomcat..." -ForegroundColor Cyan
    Expand-Archive -Path $ZipFile -DestinationPath $ServerDir -Force
    Remove-Item $ZipFile -Force
}

# 3. Create Classes Directory for Compiled Java Files
$ClassesDir = "$ProjectDir\webapp\WEB-INF\classes"
if (!(Test-Path $ClassesDir)) {
    New-Item -ItemType Directory -Force -Path $ClassesDir | Out-Null
}

# Compile Java Code
Write-Host "Compiling Java Files..." -ForegroundColor Cyan
$ServletApi = "$TomcatExtractDir\lib\servlet-api.jar"
$Ojdbc = Get-ChildItem -Path "$ProjectDir\webapp\WEB-INF\lib" -Filter ojdbc*.jar | Select-Object -First 1 -ExpandProperty FullName

# Find all Java files and wrap them in quotes to handle spaces in folder names
$JavaFiles = Get-ChildItem -Path "$ProjectDir\src" -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
$JavaFilesList = ($JavaFiles | ForEach-Object { "`"$_`"" }) -join " "

# Run javac
$Classpath = "$ServletApi;$Ojdbc"
$JavacCommand = "javac -cp `"$Classpath`" -d `"$ClassesDir`" $JavaFilesList"

Invoke-Expression $JavacCommand

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation Failed! Please check your Java code." -ForegroundColor Red
    pause
    exit
}

Write-Host "Compilation Successful!" -ForegroundColor Green

# 5. Deploy to Tomcat
Write-Host "Deploying to Tomcat..." -ForegroundColor Cyan
$WebappsDir = "$TomcatExtractDir\webapps\SmartGoalSystem"

# Remove old deployment if exists
if (Test-Path $WebappsDir) {
    Remove-Item -Recurse -Force $WebappsDir
}

# Copy webapp folder to Tomcat webapps
Copy-Item -Path "$ProjectDir\webapp" -Destination $WebappsDir -Recurse -Force

# 6. Start the Server
Write-Host "Starting Apache Tomcat Server..." -ForegroundColor Green

# Tomcat requires JAVA_HOME or JRE_HOME. We will find it automatically and set it.
$JavaInfo = (java -XshowSettings:properties -version 2>&1) -match "java.home"
if ($JavaInfo) {
    $JavaHomePath = ($JavaInfo -split "=")[1].Trim()
    $SetEnvFile = "$TomcatExtractDir\bin\setenv.bat"
    Set-Content -Path $SetEnvFile -Value "set `"JRE_HOME=$JavaHomePath`"`r`nset `"CATALINA_HOME=$TomcatExtractDir`""
    Write-Host "Set JRE_HOME to $JavaHomePath" -ForegroundColor DarkCyan
}

$StartupScript = "$TomcatExtractDir\bin\startup.bat"
Start-Process -FilePath $StartupScript -WorkingDirectory "$TomcatExtractDir\bin"

Write-Host "=========================================================" -ForegroundColor Yellow
Write-Host "Server is starting up in a new window!" -ForegroundColor Yellow
Write-Host "Wait 10 seconds, opening your browser automatically..." -ForegroundColor Yellow
Start-Sleep -Seconds 10
Start-Process "http://localhost:8080/SmartGoalSystem/login.html"
Write-Host "=========================================================" -ForegroundColor Yellow

pause
