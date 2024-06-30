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

import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.datagen.base.EETagProvider;
import com.ridanisaurus.emendatusenigmatica.datagen.base.IFinishedGenericJSON;
import com.ridanisaurus.emendatusenigmatica.datagen.base.TagBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.parser.model.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class FluidTagsGen extends EETagProvider {

	private final EmendatusDataRegistry registry;

	public FluidTagsGen(DataGenerator gen, EmendatusDataRegistry registry) {
		super(gen);
		this.registry = registry;
	}

	@Override
	protected void buildTags(Consumer<IFinishedGenericJSON> consumer) {
		for (MaterialModel material : registry.getMaterials()) {
			List<String> processedType = material.getProcessedTypes();
			if (processedType.contains("fluid")) {
				ResourceLocation fluidSource = EERegistrar.fluidSourceMap.get(material.getId()).getId();
				ResourceLocation fluidFlowing = EERegistrar.fluidFlowingMap.get(material.getId()).getId();
				new TagBuilder().tag(fluidSource.toString()).tag(fluidFlowing.toString()).save(consumer, new ResourceLocation(Reference.FORGE, "/fluids/" + material.getId()));
			}
		}
	}

	@Override
	public String getName() {
		return "Emendatus Enigmatica Fluid Tags";
	}
}