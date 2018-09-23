package com.aranaira.arcanearchives.tileentities;

import net.minecraft.tileentity.TileEntity;
import java.util.UUID;

public class ImmanenceTileEntity extends TileEntity 
{
	public UUID NetworkID; //UUID of network owner
	public int ImmanenceDrain; //Immanence cost to operate the device
	public int NetworkPriority; //What order the device's Immanence is paid for
	public boolean IsDrainPaid; //Whether the device's Immanence needs have been covered
	public boolean IsProtected; //Whether the device is currently indestructable
}
