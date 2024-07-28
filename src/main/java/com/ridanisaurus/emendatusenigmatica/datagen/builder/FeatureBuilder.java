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

package com.ridanisaurus.emendatusenigmatica.datagen.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class FeatureBuilder {

	private final String type;
	private final List<String> biomes = Lists.newArrayList();
	private final List<String> features = Lists.newArrayList();
	private final String step;

	public FeatureBuilder(String type, String step) {
		this.type = type;
		this.step = step;
	}

	public FeatureBuilder feature(String singleFeature) {
		this.features.add(singleFeature);
		return this;
	}

	public FeatureBuilder features(List<String> multipleFeatures) {
		this.features.addAll(multipleFeatures);
		return this;
	}

	public FeatureBuilder biome(String singleBiome) {
		this.biomes.add(singleBiome);
		return this;
	}

	public FeatureBuilder biomes(List<String> multipleBiomes) {
		this.biomes.addAll(multipleBiomes);
		return this;
	}

	public void save(Consumer<IFinishedGenericJSON> consumer, ResourceLocation jsonResourceLocation) {
		consumer.accept(new Result(jsonResourceLocation, this.type, this.biomes, this.features, this.step));
	}

	public static class Result implements IFinishedGenericJSON {
		private final ResourceLocation id;
		private final String type;
		private final List<String> biomes;
		private final List<String> features;
		private final String step;

		public Result(ResourceLocation id, String type, List<String> biomes, List<String> features, String step) {
			this.id = id;
			this.type = type;
			this.biomes = biomes;
			this.features = features;
			this.step = step;
		}

		public void serializeJSONData(JsonObject recipeJson) {
			if (!this.type.isEmpty()) {
				recipeJson.addProperty("type", this.type);
			}

			if (this.features.size() > 1) {
				JsonArray jsonarray = new JsonArray();
				for (String s : this.features) {
					jsonarray.add(s);
				}
				recipeJson.add("features", jsonarray);
			}

			if (this.features.size() == 1) {
				recipeJson.addProperty("features", this.features.getFirst());
			}

			if (this.biomes.size() > 1) {
				JsonArray jsonarray = new JsonArray();
				for (String b : this.biomes) {
					jsonarray.add(b);
				}
				recipeJson.add("biomes", jsonarray);
			}

			if (this.biomes.size() == 1) {
				recipeJson.addProperty("biomes", this.biomes.getFirst());
			}

			if (!this.step.isEmpty()) {
				recipeJson.addProperty("step", this.step);
			}
		}

		public String getType() {
			return this.type;
		}

		public ResourceLocation getId() {
			return this.id;
		}
	}
}