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
    public List<TrapTypeSettings> trapTypes = new ArrayList<>(List.of(TrapTypeSettings.createDefault()));

    public static class GeneralSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean debugLogging = false;
    }

    public static class TrapTypeSettings {

        @ConfigEntry.Gui.Tooltip
        public String trapTypeId = "default";

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
