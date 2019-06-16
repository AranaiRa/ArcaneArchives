package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.types.IteRef;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

import javax.annotation.Nonnull;

public class BrazierTileEntity extends ImmanenceTileEntity {
	public BrazierTileEntity () {
		super("brazier");
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
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

	public static class Tags {
		private Tags () {
		}
	}

	public boolean tryInsert(ItemStack stack) {
		boolean inserted = false;

		ArcaneArchives.logger.info("Trying to insert "+stack.getItem()+" into network belonging to "+getServerNetwork().getPlayer().getName());

		return true;//inserted;
	}
}
