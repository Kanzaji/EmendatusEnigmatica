package com.ridanisaurus.emendatusenigmatica.plugin.model.compat;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.util.validation.Validator;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class CompatIOModel {
    public static final Codec<CompatIOModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.optionalFieldOf("item").forGetter(i -> Optional.ofNullable(i.item)),
            Codec.INT.optionalFieldOf("count").forGetter(i -> Optional.of(i.count)),
            Codec.FLOAT.optionalFieldOf("chance").forGetter(i -> Optional.of(i.chance))
    ).apply(x, (item, count, chance) -> new CompatIOModel(
            item.orElse(""),
            count.orElse(0),
            chance.orElse(0.0f)
    )));

    /**
     * Holds verifying functions for each field.
     * Function returns true if verification was successful, false otherwise to stop registration of the json.
     * Adding suffix _rg will request the original object instead of just the value of the field.
     */
    public static final Map<String, BiFunction<JsonElement, Path, Boolean>> validators = new LinkedHashMap<>();

    private final String item;
    private final int count;
    private final float chance;

    public CompatIOModel(String item, int count, float chance) {
        this.item = item;
        this.count = count;
        this.chance = chance;
    }

    public String getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public float getChance() {
        return chance;
    }

    static {
        validators.put("item", new Validator("item").getResourceIDValidation(false));
        validators.put("count", new Validator("count").getIntRange(0, 64, false));
        validators.put("chance", new Validator("chance").getRange(0, 1, false));
    }
}
