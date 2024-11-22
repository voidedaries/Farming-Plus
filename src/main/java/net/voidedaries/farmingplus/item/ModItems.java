package net.voidedaries.farmingplus.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.item.custom.FertiliserItem;
import net.voidedaries.farmingplus.item.custom.WineItem;

public class ModItems {
    public static final Item MANURE = registerItem("manure",
            new Item(new FabricItemSettings()));
    public static final Item FERTILISER = registerItem("fertiliser",
            new FertiliserItem(new FabricItemSettings()));
    public static final Item WINE_BOTTLE = registerItem("wine_bottle",
            new Item(new FabricItemSettings()));
    public static final Item RED_WINE = registerItem("red_wine",
            new WineItem(new FabricItemSettings().maxCount(16)));
    public static final Item BLUE_WINE = registerItem("blue_wine",
            new WineItem(new FabricItemSettings().maxCount(16)));
    public static final Item WHITE_WINE = registerItem("white_wine",
            new WineItem(new FabricItemSettings().maxCount(16)));
    public static final Item CORK = registerItem("cork",
            new Item(new FabricItemSettings()));
    public static final Item RED_GRAPES = registerItem("red_grapes",
            new FertiliserItem(new FabricItemSettings().food(ModFoodComponents.RED_GRAPES)));
    public static final Item WHITE_GRAPES = registerItem("white_grapes",
            new FertiliserItem(new FabricItemSettings().food(ModFoodComponents.RED_GRAPES)));
    public static final Item BLUE_GRAPES = registerItem("blue_grapes",
            new FertiliserItem(new FabricItemSettings().food(ModFoodComponents.RED_GRAPES)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(FarmingPlus.MOD_ID, name), item);
    }

    public static void registerModItems() {
        FarmingPlus.LOGGER.info("Registering Mod Items for " + FarmingPlus.MOD_ID);
    }
}
