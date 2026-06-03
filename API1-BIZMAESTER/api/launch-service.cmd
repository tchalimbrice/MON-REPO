@echo off
setlocal

if "%~2"=="" (
  echo Usage: launch-service.cmd ^<argfile^> ^<main-class^>
  exit /b 1
)

set "JAVA_EXE=C:\Program Files\Java\jdk-21\bin\java.exe"
set "ARGFILE=%~1"
set "MAIN_CLASS=%~2"

start "" /b "%JAVA_EXE%" "@%ARGFILE%" "%MAIN_CLASS%"

endlocal
