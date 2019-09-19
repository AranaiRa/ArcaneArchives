package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.ItemStackWrapper;
import com.aranaira.arcanearchives.inventory.handlers.OptionalUpgradesHandler;
import com.aranaira.arcanearchives.inventory.handlers.SizeUpgradeItemHandler;
import com.aranaira.arcanearchives.items.EchoItem;
import com.aranaira.arcanearchives.tileentities.interfaces.IUpgradeableStorage;
import com.aranaira.arcanearchives.util.DuplicationUtils;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RadiantFurnaceTileEntity extends ImmanenceTileEntity implements IUpgradeableStorage {
	private OptionalUpgradesHandler optionalUpgrades = new OptionalUpgradesHandler();

	public static final int BURN_TIME = 200;

	private int burnTime;
	private int burnTimeTotal;
	private int cookTime;
	private int cookTimeTotal;
	private ItemStack itemCooking = ItemStack.EMPTY;
	private Ingredient lastRecipe = Ingredient.EMPTY;
	private ItemStack lastResult = ItemStack.EMPTY;
	private float lastXP = -1;

	private ItemStackHandler inventory = new ItemStackHandler(4);
	public ItemStackWrapper fuel = new ItemStackWrapper(0, inventory);
	public ItemStackWrapper input = new ItemStackWrapper(1, inventory);
	public ItemStackWrapper combined = new ItemStackWrapper(0, 1, inventory);
	public ItemStackWrapper output = new ItemStackWrapper(2, 3, inventory);

	public RadiantFurnaceTileEntity () {
		super("radiant_furnace");
	}

	public boolean isBurning () {
		return burnTime > 0;
	}

	public boolean isCooking () {
		return cookTime > 0;
	}

	@Override
	public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return null;
		}

		if (facing == EnumFacing.UP || facing == null) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combined);
		} else {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(output);
		}
	}

	public int getMaxCookTime () {
		return BURN_TIME + 1;
	}

	@Override
	public void update () {
		super.update();

		if (world.isRemote) {
			return;
		}

		if (isBurning()) {
			// Burn is reduced no matter what
			this.burnTime--;
			/*if (this.burnTime % 20 == 0) {
				ArcaneArchives.logger.info("Burn time: " + this.burnTime);
				ArcaneArchives.logger.info("Cook time: " + this.cookTime);
				ArcaneArchives.logger.info("Cook time total: " + this.cookTimeTotal);
			}*/
		}

		boolean shouldConsumeFuel = false;
		boolean shouldProduce = false;
		boolean canCook = false;

		// Inputs
		ItemStack inSlot = input.getStackInSlot(0);
		ItemStack outSlot = output.getStackInSlot(0);
		if (isCooking() && isBurning() && !inSlot.isEmpty() && ItemUtils.areStacksEqualIgnoreSize(inSlot, itemCooking)) {
			if (!outSlot.isEmpty() && ItemUtils.areStacksEqualIgnoreSize(lastResult, outSlot)) {
				// TODO: Go off the slot size
				if (outSlot.getCount() < outSlot.getMaxStackSize()) {
					canCook = true;
				}
			} else if (outSlot.isEmpty()) {
				canCook = true;
			}

			if (canCook) {
				if (cookTime == 1) {
					shouldProduce = true;
				}
				cookTime--;
				if (!isBurning()) {
					shouldConsumeFuel = true;
				}
			}
		}

		if (inSlot.isEmpty() || !ItemUtils.areStacksEqualIgnoreSize(inSlot, itemCooking)) {
			cookTime = 0;
			cookTimeTotal = 0;
		}

		if (!inSlot.isEmpty() && cookTime == 0) {
			// Try to start cooking the item in the slot
			if (lastRecipe != Ingredient.EMPTY && lastRecipe.apply(inSlot)) {
				cookTimeTotal = getMaxCookTime(); // Handle recipes with variable speeds/cook durations
				cookTime = getMaxCookTime();
				itemCooking = inSlot.copy();
				shouldConsumeFuel = true;
			} else {
				ItemStack resultPotential = FurnaceRecipes.instance().getSmeltingResult(inSlot);
				if (!resultPotential.isEmpty()) {
					lastResult = resultPotential.copy();
					cookTimeTotal = getMaxCookTime();
					cookTime = getMaxCookTime();
					itemCooking = inSlot.copy();
					lastXP = FurnaceRecipes.instance().getSmeltingExperience(inSlot);
					shouldConsumeFuel = true;
				}
			}
		}

		// Resolve fuels
		if (!this.isBurning() && shouldConsumeFuel) {
			ItemStack fuel = this.fuel.getStackInSlot(0);
			if (!fuel.isEmpty()) {
				int burnTime = TileEntityFurnace.getItemBurnTime(fuel);
				if (burnTime > 0) {
					this.burnTime = burnTime;
					this.burnTimeTotal = burnTime;
				}
			}
			this.fuel.extractItem(0, 1, false);
		}

		if (canCook && shouldProduce) {
			cookTime--;
			input.extractItem(0, 1, false);
			if (lastRecipe == Ingredient.EMPTY || lastRecipe.apply(itemCooking)) {
				lastResult = FurnaceRecipes.instance().getSmeltingResult(inSlot);
				lastXP = FurnaceRecipes.instance().getSmeltingExperience(inSlot);
			}

			boolean shouldDouble = DuplicationUtils.shouldDuplicate(inSlot);
			boolean doDouble = false;

			if (!outSlot.isEmpty() && !ItemUtils.areStacksEqualIgnoreSize(outSlot, lastResult)) {
				ArcaneArchives.logger.info("Noob messed something up!");
			} else if (outSlot.isEmpty()) {
				output.insertItem(0, lastResult.copy(), false);
				doDouble = shouldDouble;
			} else {
				outSlot.grow(1);
				doDouble = shouldDouble;
			}

			if (doDouble) {
				ItemStack doubleSlot = output.getStackInSlot(1);
				ItemStack doubled = EchoItem.echoFromItem(lastResult);
				if (!doubleSlot.isEmpty() && ItemUtils.areStacksEqualIgnoreSize(doubleSlot, doubled)) {
					doubleSlot.grow(1);
				} else if (doubleSlot.isEmpty()) {
					output.insertItem(1, doubled, false);
				}
			}
		}

		this.defaultServerSideUpdate();
	}

	@Override
	public SizeUpgradeItemHandler getSizeUpgradesHandler () {
		return null;
	}

	@Override
	public OptionalUpgradesHandler getOptionalUpgradesHandler () {
		return optionalUpgrades;
	}

	@Override
	public int getModifiedCapacity () {
		return 0;
	}

	public ItemStackHandler getInventory () {
		return inventory;
	}

	public int getBurnTime () {
		return burnTime;
	}

	public int getCookTime () {
		return cookTime;
	}

	public int getBurnTimeTotal () {
		return burnTimeTotal;
	}

	public int getCookTimeTotal () {
		return cookTimeTotal;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(Tags.OPTIONAL_UPGRADES, optionalUpgrades.serializeNBT());
		compound.setTag(Tags.INVENTORY, inventory.serializeNBT());
		compound.setInteger(Tags.BURN_TIME, burnTime);
		compound.setInteger(Tags.COOK_TIME, cookTime);
		compound.setInteger(Tags.COOK_TIME_TOTAL, cookTimeTotal);
		compound.setInteger(Tags.BURN_TIME_TOTAL, burnTimeTotal);
		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		optionalUpgrades.deserializeNBT(compound.getCompoundTag(Tags.OPTIONAL_UPGRADES));
		inventory.deserializeNBT(compound.getCompoundTag(Tags.INVENTORY));
		burnTime = compound.getInteger(Tags.BURN_TIME);
		burnTimeTotal = compound.getInteger(Tags.BURN_TIME_TOTAL);
		cookTime = compound.getInteger(Tags.COOK_TIME);
		cookTimeTotal = compound.getInteger(Tags.COOK_TIME_TOTAL);
	}

	public static class Tags {
		public static final String OPTIONAL_UPGRADES = "optional_upgrades";
		public static final String INVENTORY = "inventory";

		public static final String COOK_TIME = "cook_time";
		public static final String COOK_TIME_TOTAL = "cook_time_total";
		public static final String BURN_TIME = "burn_time";
		public static final String BURN_TIME_TOTAL = "burn_time_total";

		public Tags () {}
	}
}
