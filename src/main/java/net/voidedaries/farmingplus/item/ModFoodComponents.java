package net.voidedaries.farmingplus.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent GRAPES = new
            FoodComponent.Builder().hunger(4).saturationModifier(0.75f).alwaysEdible()
            .statusEffect(new StatusEffectInstance((StatusEffects.REGENERATION), 60), 1).build();

    public static final FoodComponent CHERRIES = new
            FoodComponent.Builder().hunger(4).saturationModifier(0.75f).alwaysEdible().build();

    public static final FoodComponent GOLDEN_CHERRIES = new
            FoodComponent.Builder().hunger(6).saturationModifier(6.0f).alwaysEdible()
            .statusEffect(new StatusEffectInstance((StatusEffects.ABSORPTION), 1200), 1).build();
}