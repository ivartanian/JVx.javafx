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
package com.sibvisions.rad.ui.javafx.impl.layout;

import javax.rad.ui.IComponent;

import javafx.scene.Node;

import com.sibvisions.rad.ui.javafx.ext.panes.FXNullPane;

/**
 * The {@link JavaFXNoLayout} is a simple layout that does nothing.
 * 
 * @author Robert Zenz
 */
public class JavaFXNoLayout extends JavaFXAbstractLayoutContainerHybrid<FXNullPane, Object>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXNoLayout}.
	 */
	public JavaFXNoLayout()
	{
		super(new FXNullPane());
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
		if (pIndex >= 0)
		{
			resource.getChildren().add(pIndex, (Node)pComponent.getResource());
		}
		else
		{
			resource.getChildren().add((Node)pComponent.getResource());
		}
	}
	
	/**
	 * Not used, this layout doesn't have constraints.
	 * 
	 * @param pComp not used.
	 * @return always {@code null}.
	 */
	@Override
	public Object getConstraints(IComponent pComp)
	{
		// This layout doesn't have constraints.
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(IComponent pComponent)
	{
		resource.getChildren().remove(pComponent.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAll()
	{
		resource.getChildren().clear();
	}
	
	/**
	 * Not used, this layout doesn't have constraints.
	 * 
	 * @param pComp not used.
	 * @param pConstraints not used.
	 */
	@Override
	public void setConstraints(IComponent pComp, Object pConstraints)
	{
		// This layout doesn't have constraints.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Not used, this layout doesn't use gaps.
	 */
	@Override
	protected void updateGaps()
	{
		// This layout doesn't care about gaps.
	}
	
}	// JavaFXNoLayout
