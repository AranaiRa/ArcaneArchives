package com.aranaira.arcanearchives.immanence;

import java.util.UUID;

public class Actions {
	public static abstract class ImmanenceAction {
		protected UUID uuid;

		public ImmanenceAction (UUID uuid) {
			this.uuid = uuid;
		}

		public UUID getUuid () {
			return uuid;
		}
	}

	public static class ReturnAction extends ImmanenceAction {
		public ReturnAction (UUID uuid) {
			super(uuid);
		}
	}

	public static class RequestAction extends ImmanenceAction {
		protected int immanenceCount;

		public RequestAction (UUID uuid, int immanenceCount) {
			super(uuid);
			this.immanenceCount = immanenceCount;
		}

		public int getImmanenceCount () {
			return immanenceCount;
		}
	}

	public static class JoinAction extends RequestAction {
		public JoinAction (UUID uuid, int immanenceCount) {
			super(uuid, immanenceCount);
		}
	}
}
