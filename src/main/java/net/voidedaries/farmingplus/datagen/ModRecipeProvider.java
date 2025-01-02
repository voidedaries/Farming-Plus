package net.voidedaries.farmingplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.item.ModItems;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        SmithingTransformRecipeJsonBuilder.create(
                Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.ofItems(ModItems.DIAMOND_SCYTHE),
                Ingredient.ofItems(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                ModItems.NETHERITE_SCYTHE
        )
                .criterion("has_diamond_scythe", conditionsFromItem(ModItems.DIAMOND_SCYTHE))
                .criterion("has_netherite_ingot", conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.NETHERITE_SCYTHE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LOAM, 4)
                .pattern(" # ")
                .pattern("SCS")
                .pattern(" # ")
                .input('S', Items.SAND)
                .input('C', Items.CLAY)
                .input('#', ModBlocks.SILT)
                .criterion(hasItem(Items.SAND), conditionsFromItem(Items.SAND))
                .criterion(hasItem(Items.CLAY), conditionsFromItem(Items.CLAY))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.LOAM)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BOTTLER, 1)
                .pattern("   ")
                .pattern(" I ")
                .pattern("SSS")
                .input('S', Items.STONE)
                .input('I', Items.IRON_BARS)
                .criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
                .criterion(hasItem(Items.IRON_BARS), conditionsFromItem(Items.IRON_BARS))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.BOTTLER)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.SEED_BAG, 1)
                .pattern("LSL")
                .pattern("S S")
                .pattern("LSL")
                .input('L', Items.LEATHER)
                .input('S', Items.STRING)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SEED_BAG)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.WINE_BOTTLE, 4)
                .pattern("G G")
                .pattern("G G")
                .pattern(" G ")
                .input('G', Items.GLASS_PANE)
                .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.WINE_BOTTLE)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModItems.CORK, 2)
                .input(Blocks.OAK_SLAB)
                .criterion(hasItem(ModItems.CORK), conditionsFromItem(ModItems.CORK))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.CORK)));


        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SPRUCE_CRUSHING_TUB, 2)
                .pattern("   ")
                .pattern("S S")
                .pattern("SSS")
                .input('S', Items.SPRUCE_SLAB)
                .criterion(hasItem(Items.SPRUCE_SLAB), conditionsFromItem(Items.SPRUCE_SLAB))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.SPRUCE_CRUSHING_TUB)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SPRUCE_FERMENTATION_BARREL, 1)
                .pattern("PPP")
                .pattern("SBS")
                .pattern("PPP")
                .input('S', Items.SPRUCE_SLAB)
                .input('P', Items.SPRUCE_PLANKS)
                .input('B', Items.BUCKET)
                .criterion(hasItem(Items.SPRUCE_PLANKS), conditionsFromItem(Items.SPRUCE_PLANKS))
                .criterion(hasItem(Items.SPRUCE_SLAB), conditionsFromItem(Items.SPRUCE_SLAB))
                .criterion(hasItem(Items.BUCKET), conditionsFromItem(Items.BUCKET))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.SPRUCE_FERMENTATION_BARREL)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.FERTILISER, 1)
                .input(ModItems.MANURE)
                .input(Items.BONE_MEAL)
                .input(Items.DIRT)
                .input(ItemTags.LEAVES)
                .criterion(hasItem(ModItems.MANURE), conditionsFromItem(ModItems.MANURE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.FERTILISER)));

        //COMPOSTERS
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CINDER_COMPOSTER, 1)
                .pattern("EOE")
                .pattern("NCN")
                .pattern("EOE")
                .input('E', ModItems.EMBER_SHARD)
                .input('N', Blocks.NETHER_BRICKS)
                .input('O', Blocks.CRYING_OBSIDIAN)
                .input('C', Blocks.COMPOSTER)
                .criterion(hasItem(ModBlocks.CINDER_COMPOSTER), conditionsFromItem(ModBlocks.CINDER_COMPOSTER))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.CINDER_COMPOSTER)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.VOID_COMPOSTER, 1)
                .pattern("EPE")
                .pattern("SCS")
                .pattern("EPE")
                .input('E', ModItems.ENDER_SHARD)
                .input('S', Blocks.END_STONE_BRICKS)
                .input('P', Blocks.PURPUR_BLOCK)
                .input('C', Blocks.COMPOSTER)
                .criterion(hasItem(ModBlocks.VOID_COMPOSTER), conditionsFromItem(ModBlocks.VOID_COMPOSTER))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.VOID_COMPOSTER)));

        //SCYTHES
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.WOODEN_SCYTHE, 1)
                .pattern("PP ")
                .pattern(" SP")
                .pattern("S  ")
                .input('P', ItemTags.PLANKS)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.WOODEN_SCYTHE), conditionsFromItem(ModItems.WOODEN_SCYTHE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.WOODEN_SCYTHE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.STONE_SCYTHE, 1)
                .pattern("CC ")
                .pattern(" SC")
                .pattern("S  ")
                .input('C', Items.COBBLESTONE)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.STONE_SCYTHE), conditionsFromItem(ModItems.STONE_SCYTHE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.STONE_SCYTHE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.IRON_SCYTHE, 1)
                .pattern("II ")
                .pattern(" SI")
                .pattern("S  ")
                .input('I', Items.IRON_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.IRON_SCYTHE), conditionsFromItem(ModItems.IRON_SCYTHE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.IRON_SCYTHE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.GOLDEN_SCYTHE, 1)
                .pattern("GG ")
                .pattern(" SG")
                .pattern("S  ")
                .input('G', Items.GOLD_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.GOLDEN_SCYTHE), conditionsFromItem(ModItems.GOLDEN_SCYTHE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.GOLDEN_SCYTHE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.DIAMOND_SCYTHE, 1)
                .pattern("DD ")
                .pattern(" SD")
                .pattern("S  ")
                .input('D', Items.DIAMOND)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.DIAMOND_SCYTHE), conditionsFromItem(ModItems.DIAMOND_SCYTHE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.DIAMOND_SCYTHE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.NETHERITE_SCYTHE, 1)
                .pattern("NN ")
                .pattern(" SN")
                .pattern("S  ")
                .input('N', Items.NETHERITE_INGOT)
                .input('S', Items.STICK)
                .criterion(hasItem(ModItems.NETHERITE_SCYTHE), conditionsFromItem(ModItems.NETHERITE_SCYTHE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.NETHERITE_SCYTHE) + "_shaped"));

    }
}
