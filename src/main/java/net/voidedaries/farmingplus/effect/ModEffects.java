package net.voidedaries.farmingplus.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;

public class ModEffects {
    public static StatusEffect INTOXICATION;

    public static StatusEffect registerStatusEffect(String name) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(FarmingPlus.MOD_ID, name),
                new IntoxicationEffect(StatusEffectCategory.HARMFUL, 5845074));
    }

    public static void registerEffects() {
        INTOXICATION = registerStatusEffect("intoxication");

        FarmingPlus.LOGGER.info("Registering Mod Effects for " + FarmingPlus.MOD_ID);
    }
}