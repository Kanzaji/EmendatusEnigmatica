package com.ridanisaurus.emendatusenigmatica.datagen.gen.block.tags;

import com.google.common.collect.Lists;
import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import com.ridanisaurus.emendatusenigmatica.datagen.builder.TagBuilder;
import com.ridanisaurus.emendatusenigmatica.datagen.provider.EETagProvider;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static com.ridanisaurus.emendatusenigmatica.util.Reference.COMMON;

public class BlockHarvestLevelTagsGen extends EETagProvider {
    private static final ResourceLocation STONE = ResourceLocation.parse("minecraft:stone");
    private final EmendatusDataRegistry registry;

    public BlockHarvestLevelTagsGen(DataGenerator gen, EmendatusDataRegistry registry) {
        super(gen);
        this.registry = registry;
    }

    private final List<String> woodTool = Lists.newArrayList();
    private final List<String> stoneTool = Lists.newArrayList();
    private final List<String> ironTool = Lists.newArrayList();
    private final List<String> diamondTool = Lists.newArrayList();
    private final List<String> netheriteTool = Lists.newArrayList();

    @Override
    protected void buildTags(Consumer<IFinishedGenericJSON> consumer) {
        for (MaterialModel material : registry.getMaterials()) {
            List<String> processedType = material.getProcessedTypes();

            if (processedType.contains("storage_block")) {
                harvestLevelSwitch(material, EERegistrar.storageBlockMap.get(material.getId()).getId());
                if (material.getProperties().hasOxidization()) {
                    harvestLevelSwitch(material, EERegistrar.exposedBlockMap.get(material.getId()).getId());
                    harvestLevelSwitch(material, EERegistrar.weatheredBlockMap.get(material.getId()).getId());
                    harvestLevelSwitch(material, EERegistrar.oxidizedBlockMap.get(material.getId()).getId());
                    harvestLevelSwitch(material, EERegistrar.waxedStorageBlockMap.get(material.getId()).getId());
                    harvestLevelSwitch(material, EERegistrar.waxedExposedBlockMap.get(material.getId()).getId());
                    harvestLevelSwitch(material, EERegistrar.waxedWeatheredBlockMap.get(material.getId()).getId());
                    harvestLevelSwitch(material, EERegistrar.waxedOxidizedBlockMap.get(material.getId()).getId());
                }
            }

            if (processedType.contains("raw"))
                harvestLevelSwitch(material, EERegistrar.rawBlockMap.get(material.getId()).getId());

            if (processedType.contains("cluster")) {
                harvestLevelSwitch(material, EERegistrar.buddingBlockMap.get(material.getId()).getId());
                harvestLevelSwitch(material, EERegistrar.smallBudBlockMap.get(material.getId()).getId());
                harvestLevelSwitch(material, EERegistrar.mediumBudBlockMap.get(material.getId()).getId());
                harvestLevelSwitch(material, EERegistrar.largeBudBlockMap.get(material.getId()).getId());
                harvestLevelSwitch(material, EERegistrar.clusterBlockMap.get(material.getId()).getId());
                harvestLevelSwitch(material, EERegistrar.clusterShardBlockMap.get(material.getId()).getId());
            }

            for (StrataModel strata : registry.getStrata()) {
                if (processedType.contains("ore")) {

                    ResourceLocation ore = STONE;
                    if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId()))
                        ore = EERegistrar.oreBlockTable.get(strata.getId(), material.getId()).getId();
                    harvestLevelSwitch(material, ore);

                    // TODO: Processed Type "sample" doesn't tho?
                    if (processedType.contains("sample")) {
                        ResourceLocation sample = STONE;
                        if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId()))
                            sample = EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).getId();
                        harvestLevelSwitch(material, sample);
                    }
                }
            }
        }

        if (!woodTool.isEmpty())
            new TagBuilder().tags(woodTool).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/needs_wood_tool"));
        if (!stoneTool.isEmpty())
            new TagBuilder().tags(stoneTool).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/needs_stone_tool"));
        if (!ironTool.isEmpty())
            new TagBuilder().tags(ironTool).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/needs_iron_tool"));
        if (!diamondTool.isEmpty())
            new TagBuilder().tags(diamondTool).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/needs_diamond_tool"));
        if (!netheriteTool.isEmpty())
            new TagBuilder().tags(netheriteTool).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/needs_netherite_tool"));
    }

    private void harvestLevelSwitch(@NotNull MaterialModel material, ResourceLocation loc) {
        switch (material.getProperties().getHarvestLevel()) {
            case 0 -> woodTool.add(loc.toString());
            case 1 -> stoneTool.add(loc.toString());
            case 2 -> ironTool.add(loc.toString());
            case 3 -> diamondTool.add(loc.toString());
            case 4 -> netheriteTool.add(loc.toString());
            default ->
                throw new IllegalStateException("Harvest level " + material.getProperties().getHarvestLevel() + " for " + material.getId() + " is out of Vanilla tier system bounds, and the tag should be added manually");
        }
    }

    @Override
    public @NotNull String getName() {
        return "Emendatus Enigmatica Block Harvest Level Tags";
    }
}
