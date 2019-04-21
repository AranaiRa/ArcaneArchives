package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.util.types.Size;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

/**
 * The base Arcane Archives TileEntity.
 * <p>
 * The majority of functionality for actual machines is implemented in `ImmanenceTileEntity`.
 * The only direct descendents of this that are not ITEs are the Gem Cutter's Table (as it
 * does not use immanence), and the Accessor Block.
 */
public class AATileEntity extends TileEntity
{
	public String name;
	public Size size;

	/**
	 * This boolean value is used to ensure that cascading break events do not
	 * result in an infinite loop. Once the process has started, all future function
	 * calls to breakBlock are discarded.
	 */
	private boolean breaking = false;

	/**
	 * @return The current size associated with this tile entity. See `setSize`.
	 */
	public Size getSize()
	{
		return this.size;
	}

	/**
	 * At this point in time, size objects, while stored in the TileEntity, are more impactful
	 * on the actual instances of BlockTemplate than on the TileEntity. They are stored here for
	 * posterity's sake, but the duplication could probably be discarded.
	 *
	 * @param newSize A Size object containing the width, height and length of the multi-
	 *                block structure associated with this block. This size is used in order
	 *                to calculate accessor block positions, and to determine if the block
	 *                can actually be placed.
	 */
	public void setSize(Size newSize)
	{
		this.size = newSize;
	}

	/**
	 * @return The name associated with this TileEntity, which is the path part of the
	 * name that it was registered with. Note that this simply returns that part and does
	 * not include the modid.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to be used as the registry name path and also to refer
	 *             to the specific type of tile entity.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return The current facing of the block associated with this TileEntity.
	 * Theoretically there are issues with the presumption that all BlockTemplates
	 * are also BlockDirectionalTemplates; few are.
	 * <p>
	 * The default value for this function is EnumFacing.WEST; this may change in
	 * the future.
	 */
	public EnumFacing getFacing()
	{
		IBlockState state = world.getBlockState(getPos());
		if(state.getBlock() instanceof BlockTemplate)
		{
			return ((BlockTemplate) state.getBlock()).getFacing(world, pos);
		}

		return EnumFacing.WEST;
	}


	/**
	 * This function is called when individual accessor blocks are broken by the
	 * AccessorTileEntity.
	 */
	public void breakBlock()
	{
		breakBlock(null, true);
	}

	/**
	 * This is a direct link to Block::breakBlock and is called by both the
	 * core block of a pseudo-multiblock-structure and by each of its individual
	 * accessor blocks.
	 * <p>
	 * It specifically does the work of removing tile entities and blocks based
	 * on the position of the TileEntity and the facing of the block associated
	 * with it (if it has one).
	 *
	 * @param state   The current state of the block being destroyed. If null, it
	 *                will find the current state of the block at this location.
	 * @param harvest Setting this to false will result in the center block
	 *                not being destroyed (if this was trigger from the break of
	 *                one of the accessors).
	 */
	public void breakBlock(@Nullable IBlockState state, boolean harvest)
	{
		if(breaking) return;

		breaking = true;

		Block block = (state == null) ? world.getBlockState(getPos()).getBlock() : state.getBlock();
		EnumFacing facing = null;

		if(block instanceof BlockDirectionalTemplate && state != null)
		{
			facing = state.getValue(BlockDirectionalTemplate.FACING);
		}
		if(block instanceof BlockTemplate)
		{
			for(BlockPos point : ((BlockTemplate) block).calculateAccessors(world, getPos(), facing))
			{
				world.removeTileEntity(point);
				world.setBlockState(point, Blocks.AIR.getDefaultState());
			}

			// Is this enough to properly break the center?
			world.destroyBlock(getPos(), harvest);
		}
	}

	/**
	 * Future tile entities are going to require a "start up" time. This exists as a way
	 * of determining if those tile entities are ready or not. At the minute, it always
	 * returns true.
	 *
	 * @return Returns true by default.
	 */
	public boolean isActive()
	{
		return true;
	}

	public void defaultServerSideUpdate()
	{
		if(world == null || world.isRemote) return;

		IBlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, state, 8);
		this.markDirty();
	}

	public static class Tags
	{
		public static final String INVENTORY = "inventory";
	}
}
