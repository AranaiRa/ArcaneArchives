package com.aranaira.arcanearchives.api.crafting.processors;

import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.crafting.ICrafter;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import com.google.gson.JsonObject;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IProcessor<H extends IArcaneInventory, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> {

  ItemStack process (ItemStack result, List<Ingredient> ingredients, List<ItemStack> incoming, List<List<ItemStack>> outgoing, ICrafter<H, C, T> crafter);

  JsonObject serialize ();
}
