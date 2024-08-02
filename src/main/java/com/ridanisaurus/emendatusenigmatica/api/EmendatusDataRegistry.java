/*
 * MIT License
 *
 * Copyright (c) 2024. Ridanisaurus
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

package com.ridanisaurus.emendatusenigmatica.api;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.ridanisaurus.emendatusenigmatica.plugin.model.compat.CompatModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry of all the data loaded from the plugins in {@link com.ridanisaurus.emendatusenigmatica.loader.EELoader} using {@link IEmendatusPlugin}
 *
 * Here are stored all the materials, strata and compat.
 */
public class EmendatusDataRegistry {

    private final Map<String, MaterialModel> materials;
    private final List<StrataModel> strata;
    private final List<CompatModel> compat;
    private final Map<String, Integer> strataByIndex;

    public EmendatusDataRegistry() {
        this.materials = new HashMap<>();
        this.strata = new ArrayList<>();
        this.compat = new ArrayList<>();
        this.strataByIndex = new HashMap<>();
    }

    /**
     * Used to register new Material model, returning passed defaultModel or previous value under that id.
     * @param material ID of the material.
     * @param defaultModel MaterialModel to register under that id.
     * @return MaterialModel passed to the argument or previous MaterialModel that was registered under that id.
     */
    @CanIgnoreReturnValue
    public MaterialModel getMaterialOrRegister(String material, MaterialModel defaultModel){
        return this.materials.computeIfAbsent(material, s -> defaultModel);
    }

    /**
     * Used to get MaterialModel by its ID.
     * @param materialID MaterialID to get model of.
     * @return MaterialModel under that ID, or null if not registered.
     */
    public @Nullable MaterialModel getMaterialByID(String materialID) {
        return this.materials.get(materialID);
    }

    public void registerStrata(StrataModel strataModel){
        this.strata.add(strataModel);
        this.strataByIndex.put(strataModel.getFillerType().toString(), this.strata.size() - 1);
    }

    public void registerCompat(CompatModel compatModel){
        this.compat.add(compatModel);
    }


    public List<MaterialModel> getMaterials(){
        return ImmutableList.copyOf(materials.values());
    }

    public List<StrataModel> getStrata(){
        return ImmutableList.copyOf(strata);
    }

    public List<CompatModel> getCompat() {
        return ImmutableList.copyOf(compat);
    }

    /**
     * Used to get Map with Strata Filler Types mapped to indexes of {@link EmendatusDataRegistry#strata} list.
     * @return Map with mapping of Strata Filler Type -> Index of the model
     */
    public Map<String, Integer> getStrataByIndex() {
        return strataByIndex;
    }
}
