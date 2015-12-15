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
package com.sibvisions.rad.ui.javafx.impl.layout;

import javax.rad.ui.IInsets;
import javax.rad.ui.ILayout;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.impl.JavaFXInsets;
import com.sibvisions.rad.ui.javafx.impl.JavaFXResource;

/**
 * The {@link JavaFXAbstractLayoutContainerHybrid} is an abstract implementation
 * of {@link JavaFXILayoutContainerHybrid} and {@link ILayout} which handles
 * most of the "low-level" stuff like keeping track of the gaps, margins and the
 * resource.
 * 
 * @author Robert Zenz
 * @param <P> the type of the {@link Pane}.
 * @param <CO> the type of the constraints.
 */
public abstract class JavaFXAbstractLayoutContainerHybrid<P extends Pane, CO> extends JavaFXResource<P> 
                                                                              implements ILayout<CO>, JavaFXILayoutContainerHybrid
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The value for the horizontal gap. */
	protected int horizontalGap = 0;
	
	/** The value for the vertical gap. */
	protected int verticalGap = 0;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXAbstractLayoutContainerHybrid}.
	 *
	 * @param pPane the {@link Pane}.
	 */
	protected JavaFXAbstractLayoutContainerHybrid(P pPane)
	{
		super(pPane);
		
		resource.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReverseOrderNeeded()
	{
		return false;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalGap()
	{
		return horizontalGap;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IInsets getMargins()
	{
		return new JavaFXInsets(resource.getPadding());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVerticalGap()
	{
		return verticalGap;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalGap(int pHorizontalGap)
	{
		horizontalGap = pHorizontalGap;
		
		updateGaps();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMargins(IInsets pMargins)
	{
		resource.setPadding((Insets)pMargins.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalGap(int pVerticalGap)
	{
		verticalGap = pVerticalGap;
		
		updateGaps();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets called whenever the gaps change.
	 * 
	 * @see #horizontalGap
	 * @see #verticalGap
	 */
	protected abstract void updateGaps();
	
}	// JavaFXAbstractLayoutContainerHybrid
