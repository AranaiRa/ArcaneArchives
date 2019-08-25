package com.aranaira.arcanearchives.network;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.network.Handlers.ConfigServerHandler;
import com.aranaira.arcanearchives.network.Messages.ConfigPacket;
import com.aranaira.arcanearchives.network.Messages.EmptyMessageClient;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConfig {
	public static class MaxDistance extends ConfigPacket<Integer> {
		public MaxDistance () {
			super();
		}

		public MaxDistance (int distance) {
			super(distance);
		}

		public static class Handler extends ConfigServerHandler<MaxDistance> {
			@Override
			public void configValueChanged (ServerNetwork network, MaxDistance message, MessageContext ctx) {
				network.setMaxDistance(message.getValue());
			}
		}
	}

	public static class RequestMaxDistance implements EmptyMessageClient<RequestMaxDistance> {
		public RequestMaxDistance () {
		}

		@Override
		public void processMessage (RequestMaxDistance message, MessageContext ctx) {
			MaxDistance packet = new MaxDistance(ConfigHandler.ManifestConfig.MaxDistance);
			Networking.CHANNEL.sendToServer(packet);
		}
	}

	public static class DefaultRoutingType extends ConfigPacket<Boolean> {
		public DefaultRoutingType () {
			super();
		}

		public DefaultRoutingType (boolean value) {
			super(value);
		}

		public static class Handler extends ConfigServerHandler<DefaultRoutingType> {
			@Override
			public void configValueChanged (ServerNetwork network, DefaultRoutingType message, MessageContext ctx) {
				network.setNoNewDefault(message.getValue());
			}
		}
	}

	public static class RequestDefaultRoutingType implements EmptyMessageClient<RequestDefaultRoutingType> {
		public RequestDefaultRoutingType () {
		}

		@Override
		public void processMessage (RequestDefaultRoutingType message, MessageContext ctx) {
			DefaultRoutingType packet = new DefaultRoutingType(ConfigHandler.defaultRoutingNoNewItems);
			Networking.CHANNEL.sendToServer(packet);
		}
	}

	public static class TrovesDispense extends ConfigPacket<Boolean> {
		public TrovesDispense () {
			super();
		}

		public TrovesDispense (Boolean value) {
			super(value);
		}

		public static class Handler extends ConfigServerHandler<TrovesDispense> {
			@Override
			public void configValueChanged (ServerNetwork network, TrovesDispense message, MessageContext ctx) {
				network.setTrovesDispense(message.getValue());
			}
		}
	}

	public static class RequestTrovesDispense implements EmptyMessageClient<RequestTrovesDispense> {
		public RequestTrovesDispense () {
		}

		@Override
		public void processMessage (RequestTrovesDispense message, MessageContext ctx) {
			TrovesDispense packet = new TrovesDispense(ConfigHandler.trovesDispense);
			Networking.CHANNEL.sendToServer(packet);
		}
	}
}
