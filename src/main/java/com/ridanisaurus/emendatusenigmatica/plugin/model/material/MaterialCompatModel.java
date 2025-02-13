/*
 * MIT License
 *
 * Copyright (c) 2020-2024. Ridanisaurus
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

package com.ridanisaurus.emendatusenigmatica.plugin.model.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.loader.validation.ValidationManager;
import com.ridanisaurus.emendatusenigmatica.loader.validation.enums.Types;
import com.ridanisaurus.emendatusenigmatica.loader.validation.validators.TypeValidator;

import java.util.Optional;

public class MaterialCompatModel {
	public static final Codec<MaterialCompatModel> CODEC = RecordCodecBuilder.create(x -> x.group(
			Codec.BOOL.optionalFieldOf("create").forGetter(i -> Optional.of(i.create)),
			Codec.BOOL.optionalFieldOf("thermal").forGetter(i -> Optional.of(i.thermal)),
			Codec.BOOL.optionalFieldOf("mekanism").forGetter(i -> Optional.of(i.mekanism)),
			Codec.BOOL.optionalFieldOf("ars_nouveau").forGetter(i -> Optional.of(i.ars_nouveau)),
			Codec.BOOL.optionalFieldOf("blood_magic").forGetter(i -> Optional.of(i.blood_magic)),
			Codec.BOOL.optionalFieldOf("occultism").forGetter(i -> Optional.of(i.occultism))
	).apply(x, (create, thermal, mekanism, ars_nouveau, blood_magic, occultism) -> new MaterialCompatModel(
			create.orElse(true),
			thermal.orElse(true),
			mekanism.orElse(true),
			ars_nouveau.orElse(true),
			blood_magic.orElse(true),
			occultism.orElse(true)
	)));

	public static final ValidationManager VALIDATION_MANAGER = ValidationManager.create()
		.addValidator("create", new TypeValidator(Types.BOOLEAN, false))
		.addValidator("thermal", new TypeValidator(Types.BOOLEAN, false))
		.addValidator("mekanism", new TypeValidator(Types.BOOLEAN, false))
		.addValidator("ars_nouveau", new TypeValidator(Types.BOOLEAN, false))
		.addValidator("occultism", new TypeValidator(Types.BOOLEAN, false));

	private final boolean create;
	private final boolean thermal;
	private final boolean mekanism;
	private final boolean ars_nouveau;
	private final boolean blood_magic;
	private final boolean occultism;

	public MaterialCompatModel(boolean create, boolean thermal, boolean mekanism, boolean ars_nouveau, boolean blood_magic, boolean occultism) {
		this.create = create;
		this.thermal = thermal;
		this.mekanism = mekanism;
		this.ars_nouveau = ars_nouveau;
		this.blood_magic = blood_magic;
		this.occultism = occultism;
	}

	public MaterialCompatModel() {
		this.create = true;
		this.thermal = true;
		this.mekanism = true;
		this.ars_nouveau = true;
		this.blood_magic = true;
		this.occultism = true;
	}

	public boolean getCreateCompat() {
		return create;
	}

	public boolean getThermalCompat() {
		return thermal;
	}

	public boolean getMeknaismCompat() {
		return mekanism;
	}

	public boolean getANCompat() {
		return ars_nouveau;
	}

	public boolean getBMCompat() {
		return blood_magic;
	}

	public boolean getOccultismCompat() {
		return occultism;
	}
}