# Building and Running the Fortune Cookie Android App

## Prerequisites

- Android Studio (recommended) or Android SDK command-line tools
- Java Development Kit (JDK) 8 or higher
- Android SDK with API level 24 or higher

## Building the App

### Using Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the fortune-cookie-android directory and select it
4. Wait for Gradle sync to complete
5. Click the "Run" button (green play icon) or use Run > Run 'app'

### Using Command Line

1. Navigate to the project directory:
   ```bash
   cd fortune-cookie-android
   ```

2. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
   
   The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

3. Install on a connected device or emulator:
   ```bash
   ./gradlew installDebug
   ```

## Running the App

### On an Emulator

1. In Android Studio, go to Tools > Device Manager
2. Create a new virtual device (if needed) with API 24 or higher
3. Start the emulator
4. Click "Run" in Android Studio

### On a Physical Device

1. Enable Developer Options on your Android device:
   - Go to Settings > About Phone
   - Tap "Build Number" 7 times
   
2. Enable USB Debugging:
   - Go to Settings > Developer Options
   - Enable "USB Debugging"

3. Connect your device via USB

4. Run the app from Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

## Application Features

When you launch the app:

1. **Loading Phase**: The app will fetch fortune cookie phrases from a remote URL
2. **Interactive Cookie**: Tap the fortune cookie image to trigger the breaking animation
3. **Fortune Display**: After the breaking animation, your fortune message appears
4. **Reset**: Press the "Reset" button to restore the cookie and get a new fortune

## Troubleshooting

### Gradle Sync Issues

If you encounter Gradle sync problems:
- Ensure you have a stable internet connection
- Try File > Invalidate Caches / Restart in Android Studio
- Update Gradle wrapper if needed

### Network Permissions

The app requires internet access to load fortunes. If fortunes fail to load:
- Check your device's internet connection
- The app will fallback to built-in sample fortunes if the URL is unreachable

### Build Errors

If you see compilation errors:
- Ensure your Android SDK is up to date
- Check that you have API level 34 installed
- Try cleaning and rebuilding: `./gradlew clean build`

## Customization

To change the fortune source URL, edit the `FORTUNE_URL` constant in `MainActivity.kt`:

```kotlin
companion object {
    private const val FORTUNE_URL = "your-custom-url-here"
}
```

The URL should return text with fortunes separated by the `%` character.
