package com.aranaira.arcanearchives.init;

import java.util.ArrayList;
import java.util.List;

import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.blocks.MatrixCrystalCore;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.blocks.RawQuartz;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockLibrary {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	//Matrices
	public static final Block MATRIX_CRYSTAL_CORE = new MatrixCrystalCore("matrix_crystal_core");
	
	//Blocks
	public static final Block RADIANT_RESONATOR = new RadiantResonator("radiant_resonator");
	public static final Block RAW_QUARTZ = new RawQuartz("raw_quartz");
}
