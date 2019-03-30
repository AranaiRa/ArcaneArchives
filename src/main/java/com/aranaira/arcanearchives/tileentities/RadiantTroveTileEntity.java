package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.util.ItemComparison;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class RadiantTroveTileEntity extends ImmanenceTileEntity
{
	private final TroveItemHandler inventory = new TroveItemHandler(this::update);
	private int lastTick = 0;

	public Object2IntOpenHashMap<UUID> rightClickCache = new Object2IntOpenHashMap<>();

	public void update () {
		if (world.isRemote) return;

		defaultServerSideUpdate();
	}

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

	public boolean isEmpty () {
		return inventory.isEmpty();
	}

	public void onLeftClickTrove (EntityPlayer player) {
		if(world.isRemote) return;

		int curTick = world.getMinecraftServer().getTickCounter();
		if (lastTick == curTick) return;
		lastTick = curTick;

		this.markDirty();

		ItemStack stack = inventory.extractItem(0, 64, false);
		if (stack.isEmpty()) return;

		EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, stack);
		world.spawnEntity(item);
	}

	public void onRightClickTrove (EntityPlayer player) {
		ItemStack mainhand = player.getHeldItemMainhand();
		if (mainhand.isEmpty()) return;

		this.markDirty();

		if (mainhand.getItem() == ItemRegistry.COMPONENT_MATERIALINTERFACE && player.isSneaking()) {
			if (inventory.upgrade())
			{
				mainhand.shrink(1);
				if (!player.world.isRemote) {
					player.sendStatusMessage(new TextComponentTranslation("arcanearchives.success.upgraded_trove", inventory.getUpgrades(), TroveItemHandler.MAX_UPGRADES), true);
				}
				return;
			} else {
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.upgrade_trove_failed", inventory.getUpgrades(), TroveItemHandler.MAX_UPGRADES), true);
				return;
			}
		}

		if (inventory.isEmpty()) {
			inventory.setItem(mainhand);
		}

		ItemStack reference = inventory.getItem();

		if (!ItemComparison.areStacksEqualIgnoreSize(reference, mainhand)) {
			if (mainhand.getItem() == ItemRegistry.COMPONENT_MATERIALINTERFACE) {
				player.sendStatusMessage(new TextComponentTranslation("arcanearchives.warning.sneak_to_upgrade"), true);
				return;
			}
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.trove_insertion_failed.wrong"), true);
			return;
		}

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

		ItemStack result = inventory.insertItem(0, mainhand, false);

		if (!result.isEmpty()) {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.trove_insertion_failed.full"), true);
			mainhand.setCount(result.getCount());
			return;
		} else {
			mainhand.setCount(0);
		}

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
						result = inventory.insertItem(0, inSlot, true);
						if (!result.isEmpty()) {
							int diff = inSlot.getCount() - result.getCount();
							inventory.insertItem(0, playerMain.extractItem(i, diff, false), false);
							player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.trove_insertion_failed.full"), true);
							this.markDirty();
							return;
						} else {
							int thisCount = inSlot.getCount();
							inventory.insertItem(0, playerMain.extractItem(i, thisCount, false), false);
						}
					}
				}
			}
		}
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
