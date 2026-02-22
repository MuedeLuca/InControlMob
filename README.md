# InControlMob

**InControlMob** is a powerful, rule-driven mob control plugin for Minecraft servers running Paper 1.21. Inspired by the "In Control!" mod, it allows server admins to fine-tune mob spawns, attributes, loot, and experience using flexible JSON rules.

This fork intends to improve upon [the Original](https://github.com/mahadimdinfo-rgb/InControlMob) by providing further customization options as well as adding clearer documentation.
For convenience, I'll try to keep the logic as close to the Original as possible.

This fork has been created mainly for private usage, and I cannot guarantee to keep it up-to-date. However, anybody is free to use it as they like and improve upon it.

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

Files for adding rules are located in `plugins/InControlMob/*.json`.

Currently, the following files exist:
`experience.json`
`loot.json`
`spawn.json`
`spawner.json`
`summon.json`

### Logic

The plugin follows this order when changing the way entities are spawned:
1. check conditions 
2. check allow/deny
3. replace entity
4. apply actions

Due to this, please be aware of the logic during use. Any applied actions currently do **not** carry over if the entity is replaced.  Applying actions in the same rule as replacing an entity would, theoretically, apply those actions to the entity that is being replaced. Therefore, to apply effects to the new entity, you'll currently need two seperate rules.

**Please be aware that as of this version, recursive replacement of a unit (e.g. spawning two zombies instead of one) is possible, as no safeguard has been built in. This is true for both direct recursive rules (a zombie being replaced by two zombies) as well as recursive chains (e.g. 2 zombies replacing a creeper, two creepers replacing a zombie). You have been warned.** 

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

**v1.1.0**
- Forked Version "InControlMob-MuedeLuca"
- added extended entity replacement options

**v1.0.0**
- Initial release.
- Support for Spawn, Spawner, Summon, Loot, and Experience rules.
- JSON rule system with hot reload.
- WorldGuard soft dependency support.
