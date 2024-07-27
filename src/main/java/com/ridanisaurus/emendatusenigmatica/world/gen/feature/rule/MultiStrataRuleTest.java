package com.ridanisaurus.emendatusenigmatica.world.gen.feature.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.ArrayList;
import java.util.List;

public class MultiStrataRuleTest extends RuleTest {
	public static final MapCodec<MultiStrataRuleTest> CODEC = RecordCodecBuilder.mapCodec(x -> x.group(
			Codec.list(Codec.STRING).fieldOf("fillerList").forGetter(it -> it.fillerList)
	).apply(x, MultiStrataRuleTest::new));

	public static void register() {
		TYPE = RuleTestType.register("multi_block_test", CODEC);
	}
	public static RuleTestType<MultiStrataRuleTest> TYPE;
	private final List<Block> blockFillerList = new ArrayList<>();
	private List<String> fillerList;

	public MultiStrataRuleTest(List<String> fillerList) {
		this.fillerList = fillerList;
		setup();
	}

	private void setup() {
		for (StrataModel stratum : EmendatusEnigmatica.getInstance().getLoader().getDataRegistry().getStrata()) {
			if (this.fillerList.contains(stratum.getId())) {
				this.blockFillerList.add(BuiltInRegistries.BLOCK.get(stratum.getFillerType()));
			}
		}
	}

	@Override
	public boolean test(BlockState state, RandomSource rand) {
		for (Block block : blockFillerList) {
			if (BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString().equals(BuiltInRegistries.BLOCK.getKey(block).toString())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected RuleTestType<?> getType() {
		return TYPE;
	}
}
