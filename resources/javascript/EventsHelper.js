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
	
	DWREventService.registerListenerByEventType('selectGroup', new DWREventListenerBean(ourEvent), registerListenerByEventTypeCallback);
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
	if(result.result){
		window.fireEvent(result.eventType, result);
		var nestedEventsCount = result.nestedEvents.length;
		if(nestedEventsCount>0){
			//nested dwrevents fire each one
			for(i=0;i<nestedEventsCount;i++){
				dwrFireEvent(result.nestedEvents[i]);
			}
		}
	}
	else{
		alert("server side event call returned false, cancelling client event");
	}
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

/**
*Calls DWREventService.fireEvent and then calls your custom callback method or the standard method if undefined. 
*The standard method fires the same event on the client side. To listen to a method use mootools element.addEvents(...)
**/
function dwrFireEvent(dwrEvent, callBackMethod){
	if(callBackMethod){
		DWREventService.fireEvent(dwrEvent, callBackMethod);
	}
	else{
		DWREventService.fireEvent(dwrEvent, fireEventCallback);
	}	
}



