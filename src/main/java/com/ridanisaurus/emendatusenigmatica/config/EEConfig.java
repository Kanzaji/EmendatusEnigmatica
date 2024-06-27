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

package com.ridanisaurus.emendatusenigmatica.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ConfigFileTypeHandler;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;


public class EEConfig {
	public static ClientConfig client;
	public static CommonConfig common;

	public static void registerClient(ModContainer container) {
		Pair<ClientConfig, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
		client = clientSpecPair.getLeft();
		container.registerConfig(ModConfig.Type.CLIENT, clientSpecPair.getRight());
		EmendatusEnigmatica.LOGGER.info("Emendatus Enigmatica Client Config has been registered.");
	}

	public static void setupCommon(ModContainer container) {
		Pair<CommonConfig, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
		common = commonSpecPair.getLeft();
		ModConfigSpec commonSpec = commonSpecPair.getRight();

		try {
			// Even tho this uses ModConfig object,
			// which will be tracked for changes,
			// there are no listeners for this config, so it's going to be fine.
			CommentedFileConfig configData = new ConfigFileTypeHandler()
				.reader(FMLPaths.CONFIGDIR.get())
				.apply(new ModConfig(ModConfig.Type.COMMON, commonSpec, container));
			commonSpec.setConfig(configData);
			EmendatusEnigmatica.LOGGER.info("EmendatusEnigmatica Common Config has been parsed.");
		} catch (Exception e) {
			EmendatusEnigmatica.LOGGER.error("Failed parsing Common config!", e);
			throw new IllegalStateException("Common Config for EmendatusEnigmatica wasn't possible to parse.", e);
		}
	}

	public static class CommonConfig {
		public final ModConfigSpec.BooleanValue logConfigErrors;
		CommonConfig(ModConfigSpec.@NotNull Builder builder) {
			builder.push("Debug");
			logConfigErrors = builder
					.comment("Whether EmendatusEnigmatica should log warnings and errors generated on the configuration parsing.")
					.translation(Reference.MOD_ID + ".config.common.log_errors")
					.define("logConfigErrors", true);
			builder.pop();
		}
	}

	public static class ClientConfig {
		public final ModConfigSpec.BooleanValue showPatreonReward;
		ClientConfig(ModConfigSpec.@NotNull Builder builder) {
			builder.push("Patreon Reward");
			showPatreonReward = builder
					.comment("Whether the Patreon Reward should appear floating over the player's head")
					.translation(Reference.MOD_ID + ".config.client.show_reward")
					.define("showReward", true);
			builder.pop();
		}
	}
}