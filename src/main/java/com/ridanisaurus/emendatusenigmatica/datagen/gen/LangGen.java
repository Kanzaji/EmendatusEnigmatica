/*
 * MIT License
 *
 * Copyright (c) 2020 Ridanisaurus
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

package com.ridanisaurus.emendatusenigmatica.datagen.gen;

import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.datagen.provider.EELangProvider;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.data.DataGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LangGen extends EELangProvider {
	private final EmendatusDataRegistry registry;

	public LangGen(DataGenerator gen, EmendatusDataRegistry registry) {
		super(gen, Reference.MOD_ID, "en_us");
		this.registry = registry;
	}

	@Override
	protected void addTranslations() {
		// Item Group
//		add("itemGroup." + Reference.MOD_ID, Reference.MOD_NAME);
//
//		add(EERegistrar.ENIGMATIC_HAMMER.get(), "Enigmatic Hammer");
//		add(EERegistrar.FELINIUM_JAMINITE.get(), "Felinium Jaminite Ingot");

		for (MaterialModel material : registry.getMaterials()) {
			List<String> processedType = material.getProcessedTypes();
			// Storage Blocks
			if (processedType.contains("storage_block")) {
				StringBuilder sb = new StringBuilder();
				sb.append("Block of ");
				sb.append(material.getLocalizedName());
				add(EERegistrar.storageBlockMap.getValue(material), sb.toString());
				if (material.getProperties().hasOxidization()) {
					StringBuilder sb2 = new StringBuilder();
					sb2.append("Exposed ");
					sb2.append(material.getLocalizedName());
					add(EERegistrar.exposedBlockMap.getValue(material), sb2.toString());
					StringBuilder sb3 = new StringBuilder();
					sb3.append("Weathered ");
					sb3.append(material.getLocalizedName());
					add(EERegistrar.weatheredBlockMap.getValue(material), sb3.toString());
					StringBuilder sb4 = new StringBuilder();
					sb4.append("Oxidized ");
					sb4.append(material.getLocalizedName());
					add(EERegistrar.oxidizedBlockMap.getValue(material), sb4.toString());
					StringBuilder sb5 = new StringBuilder();
					sb5.append("Waxed Block of ");
					sb5.append(material.getLocalizedName());
					add(EERegistrar.waxedStorageBlockMap.getValue(material), sb5.toString());
					StringBuilder sb6 = new StringBuilder();
					sb6.append("Waxed Exposed ");
					sb6.append(material.getLocalizedName());
					add(EERegistrar.waxedExposedBlockMap.getValue(material), sb6.toString());
					StringBuilder sb7 = new StringBuilder();
					sb7.append("Waxed Weathered ");
					sb7.append(material.getLocalizedName());
					add(EERegistrar.waxedWeatheredBlockMap.getValue(material), sb7.toString());
					StringBuilder sb8 = new StringBuilder();
					sb8.append("Waxed Oxidized ");
					sb8.append(material.getLocalizedName());
					add(EERegistrar.waxedOxidizedBlockMap.getValue(material), sb8.toString());
				}
			}

			// Ingots
			if (processedType.contains("ingot")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Ingot");
				add(EERegistrar.ingotMap.getValue(material), sb.toString());
			}

			// Gems
			if (processedType.contains("gem")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Gem");
				add(EERegistrar.gemMap.getValue(material), sb.toString());
			}

			// Nuggets
			if (processedType.contains("nugget")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Nugget");
				add(EERegistrar.nuggetMap.getValue(material), sb.toString());
			}

			// Dusts
			if (processedType.contains("dust")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Dust");
				add(EERegistrar.dustMap.getValue(material), sb.toString());
			}

			// Plates
			if (processedType.contains("plate")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Plate");
				add(EERegistrar.plateMap.getValue(material), sb.toString());
			}

			// Gears
			if (processedType.contains("gear")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Gear");
				add(EERegistrar.gearMap.getValue(material), sb.toString());
			}

			// Rods
			if (processedType.contains("rod")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Rod");
				add(EERegistrar.rodMap.getValue(material), sb.toString());
			}

			// Swords
			if (processedType.contains("sword")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Sword");
				add(EERegistrar.swordMap.getValue(material), sb.toString());
			}

			// Pickaxes
			if (processedType.contains("pickaxe")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Pickaxe");
				add(EERegistrar.pickaxeMap.getValue(material), sb.toString());
			}

			// Axes
			if (processedType.contains("axe")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Axe");
				add(EERegistrar.axeMap.getValue(material), sb.toString());
			}

			// Shovels
			if (processedType.contains("shovel")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Shovel");
				add(EERegistrar.shovelMap.getValue(material), sb.toString());
			}

			// Hoes
			if (processedType.contains("hoe")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Hoe");
				add(EERegistrar.hoeMap.getValue(material), sb.toString());
			}

			// Paxels
			if (processedType.contains("paxel")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Paxel");
				add(EERegistrar.paxelMap.getValue(material), sb.toString());
			}

			// Helmet
			if (processedType.contains("helmet")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Helmet");
				add(EERegistrar.helmetMap.getValue(material), sb.toString());
			}

			// Chestplate
			if (processedType.contains("chestplate")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Chestplate");
				add(EERegistrar.chestplateMap.getValue(material), sb.toString());
			}

			// Leggings
			if (processedType.contains("leggings")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Leggings");
				add(EERegistrar.leggingsMap.getValue(material), sb.toString());
			}

			// Boots
			if (processedType.contains("boots")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Boots");
				add(EERegistrar.bootsMap.getValue(material), sb.toString());
			}

			// Shields
			if (processedType.contains("shield")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Shield");
				add(EERegistrar.shieldMap.getValue(material), sb.toString());
			}

			// Raw
			if (processedType.contains("raw")) {
				StringBuilder sb = new StringBuilder();
				sb.append("Raw ");
				sb.append(material.getLocalizedName());
				add(EERegistrar.rawMap.getValue(material), sb.toString());

				StringBuilder sb2 = new StringBuilder();
				sb2.append("Block of Raw ");
				sb2.append(material.getLocalizedName());
				add(EERegistrar.rawBlockMap.getValue(material), sb2.toString());
			}

			// Cluster
			if (processedType.contains("cluster")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				sb.append(" Cluster Shard");
				add(EERegistrar.clusterShardMap.getValue(material), sb.toString());

				StringBuilder sb2 = new StringBuilder();
				sb2.append("Budding ");
				sb2.append(material.getLocalizedName());
				add(EERegistrar.buddingBlockMap.getValue(material), sb2.toString());

				StringBuilder sb3 = new StringBuilder();
				sb3.append("Small ");
				sb3.append(material.getLocalizedName());
				sb3.append(" Bud");
				add(EERegistrar.smallBudBlockMap.getValue(material), sb3.toString());

				StringBuilder sb4 = new StringBuilder();
				sb4.append("Medium ");
				sb4.append(material.getLocalizedName());
				sb4.append(" Bud");
				add(EERegistrar.mediumBudBlockMap.getValue(material), sb4.toString());

				StringBuilder sb5 = new StringBuilder();
				sb5.append("Large ");
				sb5.append(material.getLocalizedName());
				sb5.append(" Bud");
				add(EERegistrar.largeBudBlockMap.getValue(material), sb5.toString());

				StringBuilder sb6 = new StringBuilder();
				sb6.append(material.getLocalizedName());
				sb6.append(" Cluster");
				add(EERegistrar.clusterBlockMap.getValue(material), sb6.toString());

				StringBuilder sb7 = new StringBuilder();
				sb7.append("Block of ");
				sb7.append(material.getLocalizedName());
				sb7.append(" Cluster Shard");
				add(EERegistrar.clusterShardBlockMap.getValue(material), sb7.toString());
			}

			// Fluids
			if (processedType.contains("fluid")) {
				StringBuilder sb = new StringBuilder();
				sb.append(material.getLocalizedName());
				add(EERegistrar.fluidTypeMap.getValue(material).getDescriptionId(), sb.toString());

				StringBuilder sb2 = new StringBuilder();
				sb2.append(material.getLocalizedName());
				sb2.append(" Bucket");
				add(EERegistrar.fluidBucketMap.getValue(material), sb2.toString());
			}

//			if (EmendatusEnigmatica.BLOODMAGIC_LOADED) {
//				// Fragment
//				if (processedType.contains("fragment")) {
//					StringBuilder sb = new StringBuilder();
//					sb.append(material.getLocalizedName());
//					sb.append(" Fragment");
//					add(EEBloodMagicRegistrar.fragmentMap.get(material, sb.toString());
//				}
//				// Gravel
//				if (processedType.contains("gravel")) {
//					StringBuilder sb = new StringBuilder();
//					sb.append(material.getLocalizedName());
//					sb.append(" Gravel");
//					add(EEBloodMagicRegistrar.gravelMap.get(material, sb.toString());
//				}
//			}

			for (StrataModel strata : registry.getStrata()) {
				if (processedType.contains("ore")) {
					StringBuilder sb = new StringBuilder();
					sb.append(material.getLocalizedName());
					sb.append(" Ore");

					if (!strata.getId().equals("minecraft_stone")) {
						sb.append(" - ");
						sb.append(strata.getLocalizedName());
					}

					if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
						add(EERegistrar.oreBlockTable.get(strata.getId(), material.getId()).get(), sb.toString());
					}

					if (processedType.contains("sample")) {
						StringBuilder sb2 = new StringBuilder();
						sb2.append(material.getLocalizedName());
						sb2.append(" Rich ");
						sb2.append(strata.getLocalizedName());
						if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
							add(EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).get(), sb2.toString());
						}
					}
				}
			}
		}
	}

	@Override
	public @NotNull String getName() {
		return "Emendatus Enigmatica Languages: en_us";
	}
}