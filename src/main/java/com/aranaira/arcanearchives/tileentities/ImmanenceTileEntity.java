package com.aranaira.arcanearchives.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.UUID;

import com.aranaira.arcanearchives.init.BlockLibrary;

public class ImmanenceTileEntity extends TileEntity implements ITickable
{
	public UUID NetworkID; //UUID of network owner
	public int ImmanenceDrain; //Immanence cost to operate the device
	public int NetworkPriority; //What order the device's Immanence is paid for
	public boolean IsDrainPaid; //Whether the device's Immanence needs have been covered
	public boolean IsProtected; //Whether the device is currently indestructable
	
	public ImmanenceTileEntity(String name)
	{
		BlockLibrary.TILE_ENTITIES.put(name, this);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setUniqueId("playerId", NetworkID);
		
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NetworkID = compound.getUniqueId("playerId");
		
		super.readFromNBT(compound);
	}
}
