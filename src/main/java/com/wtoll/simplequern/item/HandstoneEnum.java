package com.wtoll.simplequern.item;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum HandstoneEnum implements StringIdentifiable {
	NONE(0, "none"),
	CONCRETE(1, "handstone"),
	DIAMOND(2, "diamond_handstone"),
	OBSIDIAN(3, "obsidian_handstone");

	private final int id;
	private final String name;

	private static final HandstoneEnum[] ALL = values();
	private static final Map<String, HandstoneEnum> NAME_MAP = Arrays.stream(ALL).collect(Collectors.toMap(HandstoneEnum::getName, (handstoneEnum) -> handstoneEnum));
	private static final HandstoneEnum[] VALUES = Arrays.stream(ALL).sorted(Comparator.comparingInt((handstoneEnum -> handstoneEnum.id))).toArray(HandstoneEnum[]::new);

	HandstoneEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId(){
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String asString() {
		return this.name;
	}

	public static HandstoneEnum byName(String name) {
		return name == null ? null : NAME_MAP.get(name.toLowerCase(Locale.ROOT));
	}

	public static HandstoneEnum byId(int id) {
		return VALUES[MathHelper.abs(id % VALUES.length)];
	}
}
