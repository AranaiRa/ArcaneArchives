package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;

public class SharedGCTData
{
	public static final int RECIPE_PAGE_LIMIT = 7;
	private GCTRecipe currentRecipe;
	private GCTRecipe lastRecipe;
	private GCTRecipe penultimateRecipe;
	private int page;

	public SharedGCTData(GCTRecipe currentRecipe)
	{
		this.currentRecipe = currentRecipe;
	}

	public SharedGCTData()
	{
	}

	public GCTRecipe getCurrentRecipe()
	{
		return currentRecipe;
	}

	public void setCurrentRecipe(GCTRecipe currentRecipe)
	{
		this.currentRecipe = currentRecipe;
	}

	public boolean hasCurrentRecipe()
	{
		return currentRecipe != null;
	}

	public GCTRecipe getLastRecipe()
	{
		return lastRecipe;
	}

	public void setLastRecipe(GCTRecipe lastRecipe)
	{
		this.lastRecipe = lastRecipe;
	}

	public boolean hasLastRecipe()
	{
		return lastRecipe != null;
	}

	public GCTRecipe getPenultimateRecipe()
	{
		return penultimateRecipe;
	}

	public void setPenultimateRecipe(GCTRecipe lastLastRecipe)
	{
		this.penultimateRecipe = lastLastRecipe;
	}

	public boolean hasPenultimateRecipe()
	{
		return penultimateRecipe != null;
	}

	public void updatePenultimateRecipe()
	{
		this.penultimateRecipe = this.lastRecipe;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public void previousPage()
	{
		if(getPage() > 0) setPage(page - 1);
	}

	public void nextPage()
	{
		if(GCTRecipeList.getSize() > (page + 1) * RECIPE_PAGE_LIMIT) setPage(page + 1);
	}
}
