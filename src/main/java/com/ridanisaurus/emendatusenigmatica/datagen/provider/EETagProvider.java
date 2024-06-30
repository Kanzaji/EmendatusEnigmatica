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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EETagProvider implements DataProvider {
	private static final Logger logger = LoggerFactory.getLogger("EE Tag Provider");
	protected final DataGenerator generator;

	public EETagProvider(DataGenerator gen) {
		this.generator = gen;
	}

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput directoryCache) {
		Path path = this.generator.getPackOutput().getOutputFolder();
		Set<ResourceLocation> set = Sets.newHashSet();
		buildTags((consumer) -> {
			if (!set.add(consumer.getId())) {
				throw new IllegalStateException("Duplicate JSON " + consumer.getId());
			} else {
				try {
					saveJSON(directoryCache, consumer.serializeJSON(), path.resolve("data/" + consumer.getId().getNamespace() + "/tags/" + consumer.getId().getPath() + ".json"));
				} catch (IOException e) {
					logger.error("Exception caught while saving JSON Files!", e);
				}
			}
		});
		return CompletableFuture.allOf();
	}

	private static void saveJSON(@NotNull CachedOutput directoryCache, JsonObject recipeJson, Path recipePath) throws IOException {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		//TODO: Make sure changing the hashing algorithm didn't break things. Murmur3 is apparently way faster than SHA-1 (which is deprecated) and 256
		HashingOutputStream hashingoutputstream = new HashingOutputStream(Hashing.murmur3_128(), bytearrayoutputstream);
		Writer writer = new OutputStreamWriter(hashingoutputstream, StandardCharsets.UTF_8);
		JsonWriter jsonwriter = new JsonWriter(writer);
		jsonwriter.setSerializeNulls(false);
		jsonwriter.setIndent("  ");
		GsonHelper.writeValue(jsonwriter, recipeJson, KEY_COMPARATOR);
		jsonwriter.close();
		directoryCache.writeIfNeeded(recipePath, bytearrayoutputstream.toByteArray(), hashingoutputstream.hash());
	}

	protected void buildTags(Consumer<IFinishedGenericJSON> consumer) {
		// It's called generic for a reason!
	}

	@Override
	public String getName() {
		return null;
	}
}
