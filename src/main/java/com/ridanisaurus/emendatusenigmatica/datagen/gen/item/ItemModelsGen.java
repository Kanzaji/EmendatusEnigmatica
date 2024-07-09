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

package com.ridanisaurus.emendatusenigmatica.datagen.gen.item;

import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.datagen.provider.EEItemModelProvider;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import com.ridanisaurus.emendatusenigmatica.datagen.builder.ItemModelBuilder;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ItemModelsGen extends EEItemModelProvider {
	private final EmendatusDataRegistry registry;

	public ItemModelsGen(DataGenerator gen, EmendatusDataRegistry registry) {
		super(gen);
		this.registry = registry;
	}

	@Override
	protected void buildItemModels(Consumer<IFinishedGenericJSON> consumer) {
		for (MaterialModel material : registry.getMaterials()) {
			List<String> processedType = material.getProcessedTypes();
			// Storage Blocks
			if (processedType.contains("storage_block")) {
				if (material.getProperties().hasOxidization()) {
					new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/exposed_" + material.getId()).toString())
							.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "exposed_" + material.getId()));
					new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/weathered_" + material.getId()).toString())
							.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "weathered_" + material.getId()));
					new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/oxidized_" + material.getId()).toString())
							.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "oxidized_" + material.getId()));
					new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/waxed_" + material.getId() + "_block").toString())
							.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed_" + material.getId() + "_block"));
					new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/waxed_exposed_" + material.getId()).toString())
							.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed_exposed_" + material.getId()));
					new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/waxed_weathered_" + material.getId()).toString())
							.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed_weathered_" + material.getId()));
					new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/waxed_oxidized_" + material.getId()).toString())
							.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed_oxidized_" + material.getId()));
				}
				new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/" + material.getId() + "_block").toString())
						.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_block"));
			}
			// Shard Blocks
			if (processedType.contains("cluster")) {
				new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/" + material.getId() + "_cluster_shard_block").toString())
						.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_cluster_shard_block"));
				new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/budding_" + material.getId()).toString())
						.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "budding_" + material.getId()));
				ItemModelBuilder smallBudBuilder = new ItemModelBuilder("emendatusenigmatica:item/bud");
				if (!material.getColors().hasMaterialColor()) {
					smallBudBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "blocks/small_" + material.getId() + "_bud").toString());
				} else {
					smallBudBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/small_bud/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/small_bud/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/small_bud/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/small_bud/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/small_bud/04").toString());
				}
				smallBudBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "small_" + material.getId() + "_bud"));
				ItemModelBuilder mediumBudBuilder = new ItemModelBuilder("emendatusenigmatica:item/bud");
				if (!material.getColors().hasMaterialColor()) {
					mediumBudBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "blocks/medium_" + material.getId() + "_bud").toString());
				} else {
					mediumBudBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/medium_bud/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/medium_bud/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/medium_bud/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/medium_bud/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/medium_bud/04").toString());
				}
				mediumBudBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "medium_" + material.getId() + "_bud"));
				ItemModelBuilder largeBudBuilder = new ItemModelBuilder("emendatusenigmatica:item/bud");
				if (!material.getColors().hasMaterialColor()) {
					largeBudBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "blocks/large_" + material.getId() + "_bud").toString());
				} else {
					largeBudBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/large_bud/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/large_bud/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/large_bud/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/large_bud/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/large_bud/04").toString());
				}
				largeBudBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "large_" + material.getId() + "_bud"));
				ItemModelBuilder clusterBuilder = new ItemModelBuilder("emendatusenigmatica:item/bud");
				if (!material.getColors().hasMaterialColor()) {
					clusterBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "blocks/" + material.getId() + "_cluster").toString());
				} else {
					clusterBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/cluster/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/cluster/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/cluster/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/cluster/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/clusters/cluster/04").toString());
				}
				clusterBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_cluster"));
				ItemModelBuilder clusterShardBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					clusterShardBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_cluster_shard").toString());
				} else {
					clusterShardBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/cluster_shard/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/cluster_shard/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/cluster_shard/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/cluster_shard/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/cluster_shard/04").toString());
				}
				clusterShardBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_cluster_shard"));
			}
			// Ingots
			if (processedType.contains("ingot")) {
				ItemModelBuilder ingotBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					ingotBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_ingot").toString());
				} else {
					ingotBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/ingot/00").toString())
								.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/ingot/01").toString())
								.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/ingot/02").toString())
								.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/ingot/03").toString())
								.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/ingot/04").toString());
				}
				ingotBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_ingot"));
			}
			// Nuggets
			if (processedType.contains("nugget")) {
				ItemModelBuilder nuggetBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					nuggetBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_nugget").toString());
				} else {
					nuggetBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/nugget/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/nugget/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/nugget/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/nugget/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/nugget/04").toString());
				}
				nuggetBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_nugget"));
			}
			// Gems
			if (processedType.contains("gem")) {
				ItemModelBuilder gemBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					gemBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_gem").toString());
				} else {
					gemBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gem/template_" + material.getProperties().getGemTexture() + "/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gem/template_" + material.getProperties().getGemTexture() + "/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gem/template_" + material.getProperties().getGemTexture() + "/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gem/template_" + material.getProperties().getGemTexture() + "/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gem/template_" + material.getProperties().getGemTexture() + "/04").toString());
				}
				gemBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_gem"));
			}
			// Dusts
			if (processedType.contains("dust")) {
				ItemModelBuilder dustBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					dustBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_dust").toString());
				} else {
					dustBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/dust/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/dust/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/dust/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/dust/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/dust/04").toString());
				}
				dustBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_dust"));
			}
			// Plates
			if (processedType.contains("plate")) {
				ItemModelBuilder plateBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					plateBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_plate").toString());
				} else {
					plateBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/plate/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/plate/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/plate/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/plate/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/plate/04").toString());
				}
				plateBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_plate"));
			}
			// Gears
			if (processedType.contains("gear")) {
				ItemModelBuilder gearBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					gearBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_gear").toString());
				} else {
					gearBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gear/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gear/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gear/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gear/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/gear/04").toString());
				}
				gearBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_gear"));
			}
			// Rods
			if (processedType.contains("rod")) {
				ItemModelBuilder rodBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					rodBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_rod").toString());
				} else {
					rodBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/rod/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/rod/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/rod/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/rod/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/rod/04").toString());
				}
				rodBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_rod"));
			}
			// Raw Materials
			if (processedType.contains("raw")) {
				ItemModelBuilder rawBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					rawBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/raw_" + material.getId()).toString());
				} else {
					rawBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/raw/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/raw/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/raw/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/raw/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/raw/04").toString());
				}
				rawBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "raw_" + material.getId()));

				new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/raw_" + material.getId() + "_block").toString())
						.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "raw_" + material.getId() + "_block"));
			}
			// Swords
			if (processedType.contains("sword")) {
				ItemModelBuilder swordBuilder = new ItemModelBuilder("minecraft:item/handheld");
				if (!material.getColors().hasMaterialColor()) {
					swordBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_sword").toString());
				} else {
					swordBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/sword/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/sword/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/sword/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/sword/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/sword/grip").toString());
				}
				swordBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_sword"));
			}
			// Pickaxes
			if (processedType.contains("pickaxe")) {
				ItemModelBuilder pickaxeBuilder = new ItemModelBuilder("minecraft:item/handheld");
				if (!material.getColors().hasMaterialColor()) {
					pickaxeBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_pickaxe").toString());
				} else {
					pickaxeBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/pickaxe/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/pickaxe/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/pickaxe/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/pickaxe/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/pickaxe/grip").toString());
				}
				pickaxeBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_pickaxe"));
			}
			// Axes
			if (processedType.contains("axe")) {
				ItemModelBuilder axeBuilder = new ItemModelBuilder("minecraft:item/handheld");
				if (!material.getColors().hasMaterialColor()) {
					axeBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_axe").toString());
				} else {
					axeBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/axe/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/axe/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/axe/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/axe/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/axe/grip").toString());

				}
				axeBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_axe"));
			}
			// Shovels
			if (processedType.contains("shovel")) {
				ItemModelBuilder shovelBuilder = new ItemModelBuilder("minecraft:item/handheld");
				if (!material.getColors().hasMaterialColor()) {
					shovelBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_shovel").toString());
				} else {
					shovelBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/shovel/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/shovel/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/shovel/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/shovel/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/shovel/grip").toString());
				}
				shovelBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_shovel"));
			}
			// Hoes
			if (processedType.contains("hoe")) {
				ItemModelBuilder hoeBuilder = new ItemModelBuilder("minecraft:item/handheld");
				if (!material.getColors().hasMaterialColor()) {
					hoeBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_hoe").toString());
				} else {
					hoeBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/hoe/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/hoe/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/hoe/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/hoe/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/hoe/grip").toString());
				}
				hoeBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_hoe"));
			}
			// Paxels
			if (processedType.contains("paxel")) {
				ItemModelBuilder paxelBuilder = new ItemModelBuilder("minecraft:item/handheld");
				if (!material.getColors().hasMaterialColor()) {
					paxelBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_paxel").toString());
				} else {
					paxelBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/paxel/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/paxel/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/paxel/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/paxel/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/paxel/grip").toString());
				}
				paxelBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_paxel"));
			}
			// Helmet
			if (processedType.contains("helmet")) {
				ItemModelBuilder helmetBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					helmetBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_helmet").toString());
				} else {
					helmetBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/helmet/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/helmet/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/helmet/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/helmet/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/helmet/04").toString());
				}
				helmetBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_helmet"));
			}
			// Chestplate
			if (processedType.contains("chestplate")) {
				ItemModelBuilder chestplateBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					chestplateBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_chestplate").toString());
				} else {
					chestplateBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/chestplate/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/chestplate/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/chestplate/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/chestplate/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/chestplate/04").toString());
				}
				chestplateBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_chestplate"));
			}
			// Leggings
			if (processedType.contains("leggings")) {
				ItemModelBuilder leggingsBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					leggingsBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_leggings").toString());
				} else {
					leggingsBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/leggings/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/leggings/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/leggings/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/leggings/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/leggings/04").toString());
				}
				leggingsBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_leggings"));
			}
			// Boots
			if (processedType.contains("boots")) {
				ItemModelBuilder bootsBuilder = new ItemModelBuilder("minecraft:item/generated");
				if (!material.getColors().hasMaterialColor()) {
					bootsBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_boots").toString());
				} else {
					bootsBuilder.texture("layer0", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/boots/00").toString())
							.texture("layer1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/boots/01").toString())
							.texture("layer2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/boots/02").toString())
							.texture("layer3", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/boots/03").toString())
							.texture("layer4", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/templates/boots/04").toString());
				}
				bootsBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_boots"));
			}
			// Shields
			if (processedType.contains("shield")) {
				ItemModelBuilder shieldBlockingBuilder = new ItemModelBuilder("minecraft:item/shield_blocking")
						.texture("particle", ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "block/dark_oak_planks").toString());
				shieldBlockingBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_shield_blocking"));

				ItemModelBuilder shieldBuilder = new ItemModelBuilder("minecraft:item/shield")
						.texture("particle", ResourceLocation.fromNamespaceAndPath(Reference.MINECRAFT, "block/dark_oak_planks").toString())
						.override()
						.predicate(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "blocking"), 1)
						.model(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "item/" + material.getId() + "_shield_blocking"))
						.end();
				shieldBuilder.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_shield"));
			}
			// Fluid Buckets
			if (processedType.contains("fluid")) {
                //FIXME: getID in fluid is missing apparently.
//				new ItemModelBuilder("forge:item/bucket_drip")
//						.applyTint(true)
//						.fluid(EERegistrar.fluidSourceMap.get(material.getId()).getId().toString())
//						.loader("forge:fluid_container")
//						.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, material.getId() + "_bucket"));
			}
			// Ores
			for (StrataModel stratum : registry.getStrata()) {
				if (processedType.contains("ore")) {
					new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/" + getOreModelName(stratum, material)).toString())
							.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, getOreModelName(stratum, material)));
					if (processedType.contains("sample")) {
						new ItemModelBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/" + getSampleModelName(stratum, material)).toString())
								.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, getSampleModelName(stratum, material)));
					}
				}
			}
		}
	}

	public static String getOreModelName(StrataModel stratum, MaterialModel material) {
		return material.getId() + (!stratum.getId().equals("minecraft_stone") ? "_" + stratum.getSuffix() : "") + "_ore";
	}

	public static String getSampleModelName(StrataModel stratum, MaterialModel material) {
		return material.getId() + "_" + stratum.getSuffix() + "_ore_sample";
	}

	@Override
	public @NotNull String getName() {
		return "Emendatus Enigmatica Item Models";
	}
}