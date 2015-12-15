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

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.menu.IMenuBar;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;

/**
 * The {@link JavaFXToolBarPanel} is the JavaFX specific implementation of
 * {@link IToolBarPanel}.
 * 
 * @author Robert Zenz
 * @see IToolBarPanel
 */
public class JavaFXToolBarPanel extends JavaFXAbstractForwardingContainer<BorderPane> implements IToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link IMenuBar}. */
	protected IMenuBar menuBar;
	
	/** The container for the {@link IToolBar}s. */
	protected Pane toolBarContainer;
	
	/** The {@link List} of {@link IToolBar}s. */
	protected List<IToolBar> toolBars;
	
	/** The container for the top, to house the menu. */
	protected BorderPane topContainer;
	
	/** The current toolbar area, where the toolbars are. */
	private int toolBarArea;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXToolBarPanel}.
	 */
	public JavaFXToolBarPanel()
	{
		super(new BorderPane());
		
		toolBarArea = AREA_TOP;
		
		toolBars = new ArrayList<>();
		toolBarContainer = new HBox();
		
		topContainer = new BorderPane();
		
		resource.setTop(topContainer);
		
		topContainer.setCenter(toolBarContainer);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToolBar(IToolBar pToolBar)
	{
		addToolBar(pToolBar, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToolBar(IToolBar pToolBar, int pIndex)
	{
		if (pIndex >= 0)
		{
			toolBars.add(pIndex, pToolBar);
			toolBarContainer.getChildren().add(pIndex, (Node)pToolBar.getResource());
		}
		else
		{
			toolBars.add(pToolBar);
			toolBarContainer.getChildren().add((Node)pToolBar.getResource());
		}
		
		if (toolBarArea == AREA_TOP || toolBarArea == AREA_BOTTOM)
		{
			pToolBar.setOrientation(IToolBar.HORIZONTAL);
		}
		else
		{
			pToolBar.setOrientation(IToolBar.VERTICAL);
		}
		
		fixGrowth();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IToolBar getToolBar(int pIndex)
	{
		return toolBars.get(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getToolBarArea()
	{
		return toolBarArea;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getToolBarCount()
	{
		return toolBars.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return toolBars.indexOf(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllToolBars()
	{
		toolBars.clear();
		toolBarContainer.getChildren().clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeToolBar(int pIndex)
	{
		if (pIndex >= 0)
		{
			toolBars.remove(pIndex);
			toolBarContainer.getChildren().remove(pIndex);
			
			fixGrowth();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeToolBar(IToolBar pToolBar)
	{
		removeToolBar(toolBars.indexOf(pToolBar));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolBarArea(int pArea)
	{
		toolBarArea = pArea;
		
		// Remove the toolbars from everywhere
		topContainer.setCenter(null);
		resource.setBottom(null);
		resource.setLeft(null);
		resource.setRight(null);
		
		// Create a new container
		Pane newToolBarContainer = null;
		
		if (toolBarArea == AREA_TOP || toolBarArea == AREA_BOTTOM)
		{
			newToolBarContainer = new HBox();
		}
		else
		{
			newToolBarContainer = new VBox();
		}
		
		newToolBarContainer.getChildren().addAll(toolBarContainer.getChildren());
		toolBarContainer.getChildren().clear();
		toolBarContainer = newToolBarContainer;
		
		if (toolBarArea == AREA_TOP || toolBarArea == AREA_BOTTOM)
		{
			for (IToolBar toolBar : toolBars)
			{
				toolBar.setOrientation(IToolBar.HORIZONTAL);
			}
		}
		else
		{
			for (IToolBar toolBar : toolBars)
			{
				toolBar.setOrientation(IToolBar.VERTICAL);
			}
		}
		
		// Put the container at the right place.
		switch (toolBarArea)
		{
			case AREA_BOTTOM:
				resource.setBottom(toolBarContainer);
				break;
				
			case AREA_LEFT:
				resource.setLeft(toolBarContainer);
				break;
				
			case AREA_RIGHT:
				resource.setRight(toolBarContainer);
				break;
				
			case AREA_TOP:
			default:
				resource.setTop(toolBarContainer);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutInternal(JavaFXILayoutContainerHybrid pHybrid)
	{
		resource.setCenter((Node)pHybrid.getResource());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the embedded menu bar.
	 *
	 * @return the embedded menu bar.
	 */
	protected IMenuBar getEmbeddedMenuBar()
	{
		return menuBar;
	}
	
	/**
	 * Sets the embedded menu bar.
	 *
	 * @param pMenuBar the new embedded menu bar.
	 */
	protected void setEmbeddedMenuBar(IMenuBar pMenuBar)
	{
		menuBar = pMenuBar;
		
		topContainer.setTop(null);
		
		if (pMenuBar != null)
		{
			topContainer.setTop((Node)pMenuBar.getResource());
		}
	}
	
	/**
	 * Fixes the growth of all toolbars, setting only the last visible/managed
	 * one to ALWAYS.
	 */
	private void fixGrowth()
	{
		for (Node toolBar : toolBarContainer.getChildren())
		{
			if (toolBarArea == AREA_TOP || toolBarArea == AREA_BOTTOM)
			{
				HBox.setHgrow(toolBar, Priority.SOMETIMES);
			}
			else
			{
				VBox.setVgrow(toolBar, Priority.SOMETIMES);
			}
		}
		
		for (int index = toolBarContainer.getChildren().size() - 1; index >= 0; index--)
		{
			Node child = toolBarContainer.getChildren().get(index);
			
			if (child.isManaged())
			{
				if (toolBarArea == AREA_TOP || toolBarArea == AREA_BOTTOM)
				{
					HBox.setHgrow(child, Priority.ALWAYS);
				}
				else
				{
					VBox.setVgrow(child, Priority.ALWAYS);
				}
				return;
			}
			
		}
	}
	
}	// JavaFXToolBarPanel
