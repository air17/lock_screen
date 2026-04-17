# Lock Screen

A minimal Android application (targeting Android 16 / API 36) that locks the device screen with a single tap on its launcher icon.

## How it works

The app uses Android's **Accessibility Services** API.  
When the service is enabled, tapping the app icon calls  
`AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN` — the screen locks instantly with no visible UI.

## First-time setup

1. Install the APK.
2. Tap the **Lock Screen** icon.
3. A dialog will appear — tap **Open Settings**.
4. In **Settings → Accessibility**, find **Lock Screen** and toggle it **on**.
5. Return to the home screen. Tapping the icon now locks the screen immediately.

## Requirements

| Item | Value |
|------|-------|
| Minimum Android version | Android 9 (API 28) |
| Target Android version | Android 16 (API 36) |
| Required setup | Enable the included Accessibility Service once |

> **Why Accessibility?**  
> Since Android 9 (API 28), only Device Policy / accessibility services can lock the screen
> programmatically. This app uses the least-privileged path: no device-admin enrollment,
> no additional permissions beyond `BIND_ACCESSIBILITY_SERVICE` (which is granted
> automatically to the declared service).

## Building

### Prerequisites

- Android Studio (Ladybug 2024.2 or newer) **or** Gradle 8.9 + JDK 17
- Android SDK platform 36

### Build a debug APK

```bash
./gradlew assembleDebug
```

### Build a release APK (minified)

```bash
./gradlew assembleRelease
```

The release APK will be in `app/build/outputs/apk/release/`.

## Project structure

```
app/src/main/
├── AndroidManifest.xml                   – permissions & component declarations
├── java/com/lockscreen/app/
│   ├── LockActivity.kt                   – transparent launcher Activity
│   └── LockScreenService.kt              – AccessibilityService that locks the screen
└── res/
    ├── drawable/
    │   ├── ic_launcher_background.xml    – icon background (blue)
    │   └── ic_launcher_foreground.xml    – icon foreground (white padlock)
    ├── mipmap-anydpi-v26/
    │   └── ic_launcher.xml               – adaptive launcher icon
    ├── values/
    │   ├── strings.xml                   – all user-visible strings
    │   └── themes.xml                    – transparent Activity theme
    └── xml/
        └── accessibility_service_config.xml – service metadata
```

## Privacy

The accessibility service sets `android:accessibilityEventTypes="0"`, meaning it **does not
read, observe, or intercept any accessibility events** on the device. It only performs a
single global action (lock screen) when explicitly triggered by the launcher Activity.

## License

[MIT](LICENSE)
