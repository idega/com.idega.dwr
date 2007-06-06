package com.idega.dwr.event;

import com.idega.business.IBOService;

public interface DWREventService extends IBOService {
	
	public void registerListener(DWREventListener listener);
	
	public void registerListenerByEventType(String eventType, DWREventListener listener);
	
	public boolean fireEvent(DWREvent event);
	
}