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
package com.sibvisions.rad.ui.javafx.impl;

import javax.rad.ui.IResource;

import javafx.scene.Node;

/**
 * Encapsulates a JavaFX resource.
 *
 * @param <R> the type of the resource.
 */
public class JavaFXResource<R> implements IResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The key for the {@link Node#getProperties() property} that contains this
	 * wrapper object.
	 */
	public static final String COMPONENT_KEY = "javafx.component";
	
	/** The resource. */
	protected R resource;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXResource}.
	 *
	 * @param pResource the resource.
	 */
	protected JavaFXResource(R pResource)
	{
		setResource(pResource);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getResource()
	{
		return resource;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the current {@link #resource}.
	 * 
	 * @param pResource the new resource
	 */
	protected void setResource(R pResource)
	{
		if (resource != null && resource instanceof Node)
		{
			// Remove this from the properties of the resource.
			((Node)resource).getProperties().remove(COMPONENT_KEY);
		}
		
		resource = pResource;
		
		if (resource instanceof Node)
		{
			// Add this to the properties of the resource.
			((Node)resource).getProperties().put(COMPONENT_KEY, this);
		}
	}
	
}	// JavaFXResource
