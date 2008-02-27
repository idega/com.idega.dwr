package com.idega.dwr.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class ScriptCaller implements Runnable {
	
	private ScriptBuffer script = null;
	private String uri = null;
	private boolean sendToAllSessions = false;
	
	public ScriptCaller(ScriptBuffer script) {
		this.script = script;
	}
	
	public ScriptCaller(ScriptBuffer script, boolean sendToAllSessions) {
		this(script);
		
		this.sendToAllSessions = sendToAllSessions;
	}
	
	public ScriptCaller(ScriptBuffer script, String uri, boolean sendToAllSessions) {
		this(script);
		
		this.uri = uri;
		this.sendToAllSessions = sendToAllSessions;
	}
	
	private void sendScript() {
		ScriptSession ss = WebContextFactory.get().getScriptSession();
		
		List<ScriptSession> allPages = getAllCurrentPageSessions();
		if (allPages == null) {
			return;
		}
		
		ScriptSession session = null;
		for (int i = 0; i < allPages.size(); i++) {
			session = allPages.get(i);
			if (sendToAllSessions) {
				session.addScript(script);
			}
			else {
				if (ss != null && !session.equals(ss)) {
					session.addScript(script);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<ScriptSession> getAllCurrentPageSessions() {
		WebContext wctx = WebContextFactory.get();
		if (wctx == null) {
			return null;
		}
		
		Collection currentPageSessions = null;
		String currentPage = wctx.getCurrentPage();
		if (currentPage != null) {
			currentPageSessions = wctx.getScriptSessionsByPage(currentPage);
		}
		if (currentPageSessions == null) {
			currentPageSessions = new ArrayList();
		}
		
		if (uri != null) {
			//	Looking for sessions in custom uri
			Collection pages = wctx.getScriptSessionsByPage(uri);
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

	public void run() {
		if (script == null) {
			return;
		}
		sendScript();
	}

}
