package net.voidedaries.farmingplus.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<CrushingTubBlockEntity> CRUSHING_TUB_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(FarmingPlus.MOD_ID, "crushing_table_be"),
                    FabricBlockEntityTypeBuilder.create(CrushingTubBlockEntity::new,
                            ModBlocks.SPRUCE_CRUSHING_TUB).build());

    public static final BlockEntityType<FermentationBarrelBlockEntity> FERMENTATION_BARREL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(FarmingPlus.MOD_ID, "fermentation_barrel_be"),
                    FabricBlockEntityTypeBuilder.create(FermentationBarrelBlockEntity::new,
                            ModBlocks.SPRUCE_FERMENTATION_BARREL).build());

    public static final BlockEntityType<BottlerBlockEntity> BOTTLER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(FarmingPlus.MOD_ID, "bottler_be"),
                    FabricBlockEntityTypeBuilder.create(BottlerBlockEntity::new,
                            ModBlocks.BOTTLER).build());

    @SuppressWarnings("UnstableApiUsage")
    public static void registerBlockEntities() {

        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.primaryFluidStorage, FERMENTATION_BARREL_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.secondaryFluidStorage, FERMENTATION_BARREL_BLOCK_ENTITY);

        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, BOTTLER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, CRUSHING_TUB_BLOCK_ENTITY);

        FarmingPlus.LOGGER.info("Registering Block Entities for " + FarmingPlus.MOD_ID);
    }
}
