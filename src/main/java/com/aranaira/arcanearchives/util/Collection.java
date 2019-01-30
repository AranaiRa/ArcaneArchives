package com.aranaira.arcanearchives.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Collection
{
	public static <T, A extends List<T>> Collector<T, A, List<T>> toImmutableList (Supplier<A> supplier) {
		return Collector.of(supplier, List::add, (left, right) -> {
			left.addAll(right);
			return left;
		}, Collections::unmodifiableList);
	}

	public static <T> Collector<T, List<T>, List<T>> toImmutableList () {
		return Collector.of(ArrayList::new, List::add, (left, right) -> {
			left.addAll(right);
			return left;
		}, Collections::unmodifiableList);
	}
}
