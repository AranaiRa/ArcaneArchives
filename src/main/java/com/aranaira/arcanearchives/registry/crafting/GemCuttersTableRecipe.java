package com.aranaira.arcanearchives.registry.crafting;

import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.types.Tuple;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class GemCuttersTableRecipe
{
	public static final GemCuttersTableRecipe EMPTY = new GemCuttersTableRecipe(new ArrayList<>(), ItemStack.EMPTY);

	private ItemStack mOutput;
	private List<ItemStack> mInput;

	// Use ItemStack.EMPTY over null.
	public GemCuttersTableRecipe(@Nonnull List<ItemStack> input, @Nonnull ItemStack output)
	{
		// Make sure we're keeping copies of things
		// Also, set this up so we can sanitize it later if we need to stet values to one
		// although I believe a recipe can contain more than one of an item?
		mInput = input.stream().map(ItemStack::copy).collect(Collectors.toList());
		mOutput = output.copy();
	}

	public boolean isEmpty()
	{
		return this == EMPTY;
	}

	public boolean matchesRecipe(ItemStackHandler internal, InvWrapper playerInventory)
	{
		if(isEmpty()) return false;

		RecipeMatcher match = new RecipeMatcher();
		match.accept(internal);
		match.accept(playerInventory);

		return match.match();
	}

	public boolean consume (ItemStackHandler internal, InvWrapper playerInventory) {
		RecipeMatcher match = new RecipeMatcher(false);
		match.accept(internal);
		match.accept(playerInventory);

		return match.match();
	}

	@Nonnull
	public ItemStack getOutput()
	{
		if(mOutput == null || mOutput.isEmpty()) return ItemStack.EMPTY;

		return mOutput.copy();
	}

	public List<ItemStack> getInput()
	{
		return new ArrayList<>(mInput);
	}

	public class RecipeMatcher
	{
		private boolean simulate = true;
		private List<ItemStack> stacks;

		public RecipeMatcher()
		{
			reset();
		}

		public RecipeMatcher(boolean simulate)
		{
			this.simulate = simulate;
			reset();
		}

		public void reset()
		{
			stacks = mInput.stream().map(ItemStack::copy).collect(Collectors.toList());
		}

		public void accept(IItemHandlerModifiable input)
		{
			for(int j = 0; j < input.getSlots(); j++)
			{
				ItemStack potential = input.getStackInSlot(j);

				stacks.removeIf(ItemStack::isEmpty);

				for(int i = 0; i < stacks.size(); i++)
				{
					if (potential.isEmpty()) return;

					ItemStack requirement = stacks.get(i);
					if(requirement.isEmpty()) continue;

					if(ItemComparison.AreItemsEqual(requirement, potential))
					{
						// There's less needed than in the slot
						if(requirement.getCount() == potential.getCount())
						{
							if(!simulate) {
								input.setStackInSlot(j, ItemStack.EMPTY);
							}
							requirement.setCount(0);
							break;
						} else if (potential.getCount() < requirement.getCount()) {
							int diff = requirement.getCount() - potential.getCount();
							requirement.setCount(diff);
							if (!simulate) {
								input.setStackInSlot(j, ItemStack.EMPTY);
							}
						} else { // if (potential.getCount() > requirement.getCount()) {
							if (!simulate) {
								potential.shrink(requirement.getCount());
								input.setStackInSlot(j, potential);
							}
							requirement.setCount(0);
						}
					}
				}
			}

			stacks.removeIf(ItemStack::isEmpty);
		}

		public boolean match()
		{
			return stacks.isEmpty();
		}
	}
}
