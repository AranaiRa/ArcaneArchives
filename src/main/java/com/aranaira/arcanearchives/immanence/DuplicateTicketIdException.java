package com.aranaira.arcanearchives.immanence;

import java.util.UUID;

public class DuplicateTicketIdException extends RuntimeException {
	public UUID duplicateId;

	public DuplicateTicketIdException (UUID duplicateId) {
		this.duplicateId = duplicateId;
	}

	public DuplicateTicketIdException (String message, UUID duplicateId) {
		super(message);
		this.duplicateId = duplicateId;
	}

	public DuplicateTicketIdException (String message, Throwable cause, UUID duplicateId) {
		super(message, cause);
		this.duplicateId = duplicateId;
	}

	public DuplicateTicketIdException (Throwable cause, UUID duplicateId) {
		super(cause);
		this.duplicateId = duplicateId;
	}

	public DuplicateTicketIdException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, UUID duplicateId) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.duplicateId = duplicateId;
	}
}
