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

package com.ridanisaurus.emendatusenigmatica.plugin.validators.material.gas;

import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.FilterMode;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.Types;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.IValidationFunction;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.NumberRangeValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.ValuesValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.FieldTrueValidator;

import java.util.List;

public class CoolantValidator extends FieldTrueValidator {
    private static final IValidationFunction validator = new NumberRangeValidator(Types.FLOAT, 0, Float.MAX_VALUE, false);
    /**
     * Constructs CoolantValidator.
     *
     * @see CoolantValidator Documentation of the validator.
     */
    public CoolantValidator() {
        super("isCoolant", validator);
    }
}
