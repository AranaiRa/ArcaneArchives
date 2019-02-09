package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.util.types.Size;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@MethodsReturnNonnullByDefault // TODO: Make sure this extending from AATileEntity doesn't cause ongoing issues
public class AccessorTileEntity extends AATileEntity
{
	private boolean breaking = false;
	private BlockPos parent = BlockPos.ORIGIN;

	public AccessorTileEntity()
	{
		super();
		setName("accessor");
		setSize(new Size(1, 1, 1));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if(parent != BlockPos.ORIGIN) compound.setLong("pos", parent.toLong());
		return super.writeToNBT(compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("pos")) parent = BlockPos.fromLong(compound.getLong("pos"));
		super.readFromNBT(compound);
	}

	public boolean onBlockActivated(BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		BlockPos par = getParent();
		IBlockState parState = world.getBlockState(par);
		Block parBlock = parState.getBlock();

		if(!(parBlock instanceof BlockTemplate))
		{
			return false;
		}

		return parBlock.onBlockActivated(world, getParent(), world.getBlockState(getParent()), playerIn, hand, facing, hitX, hitY, hitZ);
	}

	public BlockPos getParent()
	{
		return parent;
	}

	public void setParent(BlockPos pos)
	{
		this.parent = pos;
	}

	@Nullable
	public AATileEntity getParentTile()
	{
		TileEntity te = world.getTileEntity(getParent());
		if(te instanceof AATileEntity) return (AATileEntity) te;
		return null;
	}

	@Nullable
	public BlockTemplate getParentBlock()
	{
		Block block = world.getBlockState(getParent()).getBlock();
		if(block instanceof BlockTemplate) return (BlockTemplate) block;
		return null;
	}

	@Override
	public void breakBlock()
	{
		if(breaking) return;

		breaking = true;

		AATileEntity tile = getParentTile();

		if(tile != null) tile.breakBlock();
	}

	@Override
	public void breakBlock(@Nullable IBlockState state, boolean harvest)
	{
		super.breakBlock(state, harvest);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		AATileEntity parent = getParentTile();
		if(parent != null) return parent.hasCapability(capability, facing);
		return false;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		AATileEntity parent = getParentTile();
		if(parent != null) return parent.getCapability(capability, facing);
		return null;
	}
}
