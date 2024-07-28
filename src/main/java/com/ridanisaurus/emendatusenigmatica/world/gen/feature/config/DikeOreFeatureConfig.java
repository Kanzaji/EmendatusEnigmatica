package com.ridanisaurus.emendatusenigmatica.world.gen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common.CommonBlockDefinitionModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.dike.DikeDepositModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.sample.SampleBlockDefinitionModel;
import com.ridanisaurus.emendatusenigmatica.world.gen.feature.rule.MultiStrataRuleTest;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.ArrayList;

public class DikeOreFeatureConfig implements FeatureConfiguration {
	public static final Codec<DikeOreFeatureConfig> CODEC = RecordCodecBuilder.create((x) -> x.group(
		DikeDepositModel.CODEC.fieldOf("model").forGetter(it -> it.model),
		MultiStrataRuleTest.CODEC.fieldOf("target").forGetter(it -> (MultiStrataRuleTest) it.target)
	).apply(x, DikeOreFeatureConfig::new));

	public final RuleTest target;
	public final DikeDepositModel model;
	public final ArrayList<CommonBlockDefinitionModel> blocks;
	public final ArrayList<SampleBlockDefinitionModel> sampleBlocks;
	public boolean placed = false;

	public DikeOreFeatureConfig(DikeDepositModel model, RuleTest target) {
		this.target = target;
		this.model = model;

		blocks = new ArrayList<>();
		for (CommonBlockDefinitionModel block : model.getBlocks()) {
			NonNullList<CommonBlockDefinitionModel> filled = NonNullList.withSize(block.getWeight(), block);
			blocks.addAll(filled);
		}

		sampleBlocks = new ArrayList<>();
		for (SampleBlockDefinitionModel sampleBlock : model.getSampleBlocks()) {
			NonNullList<SampleBlockDefinitionModel> filled = NonNullList.withSize(sampleBlock.getWeight(), sampleBlock);
			sampleBlocks.addAll(filled);
		}
	}

	public DikeOreFeatureConfig(DikeDepositModel model) {
		this(model, new MultiStrataRuleTest(model.getFillerTypes()));
	}
}
