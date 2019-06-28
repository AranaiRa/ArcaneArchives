package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ScepterManipulationItem extends ItemTemplate {
	public static final String NAME = "scepter_manipulation";

	public ScepterManipulationItem () {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.scepter_manipulation"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public EnumActionResult onItemUseFirst (EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		ImmanenceTileEntity te = WorldUtil.getTileEntity(ImmanenceTileEntity.class, world, pos);
		if (te != null) {
			player.swingArm(hand);

			if (!world.isRemote) {
				if (te.handleManipulationInterface(player, hand, side, hitX, hitY, hitZ)) {
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}
}
