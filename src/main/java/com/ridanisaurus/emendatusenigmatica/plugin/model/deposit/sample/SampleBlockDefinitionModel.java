/*
 * MIT License
 *
 * Copyright (c) 2024. Ridanisaurus
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

package com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.sample;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.plugin.DefaultLoader;
import com.ridanisaurus.emendatusenigmatica.util.validation.Validator;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.DepositValidators;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class SampleBlockDefinitionModel {
	public static final Codec<SampleBlockDefinitionModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.STRING.optionalFieldOf("block").forGetter(it -> Optional.ofNullable(it.block)),
			Codec.STRING.optionalFieldOf("tag").forGetter(it -> Optional.ofNullable(it.tag)),
			Codec.STRING.optionalFieldOf("material").forGetter(it -> Optional.ofNullable(it.material)),
			Codec.INT.fieldOf("weight").orElse(100).forGetter(it -> it.weight),
			Codec.STRING.optionalFieldOf("strata").forGetter(it -> Optional.ofNullable(it.strata))
	).apply(x, (s, s2, s3, i, s4) -> new SampleBlockDefinitionModel(s.orElse(null), s2.orElse(null), s3.orElse(null), i, s4.orElse(null))));
	protected final String block;
	protected final String tag;
	private final String material;
	protected final int weight;
	private final String strata;

	/**
	 * Holds verifying functions for each field.
	 * Function returns true if verification was successful, false otherwise to stop registration of the json.
	 * Adding suffix _rg will request the original object instead of just the value of the field.
	 */
	public static Map<String, BiFunction<JsonElement, Path, Boolean>> validators = new LinkedHashMap<>();

	static {
		validators.put("block", 	new Validator("block").getResourceIDValidation(false));
		validators.put("tag", 		new Validator("tag").getResourceIDValidation(false));
		validators.put("weight", 	new Validator("weight").REQUIRES_INT);
		validators.put("material_rg", DepositValidators.getFullMaterialValidation(new Validator("material")));

		Validator strataValidator = new Validator("strata");
		validators.put("strata_rg", (element, path) -> {
			if (!strataValidator.assertParentObject(element, path)) return false;
			JsonObject parent = element.getAsJsonObject();

			if (Validator.isOtherFieldPresent("material", parent))
				return strataValidator.getRequiredRegisteredIDValidation(DefaultLoader.STRATA_IDS, "Strata Registry", false).apply(parent.get(strataValidator.getName()), path);

			Validator.LOGGER.warn(
				"\"%s\" should not be present when specified sample is not material based in file \"%s\"."
				.formatted(strataValidator.getName(), Validator.obfuscatePath(path))
			);
			return strataValidator.getRegisteredIDValidation(DefaultLoader.STRATA_IDS, "Strata Registry", false).apply(parent.get(strataValidator.getName()), path);
		});
	}

	public SampleBlockDefinitionModel(@Nullable String block, @Nullable String tag, @Nullable String material, int weight, @Nullable String strata) {
		this.block = block;
		this.tag = tag;
		this.weight = weight;
		this.material = material;
		this.strata = strata;
	}

	public int getWeight() {
		return weight;
	}

	public @Nullable String getBlock() {
		return block;
	}

	public @Nullable String getTag() {
		return tag;
	}

	public @Nullable String getMaterial() {
		return material;
	}

	public @Nullable String getStrata() {
		return strata;
	}
}