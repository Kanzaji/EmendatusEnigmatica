package com.ridanisaurus.emendatusenigmatica.registries.data;

import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * An "addon" to Java's HashMap, allowing for easier access to values with just material provided.
 * @apiNote Key and Value type is not customizable. Only Strings -> DeferredBlock map is supported.
 */
public class EEBlockMap<T extends Block> extends HashMap<String, DeferredBlock<T>> {

    /**
     * Utility method that wraps around HashMap {@code get()} method that accepts material and returns block.
     * @param material Material to get the block of.
     * @return Block associated with the Material provided.
     * @apiNote If DeferredBlock is required, use normal get() method of HashMap.
     */
    public T get(@NotNull MaterialModel material) {
        return this.get(material.getId()).get();
    }

    /**
     * Utility method that wraps around HashMap {@code get()} method that accepts material and returns the block ID.
     * @param material Material to get the block ID of.
     * @return ResourceLocation of the Block associated with the Material provided.
     */
    public ResourceLocation getId(@NotNull MaterialModel material) {
        return this.get(material.getId()).getId();
    }

    /**
     * Utility method that wraps around HashMap {@code get()} method that accepts material and returns the block ID as a string.
     * @param material Material to get the block id of.
     * @return String representation of the Block ResourceLocation, associated with the Material provided.
     */
    public String getIdAsString(MaterialModel material) {
        return this.getId(material).toString();
    }
}
