package com.ridanisaurus.emendatusenigmatica.registries.data;

import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * An "addon" to Java's HashMap, allowing for easier access to values with just material provided.
 * @apiNote Key and Value type is not customizable. Only Strings -> DeferredItem map is supported.
 */
public class EEItemMap<T extends Item> extends HashMap<String, DeferredItem<T>> {

    /**
     * Used to get the DeferredItem of the specified material.
     * @param model MaterialModel of the item.
     * @return DeferredItem associated with the material provided.
     */
    public DeferredItem<T> get(@NotNull MaterialModel model) {
        return this.get(model.getId());
    }

    /**
     * Used to get the item under a specified Material ID.
     * @param key Material ID under which the required item is stored.
     * @return Direct Reference to the item associated with the material provided.
     */
    public T getValue(@NotNull String key) {
        return this.get(key).get();
    }

    /**
     * Used to get the item of the specified material.
     * @param model MaterialModel of the item.
     * @return Direct Reference to the item associated with the material provided.
     */
    public T getValue(@NotNull MaterialModel model) {
        return this.get(model).get();
    }

    /**
     * Used to get the ID of the stored item with use of the material model.
     * @param model MaterialModel of the item.
     * @return ResourceLocation of the item associated with the material provided.
     */
    public ResourceLocation getId(@NotNull MaterialModel model) {
        return this.get(model).getId();
    }

    /**
     * Used to get the ID of the stored item with use of the material model.
     * @param model MaterialModel of the item.
     * @return String representation of the item ID associated with the material provided.
     */
    public String getIdAsString(MaterialModel model) {
        return this.getId(model).toString();
    }
}
