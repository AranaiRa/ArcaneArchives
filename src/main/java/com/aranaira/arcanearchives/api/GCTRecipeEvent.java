package com.aranaira.arcanearchives.api;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is fired after the default recipes have been
 * registered with the GCT Recipe List. In order to add recipes
 * you can access the IGCTRecipeList instance from the
 * event.
 */
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
