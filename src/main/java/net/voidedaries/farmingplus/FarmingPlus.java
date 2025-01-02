package net.voidedaries.farmingplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
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

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
				SyncPlayerStatsPacket.send(handler.player));

		org.spongepowered.asm.mixin.Mixins.addConfiguration("farmingplus.mixins.json");

		registerHoeInteraction();
	}

	private void registerHoeInteraction() {
		UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
			if (hand != Hand.MAIN_HAND) return ActionResult.PASS;

			BlockPos pos = hitResult.getBlockPos();
			ItemStack stack = player.getStackInHand(hand);

			if (stack.getItem() instanceof HoeItem) {
				BlockState state = world.getBlockState(pos);

				if (state.isOf(Blocks.END_STONE)) {
					if (!world.isClient) {
						world.setBlockState(pos, ModBlocks.END_FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 0), 11);
						world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
						if (!player.isCreative()) {
							stack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
						}
					}
					return ActionResult.SUCCESS;
				}

				// Convert Netherrack to Netherrack Farmland
				if (state.isOf(Blocks.NETHERRACK)) {
					if (!world.isClient) {
						world.setBlockState(pos, ModBlocks.NETHERRACK_FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 0), 11);
						world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
						if (!player.isCreative()) {
							stack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
						}
					}
					return ActionResult.SUCCESS;
				}
			}

			return ActionResult.PASS;
		}));
	}

}