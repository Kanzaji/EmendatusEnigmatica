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
        for (MaterialModel material : materialModels) {
            List<String> types = material.getProcessedTypes();
            if (types.contains("storage_block")) EERegistrar.registerStorageBlocks(material);
            if (types.contains("ingot"))    EERegistrar.registerIngots(material);
            if (types.contains("nugget"))   EERegistrar.registerNuggets(material);
            if (types.contains("gem"))      EERegistrar.registerGems(material);
            if (types.contains("dust"))     EERegistrar.registerDusts(material);
            if (types.contains("plate"))    EERegistrar.registerPlates(material);
            if (types.contains("gear"))     EERegistrar.registerGears(material);
            if (types.contains("rod"))      EERegistrar.registerRods(material);
            if (types.contains("sword"))    EERegistrar.registerSwords(material);
            if (types.contains("pickaxe"))  EERegistrar.registerPickaxes(material);
            if (types.contains("axe"))      EERegistrar.registerAxes(material);
            if (types.contains("shovel")) EERegistrar.registerShovels(material);
            if (types.contains("hoe")) EERegistrar.registerHoes(material);
            if (types.contains("paxel")) EERegistrar.registerPaxels(material);
//            if (types.contains("armor")) EERegistrar.registerArmor(material); //TODO: Uncomment when armor processed type is impl.
            if (types.contains("shield")) EERegistrar.registerShields(material);
            if (types.contains("fluid")) EERegistrar.registerFluids(material);

            if (types.contains("raw")) {
                EERegistrar.registerRaw(material);
                EERegistrar.registerRawBlocks(material);
            }

            if (types.contains("cluster")) {
                EERegistrar.registerSmallBudBlocks(material);
                EERegistrar.registerMediumBudBlocks(material);
                EERegistrar.registerLargeBudBlocks(material);
                EERegistrar.registerClusterBlocks(material);
                EERegistrar.registerBuddingBlocks(material);
                EERegistrar.registerClusterShardBlocks(material);
                EERegistrar.registerClusterShards(material);
            }

            @Deprecated(forRemoval = true)
            boolean registerArmorMaterial = false;
            if (types.contains("helmet")) {
                EERegistrar.registerHelmets(material);
                registerArmorMaterial = true;
            }
            if (types.contains("chestplate")) {
                EERegistrar.registerChestplates(material);
                registerArmorMaterial = true;
            }
            if (types.contains("leggings")) {
                EERegistrar.registerLeggings(material);
                registerArmorMaterial = true;
            }
            if (types.contains("boots")) {
                EERegistrar.registerBoots(material);
                registerArmorMaterial = true;
            }
            if (registerArmorMaterial) EERegistrar.registerArmorMaterial(material);

            for (StrataModel strata : strataModels) {
                if (types.contains("ore")) {
                    if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) EERegistrar.registerOre(strata, material);

                    //TODO: Rework Sample System.
//                    if (types.contains("sample")) {
//                        if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
//                            EERegistrar.registerSample(strata, material);
//                        }
//                    }
                }
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