package com.aranaira.arcanearchives.items.gems.asscher;


import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.GemUtil.AvailableGemsHandler;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public class CleansegleamItem extends ArcaneGemItem {
	public static final String NAME = "cleansegleam";

	public CleansegleamItem () {
		super(NAME, GemCut.ASSCHER, GemColor.BLUE, 30, 150);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.cleansegleam"));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.recharge.cleansegleam"));
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
		if (!world.isRemote) {
			AvailableGemsHandler handler = GemUtil.getHeldGem(player, hand);
			if (handler.getHeld() != null) {
				if(GemUtil.getCharge(handler.getHeld()) == 0) {
					IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
					for (int i = 0; i < playerInventory.getSlots(); i++) {
						ItemStack bucket = ItemStack.EMPTY;
						ItemStack stackInSlot = playerInventory.getStackInSlot(i);
						if (stackInSlot.getItem() == Items.MILK_BUCKET) {
							playerInventory.extractItem(i, 1, false);
							if (bucket.isEmpty()) {
								bucket = new ItemStack(Items.BUCKET);
							} else {
								bucket.setCount(bucket.getCount()+1);
							}
						}
						ItemStack result = ItemHandlerHelper.insertItemStacked(playerInventory, bucket, false);
						if (!result.isEmpty()) {
							Block.spawnAsEntity(world, player.getPosition(), result);
						}
					}
					//consumeFluidForChargeRecovery(player, handler, FluidRegistry.getFluid("milk"), 1, GemUtil.getMaxCharge(handler.getHeld()));
					//TODO: Check for amphora linked to milk
				}
				else {
					int chargeCost = removeEffects(player, false);
					if (player.isSneaking()) {
						double boxRadius = 3.5;
						AxisAlignedBB aabb = new AxisAlignedBB(player.posX - boxRadius, player.posY - boxRadius, player.posZ - boxRadius, player.posX + boxRadius, player.posY + boxRadius, player.posZ + boxRadius);
						for (EntityLivingBase entity : player.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb)) {
							chargeCost += removeEffects(entity, false);
						}
					}

					if (chargeCost > 0) {
						GemUtil.consumeCharge(handler.getHeld(), chargeCost);
					}
				}
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	/**
	 * Removes effects from the target entity.
	 *
	 * @param target           What entity to affect
	 * @param hasMatterUpgrade Whether the gem has a matter upgrade (and therefore removes all negative effects)
	 * @return Charge cost of the operation
	 */
	private int removeEffects (EntityLivingBase target, boolean hasMatterUpgrade) {
		ArrayList<Potion> toRemove = new ArrayList<>();
		int cost = 0;
		for (PotionEffect effect : target.getActivePotionEffects()) {
			if (effect.getEffectName().equals(MobEffects.HUNGER.getName())) {
				toRemove.add(effect.getPotion());
			} else if (effect.getEffectName().equals(MobEffects.NAUSEA.getName())) {
				toRemove.add(effect.getPotion());
			} else if (effect.getEffectName().equals(MobEffects.POISON.getName())) {
				toRemove.add(effect.getPotion());
			} else if (hasMatterUpgrade) {
				if (effect.getPotion().isBadEffect()) {
					toRemove.add(effect.getPotion());
					cost = 3;
				}
			}
		}

		if (toRemove.size() > 0 && cost == 0) {
			cost = 1;
		}

		for (Potion remove : toRemove) {
			target.removePotionEffect(remove);
		}

		//If potion core is loaded, add Antidote/Purity on a cleanse

		return cost;
	}
}
