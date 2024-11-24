package net.voidedaries.farmingplus.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.voidedaries.farmingplus.item.custom.SeedBagItem;

public class SeedBagScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public SeedBagScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, readInventoryFromBuf(buf));
    }

    private static Inventory readInventoryFromBuf(PacketByteBuf buf) {
        ItemStack stack = buf.readItemStack();
        DefaultedList<ItemStack> inventoryData = DefaultedList.ofSize(18, ItemStack.EMPTY);
        SeedBagItem.readNbt(stack, inventoryData);
        return new SimpleInventory(inventoryData.toArray(new ItemStack[0]));
    }

    public SeedBagScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.SEED_BAG_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        checkSize(inventory, 18);
        inventory.onOpen(playerInventory.player);

        addSeedBagInventory();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ItemStack stack = serverPlayer.getMainHandStack();
            if (stack.getItem() instanceof SeedBagItem) {
                DefaultedList<ItemStack> inventory = DefaultedList.ofSize(18, ItemStack.EMPTY);
                for (int i = 0; i < this.inventory.size(); i++) {
                    inventory.set(i, this.inventory.getStack(i));
                }
                SeedBagItem.writeNbt(stack, inventory);
            }
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        ItemStack mainHandStack = player.getMainHandStack();
        return mainHandStack.getItem() instanceof SeedBagItem;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.size()) {
                // Move from seed bag inventory to player inventory
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from player inventory to seed bag inventory
                if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }


    private void addSeedBagInventory() {
        int xStart = 8;
        int yStart = 18;
        for (int i = 0; i < inventory.size(); i++) {
            int x = xStart + (i % 9) * 18;
            int y = yStart + (i / 9) * 18;
            this.addSlot(new Slot(inventory, i, x, y));
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 66 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 124));
        }
    }
}
