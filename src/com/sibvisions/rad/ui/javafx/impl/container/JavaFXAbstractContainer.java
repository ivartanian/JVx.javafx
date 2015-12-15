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

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;

import javafx.scene.layout.Region;

import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;

/**
 * The {@link JavaFXAbstractContainer} is the abstract base implementation of
 * {@link IContainer}.
 * <p>
 * It manages the interface methods and a list of components that this container
 * holds and extending classes only need to implement a thin set of functions.
 * 
 * @author Robert Zenz
 * @param <C> the type of the container.
 */
public abstract class JavaFXAbstractContainer<C extends Region> extends JavaFXComponent<C> implements IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link List} of {@link IComponent}s that this container holds. */
	protected List<IComponent> components;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXAbstractContainer}.
	 *
	 * @param pContainer the container.
	 */
	protected JavaFXAbstractContainer(C pContainer)
	{
		super(pContainer);
		
		components = new ArrayList<>();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
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
		
		// We need to "invert" the inserting index because JavaFX draws the last
		// added object. But we want the first object to be drawn.
		if (pIndex >= 0)
		{
			components.add(pIndex, pComponent);
			
			if (isReverseAddOrderNeeded())
			{
				addInternal(pComponent, pConstraints, components.size() - pIndex - 1);
			}
			else
			{
				addInternal(pComponent, pConstraints, pIndex);
			}
		}
		else
		{
			components.add(pComponent);
			
			if (isReverseAddOrderNeeded())
			{
				addInternal(pComponent, pConstraints, 0);
			}
			else
			{
				addInternal(pComponent, pConstraints, pIndex);
			}
		}
		
		pComponent.setParent(this);
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
			
			removeInternal(pComponent);
			
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
		
		removeAllInternal();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The internal add method that extending classes need to implement.
	 * <p>
	 * Is called whenever an {@link IComponent} is added to this container,
	 * implementing classes don't need to add it to the {@link #components
	 * component list} as that already happened.
	 * 
	 * @param pComponent the {@link IComponent} to add.
	 * @param pConstraints the constraints to use.
	 * @param pIndex the index at which to insert the given {@link IComponent}.
	 *            Is {@code -1} for appending at the end.
	 */
	protected abstract void addInternal(IComponent pComponent, Object pConstraints, int pIndex);
	
	/**
	 * If components should be added this container in reverse order.
	 * <p>
	 * If this function returns {@code true}, the index provided to the
	 * {@link #addInternal(IComponent, Object, int)} method will be mangled so
	 * that the add happens in reverse order.
	 *
	 * @return {@code true}, if {@link IComponent}s need to be added in reverse
	 *         order to this container.
	 */
	protected abstract boolean isReverseAddOrderNeeded();
	
	/**
	 * The internal method to remove all {@link IComponent}s from this
	 * container.
	 * <p>
	 * Implementing classes don't need to remove {@link IComponent}s from the
	 * {@link #components component list}.
	 */
	protected abstract void removeAllInternal();
	
	/**
	 * The internal method to remove the given {@link IComponent} from this
	 * container.
	 * <p>
	 * Implementing classes don't need to remove the {@link IComponent} from the
	 * {@link #components component list}.
	 * 
	 * @param pComponent the {@link IComponent} to remove.
	 */
	protected abstract void removeInternal(IComponent pComponent);
	
}	// JavaFXAbstractContainer
