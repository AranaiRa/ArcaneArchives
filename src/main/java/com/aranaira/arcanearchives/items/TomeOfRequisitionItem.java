package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.GUIBook;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TomeOfRequisitionItem extends ItemTemplate {

	public TomeOfRequisitionItem(String name) {
		super(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		//!?!?!?! WHAT IS THE MOD OBJECT ITS TALKING ABOUT
		//Minecraft.getMinecraft().player.openGui(ArcaneArchives.instance(), 0, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		Minecraft.getMinecraft().displayGuiScreen(new GUIBook());
		ArcaneArchives.logger.info("TEST");
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		//Minecraft.getMinecraft().displayGuiScreen(new GUIBook());
		ArcaneArchives.logger.info("TEST");
		return EnumActionResult.SUCCESS;
		//player.display
		//return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		// TODO Auto-generated method stub
		ArcaneArchives.logger.info("TEST");
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}
}
