package com.idega.dwr.event;

import com.idega.business.IBOService;

public interface DWREventService extends IBOService {
	
	public void registerListenerBean(DWREventListenerBean listener);
	
	public void registerListener(DWREventListener listener);
	
	public void registerListenerBeanByEventType(String eventType, DWREventListenerBean listener);
	
	public void registerListenerByEventType(String eventType, DWREventListener listener);
	
	public DWREvent fireEvent(DWREvent event);
	
	public DWREvent fireEventToAllSessions(DWREvent event);
	
}