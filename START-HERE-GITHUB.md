# BizPulse — GitHub setup

Repository: https://github.com/Jedfromanotherworld/BizPulse

This ZIP includes a local `.git` folder with `origin` configured to your repository and the initial branch named `main`. No commit or push has been made. Your GitHub repository was empty when this package was prepared. Application source and APKs are unchanged from the previously verified build.

## 1. Extract and open

Extract the entire ZIP to a NEW folder. Do not copy it over another Git working copy or delete its hidden `.git` folder. Open the extracted BizPulse folder (the one containing `settings.gradle.kts`) in Android Studio. Follow README.md for JDK 17 and SDK 34 setup.

Install Git for Windows from https://git-scm.com/downloads/win if needed, then restart Android Studio. Open Android Studio's Terminal and check:

```bash
git status
git remote -v
```

Both origin entries should show `https://github.com/Jedfromanotherworld/BizPulse.git`. No commits yet is normal. If Git reports that this is not a repository, double-click CONNECT-GITHUB.bat on Windows (or run `sh CONNECT-GITHUB.sh` on macOS/Linux), then check again.

## 2. First upload

Replace YOUR-GITHUB-EMAIL with an email associated with your GitHub account, or the exact private noreply email shown in GitHub Settings > Emails. The email is a commit-author field, not a password.

```bash
git config user.email "YOUR-GITHUB-EMAIL"
git add .
git status
git commit -m "Initial BizPulse Android project"
git push -u origin main
```

Complete the browser sign-in if Git opens it. If prompted for an HTTPS password in the terminal, use a GitHub personal access token, not your account password. Never put a token in the repository URL or a source file. The included GitHub Actions workflow may require workflow permission if you authenticate with a token.

Refresh your GitHub repository page after the push succeeds. APK files and ZIPs are excluded from Git by .gitignore, but the APKs are still included in this download. Gradle builds, local.properties, and Android Studio machine settings are also excluded.

## 3. Daily git pull and git push

Before editing, with a clean working tree:

```bash
git pull --ff-only
```

After editing:

```bash
git add .
git commit -m "Describe your changes"
git pull --rebase
git push
```

If rebase reports a conflict, stop and resolve the named files before continuing. Use `git rebase --abort` to return to the state before that rebase if needed. Do not force-push to fix a rejected upload.

## If the remote now contains commits

If someone has pushed to GitHub since this ZIP was prepared, the initial push may be rejected. Preserve that history: clone your repository into a separate folder, copy the project files into the clone WITHOUT copying this ZIP's `.git` folder, review overlapping files, then commit and push from the clone. Do not delete the remote's existing history.

## Included helper scripts

CONNECT-GITHUB.bat and CONNECT-GITHUB.sh only initialize missing local Git metadata and verify/configure the origin URL. They do not authenticate, commit, push, pull, or replace another configured origin. They are recovery helpers; the ZIP is already configured.

## Build evidence

The original app build and 15 passing unit tests are documented in BUILD-VERIFICATION.md. This edition changes Git setup and documentation only; all app source and APK bytes match the original archive. Device testing remains pending.
