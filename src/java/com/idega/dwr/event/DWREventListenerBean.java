package com.idega.dwr.event;

public class DWREventListenerBean implements DWREventListener {

	private DWREvent event;
	
	public boolean processEvent(DWREvent event) {
		//	TOOD: add real logic
		
		System.out.println("Got event: " + event);
		return true;
	}

	public DWREvent getEvent() {
		return event;
	}

	public void setEvent(DWREvent event) {
		this.event = event;
	}
	
	
}
