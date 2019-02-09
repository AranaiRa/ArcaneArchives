package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ComponentMaterialInterfaceItem extends ItemTemplate
{
	public static final String NAME = "item_component_materialinterface";

	public ComponentMaterialInterfaceItem()
	{
		super(NAME);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		//TODO: Add real tooltip
	}
}
