package net.voidedaries.farmingplus.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(FarmingPlus.MOD_ID, FermentationBarrelRecipe.Serializer.ID),
                FermentationBarrelRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(FarmingPlus.MOD_ID, FermentationBarrelRecipe.Type.ID),
                FermentationBarrelRecipe.Type.INSTANCE);

        FarmingPlus.LOGGER.info("Registering Mod Recipes for " + FarmingPlus.MOD_ID);

    }

}
