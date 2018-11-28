package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.GUIBookContainer;
import com.aranaira.arcanearchives.common.NetworkContainer;

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

	public static final String NAME = "item_tomeofrequisition";
	
	public TomeOfRequisitionItem() {
		super(NAME);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.openGui(ArcaneArchives.instance(), 0, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		player.openGui(ArcaneArchives.instance(), 0, worldIn, (int)player.posX, (int)player.posY, (int)player.posZ);
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}
}
