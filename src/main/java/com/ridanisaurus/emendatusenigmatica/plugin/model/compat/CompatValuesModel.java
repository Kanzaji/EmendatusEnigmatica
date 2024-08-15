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
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationManager;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.ArrayPolicy;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.RequiredValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.compat.CompatTypeValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.compat.CompatValueInputValidator;
import com.ridanisaurus.emendatusenigmatica.util.validation.Validator;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

import static com.ridanisaurus.emendatusenigmatica.util.validation.Validator.LOGGER;

public class CompatValuesModel {
    public static final Codec<CompatValuesModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("type").forGetter(i -> i.type),
            Codec.list(CompatIOModel.CODEC).fieldOf("input").orElse(List.of()).forGetter(i -> i.input),
            Codec.list(CompatIOModel.CODEC).fieldOf("output").orElse(List.of()).forGetter(i -> i.output)
    ).apply(x, CompatValuesModel::new));

    public static final ValidationManager VALIDATION_MANAGER = ValidationManager.create()
        .addValidator("type", new CompatTypeValidator())
        .addValidator("input", new CompatValueInputValidator(), ArrayPolicy.REQUIRES_ARRAY)
        .addValidator("output", CompatIOModel.VALIDATION_MANAGER.getAsValidator(false), ArrayPolicy.REQUIRES_ARRAY);

    private final String type;
    private final List<CompatIOModel> input;
    private final List<CompatIOModel> output;

    /**
     * Holds all acceptable mods and validators for their machines for the CompatModel.
     *
     * @apiNote To add support for additional mod + machines, add an entry to this map with mod name and validator of the machine.
     * @see CompatRecipesModel#acceptableMods
     * @see CompatRecipesModel#acceptableModIds
     * @see CompatValuesModel#acceptableMods
     */
    public static final Map<String, BiFunction<JsonElement, Path, Boolean>> acceptableMods = new HashMap<>();

    /**
     * Holds verifying functions for each field.
     * Function returns true if verification was successful, false otherwise to stop registration of the json.
     * Adding suffix _rg will request the original object instead of just the value of the field.
     */
    public static final Map<String, BiFunction<JsonElement, Path, Boolean>> validators = new LinkedHashMap<>();

    CompatValuesModel(String type, List<CompatIOModel> input, List<CompatIOModel> output) {
        this.type = type;
        this.input = input;
        this.output = output;
    }

    public String getType() {
        return type;
    }

    public List<CompatIOModel> getInput() {
        return input;
    }

    public List<CompatIOModel> getOutput() {
        return output;
    }

    static {
        //TODO: Move thermal and create validators to the respective addons.
        Validator typeValidator = new Validator("type");
        Validator inputValidator = new Validator("input");
        Validator outputValidator = new Validator("output");

        acceptableMods.put("thermal", typeValidator.getRequiredAcceptsOnlyValidation(List.of("ore", "raw", "alloy"), false));
        acceptableMods.put("create", typeValidator.getRequiredAcceptsOnlyValidation(List.of("ore", "crushed_ore"), false));

        validators.put("type_rg", (element, path) -> {
            if (!typeValidator.assertParentObject(element, path)) return false;

            String mod = element.getAsJsonObject().get("TEMP").getAsJsonObject().get("mod").getAsString();
            if (mod.equals("NONE")) {
                LOGGER.error("Mod is none! Can't verify values of type for \"%s\"".formatted(Validator.obfuscatePath(path)));
                return false;
            }

            var validator = acceptableMods.get(mod);
            if (Objects.isNull(validator)) {
                LOGGER.error("Illegal value for mod present! Can't verify values of type for \"%s\"".formatted(Validator.obfuscatePath(path)));
                return false;
            }
            return validator.apply(element.getAsJsonObject().get("type"), path);
        });

        validators.put("input_rg", (element_rg, path) -> {
            if (!inputValidator.assertParentObject(element_rg, path)) return false;

            JsonObject element = element_rg.getAsJsonObject();
            JsonObject temp = element.get("TEMP").getAsJsonObject();
            String mod = temp.get("mod").getAsString();
            String machine = temp.get("machine").getAsString();
            String type = "NONE";
            boolean required = true;

            if (mod.equals("NONE"))
                LOGGER.warn("Mod is none! Can't accurately verify values of input for \"%s\"".formatted(Validator.obfuscatePath(path)));
            if (machine.equals("NONE"))
                LOGGER.warn("Machine is none! Can't accurately verify values of input for \"%s\"".formatted(Validator.obfuscatePath(path)));

            try {
                type = element.get("type").getAsString();
            } catch (ClassCastException e) {
                LOGGER.error("Type is not a string! Can't accurately verify values of input for \"%s\".".formatted(Validator.obfuscatePath(path)));
            } catch (NullPointerException e) {
                LOGGER.warn("Type is null! Can't accurately verify values of input for \"%s\"".formatted(Validator.obfuscatePath(path)));
            }


            if (!mod.equals("thermal")) {
                LOGGER.warn("Input should not be present when selected mod is different than thermal (Currently: %s). Found in file \"%s\".".formatted(mod, Validator.obfuscatePath(path)));
                required = false;
            }
            if (!machine.equals("induction_smelter")) {
                LOGGER.warn("Input should not be present when selected machine is different than induction_smelter (Currently: %s). Found in file \"%s\".".formatted(machine, Validator.obfuscatePath(path)));
                required = false;
            }
            if (!type.equals("alloy")) {
                LOGGER.warn("Input should not be present when selected type is different than alloy (Currently: %s). Found in file \"%s\".".formatted(type, Validator.obfuscatePath(path)));
                required = false;
            }

            if (LOGGER.shouldLog) element.get("input").getAsJsonArray().forEach(entry -> {
                if (Validator.isOtherFieldPresent("chance", entry.getAsJsonObject()))
                    LOGGER.warn("Chance should not be present in input object! Found in file \"%s\".".formatted(Validator.obfuscatePath(path)));
            });

            if (required) {
                return inputValidator.getRequiredObjectValidation(CompatIOModel.validators, true).apply(element.get("input"), path);
            } else {
                return inputValidator.getObjectValidation(CompatIOModel.validators, true).apply(element.get("input"), path);
            }
        });

        validators.put("output", outputValidator.getObjectValidation(CompatIOModel.validators, true));
    }
}
