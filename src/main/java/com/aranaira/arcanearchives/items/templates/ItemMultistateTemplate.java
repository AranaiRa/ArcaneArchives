package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Map;

//Referenced and edited from COFHCore's ItemMulti
public class ItemMultistateTemplate extends ItemTemplate
{
	protected ArrayList<Integer> itemList = new ArrayList<>();
	protected Map<Integer, String> itemMap = new Int2ObjectOpenHashMap<>();

	public ItemMultistateTemplate(String NAME)
	{
		super(NAME);
		//setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
		{
			for(int metadata : itemList)
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

	public boolean preInit()
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

	//Functions cribbed from CoFH Core
	public ItemStack addItem(int number, String entry)
	{
		if(itemMap.containsKey(number))
		{
			return ItemStack.EMPTY;
		}
		itemMap.put(number, entry);
		itemList.add(number);

		return new ItemStack(this, 1, number);
	}

	public ImmutableList<ItemStack> getAllItems()
	{
		ArrayList<ItemStack> items = new ArrayList<>();

		for(int metadata : itemList)
		{
			items.add(new ItemStack(this, 1, metadata));
		}

		return ImmutableList.copyOf(items);
	}

	@SideOnly(Side.CLIENT)
	public void registerModels()
	{

		for(Map.Entry<Integer, String> entry : itemMap.entrySet())
		{
			ArcaneArchives.logger.info("Setting custom resource location for " + getRegistryName() + " variant \"" + entry.getValue() + "\"");
			ModelLoader.setCustomModelResourceLocation(this, entry.getKey(), new ModelResourceLocation(getRegistryName(), "type=" + entry.getValue()));
		}
	}
}
