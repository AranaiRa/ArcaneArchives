package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class MatrixDistillate extends BlockTemplate
{

	public static final String name = "matrix_distillate";

	public MatrixDistillate()
	{
		super(name, Material.GLASS);
		setLightLevel(16 / 16f);
		setSize(1, 3, 1);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("§c§lUNIMPLEMENTED§r");
		tooltip.add("§c§oUsing this item may crash your game!§r");
	}

	@Override
	public boolean hasOBJModel()
	{
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

}
