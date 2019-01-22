package com.aranaira.arcanearchives.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ScepterManipulationItem extends ItemTemplate
{
	public static final String NAME = "item_sceptermanipulation";

	public ScepterManipulationItem()
	{
		super(NAME);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("�c�lUNIMPLEMENTED�r");
		tooltip.add("�c�oUsing this item may crash your game!�r");
	}
}
