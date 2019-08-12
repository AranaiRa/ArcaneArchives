package com.aranaira.arcanearchives.immanence;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.*;

public class ImmanenceBus implements IImmanenceBus {
	private List<IImmanenceSubscriber> subscribers = new ArrayList<>();
	private List<ImmanenceTicket> ticketList_primary = new ArrayList<>();
	private Map<UUID, ImmanenceTicket> ticketUUIDReference_generated = new HashMap<>();
	private Int2ObjectOpenHashMap<ImmanenceTicket> ticketReference_generated = new Int2ObjectOpenHashMap<>();

	boolean reset = false;

	private void regenerateLists () {
		if (reset) {
			reset = false;
			ticketReference_generated.clear();
			ticketUUIDReference_generated.clear();

			ticketList_primary.sort((o1, o2) -> Integer.compare(o2.getPriority(), o1.getPriority()));

			int totalImmanenceAvailable = getIncomingImmanence();
			int i = 0;
			boolean exhausted = false;
			List<ImmanenceTicket> inform = new ArrayList<>();

			for (ImmanenceTicket ticket : ticketList_primary) {
				if (ticketUUIDReference_generated.containsKey(ticket.getUUID())) {
					throw new DuplicateTicketIdException(ticket.getUUID());
				}
				IntArrayList ticketIds = new IntArrayList();
				for (int x = 0; x < ticket.getTicketsUsed(); x++) {
					if (x + i > totalImmanenceAvailable) {
						exhausted = true;
						break;
					} else {
						ticketIds.add(x + i);
						ticketReference_generated.put(x + i, ticket);
					}
				}
				if (exhausted) {
					ticket.allocateTickets(null);
					inform.add(ticket);
				} else {
					ticket.allocateTickets(ticketIds);
				}
				ticketUUIDReference_generated.put(ticket.getUUID(), ticket);
			}
		}
	}

	private Int2ObjectOpenHashMap<ImmanenceTicket> getTicketReference () {
		regenerateLists();
		return ticketReference_generated;
	}

	private Map<UUID, ImmanenceTicket> getUUIDReference () {
		regenerateLists();
		return ticketUUIDReference_generated;
	}

	private List<ImmanenceTicket> getTicketList () {
		return ticketList_primary;
	}

	public int getIncomingImmanence () {
		return 1000;
	}
}
