# InControlMob

**InControlMob** is a powerful, rule-driven mob control plugin for Minecraft servers running Paper 1.21. Inspired by the "In Control!" mod, it allows server admins to fine-tune mob spawns, attributes, loot, and experience using flexible JSON rules.

This fork of [the original](https://github.com/mahadimdinfo-rgb/InControlMob) intends to improve upon the Original by providing further customization options as well as adding clearer documentation.
For convenience, I'll try to keep the logic as close to the Original as possible.

This fork has been created mainly for private usage, but for the easons of ease of use, as well as for documentation, I've decided for it to be public.

## Features

- **Rule Engine**: Control spawns, health, equipment, potion effects, and more based on detailed conditions (Biome, Time, Light Level, WorldGuard Region, etc.).
- **Loot Control**: Modify, replace, or clear drops and experience for any mob death.
- **Performance**: Asynchronous rule loading, efficient caching, and optimized event handling.
- **Hot Reload**: Reload rules instantly with `/incontrolmob reload` without restarting the server.
- **WorldGuard Support**: Optional integration for region-specific rules.

## Installation

1. Stop your server.
2. Place `InControlMob.jar` in your `plugins` folder.
3. Start the server.
4. The plugin will convert default rules to `plugins/InControlMob/` (spawn.json, loot.json, etc.).

## Commands & Permissions

| Command | Permission | Description |
|Args|---|---|
| `/incontrolmob reload` | `incontrolmob.reload` | Reloads all rule files and config. |
| `/incontrolmob list` | `incontrolmob.manage` | Lists all loaded rules and their status. |
| `/incontrolmob test <entity>` | `incontrolmob.test` | Simulates rule evaluation for a mob at your location. |

**Default Permission**: `incontrolmob.manage` (OP default)

## Configuration Rules

Rules are located in `plugins/InControlMob/*.json`.

### Example: Deny Pigs Globally
File: `spawn.json`
```json
[
  {
    "id": "deny_pigs",
    "priority": 100,
    "conditions": {
      "entityType": "PIG"
    },
    "actions": {
      "allow": false,
      "message": "Pig spawn blocked"
    }
  }
]
```

### Example: Strong Zombies in Plains at Night
File: `spawn.json`
```json
[
  {
    "id": "strong_zombies",
    "priority": 50,
    "conditions": {
      "entityType": "ZOMBIE",
      "biome": "PLAINS",
      "timeOfDay": { "from": 13000, "to": 23000 }
    },
    "actions": {
      "setMaxHealth": 40.0,
      "setHealth": 40.0,
      "addPotionEffects": [
        { "type": "SPEED", "duration": 600, "amplifier": 1 }
      ],
      "setEquipment": {
        "mainHand": { "item": "minecraft:iron_sword" }
      }
    }
  }
]
```

## Troubleshooting

- **Rules not applying?** Check the console for JSON syntax errors on startup or reload.
- **WorldGuard not working?** Ensure you have WorldGuard 7.0+ installed. The plugin works without it but region conditions will be ignored.
- **Debug Mode**: Enable `debug: true` in `config.yml` to see detailed matching logs (Warning: Spammy).

## Changelog

**v1.0.0**
- Initial release.
- Support for Spawn, Spawner, Summon, Loot, and Experience rules.
- JSON rule system with hot reload.
- WorldGuard soft dependency support.
