package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.items.templates.IItemScepter;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ScepterManipulationItem extends ItemTemplate implements IItemScepter {
	public static final String NAME = "scepter_manipulation";

	public ScepterManipulationItem () {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.scepter_manipulation"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, PlayerEntity player) {
		return true;
	}

	@Override
	public ActionResultType onItemUseFirst (PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
		ImmanenceTileEntity te = WorldUtil.getTileEntity(ImmanenceTileEntity.class, world, pos);
		if (te != null) {
			player.swingArm(hand);

			if (!world.isRemote) {
				if (te.handleManipulationInterface(player, hand, side, hitX, hitY, hitZ)) {
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.PASS;
	}
}
