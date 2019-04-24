package com.aranaira.arcanearchives.inventory.unused;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkCraftingContainer extends ContainerWorkbench
{

	public NetworkCraftingContainer(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
		super(playerInventory, worldIn, posIn);
	}
}
