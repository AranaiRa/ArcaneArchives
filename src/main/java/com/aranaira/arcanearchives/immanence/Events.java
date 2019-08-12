package com.aranaira.arcanearchives.immanence;

public class Events {
	public interface IImmanenceEvent {

	}

	public static class GenerateEvent implements IImmanenceEvent {

	}

	public static class ConsumeEvent implements IImmanenceEvent {

	}

	public static class LateConsumeEvent extends ConsumeEvent {

	}
}
