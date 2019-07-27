package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.data.DataHelper.HiveMembershipInfo;
import com.aranaira.arcanearchives.data.ServerNetwork.SynchroniseInfo;
import com.aranaira.arcanearchives.inventory.handlers.ManifestItemHandler;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketNetworks;
import com.aranaira.arcanearchives.types.lists.ManifestList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientNetwork {
	public ManifestList manifestItems = new ManifestList(new ArrayList<>());
	/* Updated data via packet */

	private int totalResonators = 0;
	private int totalCores = 0;

	private Consumer<ManifestItemHandler> callback = null;

	private ManifestItemHandler manifestItemHandler = null;

	private boolean inHive = false;
	private boolean ownsHive = false;

	ClientNetwork (UUID id) {
		this.manifestItemHandler = new ManifestItemHandler(manifestItems);
	}

	public int getTotalResonators () {
		return totalResonators;
	}

	public int getTotalCores () {
		return totalCores;
	}

	public ManifestItemHandler getManifestHandler () {
		return manifestItemHandler;
	}

	// This requests a synchronise packet from the server
	// but does not include the manifest info.
	public void synchroniseData () {
		PacketNetworks.Request request = new PacketNetworks.Request(PacketNetworks.SynchroniseType.DATA);
		Networking.CHANNEL.sendToServer(request);
	}

	public void synchroniseManifest () {
		PacketNetworks.Request request = new PacketNetworks.Request(PacketNetworks.SynchroniseType.MANIFEST);
		Networking.CHANNEL.sendToServer(request);
	}

	public void synchroniseManifest (Consumer<ManifestItemHandler> callback) {
		this.callback = callback;
		synchroniseManifest();
	}

	public void deserializeManifest (ManifestList data) {
		if (data == null) {
			return;
		}

		manifestItems.clear();
		manifestItems.addAll(data);
		manifestItems.deserializationFinished();
		manifestItemHandler.nullify();

		if (callback != null) {
			callback.accept(manifestItemHandler);
			callback = null;
		}
	}

	@SideOnly(Side.CLIENT)
	public EntityPlayer getPlayer () {
		return Minecraft.getMinecraft().player;
	}

	public void deserializeData (SynchroniseInfo info) {
		this.totalCores = info.totalCores;
		this.totalResonators = info.totalResonators;
	}

	public void deserializeHive (HiveMembershipInfo info) {
		this.inHive = info.inHive;
		this.ownsHive = info.isOwner;
	}

	public boolean inHive () {
		return inHive;
	}

	public boolean ownsHive () {
		return ownsHive;
	}
}

