package com.aranaira.arcanearchives.items.gems.oval;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketArcaneGem;
import net.minecraft.block.Block;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class MunchstoneItem extends ArcaneGemItem {
	public static final String NAME = "munchstone";
	public static EdibleBlock[] entries = {
			new EdibleBlock(Blocks.LOG, 4, 1),
			new EdibleBlock(Blocks.LOG2, 4, 1),
			new EdibleBlock(Blocks.LEAVES, 2, 1),
			new EdibleBlock(Blocks.LEAVES2, 2, 1),
			new EdibleBlock(Blocks.HAY_BLOCK, 15, 1),
			new EdibleBlock(Blocks.MELON_BLOCK, 15, 1),
			new EdibleBlock(Blocks.BROWN_MUSHROOM_BLOCK, 8, 1),
			new EdibleBlock(Blocks.BROWN_MUSHROOM, 4, 1),
			new EdibleBlock(Blocks.RED_MUSHROOM_BLOCK, 8, 1),
			new EdibleBlock(Blocks.RED_MUSHROOM, 4, 1),
			new EdibleBlock(Blocks.NETHER_WART_BLOCK, 15, 1),
			new EdibleBlock(Blocks.NETHER_WART, 4, 1),
			new EdibleBlock(Blocks.CHORUS_FLOWER, 6, 1),
			new EdibleBlock(Blocks.CHORUS_PLANT, 6, 1),
			new EdibleBlock(Blocks.CACTUS, 6, 1),
			new EdibleBlock(Blocks.COCOA, 4, 1),
			new EdibleBlock(Blocks.DEADBUSH, 2, 1),
			new EdibleBlock(Blocks.DOUBLE_PLANT, 2, 1),
			new EdibleBlock(Blocks.PUMPKIN, 15, 1),
			new EdibleBlock(Blocks.LIT_PUMPKIN, 16, 1),
			new EdibleBlock(Blocks.PUMPKIN_STEM, 4, 1),
			new EdibleBlock(Blocks.MELON_STEM, 4, 1),
			new EdibleBlock(Blocks.RED_FLOWER, 2, 1),
			new EdibleBlock(Blocks.YELLOW_FLOWER, 2, 1),
			new EdibleBlock(Blocks.REEDS, 4, 1),
			new EdibleBlock(Blocks.SAPLING, 2, 1),
			new EdibleBlock(Blocks.TALLGRASS, 1, 1),
			new EdibleBlock(Blocks.VINE, 2, 1),
			new EdibleBlock(Blocks.WATERLILY, 4, 1),
			new EdibleBlock(Blocks.WHEAT, 4, 1),
			new EdibleBlock(Blocks.POTATOES, 4, 1),
			new EdibleBlock(Blocks.CARROTS, 4, 1),
			new EdibleBlock(Blocks.BEETROOTS, 4, 1)
	};

	public MunchstoneItem () {
		super(NAME, GemCut.OVAL, GemColor.BLACK, 60, 240);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.munchstone"));
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
					if (stack.getItem() == Items.DYE && stack.getMetadata() == 15) {
						int numConsumed = 5;
						if (numConsumed > stack.getCount()) {
							numConsumed = stack.getCount();
						}
						GemUtil.restoreCharge(gem, numConsumed * 12);
						stack.shrink(numConsumed);
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

	@Override
	public EnumActionResult onItemUse (EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {

			ArcaneArchives.logger.info("munchstone trigger");
			ItemStack gem = player.getHeldItemMainhand();

			if (GemUtil.getCharge(gem) > 0) {
				Block block = world.getBlockState(pos).getBlock();

				for (EdibleBlock eb : entries) {
					if (block == eb.block) {
						int hungerLevel = player.getFoodStats().getFoodLevel();
						int hungerMod = eb.hungerValue;
						float saturationLevel = player.getFoodStats().getSaturationLevel();
						float saturationMod = eb.saturationValue;

						hungerLevel += eb.hungerValue;
						saturationLevel += eb.saturationValue;
						if (hungerLevel + hungerMod > 20) {
							hungerMod = MathHelper.clamp(20 - hungerLevel, 0, 20);
						}
						if (saturationLevel + saturationMod > 20) {
							saturationMod = MathHelper.clamp(20 - saturationLevel, 0, 20);
						}

						int chargeConsumed = hungerMod + (int) saturationMod;
						ArcaneArchives.logger.info("charge: "+chargeConsumed);

						if (chargeConsumed > 0) {
							player.getFoodStats().addStats(eb.hungerValue, eb.saturationValue);
							GemUtil.consumeCharge(gem, chargeConsumed);
							world.setBlockState(pos, Blocks.AIR.getDefaultState());

							PacketArcaneGem packet = new PacketArcaneGem(cut, color, blockPosToVector(pos, true), blockPosToVector(pos, true));
							NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(player.dimension, pos.getX(), pos.getY(), pos.getZ(), 40);
							NetworkHandler.CHANNEL.sendToAllAround(packet, tp);
						}
						break;
					}
				}
			}
		}
		return EnumActionResult.PASS;
	}

	private static class EdibleBlock {
		public Block block;
		public int hungerValue;
		float saturationValue;

		public EdibleBlock (Block block, int hungerValue, float saturationValue) {
			this.block = block;
			this.hungerValue = hungerValue;
			this.saturationValue = saturationValue;
		}
	}
}
