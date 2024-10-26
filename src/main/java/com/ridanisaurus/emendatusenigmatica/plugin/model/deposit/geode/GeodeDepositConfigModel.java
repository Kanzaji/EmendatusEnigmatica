/*
 * MIT License
 *
 * Copyright (c) 2024. Ridanisaurus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.geode;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationManager;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.ArrayPolicy;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.FilterMode;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.Types;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.NumberRangeValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.ResourceLocationValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.TypeValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.ValuesValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.registry.BlockRegistryValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.DefaultLoader;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common.CommonBlockDefinitionModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.sample.SampleBlockDefinitionModel;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.EERegistryValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.FieldTrueValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.MaxValidator;

import java.util.List;

public class GeodeDepositConfigModel {
	public static final Codec<GeodeDepositConfigModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.list(CommonBlockDefinitionModel.CODEC).fieldOf("outerShellBlocks").orElse(List.of()).forGetter(i -> i.outerShellBlocks),
			Codec.list(CommonBlockDefinitionModel.CODEC).fieldOf("innerShellBlocks").orElse(List.of()).forGetter(i -> i.innerShellBlocks),
			Codec.list(CommonBlockDefinitionModel.CODEC).fieldOf("innerBlocks").orElse(List.of()).forGetter(i -> i.innerBlocks),
			Codec.list(CommonBlockDefinitionModel.CODEC).fieldOf("fillBlocks").orElse(List.of()).forGetter(i -> i.fillBlocks),
			Codec.list(Codec.STRING).fieldOf("fillerTypes").orElse(List.of()).forGetter(it -> it.fillerTypes),
			Codec.list(Codec.STRING).fieldOf("clusters").orElse(List.of()).forGetter(i -> i.clusters),
			Codec.INT.fieldOf("chance").orElse(0).forGetter(it -> it.chance),
			Codec.DOUBLE.fieldOf("crackChance").orElse(0D).forGetter(it -> it.crackChance),
			Codec.INT.fieldOf("minYLevel").orElse(0).forGetter(it -> it.minYLevel),
			Codec.INT.fieldOf("maxYLevel").orElse(0).forGetter(it -> it.maxYLevel),
			Codec.STRING.fieldOf("placement").orElse("uniform").forGetter(it -> it.placement),
			Codec.STRING.fieldOf("rarity").orElse("rare").forGetter(it -> it.rarity),
			Codec.BOOL.fieldOf("generateSamples").orElse(false).forGetter(it -> it.generateSamples),
			Codec.list(SampleBlockDefinitionModel.CODEC).fieldOf("sampleBlocks").orElse(List.of()).forGetter(it -> it.sampleBlocks)
	).apply(x, GeodeDepositConfigModel::new));

	public static final ValidationManager VALIDATION_MANAGER = ValidationManager.create()
		.addValidator("outerShellBlocks", CommonBlockDefinitionModel.VALIDATION_MANAGER.getAsValidator(true), ArrayPolicy.REQUIRES_ARRAY)
		.addValidator("innerShellBlocks", CommonBlockDefinitionModel.VALIDATION_MANAGER.getAsValidator(true), ArrayPolicy.REQUIRES_ARRAY)
		.addValidator("innerBlocks",      CommonBlockDefinitionModel.VALIDATION_MANAGER.getAsValidator(true), ArrayPolicy.REQUIRES_ARRAY)
		.addValidator("fillBlocks",       CommonBlockDefinitionModel.VALIDATION_MANAGER.getAsValidator(true), ArrayPolicy.REQUIRES_ARRAY)
		.addValidator("fillerTypes",      new EERegistryValidator(DefaultLoader.STRATA_IDS, EERegistryValidator.REFERENCE, "Strata", true), ArrayPolicy.REQUIRES_ARRAY)
		.addValidator("clusters",         new ResourceLocationValidator(false, new BlockRegistryValidator()), ArrayPolicy.REQUIRES_ARRAY)
		.addValidator("chance",           new NumberRangeValidator(Types.INTEGER, 1, 100, true))
		.addValidator("crackChance",      new NumberRangeValidator(Types.FLOAT, 0, 1, false))
		.addValidator("minYLevel",        new NumberRangeValidator(Types.INTEGER, -64, 320, true))
		.addValidator("maxYLevel",        new MaxValidator(Types.INTEGER, "minYLevel", -64, 320, true))
		.addValidator("placement",        new ValuesValidator(List.of("uniform", "triangle"), FilterMode.WHITELIST, false))
		.addValidator("rarity",           new ValuesValidator(List.of("common", "rare"), FilterMode.WHITELIST, false))
		.addValidator("generateSamples",  new TypeValidator(Types.BOOLEAN, false))
		.addValidator("sampleBlocks",     new FieldTrueValidator("generateSamples", SampleBlockDefinitionModel.VALIDATION_MANAGER.getAsValidator(false)), ArrayPolicy.REQUIRES_ARRAY);

	public final List<CommonBlockDefinitionModel> outerShellBlocks;
	public final List<CommonBlockDefinitionModel> innerShellBlocks;
	public final List<CommonBlockDefinitionModel> innerBlocks;
	public final List<CommonBlockDefinitionModel> fillBlocks;
	public final List<String> fillerTypes;
	public final List<String> clusters;
	public final int chance;
	public final double crackChance;
	public final int minYLevel;
	public final int maxYLevel;
	public final String placement;
	public final String rarity;
	public final boolean generateSamples;
	public final List<SampleBlockDefinitionModel> sampleBlocks;

	public GeodeDepositConfigModel(List<CommonBlockDefinitionModel> outerShellBlocks, List<CommonBlockDefinitionModel> innerShellBlocks, List<CommonBlockDefinitionModel> innerBlocks, List<CommonBlockDefinitionModel> fillBlocks, List<String> fillerTypes, List<String> clusters, int chance, double crackChance, int minYLevel, int maxYLevel, String placement, String rarity, boolean generateSamples, List<SampleBlockDefinitionModel> sampleBlocks) {
		this.outerShellBlocks = outerShellBlocks;
		this.innerShellBlocks = innerShellBlocks;
		this.innerBlocks = innerBlocks;
		this.fillBlocks = fillBlocks;
		this.fillerTypes = fillerTypes;
		this.clusters = clusters;
		this.chance = chance;
		this.crackChance = crackChance;
		this.minYLevel = minYLevel;
		this.maxYLevel = maxYLevel;
		this.placement = placement;
		this.rarity = rarity;
		this.generateSamples = generateSamples;
		this.sampleBlocks = sampleBlocks;
	}
}
