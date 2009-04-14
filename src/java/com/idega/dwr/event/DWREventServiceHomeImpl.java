package com.idega.dwr.event;


import javax.ejb.CreateException;

import com.idega.business.IBOHomeImpl;

public class DWREventServiceHomeImpl extends IBOHomeImpl implements DWREventServiceHome {

	private static final long serialVersionUID = -9060952409939359894L;

	@Override
	public Class<? extends DWREventService> getBeanInterfaceClass() {
		return DWREventService.class;
	}

	public DWREventService create() throws CreateException {
		return (DWREventService) super.createIBO();
	}
}