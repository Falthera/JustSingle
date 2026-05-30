# JustSingle

JustSingle is a production-oriented client-side Fabric mod for Minecraft Java Edition 1.21.11.
Developed by Falthera.

It emulates authentic hardware-style mouse double-click behavior with no user controls.

## Design Goals

- No configuration
- No commands
- No keybinds
- No GUI
- No toggles
- Always on while installed
- Client-side only
- Lightweight and low overhead

## Technical Overview

JustSingle injects directly into Minecraft's mouse button pipeline at `Mouse.onMouseButton` and only reacts to native press/release events.

For qualifying native press events, it emulates a hardware bounce by issuing:

1. Synthetic release
2. Sub-millisecond jitter gap
3. Synthetic press

This produces an additional click edge while preserving natural button state progression and avoiding CPS-script style loops.

### Architecture

- `JustSingleMod`:
  - Mod identity and logger.
- `JustSingleClient`:
  - Client bootstrap and lifecycle wiring.
- `MouseInputInterceptor`:
  - Raw mouse event handling and context gating.
- `DoubleClickEmulator`:
  - Synthetic event generation and recursion guard.
- `ClickTimingModel`:
  - Realistic probability, cooldown, and jitter model.
- `ButtonStateTracker`:
  - Per-button press/release history and reset logic.
- `MouseMixin` and `MouseInvoker`:
  - Injection and synthetic dispatch bridge.

## Build Requirements

- JDK 21
- Gradle (or Gradle wrapper if generated locally)

## GitHub Auto Release

This repository includes a one-click GitHub Actions release workflow at `.github/workflows/release.yml`.

When triggered, it will:

1. Compile the mod jar.
2. Create and push the requested git tag (if missing).
3. Create a GitHub Release for that tag.
4. Upload built jars from `build/libs` to the release.

### How to run

1. Open Actions in GitHub.
2. Run `Build Tag Release`.
3. Enter a tag like `v1.0.0`.
4. Click `Run workflow`.

## Build Instructions

From the project root:

```powershell
gradle build
```

Output jar:

- `build/libs/justsingle-<version>.jar`

## Installation

1. Install Fabric Loader for Minecraft 1.21.11.
2. Build the mod jar.
3. Place the jar in your client `mods` folder.
4. Launch Minecraft with the Fabric profile.

No additional setup is required.

## Verification Checklist

- Combat testing:
  - Repeated left-click exchanges in close combat, verify natural feel and no visible artificial cadence.
- Block placing testing:
  - Fast right-click placement patterns, verify no sticky behavior.
- Block breaking testing:
  - Rapid mining and tap-breaking transitions, verify responsiveness.
- Inventory testing:
  - Click, shift-click, drag-split, and collect patterns in inventory and containers.
- GUI testing:
  - Chest, crafting, and creative inventory interactions where click semantics matter.
- Multiplayer testing:
  - Join vanilla/Fabric-compatible servers and verify client-only operation.
- Long-session stability testing:
  - 60+ minute mixed session with alt-tab transitions and FPS changes.

## Notes

- JustSingle is intentionally always enabled.
- This mod does not implement an autoclicker and does not contain CPS boosting logic.
