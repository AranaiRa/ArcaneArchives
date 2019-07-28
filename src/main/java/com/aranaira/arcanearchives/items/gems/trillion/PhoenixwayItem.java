package com.aranaira.arcanearchives.items.gems.trillion;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.GemUtil.AvailableGemsHandler;
import com.aranaira.arcanearchives.items.gems.GemUtil.GemStack;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketArcaneGems.GemParticle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class PhoenixwayItem extends ArcaneGemItem {
	public static final String NAME = "phoenixway";

	public PhoenixwayItem () {
		super(NAME, GemCut.TRILLION, GemColor.ORANGE, 75, 300);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.phoenixway"));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.recharge.phoenixway"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			AvailableGemsHandler handler = GemUtil.getHeldGem(player, hand);
			GemStack gem = handler.getHeld();
			if (!recharge(world, player, gem)) {
				if (GemUtil.getCharge(handler.getHeld()) > 0) {
					Vec3d start = new Vec3d(player.posX, player.posY + player.height, player.posZ);
					Vec3d dir = player.getLookVec();
					Vec3d rayTarget = new Vec3d(start.x + dir.x * 40, start.y + dir.y * 40, start.z + dir.z * 40);

					RayTraceResult ray = world.rayTraceBlocks(start, rayTarget, false, true, false);

					if (ray != null) {
						BlockPos pos = ray.getBlockPos();
						EnumFacing facing = ray.sideHit;

						if (world.isAirBlock(pos.offset(facing))) {
							world.setBlockState(pos.offset(facing), Blocks.FIRE.getDefaultState());
							GemUtil.consumeCharge(handler.getHeld(), 1);

							Vec3d end = new Vec3d(pos.offset(facing).getX(), pos.offset(facing).getY(), pos.offset(facing).getZ());

							GemParticle packet = new GemParticle(cut, color, start, end);
							Networking.sendToAllTracking(packet, player);
						}
					}
				}
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public boolean recharge (World world, EntityPlayer player, GemStack gem) {
		if (GemUtil.getCharge(gem) == 0) {
			for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
				ItemStack stack = player.inventory.mainInventory.get(i);
				if (stack.getItem() == Items.GUNPOWDER) {
					int numConsumed = 3;
					if (numConsumed > stack.getCount()) {
						numConsumed = stack.getCount();
					}
					GemUtil.restoreCharge(gem, numConsumed * 25);
					stack.shrink(numConsumed);
					//TODO: Play a particle effect
					Vec3d pos = player.getPositionVector().add(0, 1, 0);
					GemParticle packet = new GemParticle(cut, color, pos, pos);
					Networking.sendToAllTracking(packet, player);
					return true;
				}
			}
		}

		return false;
	}
}
