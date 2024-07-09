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

package com.ridanisaurus.emendatusenigmatica.datagen.provider;

import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.ridanisaurus.emendatusenigmatica.datagen.IFinishedGenericJSON;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class EEBlockModelProvider implements DataProvider {
	protected final DataGenerator generator;

	public EEBlockModelProvider(DataGenerator gen) {
		this.generator = gen;
	}

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput directoryCache) {
		Path path = this.generator.getPackOutput().getOutputFolder();
		Set<ResourceLocation> set = Sets.newHashSet();
		List<CompletableFuture<?>> cs = new ArrayList<>();
		buildBlockModel((consumer) -> {
			if (!set.add(consumer.getId())) throw new IllegalStateException("Duplicate JSON " + consumer.getId());
			cs.add(DataProvider.saveStable(
				directoryCache,
				consumer.serializeJSON(),
				path.resolve("assets/" + consumer.getId().getNamespace() + "/models/block/" + consumer.getId().getPath() + ".json")
			));
		});
		return CompletableFuture.allOf(cs.toArray(new CompletableFuture<?>[]{}));
	}

	protected abstract void buildBlockModel(Consumer<IFinishedGenericJSON> consumer);

	@Override
	public abstract @NotNull String getName();
}