package com.ridanisaurus.emendatusenigmatica.util;

import com.ridanisaurus.emendatusenigmatica.plugin.deposit.DepositType;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.IDepositProcessor;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.processors.*;
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

	public static List<PlacementModifier> getFullOrePlacement(@NotNull IDepositProcessor processor) {
		return switch (processor) {
			case VanillaDepositProcessor p -> 	getOrePlacement(p.getRarity(), p.getChance(), getPlacementModifier(p.getPlacement(), p.getMinY(), p.getMaxY()));
			case DikeDepositProcessor p -> 		getOrePlacement(p.getRarity(), p.getChance(), getPlacementModifier(p.getPlacement(), p.getMinY(), p.getMaxY()));
			case DenseDepositProcessor p -> 	getOrePlacement(p.getRarity(), p.getChance(), getPlacementModifier(p.getPlacement(), p.getMinY(), p.getMaxY()));
			case GeodeDepositProcessor p -> 	getOrePlacement(p.getRarity(), p.getChance(), getPlacementModifier(p.getPlacement(), p.getMinY(), p.getMaxY()));
			case SphereDepositProcessor p -> 	getOrePlacement(p.getRarity(), p.getChance(), getPlacementModifier(p.getPlacement(), p.getMinY(), p.getMaxY()));
			default -> null;
		};
	}
}
