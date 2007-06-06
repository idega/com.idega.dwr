package com.idega.dwr.event;

import java.util.EventListener;

public interface DWREventListener extends EventListener{
	
	public boolean processEvent(DWREvent event);
	
}
