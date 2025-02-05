/*
 * MIT License
 *
 * Copyright (c) 2020-2024. Ridanisaurus
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

package com.ridanisaurus.emendatusenigmatica.datagen.gen.block;

import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.datagen.builder.BlockModelBuilder;
import com.ridanisaurus.emendatusenigmatica.datagen.provider.EEBlockModelProvider;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class BlockModelsGen extends EEBlockModelProvider {
	private final EmendatusDataRegistry registry;

	public BlockModelsGen(DataGenerator gen, EmendatusDataRegistry registry) {
		super(gen);
		this.registry = registry;
	}

	@Override
	protected void buildBlockModel(Consumer<IFinishedGenericJSON> consumer) {
		for (MaterialModel material : registry.getMaterials()) {
			List<String> processedType = material.getProcessedTypes();
			// Storage Blocks
			if (processedType.contains("storage_block")) {
				if (!material.getColors().hasMaterialColor()) {

					if (material.getProperties().hasOxidization()) {
						if (material.getProperties().getMaterialType().equals("gem")) {
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "exposed", "gem", "exposed_" + material.getId());
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "weathered", "gem", "weathered_" + material.getId());
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "oxidized", "gem", "oxidized_" + material.getId());
							storageBlock	(consumer, "blocks/" + material.getId() + "_block", "waxed_" + material.getId() + "_block");
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "exposed", "gem", "waxed_exposed_" + material.getId());
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "weathered", "gem", "waxed_weathered_" + material.getId());
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "oxidized", "gem", "waxed_oxidized_" + material.getId());
						} else {
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "exposed", "metal", "exposed_" + material.getId());
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "weathered", "metal", "weathered_" + material.getId());
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "oxidized", "metal", "oxidized_" + material.getId());
							storageBlock	(consumer, "blocks/" + material.getId() + "_block", "waxed_" + material.getId() + "_block");
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "exposed", "metal", "waxed_exposed_" + material.getId());
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "weathered", "metal", "waxed_weathered_" + material.getId());
							oxidizationBlock(consumer, "blocks/" + material.getId() + "_block", "oxidized", "metal", "waxed_oxidized_" + material.getId());
						}
					}
					storageBlock(consumer, "blocks/" + material.getId() + "_block", material.getId() + "_block");
				} else {
					if (material.getProperties().getMaterialType().equals("gem")) {
						if (material.getProperties().hasOxidization()) {
							oxidizationTintBlock(consumer,
									"block/templates/block/gem/00",
									"block/templates/block/gem/01",
									"block/templates/block/gem/02",
									"block/templates/block/gem/03",
									"block/templates/block/gem/04",
									"exposed",
									"gem",
									"exposed_" + material.getId());
							oxidizationTintBlock(consumer,
									"block/templates/block/gem/00",
									"block/templates/block/gem/01",
									"block/templates/block/gem/02",
									"block/templates/block/gem/03",
									"block/templates/block/gem/04",
									"weathered",
									"gem",
									"weathered_" + material.getId());
							oxidizationTintBlock(consumer,
									"block/templates/block/gem/00",
									"block/templates/block/gem/01",
									"block/templates/block/gem/02",
									"block/templates/block/gem/03",
									"block/templates/block/gem/04",
									"oxidized",
									"gem",
									"oxidized_" + material.getId());
							storageTintBlock(consumer,
									"block/templates/block/gem/00",
									"block/templates/block/gem/01",
									"block/templates/block/gem/02",
									"block/templates/block/gem/03",
									"block/templates/block/gem/04",
									"waxed_" + material.getId() + "_block");
							oxidizationTintBlock(consumer,
									"block/templates/block/gem/00",
									"block/templates/block/gem/01",
									"block/templates/block/gem/02",
									"block/templates/block/gem/03",
									"block/templates/block/gem/04",
									"exposed",
									"gem",
									"waxed_exposed_" + material.getId());
							oxidizationTintBlock(consumer,
									"block/templates/block/gem/00",
									"block/templates/block/gem/01",
									"block/templates/block/gem/02",
									"block/templates/block/gem/03",
									"block/templates/block/gem/04",
									"weathered",
									"gem",
									"waxed_weathered_" + material.getId());
							oxidizationTintBlock(consumer,
									"block/templates/block/gem/00",
									"block/templates/block/gem/01",
									"block/templates/block/gem/02",
									"block/templates/block/gem/03",
									"block/templates/block/gem/04",
									"oxidized",
									"gem",
									"waxed_oxidized_" + material.getId());
						}
						storageTintBlock(consumer,
								"block/templates/block/gem/00",
								"block/templates/block/gem/01",
								"block/templates/block/gem/02",
								"block/templates/block/gem/03",
								"block/templates/block/gem/04",
								material.getId() + "_block");
					} else {
						if (material.getProperties().hasOxidization()) {
							oxidizationTintBlock(consumer,
									"block/templates/block/metal/00",
									"block/templates/block/metal/01",
									"block/templates/block/metal/02",
									"block/templates/block/metal/03",
									"block/templates/block/metal/04",
									"exposed",
									"metal",
									"exposed_" + material.getId());
							oxidizationTintBlock(consumer,
									"block/templates/block/metal/00",
									"block/templates/block/metal/01",
									"block/templates/block/metal/02",
									"block/templates/block/metal/03",
									"block/templates/block/metal/04",
									"weathered",
									"metal",
									"weathered_" + material.getId());
							oxidizationTintBlock(consumer,
									"block/templates/block/metal/00",
									"block/templates/block/metal/01",
									"block/templates/block/metal/02",
									"block/templates/block/metal/03",
									"block/templates/block/metal/04",
									"oxidized",
									"metal",
									"oxidized_" + material.getId());
							storageTintBlock(consumer,
									"block/templates/block/metal/00",
									"block/templates/block/metal/01",
									"block/templates/block/metal/02",
									"block/templates/block/metal/03",
									"block/templates/block/metal/04",
									"waxed_" + material.getId() + "_block");
							oxidizationTintBlock(consumer,
									"block/templates/block/metal/00",
									"block/templates/block/metal/01",
									"block/templates/block/metal/02",
									"block/templates/block/metal/03",
									"block/templates/block/metal/04",
									"exposed",
									"metal",
									"waxed_exposed_" + material.getId());
							oxidizationTintBlock(consumer,
									"block/templates/block/metal/00",
									"block/templates/block/metal/01",
									"block/templates/block/metal/02",
									"block/templates/block/metal/03",
									"block/templates/block/metal/04",
									"weathered",
									"metal",
									"waxed_weathered_" + material.getId());
							oxidizationTintBlock(consumer,
									"block/templates/block/metal/00",
									"block/templates/block/metal/01",
									"block/templates/block/metal/02",
									"block/templates/block/metal/03",
									"block/templates/block/metal/04",
									"oxidized",
									"metal",
									"waxed_oxidized_" + material.getId());
						}
						storageTintBlock(consumer,
								"block/templates/block/metal/00",
								"block/templates/block/metal/01",
								"block/templates/block/metal/02",
								"block/templates/block/metal/03",
								"block/templates/block/metal/04",
								material.getId() + "_block");
					}
				}
			}
			// Shard Blocks
			if (processedType.contains("cluster")) {
				if (!material.getColors().hasMaterialColor()) {
					// Cluster Shard Block
					storageBlock(consumer, "blocks/" + material.getId() + "_cluster_shard_block", material.getId() + "_cluster_shard_block");
					// Budding Block
					storageBlock(consumer, "blocks/budding_" + material.getId(), "budding_" + material.getId());
					// Clusters
					clusterBlock(consumer, "blocks/small_" + material.getId() + "_bud", "small_" + material.getId() + "_bud");
					clusterBlock(consumer, "blocks/medium_" + material.getId() + "_bud", "medium_" + material.getId() + "_bud");
					clusterBlock(consumer, "blocks/large_" + material.getId() + "_bud", "large_" + material.getId() + "_bud");
					clusterBlock(consumer, "blocks/" + material.getId() + "_cluster", material.getId() + "_cluster");
				} else {
					// Cluster Shard Block
					storageTintBlock(consumer,
							"block/templates/clusters/block/00",
							"block/templates/clusters/block/01",
							"block/templates/clusters/block/02",
							"block/templates/clusters/block/03",
							"block/templates/clusters/block/04",
							material.getId() + "_cluster_shard_block"
					);
					// Budding Block
					storageTintBlock(consumer,
							"block/templates/clusters/budding/00",
							"block/templates/clusters/budding/01",
							"block/templates/clusters/budding/02",
							"block/templates/clusters/budding/03",
							"block/templates/clusters/budding/04",
							"budding_" + material.getId()
					);
					// Clusters
					clusterTintBlock(consumer,
							"block/templates/clusters/small_bud/00",
							"block/templates/clusters/small_bud/01",
							"block/templates/clusters/small_bud/02",
							"block/templates/clusters/small_bud/03",
							"block/templates/clusters/small_bud/04",
							"small_" + material.getId() + "_bud"
					);
					clusterTintBlock(consumer,
							"block/templates/clusters/medium_bud/00",
							"block/templates/clusters/medium_bud/01",
							"block/templates/clusters/medium_bud/02",
							"block/templates/clusters/medium_bud/03",
							"block/templates/clusters/medium_bud/04",
							"medium_" + material.getId() + "_bud"
					);
					clusterTintBlock(consumer,
							"block/templates/clusters/large_bud/00",
							"block/templates/clusters/large_bud/01",
							"block/templates/clusters/large_bud/02",
							"block/templates/clusters/large_bud/03",
							"block/templates/clusters/large_bud/04",
							"large_" + material.getId() + "_bud"
					);
					clusterTintBlock(consumer,
							"block/templates/clusters/cluster/00",
							"block/templates/clusters/cluster/01",
							"block/templates/clusters/cluster/02",
							"block/templates/clusters/cluster/03",
							"block/templates/clusters/cluster/04",
							material.getId() + "_cluster"
					);
				}
			}
			// Raw Storage Blocks
			if (processedType.contains("raw")) {
				if (!material.getColors().hasMaterialColor()) {
					storageBlock(consumer, "blocks/raw_" + material.getId() + "_block", "raw_" + material.getId() + "_block");
				} else {
					storageTintBlock(consumer,
							"block/templates/raw_block/00",
							"block/templates/raw_block/01",
							"block/templates/raw_block/02",
							"block/templates/raw_block/03",
							"block/templates/raw_block/04",
							"raw_" + material.getId() + "_block");
				}
			}
			// Ores
			if (processedType.contains("ore")) {
				for (StrataModel stratum : registry.getStrata()) {
					if (!material.getStrata().isEmpty() && !material.getStrata().contains(stratum.getId())) continue;
					if (!material.getColors().hasMaterialColor()) {
						if (material.getProperties().isEmissive()) {
							oreEmissiveBlock(consumer, stratum.getBaseTexture().toString(), "block/overlays/" + material.getId(), getOreModelName(stratum, material));
						} else {
							oreBlock(consumer, stratum.getBaseTexture().toString(), "block/overlays/" + material.getId(), getOreModelName(stratum, material));
						}
					} else {
						if (material.getProperties().getMaterialType().equals("gem")) {
							if (material.getProperties().isEmissive()) {
								oreEmissiveTintBlock(consumer, stratum.getBaseTexture().toString(),
										"block/templates/ore/gem/00",
										"block/templates/ore/gem/01",
										"block/templates/ore/gem/02",
										"block/templates/ore/gem/03",
										"block/templates/ore/gem/04",
										"block/templates/ore/gem/shadow_drop",
										getOreModelName(stratum, material));
							} else {
								oreTintBlock(consumer, stratum.getBaseTexture().toString(),
										"block/templates/ore/gem/00",
										"block/templates/ore/gem/01",
										"block/templates/ore/gem/02",
										"block/templates/ore/gem/03",
										"block/templates/ore/gem/04",
										"block/templates/ore/gem/shadow_drop",
										getOreModelName(stratum, material));
							}
						} else {
							if (material.getProperties().isEmissive()) {
								oreEmissiveTintBlock(consumer, stratum.getBaseTexture().toString(),
										"block/templates/ore/metal/00",
										"block/templates/ore/metal/01",
										"block/templates/ore/metal/02",
										"block/templates/ore/metal/03",
										"block/templates/ore/metal/04",
										"block/templates/ore/metal/shadow_drop",
										getOreModelName(stratum, material));
							} else {
								oreTintBlock(consumer, stratum.getBaseTexture().toString(),
										"block/templates/ore/metal/00",
										"block/templates/ore/metal/01",
										"block/templates/ore/metal/02",
										"block/templates/ore/metal/03",
										"block/templates/ore/metal/04",
										"block/templates/ore/metal/shadow_drop",
										getOreModelName(stratum, material));
							}
						}
					}
					//TODO: Rework Sample System
//					if (processedType.contains("sample")) {
//						if (!material.getColors().hasMaterialColor()) {
//							if (material.getProperties().isEmissive()) {
//								oreEmissiveBlock(consumer, stratum.getBaseTexture().toString(), "block/overlays/" + material.getId() + "_sample", getSampleModelName(stratum, material));
//							} else {
//								oreBlock(consumer, stratum.getBaseTexture().toString(), "block/overlays/" + material.getId() + "_sample", getSampleModelName(stratum, material));
//							}
//						} else {
//							if (material.getProperties().isEmissive()) {
//								oreEmissiveTintBlock(consumer, stratum.getBaseTexture().toString(),
//										"block/templates/sample/00",
//										"block/templates/sample/01",
//										"block/templates/sample/02",
//										"block/templates/sample//03",
//										"block/templates/sample/04",
//										"block/templates/sample/shadow_drop",
//										getSampleModelName(stratum, material));
//							} else {
//								oreTintBlock(consumer, stratum.getBaseTexture().toString(),
//										"block/templates/sample/00",
//										"block/templates/sample/01",
//										"block/templates/sample/02",
//										"block/templates/sample//03",
//										"block/templates/sample/04",
//										"block/templates/sample/shadow_drop",
//										getSampleModelName(stratum, material));
//							}
//						}
//					}
				}
			}
		}
	}

	private void oxidizationBlock(Consumer<IFinishedGenericJSON> consumer, String base, String age, String type, String path) {
		new BlockModelBuilder("minecraft:block/block")
				.setLoader(Reference.NEOFORGE + ":composite")
				.texture("particle",  ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.child("solid", new BlockModelBuilder("minecraft:block/block")
						.texture("base",  ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#base")
						.allFaces((dir, uv) -> uv.tintindex(-1))
						.end()
						.renderType("solid")
				)
				.child("translucent", new BlockModelBuilder("minecraft:block/block")
						.texture(age, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/block/" + type + "/oxidization/" + age))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#" + age)
						.allFaces((dir, uv) -> uv.tintindex(9))
						.end()
						.renderType("translucent")
				)
				.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	private void oxidizationTintBlock(Consumer<IFinishedGenericJSON> consumer, String highlight2, String highlight1, String base, String shadow1, String shadow2, String age, String type, String path) {
		new BlockModelBuilder("minecraft:block/block")
				.setLoader(Reference.NEOFORGE + ":composite")
				.texture("particle",  ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.child("cutout", new BlockModelBuilder("minecraft:block/block")
						.texture("highlight2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight2))
						.texture("highlight1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight1))
						.texture("base", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
						.texture("shadow1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow1))
						.texture("shadow2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow2))
						.element()
						.cube("#highlight2")
						.allFaces((d, u) -> u.tintindex(0))
						.end()
						.element()
						.cube("#highlight1")
						.allFaces((d, u) -> u.tintindex(1))
						.end()
						.element()
						.cube("#base")
						.allFaces((d, u) -> u.tintindex(2))
						.end()
						.element()
						.cube("#shadow1")
						.allFaces((d, u) -> u.tintindex(3))
						.end()
						.element()
						.cube("#shadow2")
						.allFaces((d, u) -> u.tintindex(4))
						.end()
						.renderType("cutout")
				)
				.child("translucent", new BlockModelBuilder("minecraft:block/block")
						.texture(age, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/templates/block/" + type + "/oxidization/" + age))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#" + age)
						.allFaces((dir, uv) -> uv.tintindex(9))
						.end()
						.renderType("translucent")
				)
				.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	private void clusterBlock(Consumer<IFinishedGenericJSON> consumer, String base, String path) {
		new BlockModelBuilder("minecraft:block/cross")
				.texture("cross",  ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.texture("particle",  ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.renderType("cutout")
				.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	private void clusterTintBlock(Consumer<IFinishedGenericJSON> consumer, String highlight2, String highlight1, String base, String shadow1, String shadow2, String path) {
		new BlockModelBuilder("emendatusenigmatica:block/bud")
				.renderType("cutout")
				.texture("highlight2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight2))
				.texture("highlight1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight1))
				.texture("base", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.texture("shadow1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow1))
				.texture("shadow2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow2))
				.texture("particle", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	private void storageBlock(Consumer<IFinishedGenericJSON> consumer, String base, String path) {
		new BlockModelBuilder("minecraft:block/block")
				.texture("base",  ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.texture("particle",  ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.element()
				.cube("#base")
				.allFaces((d, u) -> u.tintindex(-1))
				.end()
				.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	private void storageTintBlock(Consumer<IFinishedGenericJSON> consumer, String highlight2, String highlight1, String base, String shadow1, String shadow2, String path) {
		new BlockModelBuilder("minecraft:block/block")
				.renderType("cutout")
				.texture("highlight2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight2))
				.texture("highlight1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight1))
				.texture("base", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.texture("shadow1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow1))
				.texture("shadow2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow2))
				.texture("particle", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
				.element()
				.cube("#highlight2")
				.allFaces((d, u) -> u.tintindex(0))
				.end()
				.element()
				.cube("#highlight1")
				.allFaces((d, u) -> u.tintindex(1))
				.end()
				.element()
				.cube("#base")
				.allFaces((d, u) -> u.tintindex(2))
				.end()
				.element()
				.cube("#shadow1")
				.allFaces((d, u) -> u.tintindex(3))
				.end()
				.element()
				.cube("#shadow2")
				.allFaces((d, u) -> u.tintindex(4))
				.end()
				.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	public void oreBlock(Consumer<IFinishedGenericJSON> consumer, String strata, String overlayTexture, String path) {
		new BlockModelBuilder("minecraft:block/block")
				.texture("particle", strata)
				.setLoader(Reference.NEOFORGE + ":composite")
				.child("solid", new BlockModelBuilder("minecraft:block/block")
						.texture("strata", strata)
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#strata")
						.allFaces((dir, uv) -> uv.tintindex(-1))
						.end()
						.renderType("solid")
				)
				.child("translucent", new BlockModelBuilder("minecraft:block/block")
						.texture("overlay", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, overlayTexture))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#overlay")
						.allFaces((dir, uv) -> uv.tintindex(-1))
						.end()
						.renderType("translucent")
				).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	public void oreEmissiveBlock(Consumer<IFinishedGenericJSON> consumer, String strata, String overlayTexture, String path) {
		new BlockModelBuilder("minecraft:block/block")
				.texture("particle", strata)
				.setLoader(Reference.NEOFORGE + ":composite")
				.child("solid", new BlockModelBuilder("minecraft:block/block")
						.texture("strata", strata)
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#strata")
						.allFaces((dir, uv) -> uv.tintindex(-1))
						.end()
						.renderType("solid")
				)
				.child("translucent", new BlockModelBuilder("minecraft:block/block")
						.texture("overlay", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, overlayTexture))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#overlay")
						.allFaces((dir, uv) -> uv.tintindex(-1).emissive())
						.end()
						.renderType("translucent")
				).save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	public void oreTintBlock(Consumer<IFinishedGenericJSON> consumer, String strata, String highlight2, String highlight1, String base, String shadow1, String shadow2, String drop, String path) {
		new BlockModelBuilder("minecraft:block/block")
				.texture("particle", strata)
				.setLoader(Reference.NEOFORGE + ":composite")
				.child("solid", new BlockModelBuilder("minecraft:block/block")
						.texture("strata", strata)
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#strata")
						.allFaces((dir, uv) -> uv.tintindex(-1))
						.end()
						.renderType("solid")
				)
				.child("cutout", new BlockModelBuilder("minecraft:block/block")
						.texture("highlight2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight2))
						.texture("highlight1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight1))
						.texture("base", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
						.texture("shadow1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow1))
						.texture("shadow2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow2))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#highlight2")
						.allFaces((dir, uv) -> uv.tintindex(0))
						.end()
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#highlight1")
						.allFaces((dir, uv) -> uv.tintindex(1))
						.end()
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#base")
						.allFaces((dir, uv) -> uv.tintindex(2))
						.end()
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#shadow1")
						.allFaces((dir, uv) -> uv.tintindex(3))
						.end()
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#shadow2")
						.allFaces((dir, uv) -> uv.tintindex(4))
						.end()
						.renderType("cutout")
				)
				.child("translucent", new BlockModelBuilder("minecraft:block/block")
						.texture("drop", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, drop))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#drop")
						.allFaces((dir, uv) -> uv.tintindex(-1))
						.end()
						.renderType("translucent")
				)
				.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	public void oreEmissiveTintBlock(Consumer<IFinishedGenericJSON> consumer, String strata, String highlight2, String highlight1, String base, String shadow1, String shadow2, String drop, String path) {
		new BlockModelBuilder("minecraft:block/block")
				.texture("particle", strata)
				.setLoader(Reference.NEOFORGE + ":composite")
				.child("solid", new BlockModelBuilder("minecraft:block/block")
						.texture("strata", strata)
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#strata")
						.allFaces((dir, uv) -> uv.tintindex(-1))
						.end()
						.renderType("solid")
				)
				.child("cutout", new BlockModelBuilder("minecraft:block/block")
						.texture("highlight2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight2))
						.texture("highlight1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, highlight1))
						.texture("base", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, base))
						.texture("shadow1", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow1))
						.texture("shadow2", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, shadow2))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#highlight2")
						.allFaces((dir, uv) -> uv.tintindex(0).emissive())
						.end()
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#highlight1")
						.allFaces((dir, uv) -> uv.tintindex(1).emissive())
						.end()
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#base")
						.allFaces((dir, uv) -> uv.tintindex(2).emissive())
						.end()
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#shadow1")
						.allFaces((dir, uv) -> uv.tintindex(3).emissive())
						.end()
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#shadow2")
						.allFaces((dir, uv) -> uv.tintindex(4).emissive())
						.end()
						.renderType("cutout")
				)
				.child("translucent", new BlockModelBuilder("minecraft:block/block")
						.texture("drop", ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, drop))
						.element()
						.from(0, 0, 0)
						.to(16, 16, 16)
						.cube("#drop")
						.allFaces((dir, uv) -> uv.tintindex(-1))
						.end()
						.renderType("translucent")
				)
				.save(consumer, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, path));
	}

	public static @NotNull String getOreModelName(@NotNull StrataModel stratum, @NotNull MaterialModel material) {
		return material.getId() + (!stratum.getId().equals("minecraft_stone") ? "_" + stratum.getSuffix() : "") + "_ore";
	}

	public static @NotNull String getSampleModelName(@NotNull StrataModel stratum, @NotNull MaterialModel material) {
		return material.getId() + "_" + stratum.getSuffix() + "_ore_sample";
	}

	@Override
	public @NotNull String getName() {
		return "Emendatus Enigmatica: Block Models";
	}
}
