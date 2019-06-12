package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import net.minecraft.block.material.Material;

public class StorageShapedQuartz extends BlockTemplate {

	public static final String name = "storage_shaped_quartz";

	public StorageShapedQuartz () {
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setHarvestLevel("pickaxe", 0);
	}
}
