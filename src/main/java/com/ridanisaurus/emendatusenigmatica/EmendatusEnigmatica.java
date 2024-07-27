package com.ridanisaurus.emendatusenigmatica;

import com.mojang.logging.LogUtils;
import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.config.ConfigMenu;
import com.ridanisaurus.emendatusenigmatica.config.EEConfig;
import com.ridanisaurus.emendatusenigmatica.datagen.DataGeneratorFactory;
import com.ridanisaurus.emendatusenigmatica.datagen.EEDataGenerator;
import com.ridanisaurus.emendatusenigmatica.datagen.EEPackFinder;
import com.ridanisaurus.emendatusenigmatica.loader.EELoader;
import com.ridanisaurus.emendatusenigmatica.plugin.deposit.EEDeposits;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.tabs.EECreativeTab;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.progress.StartupNotificationManager;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Reference.MOD_ID)
public class EmendatusEnigmatica {
    // Directly reference a slf4j logger
    public static final Logger logger = LogUtils.getLogger();
    private static EmendatusEnigmatica instance;
    private final EELoader loader;
    private final EEDataGenerator generator;

    // Creative Tabs Registration
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, EECreativeTab> TOOLS_TAB = CREATIVE_MODE_TABS.register("ee_tools_tab", () -> new EECreativeTab(
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.emendatusenigmatica.tools"))
            // Fallback
            .icon(() -> EERegistrar.ENIGMATIC_HAMMER.get().getDefaultInstance())
            .displayItems((parameters, output) -> output.accept(EERegistrar.ENIGMATIC_HAMMER))
    ));
    public static final DeferredHolder<CreativeModeTab, EECreativeTab> RESOURCES_TAB = CREATIVE_MODE_TABS.register("ee_resources_tab", () -> new EECreativeTab(
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.emendatusenigmatica.resources"))
            .withTabsBefore(TOOLS_TAB.getId())
            // Fallback
            .icon(() -> EERegistrar.FELINIUM_JAMINITE.get().getDefaultInstance())
            .displayItems((parameters, output) -> output.accept(EERegistrar.FELINIUM_JAMINITE))
    ));

    public EmendatusEnigmatica(@NotNull IEventBus modEventBus, ModContainer modContainer) throws IOException {
        instance = this;
        EEConfig.registerClient(modContainer);
        EEConfig.setupStartup(modContainer);

        DataGeneratorFactory.init();
        this.generator = DataGeneratorFactory.createEEDataGenerator();

        this.loader = new EELoader();
        this.loader.load();

        EERegistrar.finalize(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        this.loader.datagen(this.generator);

        // Creative Tab Item Registration.
        modEventBus.addListener(this::addCreative);
        // Virtual ResourcePack
        modEventBus.addListener(this::addPackFinder);
        // Generator check, we can't launch the game if the generator wasn't executed!
        modEventBus.addListener(this::hasGenerated);
        // Config screen
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (client, last) -> new ConfigMenu(last));
        this.loader.finish();
    }

    public static EmendatusEnigmatica getInstance() {
        return instance;
    }

    public EELoader getLoader() {
        return loader;
    }

    public EmendatusDataRegistry getDataRegistry() {
        return loader.getDataRegistry();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        EERegistrar.registerToCreativeTabs(event);
    }

    private void addPackFinder(@NotNull AddPackFindersEvent event) {
        event.addRepositorySource(new EEPackFinder(event.getPackType()));
        this.generator.run();
    }

    private void hasGenerated(FMLLoadCompleteEvent event) {
        if (this.generator.hasExecuted()) return;
        StartupNotificationManager.addModMessage("Emendatus Enigmatica - Missing Data Generation!");
        throw new IllegalStateException("Mod loading finished, but Data Generation wasn't executed!");
    }
}
