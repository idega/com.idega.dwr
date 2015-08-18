package uk.ltd.getahead.dwr;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;

/**
 * This is a bridge class to keep support for DWR version 2.x
 *
 * @author valdas
 *
 */
public class ExecutionContext {

	private static ExecutionContext instance = null;

	public static ExecutionContext get() {
		if (instance == null) {
			instance = new ExecutionContext();
		}

		return instance;
	}

	public HttpSession getSession() {
		return WebContextFactory.get().getSession();
	}

}