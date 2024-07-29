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

package com.ridanisaurus.emendatusenigmatica.plugin.validation.logger;

import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class ValidationAnalytics {
    /**
     * Used to hold instance of the logger.
     */
    private static final class InstanceHolder { private static final ValidationAnalytics instance = new ValidationAnalytics(); }

    /**
     * Path to the file where all messages will be written to.
     */
    private final Path summaryFile;

    /**
     * Used to lock the analytics if they were already summarized on this launch of the game.
     */
    private boolean finalized = false;

    /**
     * Used to store messages and necessary data for each file.
     */
    private final Map<String, Map<String, Messages>> messages = new HashMap<>();

    /**
     * Used to store Categories, which will be used to organize the Validation Summary file.
     */
    private final Map<String, String> messageCategories = new LinkedHashMap<>();

    /**
     * Used to store Performance Analytics, which will get printed at the end of the Validation Summary file into the table.
     */
    private final Map<String, String> performanceMap = new LinkedHashMap<>();

    /**
     * Private constructor. This class is a singleton!
     */
    private ValidationAnalytics() {
        CONFIG_DIR = FMLPaths.CONFIGDIR.get().resolve("emendatusenigmatica/");
        summaryFile = CONFIG_DIR.resolve("Validation Results.md");
    }

    /**
     * Used as a cache of ConfigDir.
     */
    public static Path CONFIG_DIR = null;

    /**
     * Used to get an instance of the Logger.
     * @return Reference to an instance of the Logger.
     */
    public static ValidationAnalytics getInstance() {
        return ValidationAnalytics.InstanceHolder.instance;
    }

    /**
     * Used to get a path to a log file.
     * @return Copy of the Path object, representing the location of the summary file.
     */
    public Path getSummaryFile() {
        return this.summaryFile;
    }

    /**
     * Used to check if Analytics were finalized and saved to the file.<br>
     * When {@code true}, any further calls to other methods will result in an exception.
     * @return Status of the analytics.
     */
    public boolean isFinalized() {
        return this.finalized;
    }

    /**
     * Used to add warn messages for specified file.
     * @param msg Message to add
     * @param path Path to the element in question.
     * @param jsonPath Path to the json file.
     * @apiNote The types are determined based on the jsonPath, using format {@code type/folder_if_any/file.json}.
     * @see ValidationAnalytics#isFinalized()
     */
    public void warn(String msg, String path, String jsonPath) {
        if (finalized) throw new IllegalStateException("Analytics were already finalized!");
        this.messages.computeIfAbsent(StringUtils.substringBefore(jsonPath, "/"), it -> new HashMap<>())
            .computeIfAbsent(jsonPath, it -> new Messages(new ArrayList<>(), new ArrayList<>())).warnings().add(new Messages.WarningMessage(path, msg));
    }

    /**
     * Used to add error messages for specified file.
     * @param msg Message to add.
     * @param additional Additional details to be printed after "cause". This gets written directly into the file!
     * @param path Path to the element in question.
     * @param jsonPath Obfuscated path to the json file.
     * @apiNote The types are determined based on the jsonPath, using format {@code type/folder_if_any/file.json}.
     * @see ValidationAnalytics#isFinalized()
     */
    public void error(String msg, String additional, String path, String jsonPath) {
        if (finalized) throw new IllegalStateException("Analytics were already finalized!");
        this.messages.computeIfAbsent(StringUtils.substringBefore(jsonPath, "/"), it -> new HashMap<>())
            .computeIfAbsent(jsonPath, it -> new Messages(new ArrayList<>(), new ArrayList<>())).errors().add(new Messages.ErrorMessage(path, msg, additional));
    }

    /**
     * Used to add a new category to the validation summary file, under which all messages from the provided directory will be printed.
     * <br><br>
     * You can add your own category if your addon provides additional folder for the validation,
     * otherwise the messages from that directory will be printed under {@code Custom Messages} category.
     * @param header The String to print as the Category Name
     * @param directory Directory / Type for messages to print under this category.
     * @apiNote Take a note that categories added by this method, unlike Custom Messages, will appear in the validation summary even if no messages are found for it!
     * @see ValidationAnalytics#isFinalized()
     */
    public void addNewCategory(String header, String directory) {
        if (finalized) throw new IllegalStateException("Analytics were already finalized!");
        this.messageCategories.put(header, directory);
    }

    /**
     * Used to finalize the Validation Analytics and generate a summary file.<br>
     * This method will also lock the analytics instance, and block any further calls to its methods.
     * @see ValidationAnalytics#isFinalized()
     */
    public void finish() {
        if (finalized) throw new IllegalStateException("Analytics were already finalized!");
        try {
            if (Files.exists(this.summaryFile)) {
                Files.deleteIfExists(this.summaryFile);
                EmendatusEnigmatica.logger.info("Old Validation Summary file was deleted.");
            }
            // If somehow the config directory doesn't exist.
            Files.createDirectories(this.summaryFile.getParent());
            Files.createFile(this.summaryFile);
            this.writeSpacer();
            this.writeHeader("Emendatus Enigmatica Validation Results", 1);
            this.writeLine("Emendatus Enigmatica version: " + EmendatusEnigmatica.VERSION);
            this.writeLine("File generated at: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date()));
            this.writeSpacer();

            this.addNewCategory("Strata", "strata");
            this.addNewCategory("Materials", "material");
            this.addNewCategory("Compatibility", "compat");
            this.addNewCategory("Deposits", "deposit");

            this.messageCategories.forEach((header, type) -> {
                this.writeHeader(header, 2);
                this.printMessages(type);
            });

            // Print Messages from any custom directories, which don't have their own category, if any.
            if (!this.messages.isEmpty()) {
                this.writeHeader("Custom Messages", 2);
                this.messages.keySet().forEach(this::printMessages);
            }

            this.printPerformance();
            this.write("> You can disable the generation of this summary and speed up the validation in the configuration file!");

            this.finalized = true;
        } catch (Exception e) {
            EmendatusEnigmatica.logger.error("Exception caught while summarizing the validation results!", e);
        }
    }

    private void printMessages(@NotNull String key) {
        this.messages.computeIfAbsent(key, it -> new HashMap<>()).forEach((file, messages) -> {
            this.writeHeader("File <code>%s</code>".formatted(file), 3);
            if (!messages.warnings().isEmpty()) {
                this.writeLine("Warnings:");
                messages.warnings().forEach(warning -> {
                    this.writeLine("- Element: <code>%s</code>".formatted(warning.element()));
                    this.writeLine("Message: " + warning.message());
                    this.write(""); // Additional line to make "space" between list elements.
                });
            }

            if (!messages.errors().isEmpty()) {
                this.writeLine("Errors:");
                messages.errors().forEach(error -> {
                    this.writeLine("- Element: <code>%s</code>".formatted(error.element()));
                    this.writeLine("Cause: " + error.cause());
                    this.writeLine( error.additionalInfo());
                    this.write("\n"); // Additional line to make "space" between list elements.
                });
            }
        });

        if (this.messages.get(key).isEmpty()) this.writeLine("All files were parsed and registered successfully!");

        // Clears already printed messages, no need to store them in memory after saving.
        this.messages.remove(key);
    }

    private void printPerformance() {
        this.writeHeader("Additional Information", 2);
        this.writeHeader("Performance", 3);
        StringBuilder table = new StringBuilder();
        table.append("<table>");
        this.performanceMap.forEach((category, time) -> table
            .append("<tr>")
            .append("<td>")
            .append(category)
            .append("</td>")
            .append("<td>")
            .append(time)
            .append("</td>")
            .append("</tr>"));
        table.append("</table>");
        this.write(table.toString());
        this.write("\n");
    }

    public void addPerformanceAnalytic(String category, String time) {
        this.performanceMap.put(category, time);
    }

    private void writeHeader(String msg, int lvl) {
        if (lvl < 1 || lvl > 6) throw new IllegalArgumentException("Lvl out of range. Provided: %d, expected: [1-6]".formatted(lvl));
        this.write("%s %s".formatted("#".repeat(lvl), msg));
    }

    private void writeSpacer() {
        this.write("<hr>\n");
    }

    private void writeLine(String msg) {
        this.write(msg + "<br>");
    }

    private void write(String msg) {
        try {
            Files.writeString(this.summaryFile, msg + "\n", StandardOpenOption.APPEND);
        } catch (Exception e) {
            EmendatusEnigmatica.logger.error("Exception caught while logging a message!", e);
        }
    }
}
