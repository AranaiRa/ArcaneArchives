package com.aranaira.arcanearchives.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class RadiantCraftingTableTileEntity extends AATileEntity
{
	private ItemStackHandler persistentMatrix = new ItemStackHandler(9);

	public RadiantCraftingTableTileEntity()
	{
		super();
		setName("radiantcraftingtable");
	}

	public ItemStackHandler getInventory()
	{
		return persistentMatrix;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if (compound.hasKey(Tags.INVENTORY))
		{
			persistentMatrix.deserializeNBT(compound.getCompoundTag(Tags.INVENTORY));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);

		compound.setTag(Tags.INVENTORY, persistentMatrix.serializeNBT());

		return compound;
	}
}
