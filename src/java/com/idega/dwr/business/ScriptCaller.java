package com.idega.dwr.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;

public class ScriptCaller implements Runnable {
	
	private WebContext webContext = null;
	
	private ScriptBuffer script = null;
	private String uri = null;
	private boolean sendToAllSessions = false;
	
	public ScriptCaller(WebContext webContext, ScriptBuffer script) {
		this.webContext = webContext;
		this.script = script;
		
		sendToAllSessions = true;
	}
	
	public ScriptCaller(WebContext webContext, ScriptBuffer script, boolean sendToAllSessions) {
		this(webContext, script);
		
		this.sendToAllSessions = sendToAllSessions;
	}
	
	public ScriptCaller(WebContext webContext, ScriptBuffer script, String uri, boolean sendToAllSessions) {
		this(webContext, script);
		
		this.uri = uri;
		this.sendToAllSessions = sendToAllSessions;
	}
	
	private void sendScript() {
		WebContext webContext = getWebContext();
		if (webContext == null) {
			return;
		}
		
		ScriptSession currentSession = webContext.getScriptSession();
		
		List<ScriptSession> allPages = getAllCurrentPageSessions();
		if (allPages == null) {
			return;
		}
		
		ScriptSession session = null;
		String sessionId = null;
		List<String> sessionsWithScripts = new ArrayList<String>();
		for (int i = 0; i < allPages.size(); i++) {
			session = allPages.get(i);
			
			sessionId = session.getId();
			if (!session.isInvalidated() && !sessionsWithScripts.contains(sessionId)) {
				sessionsWithScripts.add(sessionId);
				
				if (sendToAllSessions) {
					session.addScript(script);
				}
				else {
					if (currentSession != null && !session.equals(currentSession)) {
						session.addScript(script);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<ScriptSession> getAllCurrentPageSessions() {
		WebContext webContext = getWebContext();
		if (webContext == null) {
			return null;
		}
		
		Collection currentPageSessions = null;
		String currentPage = webContext.getCurrentPage();
		if (currentPage != null) {
			currentPageSessions = webContext.getScriptSessionsByPage(currentPage);
		}
		if (currentPageSessions == null) {
			currentPageSessions = new ArrayList();
		}
		
		if (uri != null) {
			//	Looking for sessions in custom uri
			Collection pages = webContext.getScriptSessionsByPage(uri);
			if (pages != null) {
				currentPageSessions.addAll(pages);
			}
		}
		
		List<ScriptSession> allPages = new ArrayList<ScriptSession>();
		Object o = null;
		for (Iterator it = currentPageSessions.iterator(); it.hasNext(); ) {
			o = it.next();
			if (o instanceof ScriptSession) {
	            allPages.add((ScriptSession) o);
	        }
		}
		
		return allPages;
	}
	
	private WebContext getWebContext() {
		return webContext;
	}

	public void run() {
		if (script == null) {
			return;
		}
		sendScript();
	}
}