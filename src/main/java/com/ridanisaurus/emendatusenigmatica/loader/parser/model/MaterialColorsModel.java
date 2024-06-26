/*
 *  MIT License
 *
 *  Copyright (c) 2020 Ridanisaurus
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.ridanisaurus.emendatusenigmatica.loader.parser.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.util.ColorHelper;

import java.util.Optional;

public class MaterialColorsModel
{
    // TODO 3.0: Change gasColor to chemicalColor
    // gasColor has been changed to chemicalColor. -Footer
    public static final Codec<MaterialColorsModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.optionalFieldOf("fluidColor").forGetter(i -> Optional.of(i.fluidColor)),
            Codec.STRING.optionalFieldOf("chemicalColor").forGetter(i -> Optional.of(i.gasColor)),
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

    private final String fluidColor;
    private final String gasColor;
    private final String particlesColor;
    private final String materialColor;
    private final String oxidizationColor;

    public MaterialColorsModel(String fluidColor, String gasColor, String particlesColor, String materialColor, String oxidizationColor) {
        this.fluidColor = fluidColor;
        this.gasColor = gasColor;
        this.particlesColor = particlesColor;
        this.materialColor = materialColor;
        this.oxidizationColor = oxidizationColor;
    }

    public MaterialColorsModel() {
        this.fluidColor = null;
        this.gasColor = null;
        this.particlesColor = null;
        this.materialColor = null;
        this.oxidizationColor = null;
    }

    public int getMaterialColor() {
        return materialColor != null ? ColorHelper.HEXtoDEC(materialColor) : -1;
    }

    public int getHighlightColor(int factor) {
        return materialColor != null ? ColorHelper.HEXtoDEC(ColorHelper.hueShift(materialColor, factor, true)) : -1;
    }

    public int getShadowColor(int factor) {
        return materialColor != null ? ColorHelper.HEXtoDEC(ColorHelper.hueShift(materialColor, factor, false)) : -1;
    }

    public int getOxidizationColor() {
        return oxidizationColor != null ? ColorHelper.HEXtoDEC(oxidizationColor) : -1;
    }

    public int getFluidColor() {
        return fluidColor != null ? ColorHelper.HEXtoDEC(fluidColor) : -1;
    }

    public int getGasColor() {
        return gasColor != null ? ColorHelper.HEXtoDEC(gasColor) : -1;
    }

    public int getParticlesColor() {
        return particlesColor != null ? ColorHelper.HEXtoDEC(particlesColor) : -1;
    }
}
