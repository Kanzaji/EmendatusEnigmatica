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

package com.ridanisaurus.emendatusenigmatica.plugin.deposit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.plugin.DefaultLoader;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.dense.DenseDepositConfigModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.dike.DikeDepositConfigModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.geode.GeodeDepositConfigModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.sphere.SphereDepositConfigModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.vanilla.VanillaDepositConfigModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.util.validation.Validator;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import static com.ridanisaurus.emendatusenigmatica.util.validation.Validator.LOGGER;

/**
 * Class used to manage validators for each deposit type, and as a utility class for DepositValidators,
 * containing a few of the specific validators that can be used between different config types.
 * @see DepositValidators#get(String)
 * @see DepositValidators#get(JsonElement)
 * @see DepositValidators#getMaxYLevelValidation
 */
// I have no clue where getMaxYLevelValidation went - Kanz
public class DepositValidators {
    private static final EmendatusDataRegistry registry = EmendatusEnigmatica.getInstance().getLoader().getDataRegistry();
    private static final Map<String, Map<String, BiFunction<JsonElement, Path, Boolean>>> validators = new HashMap<>();
    static {
        Map<String, BiFunction<JsonElement, Path, Boolean>> common = new LinkedHashMap<>();
        common.put("type",          new Validator("type")       .getRequiredAcceptsOnlyValidation(DefaultLoader.DEPOSIT_TYPES, false));
        common.put("dimension",     new Validator("dimension")  .getRequiredResourceIDValidation(false));
        common.put("biomes",        new Validator("biomes")     .getResourceIDValidation(true));
        common.put("registryName",  new Validator("registryName").getIDValidation(DefaultLoader.DEPOSIT_IDS));
        // This one should be overridden for each type, it's only as a "default" for an unknown type.
        common.put("config", new Validator("config").REQUIRED);
        validators.put("common", common);
        // Here you can add validators for specific Deposit Types. Take a note for each "type" all common validators are going to be added.
        // Add entry with the same key to override validator if it's not required for your type.
        validators.put(DepositType.VANILLA.getType(),new LinkedHashMap<>(Map.of("config", new Validator("config").getRequiredObjectValidation(VanillaDepositConfigModel.validators, false))));
        validators.put(DepositType.SPHERE.getType(), new LinkedHashMap<>(Map.of("config", new Validator("config").getRequiredObjectValidation(SphereDepositConfigModel.validators, false))));
        validators.put(DepositType.GEODE.getType(),  new LinkedHashMap<>(Map.of("config", new Validator("config").getRequiredObjectValidation(GeodeDepositConfigModel.validators, false))));
        validators.put(DepositType.DENSE.getType(),  new LinkedHashMap<>(Map.of("config", new Validator("config").getRequiredObjectValidation(DenseDepositConfigModel.validators, false))));
        validators.put(DepositType.DIKE.getType(),   new LinkedHashMap<>(Map.of("config", new Validator("config").getRequiredObjectValidation(DikeDepositConfigModel.validators, false))));
    }

    /**
     * Used to get a validator for Material Field, when Block and Tag fields are as an alternatives.
     * Requires usage of _rg suffix.
     * @param validator Validator to use for the template.
     * @return BiFunction used as a validator.
     */
    public static BiFunction<JsonElement, Path, Boolean> getFullMaterialValidation(Validator validator) {
        return (element, path) -> {
            if (!validator.assertParentObject(element, path)) return false;
            JsonObject parent = element.getAsJsonObject();
            JsonElement blockElement = parent.get("block");
            JsonElement materialElement = parent.get("material");
            JsonElement tagElement = parent.get("tag");

            if (Objects.isNull(blockElement) && Objects.isNull(materialElement) && Objects.isNull(tagElement)) {
                LOGGER.error("Either \"block\", \"material\" or \"tag\" is required to be present in the file \"%s\".".formatted(Validator.obfuscatePath(path)));
                return false;
            }
            if (
                    (Objects.nonNull(materialElement) && (Objects.nonNull(blockElement) || Objects.nonNull(tagElement))) ||
                    (Objects.nonNull(blockElement) && Objects.nonNull(tagElement))
            ) {
                LOGGER.error("\"tag\", \"block\" and \"material\" can't be present at the same time in the file \"%s\".".formatted(Validator.obfuscatePath(path)));
                return false;
            }
            if (Objects.nonNull(blockElement)) return true;
            if (Objects.nonNull(tagElement)) return true;
            return getOnlyMaterialValidation(validator).apply(materialElement, path);
        };
    }

    /**
     * Used to get a validator for Material field, when Block Field is an alternative. Requires usage of _rg suffix.
     * @param validator Validator to use for the template.
     * @return BiFunction used as a validator.
     */
    public static BiFunction<JsonElement, Path, Boolean> getBlockMaterialValidation(Validator validator) {
        return (element, path) -> {
            if (!validator.assertParentObject(element, path)) return false;
            JsonObject parent = element.getAsJsonObject();
            JsonElement blockElement = parent.get("block");
            JsonElement materialElement = parent.get("material");

            if (Objects.isNull(blockElement) && Objects.isNull(materialElement)) {
                LOGGER.error("Either \"block\" or \"material\" is required to be present in the file \"%s\".".formatted(Validator.obfuscatePath(path)));
                return false;
            }
            if (Objects.nonNull(blockElement) && Objects.nonNull(materialElement)) {
                LOGGER.error("\"block\" and \"material\" can't be present at the same time in the file \"%s\".".formatted(Validator.obfuscatePath(path)));
                return false;
            }
            if (Objects.nonNull(blockElement)) return true;
            return getOnlyMaterialValidation(validator).apply(materialElement, path);
        };
    }

    /**
     * Used to get a validator for Material Field. Checks if Material under that ID is registered and if it contains "ore" as processed type.
     * @param validator Validator to use for the template.
     * @return BiFunction used as a validator
     * @apiNote This does not check for anything related to other possible fields, like block or tag.
     * Use {@link DepositValidators#getBlockMaterialValidation(Validator)} instead.
     */
    public static BiFunction<JsonElement, Path, Boolean> getOnlyMaterialValidation(Validator validator) {
        return (element, path) -> {
            if (!validator.getRequiredNonEmptyValidation(false).apply(element, path)) return false;
            String materialID = element.getAsString();
            MaterialModel material = registry.getMaterial(materialID);
            if (Objects.isNull(material)) {
                LOGGER.error("\"material\" (%s) found in file \"%s\" isn't registered in \"Material Registry\"!".formatted(materialID, Validator.obfuscatePath(path)));
                return false;
            }
            if (!material.getProcessedTypes().contains("ore")) {
                LOGGER.error(
                    "Specified \"material\" (%s) found in file \"%s\" doesn't contain \"ore\" as a processed type. Usage of \"material\" field is prohibited in this situation."
                    .formatted(materialID, Validator.obfuscatePath(path))
                );
                return false;
            }
            return true;
        };
    }

    /**
     * Used to get validators for a provided deposit type.
     * @param type Deposit type to get validators for.
     * @return Map with validators for a specified type + common validators.
     * @apiNote Returns only common validators if a specified type is not registered.
     */
    public static Map<String, BiFunction<JsonElement, Path, Boolean>> get(String type) {
        var commonValidators = new LinkedHashMap<>(validators.get("common"));
        if (!type.equals("common")) {
            var typeValidators = validators.get(type);
            if (Objects.nonNull(typeValidators)) commonValidators.putAll(typeValidators);
        }
        return commonValidators;
    }

    /**
     * Used to get validators for a provided JsonElement containing a deposit type.
     * @param typeElement JsonElement with a Deposit type to get validators for.
     * @return Map with validators for a specified type + common validators.
     * @apiNote Returns only common validators if a specified type is not registered, or the typeElement is null / not Primitive.
     */
    public static Map<String, BiFunction<JsonElement, Path, Boolean>> get(@Nullable JsonElement typeElement) {
        if (Objects.isNull(typeElement) || !typeElement.isJsonPrimitive()) return get("common");
        return get(typeElement.getAsString());
    }
}
