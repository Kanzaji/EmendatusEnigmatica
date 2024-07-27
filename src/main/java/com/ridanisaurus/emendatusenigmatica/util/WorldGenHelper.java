package com.ridanisaurus.emendatusenigmatica.util;

import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class WorldGenHelper {
	@Contract("_, _ -> new")
	public static @Unmodifiable List<PlacementModifier> orePlacement(PlacementModifier modifier_1, PlacementModifier modifier_2) {
		return List.of(modifier_1, InSquarePlacement.spread(), modifier_2, BiomeFilter.biome());
	}

	@Contract("_, _ -> new")
	public static @Unmodifiable List<PlacementModifier> commonOrePlacement(int chancePerChunk, PlacementModifier modifier) {
		return orePlacement(CountPlacement.of(chancePerChunk), modifier);
	}

	@Contract("_, _ -> new")
	public static @Unmodifiable List<PlacementModifier> rareOrePlacement(int chancePerChunk, PlacementModifier modifier) {
		return orePlacement(RarityFilter.onAverageOnceEvery(chancePerChunk), modifier);
	}

	public static List<PlacementModifier> getOrePlacement(@NotNull String rarity, int placementChance, PlacementModifier modifier) {
		return rarity.equals("common") ?
			commonOrePlacement(placementChance, modifier):
			rareOrePlacement(placementChance, modifier);
	}

	public static HeightRangePlacement getPlacementModifier(@NotNull String placement, int minY, int maxY) {
		return placement.equals("uniform") ?
			HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)) :
			HeightRangePlacement.triangle(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY));
	}
}
