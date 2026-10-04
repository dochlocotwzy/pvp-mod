package com.pvptraps.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = "pvptraps")
public class TrapConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Category("general")
    public GeneralSettings general = new GeneralSettings();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public List<TrapTypeSettings> trapTypes = createDefaultTrapTypes();

    private static List<TrapTypeSettings> createDefaultTrapTypes() {
        return new ArrayList<>(List.of(
                TrapTypeSettings.create("spike", 6.0, List.of()),
                TrapTypeSettings.create("ice", 1.0, List.of(new PotionEffectEntry("minecraft:slowness", 1, 100))),
                TrapTypeSettings.create("poison", 1.0, List.of(new PotionEffectEntry("minecraft:poison", 0, 100))),
                TrapTypeSettings.create("electric", 3.0, List.of(new PotionEffectEntry("minecraft:slowness", 1, 60), new PotionEffectEntry("minecraft:weakness", 0, 60))),
                TrapTypeSettings.create("smoke", 0.0, List.of(new PotionEffectEntry("minecraft:blindness", 0, 50))),
                TrapTypeSettings.create("weakening", 2.0, List.of(new PotionEffectEntry("minecraft:weakness", 0, 100))),
                TrapTypeSettings.create("sticky", 0.0, List.of(new PotionEffectEntry("minecraft:slowness", 3, 60))),
                TrapTypeSettings.create("fire", 2.0, List.of()),
                TrapTypeSettings.create("exhaustion", 2.0, List.of(new PotionEffectEntry("minecraft:weakness", 0, 80), new PotionEffectEntry("minecraft:slowness", 1, 80)))
        ));
    }

    public static class GeneralSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean debugLogging = false;
    }

    public static class TrapTypeSettings {

        @ConfigEntry.Gui.Tooltip
        public String trapTypeId = "default";

        @ConfigEntry.Gui.Tooltip
        public double damage = 4.0;

        @ConfigEntry.Gui.Tooltip
        public double triggerRadius = 1.0;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 3600)
        public int cooldownSeconds = 10;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 3600)
        public int trapLifetimeSeconds = 30;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int maxStackSize = 16;

        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
        public int enemyVisibilitySeconds = 5;

        @ConfigEntry.Gui.Tooltip
        public boolean ignoreWholeOwnerTeam = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.CollapsibleObject
        public List<PotionEffectEntry> potionEffects = new ArrayList<>(List.of(
                new PotionEffectEntry("minecraft:slowness", 1, 100),
                new PotionEffectEntry("minecraft:poison", 0, 60)
        ));

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.CollapsibleObject
        public List<AttributeModifierEntry> attributeModifiers = new ArrayList<>(List.of(
                new AttributeModifierEntry("minecraft:generic.jump_strength", -0.4, "ADD_MULTIPLIED_TOTAL", 100)
        ));

        public static TrapTypeSettings createDefault() {
            return new TrapTypeSettings();
        }
    }

    public static class PotionEffectEntry {
        @ConfigEntry.Gui.Tooltip
        public String effectId;
        @ConfigEntry.Gui.Tooltip
        public int amplifier;
        @ConfigEntry.Gui.Tooltip
        public int durationTicks;

        public PotionEffectEntry() {
        }

        public PotionEffectEntry(String effectId, int amplifier, int durationTicks) {
            this.effectId = effectId;
            this.amplifier = amplifier;
            this.durationTicks = durationTicks;
        }
    }

    public static class AttributeModifierEntry {
        @ConfigEntry.Gui.Tooltip
        public String attributeId;
        @ConfigEntry.Gui.Tooltip
        public double amount;
        @ConfigEntry.Gui.Tooltip
        public String operation;
        @ConfigEntry.Gui.Tooltip
        public int durationTicks;

        public AttributeModifierEntry() {
        }

        public AttributeModifierEntry(String attributeId, double amount, String operation, int durationTicks) {
            this.attributeId = attributeId;
            this.amount = amount;
            this.operation = operation;
            this.durationTicks = durationTicks;
        }
    }
}