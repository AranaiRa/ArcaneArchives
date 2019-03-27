package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.types.IteRef;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class RadiantTroveTileEntity extends ImmanenceTileEntity
{
	private final TroveItemHandler inventory = new TroveItemHandler();

	public Object2IntOpenHashMap<UUID> rightClickCache = new Object2IntOpenHashMap<>();

	public RadiantTroveTileEntity()
	{
		super("radianttrove");
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	public void onLeftClickTrove (EntityPlayer player) {
		ItemStack reference = inventory.getItem();
		if (inventory.isEmpty()) {
			ArcaneArchives.logger.info("Trove contains no item");
		} else {
			ArcaneArchives.logger.info(String.format("Trove contains %d of %s, %s", inventory.getCount(), reference.getDisplayName(), inventory.getStackInSlot(0)));
		}

		ItemStack stack = inventory.extractItem(0, 64, false);
		if (stack.isEmpty()) return;

		EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, stack);
		world.spawnEntity(item);
	}

	public void onRightClickTrove (EntityPlayer player) {
		ItemStack mainhand = player.getHeldItemMainhand();
		if (mainhand.isEmpty()) return;

		if (inventory.isEmpty()) {
			inventory.setItem(mainhand);
			ArcaneArchives.logger.info("Trove is currently empty, setting item to: " + mainhand.getDisplayName());
		}

		ItemStack reference = inventory.getItem();

		UUID playerId = player.getUniqueID();
		boolean doubleClick = false;

		if (rightClickCache.containsKey(playerId)) {
			int lastClick = rightClickCache.getInt(playerId);
			if ((player.ticksExisted - lastClick) <= 25 && (player.ticksExisted - lastClick) >= 0) {
				doubleClick = true;
			} else {
				rightClickCache.put(playerId, player.ticksExisted);
			}
		}	else {
			rightClickCache.put(playerId, player.ticksExisted);
		}

		int count = 0;

		count += mainhand.getCount();
		mainhand.shrink(mainhand.getCount());

		if (doubleClick)
		{
			IItemHandler playerMain = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			if (playerMain != null)
			{
				for(int i = 0; i < playerMain.getSlots(); i++)
				{
					ItemStack inSlot = playerMain.getStackInSlot(i);
					if(ItemComparison.areStacksEqualIgnoreSize(reference, inSlot))
					{
						int thisCount = inSlot.getCount();
						playerMain.extractItem(i, thisCount, false);
						count += thisCount;
					}
				}
			}
		}

		inventory.insert(count);
		ArcaneArchives.logger.info(String.format("Inserted %dx%s into trove", count, reference.getDisplayName()));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.inventory.deserializeNBT(compound.getCompoundTag(Tags.HANDLER_ITEM));
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag(Tags.HANDLER_ITEM, this.inventory.serializeNBT());

		return compound;
	}

	public TroveItemHandler getInventory()
	{
		return inventory;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public static class Tags
	{
		public static final String HANDLER_ITEM = "handler_item";

		private Tags()
		{
		}
	}
}
