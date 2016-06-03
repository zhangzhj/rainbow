package net.rainbow.web;

import net.rainbow.utils.ExceptionUtils;

public class NestedException extends RuntimeException {
	private static final long serialVersionUID = -4311766097600665097L;

	public NestedException(String message) {
		super(message);
	}

	public NestedException(Throwable throwable) {
		super(throwable);
	}

	public NestedException(String message, Throwable throwable) {
		super(message, throwable);
	}

	@Override
	public String getMessage() {
		return ExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

	@Override
	public Throwable getCause() {
		return super.getCause();
	}

	public Throwable getRootCause() {
		Throwable rootCause = null;
		for (Throwable cause = getCause(); cause != null && cause != rootCause; cause = cause
				.getCause())
			rootCause = cause;

		return rootCause;
	}

	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return ((Throwable) (rootCause == null ? this : rootCause));
	}
}
