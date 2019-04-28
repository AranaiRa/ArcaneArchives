package com.aranaira.arcanearchives.items.itemblocks;

import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class RadiantTankItem extends ItemBlock
{
	public RadiantTankItem(Block block) {
		super(block);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		if (!stack.isEmpty()) {
			int capacity = RadiantTankTileEntity.BASE_CAPACITY;
			if (nbt != null) {
				capacity *= (nbt.getInteger("upgrades") + 1);
			}
			return new FluidHandlerItemStack(stack, capacity);
		}
		return super.initCapabilities(stack, nbt);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		FluidStack fluid;

		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(handler instanceof FluidHandlerItemStack) {
				FluidHandlerItemStack tank = (FluidHandlerItemStack) handler;
				fluid = tank.getFluid();
				if (fluid != null) {
					tooltip.add(I18n.format("arcanearchives.tooltip.tank.fluid", fluid.getLocalizedName()));
					tooltip.add(I18n.format("arcanearchives.tooltip.tank.amount", fluid.amount, RadiantTankTileEntity.BASE_CAPACITY * (tag.getInteger("upgrades") + 1)));
				} else {
					tooltip.add(I18n.format("arcanearchives.tooltip.tank.fluid", "None"));
					tooltip.add(I18n.format("arcanearchives.tooltip.tank.amount", 0, RadiantTankTileEntity.BASE_CAPACITY * (tag.getInteger("upgrades") + 1)));
				}
			}
		}

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
