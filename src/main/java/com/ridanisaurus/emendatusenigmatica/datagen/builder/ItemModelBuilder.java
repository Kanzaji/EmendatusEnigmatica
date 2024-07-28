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

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ItemModelBuilder {

	private final String parent;
	private final Map<String, String> textures = Maps.newLinkedHashMap();
	private final List<OverrideBuilder> overrides = new ArrayList<>();
	private boolean applyTint = false;
	private String fluid;
	private String loader;

	public ItemModelBuilder(String parent) {
		this.parent = parent;
	}

	public ItemModelBuilder(String parentNamespace, String parentPath) {
		// Conversion to ResourceLocation acts like a sanity check if the Namespace / Path are correct.
		this.parent = ResourceLocation.fromNamespaceAndPath(parentNamespace, parentPath).toString();
	}

	public ItemModelBuilder texture(String textureKey, String textureValue) {
		this.textures.put(textureKey, textureValue);
		return this;
	}

	public ItemModelBuilder texture(String textureKey, String textureNamespace, String texturePath) {
		return this.texture(textureKey, ResourceLocation.fromNamespaceAndPath(textureNamespace, texturePath).toString());
	}

	public ItemModelBuilder applyTint(boolean tint){
		this.applyTint = tint;
		return this;
	}

	public ItemModelBuilder fluid(String fluid){
		this.fluid = fluid;
		return this;
	}

	public ItemModelBuilder loader(String loader){
		this.loader = loader;
		return this;
	}

	public OverrideBuilder override() {
		OverrideBuilder ret = new OverrideBuilder();
		overrides.add(ret);
		return ret;
	}

	public OverrideBuilder override(int index) {
		Preconditions.checkElementIndex(index, overrides.size(), "override");
		return overrides.get(index);
	}

	public void save(@NotNull Consumer<IFinishedGenericJSON> consumer, ResourceLocation jsonResourceLocation) {
		consumer.accept(new Result(jsonResourceLocation, this.parent, this.textures, this.applyTint, this.fluid, this.loader, this.overrides));
	}

	public void save(@NotNull Consumer<IFinishedGenericJSON> consumer, String namespace, String path) {
		this.save(consumer, ResourceLocation.fromNamespaceAndPath(namespace, path));
	}

	public class OverrideBuilder {

		private ResourceLocation model;
		private final Map<ResourceLocation, Float> predicates = new LinkedHashMap<>();

		public OverrideBuilder model(ResourceLocation model) {
			this.model = model;
			return this;
		}

		public OverrideBuilder model(String namespace, String path) {
			this.model = ResourceLocation.fromNamespaceAndPath(namespace, path);
			return this;
		}

		public OverrideBuilder predicate(ResourceLocation key, float value) {
			this.predicates.put(key, value);
			return this;
		}

		public OverrideBuilder predicate(String namespace, String path, float value) {
			this.predicates.put(ResourceLocation.fromNamespaceAndPath(namespace, path), value);
			return this;
		}

		public ItemModelBuilder end() { return ItemModelBuilder.this; }

		JsonObject toJson() {
			JsonObject ret = new JsonObject();
			JsonObject predicatesJson = new JsonObject();
			predicates.forEach((key, val) -> predicatesJson.addProperty(key.toString(), val));
			ret.add("predicate", predicatesJson);
			ret.addProperty("model", model.toString());
			return ret;
		}
	}

	public static class Result implements IFinishedGenericJSON {
		private final ResourceLocation id;
		private final String parent;
		private final Map<String, String> textures;
		private final boolean applyTint;
		private final String fluid;
		private final String loader;
		private final List<OverrideBuilder> overrides;

		public Result(ResourceLocation id, String parent, Map<String, String> textures, boolean applyTint, @Nullable String fluid, @Nullable String loader, List<OverrideBuilder> overrides) {
			this.id = id;
			this.parent = parent;
			this.textures = textures;
			this.applyTint = applyTint;
			this.fluid = fluid;
			this.loader = loader;
			this.overrides = overrides;
		}

		public void serializeJSONData(JsonObject json) {
			if (!this.parent.isEmpty()) {
				json.addProperty("parent", this.parent);
			}
			if (!this.textures.isEmpty()) {
				JsonObject texturesObject = new JsonObject();
				for (Map.Entry<String, String> e : this.textures.entrySet()) {
					texturesObject.addProperty(e.getKey(), e.getValue());
				}
				json.add("textures", texturesObject);
			}
			//TODO: Apparently applyTint is not used anymore?
			if (this.applyTint) {
				json.addProperty("apply_tint", true);
			}
			if (this.fluid != null) {
				json.addProperty("fluid", this.fluid);
			}
			if (this.loader != null) {
				json.addProperty("loader", this.loader);
			}
			if (!this.overrides.isEmpty()) {
				JsonArray overridesJson = new JsonArray();
				overrides.stream().map(OverrideBuilder::toJson).forEach(overridesJson::add);
				json.add("overrides", overridesJson);
			}
		}

		public ResourceLocation getId() {
			return this.id;
		}
	}
}