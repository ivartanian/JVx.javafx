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

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

/**
 * The {@link FXScrollPane} is a {@link ScrollPane} extension that does take the
 * size of the scrollbars into account when calculating its preferred size.
 * 
 * @author Robert Zenz
 */
public class FXScrollPane extends ScrollPane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The horizontal {@link ScrollBar}. */
	private ScrollBar horizontalScrollBar;
	
	/** The vertical {@link ScrollBar}. */
	private ScrollBar verticalScrollBar;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXScrollPane}.
	 */
	public FXScrollPane()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double pWidth)
	{
		return super.computePrefHeight(pWidth) + getHorizontalScrollBarHeight(pWidth);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double pHeight)
	{
		return super.computePrefWidth(pHeight) + getVerticalScrollBarWidth(pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		if (getContent() != null)
		{
			double contentWidth = getContent().prefWidth(getHeight());
			double contentHeight = getContent().prefHeight(getWidth());
			
			setFitToWidth(contentWidth < getWidth());
			setFitToHeight(contentHeight < getHeight());
		}
		
		super.layoutChildren();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the height of the horizontal scrollbar.
	 * 
	 * @param pWidth the current width.
	 * @return the height of the horizontal scrollbar.
	 */
	private double getHorizontalScrollBarHeight(double pWidth)
	{
		if (horizontalScrollBar == null)
		{
			getScrollBars();
		}
		
		if (horizontalScrollBar != null && getVbarPolicy() != ScrollBarPolicy.NEVER)
		{
			return horizontalScrollBar.prefHeight(pWidth);
		}
		
		return 0;
	}
	
	/**
	 * Gets the horizontal and vertical {@link ScrollBar} and saves them in the
	 * class members.
	 * 
	 * @see #horizontalScrollBar
	 * @see #verticalScrollBar
	 */
	private void getScrollBars()
	{
		for (Node node : lookupAll(".scroll-pane *.scroll-bar"))
		{
			if (node instanceof ScrollBar)
			{
				ScrollBar scrollBar = (ScrollBar)node;
				
				if (scrollBar.getOrientation() == Orientation.HORIZONTAL)
				{
					horizontalScrollBar = scrollBar;
				}
				else
				{
					verticalScrollBar = scrollBar;
				}
			}
		}
	}
	
	/**
	 * Gets the width of the vertical scrollbar.
	 * 
	 * @param pHeight the current height.
	 * @return the width of the vertical scrollbar.
	 */
	private double getVerticalScrollBarWidth(double pHeight)
	{
		if (verticalScrollBar == null)
		{
			getScrollBars();
		}
		
		if (verticalScrollBar != null && getHbarPolicy() != ScrollBarPolicy.NEVER)
		{
			return verticalScrollBar.prefWidth(pHeight);
		}
		
		return 0;
	}
	
}	// FXScrollPane
