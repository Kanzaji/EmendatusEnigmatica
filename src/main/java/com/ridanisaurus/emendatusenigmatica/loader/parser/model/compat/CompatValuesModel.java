package com.ridanisaurus.emendatusenigmatica.loader.parser.model.compat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.Validator;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

import static com.ridanisaurus.emendatusenigmatica.loader.Validator.LOGGER;

public class CompatValuesModel {
    public static final Codec<CompatValuesModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("type").forGetter(i -> i.type),
            Codec.list(CompatIOModel.CODEC).fieldOf("input").orElse(List.of()).forGetter(i -> i.input),
            Codec.list(CompatIOModel.CODEC).fieldOf("output").orElse(List.of()).forGetter(i -> i.output)
    ).apply(x, CompatValuesModel::new));

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
