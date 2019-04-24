package com.aranaira.arcanearchives.integration.jei;

import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shadowfacts
 */
public class CraftingStationRecipeTransferInfo implements IRecipeTransferInfo<ContainerRadiantCraftingTable>
{

	@Nonnull
	@Override
	public Class<ContainerRadiantCraftingTable> getContainerClass() {
		return ContainerRadiantCraftingTable.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	public boolean canHandle(ContainerRadiantCraftingTable container) {
		return true;
	}

	@Nonnull
	@Override
	public List<Slot> getRecipeSlots(ContainerRadiantCraftingTable container) {
		List<Slot> slots = new ArrayList<>();
		for(int i = 1; i < 10; i++) {
			slots.add(container.getSlot(i));
		}
		return slots;
	}

	@Nonnull
	@Override
	public List<Slot> getInventorySlots(ContainerRadiantCraftingTable container) {
		List<Slot> slots = new ArrayList<>();

		// skip the actual slots of the crafting table
		for(int i = 10; i < container.inventorySlots.size(); i++) {
			slots.add(container.getSlot(i));
		}
		return slots;
	}
}
