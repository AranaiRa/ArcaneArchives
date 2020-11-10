/*
package com.aranaira.arcanearchives.inventories;


import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity.Tags;
import com.aranaira.arcanearchives.util.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class TankItemFluidHandler extends FluidHandlerItemStack {
  @Nonnull
  protected ItemStack container;

  public TankItemFluidHandler(@Nonnull ItemStack container) {
    super(container, NBTUtils.defaultInt(container, Tags.MAXIMUM_CAPACITY, RadiantTankTileEntity.BASE_CAPACITY));
    this.container = container;
  }

  public int getCapacity() {
    return NBTUtils.defaultInt(container, Tags.MAXIMUM_CAPACITY, RadiantTankTileEntity.BASE_CAPACITY);
  }

  @Override
  public IFluidTankProperties[] getTankProperties() {
    return new FluidTankProperties[]{new FluidTankProperties(getFluid(), getCapacity())};
  }

  @Override
  public int fill(FluidStack resource, boolean doFill) {
    if (container.getCount() != 1 || resource == null || resource.amount <= 0 || !canFillFluidType(resource)) {
      return 0;
    }

    FluidStack contained = getFluid();
    if (contained == null) {
      int fillAmount = Math.min(getCapacity(), resource.amount);

      if (doFill) {
        FluidStack filled = resource.copy();
        filled.amount = fillAmount;
        setFluid(filled);
      }

      return fillAmount;
    } else {
      if (contained.isFluidEqual(resource)) {
        int fillAmount = Math.min(getCapacity() - contained.amount, resource.amount);

        if (doFill && fillAmount > 0) {
          contained.amount += fillAmount;
          setFluid(contained);
        }

        return fillAmount;
      }

      return 0;
    }
  }
}
*/

