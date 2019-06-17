package com.aranaira.arcanearchives.capabilities.tracking;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityItemTracking {
	@CapabilityInject(IItemTracking.class)
	public static Capability<IItemTracking> ITEM_TRACKING_CAPABILITY = null;

	public static void register () {
		CapabilityManager.INSTANCE.register(IItemTracking.class, new Capability.IStorage<IItemTracking>()
		{
			@Nullable
			@Override
			public NBTBase writeNBT (Capability<IItemTracking> capability, IItemTracking instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT (Capability<IItemTracking> capability, IItemTracking instance, EnumFacing side, NBTBase nbt) {
			}
		}, ItemTrackingChest::new);
	}
}
