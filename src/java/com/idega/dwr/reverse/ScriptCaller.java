package com.idega.dwr.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.idega.util.StringUtil;

/**
 * Implementation of DWR reverse Ajax. Executing provided JavaScript in page(s)
 *
 * @param WebContext
 * @param ScriptBuffer - JavaScript action to execute
 * @param uri - page where to execute action
 * @param invokeOriginalPage - if TRUE when action is executed in event's original page
 * @param task - possibility to provide (and execute) code block to execute before scripts are fired to page(s)
 *
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2009/04/14 14:22:04 $ by: $Author: valdas $
 */
public class ScriptCaller implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(ScriptCaller.class.getName());

	private WebContext webContext;

	private ScriptBuffer script;

	private String uri;

	private boolean invokeOriginalPage;

	private Runnable task;

	private Collection<String> httpSessionsServed;

	public ScriptCaller(String javaScriptAction) {
		this(WebContextFactory.get(), new ScriptBuffer(javaScriptAction), Boolean.TRUE);
	}

	public ScriptCaller(String javaScriptAction, boolean invokeOriginalPage) {
		this(WebContextFactory.get(), new ScriptBuffer(javaScriptAction), null, invokeOriginalPage);
	}

	public ScriptCaller(ScriptBuffer script, boolean invokeOriginalPage) {
		this(WebContextFactory.get(), script, null, invokeOriginalPage);
	}

	public ScriptCaller(WebContext webContext, ScriptBuffer script) {
		this(webContext, script, null, Boolean.TRUE);
	}

	public ScriptCaller(WebContext webContext, ScriptBuffer script, boolean invokeOriginalPage) {
		this(webContext, script, null, invokeOriginalPage);
	}

	public ScriptCaller(WebContext webContext, ScriptBuffer script, String uri, boolean invokeOriginalPage) {
		this(webContext, script, uri, invokeOriginalPage, null);
	}

	public ScriptCaller(WebContext webContext, ScriptBuffer script, String uri, boolean invokeOriginalPage, Runnable task) {
		this.webContext = webContext;
		this.script = script;
		this.uri = uri;
		this.invokeOriginalPage = invokeOriginalPage;
		this.task = task;
	}

	private void sendScriptToPage() {
		addTaskForCurrentPageScriptSessions(new ScriptExecutor(this));
	}

	private boolean addTaskForCurrentPageScriptSessions(Runnable task) {
		WebContext wctx = getWebContext();
		if (wctx == null) {
			return Boolean.FALSE;
		}

		String currentPage = null;
		try {
			currentPage = wctx.getCurrentPage();
		} catch(Exception e) {}
		if (StringUtil.isEmpty(currentPage)) {
			currentPage = uri;
		}

		if (isInvokeOriginalPage() && StringUtil.isEmpty(currentPage)) {
			try {
				Browser.withAllSessions(task);
			} catch(Exception e) {
				LOGGER.log(Level.SEVERE, "Error executing task: " + task, e);
				return Boolean.FALSE;
			}
		} else {
			if (StringUtil.isEmpty(currentPage)) {
				LOGGER.warning("Unable to resolve a page for task: " + task);
				return Boolean.FALSE;
			}

			try {
				Browser.withPage(currentPage, task);
			} catch(Exception e) {
				LOGGER.log(Level.SEVERE, "Error executing task: " + task + " for a page: " + currentPage, e);
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	WebContext getWebContext() {
		return webContext;
	}

	public void run() {
		if (script == null) {
			return;
		}
		sendScriptToPage();
	}

	ScriptBuffer getScript() {
		return script;
	}

	String getUri() {
		return uri;
	}

	Runnable getTask() {
		return task;
	}

	boolean isInvokeOriginalPage() {
		return invokeOriginalPage;
	}

	public void setTask(Runnable task) {
		this.task = task;
	}

	public void setWebContext(WebContext webContext) {
		this.webContext = webContext;
	}

	public void setScript(ScriptBuffer script) {
		this.script = script;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setInvokeOriginalPage(boolean invokeOriginalPage) {
		this.invokeOriginalPage = invokeOriginalPage;
	}

	public Collection<String> getHttpSessionsServed() {
		return httpSessionsServed;
	}

	protected void setHttpSessionsServed(Collection<String> httpSessionsServed) {
		this.httpSessionsServed = httpSessionsServed;
	}

	protected void addHttpSessionServed(String httpSessionId) {
		if (StringUtil.isEmpty(httpSessionId)) {
			return;
		}

		if (httpSessionsServed == null) {
			httpSessionsServed = new ArrayList<String>();
		}

		if (httpSessionsServed.contains(httpSessionId)) {
			return;
		}

		httpSessionsServed.add(httpSessionId);
	}

}
