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

import java.util.List;

import javax.rad.genui.UILayout;
import javax.rad.ui.IComponent;
import javax.rad.ui.ILayout;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXAbstractLayoutContainerHybrid;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXNoLayout;

/**
 * The {@link JavaFXAbstractForwardingContainer} is an abstract extension of
 * {@link JavaFXAbstractContainer} that forwards all functions to a
 * {@link JavaFXILayoutContainerHybrid}.
 * 
 * @author Robert Zenz
 * @param <C> the type of the container.
 */
public abstract class JavaFXAbstractForwardingContainer<C extends Region> extends JavaFXAbstractContainer<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link ILayout} of this container. */
	protected ILayout<?> layout;
	
	/** The {@link JavaFXILayoutContainerHybrid} of this container. */
	protected JavaFXILayoutContainerHybrid layoutContainerHybrid;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXAbstractForwardingContainer}.
	 *
	 * @param pContainer the container.
	 */
	protected JavaFXAbstractForwardingContainer(C pContainer)
	{
		super(pContainer);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILayout<?> getLayout()
	{
		return layout;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLayout(@SuppressWarnings("rawtypes") ILayout pLayout)
	{
		JavaFXILayoutContainerHybrid previousHybrid = layoutContainerHybrid;
		
		if (pLayout != null)
		{
			layout = pLayout;
		}
		else
		{
			layout = new JavaFXNoLayout();
		}
		
		if (layout instanceof UILayout)
		{
			layoutContainerHybrid = (JavaFXAbstractLayoutContainerHybrid<?, ?>)((UILayout<?, ?>)pLayout).getUIResource();
		}
		else if (layout instanceof JavaFXAbstractLayoutContainerHybrid<?, ?>)
		{
			layoutContainerHybrid = (JavaFXAbstractLayoutContainerHybrid<?, ?>)layout;
		}
		
		if (!components.isEmpty() && previousHybrid != null)
		{
			for (IComponent component : components)
			{
				previousHybrid.remove(component);
				layoutContainerHybrid.add(component, null, -1);
			}
		}
		
		setLayoutInternal(layoutContainerHybrid);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void addInternal(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (layoutContainerHybrid != null)
		{
			layoutContainerHybrid.add(pComponent, pConstraints, pIndex);
		}
		else
		{
			Object res = pComponent.getResource();
			
			if (res instanceof Node)
			{
				Pane pane = getPane();
				
				List<Node> liNodes = null;
				
				if (pane != null)
				{
					if (pane instanceof BorderPane)
					{
						((BorderPane)resource).setCenter((Node)res);
					}
					else
					{
						liNodes = pane.getChildren();
					}
				}
				else if (resource instanceof ListView<?>)
				{
					liNodes = ((ListView)resource).getItems();
				}
				
				if (liNodes != null)
				{
					if (pIndex < 0 || pIndex >= liNodes.size())
					{
						liNodes.add((Node)res);
					}
					else
					{
						liNodes.add(pIndex, (Node)res);
					}
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isReverseAddOrderNeeded()
	{
		if (layoutContainerHybrid != null)
		{
			return layoutContainerHybrid.isReverseOrderNeeded();
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeAllInternal()
	{
		if (layoutContainerHybrid != null)
		{
			layoutContainerHybrid.removeAll();
		}
		else
		{
			Pane pane = getPane();
			
			if (pane != null)
			{
				pane.getChildren().clear();
			}
			else if (resource instanceof ListView<?>)
			{
				((ListView<?>)resource).getItems().clear();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeInternal(IComponent pComponent)
	{
		if (layoutContainerHybrid != null)
		{
			layoutContainerHybrid.remove(pComponent);
		}
		else
		{
			Pane pane = getPane();
			
			if (pane != null)
			{
				pane.getChildren().remove(pComponent.getResource());
			}
			else if (resource instanceof ListView)
			{
				((ListView<?>)resource).getItems().remove(pComponent.getResource());
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets called whenever the layout of this container changes.
	 *
	 * @param pLayoutContainerHybrid the new
	 *            {@link JavaFXILayoutContainerHybrid} of this container.
	 */
	protected abstract void setLayoutInternal(JavaFXILayoutContainerHybrid pLayoutContainerHybrid);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link Pane} from the current resource, if possible.
	 * 
	 * @return the pane or <code>null</code> if current resource isn't a
	 *         {@link Pane}.
	 */
	private Pane getPane()
	{
		if (resource instanceof ScrollPane)
		{
			Node ndContent = ((ScrollPane)resource).getContent();
			
			if (ndContent instanceof Pane)
			{
				return (Pane)ndContent;
			}
		}
		else if (resource instanceof Pane)
		{
			return (Pane)resource;
		}
		
		//resource isn't a Pane
		return null;
	}
	
}	// JavaFXAbstractForwardingContainer
