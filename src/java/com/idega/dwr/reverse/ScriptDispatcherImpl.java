package com.idega.dwr.reverse;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogicWrapper;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.component.bean.RenderedComponent;
import com.idega.presentation.IWContext;
import com.idega.reverse.ScriptDispatcher;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

@Scope(BeanDefinition.SCOPE_SINGLETON)
@Service(ScriptDispatcher.BEAN_IDENTIFIER)
public class ScriptDispatcherImpl implements ScriptDispatcher {

	private static final long serialVersionUID = -4635974825724939573L;

	private static final Logger LOGGER = Logger.getLogger(ScriptDispatcherImpl.class.getName());

	@Autowired
	private BuilderLogicWrapper builderLogicWrapper;

	@Override
	public Collection<String> dispatchScript(IWContext iwc, String javaScriptAction) {
		return dispatchScript(iwc, javaScriptAction, Boolean.TRUE);
	}

	@Override
	public Collection<String> dispatchScript(IWContext iwc, String javaScriptAction, boolean invokeOriginalPage) {
		if (StringUtil.isEmpty(javaScriptAction)) {
			LOGGER.warning("JavaScript action is undefined!");
			return null;
		}

		ScriptCaller scriptCaller = new ScriptCaller(javaScriptAction, invokeOriginalPage);
		return dispatchScript(iwc, scriptCaller);
	}

	private Collection<String> dispatchScript(IWContext iwc, ScriptBuffer javaScriptAction, boolean invokeOriginalPage) {
		ScriptCaller scriptCaller = new ScriptCaller(javaScriptAction, invokeOriginalPage);
		return dispatchScript(iwc, scriptCaller);
	}

	private Collection<String> dispatchScript(IWContext iwc, ScriptCaller scriptCaller) {
		Container container = (Container) iwc.getServletContext().getAttribute(Container.class.getName());
		if (container == null) {
			LOGGER.warning("Container ("+Container.class+") can not be found!");
			return null;
		}
		WebContextBuilder webContextBuilder = (WebContextBuilder) iwc.getServletContext().getAttribute(WebContextBuilder.class.getName());
		if (webContextBuilder == null) {
			LOGGER.warning("WebContextBuilder ("+WebContextBuilder.class+") can not be found!");
			return null;
		}
		webContextBuilder.engageThread(container, iwc.getRequest(), iwc.getResponse());

		scriptCaller.setWebContext(WebContextFactory.get());
		scriptCaller.run();	//	We don't use threading because we want to send back IDs of "served" sessions

		return scriptCaller.getHttpSessionsServed();
	}

	@Override
	public Collection<String> dispatchRenderedComponent(IWContext iwc, UIComponent component) {
		if (component == null) {
			return null;
		}

		return dispatchRenderedComponents(iwc, Arrays.asList(component));
	}

	@Override
	public Collection<String> dispatchRenderedComponents(IWContext iwc, List<? extends UIComponent> components) {
		return dispatchRenderedComponents(iwc, components, null);
	}

	@Override
	public Collection<String> dispatchRenderedComponents(IWContext iwc, List<? extends UIComponent> components, Collection<AdvancedProperty> options) {
		if (ListUtil.isEmpty(components)) {
			return null;
		}

		ScriptBuffer scriptBuffer = new ScriptBuffer();
		String jsOptions = null;
		if (ListUtil.isEmpty(options)) {
			jsOptions = "{container: document.body, append: true}";
		} else {
			StringBuilder optionsToBuild = new StringBuilder("{");
			for (Iterator<AdvancedProperty> optionsIter = options.iterator(); optionsIter.hasNext();) {
				AdvancedProperty option = optionsIter.next();

				optionsToBuild.append(option.getId()).append(CoreConstants.COLON).append(CoreConstants.SPACE).append(CoreConstants.QOUTE_SINGLE_MARK)
					.append(option.getValue()).append(CoreConstants.QOUTE_SINGLE_MARK);
				if (optionsIter.hasNext()) {
					optionsToBuild.append(CoreConstants.COMMA).append(CoreConstants.SPACE);
				}
			}
			optionsToBuild.append("}");
			jsOptions = optionsToBuild.toString();
		}
		BuilderService builderService = builderLogicWrapper.getBuilderService(iwc);
		for (UIComponent component: components) {
			RenderedComponent rendered = builderService.getRenderedComponent(component, null);
			if (rendered == null) {
				LOGGER.warning("Component was not rendered: " + component);
			} else {
				scriptBuffer.appendScript("IWCORE.insertRenderedComponent(").appendData(rendered).appendScript(", ").appendScript(jsOptions).appendScript(");");
			}
		}

		return dispatchScript(iwc, scriptBuffer, Boolean.TRUE);
	}

}