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

package com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationManager;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.Types;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.NumberRangeValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.RequiredValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.ResourceLocationValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.TypeValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.registry.BlockRegistryValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.DepositType;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.FieldSetValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.MaxValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.deposit.MaterialValidator;
import com.ridanisaurus.emendatusenigmatica.util.validation.Validator;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.DepositValidators;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.ridanisaurus.emendatusenigmatica.util.validation.Validator.LOGGER;

public class CommonBlockDefinitionModel {
	public static final Codec<CommonBlockDefinitionModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.STRING.optionalFieldOf("block").forGetter(it -> Optional.ofNullable(it.block)),
			Codec.STRING.optionalFieldOf("tag").forGetter(it -> Optional.ofNullable(it.tag)),
			Codec.STRING.optionalFieldOf("material").forGetter(it -> Optional.ofNullable(it.material)),
			Codec.INT.fieldOf("weight").orElse(100).forGetter(it -> it.weight),
			Codec.INT.fieldOf("min").orElse(-500).forGetter(it -> it.min),
			Codec.INT.fieldOf("max").orElse(500).forGetter(it -> it.max)
	).apply(x, (s, s2, s3, i, i2, i3) -> new CommonBlockDefinitionModel(s.orElse(null), s2.orElse(null), s3.orElse(null), i, i2, i3)));

	public static final ValidationManager VALIDATION_MANAGER = ValidationManager.create()
		.addValidator("block",    new RequiredValidator(false))
		.addValidator("tag",      new RequiredValidator(false))
		.addValidator("material", new MaterialValidator())
		.addValidator("weight",   new TypeValidator(Types.INTEGER, false))
		.addValidator("min",      new FieldSetValidator(
			"root.type",
			DepositType.DIKE.getType(),
			new NumberRangeValidator(Types.INTEGER, -64, 320, false), true)
		).addValidator("max",      new FieldSetValidator(
			"root.type",
			DepositType.DIKE.getType(),
			new MaxValidator(Types.INTEGER, -64, 320, false), true)
		);

	private final String material;
	protected final String block;
	protected final String tag;
	protected final int weight;
	protected final int min;
	protected final int max;

	/**
	 * Holds verifying functions for each field.
	 * Function returns true if verification was successful, false otherwise to stop registration of the json.
	 * Adding suffix _rg will request the original object instead of just the value of the field.
	 * @implNote Validators of min/max are using simplified temp check for checking if the type is dike.<br>
	 * If you need to pass parent to those validators for any other reason,
	 * modify this implementation to check for specific field for those validators!.
	 */
	public static Map<String, BiFunction<JsonElement, Path, Boolean>> validators = new LinkedHashMap<>();

	static {
		validators.put("block", 	new Validator("block").getResourceIDValidation(false));
		validators.put("tag", 		new Validator("tag").getResourceIDValidation(false));
		validators.put("weight", 	new Validator("weight").REQUIRES_INT);
		validators.put("material_rg", DepositValidators.getFullMaterialValidation(new Validator("material")));

		Validator minValidator = new Validator("min");
		Validator maxValidator = new Validator("max");
		validators.put("min_rg", (element, path) -> {
			if (!minValidator.assertParentObject(element, path)) return false;
			JsonObject parent = element.getAsJsonObject();
			JsonElement value = parent.get(minValidator.getName());

			if (Objects.isNull(value)) return true;
			if (!Validator.checkForTEMP(parent, path, false)) {
				LOGGER.warn(
					"\"%s\" should not be present in file \"%s\", as it doesn't take effect on types different than \"%s\"!"
					.formatted(minValidator.getName(), Validator.obfuscatePath(path), DepositType.DIKE.getType())
				);
			}

			return minValidator.REQUIRES_INT.apply(value, path);
		});
		validators.put("max_rg", (element, path) -> {
			if (!maxValidator.assertParentObject(element, path)) return false;
			JsonObject parent = element.getAsJsonObject();
			JsonElement value = parent.get(minValidator.getName());

			if (Objects.isNull(value)) return true;
			if (!Validator.checkForTEMP(parent, path, false)) {
				LOGGER.warn(
					"\"%s\" should not be present in file \"%s\", as it doesn't take effect on types different than \"%s\"!"
					.formatted(maxValidator.getName(), Validator.obfuscatePath(path), DepositType.DIKE.getType())
				);
			}

			return maxValidator.getMaxYLevelValidation("min").apply(parent, path);
		});
	}

	public CommonBlockDefinitionModel(@Nullable String block, @Nullable String tag, @Nullable String material, int weight, int min, int max) {
		this.block = block;
		this.tag = tag;
		this.material = material;
		this.weight = weight;
		this.min = min;
		this.max = max;
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

	public int getWeight() {
		return weight;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}