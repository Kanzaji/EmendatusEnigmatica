package com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.sphere;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common.CommonBlockDefinitionModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common.CommonDepositModelBase;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.sample.SampleBlockDefinitionModel;

import java.util.List;

public class SphereDepositModel extends CommonDepositModelBase {
	public static final Codec<SphereDepositModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.STRING.fieldOf("type").forGetter(it -> it.type),
			Codec.STRING.fieldOf("dimension").forGetter(it -> it.dimension),
			Codec.list(Codec.STRING).fieldOf("biomes").orElse(List.of()).forGetter(it -> it.biomes),
			Codec.STRING.fieldOf("registryName").forGetter(it -> it.name),
			SphereDepositConfigModel.CODEC.fieldOf("config").forGetter(it -> it.config)
	).apply(x, SphereDepositModel::new));

	private final SphereDepositConfigModel config;

	public SphereDepositModel(String type, String dimension, List<String> biomes, String name, SphereDepositConfigModel config) {
		super(type, dimension, biomes, name);
		this.config = config;
	}

	public String getType() {
		return super.getType();
	}

	public int getChance() {
		return config.chance;
	}

	public int getPlacementChance() {
		return config.rarity.equals("rare") ? (100 - getChance()) + 1 : getChance();
	}

	public int getMaxYLevel() {
		return config.maxYLevel;
	}

	public int getMinYLevel() {
		return config.minYLevel;
	}

	public List<CommonBlockDefinitionModel> getBlocks() {
		return config.blocks;
	}

	public List<String> getFillerTypes() {
		return config.fillerTypes;
	}

	public int getRadius() {
		return config.radius;
	}

	public String getPlacement() {
		return config.placement;
	}

	public String getRarity() {
		return config.rarity;
	}

	public boolean hasSamples() {
		return config.generateSamples;
	}

	public List<SampleBlockDefinitionModel> getSampleBlocks() {
		return config.sampleBlocks;
	}
}