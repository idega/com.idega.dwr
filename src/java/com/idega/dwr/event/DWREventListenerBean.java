package com.idega.dwr.event;

import java.util.Iterator;

import org.directwebremoting.ScriptBuffer;

import com.idega.dwr.reverse.ScriptCaller;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

public class DWREventListenerBean implements DWREventListener {

	private DWREvent event;
	
	public boolean processEvent(DWREvent event) {
		ScriptBuffer script = null;
		if (event.getEventData() == null || event.getEventData().isEmpty()) {
			script = new ScriptBuffer(StringUtil.isEmpty(event.getJavaScriptFunctionName()) ? event.getSource() : event.getJavaScriptFunctionName());
		}
		else {
			script = new ScriptBuffer(event.getJavaScriptFunctionName()).appendScript("(");
			for (Iterator<String> dataIter = event.getEventData().values().iterator(); dataIter.hasNext();) {
				script.appendData(dataIter.next());
				if (dataIter.hasNext()) {
					script.appendScript(CoreConstants.COMMA);
				}
			}
			script.appendScript(");");
		}
		
		ScriptCaller scriptCaller = new ScriptCaller(script, event.isSendToAllSessionsOnPage());
		
		if (event.isUseThreading()) {
			Thread thread = new Thread(scriptCaller);
			thread.start();
		} else {
			scriptCaller.run();
		}
		
		//	TODO: execute nested events recursively?
		
		return true;
	}

	public DWREvent getEvent() {
		return event;
	}

	public void setEvent(DWREvent event) {
		this.event = event;
	}
	
	
}
