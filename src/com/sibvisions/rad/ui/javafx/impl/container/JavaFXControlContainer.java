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
package com.sibvisions.rad.ui.javafx.impl.container;

import javafx.scene.control.Control;

import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;

/**
 * The {@link JavaFXControlContainer} is a container for {@link Control}s. A
 * {@link Control} usually isn't a container, but some controls act like a
 * container, e.g. {@link javafx.scene.control.ListView}.
 * 
 * @author René Jahn
 * @see Control
 */
public class JavaFXControlContainer extends JavaFXAbstractForwardingContainer<Control>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXControlContainer}.
	 * 
	 * @param pResource the {@link Control} to use.
	 */
	public JavaFXControlContainer(Control pResource)
	{
		super(pResource);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutInternal(JavaFXILayoutContainerHybrid pLayoutContainerHybrid)
	{
		// Not possible.
	}
	
}	// JavaFXControlContainer
