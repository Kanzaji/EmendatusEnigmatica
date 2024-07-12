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
     * Used to get the DeferredBlock of the specified material.
     * @param model MaterialModel of the block.
     * @return DeferredBlock associated with the material provided.
     */
    public DeferredBlock<T> get(@NotNull MaterialModel model) {
        return this.get(model.getId());
    }

    /**
     * Used to get the block under a specified Material ID.
     * @param key Material ID under which the required block is stored.
     * @return Direct Reference to the block associated with the material provided.
     */
    public T getValue(@NotNull String key) {
        return this.get(key).get();
    }

    /**
     * Used to get the block of the specified material.
     * @param model MaterialModel of the block.
     * @return Direct Reference to the block associated with the material provided.
     */
    public T getValue(@NotNull MaterialModel model) {
        return this.get(model).get();
    }

    /**
     * Used to get the ID of the stored block with use of the material model.
     * @param model MaterialModel of the block.
     * @return ResourceLocation of the block associated with the material provided.
     */
    public ResourceLocation getId(@NotNull MaterialModel model) {
        return this.get(model).getId();
    }

    /**
     * Used to get the ID of the stored block with use of the material model.
     * @param model MaterialModel of the block.
     * @return String representation of the block ID associated with the material provided.
     */
    public String getIdAsString(MaterialModel model) {
        return this.getId(model).toString();
    }
}
