/*package com.aranaira.arcanearchives.items.gems.oval;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.GemUtil.AvailableGemsHandler;
import com.aranaira.arcanearchives.items.gems.GemUtil.GemStack;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketArcaneGems.GemParticle;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class MunchstoneItem extends ArcaneGemItem {
	public static final String NAME = "munchstone";
	public static EdibleBlock[] entries;

	public static final String[] DEFAULT_ENTRIES = {"minecraft:log, 4", "minecraft:log2, 4", "minecraft:leaves, 2", "minecraft:leaves2, 2", "minecraft:hay_block, 15", "minecraft:melon_block, 15", "minecraft:brown_mushroom_block, 8", "minecraft:brown_mushroom, 4", "minecraft:red_mushroom_block, 8", "minecraft:red_mushroom, 4", "minecraft:nether_wart_block, 15", "minecraft:nether_wart, 4", "minecraft:chorus_flower, 6", "minecraft:chorus_plant, 6", "minecraft:cactus, 6", "minecraft:cocoa, 4", "minecraft:deadbush, 2", "minecraft:double_plant, 2", "minecraft:pumpkin, 15", "minecraft:lit_pumpkin, 16", "minecraft:pumpkin_stem, 4", "minecraft:melon_stem, 4", "minecraft:red_flower, 2", "minecraft:yellow_flower, 2", "minecraft:reeds, 4", "minecraft:sapling, 2", "minecraft:tallgrass, 1", "minecraft:vine, 2", "minecraft:waterlily, 4", "minecraft:wheat, 4", "minecraft:potatoes, 4", "minecraft:carrots, 4", "minecraft:beetroots, 4", "mysticalworld:aubergine_crop, 4", "mysticalworld:thatch, 3", "betternether:agave, 3", "betternether:barrel_cactus, 4", "betternether:black_bush, 1", "betternether:egg_plant, 1", "betternether:gray_mold, 1", "betternether:lucis_spore, 1", "betternether:nether_cactus, 1", "betternether:nether_grass, 1", "betternether:nether_reed, 1", "betternether:orange_mushroom, 1", "betternether:red_mold, 1", "roots:baffle_cap_mushroom, 4", "roots:baffle_cap_huge_stem, 8", "roots:baffle_cap_huge_top, 8", "roots:moonglow_crop, 4", "roots:pereskia_crop, 4", "roots:spirit_herb_crop, 4", "roots:wildroot_crop, 4", "roots:wildewheet_crop, 4", "roots:cloud_berry_crop, 4", "roots:infernal_bulb_crop, 4", "roots:dewgonia_crop, 4", "roots:stalicripe_crop, 4", "roots:wildwood_log, 8", "roots:wildwood_leaves, 4", "roots:wildwood_sapling, 20", "rustic:log, 4", "rustic:sapling, 2", "rustic:leaves, 2", "rustic:leaves_apple, 3", "rustic:wildberry_bush, 3", "rustic:grape_stem, 4", "rustic:aloe_vera, 3", "rustic:blood_orchid, 3", "rustic:chamomile, 3", "rustic:cohosh, 3", "rustic:deathstalk_mushroom, 3", "rustic:horsetail, 3", "rustic:mooncap_mushroom, 3", "rustic:wind_thistle, 3", "rustic:cloudsbluff, 3", "rustic:core_root, 3", "rustic:ginseng, 3", "rustic:marsh_mallow, 3", "thaumcraft:sapling_greatwood, 2", "thaumcraft:sapling_silverwood, 3", "thaumcraft:log_greatwood, 4", "thaumcraft:log_silverwood, 5", "thaumcraft:leaves_greatwood, 2", "thaumcraft:leaves_silverwood, 3", "thaumcraft:cinderpearl, 3", "thaumcraft:shimmerleaf, 3", "thaumcraft:vishroom, 3"};

	public MunchstoneItem () {
		super(NAME, GemCut.OVAL, GemColor.BLACK, 60, 240);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.munchstone"));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.recharge.munchstone"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			AvailableGemsHandler handler = GemUtil.getHeldGem(player, hand);
			GemStack gem = handler.getHeld();
			recharge(world, player, gem);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public boolean recharge (World world, EntityPlayer player, GemStack gem) {
		if (gem != null && GemUtil.getCharge(gem) == 0) {
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
					GemParticle packet = new GemParticle(cut, color, pos, pos);
					Networking.sendToAllTracking(packet, player);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public EnumActionResult onItemUse (EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {

			//ArcaneArchives.logger.info("munchstone trigger");
			AvailableGemsHandler handler = GemUtil.getHeldGem(player, hand);
			*//*if (player.isSneaking())
			{
				for(EdibleBlock eb : getConfig()) {
					ArcaneArchives.logger.info(eb.block.getLocalizedName());
				}
			}*//*

			if (handler.getHeld() != null && GemUtil.getCharge(handler.getHeld()) > 0) {
				Block block = world.getBlockState(pos).getBlock();

				for (EdibleBlock eb : getConfig()) {
					if (eb == null) {
						continue;
					}
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
						//ArcaneArchives.logger.info("charge: " + chargeConsumed);

						if (chargeConsumed > 0) {
							player.getFoodStats().addStats(eb.hungerValue, eb.saturationValue);
							GemUtil.consumeCharge(handler.getHeld(), chargeConsumed);
							world.setBlockState(pos, Blocks.AIR.getDefaultState());

							GemParticle packet = new GemParticle(cut, color, blockPosToVector(pos, true), blockPosToVector(pos, true));
							Networking.sendToAllTracking(packet, player);
						}
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
		return EnumActionResult.PASS;
	}

	public static class EdibleBlock {
		public Block block;
		public int hungerValue;
		float saturationValue;

		public EdibleBlock (Block block, int hungerValue, float saturationValue) {
			this.block = block;
			this.hungerValue = hungerValue;
			this.saturationValue = saturationValue;
		}
	}

	public EdibleBlock[] getConfig () {
		if (entries == null) {
			EdibleBlock[] output = new EdibleBlock[ConfigHandler.ArsenalConfig.MunchstoneValidEntries.length];
			ArrayList<EdibleBlock> verifiedEBs = new ArrayList<>();
			//ArcaneArchives.logger.info("[MUNCHSTONE] Attempting configuration using " + output.length + " entries.");

			for (int i = 0; i < ConfigHandler.ArsenalConfig.MunchstoneValidEntries.length; i++) {
				String entry = ConfigHandler.ArsenalConfig.MunchstoneValidEntries[i];
				String[] parse = entry.split(",");
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parse[0].trim()));
				int feed = 0;
				try {
					feed = Integer.parseInt(parse[1].trim());
				} catch (Exception e) {
				}
				EdibleBlock eb = new EdibleBlock(block, feed, 1.0F);
				if (block == Blocks.AIR) {
					//ArcaneArchives.logger.info("[MUNCHSTONE] Couldn't find \"" + parse[0].trim() + "\"; skipping.");
					continue;
				} else if (feed <= 0) {
					//ArcaneArchives.logger.info("[MUNCHSTONE] Hunger restoration value for \"" + parse[0].trim() + "\" was invalid or couldn't be parsed; skipping.");
					continue;
				} else {
					//ArcaneArchives.logger.info("[MUNCHSTONE] Registering \"" + parse[0].trim() + "\" as " + block.getLocalizedName());
					verifiedEBs.add(eb);
				}
			}

			for (int i = 0; i < verifiedEBs.size(); i++) {
				output[i] = verifiedEBs.get(i);
			}

			//ArcaneArchives.logger.info("[MUNCHSTONE] Configured with " + output.length + " entries.");
			MunchstoneItem.entries = output;
		}
		return entries;
	}
}*/
