package com.pvptraps.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = "pvptraps")
public class TrapConfig implements ConfigData {

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralSettings general = new GeneralSettings();

    /**
     * Compatibility field for configs created before the per-trap GUI was introduced.
     * It is migrated once by ConfigManager and hidden from the GUI.
     */
    @ConfigEntry.Gui.Excluded
    public List<TrapTypeSettings> trapTypes = null;

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings spike = TrapTypeSettings.create("spike", 6.0, List.of());

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings ice = TrapTypeSettings.create("ice", 1.0,
            List.of(new PotionEffectEntry("minecraft:slowness", 1, 100)));

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings poison = TrapTypeSettings.create("poison", 1.0,
            List.of(new PotionEffectEntry("minecraft:poison", 0, 100)));

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings electric = TrapTypeSettings.create("electric", 3.0,
            List.of(
                    new PotionEffectEntry("minecraft:slowness", 1, 60),
                    new PotionEffectEntry("minecraft:weakness", 0, 60)
            ));

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings smoke = TrapTypeSettings.create("smoke", 0.0,
            List.of(new PotionEffectEntry("minecraft:blindness", 0, 50)));

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings weakening = TrapTypeSettings.create("weakening", 2.0,
            List.of(new PotionEffectEntry("minecraft:weakness", 0, 100)));

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings sticky = TrapTypeSettings.create("sticky", 0.0,
            List.of(new PotionEffectEntry("minecraft:slowness", 3, 60)));

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings fire = TrapTypeSettings.create("fire", 2.0, List.of());

    @ConfigEntry.Category("traps")
    @ConfigEntry.Gui.CollapsibleObject
    public TrapTypeSettings exhaustion = TrapTypeSettings.create("exhaustion", 2.0,
            List.of(
                    new PotionEffectEntry("minecraft:weakness", 0, 80),
                    new PotionEffectEntry("minecraft:slowness", 1, 80)
            ));

    public static TrapTypeSettings presetFor(String id) {
        if (id == null) {
            return TrapTypeSettings.createDefault();
        }
        return switch (id) {
            case "spike" -> spikeDefaults();
            case "ice" -> iceDefaults();
            case "poison" -> poisonDefaults();
            case "electric" -> electricDefaults();
            case "smoke" -> smokeDefaults();
            case "weakening" -> weakeningDefaults();
            case "sticky" -> stickyDefaults();
            case "fire" -> fireDefaults();
            case "exhaustion" -> exhaustionDefaults();
            default -> TrapTypeSettings.createDefault();
        };
    }

    private static TrapTypeSettings spikeDefaults() {
        return TrapTypeSettings.create("spike", 6.0, List.of());
    }

    private static TrapTypeSettings iceDefaults() {
        return TrapTypeSettings.create("ice", 1.0,
                List.of(new PotionEffectEntry("minecraft:slowness", 1, 100)));
    }

    private static TrapTypeSettings poisonDefaults() {
        return TrapTypeSettings.create("poison", 1.0,
                List.of(new PotionEffectEntry("minecraft:poison", 0, 100)));
    }

    private static TrapTypeSettings electricDefaults() {
        return TrapTypeSettings.create("electric", 3.0,
                List.of(
                        new PotionEffectEntry("minecraft:slowness", 1, 60),
                        new PotionEffectEntry("minecraft:weakness", 0, 60)
                ));
    }

    private static TrapTypeSettings smokeDefaults() {
        return TrapTypeSettings.create("smoke", 0.0,
                List.of(new PotionEffectEntry("minecraft:blindness", 0, 50)));
    }

    private static TrapTypeSettings weakeningDefaults() {
        return TrapTypeSettings.create("weakening", 2.0,
                List.of(new PotionEffectEntry("minecraft:weakness", 0, 100)));
    }

    private static TrapTypeSettings stickyDefaults() {
        return TrapTypeSettings.create("sticky", 0.0,
                List.of(new PotionEffectEntry("minecraft:slowness", 3, 60)));
    }

    private static TrapTypeSettings fireDefaults() {
        return TrapTypeSettings.create("fire", 2.0, List.of());
    }

    private static TrapTypeSettings exhaustionDefaults() {
        return TrapTypeSettings.create("exhaustion", 2.0,
                List.of(
                        new PotionEffectEntry("minecraft:weakness", 0, 80),
                        new PotionEffectEntry("minecraft:slowness", 1, 80)
                ));
    }

    public static class GeneralSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean debugLogging = false;
    }

    public static class TrapTypeSettings {
        @ConfigEntry.Gui.Excluded
        public String trapTypeId = "default";

        @ConfigEntry.Gui.Tooltip
        public double damage = 4.0;

        @ConfigEntry.Gui.Tooltip
        public double triggerRadius = 1.0;

        @ConfigEntry.Gui.Tooltip
        public String activationMode = "proximity";

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 3600)
        public int cooldownSeconds = 10;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 3600)
        public int trapLifetimeSeconds = 30;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int maxStackSize = 16;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 60)
        public int enemyVisibilitySeconds = 5;

        @ConfigEntry.Gui.Tooltip
        public boolean affectPlayers = true;

        @ConfigEntry.Gui.Tooltip
        public boolean affectMobs = true;

        @ConfigEntry.Gui.Tooltip
        public boolean ignoreWholeOwnerTeam = true;

        @ConfigEntry.Gui.Tooltip
        public List<PotionEffectEntry> potionEffects = new ArrayList<>();

        @ConfigEntry.Gui.Tooltip
        public List<AttributeModifierEntry> attributeModifiers = new ArrayList<>();

        public static TrapTypeSettings create(String id, double damage, List<PotionEffectEntry> effects) {
            TrapTypeSettings settings = new TrapTypeSettings();
            settings.trapTypeId = id;
            settings.damage = damage;
            settings.potionEffects = new ArrayList<>(effects);
            settings.attributeModifiers = new ArrayList<>();
            settings.activationMode = switch (id) {
                case "spike", "sticky", "fire" -> "step";
                default -> "proximity";
            };
            return settings;
        }

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
