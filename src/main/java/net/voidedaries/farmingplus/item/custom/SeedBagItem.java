package net.voidedaries.farmingplus.item.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.screen.SeedBagScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeedBagItem extends Item {

    public SeedBagItem(Settings settings) {
        super(settings);
    }

    public static void writeNbt(ItemStack stack, DefaultedList<ItemStack> inventory) {
        NbtCompound compound = stack.getOrCreateNbt();
        NbtList itemsList = new NbtList();

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty()) {
                NbtCompound itemTag = new NbtCompound();
                itemTag.putByte("Slot", (byte) (i + 36));
                itemStack.writeNbt(itemTag);
                itemsList.add(itemTag);
            }
        }

        compound.put("Items", itemsList);
    }

    public static void readNbt(ItemStack stack, DefaultedList<ItemStack> inventory) {
        NbtCompound compound = stack.getNbt();
        if (compound == null || !compound.contains("Items", NbtElement.LIST_TYPE)) {
            return;
        }

        NbtList itemsList = compound.getList("Items", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < itemsList.size(); i++) {
            NbtCompound itemTag = itemsList.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;
            if (slot >= 36 && slot < 36 + inventory.size()) {
                inventory.set(slot - 36, ItemStack.fromNbt(itemTag));
            }
        }
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

        player.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeItemStack(stack);
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable("gui.farmingplus.seed_bag");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                Inventory inventory = new SimpleInventory(inventoryData.toArray(new ItemStack[0]));
                return new SeedBagScreenHandler(syncId, playerInventory, inventory);
            }
        });

        return TypedActionResult.success(stack);
    }


}