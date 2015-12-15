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
package com.sibvisions.rad.ui.javafx.impl.container;

import javax.rad.ui.IImage;
import javax.rad.ui.container.IPanel;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;

/**
 * The {@link JavaFXPanel} is the JavaFX specific implementation of
 * {@link IPanel}.
 * 
 * @author Robert Zenz
 * @see IPanel
 * @see StackPane
 */
public class JavaFXPanel extends JavaFXAbstractForwardingContainer<Pane> implements IPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link IImage} that is used for the background. */
	private IImage backgroundImage;
	
	/** The {@link FXImageRegion} that is used for the background. */
	private FXImageRegion backgroundImageRegion;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXPanel}.
	 */
	public JavaFXPanel()
	{
		// Use a StackPane, as a normal Pane does not resize it's children
		// beyond setting them to the preferred size. A StackPane does
		// a "back-to-front" layout with each children maximized.
		super(new StackPane());
	}
	
	/**
	 * Creates a new instance of {@link JavaFXPanel}.
	 * 
	 * @param pResource the {@link Pane} to use.
	 */
	public JavaFXPanel(Pane pResource)
	{
		super(pResource);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getBackgroundImage()
	{
		return backgroundImage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackgroundImage(IImage pImage)
	{
		backgroundImage = pImage;
		
		if (backgroundImage != null)
		{
			if (backgroundImageRegion == null)
			{
				backgroundImageRegion = new FXImageRegion((Image)backgroundImage.getResource());
				if (!resource.getChildren().isEmpty())
				{
					resource.getChildren().add(0, backgroundImageRegion);
				}
				else
				{
					resource.getChildren().add(backgroundImageRegion);
				}
			}
			else
			{
				backgroundImageRegion.setImage((Image)backgroundImage.getResource());
			}
		}
		else
		{
			resource.getChildren().remove(backgroundImageRegion);
			backgroundImageRegion = null;
		}
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
		resource.getChildren().clear();
		
		if (backgroundImageRegion != null)
		{
			resource.getChildren().add(backgroundImageRegion);
		}
		
		resource.getChildren().add((Node)pHybrid.getResource());
	}
	
}	// JavaFXPanel
