package com.aranaira.arcanearchives.items.gems.asscher;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CleansegleamItem extends ArcaneGemItem {
	public static final String NAME = "cleansegleam";

	public CleansegleamItem() {
		super(NAME, GemCut.ASSCHER, GemColor.BLUE, 30, 150);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.cleansegleam"));
	}

	@Override
	public boolean hasToggleMode () {
		//return true if Betweenlands is loaded
		return false;
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			if (GemUtil.getCharge(player.getHeldItemMainhand()) > 0) {
				int chargeCost = 0;
				if (player.isSneaking()) {
					ArcaneArchives.logger.info("player is sneaking");
					Vec3d start = new Vec3d(player.posX, player.posY+player.height, player.posZ);
					Vec3d dir = player.getLookVec();
					Vec3d rayTarget = new Vec3d(start.x + dir.x * 10, start.y + dir.y * 10, start.z + dir.z * 10);

					RayTraceResult ray = world.rayTraceBlocks(start, rayTarget, false, false, true);

					if(ray != null) {
						ArcaneArchives.logger.info("entity=null? " + (ray.entityHit == null));
						if(ray.entityHit instanceof EntityLivingBase) {
							ArcaneArchives.logger.info("is livingbase");
							chargeCost = removeEffects((EntityLivingBase) ray.entityHit, false);
						}
					}

				} else {
					chargeCost = removeEffects(player, false);
				}

				if (chargeCost > 0)
					GemUtil.consumeCharge(player.getHeldItemMainhand(), chargeCost);
			}
		}
	    return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	/**
	 * Removes effects from the target entity.
	 * @param target What entity to affect
	 * @param hasMatterUpgrade Whether the gem has a matter upgrade (and therefore removes all negative effects)
	 * @return Charge cost of the operation
	 */
	private int removeEffects(EntityLivingBase target, boolean hasMatterUpgrade) {
		ArrayList<Potion> toRemove = new ArrayList<>();
		int cost = 0;
		for (PotionEffect effect : target.getActivePotionEffects()) {
			if (effect.getEffectName() == MobEffects.HUNGER.getName())
				toRemove.add(effect.getPotion());
			else if (effect.getEffectName() == MobEffects.NAUSEA.getName())
				toRemove.add(effect.getPotion());
			else if (effect.getEffectName() == MobEffects.POISON.getName())
				toRemove.add(effect.getPotion());
			else if(hasMatterUpgrade) {
				if(effect.getPotion().isBadEffect()) {
					toRemove.add(effect.getPotion());
					cost = 3;
				}
			}
		}

		if(toRemove.size() > 0 && cost == 0) cost = 1;

		for(Potion remove : toRemove) {
			target.removePotionEffect(remove);
		}

		//If potion core is loaded, add Antidote/Purity on a cleanse

		return cost;
	}
}
