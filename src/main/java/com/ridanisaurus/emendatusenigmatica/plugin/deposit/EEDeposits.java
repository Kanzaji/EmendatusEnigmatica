package com.ridanisaurus.emendatusenigmatica.plugin.deposit;

import com.ridanisaurus.emendatusenigmatica.loader.EELoader;
//import com.ridanisaurus.emendatusenigmatica.util.WorldGenHelper;
//import com.ridanisaurus.emendatusenigmatica.world.gen.feature.*;
//import com.ridanisaurus.emendatusenigmatica.world.gen.feature.config.DenseOreFeatureConfig;
//import com.ridanisaurus.emendatusenigmatica.world.gen.feature.config.DikeOreFeatureConfig;
//import com.ridanisaurus.emendatusenigmatica.world.gen.feature.config.GeodeOreFeatureConfig;
//import com.ridanisaurus.emendatusenigmatica.world.gen.feature.config.SphereOreFeatureConfig;
//import com.ridanisaurus.emendatusenigmatica.world.gen.feature.rule.MultiStrataRuleTest;
//import net.minecraft.world.level.levelgen.VerticalAnchor;
//import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//import net.minecraft.world.level.levelgen.feature.Feature;
//import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
//import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
//import net.minecraft.world.level.levelgen.placement.PlacedFeature;
//import net.neoforged.neoforge.eventbus.api.IEventBus;
//import net.neoforged.neoforge.registries.DeferredRegister;
//import net.neoforged.neoforge.registries.RegistryObject;


//FIXME: Do I need to add anything to why this has to be fixed :D?

/**
 * @deprecated Moved to {@link com.ridanisaurus.emendatusenigmatica.plugin.DefaultConfigPlugin}.
 */
@Deprecated(since = "1.21-2.1.7", forRemoval = true)
public class EEDeposits {

	//	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registry.FEATURE_REGISTRY, Reference.MOD_ID);
//	public static final DeferredRegister<ConfiguredFeature<?,?>> ORE_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Reference.MOD_ID);
//	public static final DeferredRegister<PlacedFeature> PLACED_ORE_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Reference.MOD_ID);
	private final EELoader loader;

	public EEDeposits(EELoader loader) {
		this.loader = loader;
	}

	//	public void setup() {
//		for (IDepositProcessor activeProcessor : ACTIVE_PROCESSORS) {
//			if(activeProcessor.getType().equals(DepositType.VANILLA.getType())) {
//				var model = ((VanillaDepositProcessor) activeProcessor).getVanillaModel();
//
//				RegistryObject<VanillaOreFeature> vanillaOreFeature = FEATURES.register(model.getName(), () -> new VanillaOreFeature(model, this.loader.getDataRegistry()));
//		        RegistryObject<ConfiguredFeature<?, ?>> oreFeature = ORE_FEATURES.register(model.getName(),
//				        () -> new ConfiguredFeature<>(vanillaOreFeature.get(), new NoneFeatureConfiguration())
//		        );
//
//				HeightRangePlacement placement = model.getPlacement().equals("uniform") ?
//						HeightRangePlacement.uniform(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel())) :
//						HeightRangePlacement.triangle(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel()));
//
//		        PLACED_ORE_FEATURES.register(
//		                model.getName(), () -> new PlacedFeature(oreFeature.getHolder().get(),
//						        model.getRarity().equals("common") ?
//								         WorldGenHelper.commonOrePlacement(model.getPlacementChance(), placement) :
//								         WorldGenHelper.rareOrePlacement(model.getPlacementChance(), placement)
//				        )
//		        );
//			}
//			if(activeProcessor.getType().equals(DepositType.SPHERE.getType())) {
//				var model = ((SphereDepositProcessor) activeProcessor).getSphereModel();
//
//				RegistryObject<SphereOreFeature> sphereOreFeature = FEATURES.register(model.getName(), () -> new SphereOreFeature(SphereOreFeatureConfig.CODEC, model, this.loader.getDataRegistry()));
//				RegistryObject<ConfiguredFeature<?, ?>> oreFeature = ORE_FEATURES.register(model.getName(),
//						() -> new ConfiguredFeature<>(sphereOreFeature.get(), new SphereOreFeatureConfig(new MultiStrataRuleTest(model.getFillerTypes())))
//				);
//
//				HeightRangePlacement placement = model.getPlacement().equals("uniform") ?
//						HeightRangePlacement.uniform(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel())) :
//						HeightRangePlacement.triangle(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel()));
//
//				PLACED_ORE_FEATURES.register(model.getName(),
//						() -> new PlacedFeature(oreFeature.getHolder().get(),
//								model.getRarity().equals("common") ?
//										WorldGenHelper.commonOrePlacement(model.getPlacementChance(), placement) :
//										WorldGenHelper.rareOrePlacement(model.getPlacementChance(), placement)
//						)
//				);
//			}
//			if(activeProcessor.getType().equals(DepositType.GEODE.getType())) {
//				var model = ((GeodeDepositProcessor) activeProcessor).getGeodeModel();
//
//				RegistryObject<GeodeOreFeature> geodeOreFeature = FEATURES.register(model.getName(), () -> new GeodeOreFeature(GeodeOreFeatureConfig.CODEC, model, this.loader.getDataRegistry()));
//				RegistryObject<ConfiguredFeature<?, ?>> oreFeature = ORE_FEATURES.register(model.getName(),
//						() -> new ConfiguredFeature<>(geodeOreFeature.get(), new GeodeOreFeatureConfig(new MultiStrataRuleTest(model.getFillerTypes())))
//				);
//
//				HeightRangePlacement placement = model.getPlacement().equals("uniform") ?
//						HeightRangePlacement.uniform(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel())) :
//						HeightRangePlacement.triangle(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel()));
//
//				PLACED_ORE_FEATURES.register(model.getName(),
//						() -> new PlacedFeature(oreFeature.getHolder().get(),
//								model.getRarity().equals("common") ?
//										WorldGenHelper.commonOrePlacement(model.getPlacementChance(), placement) :
//										WorldGenHelper.rareOrePlacement(model.getPlacementChance(), placement)
//						)
//				);
//			}
//			if(activeProcessor.getType().equals(DepositType.DIKE.getType())) {
//				var model = ((DikeDepositProcessor) activeProcessor).getDikeModel();
//
//				RegistryObject<DikeOreFeature> dikeOreFeature = FEATURES.register(model.getName(), () -> new DikeOreFeature(DikeOreFeatureConfig.CODEC, model, this.loader.getDataRegistry()));
//				RegistryObject<ConfiguredFeature<?, ?>> oreFeature = ORE_FEATURES.register(model.getName(),
//						() -> new ConfiguredFeature<>(dikeOreFeature.get(), new DikeOreFeatureConfig(new MultiStrataRuleTest(model.getFillerTypes())))
//				);
//
//				HeightRangePlacement placement = model.getPlacement().equals("uniform") ?
//						HeightRangePlacement.uniform(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel())) :
//						HeightRangePlacement.triangle(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel()));
//
//				PLACED_ORE_FEATURES.register(model.getName(),
//						() -> new PlacedFeature(oreFeature.getHolder().get(),
//								model.getRarity().equals("common") ?
//										WorldGenHelper.commonOrePlacement(model.getPlacementChance(), placement) :
//										WorldGenHelper.rareOrePlacement(model.getPlacementChance(), placement)
//						)
//				);
//			}
//			if(activeProcessor.getType().equals(DepositType.DENSE.getType())) {
//				var model = ((DenseDepositProcessor) activeProcessor).getDenseModel();
//
//				RegistryObject<DenseOreFeature> denseOreFeature = FEATURES.register(model.getName(), () -> new DenseOreFeature(DenseOreFeatureConfig.CODEC, model, this.loader.getDataRegistry()));
//				RegistryObject<ConfiguredFeature<?, ?>> oreFeature = ORE_FEATURES.register(model.getName(),
//						() -> new ConfiguredFeature<>(denseOreFeature.get(), new DenseOreFeatureConfig(new MultiStrataRuleTest(model.getFillerTypes())))
//				);
//
//				HeightRangePlacement placement = model.getPlacement().equals("uniform") ?
//						HeightRangePlacement.uniform(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel())) :
//						HeightRangePlacement.triangle(VerticalAnchor.absolute(model.getMinYLevel()), VerticalAnchor.absolute(model.getMaxYLevel()));
//
//				PLACED_ORE_FEATURES.register(model.getName(),
//						() -> new PlacedFeature(oreFeature.getHolder().get(),
//								model.getRarity().equals("common") ?
//										WorldGenHelper.commonOrePlacement(model.getPlacementChance(), placement) :
//										WorldGenHelper.rareOrePlacement(model.getPlacementChance(), placement)
//						)
//				);
//			}
//		}
//	}
//
//
//	public void finalize(IEventBus eventBus) {
//		FEATURES.register(eventBus);
//		ORE_FEATURES.register(eventBus);
//		PLACED_ORE_FEATURES.register(eventBus);
//	}
}
