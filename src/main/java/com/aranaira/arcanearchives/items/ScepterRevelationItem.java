package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.blocks.MonitoringCrystal;
import com.aranaira.arcanearchives.blocks.templates.BlockDirectionalTemplate;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.items.templates.IItemScepter;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.*;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity.TroveItemHandler;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ScepterRevelationItem extends ItemTemplate implements IItemScepter {
	public static final String NAME = "scepter_revelation";

	public ScepterRevelationItem () {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public EnumActionResult onItemUseFirst (EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {

		if (hand != EnumHand.MAIN_HAND) {
			return EnumActionResult.SUCCESS;
		}

		if (world.isAirBlock(pos)) {
			return EnumActionResult.SUCCESS;
		}

		player.swingArm(hand);

		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		}

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		Style def = new Style().setColor(TextFormatting.GOLD);
		Style error = new Style().setColor(TextFormatting.DARK_RED).setBold(true);

		if (block == BlockRegistry.RADIANT_CHEST) {
			RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			if (te.getChestName().isEmpty()) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_chest.unnamed").setStyle(def));
			} else {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_chest.name", te.getChestName()).setStyle(def));
			}

			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_chest.slots", te.countEmptySlots()).setStyle(def));
		} else if (block == BlockRegistry.RADIANT_CRAFTING_TABLE) {
			RadiantCraftingTableTileEntity te = WorldUtil.getTileEntity(RadiantCraftingTableTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			ItemStackHandler inventory = te.getInventory();
			boolean empty = true;
			List<TextComponentTranslation> names = new ArrayList<>();
			for (int i = 0; i < inventory.getSlots(); i++) {
				ItemStack stack = inventory.getStackInSlot(i);
				if (!stack.isEmpty()) {
					empty = false;
					names.add(new TextComponentTranslation(stack.getTranslationKey() + ".name"));
				}
			}

			if (empty) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_crafting.empty").setStyle(def));
			} else {
				TextComponentString base = new TextComponentString("");
				for (int i = 0; i < names.size(); i++) {
					base.appendSibling(names.get(i));
					if (i != names.size() - 1) {
						base.appendText(", ");
					}
				}
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_crafting.contains", base).setStyle(def));
			}
		} else if (block == BlockRegistry.RADIANT_TANK) {
			RadiantTankTileEntity te = WorldUtil.getTileEntity(RadiantTankTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			IFluidHandler cap = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
			FluidStack fluid = te.getInventory().getFluid();
			int amount = te.getInventory().getFluidAmount();
			int maxCount = te.getCapacity();

			if (fluid == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_tank.empty").setStyle(def));
			} else {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_tank.item", new TextComponentTranslation(fluid.getLocalizedName()).setStyle(def)));
			}

			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_tank.count", amount, maxCount).setStyle(def));
		} else if (block == BlockRegistry.RADIANT_TROVE) {
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			TroveItemHandler inventory = te.getInventory();
			ItemStack reference = inventory.getItemCurrent();
			int count = inventory.getCount();
			int storageUpgrades = inventory.getTotalUpgradesCount().x;
			int optionalUpgrades = inventory.getTotalUpgradesCount().y;
			int maxCount = inventory.getMaxCount();

			if (reference.isEmpty()) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_trove.empty").setStyle(def));
			} else {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_trove.item", new TextComponentTranslation(reference.getTranslationKey() + ".name")).setStyle(def));
			}

			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_trove.count", count, maxCount).setStyle(def));
			if (storageUpgrades + optionalUpgrades > 0) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_trove.upgrades", "" + storageUpgrades, "" + optionalUpgrades).setStyle(def));
			}
		} else if (block == BlockRegistry.RADIANT_RESONATOR) {
			RadiantResonatorTileEntity te = WorldUtil.getTileEntity(RadiantResonatorTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_resonator.progress", te.getPercentageComplete()).setStyle(def));
			RadiantResonatorTileEntity.TickResult res = te.canTick();
			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_resonator.status", new TextComponentTranslation(res.getKey()).setStyle(new Style().setColor(res.getFormat()))));
		} else if (block == BlockRegistry.MONITORING_CRYSTAL) {
			MonitoringCrystalTileEntity te = WorldUtil.getTileEntity(MonitoringCrystalTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			EnumFacing te_facing = state.getValue(((BlockDirectionalTemplate) state.getBlock()).getFacingProperty()).getOpposite();
			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.monitoring_crystal.facing", te_facing.getName()).setStyle(def));
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.scepter_revelation"));
	}
}
