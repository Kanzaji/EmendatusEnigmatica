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
import com.ridanisaurus.emendatusenigmatica.datagen.builder.TagBuilder;
import com.ridanisaurus.emendatusenigmatica.datagen.provider.EETagProvider;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.ridanisaurus.emendatusenigmatica.util.Reference.COMMON;
import static com.ridanisaurus.emendatusenigmatica.util.Reference.C_TAG;

public class BlockTagsGen extends EETagProvider {
	private final EmendatusDataRegistry registry;
	private final List<String> forgeBlocks = Lists.newArrayList();
	private final List<String> forgeOres = Lists.newArrayList();
	private final Map<String, List<String>> oresPerMaterial = new HashMap<>();
	private final Map<String, List<String>> oresInGround = new HashMap<>();

	public BlockTagsGen(DataGenerator gen, EmendatusDataRegistry registry) {
		super(gen);
		this.registry = registry;
	}

	@Override
	protected void buildTags(Consumer<IFinishedGenericJSON> consumer) {
		for (MaterialModel material : registry.getMaterials()) {
			List<String> processedType = material.getProcessedTypes();
			// Storage Blocks
			if (processedType.contains("storage_block")) {
				ResourceLocation block = EERegistrar.storageBlockMap.get(material.getId()).getId();
				if (!forgeBlocks.contains(C_TAG + ":storage_blocks/" + material.getId())) forgeBlocks.add(C_TAG + ":storage_blocks/" + material.getId());
				new TagBuilder().tag(block.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/storage_blocks/" + material.getId()));
			}

			// Raw Materials
			if (processedType.contains("raw")) {
				ResourceLocation raw = EERegistrar.rawBlockMap.get(material.getId()).getId();
				if (!forgeBlocks.contains(C_TAG + ":storage_blocks/raw_" + material.getId())) forgeBlocks.add(C_TAG + ":storage_blocks/raw_" + material.getId());
				new TagBuilder().tag(raw.toString()).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/storage_blocks/raw_" + material.getId()));
			}

			// Ores
			for (StrataModel strata : registry.getStrata()) {
				if (processedType.contains("ore")) {
					if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
						ResourceLocation ore = EERegistrar.oreBlockTable.get(strata.getId(), material.getId()).getId();
						if (!forgeOres.contains(C_TAG + ":ores/" + material.getId())) forgeOres.add(C_TAG + ":ores/" + material.getId());
						oresPerMaterial.computeIfAbsent(material.getId(), s -> new ArrayList<>()).add(ore.toString());
						oresInGround.computeIfAbsent(strata.getSuffix(), s -> new ArrayList<>()).add(ore.toString());
					}

					if (processedType.contains("sample")) {
						if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
							ResourceLocation sample = EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).getId();
							if (!forgeOres.contains(C_TAG + ":ores/" + material.getId())) forgeOres.add(C_TAG + ":ores/" + material.getId());
							oresPerMaterial.computeIfAbsent(material.getId(), s -> new ArrayList<>()).add(sample.toString());
							oresInGround.computeIfAbsent(strata.getSuffix(), s -> new ArrayList<>()).add(sample.toString());
						}
					}
				}
			}

		}
		
		if (!oresPerMaterial.isEmpty()) oresPerMaterial.forEach((material, oreList) -> new TagBuilder().tags(oreList).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/ores/" + material)));
		if (!oresInGround.isEmpty()) oresInGround.forEach((strataPrefix, oreType) -> new TagBuilder().tags(oreType).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/ores_in_ground/" + strataPrefix)));

		if (!forgeBlocks.isEmpty()) new TagBuilder().tags(forgeBlocks).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/storage_blocks"));
		if (!forgeOres.isEmpty()) new TagBuilder().tags(forgeOres).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/ores"));
	}

	@Override
	public String getName() {
		return "Emendatus Enigmatica Block Tags";
	}
}