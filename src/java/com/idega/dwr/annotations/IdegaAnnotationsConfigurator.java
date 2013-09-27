package com.idega.dwr.annotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.directwebremoting.Container;
import org.directwebremoting.annotations.AnnotationsConfigurator;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

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

	@Override
	protected Set<Class<?>> getClasses(Container container) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(DataTransferObject.class));
		scanner.addIncludeFilter(new AnnotationTypeFilter(RemoteProxy.class));

		Set<Class<?>> dwrBeans = new HashSet<Class<?>>();
		List<String> commonPackages = Arrays.asList("com.idega", "is", "se");
		for (String commonPackage: commonPackages) {
			for (BeanDefinition bd: scanner.findCandidateComponents(commonPackage)) {
				Class<?> theClass = null;
				try {
					theClass = Class.forName(bd.getBeanClassName());
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error loading class: " + bd.getBeanClassName(), e);
				}
				if (theClass != null) {
					dwrBeans.add(theClass);
				}
			}
		}
		return dwrBeans;
	}

}