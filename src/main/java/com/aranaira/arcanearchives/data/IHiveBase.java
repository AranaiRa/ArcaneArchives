package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.util.types.ITileList;
import com.aranaira.arcanearchives.util.types.IteRef;
import com.aranaira.arcanearchives.util.types.TileList.TileListIterable;
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

	Iterable<IteRef> getValidTiles ();

	@Nullable
	List<ServerNetwork> getContainedNetworks ();

	NBTTagCompound buildHiveManifest (EntityPlayer player);
}
