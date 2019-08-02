package com.aranaira.arcanearchives.immanence;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.UUID;

public class ImmanenceTicket implements IImmanenceTicket {
	private int ticketsUsed;
	private UUID ticketId;
	private int priority;
	private IntArrayList ticketAllocation = new IntArrayList();

	public ImmanenceTicket (int priority, int ticketsUsed, UUID ticketId) {
		this.ticketsUsed = ticketsUsed;
		this.ticketId = ticketId;
		this.priority = priority;
	}

	public boolean met () {
		return ticketAllocation.size() == ticketsUsed;
	}

	public void allocateTickets (IntArrayList tickets) {
		ticketAllocation.clear();
		if (tickets != null) {
			ticketAllocation.addAll(tickets);
		}
	}

	@Override
	public int getPriority () {
		return priority;
	}

	@Override
	public int getTicketsUsed () {
		return ticketsUsed;
	}

	@Override
	public UUID getUUID () {
		return ticketId;
	}
}
