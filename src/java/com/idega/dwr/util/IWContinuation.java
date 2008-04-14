package com.idega.dwr.util;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.util.Continuation;

public class IWContinuation extends Continuation {

	public static final void setUseJetty(boolean useJetty) {
		isJetty = useJetty;
	}
	
	public IWContinuation(HttpServletRequest request) {
		super(request);
	}

}
