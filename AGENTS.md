# AGENTS

Guidelines for AI coding agents working on this repository.

## Project overview

**Lock Screen** is a minimal Android application (Kotlin, no third-party libraries) that locks
the device screen via the Accessibility Services API when the user taps the launcher icon.

## Key facts

- **Language**: Kotlin (JVM target 17)
- **Build system**: Gradle 8.9 + Android Gradle Plugin 8.7.3
- **Min SDK**: 28 (Android 9) — required for `GLOBAL_ACTION_LOCK_SCREEN`
- **Target / Compile SDK**: 36 (Android 16)
- **No external dependencies** — only Android framework APIs are used
- **No UI** — `LockActivity` uses a fully transparent theme; no layouts exist

## Architecture

```
LockActivity  ──(checks enabled?)──► LockScreenService.instance.lock()
     │                                        │
     └──(not enabled)──► AlertDialog          └──► performGlobalAction(LOCK_SCREEN)
                          └──► Accessibility Settings
```

The static `LockScreenService.instance` field is the bridge between the Activity and the
running service. It is set in `onServiceConnected()` and cleared in `onDestroy()`.

## Build & test commands

```bash
# Generate Gradle wrapper (first clone only – jar not committed)
gradle wrapper --gradle-version 8.9

# Assemble debug APK
./gradlew assembleDebug

# Assemble release APK (minified with R8)
./gradlew assembleRelease

# Lint
./gradlew lint
```

## File map

| File | Purpose |
|------|---------|
| `app/src/main/AndroidManifest.xml` | Declares Activity, Service, and permissions |
| `app/src/main/java/…/LockActivity.kt` | Launcher Activity – checks service state, locks or shows dialog |
| `app/src/main/java/…/LockScreenService.kt` | AccessibilityService – performs `GLOBAL_ACTION_LOCK_SCREEN` |
| `app/src/main/res/xml/accessibility_service_config.xml` | Service metadata (event types = 0) |
| `app/src/main/res/values/strings.xml` | All user-facing strings |
| `app/src/main/res/values/themes.xml` | Transparent Activity theme |
| `app/src/main/res/drawable/ic_launcher_*.xml` | Vector launcher icon assets |
| `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` | Adaptive icon definition |
| `app/proguard-rules.pro` | Keeps Service and Activity classes from R8 shrinking |

## Conventions

- Keep the app dependency-free; do not add AppCompat, Material, or other libraries.
- All user-visible strings must go in `strings.xml` (no hardcoded strings in Kotlin).
- The accessibility service must keep `accessibilityEventTypes="0"` to preserve the
  privacy guarantee stated in the README.
- Do not add Activities with layouts; the app intentionally has no visible UI.

## Sensitive areas

- `LockScreenService.instance` is a mutable static field — be careful with thread safety
  if you ever add background threading.
- `GLOBAL_ACTION_LOCK_SCREEN` requires the service to be connected; always null-check
  `instance` before calling `lock()`.
