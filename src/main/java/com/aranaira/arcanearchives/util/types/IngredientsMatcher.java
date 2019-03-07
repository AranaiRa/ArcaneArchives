package com.aranaira.arcanearchives.util.types;

import java.util.List;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class IngredientsMatcher
{
    private final Int2IntMap counter = new Int2IntOpenHashMap();;
    private final IntSet ingredientsSet = new IntOpenHashSet();;
    private final Int2IntMap ingredientsMap = new Int2IntOpenHashMap();
    private final List<IngredientStack> ingredients;;

    public IngredientsMatcher(List<IngredientStack> ingredients)
    {
        this.ingredients = ingredients;
        
        for(int i = 0; i < ingredients.size(); i++)
        {
            IngredientStack stack = ingredients.get(i);
            counter.put(i, stack.getCount());
            IntList packs = stack.getValidItemStacksPacked();
            this.ingredientsSet.addAll(packs);
            for(int pack : packs)
                this.ingredientsMap.put(pack, i);
        }
    }

    public void account(ItemStack stack)
    {
        int index = RecipeItemHelper.pack(stack);
        if(counter.containsKey(index))
        {
            int val = counter.get(index);
            int newVal = Math.max(0, val - stack.getCount());
            if(newVal == 0)
            {
                counter.remove(index);
            } else
            {
                counter.put(index, newVal);
            }
        }
    }

    public boolean matches(@Nonnull IItemHandler inv)
    {
        for(int i = 0; i < inv.getSlots(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if(!itemstack.isEmpty())
            {
                int stack = RecipeItemHelper.pack(itemstack);
                if(ingredientsSet.contains(stack))
                {
                    account(itemstack);
                }
            }
        }
        
        return counter.isEmpty();
    }

    public Int2IntMap getMatchingSlots(@Nonnull IItemHandler inv)
    {
        Int2IntMap matchingSlots = new Int2IntOpenHashMap();
        for(int slot = 0; slot < inv.getSlots(); ++slot)
        {
            ItemStack itemstack = inv.getStackInSlot(slot);
            if(!itemstack.isEmpty())
            {
                int packedStack = RecipeItemHelper.pack(itemstack);
                if(ingredientsSet.contains(packedStack))
                {
                    int ingredientIndex = ingredientsMap.getOrDefault(packedStack, -1);
                    if (ingredientIndex >= 0)
                    {
                        IngredientStack ingredientStack = ingredients.get(ingredientIndex);
                        matchingSlots.put(slot, ingredientStack.getCount());
                    }
                }
            }
        }

        return matchingSlots;
    }
}
