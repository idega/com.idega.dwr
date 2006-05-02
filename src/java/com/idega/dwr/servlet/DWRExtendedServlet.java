/*
 * $Id: DWRExtendedServlet.java,v 1.1 2006/05/02 18:55:24 eiki Exp $ Created on Apr 18,
 * 2006
 * 
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package com.idega.dwr.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import uk.ltd.getahead.dwr.Configuration;
import uk.ltd.getahead.dwr.Container;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.DWRServlet;
import uk.ltd.getahead.dwr.WebContextFactory;
import com.idega.dwr.create.IBOCreator;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWModuleLoader;
import com.idega.idegaweb.JarLoader;

/**
 * This servlet extends the Default implementation of AbstractDWRServlet to add
 * auto loading of dwr.xml config files from <br>
 * inside idegaweb bundle jar files.
 * 
 * Last modified: $Date: 2006/05/02 18:55:24 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">Eirikur S. Hrafnsson</a>
 * @version $Revision: 1.1 $
 */
public class DWRExtendedServlet extends DWRServlet implements JarLoader {

	protected Configuration dwrConfig;

	public DWRExtendedServlet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ltd.getahead.dwr.AbstractDWRServlet#configure(javax.servlet.ServletConfig,
	 *      uk.ltd.getahead.dwr.Configuration)
	 */
	public void configure(ServletConfig config, Configuration configuration) throws ServletException {
		// First add our custom creator
		registerIBOCreator();
		// load the configs from bundles
		this.dwrConfig = configuration;
		loadDWRConfigFilesFromBundles();
		// end with the default impl
		super.configure(config, dwrConfig);
	}

	/**
	 * 
	 */
	protected void registerIBOCreator() {
		Container container = WebContextFactory.get().getContainer();
		CreatorManager cman = (CreatorManager) container.getBean(CreatorManager.class.getName());
		cman.addCreatorType("ibo", IBOCreator.class);
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
		Enumeration entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			// JarFileEntry entryName = (JarFileEntry)entries.nextElement();
			String entryName = entry.getName();
			if (entryName.endsWith("dwr.xml")) {
				try {
					InputStream stream = jarFile.getInputStream(entry);
					this.dwrConfig.addConfig(stream);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
				catch (SAXException e) {
					e.printStackTrace();
				}
			}
		}
	}
}