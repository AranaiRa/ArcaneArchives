package com.aranaira.arcanearchives.items.itemblocks;

import com.aranaira.arcanearchives.inventory.handlers.TankItemFluidHandler;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity.Tags;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RadiantTankItem extends ItemBlock {
	public RadiantTankItem (Block block) {
		super(block);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new TankItemFluidHandler(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		FluidStack fluid;

		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (handler instanceof FluidHandlerItemStack) {
				FluidHandlerItemStack tank = (FluidHandlerItemStack) handler;
				fluid = tank.getFluid();
				int maximumCapacity = 16000;
				if (tag.hasKey(Tags.MAXIMUM_CAPACITY)) {
					maximumCapacity = tag.getInteger(Tags.MAXIMUM_CAPACITY);
				}
				if (fluid != null) {
					tooltip.add(I18n.format("arcanearchives.tooltip.tank.fluid", fluid.getLocalizedName()));
					tooltip.add(I18n.format("arcanearchives.tooltip.tank.amount", fluid.amount, maximumCapacity));
				} else {
					tooltip.add(I18n.format("arcanearchives.tooltip.tank.fluid", "None"));
					tooltip.add(I18n.format("arcanearchives.tooltip.tank.amount", 0, maximumCapacity));
				}
				if (tank.getTankProperties().length > 0) {
					tooltip.add("Capacity is: " + tank.getTankProperties()[0].getCapacity());
				}
			}
		}

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
