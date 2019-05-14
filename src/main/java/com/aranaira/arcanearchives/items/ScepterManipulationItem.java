package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ScepterManipulationItem extends ItemTemplate {
	public static final String NAME = "item_sceptermanipulation";

	public ScepterManipulationItem () {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.scepterofmanipulation"));
	}

	@Override
	public EnumActionResult onItemUse (EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND || !player.isSneaking() || world.isAirBlock(pos)) {
			return EnumActionResult.SUCCESS;
		}

		player.swingArm(hand);

		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		}

		Style def = new Style().setColor(TextFormatting.GOLD);
		Style error = new Style().setColor(TextFormatting.DARK_RED).setBold(true);

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (block == BlockRegistry.RADIANT_TROVE) {
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
			if (te == null || te.getInventory() == null) {
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.data.scepter.tile.invalid").setStyle(error), true);
				return EnumActionResult.SUCCESS;
			}

			TroveItemHandler handler = te.getInventory();

			int upgrades = handler.getUpgrades();
			if (upgrades == 0) {
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.data.scepter.tile.no_upgrades").setStyle(error), true);
				return EnumActionResult.SUCCESS;
			}

			if (handler.downgrade()) {
				ItemStack upgrade = new ItemStack(TroveItemHandler.UPGRADE_ITEM);
				Block.spawnAsEntity(world, player.getPosition(), upgrade);
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.data.scepter.tile.downgrade_success.trove", handler.getUpgrades(), TroveItemHandler.MAX_UPGRADES).setStyle(def), true);
				return EnumActionResult.SUCCESS;
			} else {
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.data.scepter.tile.cant_downgrade.trove").setStyle(error), true);
				return EnumActionResult.SUCCESS;
			}

		} else if (block == BlockRegistry.RADIANT_TANK) {

		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	@SuppressWarnings("deprecation")
	public EnumRarity getRarity (ItemStack stack) {
		return EnumRarity.RARE;
	}
}
