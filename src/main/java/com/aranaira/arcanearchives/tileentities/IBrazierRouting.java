package com.aranaira.arcanearchives.tileentities;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;

public interface IBrazierRouting {
	Int2IntOpenHashMap getOrCalculateReference ();

	BrazierRoutingType getRoutingType ();

	default boolean isVoidingTrove (ItemStack stack) {
		return false;
	}

	enum BrazierRoutingType {
		ANY,
		NO_NEW_STACKS
	}
}
