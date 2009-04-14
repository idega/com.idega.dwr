package com.idega.dwr.event;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.idega.business.IBOServiceBean;
import com.idega.util.ListUtil;
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

	private static final Logger log = Logger.getLogger(DWREventServiceBean.class.getName());
	
	private List<DWREventListener> listeners = new LinkedList<DWREventListener>();

	/**
	 * Fires the event to a single browser (current page) and the listeners registered on the current page
	 * @param event
	 * @return
	 */
	public DWREvent fireEvent(DWREvent event) {
		if (ListUtil.isEmpty(listeners)) {
			log.warning("There are no DWR event listeners registered!");
			return event;
		}
		
		for (DWREventListener listener: listeners) {
			if (!listener.processEvent(event)) {
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