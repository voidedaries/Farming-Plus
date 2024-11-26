package net.voidedaries.farmingplus.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.voidedaries.farmingplus.item.ModItems;
import net.voidedaries.farmingplus.item.custom.SeedBagItem;
import net.voidedaries.farmingplus.util.ModTags;

public class SeedBagScreenHandler extends ScreenHandler {
    Inventory inventory;


    public SeedBagScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(18));
    }

    public SeedBagScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.SEED_BAG_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        checkSize(inventory, 18);
        inventory.onOpen(playerInventory.player);

        addSeedBagInventory(inventory);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory, playerInventory.selectedSlot);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        if (this.inventory != null) {
            this.inventory.onClose(player);
        }
        super.onClosed(player);
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

            if (invSlot < 18) {
                // Move from seed bag inventory to player inventory
                if (!this.insertItem(originalStack, 18, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from player inventory to seed bag inventory
                if (!this.insertItem(originalStack, 0, 18, false)) {
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


    private void addSeedBagInventory(Inventory inventory) {
        int xStart = 8;
        int yStart = 18;
        for (int i = 0; i < 18; i++) {
            int x = xStart + (i % 9) * 18;
            int y = yStart + (i / 9) * 18;
            this.addSlot(new Slot(inventory, i, x, y){
                @Override
                public boolean canInsert(ItemStack stack) {
                    return !stack.isOf(ModItems.SEED_BAG) && stack.isIn(ModTags.Items.SEED_BAG_ACCEPTABLE);
                }
            });
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 66 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory, int selectedSlot) {
        for (int col = 0; col < 9; ++col) {
            int finalCol = col;
            this.addSlot(new Slot(playerInventory, finalCol, 8 + col * 18, 124){
                @Override
                public boolean canTakeItems(PlayerEntity playerEntity) {
                    return selectedSlot != finalCol;
                }
            });
        }
    }
}
