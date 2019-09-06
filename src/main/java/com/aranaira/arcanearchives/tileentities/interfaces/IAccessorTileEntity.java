package com.aranaira.arcanearchives.tileentities.interfaces;

import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;

public interface IAccessorTileEntity {
	/**
	 * This function is called when individual accessor blocks are broken by the
	 * AccessorTileEntity.
	 */
	void breakBlock ();

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
	void breakBlock (@Nullable IBlockState state, boolean harvest);
}
