package com.ridanisaurus.emendatusenigmatica;

import com.mojang.logging.LogUtils;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Reference.MOD_ID)
public class EmendatusEnigmatica {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // Creative Tabs Registration
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

    public EmendatusEnigmatica(@NotNull IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        EERegistrar.finalize(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);

        // Creative Tab Item Registration.
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Will probably be used to register stuff from the registries after generation.
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {}
    }
}
