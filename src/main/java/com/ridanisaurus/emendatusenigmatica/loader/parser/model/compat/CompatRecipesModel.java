package com.ridanisaurus.emendatusenigmatica.loader.parser.model.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.Validator;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

import static com.ridanisaurus.emendatusenigmatica.loader.Validator.LOGGER;

public class CompatRecipesModel {
    public static final Codec<CompatRecipesModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("mod").forGetter(i -> i.mod),
            Codec.STRING.fieldOf("machine").forGetter(i -> i.machine),
            Codec.list(CompatValuesModel.CODEC).fieldOf("values").forGetter(i -> i.values)
    ).apply(x, CompatRecipesModel::new));

    private final String mod;
    private final String machine;
    private final List<CompatValuesModel> values;

    /**
     * Holds verifying functions for each field.
     * Function returns true if verification was successful, false otherwise to stop registration of the json.
     * Adding suffix _rg will request the original object instead of just the value of the field.
     */
    public static final Map<String, BiFunction<JsonElement, Path, Boolean>> validators = new LinkedHashMap<>();
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
     * Holds all acceptable mods for mod validator.
     *
     * @see CompatRecipesModel#acceptableMods
     * @see CompatRecipesModel#acceptableModIds
     * @see CompatValuesModel#acceptableMods
     */
    public static final List<String> acceptableModIds = new ArrayList<>();

    public CompatRecipesModel(String mod, String machine, List<CompatValuesModel> values) {
        this.mod = mod;
        this.machine = machine;
        this.values = values;
    }

    public CompatRecipesModel() {
        this.mod = "";
        this.machine = "";
        this.values = List.of();
    }

    public String getMod() {
        return mod;
    }

    public String getMachine() {
        return machine;
    }

    public List<CompatValuesModel> getValues() {
        return values;
    }

    static {

        Validator machineValidator = new Validator("machine");
        acceptableMods.put("thermal", machineValidator.getRequiredAcceptsOnlyValidation(List.of("pulverizer", "induction_smelter"), false));
        acceptableMods.put("create", machineValidator.getRequiredAcceptsOnlyValidation(List.of("crushing_wheels", "fan_washing"), false));
        acceptableModIds.addAll(acceptableMods.keySet());

        validators.put("mod", new Validator("mod").getRequiredAcceptsOnlyValidation(acceptableModIds, false));

        Validator valuesValidator = new Validator("values");
        validators.put("values_rg", (element_rg, path_rg) -> {
            if (!(
                    valuesValidator.assertParentObject(element_rg, path_rg) &&
                            valuesValidator.NON_EMPTY_ARRAY_REQUIRED.apply(element_rg.getAsJsonObject().get("values"), path_rg)
            )) return false;

            JsonObject element = element_rg.getAsJsonObject();
            JsonArray array = element.get("values").getAsJsonArray();

            JsonObject temp = new JsonObject();
            try {
                temp.add("mod", new JsonPrimitive(Objects.requireNonNull(element.get("mod")).getAsString()));
            } catch (ClassCastException | NullPointerException e) {
                temp.add("mod", new JsonPrimitive("NONE"));
            }

            try {
                temp.add("machine", new JsonPrimitive(Objects.requireNonNull(element.get("machine")).getAsString()));
            } catch (ClassCastException | NullPointerException e) {
                temp.add("machine", new JsonPrimitive("NONE"));
            }

            return valuesValidator.passTempToValidators(temp, array, path_rg, CompatValuesModel.validators, false);
        });

        validators.put("machine_rg", (element, path) -> {
            if (!machineValidator.assertParentObject(element, path)) return false;
            if (!machineValidator.REQUIRED.apply(element.getAsJsonObject().get("machine"), path)) return false;

            JsonElement modJson = element.getAsJsonObject().get("mod");
            if (Objects.isNull(modJson)) {
                LOGGER.error("Mod is null! Can't verify values of machine for \"%s\"".formatted(Validator.obfuscatePath(path)));
                return false;
            }

            try {
                String mod = modJson.getAsString();
                var validator = acceptableMods.get(mod);
                if (Objects.isNull(validator)) {
                    LOGGER.error("Illegal value for mod present! Can't verify values of machine for \"%s\"".formatted(Validator.obfuscatePath(path)));
                    return false;
                }
                return validator.apply(element.getAsJsonObject().get("machine"), path);
            } catch (ClassCastException e) {
                LOGGER.error("Mod is not a string! Can't verify values of machine for \"%s\".".formatted(Validator.obfuscatePath(path)));
            }
            return false;
        });
    }
}
