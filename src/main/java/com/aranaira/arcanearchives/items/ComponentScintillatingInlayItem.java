package com.aranaira.arcanearchives.items;

import java.util.List;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ComponentScintillatingInlayItem extends ItemTemplate
{
	public static final String NAME = "item_component_scintillatinginlay";
	
	public ComponentScintillatingInlayItem()
	{
		super(NAME);
	}

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
    	//TODO: Add real tooltip
    }
    
}
