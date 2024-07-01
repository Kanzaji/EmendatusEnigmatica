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

//TODO: Create wrappers of vanilla methods for generating loot table builders.
// All of those are protected, so those are unfortunately necessary.
// Also find a way to get instance of this class as it's needed to add loot tables >.>
public class EEBlockLootSubProvider extends BlockLootSubProvider {
    protected final Map<Block, LootTable.Builder> blockLootTable = new HashMap<>();

    public EEBlockLootSubProvider(HolderLookup.Provider providers) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), providers);
    }

    @Override
    protected void generate() {
		this.blockLootTable.forEach(this::add);
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

    protected final @NotNull Holder<Enchantment> getEnchantment(ResourceKey<Enchantment> key) {
        return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }
}
