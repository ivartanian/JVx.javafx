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
package com.sibvisions.rad.ui.javafx.impl.component;

import javax.rad.ui.component.ILabel;

import javafx.scene.control.Label;

/**
 * The {@link JavaFXLabel} is the {@link ILabel} implementation for JavaFX. It
 * displays some (most of the time) short text and does normally not react to
 * input of any kind.
 * 
 * @author Robert Zenz
 * @see ILabel
 * @see Label
 */
public class JavaFXLabel extends JavaFXLabeled<Label> implements ILabel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXLabel}.
	 */
	public JavaFXLabel()
	{
		this(new Label());
	}
	
	/**
	 * Creates a new instance of {@link JavaFXLabel} for a {@link Label}.
	 * 
	 * @param pLabel the {@link Label}.
	 */
	protected JavaFXLabel(Label pLabel)
	{
		super(pLabel);
	}
	
}	// JavaFXLabel
