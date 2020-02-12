/*package com.aranaira.arcanearchives.tileentities.interfaces;

public interface ISizedTileEntity {
	*//**
 * @return The current size associated with this tile entity. See `setSize`.
 * <p>
 * At this point in time, size objects, while stored in the TileEntity, are more impactful
 * on the actual instances of BlockTemplate than on the TileEntity. They are stored here for
 * posterity's sake, but the duplication could probably be discarded.
 * @param newSize A Size object containing the width, height and length of the multi-
 * block structure associated with this block. This size is used in order
 * to calculate accessor block positions, and to determine if the block
 * can actually be placed.
 *//*
	MultiblockSize getSize ();

	*//**
 * At this point in time, size objects, while stored in the TileEntity, are more impactful
 * on the actual instances of BlockTemplate than on the TileEntity. They are stored here for
 * posterity's sake, but the duplication could probably be discarded.
 *
 * @param newSize A Size object containing the width, height and length of the multi-
 *                block structure associated with this block. This size is used in order
 *                to calculate accessor block positions, and to determine if the block
 *                can actually be placed.
 *//*
	void setSize (MultiblockSize newSize);
}*/
