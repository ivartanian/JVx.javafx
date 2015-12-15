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

import javax.rad.ui.IComponent;
import javax.rad.ui.layout.IBorderLayout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import com.sibvisions.rad.ui.javafx.ext.panes.FXBorderPane;

/**
 * The {@link JavaFXBorderLayout} is the JavaFX specific implementation of
 * {@link IBorderLayout}. It allows to layout five components, four in the
 * border (top, left, bottom, right) and one in the center.
 * 
 * @author Robert Zenz
 * @see IBorderLayout
 * @see BorderPane
 */
public class JavaFXBorderLayout extends JavaFXAbstractLayoutContainerHybrid<FXBorderPane, String> implements IBorderLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXBorderLayout}.
	 */
	public JavaFXBorderLayout()
	{
		super(new FXBorderPane());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pConstraints == null || CENTER.equals(pConstraints))
		{
			resource.setCenter((Node)pComponent.getResource());
		}
		else if (EAST.equals(pConstraints))
		{
			resource.setRight((Node)pComponent.getResource());
		}
		else if (NORTH.equals(pConstraints))
		{
			resource.setTop((Node)pComponent.getResource());
		}
		else if (SOUTH.equals(pConstraints))
		{
			resource.setBottom((Node)pComponent.getResource());
		}
		else if (WEST.equals(pConstraints))
		{
			resource.setLeft((Node)pComponent.getResource());
		}
		else
		{
			resource.setCenter((Node)pComponent.getResource());
		}
		
		BorderPane.setAlignment((Node)pComponent.getResource(), Pos.CENTER);
		
		updateGaps();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getConstraints(IComponent pComponent)
	{
		Node node = (Node)pComponent.getResource();
		
		if (resource.getBottom() == node)
		{
			return SOUTH;
		}
		else if (resource.getCenter() == node)
		{
			return CENTER;
		}
		else if (resource.getLeft() == node)
		{
			return WEST;
		}
		else if (resource.getRight() == node)
		{
			return EAST;
		}
		else if (resource.getTop() == node)
		{
			return NORTH;
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(IComponent pComponent)
	{
		tryRemove(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAll()
	{
		resource.setBottom(null);
		resource.setCenter(null);
		resource.setLeft(null);
		resource.setRight(null);
		resource.setTop(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConstraints(IComponent pComponent, String pConstraints)
	{
		if (tryRemove(pComponent))
		{
			add(pComponent, pConstraints, -1);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateGaps()
	{
		if (resource.getBottom() != null)
		{
			BorderPane.setMargin(resource.getBottom(), new Insets(verticalGap, 0, 0, 0));
		}
		
		if (resource.getLeft() != null)
		{
			BorderPane.setMargin(resource.getLeft(), new Insets(0, horizontalGap, 0, 0));
		}
		
		if (resource.getRight() != null)
		{
			BorderPane.setMargin(resource.getRight(), new Insets(0, 0, 0, horizontalGap));
		}
		
		if (resource.getTop() != null)
		{
			BorderPane.setMargin(resource.getTop(), new Insets(0, 0, verticalGap, 0));
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tries to remove the given {@link IComponent}, and returns {@code true} if
	 * that succeeded.
	 * 
	 * @param pComponent the {@link IComponent} to remove.
	 * @return {@code true} if the given {@link IComponent} has been removed.
	 */
	private boolean tryRemove(IComponent pComponent)
	{
		Node nodeToRemove = (Node)pComponent.getResource();
		
		if (resource.getBottom() == nodeToRemove)
		{
			resource.setBottom(null);
			return true;
		}
		else if (resource.getCenter() == nodeToRemove)
		{
			resource.setCenter(null);
			return true;
		}
		else if (resource.getLeft() == nodeToRemove)
		{
			resource.setLeft(null);
			return true;
		}
		else if (resource.getRight() == nodeToRemove)
		{
			resource.setRight(null);
			return true;
		}
		else if (resource.getTop() == nodeToRemove)
		{
			resource.setTop(null);
			return true;
		}
		
		return false;
	}
	
}	// JavaFXBorderLayout
