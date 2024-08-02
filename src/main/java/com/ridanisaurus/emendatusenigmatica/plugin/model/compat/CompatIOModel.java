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

package com.ridanisaurus.emendatusenigmatica.plugin.model.compat;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationManager;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.Types;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.NumberRangeValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.ResourceLocationValidator;
import com.ridanisaurus.emendatusenigmatica.util.validation.Validator;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class CompatIOModel {
    public static final Codec<CompatIOModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.optionalFieldOf("item").forGetter(i -> Optional.ofNullable(i.item)),
            Codec.INT.optionalFieldOf("count").forGetter(i -> Optional.of(i.count)),
            Codec.FLOAT.optionalFieldOf("chance").forGetter(i -> Optional.of(i.chance))
    ).apply(x, (item, count, chance) -> new CompatIOModel(
            item.orElse(""),
            count.orElse(0),
            chance.orElse(0.0f)
    )));

    public static final ValidationManager VALIDATION_MANAGER = ValidationManager.create()
        .addValidator("item", new ResourceLocationValidator(false))
        .addValidator("count", new NumberRangeValidator(Types.INTEGER, 0, 64, false))
        .addValidator("chance", new NumberRangeValidator(Types.FLOAT, 0, 1, false));

    /**
     * Holds verifying functions for each field.
     * Function returns true if verification was successful, false otherwise to stop registration of the json.
     * Adding suffix _rg will request the original object instead of just the value of the field.
     */
    public static final Map<String, BiFunction<JsonElement, Path, Boolean>> validators = new LinkedHashMap<>();

    private final String item;
    private final int count;
    private final float chance;

    public CompatIOModel(String item, int count, float chance) {
        this.item = item;
        this.count = count;
        this.chance = chance;
    }

    public String getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public float getChance() {
        return chance;
    }

    static {
        validators.put("item", new Validator("item").getResourceIDValidation(false));
        validators.put("count", new Validator("count").getIntRange(0, 64, false));
        validators.put("chance", new Validator("chance").getRange(0, 1, false));
    }
}
