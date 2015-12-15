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

import javax.rad.ui.container.IScrollPanel;

import javafx.scene.Node;

import com.sibvisions.rad.ui.javafx.ext.panes.FXScrollPane;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;

/**
 * The {@link JavaFXScrollPanel} is the JavaFX specific implementation of
 * {@link IScrollPanel}.
 * 
 * @author Robert Zenz
 * @see IScrollPanel
 * @see javafx.scene.control.ScrollPane
 * @see FXScrollPane
 */
public class JavaFXScrollPanel extends JavaFXAbstractForwardingContainer<FXScrollPane> implements IScrollPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXScrollPanel}.
	 */
	public JavaFXScrollPanel()
	{
		super(new FXScrollPane());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutInternal(JavaFXILayoutContainerHybrid pHybrid)
	{
		resource.setContent((Node)pHybrid.getResource());
	}
	
}	// JavaFXScrollPanel
