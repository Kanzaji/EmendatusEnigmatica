package com.ridanisaurus.emendatusenigmatica.datagen.provider.sub;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

//TODO: The best option would be to integrate com.ridanisaurus.emendatusenigmatica.datagen.gen.LootTablesGen in here.
public class EEBlockLootSubProvider extends BlockLootSubProvider {
	public EEBlockLootSubProvider(HolderLookup.Provider providers) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), providers);
	}

	@Override
	protected void generate() {
	}

	private void addSelfDrop(Block block) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1))
			.add(LootItem.lootTableItem(block))
			.when(ExplosionCondition.survivesExplosion())
		);
		add(block, table);
	}

	private void addDropWithSilkTouch(@NotNull Block block) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1))
			.when(hasSilkTouch())
			.add(LootItem.lootTableItem(block).apply(ApplyExplosionDecay.explosionDecay()))
		);
		add(block, table);
	}

	private void addDropCluster(Block block, @NotNull Item item, float min, float max) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1.0F))
			.when(hasSilkTouch())
			.add(LootItem.lootTableItem(block)
				.otherwise(LootItem.lootTableItem(item.asItem())
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(max)))
					.apply(ApplyBonusCount.addOreBonusCount(getEnchantment(Enchantments.FORTUNE)))
					.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
					.otherwise(LootItem.lootTableItem(item.asItem()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(min))))
				)
			)
		);
		add(block, table);
	}

	private void addOreDrop(Block block, @NotNull Item item) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1))
			.add(LootItem.lootTableItem(block).when(hasSilkTouch()).otherwise(LootItem.lootTableItem(item.asItem())
				.apply(ApplyBonusCount.addOreBonusCount(getEnchantment(Enchantments.FORTUNE)))
				.apply(ApplyExplosionDecay.explosionDecay())
			))
		);
		add(block, table);
	}

	private void addOreCountDrop(Block block, @NotNull ItemLike item, UniformGenerator range) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1.0F))
			.add(LootItem.lootTableItem(block).when(hasSilkTouch()).otherwise(LootItem.lootTableItem(item.asItem())
				.apply(SetItemCountFunction.setCount(range))
				.apply(ApplyBonusCount.addOreBonusCount(getEnchantment(Enchantments.FORTUNE)))
				.apply(ApplyExplosionDecay.explosionDecay())
			))
		);
		add(block, table);
	}

	private void addOreUniformedDrop(Block block, @NotNull ItemLike item, UniformGenerator range) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1.0F))
			.add(LootItem.lootTableItem(block).when(hasSilkTouch()).otherwise(LootItem.lootTableItem(item.asItem())
				.apply(SetItemCountFunction.setCount(range))
				.apply(ApplyBonusCount.addUniformBonusCount(getEnchantment(Enchantments.FORTUNE)))
				.apply(ApplyExplosionDecay.explosionDecay())
			))
		);
		add(block, table);
	}

	private @NotNull Holder<Enchantment> getEnchantment(ResourceKey<Enchantment> key) {
		return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
	}
}
