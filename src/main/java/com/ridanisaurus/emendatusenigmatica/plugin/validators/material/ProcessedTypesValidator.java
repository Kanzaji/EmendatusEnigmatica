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

import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationData;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.AbstractBasicValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import org.jetbrains.annotations.NotNull;

/**
 * Custom validator used to validate Processed Types field in the {@link MaterialModel}.<br>
 */
public class ProcessedTypesValidator extends AbstractBasicValidator {
    /**
     * Constructs AbstractBasicValidator, which requires implementation of {@link AbstractBasicValidator#validate(ValidationData)} method.
     *
     * @param isRequired Determines if the field is required. If true, an error will be issued if the field is missing.
     * @see AbstractBasicValidator Documentation of the validator.
     */
    public ProcessedTypesValidator(boolean isRequired) {
        super(isRequired);
    }

    /**
     * Validate method, used to validate passed in object.
     *
     * @param data ValidationData record with necessary information to validate the element.
     * @return True of the validation passes, false otherwise.
     * @apiNote Even tho it's public, this method should <i>never</i> be called directly! Call {@link AbstractBasicValidator#apply(ValidationData)} instead!
     */
    @Override
    public Boolean validate(@NotNull ValidationData data) {
        return false;
    }
}
