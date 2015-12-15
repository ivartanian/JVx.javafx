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
import javax.rad.ui.IResource;

/**
 * The {@link JavaFXILayoutContainerHybrid} is a hybrid between a container and
 * a layout. Because of the structure of JavaFX Panes, a middle-way between
 * having a fixed container and a container with a layout is needed. The
 * LayoutContainerHybrid is that middle-way.
 * 
 * @author Robert Zenz
 */
public interface JavaFXILayoutContainerHybrid extends IResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given {@link IComponent} with the given constraints and the
	 * given index.
	 * 
	 * @param pComponent the {@link IComponent} to add.
	 * @param pConstraints the constraints to use.
	 * @param pIndex the index at which to insert. Use {@code -1} to append at
	 *            the end.
	 */
	public abstract void add(IComponent pComponent, Object pConstraints, int pIndex);
	
	/**
	 * If components should be added this container in reverse order.
	 *
	 * @return {@code true}, if {@link IComponent}s need to be added in reverse
	 *         order to this container.
	 */
	public abstract boolean isReverseOrderNeeded();
	
	/**
	 * Removes the given {@link IComponent} from the container.
	 *
	 * @param pComponent the {@link IComponent} to remove.
	 */
	public abstract void remove(IComponent pComponent);
	
	/**
	 * Removes all {@link IComponent}s from the container.
	 */
	public abstract void removeAll();
	
}	// JavaFXILayoutContainerHybrid
