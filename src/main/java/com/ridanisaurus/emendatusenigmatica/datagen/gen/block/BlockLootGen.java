package com.ridanisaurus.emendatusenigmatica.datagen.gen.block;

import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialOreDropModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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

import java.util.List;
import java.util.Set;

public class BlockLootGen extends BlockLootSubProvider {
	private final EmendatusDataRegistry registry;

	public BlockLootGen(HolderLookup.Provider providers, EmendatusDataRegistry registry) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), providers);
		this.registry = registry;
	}

	@Override
	protected void generate() {
		for (MaterialModel material : registry.getMaterials()) {
			List<String> processedType = material.getProcessedTypes();

			// Storage Blocks
			if (processedType.contains("storage_block")) this.selfDrop(EERegistrar.storageBlockMap.get(material));

			// Clusters
			if (processedType.contains("cluster")) {
				// Block
				this.selfDrop(EERegistrar.clusterShardBlockMap.get(material));
				// Small Bud
				this.dropWithSilkTouch(EERegistrar.smallBudBlockMap.get(material));
				// Medium Bud
				this.dropWithSilkTouch(EERegistrar.mediumBudBlockMap.get(material));
				// Large Bud
				this.dropWithSilkTouch(EERegistrar.largeBudBlockMap.get(material));
				// Cluster
				this.dropCluster(EERegistrar.clusterBlockMap.get(material), EERegistrar.clusterShardMap.get(material), 2.0f, 4.0f);
				// Budding
				this.add(EERegistrar.buddingBlockMap.get(material), noDrop());
			}

			// Raw Storage Blocks
			if (processedType.contains("raw")) selfDrop(EERegistrar.rawBlockMap.get(material));

			// Ores
			if (!processedType.contains("ore")) continue;
			MaterialOreDropModel oreDrop = material.getOreDrop();

			for (StrataModel strata : registry.getStrata()) {
				if (!(material.getStrata().isEmpty() || material.getStrata().contains(strata.getId()))) continue;

				Block block = EERegistrar.oreBlockTable.get(strata.getId(), material.getId()).get();
				Item dropItem;
				if (!oreDrop.getDrop().isBlank()) {
					dropItem = oreDrop.getDefaultItemDropAsItem().asItem();
				} else if (material.getProperties().getMaterialType().equals("metal") && processedType.contains("raw")) {
					dropItem = EERegistrar.rawMap.get(material);
				} else if (processedType.contains("gem")) {
					dropItem = EERegistrar.gemMap.get(material);
				} else {
					// Validation shouldn't ever let this happen.
					throw new IllegalStateException("There is no ore drop, raw nor gem specified for the ore drop!");
				}

				if (oreDrop.getMax() == 1) {
					this.oreDrop(block, dropItem);
				} else if (oreDrop.isUniformCount()) {
					this.oreUniformedDrop(block, dropItem, UniformGenerator.between(oreDrop.getMin(), oreDrop.getMax()));
				} else {
					this.oreCountDrop(block, dropItem, UniformGenerator.between(oreDrop.getMin(), oreDrop.getMax()));
				}

				//TODO: Rework Sample System.
//				if (processedType.contains("sample")) {
//					if (oreDrop.getMax() == 1) {
//						oreDrop(EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).get(), (processedType.contains("raw") && oreDrop.getDrop().isEmpty() ? EERegistrar.rawMap.get(material.getId()).get() : oreDrop.getDefaultItemDropAsItem().asItem()));
//					} else {
//						if (oreDrop.isUniformCount()) {
//							oreUniformedDrop(EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).get(),
//							(processedType.contains("raw") && oreDrop.getDrop().isEmpty() ? EERegistrar.rawMap.get(material.getId()).get() : oreDrop.getDefaultItemDropAsItem()),
//							UniformGenerator.between(oreDrop.getMin(), oreDrop.getMax()));
//						}
//						else {
//							oreCountDrop(EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).get(),
//							(processedType.contains("raw") && oreDrop.getDrop().isEmpty() ? EERegistrar.rawMap.get(material.getId()).get() : oreDrop.getDefaultItemDropAsItem()),
//							UniformGenerator.between(oreDrop.getMin(), oreDrop.getMax()));
//						}
//					}
//				}
			}
		}
	}

	@Override
	protected @NotNull Iterable<Block> getKnownBlocks() {
		return BuiltInRegistries.BLOCK
			.stream()
			.filter(entry -> entry.getLootTable().location().getNamespace().equals(Reference.MOD_ID))
			.toList();
	}

	private void selfDrop(Block block) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1))
			.add(LootItem.lootTableItem(block))
			.when(ExplosionCondition.survivesExplosion())
		);
		add(block, table);
	}

	private void dropWithSilkTouch(@NotNull Block block) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1))
			.when(hasSilkTouch())
			.add(LootItem.lootTableItem(block).apply(ApplyExplosionDecay.explosionDecay()))
		);
		add(block, table);
	}

	private void dropCluster(Block block, @NotNull Item item, float min, float max) {
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

	private void oreDrop(Block block, @NotNull Item item) {
		var table = LootTable.lootTable().withPool(LootPool.lootPool()
			.setRolls(ConstantValue.exactly(1))
			.add(LootItem.lootTableItem(block).when(hasSilkTouch()).otherwise(LootItem.lootTableItem(item.asItem())
				.apply(ApplyBonusCount.addOreBonusCount(getEnchantment(Enchantments.FORTUNE)))
				.apply(ApplyExplosionDecay.explosionDecay())
			))
		);
		add(block, table);
	}

	private void oreCountDrop(Block block, @NotNull ItemLike item, UniformGenerator range) {
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

	private void oreUniformedDrop(Block block, @NotNull ItemLike item, UniformGenerator range) {
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
