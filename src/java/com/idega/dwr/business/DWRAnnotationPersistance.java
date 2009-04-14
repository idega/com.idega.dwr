package com.idega.dwr.business;

import com.idega.business.SpringBeanName;

/**
 * This interface works as "flag" for DWR servlet to check all implementations of this interface and scan for DWR annotations
 * 
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2009/04/14 13:59:03 $ by: $Author: valdas $
 */

@SpringBeanName("dwrAnnotationUserBean")
public interface DWRAnnotationPersistance {}