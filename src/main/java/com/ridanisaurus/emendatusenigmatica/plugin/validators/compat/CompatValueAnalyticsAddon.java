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

package com.ridanisaurus.emendatusenigmatica.plugin.validators.compat;

import com.ridanisaurus.emendatusenigmatica.util.analytics.Analytics;
import com.ridanisaurus.emendatusenigmatica.util.analytics.AnalyticsAddon;
import com.ridanisaurus.emendatusenigmatica.util.analytics.AnalyticsWriteContext;

public class CompatValueAnalyticsAddon implements AnalyticsAddon {
    protected static boolean shouldRun = false;

    /**
     * Method executed by the {@link Analytics} with write context provided.
     *
     * @param cx Write Context for the file.
     */
    @Override
    public void accept(AnalyticsWriteContext cx) {
        if (!shouldRun) return;
        cx.writeLine("<code>root.recipes[x].values[x].input</code> in <code>compat</code> files is only required in scenarios specified in the table below!");
        StringBuilder table = new StringBuilder();
        table.append("<table><tr><th>Mod</th><th>Machine</th><th>Type</th></tr>");
        CompatValueInputValidator.VALUES.cellSet().forEach(cell -> {
            table
                .append("<tr><td>")
                .append(cell.getRowKey())
                .append("</td><td>")
                .append(cell.getColumnKey())
                .append("</td><td>")
                .append(cell.getValue())
                .append("</td></tr>");
        });
        table.append("</table>");
        cx.write(table.toString());
        cx.write("\n");
    }
}
