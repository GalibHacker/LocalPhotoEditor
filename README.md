# Local Photo Editor

An Android 8+ (API 26) local-only photo editor MVP. It imports images through the system picker, applies exposure/contrast/saturation adjustments and rotation, and exports JPEGs to `Pictures/LocalPhotoEditor`.

There are intentionally no network permissions, account services, cloud sync, analytics, or billing dependencies.

## Build

Install Android Studio or a JDK plus Android SDK, then run:

```sh
./gradlew :app:assembleDebug
```

The current execution environment has no Java/Gradle toolchain, so compilation must be performed on an Android development machine. The project is otherwise self-contained and uses only Android platform APIs.
