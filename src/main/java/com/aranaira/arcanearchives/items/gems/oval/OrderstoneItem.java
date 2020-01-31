/*package com.aranaira.arcanearchives.items.gems.oval;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.GemUtil.AvailableGemsHandler;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.block.BlockFurnace.FACING;

public class OrderstoneItem extends ArcaneGemItem {
	public static final String NAME = "orderstone";

	public OrderstoneItem () {
		super(NAME, GemCut.OVAL, GemColor.PINK, 100, 400);
	}

	public static final String[] DEFAULT_ENTRIES = {"1, minecraft:sand, minecraft:dirt:1, minecraft:dirt, minecraft:mycelium, minecraft:dirt:2, minecraft:grass", "1, minecraft:gravel, minecraft:cobblestone, minecraft:stone", "1, minecraft:mossy_cobblestone, minecraft:cobblestone, minecraft:stone", "3, minecraft:stonebrick:2, minecraft:stonebrick, minecraft:stonebrick:3", "3, minecraft:stonebrick:1, minecraft:stonebrick, minecraft:stonebrick:3", "25, minecraft:anvil:2, minecraft:anvil:1, minecraft:anvil"};

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		NBTTagCompound nbt = ItemUtils.getOrCreateTagCompound(stack);
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.orderstone"));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.recharge.orderstone"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return false;
	}

	@Override
	public EnumActionResult onItemUse (EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			Block block = world.getBlockState(pos).getBlock();
			AvailableGemsHandler handler = GemUtil.getHeldGem(player, hand);
			if (handler.getHeld() != null && GemUtil.getCharge(handler.getHeld()) > 0) {
				int chargeCost = 0;
				*//**
				 * Gravel -> Cobblestone -> Stone
				 *//*
				if (block == Blocks.GRAVEL) {
					world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), 0);
					chargeCost = 1;
				} else if (block == Blocks.COBBLESTONE) {
					world.setBlockState(pos, Blocks.STONE.getDefaultState(), 0);
					chargeCost = 1;
				} else if (block == Blocks.MOSSY_COBBLESTONE) {
					world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), 0);
					chargeCost = 1;
				}
				*//**
				 * Sand -> Coarse Dirt -> Dirt -> Mycelium -> Podzol -> Grass
				 *//*
				else if (block == Blocks.SAND) {
					world.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT), 0);
					chargeCost = 1;
				} else if (block == Blocks.DIRT) {
					IBlockState state = world.getBlockState(pos);
					BlockDirt.DirtType variant = state.getValue(BlockDirt.VARIANT);
					if (variant == BlockDirt.DirtType.COARSE_DIRT) {
						world.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), 0);
						chargeCost = 1;
					} else if (variant == BlockDirt.DirtType.DIRT) {
						world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState(), 0);
						chargeCost = 1;
					} else if (variant == BlockDirt.DirtType.PODZOL) {
						world.setBlockState(pos, Blocks.GRASS.getDefaultState(), 0);
						chargeCost = 1;
					}
				} else if (block == Blocks.MYCELIUM) {
					IBlockState state = world.getBlockState(pos);
					BlockDirt.DirtType variant = Blocks.DIRT.getDefaultState().getValue(BlockDirt.VARIANT);
					world.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL), 0);
					chargeCost = 1;
				}
				*//**
				 * Cracked/Mossy Bricks -> Stone bricks
				 *//*
				else if (block == Blocks.STONEBRICK) {
					IBlockState state = world.getBlockState(pos);
					BlockStoneBrick.EnumType variant = state.getValue(BlockStoneBrick.VARIANT);
					if (variant == BlockStoneBrick.EnumType.CRACKED || variant == BlockStoneBrick.EnumType.MOSSY) {
						chargeCost = 1;
						world.setBlockState(pos, state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT), 0);
					} else if (variant == BlockStoneBrick.EnumType.DEFAULT) {
						chargeCost = 4;
						world.setBlockState(pos, state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 0);
					}
				}
				*//**
				 * Anvils
				 *//*
				else if (block == Blocks.ANVIL) {
					IBlockState state = world.getBlockState(pos);
					int damage = state.getValue(BlockAnvil.DAMAGE);
					if (damage > 0) {
						damage--;
						chargeCost = 25;
						world.setBlockState(pos, state.withProperty(BlockAnvil.DAMAGE, damage), 0);
					}
				}
				*//**
				 * Furnace -> Lit Furnace
				 *//*
				else if (block == Blocks.FURNACE) {
					IBlockState oldState = world.getBlockState(pos);
					IBlockState newState = Blocks.LIT_FURNACE.getDefaultState();
					newState = newState.withProperty(FACING, oldState.getValue(FACING));
					world.setBlockState(pos, newState, 0);
					chargeCost = 1;
				}
				GemUtil.consumeCharge(handler.getHeld(), chargeCost);
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	public class OrderstoneTransmutationSequence {
		public int transmutationCost;
		public IBlockState[] sequence;

		public OrderstoneTransmutationSequence (String in) {
			String[] parse = in.split(",");
			transmutationCost = Integer.parseInt(parse[0]);
			sequence = new IBlockState[parse.length - 1];
			for (int i = 1; i < parse.length - 1; i++) {
				String address = parse[i].trim();
			}
		}
	}
}*/
