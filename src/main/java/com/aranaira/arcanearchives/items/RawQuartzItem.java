package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.WorldUtil;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RawQuartzItem extends ItemTemplate {
	public static final String NAME = "raw_quartz";

	public RawQuartzItem () {
		super(NAME);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.component.rawquartz"));
	}

	@Override
	public EnumActionResult onItemUseFirst (EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (!player.isSneaking()) {
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
		}

		return EnumActionResult.SUCCESS;
	}
}
