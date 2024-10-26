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

package com.ridanisaurus.emendatusenigmatica.plugin.validators.material;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationData;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.ArrayPolicy;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.FilterMode;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.AbstractValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.IValidationFunction;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.ValuesValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.deprecation.DeprecatedValueValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.util.analytics.Analytics;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Heavily modified {@link ValuesValidator} for validation of Processed Types in {@link MaterialModel}.
 * @apiNote Supports only {@link ArrayPolicy#REQUIRES_ARRAY}.<br>
 * In practice, specified ArrayPolicy is ignored, and this validator acts as REQUIRES_ARRAY is always specified.
 */
public class ProcessedTypesValidator extends ValuesValidator {
    /**
     * @deprecated This field is temporary and will be removed next breaking update.
     */
    @Deprecated(since = "2.2.0", forRemoval = true)
    private static final IValidationFunction deprecatedValidator = new DeprecatedValueValidator(List.of(
        new DeprecatedValueValidator.ValueHolder("helmet",      "armor", ""),
        new DeprecatedValueValidator.ValueHolder("chestplate",  "armor", ""),
        new DeprecatedValueValidator.ValueHolder("leggings",    "armor", ""),
        new DeprecatedValueValidator.ValueHolder("boots",       "armor", "")
    ));

    /**
     * Constructs ProcessedTypesValidator.
     *
     * @see ProcessedTypesValidator Documentation of the validator.
     */
    public ProcessedTypesValidator() {
        super(List.of(
            "storage_block",
            "ingot",
            "gem",
            "ore",
            "raw",
            "nugget",
            "dust",
            "plate",
            "gear",
            "rod",
            "fluid",
            "cluster",
            "sword",
            "pickaxe",
            "axe",
            "shovel",
            "hoe",
            "paxel",
            "shield",
            "armor",
            //TODO: Remove next breaking update. Left here so errors are not generated for deprecated values
            "helmet",
            "chestplate",
            "leggings",
            "boots",
            //TODO: Move to the addons. Somehow
            "infuse_type",
            "gas",
            "slurry",
            "crystal",
            "shard",
            "clump",
            "dirty_dust",
            "crushed_ore"
        ), FilterMode.WHITELIST, true);
    }

    /**
     * Entry point of the validator.
     *
     * @param data ValidationData record with necessary information to validate the element.
     * @return True if the validation passes, false otherwise.
     * @implSpec Modified copy of {@link AbstractValidator} impl.
     */
    @Override
    public Boolean apply(@NotNull ValidationData data) {
        var element = data.validationElement();
        if (Objects.isNull(element)) {
            Analytics.error("This field is required!", getAdditional(data), data);
            return false;
        }

        if (element.isJsonArray()) {
            // TODO: Remove next breaking update.
            // This is here because armor values are deprecated and replaced by "armor" value.
            ProcessedTypesValidator.deprecatedValidator.apply(data);

            int index = 0;
            boolean validation = true;
            var array = element.getAsJsonArray();
            for (JsonElement entry : array) {
                if (!this.validate(new ValidationData(entry, data.rootObject(), "%s[%d]".formatted(data.currentPath(), index), data.jsonFilePath(), data.arrayPolicy())))
                    validation = false;
            }

            // Illegal pairs check.
            if (array.contains(new JsonPrimitive("ingot")) && array.contains(new JsonPrimitive("gem"))) {
                Analytics.error(
                    "Illegal pair of values found!",
                    "<code>ingot</code> and <code>gem</code> can't be present in the <code>%s</code> array at the same time!".formatted(data.currentPath()),
                    data
                );
                return false;
            }

            return validation;
        }

        Analytics.error("This field requires an array!", data);
        return false;
    }
}
