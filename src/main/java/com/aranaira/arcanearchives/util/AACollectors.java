package com.aranaira.arcanearchives.util;

import java.util.*;
import java.util.stream.Collector;

public class AACollectors {
	static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

	public static <T> Collector<T, List<T>, List<T>> toImmutableList () {
		return Collector.of(ArrayList::new, List::add, (left, right) -> {
			left.addAll(right);
			return left;
		}, Collections::unmodifiableList);
	}
}
