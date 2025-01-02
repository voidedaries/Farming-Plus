package net.voidedaries.farmingplus.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {
    WOOD(80, 2.0F, 2.5F, 8,  () ->
            Ingredient.ofItems(Items.OAK_PLANKS)),
    STONE(160, 2.0F, 3.5F, 8, () ->
            Ingredient.ofItems(Items.OAK_PLANKS)),
    IRON( 280, 2.0F, 4.5F, 12, () ->
            Ingredient.ofItems(Items.OAK_PLANKS)),
    GOLD(40, 2.0F, 2.5F, 25, () ->
            Ingredient.ofItems(Items.OAK_PLANKS)),
    DIAMOND(1580, 2.0F, 5.5F, 15, () ->
            Ingredient.ofItems(Items.OAK_PLANKS)),
    NETHERITE(2064, 2.0F, 6.5F, 20, () ->
            Ingredient.ofItems(Items.OAK_PLANKS));

    private final int itemDurability;
    private final float miningSpeedMultiplier;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterials(int itemDurability, float miningSpeedMultiplier, float attackDamage,
                     int enchantability, Supplier<Ingredient> repairIngredient) {
        this.itemDurability = itemDurability;
        this.miningSpeedMultiplier = miningSpeedMultiplier;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeedMultiplier;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
