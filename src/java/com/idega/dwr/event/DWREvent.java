package com.idega.dwr.event;

import java.io.Serializable;
import java.util.Map;

/**
 * The default event object you can construct and send with DWR or by using DWREventService, extends java.util.EventObject
 * @author eiki
 *
 */
public class DWREvent implements Serializable{ //extends EventObject{ cannot extend directly because source needs to be Object and dwr cannot translate that, perhaps create a wrapper object for dwr client event

	public static final String EVENT_NO_SOURCE = "NOSOURCE";
	private static final long serialVersionUID = -1282376140723922324L;
	Map eventData;
	String source;
	String eventType;
	boolean sendToAllSessionsOnPage = false;

	public DWREvent(){
		this.source = EVENT_NO_SOURCE;
	}
	
	public DWREvent(String source, String eventType, Map eventData) {
		this.source = source;
		this.eventType = eventType;
		this.eventData = eventData;
	}
	
	/**
	 * @return The extra data the event might have added
	 */
	public Map getEventData() {
		return eventData;
	}


	public void setEventData(Map eventData) {
		this.eventData = eventData;
	}

	/**
	 * @return The (String) type of an event that occured like a "GroupSelect" or "ReloadPage" etc.
	 */
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	
	public boolean isSetSendToAllSessionsOnPage() {
		return sendToAllSessionsOnPage;
	}

	public void setToSendToAllSessionsOnPage(boolean sendToAllSessionsOnPage) {
		this.sendToAllSessionsOnPage = sendToAllSessionsOnPage;
	}

	public boolean isSendToAllSessionsOnPage() {
		return sendToAllSessionsOnPage;
	}

	public void setSendToAllSessionsOnPage(boolean sendToAllSessionsOnPage) {
		this.sendToAllSessionsOnPage = sendToAllSessionsOnPage;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
	
}
