/*
 * MIT License
 *
 * Copyright (c) 2020-2024. Ridanisaurus
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

package com.ridanisaurus.emendatusenigmatica.plugin.model.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationManager;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.ArrayPolicy;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.FilterMode;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.Types;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.TypeValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.ValuesValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.DefaultLoader;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.EERegistryValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.material.*;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import java.util.*;

public class MaterialModel {
	public static final Codec<MaterialModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.STRING.fieldOf("id").forGetter(i -> i.id),
			Codec.STRING.fieldOf("source").forGetter(i -> i.source),
			Codec.STRING.fieldOf("localizedName").forGetter(i -> i.localizedName),
			Codec.BOOL.optionalFieldOf("disableDefaultOre").forGetter(i -> Optional.of(i.disableDefaultOre)),
			Codec.list(Codec.STRING).fieldOf("processedTypes").forGetter(i -> i.processedTypes),
			Codec.list(Codec.STRING).optionalFieldOf("strata").forGetter(i -> Optional.of(i.strata)),
			MaterialPropertiesModel.CODEC.optionalFieldOf("properties").forGetter(i -> Optional.of(i.properties)),
			MaterialGasPropertiesModel.CODEC.optionalFieldOf("gas").forGetter(i -> Optional.of(i.gas)),
			MaterialOreDropModel.CODEC.optionalFieldOf("oreDrop").forGetter(i -> Optional.of(i.oreDrop)),
			MaterialCompatModel.CODEC.optionalFieldOf("compat").forGetter(i -> Optional.of(i.compat)),
			MaterialColorsModel.CODEC.optionalFieldOf("colors").forGetter(i -> Optional.of(i.colors)),
			MaterialToolsModel.CODEC.optionalFieldOf("tools").forGetter(i -> Optional.of(i.tools)),
			MaterialArmorModel.CODEC.optionalFieldOf("armor").forGetter(i -> Optional.of(i.armor))
	).apply(x, (id, source, localizedName, disableDefaultOre, processedTypes, strata, properties, gas, oreDrop, compat, colors, tools, armor) -> new MaterialModel(
			id,
			source,
			localizedName,
			disableDefaultOre.orElse(false),
			processedTypes,
			strata.orElse(List.of()),
			properties.orElse(new MaterialPropertiesModel()),
			gas.orElse(new MaterialGasPropertiesModel()),
			oreDrop.orElse(new MaterialOreDropModel()),
			compat.orElse(new MaterialCompatModel()),
			colors.orElse(new MaterialColorsModel()),
			tools.orElse(new MaterialToolsModel()),
			armor.orElse(new MaterialArmorModel())
	)));

	private final String id;
	private final String source;
	private final String localizedName;
	private final boolean disableDefaultOre;
	private final List<String> processedTypes;
	private final List<String> strata;
	private final MaterialPropertiesModel properties;
	private final MaterialGasPropertiesModel gas;
	private final MaterialOreDropModel oreDrop;
	private final MaterialCompatModel compat;
	private final MaterialColorsModel colors;
	private final MaterialToolsModel tools;
	private final MaterialArmorModel armor;

	public static final ValidationManager VALIDATION_MANAGER = ValidationManager.create()
		.addValidator("strata",				new EERegistryValidator(DefaultLoader.STRATA_IDS, EERegistryValidator.REFERENCE, "Strata", false), ArrayPolicy.REQUIRES_ARRAY)
		.addValidator("id",					new EERegistryValidator(DefaultLoader.MATERIAL_IDS, EERegistryValidator.REGISTRATION, true))
		.addValidator("source",				new ValuesValidator(List.of("vanilla", "modded"), FilterMode.WHITELIST, true))
		.addValidator("disableDefaultOre",	new TypeValidator(Types.BOOLEAN, false))
		.addValidator("localizedName",		new TypeValidator(Types.STRING, true))
		.addValidator("processedTypes",		new ProcessedTypesValidator(), ArrayPolicy.REQUIRES_ARRAY)
		.addValidator("tools",				new ToolsFieldValidator())
		.addValidator("armor",				new ArmorFieldValidator())
		.addValidator("oreDrop",				new OreDropValidator())
		.addValidator("gas",					new ProcessedTypesContainValidator("gas", MaterialGasPropertiesModel.VALIDATION_MANAGER.getAsValidator(false)))
		.addValidator("properties",			MaterialPropertiesModel.VALIDATION_MANAGER.getAsValidator(false))
		.addValidator("colors",				MaterialColorsModel.VALIDATION_MANAGER.getAsValidator(false))
		.addValidator("compat",				MaterialCompatModel.VALIDATION_MANAGER.getAsValidator(false));

	public MaterialModel(String id, String source, String localizedName, boolean disableDefaultOre, List<String> processedTypes, List<String> strata,
	                     MaterialPropertiesModel properties, MaterialGasPropertiesModel gas, MaterialOreDropModel oreDrop, MaterialCompatModel compat, MaterialColorsModel colors, MaterialToolsModel tools, MaterialArmorModel armor) {
		this.id = id;
		this.source = source;
		this.localizedName = localizedName;
		this.disableDefaultOre = disableDefaultOre;
		this.processedTypes = processedTypes;
		this.strata = strata;
		this.properties = properties;
		this.gas = gas;
		this.oreDrop = oreDrop;
		this.compat = compat;
		this.colors = colors;
		this.tools = tools;
		this.armor = armor;
	}

	public String getId() {
		return id;
	}

	public String getSource() {
		return source;
	}

	public boolean isModded() {
		return !getSource().equals("vanilla");
	}

	public boolean isVanilla() {
		return getSource().equals("vanilla");
	}

	public String getLocalizedName() {
		return localizedName;
	}

	public boolean getDisableDefaultOre() {
		return disableDefaultOre;
	}

	public List<String> getProcessedTypes() {
		return processedTypes;
	}

	public List<String> getStrata() {
		return strata;
	}

	public MaterialPropertiesModel getProperties() {
		return properties;
	}

	public MaterialGasPropertiesModel getGas() {
		return gas;
	}

	public MaterialOreDropModel getOreDrop() {
		return oreDrop;
	}

	public MaterialCompatModel getCompat() {
		return compat;
	}

	public MaterialColorsModel getColors() {
		return colors;
	}

	public MaterialToolsModel getTools() {
		return tools;
	}

	public MaterialArmorModel getArmor() {
		return armor;
	}

	public ItemLike getOreDefaultDrop() {
		if (processedTypes.contains("ore")) {
			if (properties.getMaterialType().equals("gem")) {
				return processedTypes.contains("gem") ? EERegistrar.gemMap.getValue(id) : oreDrop.getDefaultItemDropAsItem();
			} else {
				return processedTypes.contains("raw") ? EERegistrar.rawMap.getValue(id) : oreDrop.getDefaultItemDropAsItem();
			}
		} else {
			return BuiltInRegistries.ITEM.get(Reference.AIR_RS);
		}
	}
}