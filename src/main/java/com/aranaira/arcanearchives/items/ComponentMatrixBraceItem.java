package com.aranaira.arcanearchives.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ComponentMatrixBraceItem extends ItemTemplate
{
	public static final String NAME = "item_component_matrixbrace";

	public ComponentMatrixBraceItem()
	{
		super(NAME);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		//TODO: Add real tooltip
	}
}
