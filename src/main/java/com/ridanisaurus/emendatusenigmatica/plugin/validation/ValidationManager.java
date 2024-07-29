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

package com.ridanisaurus.emendatusenigmatica.plugin.validation;

import com.ridanisaurus.emendatusenigmatica.plugin.validation.logger.ValidationLoggerOld;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonObject;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class ValidationManager {
    protected static final ValidationLoggerOld logger = new ValidationLoggerOld("Validation Manager");
    private final Map<String, ValidatorHolder> validators = new HashMap<>();
    private final String phase;

    public ValidationManager(String validationStep) {
        this.phase = validationStep;
    }

    /**
     * Used to start the validation of the JsonObject.
     * @param object JsonObject to validate.
     * @param jsonPath Path of the file.
     * @return True if validation passes, false otherwise.
     */
    public boolean validate(@NotNull JsonObject object, Path jsonPath) {
        var path = obfuscatePath(jsonPath);

        if (!object.isJsonObject()) {
            logger.error("Expected Json Object while validating file \"%s\"!".formatted(path));
            return false;
        }

        if (object.isEmpty()) {
            logger.error("Object in file \"%s\" is empty!".formatted(path));
            return false;
        }

        // Enters automatic validator execution. After this point, stack-traces are a mess!
        return this.validate(new ValidationData(object, object, "root", path, ArrayPolicy.DISALLOWS_ARRAYS));
    }

    /**
     * Used to pass this ValidationManager as a sub-object validator.
     * @param data DataHolder of the currently validated field.
     * @return True if validation passes, false otherwise.
     * @apiNote This is meant to be used as a method reference, when adding validators to a different ValidationManager!
     */
    public boolean validate(@NotNull ValidationData data) {
        var element = data.validationElement();
        var path = data.currentPath();
        var jsonPath = data.jsonFilePath();
        if (!element.isJsonObject()) {
            logger.error("Expected Json Object at \"%s\" while validating file \"%s\"!".formatted(path, jsonPath));
            return false;
        }

        JsonObject object = element.getAsJsonObject();
        AtomicBoolean validation = new AtomicBoolean(true);

        if (logger.enabled()) object.entrySet().forEach(entry -> {
            if (!(validators.containsKey(entry.getKey()))) {
                logger.warn("Unknown key (\"%s\") found in file \"%s\".".formatted(path + entry.getKey(), jsonPath));
            }
        });

        validators.forEach((field, holder) -> {
            if (!holder.validate(ValidationData.getWithField(data, field))) validation.set(false);
        });

        return validation.get();
    }

    public static boolean validateArray(@NotNull ValidationData data, Function<ValidationData, Boolean> validator) {
        if (data.validationElement().isJsonArray()) {
            if (data.arrayPolicy() == ArrayPolicy.DISALLOWS_ARRAYS) {
                logger.error("Arrays are not allowed at \"%s\" in file \"%s\"");
            }
        }
        return true;
    }

    /**
     * Used to cut off part of the path that is not in minecraft directory.<br>
     * <h4>Input:</h4>
     * <blockquote>
     * C:/Minecraft/config/emendatusenigmatica/strata/stone.json
     * </blockquote>
     * <h4>Result:</h4>
     * <blockquote>
     * strata/stone.json
     * </blockquote>
     * @param path Path to obfuscate.
     * @return String with an obfuscated path.
     */
    public static @NotNull String obfuscatePath(Path path) {;
        return logger.getLogPath().getParent().relativize(path).toString();
    }
}
