package com.aranaira.arcanearchives.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;



public class RawQuartzItem extends Item {
	public static final String NAME = "raw_quartz";

	public RawQuartzItem () {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.raw_quartz"));
	}

	@Override
	public EnumActionResult onItemUseFirst (EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
/*		if (!player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		ItemStack itemstack = player.getHeldItem(hand);
		IBlockState mainState = world.getBlockState(pos);

		if (mainState.getBlock() instanceof BlockChest) {
			if (world.isRemote) {
				return EnumActionResult.SUCCESS;
			}
			List<ItemStack> stacks = new ArrayList<>();
			TileEntity te = world.getTileEntity(pos);
			IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for (int i = 0; i < handler.getSlots(); i++) {
				ItemStack slot = handler.extractItem(i, 64, false);
				if (!slot.isEmpty()) {
					stacks.add(slot);
				}
			}

			world.setBlockState(pos, BlockRegistry.RADIANT_CHEST.getDefaultState());

			RadiantChestTileEntity ite = WorldUtil.getTileEntity(RadiantChestTileEntity.class, world, pos);
			List<ItemStack> leftover = new ArrayList<>();

			if (ite != null) {
				ite.setNetworkId(player.getUniqueID());
				handler = ite.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				for (ItemStack stack : stacks) {
					ItemStack result = ItemHandlerHelper.insertItemStacked(handler, stack, false);
					if (!result.isEmpty()) {
						leftover.add(result);
					}
				}
			} else {
				for (ItemStack stack : stacks) {
					Block.spawnAsEntity(world, pos.up(), stack);
				}
				return EnumActionResult.SUCCESS;
			}

			if (!leftover.isEmpty()) {
				for (ItemStack stack : stacks) {
					Block.spawnAsEntity(world, pos.up(), stack);
				}
			}

			if (!player.capabilities.isCreativeMode) {
				itemstack.shrink(1);
			}

			return EnumActionResult.SUCCESS;
		} else if (mainState.getBlock() instanceof BlockWorkbench) {
			IBlockState iblockstate = BlockRegistry.RADIANT_CRAFTING_TABLE.getDefaultState();
			world.setBlockState(pos, iblockstate);
			itemstack.shrink(1);
			RadiantCraftingTableTileEntity te = WorldUtil.getTileEntity(RadiantCraftingTableTileEntity.class, world, pos);
			if (te != null) {
				te.setNetworkId(player.getUniqueID());
			}
		}*/

		return EnumActionResult.SUCCESS;
	}
}
