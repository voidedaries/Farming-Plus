package net.voidedaries.farmingplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.item.ModItems;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {

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

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.FERTILISER, 1)
                .pattern("ML")
                .pattern("DB")
                .input('M', ModItems.MANURE)
                .input('L', Items.OAK_LEAVES)
                .input('B', Items.BONE_MEAL)
                .input('D', Items.DIRT)
                .criterion(hasItem(ModItems.MANURE), conditionsFromItem(ModItems.MANURE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.FERTILISER)));



    }
}
