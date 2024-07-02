/*
 * MIT License
 *
 * Copyright (c) 2020 Ridanisaurus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software").toString(), to deal
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

package com.ridanisaurus.emendatusenigmatica.datagen.gen;

import com.google.common.collect.Lists;
import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.datagen.provider.EETagProvider;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import com.ridanisaurus.emendatusenigmatica.datagen.builder.TagBuilder;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.ridanisaurus.emendatusenigmatica.util.Reference.COMMON;
import static com.ridanisaurus.emendatusenigmatica.util.Reference.C_TAG;

public class ItemTagsGen extends EETagProvider {

	private final EmendatusDataRegistry registry;

	public ItemTagsGen(DataGenerator gen, EmendatusDataRegistry registry) {
		super(gen);
		this.registry = registry;
	}

	private final List<String> forgeBlocks = Lists.newArrayList();
	private final List<String> forgeIngots = Lists.newArrayList();
	private final List<String> forgeGems = Lists.newArrayList();
	private final List<String> forgeNuggets = Lists.newArrayList();
	private final List<String> forgeDusts = Lists.newArrayList();
	private final List<String> forgePlates = Lists.newArrayList();
	private final List<String> forgeGears = Lists.newArrayList();
	private final List<String> forgeRods = Lists.newArrayList();
	private final List<String> forgeRaw = Lists.newArrayList();
	private final List<String> forgeBuckets = Lists.newArrayList();
	private final List<String> forgeOres = Lists.newArrayList();
	private final List<String> forgeTools = Lists.newArrayList();
	private final List<String> forgeSwords = Lists.newArrayList();
	private final List<String> forgePickaxes = Lists.newArrayList();
	private final List<String> forgeAxes = Lists.newArrayList();
	private final List<String> forgeShovels = Lists.newArrayList();
	private final List<String> forgeHoes = Lists.newArrayList();
	private final List<String> forgePaxels = Lists.newArrayList();
	private final List<String> forgeShields = Lists.newArrayList();
	private final List<String> forgeHelmets = Lists.newArrayList();
	private final List<String> forgeChestplates = Lists.newArrayList();
	private final List<String> forgeLeggings = Lists.newArrayList();
	private final List<String> forgeBoots = Lists.newArrayList();
	private final Map<String, List<String>> oresPerMaterial = new HashMap<>();
	private final Map<String, List<String>> oresInGround = new HashMap<>();

	private final List<String> beaconIngots = Lists.newArrayList();

	@Override
	protected void buildTags(Consumer<IFinishedGenericJSON> consumer) {
		for (MaterialModel material : registry.getMaterials()) {
			List<String> processedType = material.getProcessedTypes();
			// Storage Blocks
			if (processedType.contains("storage_block")) {
				ResourceLocation block = EERegistrar.storageBlockItemMap.get(material.getId()).getId();
				if (!forgeBlocks.contains(C_TAG + ":storage_blocks/" + material.getId())) forgeBlocks.add(C_TAG + ":storage_blocks/" + material.getId());
				new TagBuilder().tag(block.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/storage_blocks/" + material.getId()));
			}
			// Ingots
			if (processedType.contains("ingot")) {
				ResourceLocation ingot = EERegistrar.ingotMap.get(material.getId()).getId();
				if (!forgeIngots.contains(C_TAG + ":ingots/" + material.getId())) forgeIngots.add(C_TAG + ":ingots/" + material.getId());
				beaconIngots.add(ingot.toString());
				new TagBuilder().tag(ingot.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/ingots/" + material.getId()));
			}
			// Gems
			if (processedType.contains("gem")) {
				ResourceLocation gem = EERegistrar.gemMap.get(material.getId()).getId();
				if (!forgeGems.contains(C_TAG + ":gems/" + material.getId())) forgeGems.add(C_TAG + ":gems/" + material.getId());
				new TagBuilder().tag(gem.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/gems/" + material.getId()));
			}
			// Nuggets
			if (processedType.contains("nugget")) {
				ResourceLocation nugget = EERegistrar.nuggetMap.get(material.getId()).getId();
				if (!forgeNuggets.contains(C_TAG + ":nuggets/" + material.getId())) forgeNuggets.add(C_TAG + ":nuggets/" + material.getId());
				new TagBuilder().tag(nugget.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/nuggets/" + material.getId()));
			}
			// Dusts
			if (processedType.contains("dust")) {
				ResourceLocation dust = EERegistrar.dustMap.get(material.getId()).getId();
				if (!forgeDusts.contains(C_TAG + ":dusts/" + material.getId())) forgeDusts.add(C_TAG + ":dusts/" + material.getId());
				new TagBuilder().tag(dust.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/dusts/" + material.getId()));
			}
			// Plates
			if (processedType.contains("plate")) {
				ResourceLocation plate = EERegistrar.plateMap.get(material.getId()).getId();
				if (!forgePlates.contains(C_TAG + ":plates/" + material.getId())) forgePlates.add(C_TAG + ":plates/" + material.getId());
				new TagBuilder().tag(plate.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/plates/" + material.getId()));
			}
			// Gears
			if (processedType.contains("gear")) {
				ResourceLocation gear = EERegistrar.gearMap.get(material.getId()).getId();
				if (!forgeGears.contains(C_TAG + ":gears/" + material.getId())) forgeGears.add(C_TAG + ":gears/" + material.getId());
				new TagBuilder().tag(gear.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/gears/" + material.getId()));
			}
			// Rods
			if (processedType.contains("rod")) {
				ResourceLocation rod = EERegistrar.rodMap.get(material.getId()).getId();
				if (!forgeRods.contains(C_TAG + ":rods/" + material.getId())) forgeRods.add(C_TAG + ":rods/" + material.getId());
				new TagBuilder().tag(rod.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/rods/" + material.getId()));
			}
			// Raw Materials
			if (processedType.contains("raw")) {
				ResourceLocation raw = EERegistrar.rawMap.get(material.getId()).getId();
				ResourceLocation rawBlock = EERegistrar.rawBlockItemMap.get(material.getId()).getId();
				if (!forgeRaw.contains(C_TAG + ":raw_materials/" + material.getId())) forgeRaw.add(C_TAG + ":raw_materials/" + material.getId());
				if (!forgeBlocks.contains(C_TAG + ":storage_blocks/raw_" + material.getId())) forgeBlocks.add(C_TAG + ":storage_blocks/raw_" + material.getId());
				new TagBuilder().tag(raw.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/raw_materials/" + material.getId()));
				new TagBuilder().tag(rawBlock.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/storage_blocks/raw_" + material.getId()));
			}
			// Tools
			if (processedType.contains("sword")) {
				ResourceLocation sword = EERegistrar.swordMap.get(material.getId()).getId();
				if (!forgeSwords.contains(C_TAG + ":tools/swords/" + material.getId())) forgeSwords.add(C_TAG + ":tools/swords/" + material.getId());
				new TagBuilder().tag(sword.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/swords/" + material.getId()));
			}
			if (processedType.contains("pickaxe")) {
				ResourceLocation pickaxe = EERegistrar.pickaxeMap.get(material.getId()).getId();
				if (!forgePickaxes.contains(C_TAG + ":tools/pickaxes/" + material.getId())) forgePickaxes.add(C_TAG + ":tools/pickaxes/" + material.getId());
				new TagBuilder().tag(pickaxe.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/pickaxes/" + material.getId()));
			}
			if (processedType.contains("axe")) {
				ResourceLocation axe = EERegistrar.axeMap.get(material.getId()).getId();
				if (!forgeAxes.contains(C_TAG + ":tools/axes/" + material.getId())) forgeAxes.add(C_TAG + ":tools/axes/" + material.getId());
				new TagBuilder().tag(axe.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/axes/" + material.getId()));
			}
			if (processedType.contains("shovel")) {
				ResourceLocation shovel = EERegistrar.shovelMap.get(material.getId()).getId();
				if (!forgeShovels.contains(C_TAG + ":tools/shovels/" + material.getId())) forgeShovels.add(C_TAG + ":tools/shovels/" + material.getId());
				new TagBuilder().tag(shovel.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/shovels/" + material.getId()));
			}
			if (processedType.contains("hoe")) {
				ResourceLocation hoe = EERegistrar.hoeMap.get(material.getId()).getId();
				if (!forgeHoes.contains(C_TAG + ":tools/hoes/" + material.getId())) forgeHoes.add(C_TAG + ":tools/hoes/" + material.getId());
				new TagBuilder().tag(hoe.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/hoes/" + material.getId()));
			}
			if (processedType.contains("paxel")) {
				ResourceLocation paxel = EERegistrar.paxelMap.get(material.getId()).getId();
				if (!forgeTools.contains(C_TAG + ":tools/paxels")) forgeTools.add(C_TAG + ":tools/paxels");
				if (!forgePaxels.contains(C_TAG + ":tools/paxels/" + material.getId())) forgePaxels.add(C_TAG + ":tools/paxels/" + material.getId());
				new TagBuilder().tag(paxel.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/paxels/" + material.getId()));
			}
			if (processedType.contains("shield")) {
				ResourceLocation shield = EERegistrar.shieldMap.get(material.getId()).getId();
				if (!forgeShields.contains(C_TAG + ":tools/shields/" + material.getId())) forgeShields.add(C_TAG + ":tools/shields/" + material.getId());
				new TagBuilder().tag(shield.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/shields/" + material.getId()));
			}
			// Armor
			if (processedType.contains("helmet")) {
				ResourceLocation helmet = EERegistrar.helmetMap.get(material.getId()).getId();
				if (!forgeHelmets.contains(C_TAG + ":armors/helmets/" + material.getId())) forgeHelmets.add(C_TAG + ":armors/helmets/" + material.getId());
				new TagBuilder().tag(helmet.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/armors/helmets/" + material.getId()));
			}
			if (processedType.contains("chestplate")) {
				ResourceLocation chestplate = EERegistrar.chestplateMap.get(material.getId()).getId();
				if (!forgeChestplates.contains(C_TAG + ":armors/chestplates/" + material.getId())) forgeChestplates.add(C_TAG + ":armors/chestplates/" + material.getId());
				new TagBuilder().tag(chestplate.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/armors/chestplates/" + material.getId()));
			}
			if (processedType.contains("leggings")) {
				ResourceLocation leggings = EERegistrar.leggingsMap.get(material.getId()).getId();
				if (!forgeLeggings.contains(C_TAG + ":armors/leggings/" + material.getId())) forgeLeggings.add(C_TAG + ":armors/leggings/" + material.getId());
				new TagBuilder().tag(leggings.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/armors/leggings/" + material.getId()));
			}
			if (processedType.contains("boots")) {
				ResourceLocation boots = EERegistrar.bootsMap.get(material.getId()).getId();
				if (!forgeBoots.contains(C_TAG + ":armors/boots/" + material.getId())) forgeBoots.add(C_TAG + ":armors/boots/" + material.getId());
				new TagBuilder().tag(boots.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/armors/boots/" + material.getId()));
			}
			// Buckets
			if (processedType.contains("fluid")) {
				ResourceLocation bucket = EERegistrar.fluidBucketMap.get(material.getId()).getId();
				if (!forgeBuckets.contains(C_TAG + ":buckets/" + material.getId())) forgeBuckets.add(C_TAG + ":buckets/" + material.getId());
				new TagBuilder().tag(bucket.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/buckets/" + material.getId()));
			}
			// Ores
			for (StrataModel strata : registry.getStrata()) {
				if (processedType.contains("ore")) {
//					ResourceLocation ore = ResourceLocation.fromNamespaceAndPath()("minecraft:stone");
					if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
						ResourceLocation ore = EERegistrar.oreBlockItemTable.get(strata.getId(), material.getId()).getId();
						if (!forgeOres.contains(C_TAG + ":ores/" + material.getId())) forgeOres.add(C_TAG + ":ores/" + material.getId());
						oresPerMaterial.computeIfAbsent(material.getId(), s -> new ArrayList<>()).add(ore.toString());
						oresInGround.computeIfAbsent(strata.getSuffix(), s -> new ArrayList<>()).add(ore.toString());
					}

					if (processedType.contains("sample")) {
//						ResourceLocation sample = ResourceLocation.fromNamespaceAndPath()("minecraft:stone");
						if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
							ResourceLocation sample = EERegistrar.oreSampleBlockItemTable.get(strata.getId(), material.getId()).getId();
							if (!forgeOres.contains(C_TAG + ":ores/" + material.getId())) forgeOres.add(C_TAG + ":ores/" + material.getId());
							oresPerMaterial.computeIfAbsent(material.getId(), s -> new ArrayList<>()).add(sample.toString());
							oresInGround.computeIfAbsent(strata.getSuffix(), s -> new ArrayList<>()).add(sample.toString());
						}
					}
				}
			}
		}
		if (!oresPerMaterial.isEmpty())	oresPerMaterial.forEach((material, oreList) -> new TagBuilder().tags(oreList).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/ores/" + material)));
		if (!oresInGround.isEmpty()) oresInGround.forEach((strataPrefix, oreType) -> new TagBuilder().tags(oreType).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/ores_in_ground/" + strataPrefix)));

		if (!forgeBlocks.isEmpty()) new TagBuilder().tags(forgeBlocks).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/storage_blocks"));
		if (!forgeIngots.isEmpty()) new TagBuilder().tags(forgeIngots).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/ingots"));
		if (!forgeGems.isEmpty()) new TagBuilder().tags(forgeGems).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/gems"));
		if (!forgeNuggets.isEmpty()) new TagBuilder().tags(forgeNuggets).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/nuggets"));
		if (!forgeDusts.isEmpty()) new TagBuilder().tags(forgeDusts).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/dusts"));
		if (!forgePlates.isEmpty()) new TagBuilder().tags(forgePlates).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/plates"));
		if (!forgeGears.isEmpty()) new TagBuilder().tags(forgeGears).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/gears"));
		if (!forgeRods.isEmpty()) new TagBuilder().tags(forgeRods).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/rods"));
		if (!forgeRaw.isEmpty()) new TagBuilder().tags(forgeRaw).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/raw_materials"));
		if (!forgeSwords.isEmpty()) new TagBuilder().tags(forgeSwords).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/swords"));
		if (!forgePickaxes.isEmpty()) new TagBuilder().tags(forgePickaxes).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/pickaxes"));
		if (!forgeAxes.isEmpty()) new TagBuilder().tags(forgeAxes).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/axes"));
		if (!forgeShovels.isEmpty()) new TagBuilder().tags(forgeShovels).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/shovels"));
		if (!forgeHoes.isEmpty()) new TagBuilder().tags(forgeHoes).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/hoes"));
		if (!forgePaxels.isEmpty()) new TagBuilder().tags(forgePaxels).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools/paxels"));
		if (!forgeTools.isEmpty()) new TagBuilder().tags(forgeTools).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/tools"));
		if (!forgeHelmets.isEmpty()) new TagBuilder().tags(forgeHelmets).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/armors/helmets"));
		if (!forgeChestplates.isEmpty()) new TagBuilder().tags(forgeChestplates).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/armors/chestplates"));
		if (!forgeLeggings.isEmpty()) new TagBuilder().tags(forgeLeggings).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/armors/leggings"));
		if (!forgeBoots.isEmpty()) new TagBuilder().tags(forgeBoots).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/armors/boots"));
		if (!forgeBuckets.isEmpty()) new TagBuilder().tags(forgeBuckets).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/buckets"));
		if (!forgeOres.isEmpty()) new TagBuilder().tags(forgeOres).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/item/ores"));

		if (!beaconIngots.isEmpty()) new TagBuilder().tags(beaconIngots).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "/item/beacon_payment_items"));

		// TODO: Add Tag list check for Addons
	}

	@Override
	public String getName() {
		return "Emendatus Enigmatica Item Tags";
	}
}