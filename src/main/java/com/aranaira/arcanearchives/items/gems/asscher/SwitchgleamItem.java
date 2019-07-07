package com.aranaira.arcanearchives.items.gems.asscher;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.util.DetectorUtil;
import hellfirepvp.astralsorcery.common.util.RaytraceAssist;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class SwitchgleamItem extends ArcaneGemItem {
	public static final String NAME = "switchgleam";

	public SwitchgleamItem () {
		super(NAME, GemCut.ASSCHER, GemColor.PURPLE, 30, 150);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.switchgleam"));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.recharge.switchgleam"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			Vec3d pos = blockPosToVector(player.getPosition(), false).add(0, player.getEyeHeight(), 0);
			Vec3d look = player.getLook(0).scale(40);
			ArrayList<RayTraceResult> rays = new ArrayList<>();
			DetectorUtil.raytraceAll(rays, world, player, pos, pos.add(look));

			for(RayTraceResult ray : rays) {
				if (ray != null) {
					if(ray.entityHit == null) continue;
					else if(ray.entityHit instanceof EntityLivingBase) {
						ArcaneArchives.logger.info("pitch:"+player.rotationPitch+ "    yaw:"+player.rotationYaw);
						EntityLivingBase target = (EntityLivingBase) ray.entityHit;
						Vec3d tPos = new Vec3d(target.posX, target.posY, target.posZ);
						Vec3d pPos = new Vec3d(player.posX, player.posY, player.posZ);
						target.setPositionAndRotation(pPos.x, pPos.y, pPos.z, target.rotationYaw+(float)Math.PI, target.rotationPitch);
						player.setPositionAndRotation(tPos.x, tPos.y, tPos.z, player.rotationYaw+(float)Math.PI, player.rotationPitch);
					}
					break;
				}
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
