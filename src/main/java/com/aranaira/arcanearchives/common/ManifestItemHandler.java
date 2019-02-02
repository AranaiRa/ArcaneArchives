package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesClientNetwork;
import com.aranaira.arcanearchives.util.types.ManifestList;
import com.aranaira.arcanearchives.util.types.Turple;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManifestItemHandler implements IItemHandlerModifiable
{
	private ArcaneArchivesClientNetwork network;
	private ManifestList manifestBase = null;
	private ManifestList manifestActive = null;

	public ManifestItemHandler(ArcaneArchivesClientNetwork network, ManifestList manifest)
	{
		this.manifestBase = manifest;
		this.network = network;
	}

	public ManifestItemHandler(ArcaneArchivesClientNetwork network) {
		this.network = network;
		this.manifestBase = network.getManifestItems();
	}

	@Override
	public int getSlots()
	{
		return 81;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (manifestActive == null) manifestActive = manifestBase.filtered();
		return manifestActive.getItemStackForSlot(slot);
	}

	@Nullable
	public ManifestEntry getManifestEntryInSlot (int slot)
	{
		return manifestActive.getEntryForSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
	}

	public void setSearchText(String s)
	{
		manifestBase.setSearchText(s);
		manifestActive = manifestBase.filtered();
	}

	public void clear()
	{
		manifestBase.setSearchText(null);
		manifestActive = manifestBase.filtered();
	}

	public static class ManifestEntry extends Turple<ItemStack, Integer, List<BlockPos>> {

		public ManifestEntry(@Nonnull ItemStack val1, @Nonnull Integer val2, @Nonnull List<BlockPos> val3)
		{
			super(val1, val2, val3);
		}

		public ItemStack getStack () {
			return val1;
		}

		public int getDimension () {
			return val2;
		}

		public List<BlockPos> getPositions () {
			return val3;
		}

		public List<Vec3d> getVecPositions () {
			return getPositions().stream().map((pos) -> new Vec3d(pos.getX(), pos.getY(), pos.getZ())).collect(Collectors.toList());
		}
	}
}
