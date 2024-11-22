package net.voidedaries.farmingplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.block.entity.ModBlockEntities;
import net.voidedaries.farmingplus.datagen.ModFeatures;
import net.voidedaries.farmingplus.effect.ModEffects;
import net.voidedaries.farmingplus.fluid.ModFluids;
import net.voidedaries.farmingplus.item.ModItemGroups;
import net.voidedaries.farmingplus.item.ModItems;
import net.voidedaries.farmingplus.networking.ModMessages;
import net.voidedaries.farmingplus.networking.packet.SyncPlayerStatsPacket;
import net.voidedaries.farmingplus.recipe.ModRecipes;
import net.voidedaries.farmingplus.screen.ModScreenHandlers;
import net.voidedaries.farmingplus.world.gen.ModWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FarmingPlus implements ModInitializer {
	public static final String MOD_ID = "farmingplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModEffects.registerEffects();
		ModFluids.registerModFluids();

		ModRecipes.registerRecipes();

		ModWorldGeneration.generateModWorldGen();
		ModFeatures.registerFeatures();

		ModMessages.registerC2SPackets();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			SyncPlayerStatsPacket.send(handler.player);
		});

		org.spongepowered.asm.mixin.Mixins.addConfiguration("farmingplus.mixins.json");
	}

}