package com.aranaira.arcanearchives.blocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import net.minecraft.block.material.Material;

public class StorageCutQuartz extends BlockTemplate
{

	public static final String name = "storage_cut_quartz";

	public StorageCutQuartz()
	{
		super(name, Material.ROCK);
		setLightLevel(16 / 16f);
		setHardness(1.7f);
		setHarvestLevel("pickaxe", 0);
	}
}
