package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.GUIBook;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class TomeOfRequistion extends ItemTemplate {

	public TomeOfRequistion(String name) {
		super(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		//!?!?!?! WHAT IS THE MOD OBJECT ITS TALKING ABOUT
		//Minecraft.getMinecraft().player.openGui(ArcaneArchives.instance(), 0, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		Minecraft.getMinecraft().displayGuiScreen(new GUIBook());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
