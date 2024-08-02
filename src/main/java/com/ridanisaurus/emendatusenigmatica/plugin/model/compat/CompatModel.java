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

package com.ridanisaurus.emendatusenigmatica.plugin.model.compat;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationManager;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.ArrayPolicy;
import com.ridanisaurus.emendatusenigmatica.plugin.DefaultLoader;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.EERegistryValidator;
import com.ridanisaurus.emendatusenigmatica.util.validation.Validator;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

public class CompatModel {
	public static final Codec<CompatModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.STRING.fieldOf("id").forGetter(i -> i.id),
			Codec.list(CompatRecipesModel.CODEC).fieldOf("recipes").forGetter(i -> i.recipes)
	).apply(x, CompatModel::new));

	public static final ValidationManager VALIDATION_MANAGER = ValidationManager.create()
		.addValidator("id", new EERegistryValidator(DefaultLoader.MATERIAL_IDS, EERegistryValidator.REQUIRES_REGISTERED, "Material", true))
		.addValidator("recipes", CompatRecipesModel.VALIDATION_MANAGER.getAsValidator(true), ArrayPolicy.REQUIRES_ARRAY);

	private final String id;
	private final List<CompatRecipesModel> recipes;

	/**
	 * Holds verifying functions for each field.
	 * Function returns true if verification was successful, false otherwise to stop registration of the json.
	 */
	public static final Map<String, BiFunction<JsonElement, Path, Boolean>> validators = new LinkedHashMap<>();

	public CompatModel(String id, List<CompatRecipesModel> recipes) {
		this.id = id;
		this.recipes = recipes;
	}

	public CompatModel() {
		this.id = "";
		this.recipes = List.of();
	}

	public final String getId() {
		return id;
	}

	public final List<CompatRecipesModel> getRecipes() {
		return recipes;
	}

	static {
		validators.put("id", new Validator("id").getRequiredRegisteredIDValidation(DefaultLoader.MATERIAL_IDS, "Material Registry", false));
		validators.put("recipes", new Validator("recipes").getRequiredObjectValidation(CompatRecipesModel.validators, true));
	}
}