package com.aranaira.arcanearchives.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aranaira.arcanearchives.blocks.*;
import com.aranaira.arcanearchives.tileentities.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

public class BlockLibrary {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	public static final Map<String, TileEntity> TILE_ENTITIES = new HashMap<>();
	
	//Matrices
	public static final Block MATRIX_CRYSTAL_CORE = new MatrixCrystalCore();
	public static final Block MATRIX_REPOSITORY = new MatrixRepository();
	public static final Block MATRIX_RESERVOIR = new MatrixReservoir();
	public static final Block MATRIX_STORAGE = new MatrixStorage();
	public static final Block MATRIX_DISTILLATE = new MatrixDistillate(); //TODO: Check if Thaumcraft is loaded 
	
	//Blocks
	public static final Block STORAGE_RAW_QUARTZ = new StorageRawQuartz();
	public static final Block STORAGE_CUT_QUARTZ = new StorageCutQuartz();
	public static final Block RADIANT_RESONATOR = new RadiantResonator();
	public static final Block RAW_QUARTZ = new RawQuartz();
	public static final Block DOMINION_CRYSTAL = new DominionCrystal();
	public static final Block GEMCUTTERS_TABLE = new GemcuttersTable();
	//Fix for dynamic material base on block its extending.
	public static final Block ACCESSOR = new AccessorBlock(Material.GLASS);
	
	//Tile Entities
	public static final TileEntity RADIANT_RESONATOR_TILE_ENTITY = new RadiantResonatorTileEntity();
	public static final TileEntity MATRIX_CORE_TILE_ENTITY = new MatrixCoreTileEntity();
	public static final TileEntity MATRIX_REPOSITORY_TILE_ENTITY = new MatrixRepositoryTileEntity();
}
