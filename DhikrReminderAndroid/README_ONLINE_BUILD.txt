BUILD THE APK ONLINE (WITHOUT USING GRADLE ON YOUR PC)

1. Sign in to GitHub in your browser.
2. Create a NEW repository named DhikrReminderAndroid. Choose Public or Private; either is fine.
3. Upload the CONTENTS of this folder to the repository (including the hidden .github folder). Easiest: extract this ZIP, then use GitHub's Add file > Upload files. If the browser hides .github, create the workflow file on GitHub manually at .github/workflows/build-apk.yml and paste the contents of the supplied build-apk.yml.
4. Open the repository's Actions tab.
5. Choose "Build Dhikr Reminder APK" on the left, then click "Run workflow" if it has not started automatically.
6. Wait for the run to finish with a green check mark.
7. Open that completed run. At the bottom under Artifacts, download DhikrReminder-debug-apk.
8. Extract the downloaded artifact ZIP. It contains app-debug.apk. Transfer that APK to your Android phone and install it.

Note: This builds a debug APK; it is for personal installation/testing, not Play Store publishing. The build may still fail if the project itself has a compile/configuration issue. Test on your phone before relying on background speech. On first run, allow notifications and tap Start reminders. Ensure your Android text-to-speech engine has an Arabic voice installed. Battery settings may need to be set to Unrestricted.
