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

package com.ridanisaurus.emendatusenigmatica.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ridanisaurus.emendatusenigmatica.items.FeliniumJaminiteIngot;
import com.ridanisaurus.emendatusenigmatica.items.ItemHammer;
import com.ridanisaurus.emendatusenigmatica.items.PaxelItem;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EERegistrar
{
    // Registries
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Reference.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Reference.MOD_ID);

    // Existing items.
    public static final DeferredItem<Item> ENIGMATIC_HAMMER = ITEMS.register("enigmatic_hammer", ItemHammer::new);
    public static final DeferredItem<Item> FELINIUM_JAMINITE = ITEMS.register("felinium_jaminite_ingot", FeliniumJaminiteIngot::new);

    // Ore Blocks
    public static Table<String, String, DeferredBlock<Block>> oreBlockTable = HashBasedTable.create();
    public static Table<String, String, DeferredItem<Item>> oreBlockItemTable = HashBasedTable.create();

    // Ore Sample Blocks
    public static Table<String, String, DeferredBlock<Block>> oreSampleBlockTable = HashBasedTable.create();
    public static Table<String, String, DeferredItem<Item>> oreSampleBlockItemTable = HashBasedTable.create();

    // Storage Blocks
    public static Map<String, DeferredBlock<Block>> storageBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> storageBlockItemMap = new HashMap<>();

    // Weathering Blocks
    public static Map<String, DeferredBlock<Block>> exposedBlockMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> weatheredBlockMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> oxidizedBlockMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> waxedStorageBlockMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> waxedExposedBlockMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> waxedWeatheredBlockMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> waxedOxidizedBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> exposedBlockItemMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> weatheredBlockItemMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> oxidizedBlockItemMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> waxedStorageBlockItemMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> waxedExposedBlockItemMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> waxedWeatheredBlockItemMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> waxedOxidizedBlockItemMap = new HashMap<>();

    // Raw Blocks
    public static Map<String, DeferredBlock<Block>> rawBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> rawBlockItemMap = new HashMap<>();

    // Cluster Blocks
    public static Map<String, DeferredBlock<Block>> buddingBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> buddingBlockItemMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> smallBudBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> smallBudBlockItemMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> mediumBudBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> mediumBudBlockItemMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> largeBudBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> largeBudBlockItemMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> clusterBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> clusterBlockItemMap = new HashMap<>();
    public static Map<String, DeferredBlock<Block>> clusterShardBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> clusterShardBlockItemMap = new HashMap<>();

    // Items
    public static Map<String, DeferredItem<Item>> rawMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> ingotMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> nuggetMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> gemMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> dustMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> plateMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> gearMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> rodMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> clusterShardMap = new HashMap<>();

    // Tools
    public static Map<String, DeferredItem<SwordItem>> swordMap = new HashMap<>();
    public static Map<String, DeferredItem<PickaxeItem>> pickaxeMap = new HashMap<>();
    public static Map<String, DeferredItem<AxeItem>> axeMap = new HashMap<>();
    public static Map<String, DeferredItem<ShovelItem>> shovelMap = new HashMap<>();
    public static Map<String, DeferredItem<HoeItem>> hoeMap = new HashMap<>();
    public static Map<String, DeferredItem<PaxelItem>> paxelMap = new HashMap<>();

    // Armor
    public static Map<String, DeferredItem<ArmorItem>> helmetMap = new HashMap<>();
    public static Map<String, DeferredItem<ArmorItem>> chestplateMap = new HashMap<>();
    public static Map<String, DeferredItem<ArmorItem>> leggingsMap = new HashMap<>();
    public static Map<String, DeferredItem<ArmorItem>> bootsMap = new HashMap<>();
    public static Map<String, DeferredItem<ShieldItem>> shieldMap = new HashMap<>();

    // Fluids
    public static Map<String, Supplier<FluidType>> fluidTypeMap = new HashMap<>();
    public static Map<String, Supplier<FlowingFluid>> fluidSourceMap = new HashMap<>();
    public static Map<String, Supplier<FlowingFluid>> fluidFlowingMap = new HashMap<>();
    public static Map<String, Supplier<LiquidBlock>> fluidBlockMap = new HashMap<>();
    public static Map<String, DeferredItem<Item>> fluidBucketMap = new HashMap<>();

    public static Supplier<FluidType> fluidType;
    public static Supplier<FlowingFluid> fluidSource;
    public static Supplier<FlowingFluid> fluidFlowing;
    public static Supplier<LiquidBlock> fluidBlock;
    public static Supplier<Item> fluidBucket;

    public static void finalize(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }

    public static final ResourceLocation FLUID_STILL_RL = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "fluids/fluid_still");
    public static final ResourceLocation FLUID_FLOWING_RL = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "fluids/fluid_flow");
    public static final ResourceLocation FLUID_OVERLAY_RL = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "fluids/fluid_overlay");

    public static Supplier<FluidType> fluidType;
    public static Supplier<FlowingFluid> fluidSource;
    public static Supplier<FlowingFluid> fluidFlowing;
    public static Supplier<LiquidBlock> fluidBlock;
    public static DeferredItem<Item> fluidBucket;

    // TODO: Add all the functions for registering things.
}
