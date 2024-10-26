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
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.ColorValidator;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.deprecation.DeprecatedFieldValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.material.colors.ChemicalColorValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.material.colors.OxidizationColorValidator;
import com.ridanisaurus.emendatusenigmatica.plugin.validators.material.colors.ParticlesColorValidator;
import com.ridanisaurus.emendatusenigmatica.util.ColorHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MaterialColorsModel {
	public static final Codec<MaterialColorsModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.STRING.optionalFieldOf("fluidColor").forGetter(i -> Optional.of(i.fluidColor)),
			Codec.STRING.optionalFieldOf("chemicalColor").forGetter(i -> Optional.of(i.chemicalColor)),
			Codec.STRING.optionalFieldOf("particlesColor").forGetter(i -> Optional.of(i.particlesColor)),
			Codec.STRING.optionalFieldOf("materialColor").forGetter(i -> Optional.of(i.materialColor)),
			Codec.STRING.optionalFieldOf("oxidizationColor").forGetter(i -> Optional.of(i.oxidizationColor))
	).apply(x, (fluidColor, gasColor, particlesColor, materialColor, oxidizationColor) -> new MaterialColorsModel(
			fluidColor.orElse(null),
			gasColor.orElse(null),
			particlesColor.orElse(null),
			materialColor.orElse(null),
			oxidizationColor.orElse(null)
	)));

	//TODO: Move GasColor and ChemicalColor validators to the addon.
	public static final ValidationManager VALIDATION_MANAGER = ValidationManager.create()
		.addValidator("fluidColor",		new ColorValidator(false))
		.addValidator("materialColor",	new ColorValidator(false))
		.addValidator("particlesColor",	new ParticlesColorValidator())
		.addValidator("oxidizationColor",	new OxidizationColorValidator())
		.addValidator("chemicalColor",	new ChemicalColorValidator())
		.addValidator("gasColor", 		new DeprecatedFieldValidator("chemicalColor"));

	private final String fluidColor;
	private final String chemicalColor;
	private final String particlesColor;
	private final String materialColor;
	private final String oxidizationColor;

	public MaterialColorsModel(@Nullable String fluidColor, @Nullable String chemicalColor, @Nullable String particlesColor, @Nullable String materialColor, @Nullable String oxidizationColor) {
		this.fluidColor = fluidColor;
		this.chemicalColor = chemicalColor;
		this.particlesColor = particlesColor;
		this.materialColor = materialColor;
		this.oxidizationColor = oxidizationColor;
	}

	public MaterialColorsModel() {
		this.fluidColor = null;
		this.chemicalColor = null;
		this.particlesColor = null;
		this.materialColor = null;
		this.oxidizationColor = null;
	}

	public int getMaterialColor() {
		return hasMaterialColor() ? ColorHelper.HEXtoDEC(materialColor) : -1;
	}

	public int getHighlightColor(int factor) {
		return hasMaterialColor() ? ColorHelper.HEXtoDEC(ColorHelper.hueShift(materialColor, factor, true)) : -1;
	}

	public int getShadowColor(int factor) {
		return hasMaterialColor() ? ColorHelper.HEXtoDEC(ColorHelper.hueShift(materialColor, factor, false)) : -1;
	}

	public int getOxidizationColor() {
		return hasOxidizationColor() ? ColorHelper.HEXtoDEC(oxidizationColor) : -1;
	}

	public int getFluidColor() {
		return hasFluidColor() ? ColorHelper.HEXtoDEC(fluidColor) : -1;
	}

	public int getChemicalColor() {
		return hasGasColor() ? ColorHelper.HEXtoDEC(chemicalColor) : -1;
	}

	public int getParticlesColor() {
		return hasParticlesColor() ? ColorHelper.HEXtoDEC(particlesColor) : -1;
	}

	public boolean hasMaterialColor() {
		return materialColor != null;
	}

	public boolean hasOxidizationColor() {
		return oxidizationColor != null;
	}

	public boolean hasFluidColor() {
		return fluidColor != null;
	}

	public boolean hasGasColor() {
		return chemicalColor != null;
	}

	public boolean hasParticlesColor() {
		return particlesColor != null;
	}
}