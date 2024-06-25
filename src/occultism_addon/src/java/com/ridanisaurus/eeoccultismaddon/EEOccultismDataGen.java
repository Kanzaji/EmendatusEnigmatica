package com.ridanisaurus.eeoccultismaddon;

//import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
//import com.ridanisaurus.emendatusenigmatica.datagen.base.EERecipeProvider;
//import com.ridanisaurus.emendatusenigmatica.datagen.base.IFinishedGenericRecipe;
//import com.ridanisaurus.emendatusenigmatica.datagen.base.RecipeBuilder;
//import com.ridanisaurus.emendatusenigmatica.loader.parser.model.MaterialModel;
//import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
//import com.ridanisaurus.emendatusenigmatica.registries.EETags;
//import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class EEOccultismDataGen {
//	public static class Recipes extends EERecipeProvider {
//
//		private final EmendatusDataRegistry registry;
//
//		public Recipes(DataGenerator gen, EmendatusDataRegistry registry) {
//			super(gen);
//			this.registry = registry;
//		}
//
//		@Override
//		protected void buildRecipes(Consumer<IFinishedGenericRecipe> consumer) {
//			for (MaterialModel material : registry.getMaterials()) {
//				List<String> processedType = material.getProcessedTypes();
//				if (material.getCompat().getOccultismCompat()) {
//					if (processedType.contains("dust") && processedType.contains("ore")) {
//						// Dust from Ore - Crusher Spirit
//						new RecipeBuilder("result")
//								.forceOutputArray(false)
//								.type("occultism:crushing")
//								.group("emendatusenigmatica:compat_recipe")
//								.fieldJson("ingredient", new RecipeBuilder.JsonItemBuilder(false)
//										.tag(EETags.MATERIAL_ORE.apply(material.getId())))
//								.fieldInt("crushing_time", 200)
//								.addOutput(builder -> builder
//										.stackWithCount(EERegistrar.dustMap.get(material.getId()).get(), 2)
//								)
//								.save(consumer, new ResourceLocation(Reference.MOD_ID, "dust/from_ore_crusher_spirit/" + material.getId()));
//					}
//					if (processedType.contains("dust") && processedType.contains("ingot")) {
//						// Dust from Ingot - Crusher Spirit
//						new RecipeBuilder("result")
//								.forceOutputArray(false)
//								.type("occultism:crushing")
//								.group("emendatusenigmatica:compat_recipe")
//								.fieldJson("ingredient", new RecipeBuilder.JsonItemBuilder(false)
//										.tag(EETags.MATERIAL_INGOT.apply(material.getId())))
//								.fieldInt("crushing_time", 200)
//								.fieldBoolean("ignore_crushing_multiplier", true)
//								.addOutput(builder -> builder
//										.stack(EERegistrar.dustMap.get(material.getId()).get())
//								)
//								.save(consumer, new ResourceLocation(Reference.MOD_ID, "dust/from_ingot_crusher_spirit/" + material.getId()));
//					}
//					if (processedType.contains("dust") && processedType.contains("raw")) {
//						// Dust from Raw Material - Crusher Spirit
//						new RecipeBuilder("result")
//								.forceOutputArray(false)
//								.type("occultism:crushing")
//								.group("emendatusenigmatica:compat_recipe")
//								.fieldJson("ingredient", new RecipeBuilder.JsonItemBuilder(false)
//										.tag(EETags.MATERIAL_RAW.apply(material.getId())))
//								.fieldInt("crushing_time", 200)
//								.addOutput(builder -> builder
//										.stackWithCount(EERegistrar.dustMap.get(material.getId()).get(), 2)
//								)
//								.save(consumer, new ResourceLocation(Reference.MOD_ID, "dust/from_raw_crusher_spirit/" + material.getId()));
//					}
//				}
//			}
//		}
//
//		@Override
//		public String getName() {
//			return "EE Occultism Addon Recipes";
//		}
//	}
}