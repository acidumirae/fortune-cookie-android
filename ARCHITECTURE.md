# Fortune Cookie App - Architecture Overview

## Application Structure

The Fortune Cookie Android application is a single-activity app with the following components:

### Core Components

1. **MainActivity.kt** - Main activity that handles all app logic
2. **activity_main.xml** - Main layout with all UI elements
3. **Fortune Cookie Drawables** - Vector graphics for whole and broken cookies
4. **Animation Resources** - XML animations for cookie breaking and fortune appearing

## Data Flow

```
App Launch
    ↓
Load Fortunes from URL (async)
    ↓
Display Unbroken Cookie
    ↓
User Taps Cookie
    ↓
Play Breaking Animation
    ↓
Show Broken Cookie + Fortune Text
    ↓
User Taps Reset
    ↓
Play Appear Animation
    ↓
Display Unbroken Cookie (Next Fortune)
```

## Key Features Implementation

### 1. Loading Fortunes from URL

**Location**: `MainActivity.loadFortunes()`, `fetchFortunesFromUrl()`

- Uses Kotlin Coroutines with `Dispatchers.IO` for network operations
- Fetches from: `https://raw.githubusercontent.com/bmc/fortunes/master/fortunes`
- Parses fortunes separated by `%` character
- Falls back to 10 built-in sample fortunes if URL fails
- Shows loading indicator during fetch

### 2. Fortune Cookie Images

**Whole Cookie**: `drawable/fortune_cookie_whole.xml`
- Vector drawable showing an intact fortune cookie
- Golden yellow color (#F4D03F) with brown accents
- Crescent-shaped design

**Broken Cookie**: `drawable/fortune_cookie_broken.xml`
- Vector drawable showing cookie split in two pieces
- White fortune paper peeking out between pieces
- Same color scheme as whole cookie

### 3. Breaking Animation

**File**: `anim/cookie_break.xml`

Sequence:
1. Scale up to 1.1x (200ms) - cookie "expands" before breaking
2. Scale down to 0.95x (300ms) - compression effect
3. Fade out (200ms) - cookie disappears

Total duration: 700ms

**Implementation**: After animation completes, the image is switched from whole cookie to broken cookie drawable.

### 4. Fortune Display Animation

**File**: `anim/fortune_appear.xml`

Effects:
- Fade in from alpha 0.0 to 1.0 (600ms)
- Scale up from 0.8x to 1.0x (600ms)
- Creates a smooth "pop-in" effect for the fortune text

### 5. Reset Functionality

**File**: `MainActivity.resetCookie()`

Actions:
1. Hides fortune text (alpha = 0)
2. Hides reset button
3. Shows instruction text
4. Switches to whole cookie image
5. Plays appear animation on cookie
6. Advances to next fortune in the list

**Animation**: `anim/cookie_appear.xml`
- Fade in and scale up (300ms)
- Makes the cookie reappear smoothly

## UI Layout Structure

```
ConstraintLayout (fortune_background color)
├── instructionText (TextView)
│   └── "Tap the cookie to reveal your fortune!"
├── fortuneCookieImage (ImageView)
│   └── 300dp x 300dp, clickable
├── fortuneText (TextView)
│   └── Initially hidden (alpha=0)
├── resetButton (Button)
│   └── Initially hidden (visibility=gone)
├── loadingProgress (ProgressBar)
│   └── Shown during fortune loading
└── loadingText (TextView)
    └── "Loading fortunes..."
```

## State Management

The app maintains state through three variables:

- `fortunes: List<String>` - All loaded fortune messages
- `currentFortuneIndex: Int` - Index of current fortune to display
- `isCookieBroken: Boolean` - Whether cookie is currently broken

### State Transitions

**Initial State**:
- `isCookieBroken = false`
- `currentFortuneIndex = 0`
- Showing whole cookie

**After Breaking**:
- `isCookieBroken = true`
- Same index
- Showing broken cookie + fortune

**After Reset**:
- `isCookieBroken = false`
- `currentFortuneIndex++` (incremented, wraps around)
- Showing whole cookie

## Network Operations

Uses standard `HttpURLConnection` for simplicity:

```kotlin
val url = URL(urlString)
val connection = url.openConnection() as HttpURLConnection
connection.requestMethod = "GET"
connection.connectTimeout = 10000
connection.readTimeout = 10000
```

**Error Handling**:
- Network failures trigger fallback to sample fortunes
- Toast notification informs user of error
- App remains functional with built-in fortunes

## Permissions

**Required Permissions** (AndroidManifest.xml):
- `INTERNET` - To fetch fortunes from URL
- `ACCESS_NETWORK_STATE` - To check network availability

**Additional Settings**:
- `usesCleartextTraffic="true"` - Allows HTTP connections (some fortune URLs may use HTTP)

## Gradle Dependencies

Key dependencies:
- `androidx.core:core-ktx` - Kotlin Android extensions
- `androidx.appcompat:appcompat` - Compatibility library
- `com.google.android.material:material` - Material Design components
- `androidx.constraintlayout:constraintlayout` - Layout manager
- `kotlinx-coroutines-android` - Coroutines for async operations

## Build Configuration

- **Minimum SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 34 (Android 14)
- **Language**: Kotlin
- **Build Tool**: Gradle 8.0
- **Android Gradle Plugin**: 8.1.0
- **Kotlin Version**: 1.9.0

## Future Enhancements

Potential improvements:
- Add settings screen to customize fortune URL
- Implement local caching of fortunes
- Add sound effects for cookie breaking
- Support for multiple languages
- Share fortune feature (social media integration)
- Custom cookie themes/skins
- Favorites/bookmark system for fortunes
