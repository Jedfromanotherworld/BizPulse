@echo off
setlocal
cd /d "%~dp0"
git --version >nul 2>&1
if errorlevel 1 (
  echo Install Git for Windows, then restart Android Studio and try again.
  pause
  exit /b 1
)
if not exist ".git" (
  git init -b main
  if errorlevel 1 goto failed
)
set "BIZPULSE_ORIGIN="
for /f "delims=" %%U in ('git remote get-url origin 2^>nul') do set "BIZPULSE_ORIGIN=%%U"
if defined BIZPULSE_ORIGIN (
  if not "%BIZPULSE_ORIGIN%"=="https://github.com/Jedfromanotherworld/BizPulse.git" (
    echo A different origin is configured. No changes made to it.
    git remote -v
    pause
    exit /b 1
  )
) else (
  git remote add origin https://github.com/Jedfromanotherworld/BizPulse.git
  if errorlevel 1 goto failed
)
git remote -v
echo Local connection ready. Follow START-HERE-GITHUB.md to commit and push.
pause
exit /b 0
:failed
echo Git setup failed. Copy the error above for troubleshooting.
pause
exit /b 1
