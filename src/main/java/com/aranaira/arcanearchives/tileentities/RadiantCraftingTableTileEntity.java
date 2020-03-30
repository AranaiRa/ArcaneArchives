/*package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import com.aranaira.arcanearchives.recipe.fastcrafting.FastCraftingRecipe;
import com.aranaira.arcanearchives.tileentities.interfaces.IManifestTileEntity;
import com.aranaira.arcanearchives.util.NBTUtils;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public class RadiantCraftingTableTileEntity extends ImmanenceTileEntity implements IManifestTileEntity {
	private ItemStackHandler persistentMatrix = new ItemStackHandler(9);
	private RecipeList recipeList = new RecipeList();

	public RadiantCraftingTableTileEntity () {
		super("radiantcraftingtable");
	}

	@Override
	public String getDescriptor () {
		return "Radiant Crafting Table";
	}

	@Override
	public String getChestName () {
		return "";
	}

	@Override
	public ItemStackHandler getInventory () {
		return persistentMatrix;
	}

	public IRecipe getRecipe (int index) {
		return recipeList.get(index);
	}

	public void setRecipe (int index, IRecipe recipe) {
		recipeList.set(index, recipe);
		markDirty();
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey(AATileEntity.Tags.inventory)) {
			persistentMatrix.deserializeNBT(compound.getCompoundTag(AATileEntity.Tags.inventory));
		}

		for (int i = 0; i < 3; i++) {
			recipeList.set(i, null);
		}

		recipeList.set(0, NBTUtils.getRecipe(compound, Tags.RECIPE1));
		recipeList.set(1, NBTUtils.getRecipe(compound, Tags.RECIPE2));
		recipeList.set(2, NBTUtils.getRecipe(compound, Tags.RECIPE3));
	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		compound = super.writeToNBT(compound);

		compound.setTag(AATileEntity.Tags.inventory, persistentMatrix.serializeNBT());

		NBTUtils.setRecipe(compound, Tags.RECIPE1, recipeList.get(0));
		NBTUtils.setRecipe(compound, Tags.RECIPE2, recipeList.get(1));
		NBTUtils.setRecipe(compound, Tags.RECIPE3, recipeList.get(2));

		return compound;
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	@Override
	public NBTTagCompound getUpdateTag () {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	public void blockBroken () {
		if (!world.isRemote) {
			WorldUtil.spawnInventoryInWorld(world, getPos(), persistentMatrix);
		}
	}

	public boolean canCraftRecipe (EntityPlayer player, int index) {
		IRecipe recipe = recipeList.get(index);
		if (recipe == null) {
			return false;
		}

		if (!(player.openContainer instanceof ContainerRadiantCraftingTable)) {
			return false;
		}

		FastCraftingRecipe fast = new FastCraftingRecipe(recipe, world);
		IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		CombinedInvWrapper inv = new CombinedInvWrapper((IItemHandlerModifiable) playerInventory, new InvWrapper(((ContainerRadiantCraftingTable) player.openContainer).getCraftMatrix()));
		return fast.matches(inv);
	}

	public void tryCraftingRecipe (EntityPlayer player, int index) {
		IRecipe recipe = recipeList.get(index);
		if (recipe == null) {
			return;
		}

		if (player.openContainer instanceof ContainerRadiantCraftingTable) {
			InventoryCrafting matrix = ((ContainerRadiantCraftingTable) player.openContainer).getCraftMatrix();

			FastCraftingRecipe fast = new FastCraftingRecipe(recipe, world);
			IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			CombinedInvWrapper inv = new CombinedInvWrapper(new InvWrapper(matrix), (IItemHandlerModifiable) playerInventory);
			if (fast.matches(inv)) {
				fast.consumeAndHandleInventory(fast, inv, player, null, null, null, null);
				List<ItemStack> returned = fast.getReturned();
				for (ItemStack result : returned) {
					result = ItemHandlerHelper.insertItemStacked(playerInventory, result, false);
					if (!result.isEmpty()) {
						Block.spawnAsEntity(world, getPos(), result);
					}
				}
			}
		}
	}

	@SuppressWarnings("WeakerAccess")
	public static class Tags {
		public static final String RECIPE1 = "recipe1";
		public static final String RECIPE2 = "recipe2";
		public static final String RECIPE3 = "recipe3";
	}

	private static class RecipeList {
		private IRecipe recipe1;
		private IRecipe recipe2;
		private IRecipe recipe3;

		public IRecipe get (int index) {
			switch (index) {
				case 0:
					return recipe1;
				case 1:
					return recipe2;
				case 2:
					return recipe3;
				default:
					return null;
			}
		}

		public void set (int index, IRecipe recipe) {
			switch (index) {
				case 0:
					this.recipe1 = recipe;
					break;
				case 1:
					this.recipe2 = recipe;
					break;
				case 2:
					this.recipe3 = recipe;
					break;
			}
		}
	}
}*/
