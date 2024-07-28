package com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.vanilla;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common.CommonDepositModelBase;

import javax.annotation.Nullable;
import java.util.List;

public class VanillaDepositModel extends CommonDepositModelBase {
	public static final Codec<VanillaDepositModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.STRING.fieldOf("type").forGetter(it -> it.type),
			Codec.STRING.fieldOf("dimension").forGetter(it -> it.dimension),
			Codec.list(Codec.STRING).fieldOf("biomes").orElse(List.of()).forGetter(it -> it.biomes),
			Codec.STRING.fieldOf("registryName").forGetter(it -> it.name),
			VanillaDepositConfigModel.CODEC.fieldOf("config").forGetter(it -> it.config)
	).apply(x, VanillaDepositModel::new));

	private final VanillaDepositConfigModel config;

	public VanillaDepositModel(String type, String dimension, List<String> biomes, String name, VanillaDepositConfigModel config) {
		super(type, dimension, biomes, name);
		this.config = config;
	}

	public String getType() {
		return super.getType();
	}

	public int getMinYLevel() {
		return config.minYLevel;
	}

	public int getMaxYLevel() {
		return config.maxYLevel;
	}

	public String getPlacement() {
		return config.placement;
	}

	public String getRarity() {
		return config.rarity;
	}

	public int getChance() {
		return config.chance;
	}

	public int getPlacementChance() {
		return config.rarity.equals("rare") ? (100 - getChance()) + 1 : getChance();
	}

	public int getSize() {
		return config.size;
	}

	public List<String> getFillerTypes() {
		return config.fillerTypes;
	}

	@Nullable
	public String getBlock() {
		return config.block;
	}

	@Nullable
	public String getMaterial() {
		return config.material;
	}
}
