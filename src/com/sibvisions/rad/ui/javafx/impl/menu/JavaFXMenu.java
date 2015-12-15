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
package com.sibvisions.rad.ui.javafx.impl.menu;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import javax.rad.ui.IComponent;
import javax.rad.ui.ILayout;
import javax.rad.ui.menu.IMenu;

/**
 * The {@link JavaFXMenu} is the JavaFX specific implementation of {@link IMenu}
 * .
 * 
 * @author Robert Zenz
 * @see IMenu
 * @see Menu
 */
public class JavaFXMenu extends JavaFXMenuItem implements IMenu
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The components. */
	private List<IComponent> components;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXMenu}.
	 */
	public JavaFXMenu()
	{
		super(new Menu());
		
		components = new ArrayList<>();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent)
	{
		add(pComponent, null, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, int pIndex)
	{
		add(pComponent, null, pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, Object pConstraints)
	{
		add(pComponent, pConstraints, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
	{
		// Make sure that the component is not added somewhere else, and if it
		// is we'll remove it from the old parent first.
		if (pComponent.getParent() != null)
		{
			pComponent.getParent().remove(pComponent);
		}
		
		MenuItem componentResource = null;
		
		if (pComponent instanceof JavaFXSeparator)
		{
			componentResource = ((JavaFXSeparator) pComponent).getAsMenuItem();
		}
		else
		{
			componentResource = (MenuItem) pComponent.getResource();
		}
		
		if (pIndex >= 0)
		{
			components.add(pIndex, pComponent);
			((Menu) resource).getItems().add(pIndex, componentResource);
		}
		else
		{
			components.add(pComponent);
			((Menu) resource).getItems().add(componentResource);
		}
		
		pComponent.setParent(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSeparator()
	{
		add(new JavaFXSeparator());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSeparator(int pIndex)
	{
		add(new JavaFXSeparator(), pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IComponent getComponent(int pIndex)
	{
		return components.get(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getComponentCount()
	{
		return components.size();
	}
	
	/**
	 * Not used, always returns {@code null}.
	 * 
	 * @return always {@code null}.
	 */
	@Override
	public ILayout<?> getLayout()
	{
		// NOOP
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(IComponent pComponent)
	{
		return components.indexOf(pComponent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(IComponent pComponent)
	{
		// Make sure that we don't alter a component that is not even our child.
		if (pComponent.getParent() == this)
		{
			components.remove(pComponent);
			
			MenuItem componentResource = null;
			
			if (pComponent instanceof JavaFXSeparator)
			{
				componentResource = ((JavaFXSeparator) pComponent).getAsMenuItem();
			}
			else
			{
				componentResource = (MenuItem) pComponent.getResource();
			}
			
			((Menu) resource).getItems().remove(componentResource);
			pComponent.setParent(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int pIndex)
	{
		remove(components.get(pIndex));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAll()
	{
		while (!components.isEmpty())
		{
			remove(components.get(0));
		}
		
		((Menu) resource).getItems().clear();
	}
	
	/**
	 * Not used.
	 * 
	 * @param pLayout not used.
	 */
	@Override
	public void setLayout(@SuppressWarnings("rawtypes") ILayout pLayout)
	{
		// NOOP
	}
	
}	// JavaFXMenu
