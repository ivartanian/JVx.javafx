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

import javax.rad.ui.component.IRadioButton;

import com.sibvisions.rad.ui.javafx.ext.FXRadioButton;

/**
 * The {@link JavaFXRadioButton} is the JavaFX specific implementation of
 * {@link IRadioButton}.
 * 
 * @author Robert Zenz
 * @see IRadioButton
 * @see FXRadioButton
 */
public class JavaFXRadioButton extends JavaFXToggleButton implements IRadioButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXRadioButton}.
	 */
	public JavaFXRadioButton()
	{
		super(new FXRadioButton());
	}
	
}	// JavaFXRadioButton
