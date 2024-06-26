package com.ridanisaurus.emendatusenigmatica.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import com.ridanisaurus.emendatusenigmatica.items.PaxelItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
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
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EmendatusEnigmatica.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EmendatusEnigmatica.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, EmendatusEnigmatica.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, EmendatusEnigmatica.MODID);

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

    // TODO: Make a replacement for ResourceLocation Fluid textures.

    public static Supplier<FluidType> fluidType;
    public static Supplier<FlowingFluid> fluidSource;
    public static Supplier<FlowingFluid> fluidFlowing;
    public static Supplier<LiquidBlock> fluidBlock;
    public static Supplier<Item> fluidBucket;

    //TODO: UNFINISHED
}
