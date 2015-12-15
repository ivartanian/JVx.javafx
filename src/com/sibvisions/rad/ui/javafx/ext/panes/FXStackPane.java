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
package com.sibvisions.rad.ui.javafx.ext.panes;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * The {@link FXStackPane} is a {@link StackPane} extension that does ignore the
 * maximum and minimum size of its children and does not have a maximum and
 * minimum size.
 * 
 * @author Robert Zenz
 */
public class FXStackPane extends StackPane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXStackPane}.
	 */
	public FXStackPane()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link FXStackPane}.
	 *
	 * @param pChildren the {@link Node[] children} to add.
	 */
	public FXStackPane(Node... pChildren)
	{
		super(pChildren);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxHeight(double pWidth)
	{
		return Double.MAX_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxWidth(double pHeight)
	{
		return Double.MAX_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinHeight(double pWidth)
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinWidth(double pHeight)
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		if (getChildren().isEmpty())
		{
			return;
		}
		
		double x = 0;
		double y = 0;
		double width = getWidth();
		double height = getHeight();
		
		Insets padding = getPadding();
		if (padding != null)
		{
			x = padding.getLeft();
			y = padding.getTop();
			width = width - padding.getLeft() - padding.getRight();
			height = height - padding.getTop() - padding.getBottom();
		}
		
		for (Node child : getChildren())
		{
			child.resizeRelocate(x, y, width, height);
		}
	}
	
}	// FXStackPane
