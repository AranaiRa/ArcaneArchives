package com.aranaira.arcanearchives.immanence;

import com.aranaira.arcanearchives.data.NetworkEvent;

import java.util.UUID;

public class ImmanenceTickEvent extends NetworkEvent {
	public ImmanenceTickEvent (UUID ownerNetwork) {
		super(ownerNetwork);
	}

	public static class Pre extends ImmanenceTickEvent {
		public Pre (UUID ownerNetwork) {
			super(ownerNetwork);
		}
	}

	public static class Generate extends ImmanenceTickEvent {
		public Generate (UUID ownerNetwork) {
			super(ownerNetwork);
		}
	}

	public static class Consume extends ImmanenceTickEvent {
		public Consume (UUID ownerNetwork) {
			super(ownerNetwork);
		}
	}

	public static class Post extends ImmanenceTickEvent {
		public Post (UUID ownerNetwork) {
			super(ownerNetwork);
		}
	}
}
