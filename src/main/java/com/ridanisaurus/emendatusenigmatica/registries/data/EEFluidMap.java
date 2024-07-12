package com.ridanisaurus.emendatusenigmatica.registries.data;

import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * An "addon" to Java's HashMap, allowing for easier access to values with just material provided.
 * @apiNote Key and Value type is not customizable. Only Strings -> DeferredItem map is supported.
 */
public class EEFluidMap<T extends Fluid> extends HashMap<String, Supplier<T>> {

    /**
     * Used to get the Fluid Supplier under a specified Material ID.
     * @param key Material ID under which the required fluid is stored.
     * @return Supplier of the fluid associated with the material provided.
     */
    public Supplier<T> getSupplier(@NotNull String key) {
        return super.get(key);
    }

    /**
     * Used to get the Fluid Supplier of the specified material.
     * @param model MaterialModel of the fluid.
     * @return Supplier of the fluid associated with the material provided.
     */
    public Supplier<T> getSupplier(@NotNull MaterialModel model) {
        return this.getSupplier(model.getId());
    }

    /**
     * Used to get the Fluid under a specified Material ID.
     * @param key Material ID under which the required fluid is stored.
     * @return Direct Reference to the fluid associated with the material provided.
     */
    public T get(@NotNull String key) {
        return this.getSupplier(key).get();
    }

    /**
     * Used to get the Fluid of the specified material.
     * @param model MaterialModel of the fluid.
     * @return Direct Reference to the fluid associated with the material provided.
     */
    public T get(@NotNull MaterialModel model) {
        return this.getSupplier(model).get();
    }

    /**
     * Used to get the ID of the stored fluid with use of the material model.
     * @param model MaterialModel of the fluid.
     * @return ResourceLocation of the fluid associated with the material provided.
     * @apiNote This method uses deprecated methods to get the ResourceLocation of the fluid.
     * <br>This is what also Minecraft itself is using currently. No alternative is available at this point.
     * <br>See {@link FluidTagsProvider#FluidTagsProvider(PackOutput, CompletableFuture, String, ExistingFileHelper)}
     */
    @ApiStatus.Experimental
    public ResourceLocation getId(@NotNull MaterialModel model) {
        return this.get(model).builtInRegistryHolder().getKey().location();
    }

    /**
     * Used to get the ID of the stored fluid under provided ID.
     * @param key Material ID under which the required fluid is stored.
     * @return ResourceLocation of the fluid associated with the material provided.
     * @apiNote This method uses deprecated methods to get the ResourceLocation of the fluid.
     * <br>This is what also Minecraft itself is using currently. No alternative is available at this point.
     * <br>See {@link FluidTagsProvider#FluidTagsProvider(PackOutput, CompletableFuture, String, ExistingFileHelper)}
     */
    @ApiStatus.Experimental
    public ResourceLocation getId(@NotNull String key) {
        return this.get(key).builtInRegistryHolder().getKey().location();
    }

    /**
     * Used to get the ID of the stored fluid with use of the material model.
     * @param model MaterialModel of the fluid.
     * @return String representation of the fluid ID associated with the material provided.
     * @apiNote This method uses deprecated methods to get the ResourceLocation of the fluid.
     * <br>This is what also Minecraft itself is using currently. No alternative is available at this point.
     * <br>See {@link FluidTagsProvider#FluidTagsProvider(PackOutput, CompletableFuture, String, ExistingFileHelper)}
     */
    @ApiStatus.Experimental
    public String getIdAsString(MaterialModel model) {
        return this.getId(model).toString();
    }

    /**
     * Used to get the ID of the stored fluid under provided ID.
     * @param key Material ID under which the required fluid is stored.
     * @return String representation of the fluid ID associated with the material provided.
     * @apiNote This method uses deprecated methods to get the ResourceLocation of the fluid.
     * <br>This is what also Minecraft itself is using currently. No alternative is available at this point.
     * <br>See {@link FluidTagsProvider#FluidTagsProvider(PackOutput, CompletableFuture, String, ExistingFileHelper)}
     */
    @ApiStatus.Experimental
    public String getIdAsString(String key) {
        return this.getId(key).toString();
    }
}
