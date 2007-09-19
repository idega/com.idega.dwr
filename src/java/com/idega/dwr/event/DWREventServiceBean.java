package com.idega.dwr.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultScriptSession;

import com.idega.business.IBOServiceBean;
/**
 * A service bean for controlling the registration, fireing and flow of events to and from DWR enabled modules and pages.
 * It should handle three cases:
 * Modules use Mootools' event system to register themselves all listeners on the client side
 * OR DWREventService registers them for them, one listener for all the events
 *
 *Listeners can be global or single page / identifier
 *
 * DWREventService grabs/receives events from the client side and passes it on to the server side
 * The DWREventService also sends events either to a single page or all pages.
 * 
 * 0. Registering of listeners
 * 1. Client call generates an event to be sent to the listeners
 * 2. Serverside event is sent to all listeners (serverside,clientside)
 * 3. 
 * @author eiki
 *
 */
//TODO enable Class (style) registration of listeners with bean references, beanId, class...
public class DWREventServiceBean extends IBOServiceBean implements DWREventService {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(DWREventServiceBean.class);	
	
	private LinkedList<DWREventListener> listeners = new LinkedList<DWREventListener>();
	

	/**
	 * Fires the event to a single browser (current page) and the listeners registered on the current page
	 * @param event
	 * @return
	 */
	public DWREvent fireEvent(DWREvent event) {
		for (Iterator dwrListeners = listeners.iterator(); dwrListeners.hasNext();) {
			DWREventListener listener = (DWREventListener) dwrListeners.next();
			if(!listener.processEvent(event)){
				event.setResult(false);
				return event;
			}
		}
		
		event.setResult(true);
		
		return event;
	}
	
	public DWREvent fireEventToAllSessions(DWREvent event){
		//todo implement
		return fireEvent(event);
	}
	
	
		
	/**
	 * Updates particular comments block in client's browser(s)
	 * @param uri - link to comments
	 * @param id - comments group id
	 * @return
	 */
	public boolean getCommentsForAllPages(String uri, String id) {
		return executeScriptForAllPages(getScriptForCommentsList(uri, id));
	}
	
	private ScriptBuffer getScriptForCommentsList(String uri, String id) {
		ScriptBuffer script = new ScriptBuffer();
		script = new ScriptBuffer("getCommentsCallback(").appendData(uri).appendScript(", ").appendData(id);
		script.appendScript(", ").appendData(uri).appendScript(");");
		return script;
	}
	
	private Collection getAllCurrentPageSessions() {
		WebContext wctx = WebContextFactory.get();
		if (wctx == null) {
			return null;
		}
		Collection pages = wctx.getScriptSessionsByPage(wctx.getCurrentPage());
		if (pages == null) {
			return null;
		}
		log.info("Found JavaScript sessions on same page ('"+wctx.getCurrentPage()+"'): " + pages.size());

		return pages;
	}
	
	private boolean executeScriptForAllPages(ScriptBuffer script) {
		Collection allPages = getAllCurrentPageSessions();
		if (allPages == null) {
			return false;
		}
		Object o = null;
		DefaultScriptSession session = null;
		for (Iterator it = allPages.iterator(); it.hasNext(); ) {
			o = it.next();
			if (o instanceof DefaultScriptSession) {
	            session = (DefaultScriptSession) o;
	            session.addScript(script);
	        }
		}
		return true;
	}
	
	/**
	 * Closes loading layer in client's browser
	 */
	private void closeLoadingMessage() {
		ScriptBuffer script = new ScriptBuffer("closeLoadingMessage();");
		executeScriptForAllPages(script);
	}


	
	/**
	 * The method to register listeners
	 * @param listener
	 */
	public void registerListener(DWREventListener listener) {
		//this might not be enoug to ensure only one listener of a certain type
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	/**
	 * The method to register a javascript listener through DWR
	 * @param listener
	 * */
	public void registerListenerBean(DWREventListenerBean listener) {
		registerListener(listener);
	}
	
	public void registerListenerByEventType(String eventType, DWREventListener listener) {
		listeners.add(listener);	
	}
	
	public void registerListenerBeanByEventType(String eventType, DWREventListenerBean listener) {
		registerListenerByEventType(eventType,listener);
	}
	
	
}