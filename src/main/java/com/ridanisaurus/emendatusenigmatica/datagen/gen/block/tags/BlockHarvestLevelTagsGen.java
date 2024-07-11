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
import static com.ridanisaurus.emendatusenigmatica.util.Reference.MINECRAFT;

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
                harvestLevelSwitch(material, EERegistrar.storageBlockMap.getId(material));
                if (material.getProperties().hasOxidization()) {
                    harvestLevelSwitch(material, EERegistrar.exposedBlockMap.getId(material));
                    harvestLevelSwitch(material, EERegistrar.weatheredBlockMap.getId(material));
                    harvestLevelSwitch(material, EERegistrar.oxidizedBlockMap.getId(material));
                    harvestLevelSwitch(material, EERegistrar.waxedStorageBlockMap.getId(material));
                    harvestLevelSwitch(material, EERegistrar.waxedExposedBlockMap.getId(material));
                    harvestLevelSwitch(material, EERegistrar.waxedWeatheredBlockMap.getId(material));
                    harvestLevelSwitch(material, EERegistrar.waxedOxidizedBlockMap.getId(material));
                }
            }

            if (processedType.contains("raw"))
                harvestLevelSwitch(material, EERegistrar.rawBlockMap.getId(material));

            if (processedType.contains("cluster")) {
                harvestLevelSwitch(material, EERegistrar.buddingBlockMap.getId(material));
                harvestLevelSwitch(material, EERegistrar.smallBudBlockMap.getId(material));
                harvestLevelSwitch(material, EERegistrar.mediumBudBlockMap.getId(material));
                harvestLevelSwitch(material, EERegistrar.largeBudBlockMap.getId(material));
                harvestLevelSwitch(material, EERegistrar.clusterBlockMap.getId(material));
                harvestLevelSwitch(material, EERegistrar.clusterShardBlockMap.getId(material));
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

        if (!woodTool.isEmpty())      new TagBuilder(woodTool)     .save(consumer, MINECRAFT, "/block/needs_wood_tool");
        if (!stoneTool.isEmpty())     new TagBuilder(stoneTool)    .save(consumer, MINECRAFT, "/block/needs_stone_tool");
        if (!ironTool.isEmpty())      new TagBuilder(ironTool)     .save(consumer, MINECRAFT, "/block/needs_iron_tool");
        if (!diamondTool.isEmpty())   new TagBuilder(diamondTool)  .save(consumer, MINECRAFT, "/block/needs_diamond_tool");
        if (!netheriteTool.isEmpty()) new TagBuilder(netheriteTool).save(consumer, MINECRAFT, "/block/needs_netherite_tool");
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
