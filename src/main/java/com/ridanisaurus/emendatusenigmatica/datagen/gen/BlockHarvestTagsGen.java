/*
 *  MIT License
 *
 *  Copyright (c) 2020 Ridanisaurus
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.ridanisaurus.emendatusenigmatica.datagen.gen;

import com.google.common.collect.Lists;
import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.datagen.provider.EETagProvider;
import com.ridanisaurus.emendatusenigmatica.datagen.builder.TagBuilder;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class BlockHarvestTagsGen {

	public static class BlockHarvestLevelTagsGen extends EETagProvider {
		private final EmendatusDataRegistry registry;

		public BlockHarvestLevelTagsGen(DataGenerator gen, EmendatusDataRegistry registry) {
			super(gen);
			this.registry = registry;
		}

		private final List<String> woodTool = Lists.newArrayList();
		private final List<String> stoneTool = Lists.newArrayList();
		private final List<String> ironTool = Lists.newArrayList();
		private final List<String> diamondTool = Lists.newArrayList();
		private final List<String> netheriteTool = Lists.newArrayList();

		@Override
		protected void buildTags(Consumer<IFinishedGenericJSON> consumer) {
			for (MaterialModel material : registry.getMaterials()) {
				List<String> processedType = material.getProcessedTypes();
				if (processedType.contains("storage_block")) {
					ResourceLocation block = EERegistrar.storageBlockMap.get(material.getId()).getId();
					harvestLevelSwitch(material, block);
					if (material.getProperties().hasOxidization()) {
						ResourceLocation exposedBlock = EERegistrar.exposedBlockMap.get(material.getId()).getId();
						harvestLevelSwitch(material, exposedBlock);
						ResourceLocation weatheredBlock = EERegistrar.weatheredBlockMap.get(material.getId()).getId();
						harvestLevelSwitch(material, weatheredBlock);
						ResourceLocation oxidizedBlock = EERegistrar.oxidizedBlockMap.get(material.getId()).getId();
						harvestLevelSwitch(material, oxidizedBlock);
						ResourceLocation waxedBlock = EERegistrar.waxedStorageBlockMap.get(material.getId()).getId();
						harvestLevelSwitch(material, waxedBlock);
						ResourceLocation waxedExposedBlock = EERegistrar.waxedExposedBlockMap.get(material.getId()).getId();
						harvestLevelSwitch(material, waxedExposedBlock);
						ResourceLocation waxedWeatheredBlock = EERegistrar.waxedWeatheredBlockMap.get(material.getId()).getId();
						harvestLevelSwitch(material, waxedWeatheredBlock);
						ResourceLocation waxedOxidizedBlock = EERegistrar.waxedOxidizedBlockMap.get(material.getId()).getId();
						harvestLevelSwitch(material, waxedOxidizedBlock);
					}
				}
				if (processedType.contains("raw")) {
					ResourceLocation raw = EERegistrar.rawBlockMap.get(material.getId()).getId();
					harvestLevelSwitch(material, raw);
				}
				if (processedType.contains("cluster")) {
					ResourceLocation budding = EERegistrar.buddingBlockMap.get(material.getId()).getId();
					harvestLevelSwitch(material, budding);
					ResourceLocation smallBud = EERegistrar.smallBudBlockMap.get(material.getId()).getId();
					harvestLevelSwitch(material, smallBud);
					ResourceLocation mediumBud = EERegistrar.mediumBudBlockMap.get(material.getId()).getId();
					harvestLevelSwitch(material, mediumBud);
					ResourceLocation largeBud = EERegistrar.largeBudBlockMap.get(material.getId()).getId();
					harvestLevelSwitch(material, largeBud);
					ResourceLocation clusterBlock = EERegistrar.clusterBlockMap.get(material.getId()).getId();
					harvestLevelSwitch(material, clusterBlock);
					ResourceLocation clusterShardBlock = EERegistrar.clusterShardBlockMap.get(material.getId()).getId();
					harvestLevelSwitch(material, clusterShardBlock);
				}
				for (StrataModel strata : registry.getStrata()) {
					if (processedType.contains("ore")) {
						ResourceLocation ore = ResourceLocation.parse("minecraft:stone");
						if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
							ore = EERegistrar.oreBlockTable.get(strata.getId(), material.getId()).getId();
						}
						harvestLevelSwitch(material, ore);
						if (processedType.contains("sample")) {
							ResourceLocation sample = ResourceLocation.parse("minecraft:stone");
							if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
								sample = EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).getId();
							}
							harvestLevelSwitch(material, sample);
						}
					}
				}
			}
			if (!woodTool.isEmpty()) new TagBuilder().tags(woodTool).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.NEOFORGE, "/blocks/needs_wood_tool"));
			if (!stoneTool.isEmpty()) new TagBuilder().tags(stoneTool).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "/blocks/needs_stone_tool"));
			if (!ironTool.isEmpty()) new TagBuilder().tags(ironTool).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "/blocks/needs_iron_tool"));
			if (!diamondTool.isEmpty()) new TagBuilder().tags(diamondTool).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "/blocks/needs_diamond_tool"));
			if (!netheriteTool.isEmpty()) new TagBuilder().tags(netheriteTool).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.NEOFORGE, "/blocks/needs_netherite_tool"));
		}

		private void harvestLevelSwitch(MaterialModel material, ResourceLocation loc) {
			switch (material.getProperties().getHarvestLevel()) {
				case 0 -> woodTool.add(loc.toString());
				case 1 -> stoneTool.add(loc.toString());
				case 2 -> ironTool.add(loc.toString());
				case 3 -> diamondTool.add(loc.toString());
				case 4 -> netheriteTool.add(loc.toString());
				default -> throw new IllegalStateException("Harvest level " + material.getProperties().getHarvestLevel() + " for " + material.getId() + " is out of Vanilla tier system bounds, and the tag should be added manually");
			}
		}

		@Override
		public String getName() {
			return "Emendatus Enigmatica Block Harvest Level Tags";
		}
	}

	public static class BlockHarvestToolTagsGen extends EETagProvider {
		private final EmendatusDataRegistry registry;

		public BlockHarvestToolTagsGen(DataGenerator gen, EmendatusDataRegistry registry) {
			super(gen);
			this.registry = registry;
		}

		private final List<String> shovel = Lists.newArrayList();
		private final List<String> hoe = Lists.newArrayList();
		private final List<String> axe = Lists.newArrayList();
		private final List<String> pickaxe = Lists.newArrayList();
		private final List<String> paxel = Lists.newArrayList();

		@Override
		protected void buildTags(Consumer<IFinishedGenericJSON> consumer) {
			for (MaterialModel material : registry.getMaterials()) {
				List<String> processedType = material.getProcessedTypes();
				if(processedType.contains("storage_block")) {
					ResourceLocation block = EERegistrar.storageBlockMap.get(material.getId()).getId();
					pickaxe.add(block.toString());
					if (material.getProperties().hasOxidization()) {
						ResourceLocation exposedBlock = EERegistrar.exposedBlockMap.get(material.getId()).getId();
						pickaxe.add(exposedBlock.toString());
						ResourceLocation weatheredBlock = EERegistrar.weatheredBlockMap.get(material.getId()).getId();
						pickaxe.add(weatheredBlock.toString());
						ResourceLocation oxidizedBlock = EERegistrar.oxidizedBlockMap.get(material.getId()).getId();
						pickaxe.add(oxidizedBlock.toString());
						ResourceLocation waxedBlock = EERegistrar.waxedStorageBlockMap.get(material.getId()).getId();
						pickaxe.add(waxedBlock.toString());
						ResourceLocation waxedExposedBlock = EERegistrar.waxedExposedBlockMap.get(material.getId()).getId();
						pickaxe.add(waxedExposedBlock.toString());
						ResourceLocation waxedWeatheredBlock = EERegistrar.waxedWeatheredBlockMap.get(material.getId()).getId();
						pickaxe.add(waxedWeatheredBlock.toString());
						ResourceLocation waxedOxidizedBlock = EERegistrar.waxedOxidizedBlockMap.get(material.getId()).getId();
						pickaxe.add(waxedOxidizedBlock.toString());
					}
				}
				if(processedType.contains("raw")) {
					ResourceLocation raw = EERegistrar.rawBlockMap.get(material.getId()).getId();
					pickaxe.add(raw.toString());
				}
				if (processedType.contains("cluster")) {
					ResourceLocation budding = EERegistrar.buddingBlockMap.get(material.getId()).getId();
					pickaxe.add(budding.toString());
					ResourceLocation smallBud = EERegistrar.smallBudBlockMap.get(material.getId()).getId();
					pickaxe.add(smallBud.toString());
					ResourceLocation mediumBud = EERegistrar.mediumBudBlockMap.get(material.getId()).getId();
					pickaxe.add(mediumBud.toString());
					ResourceLocation largeBud = EERegistrar.largeBudBlockMap.get(material.getId()).getId();
					pickaxe.add(largeBud.toString());
					ResourceLocation clusterBlock = EERegistrar.clusterBlockMap.get(material.getId()).getId();
					pickaxe.add(clusterBlock.toString());
					ResourceLocation clusterShardBlock = EERegistrar.clusterShardBlockMap.get(material.getId()).getId();
					pickaxe.add(clusterShardBlock.toString());
				}
				for (StrataModel strata : registry.getStrata()) {
					if(processedType.contains("ore")) {
//						ResourceLocation ore = ResourceLocation.parse("minecraft:stone");
						if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
							ResourceLocation ore = EERegistrar.oreBlockTable.get(strata.getId(), material.getId()).getId();
							switch (strata.getHarvestTool()) {
								case "shovel" -> shovel.add(ore.toString());
								case "hoe" -> hoe.add(ore.toString());
								case "axe" -> axe.add(ore.toString());
								case "pickaxe" -> pickaxe.add(ore.toString());
								default -> throw new IllegalStateException("Harvest tool " + strata.getHarvestTool() + " for " + strata.getId() + " is out of Vanilla tool system bounds, and the tag should be added manually");
							}
						}

						if(processedType.contains("sample")) {
//							ResourceLocation sample = ResourceLocation.parse("minecraft:stone");
							if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
								ResourceLocation sample = EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).getId();
								switch (strata.getHarvestTool()) {
									case "shovel" -> shovel.add(sample.toString());
									case "hoe" -> hoe.add(sample.toString());
									case "axe" -> axe.add(sample.toString());
									case "pickaxe" -> pickaxe.add(sample.toString());
									default -> throw new IllegalStateException("Harvest tool " + strata.getHarvestTool() + " for " + strata.getId() + " is out of Vanilla tool system bounds, and the tag should be added manually");
								}
							}
						}
					}
				}
			}
			if (!shovel.isEmpty()) new TagBuilder().tags(shovel).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "/blocks/mineable/shovel"));
			if (!hoe.isEmpty()) new TagBuilder().tags(hoe).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "/blocks/mineable/hoe"));
			if (!axe.isEmpty()) new TagBuilder().tags(axe).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "/blocks/mineable/axe"));
			if (!pickaxe.isEmpty()) new TagBuilder().tags(pickaxe).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "/blocks/mineable/pickaxe"));
			paxel.add("#minecraft:mineable/axe");
			paxel.add("#minecraft:mineable/pickaxe");
			paxel.add("#minecraft:mineable/shovel");
			new TagBuilder().tags(paxel).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.NEOFORGE, "/blocks/mineable/paxel"));
		}

		@Override
		public String getName() {
			return "Emendatus Enigmatica Block Harvest Tool Tags";
		}
	}
}