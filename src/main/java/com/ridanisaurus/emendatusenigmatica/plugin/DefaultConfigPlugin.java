package com.ridanisaurus.emendatusenigmatica.plugin;

import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.api.IEmendatusPlugin;
import com.ridanisaurus.emendatusenigmatica.api.annotation.EmendatusPluginReference;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.block.*;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.block.tags.BlockHarvestLevelTagsGen;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.block.tags.BlockHarvestToolTagsGen;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.block.tags.BlockTagsGen;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.fluid.FluidModelsGen;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.fluid.FluidTagsGen;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.item.ItemModelsGen;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.item.ItemTagsGen;
import com.ridanisaurus.emendatusenigmatica.datagen.provider.EELootProvider;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import com.ridanisaurus.emendatusenigmatica.datagen.gen.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

//This plugin will be always first
@EmendatusPluginReference(modid = Reference.MOD_ID, name = "config")
public class DefaultConfigPlugin implements IEmendatusPlugin {
    //    Not used anywhere, commented to reduce memory usage a bit.
//    public static final List<MaterialModel> MATERIALS = new ArrayList<>();
//    public static final List<StrataModel> STRATA = new ArrayList<>();

    /**
     * Used to trigger loading of config directory.
     * @param registry The registry used to register the materials, strata and compat
     */
    @Override
    public void load(EmendatusDataRegistry registry) {
        DefaultLoader.load(registry);
    }

    @Override
    public void registerMinecraft(List<MaterialModel> materialModels, List<StrataModel> strataModels) {
    //FIXME: Rework this method when Data generation and EERegistrar is ported.
        for (StrataModel strata : strataModels) {
            for (MaterialModel material : materialModels) {
                if (material.getProcessedTypes().contains("ore")) {
                    if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
                        EERegistrar.registerOre(strata, material);
                    }
                    if (material.getProcessedTypes().contains("sample")) {
                        if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
                            EERegistrar.registerSample(strata, material);
                        }
                    }
                }
            }
        }

        for (MaterialModel material : materialModels) {
            if (material.getProcessedTypes().contains("storage_block")) {
                EERegistrar.registerStorageBlocks(material);
            }
            if (material.getProcessedTypes().contains("raw")) {
                EERegistrar.registerRaws(material);
                EERegistrar.registerRawBlocks(material);
            }
            if (material.getProcessedTypes().contains("cluster")) {
                EERegistrar.registerSmallBudBlocks(material);
                EERegistrar.registerMediumBudBlocks(material);
                EERegistrar.registerLargeBudBlocks(material);
                EERegistrar.registerClusterBlocks(material);
                EERegistrar.registerBuddingBlocks(material);
                EERegistrar.registerClusterShardBlocks(material);
                EERegistrar.registerClusterShards(material);
            }
            if (material.getProcessedTypes().contains("ingot")) {
                EERegistrar.registerIngots(material);
            }
            if (material.getProcessedTypes().contains("nugget")) {
                EERegistrar.registerNuggets(material);
            }
            if (material.getProcessedTypes().contains("gem")) {
                EERegistrar.registerGems(material);
            }
            if (material.getProcessedTypes().contains("dust")) {
                EERegistrar.registerDusts(material);
            }
            if (material.getProcessedTypes().contains("plate")) {
                EERegistrar.registerPlates(material);
            }
            if (material.getProcessedTypes().contains("gear")) {
                EERegistrar.registerGears(material);
            }
            if (material.getProcessedTypes().contains("rod")) {
                EERegistrar.registerRods(material);
            }
            if (material.getProcessedTypes().contains("sword")) {
                EERegistrar.registerSwords(material);
            }
            if (material.getProcessedTypes().contains("pickaxe")) {
                EERegistrar.registerPickaxes(material);
            }
            if (material.getProcessedTypes().contains("axe")) {
                EERegistrar.registerAxes(material);
            }
            if (material.getProcessedTypes().contains("shovel")) {
                EERegistrar.registerShovels(material);
            }
            if (material.getProcessedTypes().contains("hoe")) {
                EERegistrar.registerHoes(material);
            }
            if (material.getProcessedTypes().contains("paxel")) {
                EERegistrar.registerPaxels(material);
            }
            boolean registerArmorMaterial = false;
            if (material.getProcessedTypes().contains("helmet")) {
                EERegistrar.registerHelmets(material);
                registerArmorMaterial = true;
            }
            if (material.getProcessedTypes().contains("chestplate")) {
                EERegistrar.registerChestplates(material);
                registerArmorMaterial = true;
            }
            if (material.getProcessedTypes().contains("leggings")) {
                EERegistrar.registerLeggings(material);
                registerArmorMaterial = true;
            }
            if (material.getProcessedTypes().contains("boots")) {
                EERegistrar.registerBoots(material);
                registerArmorMaterial = true;
            }
            if (registerArmorMaterial) {
                EERegistrar.registerArmorMaterial(material);
            }
            if (material.getProcessedTypes().contains("shield")) {
                EERegistrar.registerShields(material);
            }
            if (material.getProcessedTypes().contains("fluid")) {
                EERegistrar.registerFluids(material);
            }
        }
    }

    @Override
    public void registerDynamicDataGen(DataGenerator generator, EmendatusDataRegistry registry, CompletableFuture<HolderLookup.Provider> providers) {
        generator.addProvider(true, new BlockStatesGen(generator, registry));
        generator.addProvider(true, new BlockModelsGen(generator, registry));
        generator.addProvider(true, new BlockTagsGen(generator, registry));
        generator.addProvider(true, new BlockHarvestLevelTagsGen(generator, registry));
        generator.addProvider(true, new BlockHarvestToolTagsGen(generator, registry));
        generator.addProvider(true, new ItemModelsGen(generator, registry));
        generator.addProvider(true, new ItemTagsGen(generator, registry));
        generator.addProvider(true, new FluidModelsGen(generator, registry));
        generator.addProvider(true, new FluidTagsGen(generator, registry));
        generator.addProvider(true, new LangGen(generator, registry));
        generator.addProvider(true, new RecipesGen(generator, registry, providers));
        generator.addProvider(true, new EELootProvider(generator, registry, providers));
//        generator.addProvider(true, new OreFeatureDataGen(generator, registry));
    }

    @Override
    public void finish(EmendatusDataRegistry registry) {

    }
}