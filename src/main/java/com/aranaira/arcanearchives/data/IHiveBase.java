package com.aranaira.arcanearchives.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public interface IHiveBase {
	@Nullable
	ServerNetwork getOwnerNetwork ();

	@Nullable
	HiveNetwork getHiveNetwork ();

	boolean isHiveMember ();

	boolean isHiveNetwork ();

	@Nullable
	List<ServerNetwork> getContainedNetworks ();

	NBTTagCompound buildHiveManifest (EntityPlayer player);
}
