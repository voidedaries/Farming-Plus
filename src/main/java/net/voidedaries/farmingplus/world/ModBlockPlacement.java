package net.voidedaries.farmingplus.world;

import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModBlockPlacement {


    public static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return List.of(CountPlacementModifier.of(count), SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    public static List<PlacementModifier> modifiersWithoutCount(PlacementModifier heightModifier) {
        return List.of(SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    public static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return List.of(RarityFilterPlacementModifier.of(chance), heightModifier);
    }

    public static List<PlacementModifier> modifiersWithHeightRange(int minY, int maxY) {
        return List.of(CountPlacementModifier.of(1), HeightRangePlacementModifier.uniform(YOffset.fixed(minY), YOffset.fixed(maxY)), BiomePlacementModifier.of());
    }

}
