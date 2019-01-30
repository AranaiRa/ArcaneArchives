package com.aranaira.arcanearchives.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MatrixDistillate extends BlockDirectionalTemplate
{
	public static final String name = "matrix_distillate";

	public MatrixDistillate()
	{
		super(name, Material.GLASS);
		setLightLevel(16 / 16f);
		setSize(3, 3, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(TextFormatting.RED+""+TextFormatting.BOLD+I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED+""+TextFormatting.ITALIC+I18n.format("arcanearchives.tooltip.notimplemented2"));
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
