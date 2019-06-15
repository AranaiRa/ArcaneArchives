package com.aranaira.arcanearchives.items.gems.asscher;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketArcaneGem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class Slaughtergleam extends ArcaneGemItem {
	public static final String NAME = "slaughtergleam";

	public Slaughtergleam() {
		super(NAME, GemCut.ASSCHER, GemColor.RED, 30, 150);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.slaughtergleam"));
	}

	@Override
	public boolean hasToggleMode () {
		return true;
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			ItemStack gem = player.getHeldItemMainhand();
			if (GemUtil.getCharge(gem) == 0) {
				for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
					ItemStack stack = player.inventory.mainInventory.get(i);
					if (stack.getItem() == Items.GOLD_NUGGET) {
						GemUtil.restoreCharge(gem, -1);
						stack.shrink(3);
						//TODO: Play a particle effect
						Vec3d pos = player.getPositionVector().add(0, 1, 0);
						PacketArcaneGem packet = new PacketArcaneGem(cut, color, pos, pos);
						NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(player.dimension, pos.x, pos.y, pos.z, 160);
						NetworkHandler.CHANNEL.sendToAllAround(packet, tp);
						break;
					} else {
						continue;
					}
				}
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
