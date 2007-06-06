function executeEventsTest() {
	var source = 'source';
	var eventType = 'refresh_object';
	var eventData = new Object();
	eventData.myKey = 'myValue';
	var sendToAllSessionsOnPage = true;
	var ourEvent = new DWREvent(source, eventType, eventData, sendToAllSessionsOnPage);
	
	DWREventService.fireEvent(ourEvent, fireEventCallback);
	DWREventService.fireEventToAllSessions(ourEvent, fireEventToAllSessionsCallback);
	
	DWREventService.registerListener(new DWREventListenerBean(ourEvent), registerListenerCallback);
	DWREventService.registerListenerByEventType('custom_type', new DWREventListenerBean(ourEvent), registerListenerByEventTypeCallback);
}

function DWREvent(source/*String*/, eventType/*String*/, eventData/*Map*/, sendToAllSessionsOnPage/*boolean*/) {
	this.source = source;
	this.eventType = eventType;
	this.eventData = eventData;
	this.sendToAllSessionsOnPage = sendToAllSessionsOnPage;
}

function DWREventListenerBean(event/*DWREvent*/) {
	this.event = event;
}

function fireEventCallback(result) {
	alert(result);
}

function fireEventToAllSessionsCallback(result) {
	alert(result);
}

function registerListenerCallback() {
	alert('listener registered')
}

function registerListenerByEventTypeCallback() {
	alert('custom type event listener registered');
}