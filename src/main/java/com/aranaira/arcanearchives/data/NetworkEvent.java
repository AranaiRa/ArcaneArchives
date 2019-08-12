package com.aranaira.arcanearchives.data;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class NetworkEvent extends Event {
	private final UUID ownerNetwork;

	public NetworkEvent (UUID ownerNetwork) {
		this.ownerNetwork = ownerNetwork;
	}

	public UUID getOwnerNetwork () {
		return ownerNetwork;
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
