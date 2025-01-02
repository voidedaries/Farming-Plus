package net.voidedaries.farmingplus.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.mixin.SimpleInventoryAccessor;
import net.voidedaries.farmingplus.screen.SeedBagScreenHandler;
import net.voidedaries.farmingplus.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeedBagItem extends Item {

    public SeedBagItem(Settings settings) {
        super(settings);
    }

    public static void writeNbt(ItemStack stack, DefaultedList<ItemStack> inventory) {
        NbtCompound inventoryNbt = Inventories.writeNbt(new NbtCompound(), inventory);
        NbtCompound compound = stack.getOrCreateNbt();
        compound.put(FarmingPlus.MOD_ID + ".inventorynbt", inventoryNbt);
    }

    public static void readNbt(ItemStack stack, DefaultedList<ItemStack> inventory) {
        NbtCompound compound = stack.getNbt();
        if (compound == null || !compound.contains(FarmingPlus.MOD_ID + ".inventorynbt", NbtElement.COMPOUND_TYPE)) {
            return;
        }

        Inventories.readNbt(compound.getCompound(FarmingPlus.MOD_ID + ".inventorynbt"), inventory);
    }

    private void seedBagCosmetics(PlayerEntity player, World world, BlockPos pos) {
        world.playSound(player, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS,
                1.0F, 1.0F);

        if (world instanceof ServerWorld) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.POOF,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    3, 0.2, 0.2, 0.2, 0);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState blockState = world.getBlockState(pos);

        if (!blockState.isOf(Blocks.FARMLAND) && !blockState.isOf(ModBlocks.LOAM_FARMLAND)) {
            return ActionResult.PASS;
        }

        PlayerEntity player = context.getPlayer();
        if (player == null) {
            return ActionResult.PASS;
        }

        ItemStack stack = context.getStack();
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(18, ItemStack.EMPTY);
        readNbt(stack, inventory);

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack seedStack = inventory.get(i);

            if (!seedStack.isEmpty() && seedStack.isIn(ModTags.Items.SEED_BAG_ACCEPTABLE)) {
                Item item = seedStack.getItem();

                if (item instanceof BlockItem blockItem) {
                    BlockPos placePos = pos.up();

                    if (world.isAir(placePos)) {
                        BlockState cropState = blockItem.getBlock().getDefaultState();

                        world.setBlockState(placePos, cropState, 3);

                        seedStack.decrement(1);
                        if (seedStack.isEmpty()) {
                            inventory.set(i, ItemStack.EMPTY);
                        }

                        writeNbt(stack, inventory);
                        seedBagCosmetics(player, world, pos);

                        return ActionResult.SUCCESS;
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!stack.hasNbt()) {
            stack.setNbt(new NbtCompound());
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!stack.hasNbt()) {
            DefaultedList<ItemStack> inventory = DefaultedList.ofSize(18, ItemStack.EMPTY);
            SeedBagItem.writeNbt(stack, inventory);
        }

        DefaultedList<ItemStack> inventoryData = DefaultedList.ofSize(18, ItemStack.EMPTY);
        SeedBagItem.readNbt(stack, inventoryData);

        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return Text.translatable("gui.farmingplus.seed_bag");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                Inventory inventory = new SimpleInventory(inventoryData.toArray(new ItemStack[0])) {
                    {
                        this.addListener(inventory -> writeNbt(stack, ((SimpleInventoryAccessor) (Object)this).getHeldStacks()));
                    }
                };
                return new SeedBagScreenHandler(syncId, playerInventory, inventory);
            }
        });

        return TypedActionResult.success(stack);
    }


}