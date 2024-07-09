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

public class BlockHarvestToolTagsGen extends EETagProvider {
    private final EmendatusDataRegistry registry;

    public BlockHarvestToolTagsGen(DataGenerator gen, EmendatusDataRegistry registry) {
        super(gen);
        this.registry = registry;
    }

    private final List<String> shovel = Lists.newArrayList();
    private final List<String> hoe = Lists.newArrayList();
    private final List<String> axe = Lists.newArrayList();
    private final List<String> pickaxe = Lists.newArrayList();
    private final List<String> paxel = Lists.newArrayList();

    @Override
    protected void buildTags(Consumer<IFinishedGenericJSON> consumer) {
        for (MaterialModel material : registry.getMaterials()) {
            List<String> processedType = material.getProcessedTypes();

            if (processedType.contains("storage_block")) {
                pickaxe.add(EERegistrar.storageBlockMap.getIdAsString(material));
                if (material.getProperties().hasOxidization()) {
                    pickaxe.add(EERegistrar.exposedBlockMap.getIdAsString(material));
                    pickaxe.add(EERegistrar.weatheredBlockMap.getIdAsString(material));
                    pickaxe.add(EERegistrar.oxidizedBlockMap.getIdAsString(material));
                    pickaxe.add(EERegistrar.waxedStorageBlockMap.getIdAsString(material));
                    pickaxe.add(EERegistrar.waxedExposedBlockMap.getIdAsString(material));
                    pickaxe.add(EERegistrar.waxedWeatheredBlockMap.getIdAsString(material));
                    pickaxe.add(EERegistrar.waxedOxidizedBlockMap.getIdAsString(material));
                }
            }

            if (processedType.contains("raw"))
                pickaxe.add(EERegistrar.rawBlockMap.getIdAsString(material));

            if (processedType.contains("cluster")) {
                pickaxe.add(EERegistrar.buddingBlockMap.getIdAsString(material));
                pickaxe.add(EERegistrar.smallBudBlockMap.getIdAsString(material));
                pickaxe.add(EERegistrar.mediumBudBlockMap.getIdAsString(material));
                pickaxe.add(EERegistrar.largeBudBlockMap.getIdAsString(material));
                pickaxe.add(EERegistrar.clusterBlockMap.getIdAsString(material));
                pickaxe.add(EERegistrar.clusterShardBlockMap.getIdAsString(material));
            }

            for (StrataModel strata : registry.getStrata()) {
                if (processedType.contains("ore")) {
//						ResourceLocation ore = ResourceLocation.parse("minecraft:stone");
                    if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
                        ResourceLocation ore = EERegistrar.oreBlockTable.get(strata.getId(), material.getId()).getId();
                        switch (strata.getHarvestTool()) {
                            case "shovel" -> shovel.add(ore.toString());
                            case "hoe" -> hoe.add(ore.toString());
                            case "axe" -> axe.add(ore.toString());
                            case "pickaxe" -> pickaxe.add(ore.toString());
                            default ->
                                throw new IllegalStateException("Harvest tool " + strata.getHarvestTool() + " for " + strata.getId() + " is out of Vanilla tool system bounds, and the tag should be added manually");
                        }
                    }

                    // TODO: Rework Sample System
//                    if (processedType.contains("sample")) {
////							ResourceLocation sample = ResourceLocation.parse("minecraft:stone");
//                        if (material.getStrata().isEmpty() || material.getStrata().contains(strata.getId())) {
//                            ResourceLocation sample = EERegistrar.oreSampleBlockTable.get(strata.getId(), material.getId()).getId();
//                            switch (strata.getHarvestTool()) {
//                                case "shovel" -> shovel.add(sample.toString());
//                                case "hoe" -> hoe.add(sample.toString());
//                                case "axe" -> axe.add(sample.toString());
//                                case "pickaxe" -> pickaxe.add(sample.toString());
//                                default ->
//                                    throw new IllegalStateException("Harvest tool " + strata.getHarvestTool() + " for " + strata.getId() + " is out of Vanilla tool system bounds, and the tag should be added manually");
//                            }
//                        }
//                    }
                }
            }
        }

        if (!shovel.isEmpty())
            new TagBuilder().tags(shovel).save(consumer, ResourceLocation.fromNamespaceAndPath(MINECRAFT, "/block/mineable/shovel"));
        if (!hoe.isEmpty())
            new TagBuilder().tags(hoe).save(consumer, ResourceLocation.fromNamespaceAndPath(MINECRAFT, "/block/mineable/hoe"));
        if (!axe.isEmpty())
            new TagBuilder().tags(axe).save(consumer, ResourceLocation.fromNamespaceAndPath(MINECRAFT, "/block/mineable/axe"));
        if (!pickaxe.isEmpty())
            new TagBuilder().tags(pickaxe).save(consumer, ResourceLocation.fromNamespaceAndPath(MINECRAFT, "/block/mineable/pickaxe"));

        // Paxel is axe / pickaxe / shovel at once, so it's pretty much just three different tags.
        paxel.addAll(List.of(
            "#minecraft:mineable/axe",
            "#minecraft:mineable/pickaxe",
            "#minecraft:mineable/shovel"
        ));
        new TagBuilder().tags(paxel).save(consumer, ResourceLocation.fromNamespaceAndPath(COMMON, "/block/mineable/paxel"));
    }

    @Override
    public @NotNull String getName() {
        return "Emendatus Enigmatica Block Harvest Tool Tags";
    }
}
