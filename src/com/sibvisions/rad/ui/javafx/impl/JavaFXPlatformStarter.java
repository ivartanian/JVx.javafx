/*
 * Copyright 2014 SIB Visions GmbH
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

import javafx.application.Platform;

import com.sun.javafx.application.PlatformImpl;

/**
 * The {@link JavaFXPlatformStarter} is a thin wrapper around the internal API
 * that allows you to start the JavaFX main thread.
 * <p>
 * The feature request for an official API is
 * <a href="https://javafx-jira.kenai.com/browse/RT-40101">RT-40101</a>.
 * 
 * @author Robert Zenz
 */
public final class JavaFXPlatformStarter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance needed.
	 */
	private JavaFXPlatformStarter()
	{
		// Not needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Starts the JavaFX Application Thread.
	 * <p>
	 * Calling this function twice <em>might</em> result in an exception,
	 * depending on the implementation. To be concrete the documentation of the
	 * internal API is talking about that calling the startup method twice
	 * <em>will</em> result in an exception. However the behavior of the method
	 * is actually that another call to the startup method will simply result in
	 * a NOOP in our case as long as the application hasn't exited.
	 * 
	 * @see Platform#isImplicitExit()
	 * @see Platform#setImplicitExit(boolean)
	 */
	public static void start()
	{
		PlatformImpl.startup(() ->
		{
			// Not needed.
		});
		Platform.setImplicitExit(true);
	}
	
}	// JavaFXPlatformStarter
