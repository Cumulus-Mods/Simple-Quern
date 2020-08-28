package com.wtoll.simplequern.item;

import com.google.common.collect.Lists;
import net.minecraft.state.property.EnumProperty;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HandstoneProperty extends EnumProperty<HandstoneEnum> {

	protected HandstoneProperty(String name, Collection<HandstoneEnum> values) {
		super(name, HandstoneEnum.class, values);
	}

	public static HandstoneProperty of(String name, Predicate<HandstoneEnum> filter) {
		return of(name, Arrays.stream(HandstoneEnum.values()).filter(filter).collect(Collectors.toList()));
	}

	public static HandstoneProperty of(String name, HandstoneEnum... values) {
		return of(name, Lists.newArrayList(values));
	}

	public static HandstoneProperty of(String name, Collection<HandstoneEnum> values) {
		return new HandstoneProperty(name, values);
	}
}
