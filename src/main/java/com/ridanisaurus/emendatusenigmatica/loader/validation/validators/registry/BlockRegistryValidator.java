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

package com.ridanisaurus.emendatusenigmatica.loader.validation.validators.registry;

import com.ridanisaurus.emendatusenigmatica.loader.validation.RegistryValidationData;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationData;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockRegistryValidator extends AbstractRegistryValidator {
    private static final List<ResourceLocation> overrides = List.of(Reference.AIR_RS);
    public BlockRegistryValidator() {
        super("Provided ResourceLocation doesn't point to a valid block in the registry!");
    }

    /**
     * This method is used to check if the resource location is present in the specified registry.
     *
     * @param data Record with ResourceLocation to validate and {@link ValidationData} object from the original json file.
     * @return The result of the validation. See {@link Result} for more details.
     * @apiNote At this stage, all registries were constructed,
     */
    @Override
    public Result validate(@NotNull RegistryValidationData data) {
        if (BuiltInRegistries.BLOCK.containsKey(data.location()) || overrides.contains(data.location())) return Result.PASS;
        return Result.FATAL;
    }
}
