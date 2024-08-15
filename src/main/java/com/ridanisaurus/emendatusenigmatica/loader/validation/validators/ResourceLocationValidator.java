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

package com.ridanisaurus.emendatusenigmatica.loader.validation.validators;

import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationData;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.Types;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.registry.AbstractRegistryValidator;
import com.ridanisaurus.emendatusenigmatica.util.analytics.Analytics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This Validator is used to validate fields, that need to provide a {@link ResourceLocation}.<br><br>
 * If provided with an implementation of {@link AbstractRegistryValidator},
 * parsed ResourceLocations will be registered for validation after all registries are ready, but before the actual data-gen.
 * @apiNote Take a note that, for now, this class is used for Post-Registration validation;
 * however, this might be changed in the future to a separate solution, which will have more control over modifying already parsed data,
 * to try and prevent a disaster.
 */
public class ResourceLocationValidator extends TypeValidator {
    private static final Map<AbstractRegistryValidator, Map<ResourceLocation, ValidationData>> validators = new HashMap<>();
    private final Map<ResourceLocation, ValidationData> resourceLocations;

    /**
     * Constructs ResourceLocationValidator.
     *
     * @param validator RegistryValidator to use and validate the resource locations.
     * @param isRequired Determines if the field is required. If true, an error will be issued if the field is missing.
     * @see ResourceLocationValidator Documentation of the validator.
     * @implNote This validator accepts only {@link Types#STRING} values.
     */
    public ResourceLocationValidator(@NotNull AbstractRegistryValidator validator, boolean isRequired) {
        super(Types.STRING, isRequired);
        // Store the map reference, for faster access.
        resourceLocations = new HashMap<>();
        validators.put(validator, resourceLocations);
    }

    /**
     * Constructs ResourceLocationValidator, with no Post-Registration step.
     *
     * @param isRequired Determines if the field is required. If true, an error will be issued if the field is missing.
     * @see ResourceLocationValidator Documentation of the validator.
     * @see ResourceLocationValidator#ResourceLocationValidator(AbstractRegistryValidator, boolean)
     * @implNote This validator accepts only {@link Types#STRING} values.
     */
    public ResourceLocationValidator(boolean isRequired) {
        super(Types.STRING, isRequired);
        resourceLocations = null;
    }

    /**
     * Validate method, used to validate passed in object.
     *
     * @param data ValidationData record with necessary information to validate the element.
     * @return True of the validation passes, false otherwise.
     * @apiNote Even tho it's public, this method should <i>never</i> be called directly! Call {@link ResourceLocationValidator#apply(ValidationData)} instead!
     */
    @Override
    public Boolean validate(@NotNull ValidationData data) {
        if (!super.validate(data)) return false;

        // Design note
        // Decided to simplify this validator to just check the format and character set.
        // Even tho neat, there is no sense in providing which part of the Resource Location is missing!
        String value = data.validationElement().getAsString();
        List<String> values = List.of(value.split(":"));
        if (values.size() != 2) {
            Analytics.error("Provided Resource Location (ID) doesn't comply to the format!", "Expected: <code>namespace:id</code>, got: <code>%s</code>".formatted(value), data);
            return false;
        }
        if (!ResourceLocation.isValidNamespace(values.get(0)) || !ResourceLocation.isValidPath(values.get(1))) {
            Analytics.error("Provided Resource Location (ID) contains non [a-z0-9/._-] character!", "Provided value: <code>%s</code>".formatted(value), data);
            return false;
        }
        // Add ResourceLocation for Post-Registration check.
        if (Objects.nonNull(resourceLocations)) resourceLocations.put(ResourceLocation.parse(value), data);
        return true;
    }

    /**
     * Used to execute Post-Registration validation, checking if specified Resource Locations point to valid registry objects.
     * @return False if at least one fatal error is found, true otherwise.
     * @apiNote Currently marked as experimental, could change behavior / be replaced in the future.
     */
    @ApiStatus.Experimental
    public static boolean runRegistryValidation() {
        //TODO: Run in parallel.
        AtomicBoolean result = new AtomicBoolean(true);
        validators.forEach((validator, list) -> list.forEach((resourceLocation, data) -> {
            switch (validator.validate(resourceLocation)) {
                case PASS -> {
                    // Nothing, it passed successfully.
                }
                case ERROR -> Analytics.error(validator.getErrorMessage(), "Missing location: <code>%s</code>".formatted(data.validationElement().getAsString()), data);
                case FATAL -> {
                    Analytics.error(validator.getErrorMessage(), "Missing location: <code>%s</code>".formatted(data.validationElement().getAsString()), data);
                    result.set(false);
                }
            }
        }));
        return result.get();
    }
}
