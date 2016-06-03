package net.rainbow.utils;

import javax.resource.ResourceException;

import net.rainbow.web.NestedException;

public class ExceptionUtils {
	public static String buildMessage(String message, Throwable cause) {
		if (cause != null) {
			StringBuffer buf = new StringBuffer();
			if (message != null)
				buf.append(message).append("; ");
			buf.append("nested exception is [").append(cause.getMessage())
					.append("]");
			return buf.toString();
		} else {
			return message;
		}
	}

	public static ResourceException buildMappingException(String mapping) {
		return new ResourceException("mapping [" + mapping
				+ "] is not config in qdevelop-config.xml");
	}

	public static ResourceException buildMappingException(Throwable t,
			String mapping) {
		return new ResourceException("mapping [" + mapping
				+ "] is not config in qdevelop-config.xml", t);
	}

	public static ResourceException buildActionException(String action) {
		return new ResourceException("Unable to find action [" + action + "]");
	}

	public static ResourceException buildActionException(Throwable t,
			String action) {
		return new ResourceException("Unable to find action [" + action + "]",
				t);
	}

	public static ResourceException buildMethodException(Throwable t,
			String action, String method) {
		return new ResourceException("Unable to find method[" + method
				+ "] in action[" + action + "]", t);
	}

	public static ResourceException buildMethodException(String action,
			String method) {
		return new ResourceException("Unable to find method (" + method
				+ ") in action [" + action + "]");
	}

	public static ResourceException buildResourceException(String resource) {
		return new ResourceException("Unable to find resource (" + resource
				+ ")");
	}

	public static ResourceException buildResourceException(Throwable t,
			String resource) {
		return new ResourceException("Unable to find resource (" + resource
				+ ")", t);
	}

	public static NestedException buildNestedException(String message) {
		return new NestedException(message);
	}

	public static NestedException buildNestedException(String message,
			Throwable t) {
		return new NestedException(message, t);
	}

	public static NestedException buildNestedException(Throwable t) {
		return new NestedException(t);
	}
	
	public static Throwable getRootCause(Throwable throwable) {
		Throwable rootCause = null;
		for (Throwable cause = throwable.getCause(); cause != null && cause != rootCause; cause = cause
				.getCause())
			rootCause = cause;
		if(rootCause == null){
			return throwable;
		}
		return rootCause;
	}
}
