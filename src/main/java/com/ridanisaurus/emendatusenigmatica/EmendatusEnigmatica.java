package com.ridanisaurus.emendatusenigmatica;

import com.mojang.logging.LogUtils;
import com.ridanisaurus.emendatusenigmatica.config.EEConfig;
import com.ridanisaurus.emendatusenigmatica.datagen.DataGeneratorFactory;
import com.ridanisaurus.emendatusenigmatica.datagen.EEPackFinder;
import com.ridanisaurus.emendatusenigmatica.loader.EELoader;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
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
    public static final Logger LOGGER = LogUtils.getLogger();
    private static EmendatusEnigmatica instance;
    private final EELoader loader;
    private final DataGenerator generator;

    // Creative Tabs Registration
    //TODO: Find out how to make cycle-through-content icon here. It will be an interesting challenge!
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS_TAB = CREATIVE_MODE_TABS.register("ee_tools_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.emendatusenigmatica.tools"))
        .icon(() -> EERegistrar.ENIGMATIC_HAMMER.get().getDefaultInstance())
        .displayItems((parameters, output) -> output.accept(EERegistrar.ENIGMATIC_HAMMER)).build()
    );
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESOURCES_TAB = CREATIVE_MODE_TABS.register("ee_resources_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.emendatusenigmatica.resources"))
        .withTabsBefore(TOOLS_TAB.getId())
        .icon(() -> EERegistrar.FELINIUM_JAMINITE.get().getDefaultInstance())
        .displayItems((parameters, output) -> output.accept(EERegistrar.FELINIUM_JAMINITE)).build()
    );

    public EmendatusEnigmatica(@NotNull IEventBus modEventBus, ModContainer modContainer) throws IOException {
        instance = this;
        EEConfig.registerClient(modContainer);
        EEConfig.setupCommon(modContainer);

        DataGeneratorFactory.init();
        this.generator = DataGeneratorFactory.createEEDataGenerator();

        this.loader = new EELoader();
        this.loader.load();

        EERegistrar.finalize(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        this.loader.datagen(this.generator);

        // Creative Tab Item Registration.
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::addPackFinder);
        modEventBus.addListener(this::runGeneration);
        this.loader.finish();
    }

    public static EmendatusEnigmatica getInstance() {
        return instance;
    }

    public EELoader getLoader() {
        return loader;
    }

    private boolean generated = false;
    private void runGeneration(FMLCommonSetupEvent event) {
        try {
            if (generated) return;
            generated = true;
            this.generator.run();
        } catch (Exception e) {
            throw new RuntimeException("Exception caught while running data generation!", e);
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Will probably be used to register stuff from the registries after generation.
    }

    private void addPackFinder(@NotNull AddPackFindersEvent event) {
        event.addRepositorySource(new EEPackFinder(event.getPackType()));
    }
}
