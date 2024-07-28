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

package com.ridanisaurus.emendatusenigmatica.plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.DepositType;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.DepositValidators;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.IDepositProcessor;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.processors.*;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.compat.CompatModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.util.FileHelper;
import com.ridanisaurus.emendatusenigmatica.util.validation.Validator;
import com.ridanisaurus.emendatusenigmatica.util.validation.ValidatorLogger;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class DefaultLoader {
    public static final List<String> MATERIAL_IDS = new ArrayList<>();
    public static final List<String> STRATA_IDS = new ArrayList<>();
    public static final Map<String, Function<JsonObject, IDepositProcessor>> DEPOSIT_PROCESSORS = new HashMap<>();
    public static final List<String> DEPOSIT_TYPES = new ArrayList<>();
    public static final List<IDepositProcessor> ACTIVE_PROCESSORS = new ArrayList<>();
    public static final List<String> DEPOSIT_IDS = new ArrayList<>();

    protected static void load(EmendatusDataRegistry registry) {
        // Set the path to the defined folder
        Path configDir = FMLPaths.CONFIGDIR.get().resolve("emendatusenigmatica/");

        // Check if the folder exists
        if (!configDir.toFile().exists() && configDir.toFile().mkdirs()) EmendatusEnigmatica.logger.info("Created /config/emendatusenigmatica/");

        File strataDir = configDir.resolve("strata/").toFile();
        if (!strataDir.exists() && strataDir.mkdirs()) EmendatusEnigmatica.logger.info("Created /config/emendatusenigmatica/strata/");

        File materialDir = configDir.resolve("material/").toFile();
        if (!materialDir.exists() && materialDir.mkdirs()) EmendatusEnigmatica.logger.info("Created /config/emendatusenigmatica/material/");

        File compatDir = configDir.resolve("compat/").toFile();
        if (!compatDir.exists() && compatDir.mkdirs()) EmendatusEnigmatica.logger.info("Created /config/emendatusenigmatica/compat/");

        File depositDir = configDir.resolve("deposit/").toFile();
        if (!depositDir.exists() && depositDir.mkdirs()) EmendatusEnigmatica.logger.info("Created /config/emendatusenigmatica/deposit/");

        Validator validator = new Validator("Main Validator");
        ValidatorLogger LOGGER = Validator.LOGGER;

        registerStrata(validator, LOGGER, strataDir, registry);
        registerMaterials(validator, LOGGER, materialDir, registry);
        registerCompat(validator, LOGGER, compatDir, registry);
        registerDeposits(validator, LOGGER, depositDir, registry);

        LOGGER.restartSpacer();
        LOGGER.info("Finished validation and registration of data files!");
        LOGGER.printSpacer(0);
    }

    private static void registerStrata(@NotNull Validator validator, @NotNull ValidatorLogger LOGGER, @NotNull File strataDir, @NotNull EmendatusDataRegistry registry) {
        Map<Path, JsonObject> strataDefinition = FileHelper.loadJsonsWithPaths(strataDir.toPath());

        LOGGER.restartSpacer();
        LOGGER.info("Validating and registering data for: Strata");
        strataDefinition.forEach((path, jsonObject) -> {
            LOGGER.restartSpacer();
            if (!validator.validateObject(jsonObject, path, StrataModel.validators)) {
                if (!LOGGER.shouldLog) return;
                LOGGER.printSpacer(2);
                LOGGER.error("File \"%s\" is not going to be registered due to errors in it's validation.".formatted(path));
                return;
            }

            Optional<Pair<StrataModel, JsonElement>> result = JsonOps.INSTANCE.withDecoder(StrataModel.CODEC).apply(jsonObject).result();
            if (result.isEmpty()) return;

            StrataModel strataModel = result.get().getFirst();
            registry.registerStrata(strataModel);
//            STRATA.add(strataModel);
            STRATA_IDS.add(strataModel.getId());
        });
    }

    private static void registerMaterials(@NotNull Validator validator, @NotNull ValidatorLogger LOGGER, @NotNull File materialDir, @NotNull EmendatusDataRegistry registry) {
        Map<Path, JsonObject> materialDefinition = FileHelper.loadJsonsWithPaths(materialDir.toPath());

        LOGGER.restartSpacer();
        LOGGER.info("Validating and registering data for: Material");
        materialDefinition.forEach((path, jsonObject) -> {
            LOGGER.restartSpacer();
            if (!validator.validateObject(jsonObject, path, MaterialModel.validators)) {
                if (!LOGGER.shouldLog) return;
                LOGGER.printSpacer(2);
                LOGGER.error("File \"%s\" is not going to be registered due to errors in it's validation.".formatted(path));
                return;
            }

            Optional<Pair<MaterialModel, JsonElement>> result = JsonOps.INSTANCE.withDecoder(MaterialModel.CODEC).apply(jsonObject).result();
            if (result.isEmpty()) return;

            MaterialModel materialModel = result.get().getFirst();
            registry.getMaterialOrRegister(materialModel.getId(), materialModel);
//            MATERIALS.add(materialModel);
            MATERIAL_IDS.add(materialModel.getId());
        });
    }

    private static void registerCompat(@NotNull Validator validator, @NotNull ValidatorLogger LOGGER, @NotNull File compatDir, @NotNull EmendatusDataRegistry registry) {
        Map<Path, JsonObject> compatDefinition = FileHelper.loadJsonsWithPaths(compatDir.toPath());

        LOGGER.restartSpacer();
        LOGGER.info("Validating and registering data for: Compatibility");
        compatDefinition.forEach((path, jsonObject) -> {
            LOGGER.restartSpacer();
            if (!validator.validateObject(jsonObject, path, CompatModel.validators)) {
                if (LOGGER.shouldLog) {
                    LOGGER.printSpacer(2);
                    LOGGER.error("File \"%s\" is not going to be registered due to errors in it's validation.".formatted(path));
                }
                return;
            }

            Optional<Pair<CompatModel, JsonElement>> result = JsonOps.INSTANCE.withDecoder(CompatModel.CODEC).apply(jsonObject).result();
            if (result.isEmpty()) return;

            CompatModel compatModel = result.get().getFirst();
            registry.registerCompat(compatModel);
        });
    }

    private static void registerDeposits(@NotNull Validator validator, @NotNull ValidatorLogger LOGGER, @NotNull File depositDir, @NotNull EmendatusDataRegistry registry) {
        Map<Path, JsonObject> depositJsonDefinitionsMap = FileHelper.loadJsonsWithPaths(depositDir.toPath());

        if (DEPOSIT_PROCESSORS.isEmpty()) initProcessors();
        if (DEPOSIT_TYPES.size() != DEPOSIT_PROCESSORS.size()) {
            DEPOSIT_TYPES.clear();
            DEPOSIT_TYPES.addAll(DEPOSIT_PROCESSORS.keySet());
        }

        LOGGER.restartSpacer();
        LOGGER.info("Validating and registering data for: Deposits");
        depositJsonDefinitionsMap.forEach((path, element) -> {
            LOGGER.restartSpacer();
            if (!validator.validateObject(element, path, DepositValidators.get(element.get("type")))) {
                LOGGER.printSpacer(2);
                LOGGER.error("File \"%s\" is not going to be registered due to errors in it's validation.".formatted(path));
                return;
            }

            ACTIVE_PROCESSORS.add(DEPOSIT_PROCESSORS.get(element.get("type").getAsString()).apply(element));
            DEPOSIT_IDS.add(element.get("registryName").getAsString());
        });

        ACTIVE_PROCESSORS.forEach(IDepositProcessor::load);
    }

    private static void initProcessors() {
        DEPOSIT_PROCESSORS.put(DepositType.VANILLA.getType(), VanillaDepositProcessor::new);
        DEPOSIT_PROCESSORS.put(DepositType.SPHERE.getType(), SphereDepositProcessor::new);
        DEPOSIT_PROCESSORS.put(DepositType.GEODE.getType(), GeodeDepositProcessor::new);
        DEPOSIT_PROCESSORS.put(DepositType.DIKE.getType(), DikeDepositProcessor::new);
        DEPOSIT_PROCESSORS.put(DepositType.DENSE.getType(), DenseDepositProcessor::new);
    }
}
