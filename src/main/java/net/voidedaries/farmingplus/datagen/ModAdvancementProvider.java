package net.voidedaries.farmingplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.effect.ModEffects;
import net.voidedaries.farmingplus.item.ModItems;
import net.voidedaries.farmingplus.util.ModTags;

import java.util.Optional;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
        Advancement.Builder.create()
                .display(
                        ModItems.RED_WINE,
                        Text.literal("Tipsy Traveller"),
                        Text.literal("Under the influence and still trying to drive. Good luck!"),
                        new Identifier(FarmingPlus.MOD_ID,"textures/block/loam_block.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("effect_and_vehicle", PlayerInteractedWithEntityCriterion.Conditions
                        .create(Optional.of(EntityPredicate.asLootContextPredicate(EntityPredicate.Builder.create()
                                .effects(EntityEffectPredicate.Builder.create()
                                        .addEffect(ModEffects.INTOXICATION)).build())), ItemPredicate.Builder.create(),
                                Optional.of(EntityPredicate.asLootContextPredicate(EntityPredicate.Builder.create()
                                        .type(ModTags.Entities.IS_VEHICLE).build()))))
                .build(consumer, FarmingPlus.MOD_ID + ":tipsy_traveller");
    }
}