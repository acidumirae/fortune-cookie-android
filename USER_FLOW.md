# Fortune Cookie App - User Flow

## Visual Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│  Step 1: APP LAUNCH                                                 │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────────────────────────────────────────────────┐      │
│  │                                                            │      │
│  │               🔄 Loading fortunes...                      │      │
│  │                                                            │      │
│  │           (ProgressBar + "Loading fortunes...")           │      │
│  │                                                            │      │
│  └──────────────────────────────────────────────────────────┘      │
│                                                                      │
│  • Fetches fortunes from URL asynchronously                         │
│  • Shows loading indicator                                          │
│  • Falls back to sample fortunes if network fails                   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

                              ⬇️

┌─────────────────────────────────────────────────────────────────────┐
│  Step 2: READY STATE                                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────────────────────────────────────────────────┐      │
│  │                                                            │      │
│  │        "Tap the cookie to reveal your fortune!"           │      │
│  │                                                            │      │
│  │                        🥠                                  │      │
│  │                  (Whole Cookie)                            │      │
│  │                   [Clickable]                              │      │
│  │                                                            │      │
│  └──────────────────────────────────────────────────────────┘      │
│                                                                      │
│  • Displays whole fortune cookie                                    │
│  • Instruction text visible                                         │
│  • Cookie is tappable                                               │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

                              ⬇️
                        (User taps cookie)

┌─────────────────────────────────────────────────────────────────────┐
│  Step 3: BREAKING ANIMATION (700ms)                                 │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────────────────────────────────────────────────┐      │
│  │                                                            │      │
│  │                        🥠                                  │      │
│  │                    (Animating)                             │      │
│  │                                                            │      │
│  │    0-200ms:  Scale up to 1.1x                             │      │
│  │  200-500ms:  Scale down to 0.95x                          │      │
│  │  500-700ms:  Fade out                                     │      │
│  │                                                            │      │
│  └──────────────────────────────────────────────────────────┘      │
│                                                                      │
│  • Cookie expands, compresses, then fades out                       │
│  • Smooth multi-stage animation                                     │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

                              ⬇️

┌─────────────────────────────────────────────────────────────────────┐
│  Step 4: BROKEN STATE                                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────────────────────────────────────────────────┐      │
│  │                                                            │      │
│  │                    🍪   📜   🍪                            │      │
│  │                 (Broken Cookie)                            │      │
│  │                                                            │      │
│  │      "A beautiful, smart, and loving person will          │      │
│  │           be coming into your life."                      │      │
│  │                                                            │      │
│  │                   [ Reset ]                                │      │
│  │                                                            │      │
│  └──────────────────────────────────────────────────────────┘      │
│                                                                      │
│  • Cookie image changes to broken pieces                            │
│  • Fortune text fades in (600ms animation)                          │
│  • Reset button appears                                             │
│  • Instruction text hidden                                          │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

                              ⬇️
                      (User taps "Reset")

┌─────────────────────────────────────────────────────────────────────┐
│  Step 5: RESET ANIMATION (300ms)                                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────────────────────────────────────────────────┐      │
│  │                                                            │      │
│  │                        🥠                                  │      │
│  │                    (Animating)                             │      │
│  │                                                            │      │
│  │    Fade in + Scale from 0.5x to 1.0x                      │      │
│  │                                                            │      │
│  └──────────────────────────────────────────────────────────┘      │
│                                                                      │
│  • Whole cookie appears with smooth animation                       │
│  • Fortune text hidden                                              │
│  • Reset button hidden                                              │
│  • Next fortune loaded                                              │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

                              ⬇️
                        (Returns to Step 2)
```

## State Diagram

```
        START
          │
          ▼
    ┌──────────┐
    │ LOADING  │
    └──────────┘
          │
          ▼
    ┌──────────┐         Tap Cookie         ┌────────────┐
    │  READY   │ ───────────────────────────▶│  BREAKING  │
    └──────────┘                             └────────────┘
          ▲                                        │
          │                                        ▼
          │                                  ┌──────────┐
          │         Tap Reset                │  BROKEN  │
          └──────────────────────────────────└──────────┘
```

## Animation Timeline

```
Cookie Breaking (Tap → Broken):
0ms    ├─────┤ Scale Up (1.0 → 1.1)
200ms  ├──────────┤ Scale Down (1.1 → 0.95)
500ms  ├─────┤ Fade Out
700ms  END
       │
       ▼ Switch to broken cookie image
       │
       ▼ Show fortune text
       ├──────────────────┤ Fade In + Scale Up
       600ms

Cookie Reset (Reset → Ready):
0ms    ├──────────┤ Fade In + Scale (0.5 → 1.0)
300ms  END
```

## UI Element Visibility States

```
┌──────────────────┬──────────┬──────────┬──────────┬──────────┐
│ Element          │ LOADING  │  READY   │ BREAKING │  BROKEN  │
├──────────────────┼──────────┼──────────┼──────────┼──────────┤
│ Loading Progress │ VISIBLE  │  GONE    │  GONE    │  GONE    │
│ Loading Text     │ VISIBLE  │  GONE    │  GONE    │  GONE    │
│ Cookie Image     │  GONE    │ VISIBLE  │ VISIBLE  │ VISIBLE  │
│ Instruction Text │  GONE    │ VISIBLE  │ VISIBLE  │  GONE    │
│ Fortune Text     │  GONE    │  GONE    │  GONE    │ VISIBLE  │
│ Reset Button     │  GONE    │  GONE    │  GONE    │ VISIBLE  │
└──────────────────┴──────────┴──────────┴──────────┴──────────┘

Cookie Image States:
- READY:    fortune_cookie_whole.xml
- BREAKING: fortune_cookie_whole.xml (animating)
- BROKEN:   fortune_cookie_broken.xml
```

## Interaction Points

1. **Cookie Image (READY state)**
   - Action: Tap
   - Result: Triggers breaking animation → Shows fortune

2. **Reset Button (BROKEN state)**
   - Action: Tap
   - Result: Hides fortune → Shows whole cookie → Next fortune queued

## Data Flow

```
App Start
    │
    ▼
Load Fortunes (Async)
    │
    ├─ Success → fortunes = List<String> (shuffled)
    │
    └─ Failure → fortunes = Sample Fortunes
    │
    ▼
Display Cookie
    │
    ▼
User Taps
    │
    ▼
Show Fortune #0
    │
    ▼
User Resets
    │
    ▼
Show Fortune #1
    │
    ▼
... (cycles through all fortunes)
```

## Technical Notes

- **Coroutine Scope**: Uses `lifecycleScope` for automatic cleanup
- **Resource Management**: BufferedReader uses `.use{}` for proper cleanup
- **Error Handling**: Network failures fall back to built-in fortunes
- **Memory Safety**: No memory leaks with lifecycle-aware coroutines
- **UI Thread Safety**: All UI updates on Main dispatcher

---

This visual guide shows the complete user interaction flow from app launch to fortune display and reset.
