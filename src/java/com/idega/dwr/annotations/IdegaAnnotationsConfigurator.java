package com.idega.dwr.annotations;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.directwebremoting.Container;
import org.directwebremoting.annotations.AnnotationsConfigurator;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.dwr.business.DWRAnnotationPersistance;

/**
* Idega's extension for {@link AnnotationsConfigurator}. Checks for Spring beans that are marked as DWR annotation "users".
* This helps to avoid filling info in web.xml (providing class names of classes that uses DWR annotations)
* 
* @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
* @version $Revision: 1.2 $
*
* Last modified: $Date: 2009/05/08 08:45:47 $ by: $Author: valdas $
*/
public class IdegaAnnotationsConfigurator extends AnnotationsConfigurator {

	private ServletContext servletContext;

	@SuppressWarnings("unchecked")
	@Override
	protected Set<Class<?>> getClasses(Container container) {
		Map beans = null;
		try {
			beans = WebApplicationContextUtils.getWebApplicationContext(servletContext).getBeansOfType(DWRAnnotationPersistance.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Set<Class<?>> dwrBeans = new HashSet<Class<?>>();
		if (beans == null || beans.isEmpty()) {
			return dwrBeans;
		}
		
		Class<?> theClass = null;
		for (Object bean: beans.values()) {
			theClass = bean.getClass();
			if (!dwrBeans.contains(theClass)) {
				dwrBeans.add(theClass);
			}
		}
		
		return dwrBeans;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
