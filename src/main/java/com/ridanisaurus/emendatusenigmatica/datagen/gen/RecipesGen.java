/*
 * MIT License
 *
 * Copyright (c) 2020 Ridanisaurus
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
//TODO: Also Changed functionality. Possible full re-write.
package com.ridanisaurus.emendatusenigmatica.datagen.gen;

import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.registries.EETags;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RecipesGen extends RecipeProvider {

	private final EmendatusDataRegistry registry;

	public RecipesGen(@NotNull DataGenerator gen, EmendatusDataRegistry registry, CompletableFuture<HolderLookup.Provider> providers) {
		super(gen.getPackOutput(), providers);
		this.registry = registry;
	}

	@Override
	public @NotNull String getName() {
		return "Emendatus Enigmatica Recipes";
	}

	@Override
	protected void buildRecipes(@NotNull RecipeOutput out) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.ENIGMATIC_HAMMER::get)
				.pattern(" IN")
				.pattern(" SI")
				.pattern("S  ")
				.define('I', EETags.MATERIAL_INGOT.apply("iron"))
				.define('N', EETags.MATERIAL_NUGGET.apply("iron"))
				.define('S', EETags.MATERIAL_ROD.apply("wooden"))
				.group(Reference.MOD_ID)
				.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
				.save(out);

		for(MaterialModel material : registry.getMaterials()) {
			List<String> processedType = material.getProcessedTypes();
			if (material.isModded()) {
				if (processedType.contains("ingot")) {
					if (processedType.contains("storage_block")) {
						// Ingot from Block
						ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.ingotMap.get(material), 9)
								.requires(EETags.MATERIAL_STORAGE_BLOCK.apply(material.getId()))
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_block/" + material.getId()));

						// Block from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EERegistrar.storageBlockItemMap.get(material))
								.define('#', EETags.MATERIAL_INGOT.apply(material.getId()))
								.pattern("###")
								.pattern("###")
								.pattern("###")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/from_ingot/" + material.getId()));

						// Waxed
						if (material.getProperties().hasOxidization()) {
							ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, EERegistrar.waxedStorageBlockItemMap.get(material))
									.requires(EERegistrar.storageBlockItemMap.get(material))
									.requires(Items.HONEYCOMB)
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed/from_block/" + material.getId() + "_block"));

							ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, EERegistrar.waxedExposedBlockItemMap.get(material))
									.requires(EERegistrar.exposedBlockItemMap.get(material))
									.requires(Items.HONEYCOMB)
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed/from_block/exposed_" + material.getId()));

							ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, EERegistrar.waxedWeatheredBlockItemMap.get(material))
									.requires(EERegistrar.weatheredBlockItemMap.get(material))
									.requires(Items.HONEYCOMB)
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed/from_block/weathered_" + material.getId()));

							ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, EERegistrar.waxedOxidizedBlockItemMap.get(material))
									.requires(EERegistrar.oxidizedBlockItemMap.get(material))
									.requires(Items.HONEYCOMB)
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed/from_block/oxidized_" + material.getId()));
						}
					}

					if (processedType.contains("nugget")) {
						// Ingot from Nugget
						ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.ingotMap.get(material))
								.define('#', EETags.MATERIAL_NUGGET.apply(material.getId()))
								.pattern("###")
								.pattern("###")
								.pattern("###")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_nugget/" + material.getId()));

						// Nugget from Ingot
						ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.nuggetMap.get(material), 9)
								.requires(EETags.MATERIAL_INGOT.apply(material.getId()))
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "nugget/from_ingot/" + material.getId()));
					}

					if (processedType.contains("dust")) {
						// Ingot from Dust
						SimpleCookingRecipeBuilder.smelting(Ingredient.of(EETags.MATERIAL_DUST.apply(material.getId())),
										RecipeCategory.MISC,
										EERegistrar.ingotMap.get(material), 0.5F, 200)
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_dust/smelting/" + material.getId()));

						SimpleCookingRecipeBuilder.blasting(Ingredient.of(EETags.MATERIAL_DUST.apply(material.getId())),
										RecipeCategory.MISC,
										EERegistrar.ingotMap.get(material), 0.5F, 100)
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_dust/blasting/" + material.getId()));
					}

					if (processedType.contains("plate")) {
						// Plate from Ingot
						ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.plateMap.get(material), 1)
								.requires(EETags.MATERIAL_INGOT.apply(material.getId()))
								.requires(EERegistrar.ENIGMATIC_HAMMER.get())
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "plate/from_ingot/" + material.getId()));
					}

					if (processedType.contains("gear")) {
						// Gear from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.gearMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.define('N', Tags.Items.NUGGETS_IRON)
								.pattern(" I ")
								.pattern("INI")
								.pattern(" I ")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gear/from_ingot/" + material.getId()));
					}

					if (processedType.contains("rod")) {
						// Rod from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.rodMap.get(material), 2)
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.pattern("I")
								.pattern("I")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "rod/from_ingot/" + material.getId()));
					}

					if (processedType.contains("ore")) {
						// Ingot from Smelting Ore
						SimpleCookingRecipeBuilder.smelting(Ingredient.of(EETags.MATERIAL_ORE.apply(material.getId())),
										RecipeCategory.MISC,
										EERegistrar.ingotMap.get(material), 1.0F, 200)
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_ore/smelting/" + material.getId()));
						// Ingot from Blasting Ore
						SimpleCookingRecipeBuilder.blasting(Ingredient.of(EETags.MATERIAL_ORE.apply(material.getId())),
										RecipeCategory.MISC,
										EERegistrar.ingotMap.get(material), 1.0F, 100)
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_ore/blasting/" + material.getId()));
					}

					if (processedType.contains("raw")) {
						// Ingot from Smelting Raw Material
						SimpleCookingRecipeBuilder.smelting(Ingredient.of(EETags.MATERIAL_RAW.apply(material.getId())),
										RecipeCategory.MISC,
										EERegistrar.ingotMap.get(material), 1.0F, 200)
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_raw/smelting/" + material.getId()));
						// Ingot from Blasting Raw Material
						SimpleCookingRecipeBuilder.blasting(Ingredient.of(EETags.MATERIAL_RAW.apply(material.getId())),
										RecipeCategory.MISC,
										EERegistrar.ingotMap.get(material), 1.0F, 100)
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_raw/blasting/" + material.getId()));
					}

					if (processedType.contains("helmet")) {
						// Helmet from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.helmetMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.pattern("III")
								.pattern("I I")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "helmet/from_ingot/" + material.getId()));
					}

					if (processedType.contains("chestplate")) {
						// Chestplate from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.chestplateMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.pattern("I I")
								.pattern("III")
								.pattern("III")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chestplate/from_ingot/" + material.getId()));
					}

					if (processedType.contains("leggings")) {
						// Leggings from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.leggingsMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.pattern("III")
								.pattern("I I")
								.pattern("I I")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "leggings/from_ingot/" + material.getId()));
					}

					if (processedType.contains("boots")) {
						// Boots from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.bootsMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.pattern("I I")
								.pattern("I I")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "boots/from_ingot/" + material.getId()));
					}

					/* FIXME: Smithing recipes need Smithing Templates.
					if (processedType.contains("shield")) {
						// Shield from Ingot
						SmithingTransformRecipeBuilder.smithing(
										Ingredient.of(Items.SHIELD),
										Ingredient.of(EETags.MATERIAL_INGOT.apply(material.getId())),
										EERegistrar.shieldMap.get(material))
								.unlocks("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shield/from_ingot/" + material.getId()));
					}
					 */

					if (processedType.contains("sword")) {
						// Sword from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.swordMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.define('#', Items.STICK)
								.pattern("I")
								.pattern("I")
								.pattern("#")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "sword/from_ingot/" + material.getId()));
					}

					if (processedType.contains("pickaxe")) {
						// Pickaxe from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.pickaxeMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.define('#', Items.STICK)
								.pattern("III")
								.pattern(" # ")
								.pattern(" # ")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "pickaxe/from_ingot/" + material.getId()));
					}

					if (processedType.contains("axe")) {
						// Axe from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.axeMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.define('#', Items.STICK)
								.pattern("II")
								.pattern("I#")
								.pattern(" #")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "axe/from_ingot/" + material.getId()));
					}

					if (processedType.contains("shovel")) {
						// Shovel from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.shovelMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.define('#', Items.STICK)
								.pattern("I")
								.pattern("#")
								.pattern("#")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shovel/from_ingot/" + material.getId()));
					}

					if (processedType.contains("hoe")) {
						// Hoe from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.hoeMap.get(material))
								.define('I', EETags.MATERIAL_INGOT.apply(material.getId()))
								.define('#', Items.STICK)
								.pattern("II")
								.pattern(" #")
								.pattern(" #")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "hoe/from_ingot/" + material.getId()));
					}

					if (processedType.contains("paxel") && processedType.contains("pickaxe") && processedType.contains("axe") && processedType.contains("shovel")) {
						// Paxel from Ingot
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.paxelMap.get(material))
								.define('P', EERegistrar.pickaxeMap.get(material))
								.define('A', EERegistrar.axeMap.get(material))
								.define('S', EERegistrar.shovelMap.get(material))
								.define('#', Items.STICK)
								.pattern("PAS")
								.pattern(" # ")
								.pattern(" # ")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "paxel/from_ingot/" + material.getId()));
					}
				}

				// Gem recipes
				if (processedType.contains("gem")) {
					if (processedType.contains("storage_block")) {
						if (material.getProperties().getBlockRecipeType() == 4) {
							// Block from Gem x4
							ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EERegistrar.storageBlockItemMap.get(material))
									.define('#', EETags.MATERIAL_GEM.apply(material.getId()))
									.pattern("##")
									.pattern("##")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/from_gem/" + material.getId()));

							//Gem from Block x4
							ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.gemMap.get(material), 4)
									.requires(EETags.MATERIAL_STORAGE_BLOCK.apply(material.getId()))
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gem/from_block/" + material.getId()));
						}

						if (material.getProperties().getBlockRecipeType() == 9) {
							// Block from Gem x9
							ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EERegistrar.storageBlockItemMap.get(material))
									.define('#', EETags.MATERIAL_GEM.apply(material.getId()))
									.pattern("###")
									.pattern("###")
									.pattern("###")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/from_gem/" + material.getId()));

							// Gem from Block x9
							ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.gemMap.get(material), 9)
									.requires(EETags.MATERIAL_STORAGE_BLOCK.apply(material.getId()))
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gem/from_block/" + material.getId()));
						}

						//Waxed
						if (material.getProperties().hasOxidization()) {
							ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, EERegistrar.waxedStorageBlockItemMap.get(material))
									.requires(EERegistrar.storageBlockItemMap.get(material))
									.requires(Items.HONEYCOMB)
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed/from_block/" + material.getId() + "_block"));
							ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, EERegistrar.waxedExposedBlockItemMap.get(material))
									.requires(EERegistrar.exposedBlockItemMap.get(material))
									.requires(Items.HONEYCOMB)
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed/from_block/exposed_" + material.getId()));
							ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, EERegistrar.waxedWeatheredBlockItemMap.get(material))
									.requires(EERegistrar.weatheredBlockItemMap.get(material))
									.requires(Items.HONEYCOMB)
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed/from_block/weathered_" + material.getId()));
							ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, EERegistrar.waxedOxidizedBlockItemMap.get(material))
									.requires(EERegistrar.oxidizedBlockItemMap.get(material))
									.requires(Items.HONEYCOMB)
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "waxed/from_block/oxidized_" + material.getId()));
						}

						if (processedType.contains("plate")) {
							// Plate from Gem
							ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.plateMap.get(material), 1)
									.requires(EETags.MATERIAL_GEM.apply(material.getId()))
									.requires(EERegistrar.ENIGMATIC_HAMMER.get())
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "plate/from_gem/" + material.getId()));
						}

						if (processedType.contains("gear")) {
							// Gear from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.gearMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.define('N', Tags.Items.NUGGETS_IRON)
									.pattern(" G ")
									.pattern("GNG")
									.pattern(" G ")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gear/from_gem/" + material.getId()));
						}

						if (processedType.contains("rod")) {
							// Rod from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.rodMap.get(material), 2)
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.pattern("G")
									.pattern("G")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "rod/from_gem/" + material.getId()));
						}

						if (processedType.contains("helmet")) {
							// Helmet from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.helmetMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.pattern("GGG")
									.pattern("G G")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "helmet/from_gem/" + material.getId()));
						}

						if (processedType.contains("chestplate")) {
							// Chestplate from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.chestplateMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.pattern("G G")
									.pattern("GGG")
									.pattern("GGG")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chestplate/from_gem/" + material.getId()));
						}

						if (processedType.contains("leggings")) {
							// Leggings from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.leggingsMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.pattern("GGG")
									.pattern("G G")
									.pattern("G G")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "leggings/from_gem/" + material.getId()));
						}

						if (processedType.contains("boots")) {
							// Boots from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.bootsMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.pattern("G G")
									.pattern("G G")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "boots/from_gem/" + material.getId()));
						}

						/* FIXME: Requires Smithing Template!
						if (processedType.contains("shield")) {
							// Shield from Ingot

							UpgradeRecipeBuilder.smithing(
											Ingredient.of(Items.SHIELD),
											Ingredient.of(EETags.MATERIAL_GEM.apply(material.getId())),
											EERegistrar.shieldMap.get(material))
									.unlocks("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shield/from_gem/" + material.getId()));

						}
						 */

						if (processedType.contains("sword")) {
							// Sword from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.swordMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.define('#', Items.STICK)
									.pattern("G")
									.pattern("G")
									.pattern("#")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "sword/from_gem/" + material.getId()));
						}

						if (processedType.contains("pickaxe")) {
							// Pickaxe from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.pickaxeMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.define('#', Items.STICK)
									.pattern("GGG")
									.pattern(" # ")
									.pattern(" # ")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "pickaxe/from_gem/" + material.getId()));
						}

						if (processedType.contains("axe")) {
							// Axe from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.axeMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.define('#', Items.STICK)
									.pattern("GG")
									.pattern("G#")
									.pattern(" #")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "axe/from_gem/" + material.getId()));
						}

						if (processedType.contains("shovel")) {
							// Shovel from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.shovelMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.define('#', Items.STICK)
									.pattern("G")
									.pattern("#")
									.pattern("#")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shovel/from_gem/" + material.getId()));
						}

						if (processedType.contains("hoe")) {
							// Hoe from Gem
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.hoeMap.get(material))
									.define('G', EETags.MATERIAL_GEM.apply(material.getId()))
									.define('#', Items.STICK)
									.pattern("GG")
									.pattern(" #")
									.pattern(" #")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "hoe/from_gem/" + material.getId()));
						}

						if (processedType.contains("paxel") && processedType.contains("pickaxe") && processedType.contains("axe") && processedType.contains("shovel")) {
							// Paxel from Ingot
							ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.paxelMap.get(material))
									.define('P', EERegistrar.pickaxeMap.get(material))
									.define('A', EERegistrar.axeMap.get(material))
									.define('S', EERegistrar.shovelMap.get(material))
									.define('#', Items.STICK)
									.pattern("PAS")
									.pattern(" # ")
									.pattern(" # ")
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "paxel/from_gem/" + material.getId()));
						}
					}

					if (processedType.contains("raw")) {
						// Raw Block from Raw Material
						ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.rawBlockItemMap.get(material))
								.define('#', EETags.MATERIAL_RAW.apply(material.getId()))
								.pattern("###")
								.pattern("###")
								.pattern("###")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "raw/block/from_material/" + material.getId()));

						// Raw Material from Raw Block
						ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.rawMap.get(material), 9)
								.requires(EETags.MATERIAL_RAW_STORAGE_BLOCK.apply(material.getId()))
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "raw/material/from_block/" + material.getId()));
					}

					if (processedType.contains("dust")) {
						if (processedType.contains("ore")) {
							// Dust from Ore
							ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.dustMap.get(material), 1)
									.requires(EETags.MATERIAL_ORE.apply(material.getId()))
									.requires(EERegistrar.ENIGMATIC_HAMMER.get())
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "dust/from_ore/" + material.getId()));
						}

						if (processedType.contains("raw")) {
							// Dust from Raw
							ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.dustMap.get(material), 1)
									.requires(EETags.MATERIAL_RAW.apply(material.getId()))
									.requires(EERegistrar.ENIGMATIC_HAMMER.get())
									.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
									.group(Reference.MOD_ID)
									.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "dust/from_raw/" + material.getId()));
						}
					}

					if (processedType.contains("cluster")) {
						// Spyglass
						ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.SPYGLASS)
								.define('#', EERegistrar.clusterShardMap.get(material))
								.define('X', EETags.MATERIAL_INGOT.apply("copper"))
								.pattern(" # ")
								.pattern(" X ")
								.pattern(" X ")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "spyglass/from_shard/" + material.getId()));
						// Tinted Glass
						ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.TINTED_GLASS, 2)
								.define('G', Blocks.GLASS)
								.define('S', EERegistrar.clusterShardMap.get(material))
								.pattern(" S ")
								.pattern("SGS")
								.pattern(" S ")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "tinted_glass/from_shard/" + material.getId()));
						// Cluster Shard Block
						ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EERegistrar.clusterShardBlockItemMap.get(material))
								.define('S', EERegistrar.clusterShardMap.get(material))
								.pattern("SS")
								.pattern("SS")
								.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
								.group(Reference.MOD_ID)
								.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/from_shard/" + material.getId()));
					}
				}

				// Vanilla Compat
				if (material.isVanilla()) {
					if (material.getProperties().getMaterialType().equals("gem")) {
						Map<String, Item> vanillaGems = new HashMap<>();
						switch (material.getId()) {
							case "coal" -> vanillaGems.put(material.getId(), Items.COAL);
							case "diamond" -> vanillaGems.put(material.getId(), Items.DIAMOND);
							case "lapis" -> vanillaGems.put(material.getId(), Items.LAPIS_LAZULI);
							case "quartz" -> vanillaGems.put(material.getId(), Items.QUARTZ);
							case "redstone" -> vanillaGems.put(material.getId(), Items.REDSTONE);
						}

						for (Map.Entry<String, Item> mat : vanillaGems.entrySet()) {
							if (processedType.contains("plate")) {
								// Plate
								ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.plateMap.get(mat.getKey()), 1)
										.requires(mat.getValue())
										.requires(EERegistrar.ENIGMATIC_HAMMER.get())
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "plate/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("gear")) {
								// Gear
								ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.gearMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.define('N', Tags.Items.NUGGETS_IRON)
										.pattern(" G ")
										.pattern("GNG")
										.pattern(" G ")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gear/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("rod")) {
								// Rod
								ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.rodMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.pattern("G")
										.pattern("G")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "rod/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("dust")) {
								// Dust from Ore
								ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.dustMap.get(mat.getKey()), 1)
										.requires(EETags.MATERIAL_ORE.apply(mat.getKey()))
										.requires(EERegistrar.ENIGMATIC_HAMMER.get())
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "dust/from_ore/" + mat.getKey()));
							}

							if (processedType.contains("ore")) {
								// Ore Smelting
								SimpleCookingRecipeBuilder.smelting(Ingredient.of(EETags.MATERIAL_ORE.apply(mat.getKey())),
												RecipeCategory.MISC,
												mat.getValue(), 0.1F, 200)
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gem/from_ore/smelting/" + mat.getKey()));

								// Ore Blasting
								SimpleCookingRecipeBuilder.blasting(Ingredient.of(EETags.MATERIAL_ORE.apply(mat.getKey())),
												RecipeCategory.MISC,
												mat.getValue(), 0.1F, 100)
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gem/from_ore/blasting/" + mat.getKey()));
							}

							if (processedType.contains("helmet")) {
								// Helmet from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.helmetMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.pattern("GGG")
										.pattern("G G")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "helmet/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("chestplate")) {
								// Chestplate from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.chestplateMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.pattern("G G")
										.pattern("GGG")
										.pattern("GGG")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chestplate/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("leggings")) {
								// Leggings from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.leggingsMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.pattern("GGG")
										.pattern("G G")
										.pattern("G G")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "leggings/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("boots")) {
								// Boots from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.bootsMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.pattern("G G")
										.pattern("G G")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "boots/from_gem/" + mat.getKey()));
							}

							/* FIXME: Smithing requires Smithing Template.
							if (processedType.contains("shield")) {
								// Shield from Ingot
								UpgradeRecipeBuilder.smithing(
												Ingredient.of(Items.SHIELD),
												Ingredient.of(mat.getValue()),
												EERegistrar.shieldMap.get(mat.getKey()))
										.unlocks("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shield/from_gem/" + mat.getKey()));
							}
							 */

							if (processedType.contains("sword")) {
								// Sword from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.swordMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.define('#', Items.STICK)
										.pattern("G")
										.pattern("G")
										.pattern("#")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "sword/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("pickaxe")) {
								// Pickaxe from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.pickaxeMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.define('#', Items.STICK)
										.pattern("GGG")
										.pattern(" # ")
										.pattern(" # ")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "pickaxe/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("axe")) {
								// Axe from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.axeMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.define('#', Items.STICK)
										.pattern("GG")
										.pattern("G#")
										.pattern(" #")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "axe/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("shovel")) {
								// Shovel from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.shovelMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.define('#', Items.STICK)
										.pattern("G")
										.pattern("#")
										.pattern("#")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shovel/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("hoe")) {
								// Hoe from Gem
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.hoeMap.get(mat.getKey()))
										.define('G', mat.getValue())
										.define('#', Items.STICK)
										.pattern("GG")
										.pattern(" #")
										.pattern(" #")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "hoe/from_gem/" + mat.getKey()));
							}

							if (processedType.contains("paxel") && processedType.contains("pickaxe") && processedType.contains("axe") && processedType.contains("shovel")) {
								// Paxel from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.paxelMap.get(mat.getKey()))
										.define('P', EERegistrar.pickaxeMap.get(mat.getKey()))
										.define('A', EERegistrar.axeMap.get(mat.getKey()))
										.define('S', EERegistrar.shovelMap.get(mat.getKey()))
										.define('#', Items.STICK)
										.pattern("PAS")
										.pattern(" # ")
										.pattern(" # ")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "paxel/from_gem/" + mat.getKey()));
							}
						}
					}

					if (material.getProperties().getMaterialType().equals("metal")) {
						Map<String, Item> vanillaMetals = new HashMap<>();

						switch (material.getId()) {
							case "copper" -> vanillaMetals.put(material.getId(), Items.COPPER_INGOT);
							case "gold" -> vanillaMetals.put(material.getId(), Items.GOLD_INGOT);
							case "iron" -> vanillaMetals.put(material.getId(), Items.IRON_INGOT);
						}

						for (Map.Entry<String, Item> mat : vanillaMetals.entrySet()) {
							if (processedType.contains("plate")) {
								// Plate
								ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.plateMap.get(mat.getKey()), 1)
										.requires(mat.getValue())
										.requires(EERegistrar.ENIGMATIC_HAMMER.get())
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "plate/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("gear")) {
								// Gear
								ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.gearMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.define('N', Tags.Items.NUGGETS_IRON)
										.pattern(" I ")
										.pattern("INI")
										.pattern(" I ")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "gear/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("rod")) {
								// Rod
								ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EERegistrar.rodMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.pattern("I")
										.pattern("I")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "rod/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("dust")) {
								// Dust from Ore
								ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.dustMap.get(mat.getKey()), 1)
										.requires(EETags.MATERIAL_ORE.apply(mat.getKey()))
										.requires(EERegistrar.ENIGMATIC_HAMMER.get())
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "dust/from_ore/" + mat.getKey()));

								// Dust from Raw
								ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EERegistrar.dustMap.get(mat.getKey()), 1)
										.requires(EETags.MATERIAL_RAW.apply(mat.getKey()))
										.requires(EERegistrar.ENIGMATIC_HAMMER.get())
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "dust/from_raw/" + mat.getKey()));

								// Dust Smelting
								SimpleCookingRecipeBuilder.smelting(Ingredient.of(EETags.MATERIAL_DUST.apply(mat.getKey())),
												RecipeCategory.MISC,
												mat.getValue(), 0.7F, 200)
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_dust/smelting/" + mat.getKey()));

								// Dust Blasting
								SimpleCookingRecipeBuilder.blasting(Ingredient.of(EETags.MATERIAL_DUST.apply(mat.getKey())),
												RecipeCategory.MISC,
												mat.getValue(), 0.7F, 100)
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_dust/blasting/" + mat.getKey()));
							}

							if (processedType.contains("ore")) {
								// Ore Smelting
								SimpleCookingRecipeBuilder.smelting(Ingredient.of(EETags.MATERIAL_ORE.apply(mat.getKey())),
												RecipeCategory.MISC,
												mat.getValue(), 0.1F, 200)
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_ore/smelting/" + mat.getKey()));

								// Ore Blasting
								SimpleCookingRecipeBuilder.blasting(Ingredient.of(EETags.MATERIAL_ORE.apply(mat.getKey())),
												RecipeCategory.MISC,
												mat.getValue(), 0.1F, 100)
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ingot/from_ore/blasting/" + mat.getKey()));
							}

							if (processedType.contains("helmet")) {
								// Helmet from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.helmetMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.pattern("III")
										.pattern("I I")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "helmet/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("chestplate")) {
								// Chestplate from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.chestplateMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.pattern("I I")
										.pattern("III")
										.pattern("III")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chestplate/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("leggings")) {
								// Leggings from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.leggingsMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.pattern("III")
										.pattern("I I")
										.pattern("I I")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "leggings/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("boots")) {
								// Boots from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.bootsMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.pattern("I I")
										.pattern("I I")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "boots/from_ingot/" + mat.getKey()));
							}

							/* FIXME: Smithing recipes need Smithing Templates.
							if (processedType.contains("shield")) {
								// Shield from Ingot
								UpgradeRecipeBuilder.smithing(
												Ingredient.of(Items.SHIELD),
												Ingredient.of(mat.getValue()),
												EERegistrar.shieldMap.get(mat.getKey()))
										.unlocks("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shield/from_ingot/" + mat.getKey()));
							}
							 */

							if (processedType.contains("sword")) {
								// Sword from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.swordMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.define('#', Items.STICK)
										.pattern("I")
										.pattern("I")
										.pattern("#")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "sword/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("pickaxe")) {
								// Pickaxe from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.pickaxeMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.define('#', Items.STICK)
										.pattern("III")
										.pattern(" # ")
										.pattern(" # ")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "pickaxe/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("axe")) {
								// Axe from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.axeMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.define('#', Items.STICK)
										.pattern("II")
										.pattern("I#")
										.pattern(" #")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "axe/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("shovel")) {
								// Shovel from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.shovelMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.define('#', Items.STICK)
										.pattern("I")
										.pattern("#")
										.pattern("#")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "shovel/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("hoe")) {
								// Hoe from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.hoeMap.get(mat.getKey()))
										.define('I', mat.getValue())
										.define('#', Items.STICK)
										.pattern("II")
										.pattern(" #")
										.pattern(" #")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "hoe/from_ingot/" + mat.getKey()));
							}

							if (processedType.contains("paxel") && processedType.contains("pickaxe") && processedType.contains("axe") && processedType.contains("shovel")) {
								// Paxel from Ingot
								ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EERegistrar.paxelMap.get(mat.getKey()))
										.define('P', EERegistrar.pickaxeMap.get(mat.getKey()))
										.define('A', EERegistrar.axeMap.get(mat.getKey()))
										.define('S', EERegistrar.shovelMap.get(mat.getKey()))
										.define('#', Items.STICK)
										.pattern("PAS")
										.pattern(" # ")
										.pattern(" # ")
										.unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
										.group(Reference.MOD_ID)
										.save(out, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "paxel/from_ingot/" + mat.getKey()));
							}
						}
					}
				}
			} /* Eventually: Maybe add commented code from original class. */
		}
	}
}