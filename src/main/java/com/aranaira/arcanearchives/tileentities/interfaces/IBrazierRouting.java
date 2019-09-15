package com.aranaira.arcanearchives.tileentities.interfaces;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public interface IBrazierRouting {
	Int2IntOpenHashMap getOrCalculateReference ();

	BrazierRoutingType getRoutingType ();

	UUID getUuid ();

	boolean isTileInvalid ();

	int countEmptySlots ();

	int totalEmptySlots ();

	int totalSlots ();

	int slotMultiplier ();

	ItemStack acceptStack (ItemStack stack, boolean simulate);

	default boolean willAvoidStack (ItemStack stack) {
		return false;
	}

	default int troveScore (ItemStack stack) {
		return -1;
	}

	enum BrazierRoutingType {
		ANY, NO_NEW_STACKS, PRIORITY, TROVE, GCT;

		public static BrazierRoutingType fromInt (int index) {
			int i = 0;
			for (BrazierRoutingType type : values()) {
				if (index == i) {
					return type;
				}
				i++;
			}
			return null;
		}
	}
}
