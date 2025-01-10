package net.voidedaries.farmingplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModEnglishLangProvider extends FabricLanguageProvider {

    public ModEnglishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {

        //Item Groups
        translationBuilder.add("itemgroup.farming_plus", "Farming Plus");

        //Items

        translationBuilder.add("item.farmingplus.ember_shard", "Ember Shard");
        translationBuilder.add("item.farmingplus.ender_shard", "Ender Shard");

            //SCYTHES
        translationBuilder.add("item.farmingplus.wooden_scythe", "Wooden Scythe");
        translationBuilder.add("item.farmingplus.stone_scythe", "Stone Scythe");
        translationBuilder.add("item.farmingplus.iron_scythe", "Iron Scythe");
        translationBuilder.add("item.farmingplus.golden_scythe", "Golden Scythe");
        translationBuilder.add("item.farmingplus.diamond_scythe", "Diamond Scythe");
        translationBuilder.add("item.farmingplus.netherite_scythe", "Netherite Scythe");

        translationBuilder.add("item.farmingplus.seed_bag", "Seed Bag");

        translationBuilder.add("item.farmingplus.manure", "Manure");
        translationBuilder.add("item.farmingplus.fertiliser", "Fertiliser");

        translationBuilder.add("item.farmingplus.cork", "Cork");
        translationBuilder.add("item.farmingplus.wine_bottle", "Wine Bottle");

            //WINES
        translationBuilder.add("item.farmingplus.red_wine", "Red Wine");
        translationBuilder.add("item.farmingplus.blue_wine", "Blue Wine");
        translationBuilder.add("item.farmingplus.white_wine", "White Wine");

        translationBuilder.add("item.farmingplus.cherries", "Cherries");
        translationBuilder.add("block.farmingplus.cherry_fruit_leaves", "Cherry Fruit Leaves");
        translationBuilder.add("item.farmingplus.golden_cherries", "Golden Cherries");

            //GRAPES
        translationBuilder.add("item.farmingplus.red_grapes", "Red Grapes");
        translationBuilder.add("item.farmingplus.blue_grapes", "Blue Grapes");
        translationBuilder.add("item.farmingplus.white_grapes", "White Grapes");

            //GRAPE JUICES
        translationBuilder.add("item.farmingplus.red_grape_fluid_bucket", "Red Grape Juice Bucket");
        translationBuilder.add("item.farmingplus.blue_grape_fluid_bucket", "Blue Grape Juice Bucket");
        translationBuilder.add("item.farmingplus.white_grape_fluid_bucket", "White Grape Juice Bucket");

        translationBuilder.add("item.farmingplus.red_grape_fluid_bottle", "Red Grape Juice Bottle");
        translationBuilder.add("item.farmingplus.blue_grape_fluid_bottle", "Blue Grape Juice Bottle");
        translationBuilder.add("item.farmingplus.white_grape_fluid_bottle", "White Grape Juice Bottle");

            //FERMENTED WINE
        translationBuilder.add("item.farmingplus.red_fermented_fluid_bucket", "Red Fermented Wine Bucket");
        translationBuilder.add("item.farmingplus.blue_fermented_fluid_bucket", "Blue Fermented Wine Bucket");
        translationBuilder.add("item.farmingplus.white_fermented_fluid_bucket", "White Fermented Wine Bucket");

        translationBuilder.add("item.farmingplus.red_fermented_fluid_bottle", "Red Fermented Wine Bottle");
        translationBuilder.add("item.farmingplus.blue_fermented_fluid_bottle", "Blue Fermented Wine Bottle");
        translationBuilder.add("item.farmingplus.white_fermented_fluid_bottle", "White Fermented Wine Bottle");

        //Blocks
        translationBuilder.add("block.farmingplus.red_grape_vine", "Red Grape Vine");
        translationBuilder.add("block.farmingplus.blue_grape_vine", "Blue Grape Vine");
        translationBuilder.add("block.farmingplus.white_grape_vine", "White Grape Vine");

        translationBuilder.add("block.farmingplus.loam_block", "Loam");
        translationBuilder.add("block.farmingplus.loam_farmland", "Loam Farmland");
        translationBuilder.add("block.farmingplus.netherrack_farmland", "Netherrack Farmland");
        translationBuilder.add("block.farmingplus.end_farmland", "End Farmland");

        translationBuilder.add("block.farmingplus.silt_block", "Silt");

        translationBuilder.add("block.farmingplus.cinder_composter", "Cinder Composter");
        translationBuilder.add("block.farmingplus.void_composter", "Void Composter");
        translationBuilder.add("block.farmingplus.bottler", "Bottler");
        translationBuilder.add("block.farmingplus.spruce_crushing_tub", "Spruce Crushing Tub");
        translationBuilder.add("block.farmingplus.spruce_fermentation_barrel", "Spruce Fermentation Barrel");

        //Effects
        translationBuilder.add("effect.farmingplus.intoxication", "Intoxication");

        //Guis
        translationBuilder.add("gui.farmingplus.seed_bag", "Seed Bag");
        translationBuilder.add("gui.farmingplus.bottler", "Bottler");
        translationBuilder.add("gui.farmingplus.fermentation_barrel", "Fermenting Barrel");

        //Tooltips
        translationBuilder.add("gui.farmingplus.fermentation_barrel_clear_widget", "Clear Fluid");
        translationBuilder.add("tutorialmod.tooltip.liquid.amount.with.capacity", "%s / %s mB");
        translationBuilder.add("tutorialmod.tooltip.liquid.amount", "%s mB");
        translationBuilder.add("block.minecraft.empty", "Empty");

        translationBuilder.add("block.farmingplus.red_grape_fluid", "Red Grape Fluid");
        translationBuilder.add("block.farmingplus.red_fermented_wine", "Red Fermented Wine");
        translationBuilder.add("block.farmingplus.blue_grape_fluid", "Blue Grape Fluid");
        translationBuilder.add("block.farmingplus.blue_fermented_wine", "Blue Fermented Wine");
        translationBuilder.add("block.farmingplus.white_grape_fluid", "White Grape Fluid");
        translationBuilder.add("block.farmingplus.white_fermented_wine", "White Fermented Wine");
    }

}