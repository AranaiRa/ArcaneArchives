package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import net.minecraft.block.material.Material;

public class StorageRawQuartz extends BlockTemplate
{

	public static final String name = "storage_raw_quartz";

	public StorageRawQuartz()
	{
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setHarvestLevel("pickaxe", 0);
	}
}
