package com.ridanisaurus.emendatusenigmatica.tabs;

import com.google.common.base.Stopwatch;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class EECreativeTab extends CreativeModeTab {
    private final Stopwatch time = Stopwatch.createUnstarted();
    private final Function<EECreativeTab, List<ItemStack>> iconsFunction;
    private List<ItemStack> icons = null;
    private int index = 0;
    private int cachedSize = 0;
    /**
     * Used to create EECreativeTab with cycling icon.
     * @param builder CreativeTabBuilder, as usual.
     * @apiNote if 'iconsProvider' list is provided, Builder#icon() will be ignored!
     */
    public EECreativeTab(Builder builder, Function<EECreativeTab, List<ItemStack>> iconsProvider) {
        super(builder);
        this.iconsFunction = iconsProvider;
    }

    /**
     * Used to create EECreativeTab with default iconsProvider, accepting all items in the creative tab.
     * @param builder CreativeTabBuilder, as usual.
     * @apiNote Builder#icon() is ignored!
     */
    public EECreativeTab(Builder builder) {
        this(builder, (t) -> t.getDisplayItems().stream().toList());
    }

    public void reload() {
        this.icons = null;
        index = 0;
        cachedSize = 0;
    }

    @Override
    public @NotNull ItemStack getIconItem() {
        if (cachedSize == 0 && iconsFunction != null && icons == null) {
            icons = iconsFunction.apply(this);
            cachedSize = icons != null ? icons.size() : -1;
        }

        if (iconsFunction == null || cachedSize < 2) return super.getIconItem();

        if (!time.isRunning()) time.start();

        if (time.elapsed(TimeUnit.SECONDS) < 1) return icons.get(index);

        time.reset();
        index++;

        if (index >= cachedSize) index = 0;

        return icons.get(index);
    }
}
