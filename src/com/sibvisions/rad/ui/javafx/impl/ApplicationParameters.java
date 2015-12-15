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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * The {@link ApplicationParameters} class encapsulates the access to
 * application startup configuration. It holds the configured application class,
 * the configuration and optional/additional arguments.
 * 
 * @author Robert Zenz
 */
public final class ApplicationParameters
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The class name of the application. */
	private String applicationClassName;
	
	/** The resource name of the configuration. */
	private String configuration;
	
	/** The additional parameters. */
	private Map<String, String> parameters;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ApplicationParameters}.
	 * 
	 * @param pApplicationClassName the class name of the application.
	 * @param pConfiguration the configuration resource.
	 * @param pParameters the parameters.
	 */
	private ApplicationParameters(String pApplicationClassName, String pConfiguration, Map<String, String> pParameters)
	{
		applicationClassName = pApplicationClassName;
		configuration = pConfiguration;
		
		parameters = pParameters;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ApplicationParameters} with given
	 * command-line arguments.
	 * 
	 * @param pArguments the arguments.
	 * @return the application parameters.
	 */
	public static ApplicationParameters parse(String[] pArguments)
	{
		if (pArguments == null || pArguments.length == 0)
		{
			throw new RuntimeException("Application classname needs to be provided as first argument.");
		}
		
		List<String> arguments = getQuoteStrippedArguments(pArguments);
		String applicationClassName = arguments.get(0);
		String configurationName = "application.xml";
		int additionalParametersStartAt = 1;
		
		// Get the name of the configuration (if any).
		if (arguments.size() > 1 && arguments.get(1).indexOf("=") < 0)
		{
			configurationName = arguments.get(1);
			
			additionalParametersStartAt = 2;
		}
		
		Map<String, String> additionalParameters = getAdditionalParameters(arguments, additionalParametersStartAt);
		
		return new ApplicationParameters(applicationClassName, configurationName, additionalParameters);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets all additional parameters in a {@link Map}.
	 * 
	 * @param pArguments the arguments to parse.
	 * @param pOffset the offset at which the additional parameters start.
	 * @return the {@link Map} if additional parameters, might be empty.
	 */
	private static Map<String, String> getAdditionalParameters(List<String> pArguments, int pOffset)
	{
		Map<String, String> additionalParameters = new HashMap<>();
	
		for (int index = pOffset; index < pArguments.size(); index++)
		{
			StringTokenizer tokenizer = new StringTokenizer(pArguments.get(index), "=");
			
			if (tokenizer.countTokens() == 2)
			{
				additionalParameters.put(tokenizer.nextToken(), tokenizer.nextToken());
			}
		}
		
		return additionalParameters;
	}
	
	/**
	 * Strips leading/trailing quotes (if any) from the given parameters.
	 * 
	 * @param pArguments the arguments to strip.
	 * @return the {@link List} of stripped arguments.
	 */
	private static List<String> getQuoteStrippedArguments(String[] pArguments)
	{
		List<String> strippedArguments = new ArrayList<>(pArguments.length);
		
		for (String argument : pArguments)
		{
			if (argument.startsWith("\"") && argument.endsWith("\""))
			{
				strippedArguments.add(argument.substring(1, argument.length() - 1));
			}
			else
			{
				strippedArguments.add(argument);
			}
		}
		
		return strippedArguments;
	}
	
	/**
	 * Gets the name of the application class. It's usually a full qualified
	 * java class name.
	 * 
	 * @return the class name of the application.
	 */
	public String getApplicationClassName()
	{
		return applicationClassName;
	}
	
	/**
	 * Gets the name of the configuration resource. It can be a file or any
	 * other resource available on the classpath.
	 * 
	 * @return the name of the configuration resource.
	 */
	public String getConfiguration()
	{
		return configuration;
	}
	
	/**
	 * Gets the value of the given parameter name.
	 * 
	 * @param pName the name of the parameter.
	 * @return the value or {@code null} if parameter wasn't found.
	 */
	public String getParameter(String pName)
	{
		return parameters.get(pName);
	}
	
	/**
	 * Gets the current parameters (original instance).
	 * 
	 * @return the parameters.
	 */
	public Map<String, String> getParameters()
	{
		return parameters;
	}
	
	/**
	 * Sets a parameter.
	 * 
	 * @param pName the name of the parameter
	 * @param pValue the value of the parameter or {@code null} to unset the
	 *            parameter.
	 * @return the previous value.
	 */
	public String setParameter(String pName, String pValue)
	{
		if (pValue == null)
		{
			return parameters.remove(pName);
		}
		else
		{
			return parameters.put(pName, pValue);
		}
	}
	
}	// ApplicationParameters
