package com.aranaira.arcanearchives.tileentities;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.util.Placeable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class AATileEntity extends TileEntity {
    public Placeable.Size size;

    public void setSize (Placeable.Size newSize)
	{
		this.size = newSize;
	}

	public Placeable.Size getSize ()
	{
		return this.size;
	}

	public boolean hasAccessors ()
	{
		return this.size.hasAccessors();
	}

	public EnumFacing getFacing (World world)
	{
		IBlockState state = world.getBlockState(getPos());
		return state.getValue(BlockTemplate.FACING);
	}
}
