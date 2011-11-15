package com.idega.dwr.reverse;

import java.util.Collection;
import java.util.logging.Logger;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.extend.ScriptSessionManager;

import com.idega.event.ScriptCallerInterface;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * A class that can be used in non-DWR thread to send script to browser. It should be used only if
 * you encounter problem with ScriptCaller (because this functionality may be implemented
 * by ScriptCaller later). If this class does not find any ScriptSessions it does nothing.
 * @author alex
 *
 */
public class ScriptCallerWithoutDWRThread implements ScriptCallerInterface {

	private static final Logger LOGGER = Logger.getLogger(ScriptCaller.class.getName());

	private ScriptBuffer script = null;;

	private String uri = null;

	private String sessionId = null;

	private ScriptSessionManager manager = null;

	public ScriptCallerWithoutDWRThread(){
	}

	public void setScript(String script) {
		this.script = new ScriptBuffer(script);
	}

	public void run(){
		if (this.script == null) {
			LOGGER.warning("Script is undefined!");
			return;
		}

		ScriptSessionManager manager = this.getManager();
		Collection<? extends ScriptSession> scriptSessions = StringUtil.isEmpty(getSessionId()) ?
				manager.getAllScriptSessions() : manager.getScriptSessionsByHttpSessionId(getSessionId());

		if (ListUtil.isEmpty(scriptSessions))
			return;

		//sending script
		for (ScriptSession scriptSession: scriptSessions) {
			scriptSession.addScript(script);
		}
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ScriptBuffer getScript() {
		return script;
	}

	public void setScript(ScriptBuffer script) {
		this.script = script;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ScriptSessionManager getManager() {
		if(this.manager == null){
			Container container = ServerContextFactory.get().getContainer();
			this.manager = container.getBean(ScriptSessionManager.class);
		}
		return this.manager;
	}

	public void setManager(ScriptSessionManager manager) {
		this.manager = manager;
	}
	
	public String getUri() {
		return uri;
	}

	public void executeScript(String httpSessionId, String script) {
		setSessionId(httpSessionId);
		setScript(script);
		run();
	}
}