package com.ridanisaurus.emendatusenigmatica.datagen;

import com.mojang.logging.LogUtils;
import net.minecraft.WorldVersion;
import net.minecraft.data.DataGenerator;
import net.neoforged.fml.ModLoader;
import org.slf4j.Logger;

import java.nio.file.Path;

public class EEDataGenerator extends DataGenerator {
    private static final Logger logger = LogUtils.getLogger();
    private boolean alreadyExecuted = false;
    private boolean crashed = false;
    public EEDataGenerator(Path rootOutputFolder, WorldVersion version, boolean alwaysGenerate) {
        super(rootOutputFolder, version, alwaysGenerate);
    }

    @Override
    public void run() {
        //TODO: Add own caching logic. Currently entire DataGen has to be executed to even know what to cache.
        if (alreadyExecuted) return;
        alreadyExecuted = true;
        try {
            super.run();
        } catch (Exception e) {
            if (ModLoader.hasErrors()) {
                // If somehow there are errors, but minecraft will load, EE will crash on later stage.
                logger.error("Exception caught while running EE Data Generation, however different mod loading errors are present!");
                logger.error("This exception is most likely caused by another mod causing a crash earlier and is going to be suppressed.", e);
                crashed = true;
                return;
            }
            throw new RuntimeException("Caught exception while running EE Data Generation!", e);
        }
    }

    public boolean hasExecuted() {
        return alreadyExecuted && !crashed;
    }
}
