/*
 * $Id: DWRExtendedServlet.java,v 1.6 2007/02/15 15:00:31 valdas Exp $ Created on Apr 18,
 * 2006
 * 
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package com.idega.dwr.servlet;

import java.io.File;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.impl.DwrXmlConfigurator;
import org.directwebremoting.servlet.DwrServlet;

import com.idega.dwr.create.IBOCreator;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWModuleLoader;
import com.idega.idegaweb.JarLoader;

/**
 * This servlet extends the Default implementation of AbstractDWRServlet to add
 * auto loading of dwr.xml config files from <br>
 * inside idegaweb bundle jar files.
 * 
 * Last modified: $Date: 2007/02/15 15:00:31 $ by $Author: valdas $
 * 
 * @author <a href="mailto:eiki@idega.com">Eirikur S. Hrafnsson</a>
 * @version $Revision: 1.6 $
 */
public class DWRExtendedServlet extends DwrServlet implements JarLoader {

	private static final long serialVersionUID = 3422209939690053482L;
	private static final Logger log = Logger.getLogger(DWRExtendedServlet.class.getName());

	public DWRExtendedServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		// First add our custom creator
		registerIBOCreator();
		
		// Load the configs from bundles
		loadDWRConfigFilesFromBundles();
	}

	/**
	 * Add a new type of dwr object for service or session beans
	 */
	protected void registerIBOCreator() {
		CreatorManager cman = (CreatorManager) getContainer().getBean(CreatorManager.class.getName());
		cman.addCreatorType("ibo", IBOCreator.class.getName());
	}

	
	public void loadDWRConfigFilesFromBundles() {
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(getServletContext());
		IWModuleLoader loader = new IWModuleLoader(iwma, getServletContext());
		loader.getJarLoaders().add(this);
		loader.loadBundlesFromJars();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.idegaweb.JarLoader#loadJar(java.io.File,java.util.jar.JarFile,
	 *      java.lang.String)
	 */
	public void loadJar(File bundleJarFile, JarFile jarFile, String jarPath) {
		JarEntry entry = jarFile.getJarEntry("WEB-INF/dwr.xml");

		if (entry != null) {
			try {
				// The dwr.xml from within the JAR file.
				InputStream stream = jarFile.getInputStream(entry);
				
	            DwrXmlConfigurator dwrFile = new DwrXmlConfigurator();
	            dwrFile.setInputStream(stream);

	            // Container is a protected variable in the super class
	            dwrFile.configure(getContainer());
			}
			catch (Exception e) {
				log.log(Level.WARNING, "Error loading dwr.xml from " + jarFile.getName(), e);
			}
		}
	}
}