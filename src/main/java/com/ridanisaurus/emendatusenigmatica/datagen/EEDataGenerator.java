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

import com.google.common.base.Stopwatch;
import com.mojang.logging.LogUtils;
import com.ridanisaurus.emendatusenigmatica.util.analytics.Analytics;
import net.minecraft.WorldVersion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.loading.progress.StartupNotificationManager;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

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
            // Re-implementing the run logic here, to add our own analytics support.
            HashCache cache = new HashCache(this.rootOutputFolder, this.allProviderIds, this.version);
            Stopwatch sMain = Stopwatch.createStarted();
            Stopwatch sPerTask = Stopwatch.createUnstarted();
            //TODO: As this is executed on a main thread, it will hang the Early Window, so the progress bar is not visible in the end :/
            var bar = StartupNotificationManager.addProgressBar("Emendatus Enigmatica: Data Generation", this.providersToRun.size());
            this.providersToRun.forEach((name, provider) -> {
                logger.info("Starting provider: {}", name);
                sPerTask.start();
                cache.applyUpdate(cache.generateUpdate(name, provider::run).join());
                sPerTask.stop();
                logger.info("{} finished after {} ms", name, sPerTask.elapsed(TimeUnit.MILLISECONDS));
                sPerTask.reset();
            });
            cache.purgeStaleAndWrite();
            bar.complete();
            String msg = "EE Data Generation finished after %s ms.".formatted(sMain.elapsed(TimeUnit.MILLISECONDS));
            StartupNotificationManager.addModMessage(msg);
            logger.info(msg);
            Analytics.addPerformanceAnalytic("Data Generation", sMain);
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
