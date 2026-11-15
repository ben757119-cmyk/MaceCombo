# Mace Combo

Fabric mod for **Minecraft 26.2**.

Detects the combo **axe shield-disable → mace smash within 3 seconds** and shows a customizable overlay + chat message.

**INS** key opens the ClickGUI.

## Features
- Client-side detection of shield disable (axe on blocking target) then mace smash (fallDistance ≥ 1.5)
- Configurable chat message & HUD overlay
- ClickGUI for live config (enabled, overlay, chat, texts, duration, scale, window)
- Config saved to `config/macecombo.json`

## Build
```bash
./gradlew build
```
Requires Java 25 + Fabric Loom.

GitHub Actions builds the jar on every push.

## License
MIT
