package com.creativestylab.incontrolmob.rules;

import java.util.List;
import java.util.Map;

public class RuleActions {
    // Flow control
    public Boolean allow; // true/false
    public String message; // For debugging/feedback

    // Entity Attributes
    public Double maxHealth; // setMaxHealth support
    public Double health; // setHealth support
    public Boolean setNoGravity;
    public Boolean setSilent;
    public Boolean setInvulnerable;
    public Boolean setGlowing;
    public Boolean setAI;
    public String setCustomName;

    // Replace Effects
    public List<SpawnReplaceData> setReplaceEffects;

    // Potion Effects
    public List<PotionEffectData> addPotionEffects;

    // Equipment
    public EquipmentData setEquipment;

    // Drops (Loot)
    public Boolean clearDrops;
    public Double modifyDropChance; // Multiplier? Or override?
    public List<ItemData> addDrops;
    public List<ItemData> replaceDrops;
    public Integer setExperience;

    public static class SpawnReplaceData {
        public String replaceEntityType; // For spawning a different mob
        public Double spawnchance; // Chance of replacing the mob
        public int count; // For spawning multiple mobs
    } 

    public static class PotionEffectData {
        public String type;
        public int duration;
        public int amplifier;
    }

    public static class EquipmentData {
        public ItemData mainHand;
        public ItemData offHand;
        public ItemData helmet;
        public ItemData chestplate;
        public ItemData leggings;
        public ItemData boots;

        public Float mainHandDropChance;
        public Float offHandDropChance;
        public Float helmetDropChance;
        public Float chestplateDropChance;
        public Float leggingsDropChance;
        public Float bootsDropChance;
    }

    public static class ItemData {
        public String item; // Material name "minecraft:iron_sword"
        public int count = 1;
        public double chance = 1.0; // 0.0 - 1.0
        // Could add name, lore, enchants later
    }
}
