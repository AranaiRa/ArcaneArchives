package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.util.types.UpgradeType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class DevouringCharmItem extends ItemTemplate implements IUpgradeItem {
	public static final String NAME = "devouring_charm";

	public DevouringCharmItem () {
		super(NAME);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.devouring_charm"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, @Nonnull EnumHand hand) {
		if (world.isRemote) {
			return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		player.openGui(ArcaneArchives.instance, AAGuiHandler.DEVOURING_CHARM, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public UpgradeType getUpgradeType (ItemStack stack) {
		return UpgradeType.VOID;
	}

	@Override
	public int getUpgradeSize (ItemStack stack) {
		return -1;
	}

	public static List<Class<?>> UPGRADE_FOR = Arrays.asList(RadiantTankTileEntity.class, RadiantTroveTileEntity.class);

	@Override
	public List<Class<?>> upgradeFor () {
		return UPGRADE_FOR;
	}
}
