package com.creativestylab.incontrolmob.listeners;

import com.creativestylab.incontrolmob.InControlMob;
import com.creativestylab.incontrolmob.rules.ActionExecutor;
import com.creativestylab.incontrolmob.rules.Rule;
import com.creativestylab.incontrolmob.rules.RuleMatcher;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;
import java.util.random;

public class MobSpawnListener implements Listener {

    private final InControlMob plugin;

    public MobSpawnListener(InControlMob plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();

        // Determine which rule sets to check
        // 1. Spawner rules if applicable
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            if (processRules(plugin.getRuleManager().getSpawnerRules(), event))
                return;
        } else if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM
                || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.COMMAND) {
            if (processRules(plugin.getRuleManager().getSummonRules(), event))
                return;
        }

        // 2. Generic spawn rules (fallthrough)
        processRules(plugin.getRuleManager().getSpawnRules(), event);
    }

    private boolean processRules(List<Rule> rules, CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        boolean matchedAny = false;

        for (Rule rule : rules) {
            if (!rule.enabled)
                continue;

            if (RuleMatcher.matches(rule.conditions, entity, event.getLocation(), event.getSpawnReason())) {
                matchedAny = true;

                // Check Allow/Deny
                if (rule.actions != null) {
                    if (rule.actions.allow != null && !rule.actions.allow) {
                        event.setCancelled(true);
                        if (rule.actions.message != null) {
                            plugin.getLogger().info(rule.actions.message);
                        }
                        return true; // Stop processing if denied
                    }

                    // Replacement
                    if (rule.actions.setReplaceEffects != null ) {
                        SpawnReplaceData replaceData = rule.actions.setReplaceEffects;
                        if (replaceData.spawnchance != null ) {
                            if (random.nextDouble() <= replaceData.spawnchance) { // First, apply chance of replacing
                                try: {
                                    EntityType newType = EntityType.valueOf(replaceData.replaceEntityType.toUpperCase());
                                    event.setCancelled(true); // select entity to spawn & cancel old entity

                                    int spawncount = Math.max(1, replaceData.count) // prevent returning 0
                                    for (int i = 0; i < spawncount; i++) {                                          //for the expected number of times:
                                        event.getLocation().getWorld().spawnEntity(event.getLocation(), newType);   //spawn expected unit
                                    }
                                    return true;
                                } catch (IllegalArgumentException e) {
                                    plugin.getLogger().warning("Invalid entity type for replacement: " + replaceData.replaceEntityType);
                                }
                            }
                        }
                    }
// I'll keep the old replace logic for now, cleanup can come later
                    
//                    if (rule.actions.replaceEntityType != null) {
//                        try {
//                            EntityType newType = EntityType.valueOf(rule.actions.replaceEntityType.toUpperCase());
//                            event.setCancelled(true);
                            // Spawn new entity
                            // TODO: Pass context to new entity? Beware infinite recursion if rule matches
                            // new entity.
                            // Basic impl:
//                            event.getLocation().getWorld().spawnEntity(event.getLocation(), newType);
                            // We return true because existing event is cancelled.
//                            return true;
//                        } catch (IllegalArgumentException e) {
//                            plugin.getLogger()
//                                    .warning("Invalid entity type for replacement: " + rule.actions.replaceEntityType);
//                        }
//                    }

                    // Apply actions
                    ActionExecutor.execute(rule.actions, entity);
                }

                if (!rule.continueOnMatch)
                    return true;
            }
        }
        return false;
    }
}
