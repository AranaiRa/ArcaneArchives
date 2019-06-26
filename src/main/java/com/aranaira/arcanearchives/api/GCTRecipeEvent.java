package com.aranaira.arcanearchives.api;

import com.aranaira.arcanearchives.api.IGCTRecipeList;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GCTRecipeEvent extends Event {
	public IGCTRecipeList recipeList;

	public GCTRecipeEvent (IGCTRecipeList recipeList) {
		this.recipeList = recipeList;
	}

	@Override
	public boolean isCancelable () {
		return false;
	}

	@Override
	public boolean hasResult () {
		return false;
	}
}
