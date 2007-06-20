/*
 * $Id: IBOCreator.java,v 1.1.2.2 2007/06/20 16:07:45 eiki Exp $
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
 *  Last modified: $Date: 2007/06/20 16:07:45 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.1.2.2 $
 */
public class IBOCreator extends NewCreator {

	public Object getInstance() throws InstantiationException {
		
		String scope = getScope();
		if(scope==null){
			scope = APPLICATION;
		}
		
		if(APPLICATION.equals(scope)){
			try {
				return IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(),getType());
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		else if(SESSION.equals(scope)){
			try {
				IWContext iwc = IWContext.getInstance();
				return IBOLookup.getSessionInstance(iwc, getType());
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

}
