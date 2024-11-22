package net.voidedaries.farmingplus.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent RED_GRAPES = new FoodComponent.Builder().hunger(4).saturationModifier(0.75f).alwaysEdible()
            .statusEffect(new StatusEffectInstance((StatusEffects.REGENERATION), 60), 1).build();
}