package com.idega.dwr.event;

import com.idega.business.IBOService;

public interface DWREventService extends IBOService {
	
	public void registerListener(DWREventListenerBean listener);
	
	public void registerListenerByEventType(String eventType, DWREventListenerBean listener);
	
	public boolean fireEvent(DWREvent event);
	
	public boolean fireEventToAllSessions(DWREvent event);
	
}