# Fortune Cookie Android

An interactive Android application that displays fortune cookie messages with animated breaking effects.

## Features

- **Load Fortunes from URL**: Fetches fortune cookie phrases from a remote URL
- **Interactive Cookie**: Tap the fortune cookie image to trigger the breaking animation
- **Breaking Animation**: Smooth animation showing the cookie breaking apart
- **Fortune Display**: After breaking, the fortune message appears with a fade-in animation
- **Reset Function**: Reset button to restore the cookie to its unbroken state and show the next fortune

## How to Use

1. Launch the app
2. Wait for fortunes to load from the URL
3. Tap the fortune cookie image to break it
4. Read your fortune message
5. Press the "Reset" button to get another fortune

## Technical Details

- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: Single Activity with coroutines for network operations
- **UI**: XML layouts with ConstraintLayout and custom vector drawables
- **Animations**: XML-based view animations for smooth transitions

## Building the Project

This project uses Gradle. To build:

```bash
./gradlew build
```

To run on an emulator or device:

```bash
./gradlew installDebug
```

## Fortune Source

By default, the app loads fortunes from:
`https://raw.githubusercontent.com/bmc/fortunes/master/fortunes`

If the URL fails to load, the app falls back to built-in sample fortunes.
