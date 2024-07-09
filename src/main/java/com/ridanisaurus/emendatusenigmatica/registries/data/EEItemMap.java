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
     * Utility method that wraps around HashMap {@code get()} method that accepts material and returns item.
     * @param material Material to get the item of.
     * @return Item associated with the Material provided.
     * @apiNote If DeferredItem is required, use normal get() method of HashMap.
     */
    public T get(MaterialModel material) {
        return this.get(material.getId()).get();
    }

    /**
     * Utility method that wraps around HashMap {@code get()} method that accepts material and returns the item ID.
     * @param material Material to get the item ID of.
     * @return ResourceLocation of the Item associated with the Material provided.
     */
    public ResourceLocation getId(@NotNull MaterialModel material) {
        return this.get(material.getId()).getId();
    }

    /**
     * Utility method that wraps around HashMap {@code get()} method that accepts material and returns the item ID as a string.
     * @param material Material to get the item id of.
     * @return String representation of the Item ResourceLocation, associated with the Material provided.
     */
    public String getIdAsString(MaterialModel material) {
        return this.getId(material).toString();
    }
}
