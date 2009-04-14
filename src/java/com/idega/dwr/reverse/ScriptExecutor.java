package com.idega.dwr.reverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.extend.ScriptSessionManager;

import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * Executes (fires) script to page(s)
 * 
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2009/04/14 14:22:04 $ by: $Author: valdas $
 */
public class ScriptExecutor implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(ScriptExecutor.class.getName());
	
	private ScriptCaller scriptCaller;
	private ScriptSessionManager scriptSessionManager;
	
	protected ScriptExecutor(ScriptCaller scriptCaller) {
		this.scriptCaller = scriptCaller;
	}
	
	public void run() {
		fireScript();
	}
	
	private ScriptSessionManager getScriptSessionManager() {
		if (scriptSessionManager == null) {
			try {
				scriptSessionManager = scriptCaller.getWebContext().getContainer().getBean(ScriptSessionManager.class);
			} catch(Exception e) {
				LOGGER.log(Level.SEVERE, "Error getting: " + ScriptSessionManager.class, e);
			}
		}
		return scriptSessionManager;
	}
	
	private void fireScript() {
		if (scriptCaller == null) {
			LOGGER.warning("Script caller is undefined!");
			return;
		}
		if (scriptCaller.getScript() == null) {
			LOGGER.warning("Script is undefined!");
			return;
		}
		
		Runnable task = scriptCaller.getTask();
		if (task != null) {
			task.run();
		}
		
		Collection<ScriptSession> allSessions = getAllScriptSessionsForPages(Arrays.asList(
				scriptCaller.getWebContext().getCurrentPage(),
				StringUtil.isEmpty(scriptCaller.getUri()) ? CoreConstants.EMPTY : scriptCaller.getUri()
		));
		if (ListUtil.isEmpty(allSessions)) {
			LOGGER.info("No DWR script sessions found for script: " + scriptCaller.getScript() + " in page: " + scriptCaller.getWebContext().getCurrentPage());
			return;
		}
		
		ScriptSession currentSession = scriptCaller.getWebContext().getScriptSession();
		
		ScriptBuffer script = scriptCaller.getScript();
		
		String sessionId = null;
		List<String> sessionsWithScripts = new ArrayList<String>();
		for (ScriptSession session: allSessions) {
			sessionId = session.getId();
			
			if (!session.isInvalidated() && !sessionsWithScripts.contains(sessionId)) {
				sessionsWithScripts.add(sessionId);
				
				if (scriptCaller.isSendToAllSessions()) {
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
	
	private Collection<ScriptSession> getAllScriptSessionsForPages(List<String> pages) {
		if (ListUtil.isEmpty(pages)) {
			return null;
		}
		
		Collection<ScriptSession> allScriptSessions = getAllScriptSessions();
		if (ListUtil.isEmpty(allScriptSessions)) {
			return null;
		}
		
		List<ScriptSession> filteredSessions = new ArrayList<ScriptSession>();
		for (ScriptSession scriptSession: allScriptSessions) {
			if (pages.contains(scriptSession.getPage())) {
				filteredSessions.add(scriptSession);
			}
		}
		
		return filteredSessions;
	}
	
	private Collection<ScriptSession> getAllScriptSessions() {
		try {
			return getScriptSessionManager().getAllScriptSessions();
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Error getting DWR script sessions", e);
		}
		return null;
	}

}
