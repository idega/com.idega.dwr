package com.idega.dwr.event;


import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.business.IBOHome;

public interface DWREventServiceHome extends IBOHome {
	public DWREventService create() throws CreateException, RemoteException;
}