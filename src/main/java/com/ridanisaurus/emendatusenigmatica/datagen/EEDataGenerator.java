/*
 * MIT License
 *
 * Copyright (c) 2024. Ridanisaurus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
