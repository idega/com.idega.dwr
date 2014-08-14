package com.idega.dwr.bean;

import java.io.Serializable;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
public class Response implements Serializable{
	private static final long serialVersionUID = -2878378701373656472L;

	private String status;
	
	private String message;
	
	private String action;

	@RemoteProperty
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@RemoteProperty
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@RemoteProperty
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	

}