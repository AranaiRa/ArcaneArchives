package com.aranaira.arcanearchives.tileentities;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;

public interface IBrazierRouting {
	Int2IntOpenHashMap getOrCalculateReference ();

	BrazierRoutingType getRoutingType ();

	int totalEmptySlots ();
	int totalSlots ();
	int slotMultiplier ();

	ItemStack acceptStack (ItemStack stack);



	default boolean isVoidingTrove (ItemStack stack) {
		return false;
	}

	enum BrazierRoutingType {
		ANY,
		NO_NEW_STACKS,
		PRIORITY,
		GCT;

		static BrazierRoutingType fromInt (int index) {
			if (index == 0) return ANY;
			else if (index == 1) return NO_NEW_STACKS;
			else if (index == 2) return PRIORITY;
			else return GCT;
		}
	}
}
