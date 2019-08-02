package com.aranaira.arcanearchives.immanence;

import java.util.UUID;

public interface IImmanenceTicket {
	int getTicketsUsed ();
	int getPriority ();

	UUID getUUID ();
}
