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

	default boolean willAvoidStack (ItemStack stack) {
		return false;
	}

	default int troveScore (ItemStack stack) {
		return -1;
	}

	enum BrazierRoutingType {
		ANY,
		NO_NEW_STACKS,
		PRIORITY,
		TROVE,
		GCT;

		static BrazierRoutingType fromInt (int index) {
			int i = 0;
			for (BrazierRoutingType type : values()) {
				if (index == i) return type;
				i++;
			}
			return null;
		}
	}
}
