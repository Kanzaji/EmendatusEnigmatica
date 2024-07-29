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

package com.ridanisaurus.emendatusenigmatica.plugin.validation.validators;

import com.google.gson.JsonElement;
import com.ridanisaurus.emendatusenigmatica.plugin.validation.ArrayPolicy;
import com.ridanisaurus.emendatusenigmatica.plugin.validation.ValidationData;
import com.ridanisaurus.emendatusenigmatica.util.Analytics;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * AbstractValidator is a template class, which more advanced validators can extend,
 * to have the basic functions already implemented and ready-to-go.
 * <br><br>
 * This validator handles the field requirement and checks the compliance with the ArrayPolicy,
 * and if the element passes, calls the abstract {@link AbstractValidator#validate(ValidationData)} method.
 * @apiNote Even tho this validator could be easily done to accept arrays of arrays... - the codecs are not created with such fields in mind!
 * Due to that limitation, this validator will issue errors if an array is found inside another array.<br>
 * @implNote This validator doesn't pass an entire array to the {@link AbstractValidator#validate(ValidationData)} method, only elements found inside it.
 * If your validator requires access to the entire array, use {@link AbstractBasicValidator} instead.
 */
public abstract class AbstractValidator implements Function<ValidationData, Boolean> {
    private final boolean isRequired;

    /**
     * Constructs AbstractValidator, which requires implementation of {@link AbstractValidator#validate(ValidationData)} method.
     * @param isRequired Determines if the field is required. If true, an error will be issued if the field is missing.
     * @see AbstractValidator Documentation of the validator.
     */
    public AbstractValidator(boolean isRequired) {
        this.isRequired = isRequired;
    }

    /**
     * Entry point of the validator.
     * @param data ValidationData record with necessary information to validate the element.
     * @return True if the validation passes, false otherwise.
     */
    @Override
    public Boolean apply(@NotNull ValidationData data) {
        var element = data.validationElement();
        if (Objects.isNull(element)) {
            if (!isRequired) return true;
            Analytics.error("This field is required!", data.currentPath(), data.jsonFilePath());
            return false;
        }

        if (element.isJsonArray()) {
            if (data.arrayPolicy() == ArrayPolicy.DISALLOWS_ARRAYS) {
                Analytics.error("Arrays are not allowed for this field!", data.currentPath(), data.jsonFilePath());
                return false;
            }

            int index = 0;
            boolean validation = true;
            for (JsonElement entry : element.getAsJsonArray()) {
                if (!this.validate(new ValidationData(entry, data.rootObject(), "%s[%d]".formatted(data.currentPath(), index), data.jsonFilePath(), data.arrayPolicy())))
                    validation = false;
            }
            return validation;
        }

        if (data.arrayPolicy() == ArrayPolicy.REQUIRES_ARRAY) {
            Analytics.error("This field requires an array!", data.currentPath(), data.jsonFilePath());
            return false;
        }
        return this.validate(data);
    }

    /**
     * Abstract Validate method, used to validate passed in object.
     * @param data ValidationData record with necessary information to validate the element.
     * @return True of the validation passes, false otherwise.
     * @apiNote Even tho it's public, this method should <i>never</i> be called directly! Call {@link AbstractBasicValidator#apply(ValidationData)} instead!
     */
    public abstract Boolean validate(@NotNull ValidationData data);
}
