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

import java.util.List;

import javax.rad.application.ILauncher;
import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IFactory;

import javafx.application.Application;
import javafx.stage.Stage;

import com.sibvisions.util.log.LoggerFactory;

/**
 * The {@link JavaFXApplication} is the main entry point for any JVx JavaFX
 * applications.
 * 
 * @author Robert Zenz
 */
public class JavaFXApplication extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The internal {@link ILauncher}. */
	private ILauncher launcher;
	
	/** The parsed {@link ApplicationParameters}. */
	private ApplicationParameters params;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The main entry point for the application.
	 *
	 * @param pArgs the arguments.
	 */
	public static void main(String[] pArgs)
	{
		launch(pArgs);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage pStage) throws Exception
	{
		List<String> liRawArguments = getParameters().getRaw();
		String[] saArguments = liRawArguments.toArray(new String[liRawArguments.size()]);
		
		params = createParameters(saArguments);
		
		initLoggerFactory(params);
		
		launcher = createLauncher(pStage, createFactory(params), params);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Initializes the {@link LoggerFactory}.
	 * 
	 * @param pParameter the {@link ApplicationParameters}.
	 */
	protected void initLoggerFactory(ApplicationParameters pParameter)
	{
		LoggerFactory.init(pParameter.getParameter(ILauncher.PARAM_LOGFACTORY));
	}
	
	/**
	 * Creates the {@link IFactory}.
	 * 
	 * @param pParameter the {@link ApplicationParameters}.
	 * @return the {@link IFactory} that was created.
	 * @see IFactory
	 */
	protected IFactory createFactory(ApplicationParameters pParameter)
	{
		Class<?> clazz;
		
		try
		{
			clazz = Class.forName(pParameter.getParameter(ILauncher.PARAM_UIFACTORY));
		}
		catch (Exception e)
		{
			clazz = JavaFXFactory.class;
		}
		
		return UIFactoryManager.getFactoryInstance(clazz);
	}
	
	/**
	 * Creates the {@link ApplicationParameters}.
	 * 
	 * @param pArguments additional application arguments to use.
	 * @return the {@link ApplicationParameters}.
	 * @see ApplicationParameters
	 */
	protected ApplicationParameters createParameters(String[] pArguments)
	{
		return ApplicationParameters.parse(pArguments);
	}
	
	/**
	 * Gets the current {@link ApplicationParameters}.
	 * 
	 * @return the current {@link ApplicationParameters}.
	 */
	protected ApplicationParameters getApplicationParameters()
	{
		return params;
	}
	
	/**
	 * Creates a new {@link ILauncher} with given configuration.
	 * 
	 * @param pStage the {@link Stage}.
	 * @param pFactory the {@link IFactory}.
	 * @param pParameter the {@link ApplicationParameters}.
	 * @return the new {@link ILauncher}.
	 */
	protected ILauncher createLauncher(Stage pStage, IFactory pFactory, ApplicationParameters pParameter)
	{
		return new JavaFXLauncher(this, pStage, pFactory, pParameter);
	}
	
	/**
	 * Gets the current {@link ILauncher}.
	 * 
	 * @return the current {@link ILauncher}.
	 */
	public ILauncher getLauncher()
	{
		return launcher;
	}
	
}	// JavaFXApplication
