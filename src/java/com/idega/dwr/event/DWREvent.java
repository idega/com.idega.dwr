package com.idega.dwr.event;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The default event object you can construct and send with DWR or by using DWREventService, extends java.util.EventObject
 * @author eiki
 *
 */
public class DWREvent implements Serializable{ //extends EventObject{ cannot extend directly because source needs to be Object and dwr cannot translate that, perhaps create a wrapper object for dwr client event

	public static final String EVENT_NO_SOURCE = "NOSOURCE";
	private static final long serialVersionUID = -1282376140723922324L;
	
	private Map<String, String> eventData;
	private String source;
	private String eventType;
	private boolean sendToAllSessionsOnPage = false;
	private final Date eventDate = new Date();//timestamp
	private boolean result = false;
	private List<DWREvent> nestedEvents = new LinkedList<DWREvent>();

	public DWREvent(){
		this.source = EVENT_NO_SOURCE;
	}
	
	public Date getEventDate() {
		return eventDate;
	}

	public DWREvent(String source, String eventType, Map<String, String> eventData) {
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

	public void setEventData(Map<String, String> eventData) {
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
	
	public String toString(){
		StringBuffer buf = new StringBuffer(source).append(':')
		.append(eventType).append(':')
		.append(eventDate).append(':')
		.append(sendToAllSessionsOnPage).append(':')
		.append(eventData);
		return buf.toString();
	}	
	
	/**
	 * Set by DWREventService, false mean the event processing somehow failed
	 * @param result
	 */
	public void setResult(boolean result){
		this.result = result;
	}
	
	/**
	 * @return false mean the event processing somehow failed
	 */
	public boolean getResult(){
		return this.result;
	}

	public List<DWREvent> getNestedEvents() {
		return nestedEvents;
	}

	public void setNestedEvents(List<DWREvent> nestedEvents) {
		this.nestedEvents = nestedEvents;
	}
	
	public void addNestedEvent(DWREvent event){
		this.nestedEvents.add(event);
	}
}