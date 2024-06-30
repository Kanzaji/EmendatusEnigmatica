package com.ridanisaurus.emendatusenigmatica.datagen.provider.sub;

import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EEBlockLootSubProvider extends BlockLootSubProvider {
    protected final Map<Block, LootTable.Builder> blockLootTable = new HashMap<>();

    public EEBlockLootSubProvider(HolderLookup.Provider providers) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), providers);
    }

	//TODO: Finish Json generation
    @Override
    protected void generate() {
		Map<ResourceLocation, LootTable> tables = new HashMap<>();
		for (Map.Entry<Block, LootTable.Builder> entry : this.blockLootTable.entrySet()) {
			tables.put(
				entry.getKey().getLootTable().registry(),
				entry.getValue().setParamSet(LootContextParamSets.BLOCK).build()
			);
		}
		this.writeTables(cache, tables);
    }

    private void writeTables(CachedOutput cache, @NotNull Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getPackOutput().getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                DataProvider.saveStable(cache, LootTable.serialize(lootTable), path);
            } catch (IOException e) {
                EmendatusEnigmatica.LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

	protected @NotNull LootTable.Builder selfDrop(Block block) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(block))
            .when(ExplosionCondition.survivesExplosion())
		);
	}

    // Replaced by vanilla createSilkTouchOnlyTable(Block)
//	protected @NotNull LootTable.Builder dropWhenSilkTouch(Block block) {
//		return LootTable.lootTable().withPool(LootPool.lootPool()
//				.setRolls(ConstantValue.exactly(1))
//                .when(hasSilkTouch())
//				.add(LootItem.lootTableItem(block).apply(ApplyExplosionDecay.explosionDecay()))
//		);
//	}

	protected @NotNull LootTable.Builder dropWhenSilkTouchWithSetCount(Block block, @NotNull Item item, float min, float max) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
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
	}

	protected @NotNull LootTable.Builder oreDrop(Block block, @NotNull Item item) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(block).when(hasSilkTouch()).otherwise(LootItem.lootTableItem(item.asItem())
                .apply(ApplyBonusCount.addOreBonusCount(getEnchantment(Enchantments.FORTUNE)))
                .apply(ApplyExplosionDecay.explosionDecay())
            ))
		);
	}

	protected @NotNull LootTable.Builder oreCountDrop(Block block, @NotNull ItemLike item, UniformGenerator range) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .add(LootItem.lootTableItem(block).when(hasSilkTouch()).otherwise(LootItem.lootTableItem(item.asItem())
                .apply(SetItemCountFunction.setCount(range))
                .apply(ApplyBonusCount.addOreBonusCount(getEnchantment(Enchantments.FORTUNE)))
                .apply(ApplyExplosionDecay.explosionDecay())
            ))
		);
	}

	protected @NotNull LootTable.Builder oreUniformedDrop(Block block, @NotNull ItemLike item, UniformGenerator range) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .add(LootItem.lootTableItem(block).when(hasSilkTouch()).otherwise(LootItem.lootTableItem(item.asItem())
                .apply(SetItemCountFunction.setCount(range))
                .apply(ApplyBonusCount.addUniformBonusCount(getEnchantment(Enchantments.FORTUNE)))
                .apply(ApplyExplosionDecay.explosionDecay())
            ))
		);
	}

    protected final Holder<Enchantment> getEnchantment(ResourceKey<Enchantment> key) {
        return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }
}
