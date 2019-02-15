package com.aranaira.arcanearchives.items.templates;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class ItemMultistateTemplate extends ItemTemplate
{
	protected Map<Integer, String> itemMap = new HashMap<>();

	public ItemMultistateTemplate(String name)
	{
		super(name);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
		{
			for(int metadata : itemMap.keySet())
			{
				items.add(new ItemStack(this, 1, metadata));
			}
		}
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}

	@Override
	public String getTranslationKey(ItemStack stack)
	{
		int i = stack.getMetadata();

		if(!itemMap.containsKey(i))
		{
			return "item.invalid";
		}

		return getTranslationKey() + "." + itemMap.get(i);
	}

	public ItemStack addItem(int number, String entry)
	{
		if(itemMap.containsKey(number))
		{
			return ItemStack.EMPTY;
		}
		itemMap.put(number, entry);

		return new ItemStack(this, 1, number);
	}

	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		for(Map.Entry<Integer, String> entry : itemMap.entrySet())
		{
			ModelLoader.setCustomModelResourceLocation(this, entry.getKey(), new ModelResourceLocation(getRegistryName(), "type=" + entry.getValue()));
		}
	}
}
