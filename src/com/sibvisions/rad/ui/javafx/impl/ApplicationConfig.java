/*
 * Copyright 2015 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sibvisions.rad.ui.javafx.impl;

import java.util.Map;
import java.util.Map.Entry;

import javax.rad.application.ILauncher;

import com.sibvisions.rad.ui.ApplicationUtil;
import com.sibvisions.rad.ui.Webstart;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The {@link ApplicationConfig} encapsulates the configuration.
 * 
 * @author Robert Zenz
 */
public class ApplicationConfig
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The name of the root node. */
	private static final String CONFIG_ROOT_NODE = "application";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The current application configuration. */
	private XmlNode appConfigNode = null;
	
	/** The {@link ILauncher}. */
	private ILauncher launcher;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ApplicationConfig}.
	 *
	 * @param pLauncher the {@link ILauncher}.
	 */
	public ApplicationConfig(ILauncher pLauncher)
	{
		launcher = pLauncher;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Configures this class with given {@link ApplicationParameters}.
	 * 
	 * @param pParameters the {@link ApplicationParameters}.
	 */
	public void configure(ApplicationParameters pParameters)
	{
		loadConfiguration(pParameters);
		updateConfiguration(pParameters);
	}
	
	/**
	 * Loads the configuration.
	 * 
	 * @param pParameters the {@link ApplicationParameters}.
	 */
	protected void loadConfiguration(ApplicationParameters pParameters)
	{
		String sConfig = pParameters.getConfiguration();
		
		if (!StringUtil.isEmpty(sConfig))
		{
			try
			{
				appConfigNode = ApplicationUtil.getConfig(launcher, sConfig);
				
				if (appConfigNode != null)
				{
					appConfigNode = appConfigNode.getNode(CONFIG_ROOT_NODE);
				}
			}
			catch (Exception e)
			{
				LoggerFactory.getInstance(getClass()).error("Configuration load error!", e);
			}
		}
		
		if (appConfigNode == null)
		{
			// If the configuration can't be loaded, use an empty node.
			appConfigNode = new XmlNode(CONFIG_ROOT_NODE);
			
			LoggerFactory.getInstance(getClass()).error("Configuration was not found, an empty configuration was created!");
		}
	}
	
	/**
	 * Updates the current configuration with given parameters. The new values
	 * will override existing values!
	 * 
	 * @param pParameter the {@link ApplicationParameters}.
	 */
	protected void updateConfiguration(ApplicationParameters pParameter)
	{
		Map<String, String> params = pParameter.getParameters();
		
		if (params != null)
		{
			for (Entry<String, String> entry : params.entrySet())
			{
				appConfigNode.setNode(entry.getKey(), entry.getValue());
			}
		}
		
		if (Webstart.isJnlp())
		{
			// Include JNLP parameters
			try
			{
				String sCodeBase = Webstart.getCodeBase();
				
				if (sCodeBase != null)
				{
					appConfigNode.setNode(ILauncher.PARAM_CODEBASE, sCodeBase);
				}
			}
			catch (Throwable th)
			{
				// We don't log the exception because the logger has not been
				// initialized yet.
			}
		}
	}
	
	/**
	 * Sets the value of given application parameter.
	 * 
	 * @param pName the name of the application parameter.
	 * @param pValue the value or {@code null} if parameter wasn't found.
	 */
	public void setParameter(String pName, String pValue)
	{
		appConfigNode.setNode(pName, pValue);
	}
	
	/**
	 * Gets the value of given application parameter.
	 * 
	 * @param pName the name of the application parameter.
	 * @return the value or {@code null} if parameter was not found.
	 */
	public String getParameter(String pName)
	{
		XmlNode xmn = appConfigNode.getNode(pName);
		
		if (xmn != null)
		{
			String sValue = ApplicationUtil.replaceParameter(xmn.getValue(), launcher);
			
			if (sValue != null)
			{
				sValue = sValue.replace("\\n", "\n");
				sValue = sValue.replace("<br>", "\n");
			}
			
			return sValue;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Sets the value for given key into the registry.
	 * 
	 * @param pKey the key.
	 * @param pValue the value.
	 */
	public void setRegistryKey(String pKey, String pValue)
	{
		try
		{
			ApplicationUtil.setRegistryKey(ApplicationUtil.getRegistryApplicationName(launcher), pKey, pValue);
		}
		catch (SecurityException se)
		{
			if (Webstart.isJnlp())
			{
				Webstart.setProperty(pKey, pValue);
			}
		}
	}
	
	/**
	 * Gets the value for given key from the registry.
	 * 
	 * @param pKey the key.
	 * @return the value.
	 */
	public String getRegistryKey(String pKey)
	{
		try
		{
			return ApplicationUtil.getRegistryKey(ApplicationUtil.getRegistryApplicationName(launcher), pKey);
		}
		catch (SecurityException se)
		{
			if (Webstart.isJnlp())
			{
				return Webstart.getProperty(pKey);
			}
			else
			{
				return null;
			}
		}
	}
	
	/**
	 * Gets the launcher.
	 * 
	 * @return the launcher
	 */
	protected ILauncher getLauncher()
	{
		return launcher;
	}
	
}	// ApplicationConfig
