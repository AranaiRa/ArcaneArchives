package com.aranaira.arcanearchives.init;

import java.util.ArrayList;
import java.util.List;

import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.blocks.RadiantResonator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockLibrary {

	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block RADIANT_RESONATOR = new RadiantResonator("radiant_resonator");
}
