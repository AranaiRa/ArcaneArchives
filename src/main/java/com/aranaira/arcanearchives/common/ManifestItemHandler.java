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
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManifestItemHandler implements IItemHandlerModifiable
{
	private ArcaneArchivesClientNetwork network;
	private ManifestList.ManifestListIterable currentIterator;
	private ManifestList manifest = null;

	public ManifestItemHandler(ArcaneArchivesClientNetwork network, ManifestList manifest)
	{
		this.manifest = manifest;
		this.network = network;
	}

	public ManifestItemHandler(ArcaneArchivesClientNetwork network) {
		this.network = network;
		this.manifest = network.getManifestItems();
	}

	@Override
	public int getSlots()
	{
		return 81;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return getCurrentIterator().translate(slot).getStack();
	}

	public ManifestEntry getManifestEntryInSlot (int slot)
	{
		return getCurrentIterator().translate(slot);
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

	public ManifestList.ManifestListIterable getCurrentIterator () {
		if (currentIterator == null) {
			if (manifest != null)
			{
				currentIterator = manifest.iterable();
			} else
			{
				return null;
			}
		}

		return currentIterator;
	}

	public void setSearchText(String s)
	{
		currentIterator = manifest.setSearchText(s);
	}

	public void Clear()
	{
		currentIterator = manifest.setSearchText(null);
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
	}
}
