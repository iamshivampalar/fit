# NeckFit – Posture & Neck Exercise Android App

NeckFit is a simple offline Android app for safe neck posture and shoulder posture exercises.

Important disclaimer: This app is for posture improvement and fitness guidance only. It does not increase neck bone length and it is not medical treatment.

## Features

- Home dashboard
- Exercise library
- Workout timer
- Progress tracking using SharedPreferences
- Daily reminder notification
- Safety guidance
- Mobile-first native Android app
- Indian theme colors: saffron, white, green, navy
- GitHub Actions workflow that builds a debug APK automatically

## Exercises Included

- Chin Tuck
- Shoulder Blade Squeeze
- Neck Side Stretch
- Neck Flexion Stretch
- Wall Posture Hold
- Chest Opener Stretch
- Phone Posture Practice

## How to build APK using GitHub Actions

1. Create a new GitHub repository.
2. Upload all files from this project to the repository.
3. Push to `main` or `master` branch.
4. Open GitHub → your repository → Actions tab.
5. Open the latest workflow run named `Build Android APK`.
6. Download the artifact named `NeckFit-debug-apk`.
7. Inside it you will find the debug APK.

## How to build locally

Open this folder in Android Studio, let Gradle sync, then choose:

Build → Build Bundle(s) / APK(s) → Build APK(s)

Or run:

```bash
gradle :app:assembleDebug
```

## Project structure

```text
.github/workflows/android-build.yml
settings.gradle
build.gradle
app/build.gradle
app/src/main/AndroidManifest.xml
app/src/main/java/com/shivam/neckfit/MainActivity.java
app/src/main/java/com/shivam/neckfit/ReminderReceiver.java
app/src/main/res/values/colors.xml
app/src/main/res/values/strings.xml
app/src/main/res/values/styles.xml
```

## Notes

This first version uses native Java and SharedPreferences to keep the project lightweight and GitHub-build friendly. Later you can upgrade it to Kotlin, Room database, Jetpack Compose, login system, or Firebase.
