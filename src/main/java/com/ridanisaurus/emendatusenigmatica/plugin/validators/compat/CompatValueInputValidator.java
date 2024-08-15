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

package com.ridanisaurus.emendatusenigmatica.plugin.validators.compat;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationData;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationHelper;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.Types;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.AbstractValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.model.compat.CompatIOModel;
import com.ridanisaurus.emendatusenigmatica.util.analytics.Analytics;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CompatValueInputValidator extends AbstractValidator {
    private static final AbstractValidator VALIDATION_FUNCTION = CompatIOModel.VALIDATION_MANAGER.getAsValidator(false);
    public static final Table<String, String, String> VALUES = HashBasedTable.create(
        ImmutableTable.of("thermal", "induction_smelter", "alloy")
    );

    /**
     * Constructs CompatValueInputValidator.
     *
     * @see CompatValueInputValidator Documentation of the validator.
     */
    public CompatValueInputValidator() {
        super(false);
    }

    /**
     * Validate method, used to validate passed in object.
     *
     * @param data ValidationData record with necessary information to validate the element.
     * @return True of the validation passes, false otherwise.
     * @apiNote Even tho it's public, this method should <i>never</i> be called directly! Call {@link CompatValueInputValidator#apply(ValidationData)} instead!
     * @implSpec Take a note that the {@link ValidationData#validationElement()} will never return null.
     */
    @Override
    public Boolean validate(@NotNull ValidationData data) {
        return VALIDATION_FUNCTION.validate(data);
    }

    /**
     * Method used to determine if the validator is required on runtime.
     *
     * @param data ValidationData record with necessary information to validate the element.
     * @return True if current element is required, false if not.
     * @implNote By default, returns the value specified in the constructor.
     */
    @Override
    public boolean isRequired(@NotNull ValidationData data) {
        var mainPath = StringUtils.substringBeforeLast(data.getParentPath(), ".");
        var modPath = mainPath + ".mod";
        var machinePath = mainPath + ".machine";
        var typePath = data.getParentFieldPath("type");
        var modElement = ValidationHelper.getElementFromPathAs(data.rootObject(), modPath, Types.STRING);
        var machineElement = ValidationHelper.getElementFromPathAs(data.rootObject(), machinePath, Types.STRING);
        var typeElement = data.getParentFieldAs(Types.STRING, "type");

        if (Objects.nonNull(modElement) && Objects.nonNull(typeElement) && Objects.nonNull(machineElement)) {
            var acceptedType = VALUES.get(modElement.getAsString(), machineElement.getAsString());
            if (Objects.nonNull(acceptedType) && acceptedType.equals(machineElement.getAsString())) return true;
        }

        CompatValueAnalyticsAddon.shouldRun = true;
        Analytics.warn(
            "This field is unnecessary!",
            "Scenarios when this field is required can be found at the end of this file.",
            data
        );

        return false;
    }

    /**
     * Method used to provide additional field for the "This field is required!" error.
     *
     * @param data
     * @return String with an additional message or null.
     */
    @Override
    public String getAdditional(@NotNull ValidationData data) {
        return "Scenarios when this field is required can be found at the end of this file.";
    }
}
