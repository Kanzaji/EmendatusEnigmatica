package com.ridanisaurus.emendatusenigmatica.datagen.gen.world;

import com.mojang.datafixers.util.Pair;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.DepositType;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.IDepositProcessor;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.processors.*;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import com.ridanisaurus.emendatusenigmatica.util.WorldGenHelper;
import com.ridanisaurus.emendatusenigmatica.world.gen.feature.config.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.ridanisaurus.emendatusenigmatica.plugin.DefaultLoader.ACTIVE_PROCESSORS;

public class OreFeatureGen implements DataProvider {
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public OreFeatureGen(@NotNull DataGenerator generator, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = generator.getPackOutput();
        this.registries = registries;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        var builder = new RegistrySetBuilder();
        List<Pair<ResourceKey<PlacedFeature>, PlacedFeature>> PLACED_FEATURES = new ArrayList<>();

        //TODO:
        // Add rest of the generation types
        // Rework The type to be enum, or something possible to use in a switch statement. (Maybe Enum to string would work?)
        // Test this a bit more.
        builder.add(Registries.CONFIGURED_FEATURE, bt -> {
            for (IDepositProcessor activeProcessor : ACTIVE_PROCESSORS) { switch (DepositType.typeOf(activeProcessor.getType())) {
                case VANILLA -> {
                    var model = ((VanillaDepositProcessor) activeProcessor).getVanillaModel();

                    var configuredFeature = bt.register(ResourceKey.create(
                            Registries.CONFIGURED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new ConfiguredFeature<>(EERegistrar.VANILLA_ORE_FEATURE.get(), new VanillaOreFeatureConfig(model))
                    );

                    PLACED_FEATURES.add(new Pair<>(
                        ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new PlacedFeature(
                            configuredFeature,
                            WorldGenHelper.getOrePlacement(model.getRarity(), model.getPlacementChance(),
                                WorldGenHelper.getPlacementModifier(model.getPlacement(), model.getMinYLevel(), model.getMaxYLevel())
                            ))
                    ));
                }
                case SPHERE -> {
                    var model = ((SphereDepositProcessor) activeProcessor).getSphereModel();

                    var configuredFeature = bt.register(ResourceKey.create(
                            Registries.CONFIGURED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new ConfiguredFeature<>(EERegistrar.SPHERE_ORE_FEATURE.get(), new VanillaOreFeatureConfig(model))
                    );

                    PLACED_FEATURES.add(new Pair<>(
                        ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new PlacedFeature(
                            configuredFeature,
                            WorldGenHelper.getOrePlacement(model.getRarity(), model.getPlacementChance(),
                                WorldGenHelper.getPlacementModifier(model.getPlacement(), model.getMinYLevel(), model.getMaxYLevel())
                            ))
                    ));
                }
                case GEODE -> {
                    var model = ((GeodeDepositProcessor) activeProcessor).getGeodeModel();

                    var configuredFeature = bt.register(ResourceKey.create(
                            Registries.CONFIGURED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new ConfiguredFeature<>(EERegistrar.GEODE_ORE_FEATURE.get(), new VanillaOreFeatureConfig(model))
                    );

                    PLACED_FEATURES.add(new Pair<>(
                        ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new PlacedFeature(
                            configuredFeature,
                            WorldGenHelper.getOrePlacement(model.getRarity(), model.getPlacementChance(),
                                WorldGenHelper.getPlacementModifier(model.getPlacement(), model.getMinYLevel(), model.getMaxYLevel())
                            ))
                    ));
                }
                case DIKE -> {
                    var model = ((DikeDepositProcessor) activeProcessor).getDikeModel();

                    var configuredFeature = bt.register(ResourceKey.create(
                            Registries.CONFIGURED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new ConfiguredFeature<>(EERegistrar.DIKE_ORE_FEATURE.get(), new VanillaOreFeatureConfig(model))
                    );

                    PLACED_FEATURES.add(new Pair<>(
                        ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new PlacedFeature(
                            configuredFeature,
                            WorldGenHelper.getOrePlacement(model.getRarity(), model.getPlacementChance(),
                                WorldGenHelper.getPlacementModifier(model.getPlacement(), model.getMinYLevel(), model.getMaxYLevel())
                            ))
                    ));
                }
                case DENSE -> {
                    var model = ((DenseDepositProcessor) activeProcessor).getDenseModel();

                    var configuredFeature = bt.register(ResourceKey.create(
                            Registries.CONFIGURED_FEATURE,
                            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new ConfiguredFeature<>(EERegistrar.DENSE_ORE_FEATURE.get(), new VanillaOreFeatureConfig(model))
                    );

                    PLACED_FEATURES.add(new Pair<>(
                        ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, model.getName())),
                        new PlacedFeature(
                            configuredFeature,
                            WorldGenHelper.getOrePlacement(model.getRarity(), model.getPlacementChance(),
                                WorldGenHelper.getPlacementModifier(model.getPlacement(), model.getMinYLevel(), model.getMaxYLevel())
                            ))
                    ));
                }
                // Test does nothing - null means addon deposit type!
                // Validation system wouldn't allow registration of an unknown type
                case TEST, null -> {}
            }}
        });

        builder.add(Registries.PLACED_FEATURE, bt -> PLACED_FEATURES.forEach(pair -> bt.register(pair.getFirst(), pair.getSecond())));

        return new DatapackBuiltinEntriesProvider(this.output, this.registries, builder, Set.of(Reference.MOD_ID, Reference.MINECRAFT)).run(output);
    }

    @Override
    public @NotNull String getName() {
        return "Emendatus Enigmatica: World Gen Features";
    }
}
