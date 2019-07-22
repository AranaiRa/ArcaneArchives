package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.api.IGCTRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GCTRecipeWithConditionsCrafter extends GCTRecipeWithCrafter {
	private List<GCTCondition> conditions = new ArrayList<>();

	public GCTRecipeWithConditionsCrafter (String name, @Nonnull ItemStack result, Object... recipe) {
		super(name, result, recipe);
	}

	public IGCTRecipe addCondition (GCTCondition predicate) {
		this.conditions.add(predicate);
		return this;
	}

	@Override
	public boolean craftable (EntityPlayer player, TileEntity tile) {
		for (GCTCondition condition : conditions) {
			if (!condition.test(player, tile)) {
				return false;
			}
		}

		return true;
	}

	@FunctionalInterface
	public interface GCTCondition {
		boolean test (EntityPlayer player, TileEntity tile);
	}
}
