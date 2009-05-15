/*
 * $Id: IBOCreator.java,v 1.4 2009/05/15 07:25:01 valdas Exp $
 * Created on Apr 19, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.dwr.create;

import org.directwebremoting.create.NewCreator;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.business.IBOSession;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.UnavailableIWContext;
import com.idega.presentation.IWContext;

/**
 * This is a custom Creator for DWR to create IdegaWeb "EJB" business objects.<br>
 * Their scope is either application (service beans) or session (session beans)<br>
 * If no scope is defined we try to instanciate a service bean.<br>
 * This custom creator is registered in the DWRExtendedServlet configure method.<br>
 * The creator key is "ibo"
 * 
 *  Last modified: $Date: 2009/05/15 07:25:01 $ by $Author: valdas $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.4 $
 */
public class IBOCreator extends NewCreator {

	@Override
	public Object getInstance() throws InstantiationException {
		String scope = getScope();
		if (scope == null) {
			scope = APPLICATION;
		}
		
		if (APPLICATION.equals(scope)) {
			try {
				return IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(), getType());
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		else if (SESSION.equals(scope)) {
			try {
				IWContext iwc = IWContext.getInstance();
				return IBOLookup.getSessionInstance(iwc, getType().asSubclass(IBOSession.class));
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
			catch (UnavailableIWContext e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	@Override
	public Class<? extends IBOService> getType() {
		Class<?> theClass = super.getType();
		
		try {
			return theClass.asSubclass(IBOSession.class);
		} catch(ClassCastException e) {
			try {
				return theClass.asSubclass(IBOService.class);
			} catch(ClassCastException c) {
				throw new RuntimeException("Class must be " + IBOSession.class + " or " + IBOService.class + " type!", c);
			}
		}
	}

}
