package net.voidedaries.farmingplus.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.item.custom.FertiliserItem;
import net.voidedaries.farmingplus.item.custom.ScytheItem;
import net.voidedaries.farmingplus.item.custom.SeedBagItem;
import net.voidedaries.farmingplus.item.custom.WineItem;

public class ModItems {

    public static final Item MANURE = registerItem("manure",
            new Item(new FabricItemSettings()));
    public static final Item FERTILISER = registerItem("fertiliser",
            new FertiliserItem(new FabricItemSettings()));
    public static final Item WINE_BOTTLE = registerItem("wine_bottle",
            new Item(new FabricItemSettings()));
    public static final Item CORK = registerItem("cork",
            new Item(new FabricItemSettings()));

    public static final Item RED_WINE = registerItem("red_wine",
            new WineItem(new FabricItemSettings().maxCount(16)));
    public static final Item BLUE_WINE = registerItem("blue_wine",
            new WineItem(new FabricItemSettings().maxCount(16)));
    public static final Item WHITE_WINE = registerItem("white_wine",
            new WineItem(new FabricItemSettings().maxCount(16)));

    public static final Item RED_GRAPES = registerItem("red_grapes",
            new AliasedBlockItem(ModBlocks.RED_GRAPE_VINE,new FabricItemSettings().food(ModFoodComponents.GRAPES)));
    public static final Item WHITE_GRAPES = registerItem("white_grapes",
            new AliasedBlockItem(ModBlocks.WHITE_GRAPE_VINE,new FabricItemSettings().food(ModFoodComponents.GRAPES)));
    public static final Item BLUE_GRAPES = registerItem("blue_grapes",
            new AliasedBlockItem(ModBlocks.BLUE_GRAPE_VINE,new FabricItemSettings().food(ModFoodComponents.GRAPES)));

    public static final Item CHERRIES = registerItem("cherries",
            new Item(new FabricItemSettings().food(ModFoodComponents.CHERRIES)));
    public static final Item GOLDEN_CHERRIES = registerItem("golden_cherries",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModFoodComponents.GOLDEN_CHERRIES)));

    public static final Item EMBER_SHARD = registerItem("ember_shard",
            new Item(new FabricItemSettings()));

    public static final Item ENDER_SHARD = registerItem("ender_shard",
            new Item(new FabricItemSettings()));

    public static final Item WOODEN_SCYTHE = registerItem("wooden_scythe",
            new ScytheItem(ModToolMaterials.WOOD, 2, 2f, new FabricItemSettings()));
    public static final Item STONE_SCYTHE = registerItem("stone_scythe",
            new ScytheItem(ModToolMaterials.STONE, 2, 2f,new FabricItemSettings()));
    public static final Item IRON_SCYTHE = registerItem("iron_scythe",
            new ScytheItem(ModToolMaterials.IRON, 2, 2f, new FabricItemSettings()));
    public static final Item GOLDEN_SCYTHE = registerItem("golden_scythe",
            new ScytheItem(ModToolMaterials.GOLD, 2, 2f, new FabricItemSettings()));
    public static final Item DIAMOND_SCYTHE = registerItem("diamond_scythe",
            new ScytheItem(ModToolMaterials.DIAMOND, 2, 2f, new FabricItemSettings()));
    public static final Item NETHERITE_SCYTHE = registerItem("netherite_scythe",
            new ScytheItem(ModToolMaterials.NETHERITE, 2, 2f, new FabricItemSettings()));

    public static final Item SEED_BAG = registerItem("seed_bag",
            new SeedBagItem(new FabricItemSettings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(FarmingPlus.MOD_ID, name), item);
    }

    public static void registerModItems() {
        FarmingPlus.LOGGER.info("Registering Mod Items for " + FarmingPlus.MOD_ID);
    }
}
