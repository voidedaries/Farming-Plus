package net.voidedaries.farmingplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.voidedaries.farmingplus.block.ModBlocks;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {super(dataOutput);}

    @Override
    public void generate() {
        addDrop(ModBlocks.LOAM);
        addDrop(ModBlocks.SILT);
        addDrop(ModBlocks.SPRUCE_CRUSHING_TUB);
        addDrop(ModBlocks.SPRUCE_FERMENTATION_BARREL);
        addDrop(ModBlocks.LOAM_FARMLAND, this::createLoamFarmlandDrops);

        addDrop(ModBlocks.BLUEBELL);
        addPottedPlantDrops(ModBlocks.POTTED_BLUEBELL);
    }

    private LootTable.Builder createLoamFarmlandDrops(Block block) {
        return LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModBlocks.LOAM)));
    }

}
