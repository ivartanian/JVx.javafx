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
package com.sibvisions.rad.ui.javafx.impl.component;

import javax.rad.ui.component.ICheckBox;

import com.sibvisions.rad.ui.javafx.ext.FXCheckBox;

/**
 * The {@link JavaFXCheckBox} is the JavaFX specific implementation of
 * {@link ICheckBox}. It allows that the user checks or unchecks a simple box.
 * 
 * @author Robert Zenz
 * @see ICheckBox
 * @see FXCheckBox
 */
public class JavaFXCheckBox extends JavaFXAbstractButtonBase<FXCheckBox> implements ICheckBox
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXCheckBox}.
	 */
	public JavaFXCheckBox()
	{
		super(new FXCheckBox());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * A CheckBox being a default button is not possible in JaavFX.
	 * 
	 * @return Always {@code false}.
	 */
	@Override
	public boolean isDefaultButton()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelected()
	{
		return resource.isSelected();
	}
	
	/**
	 * A CheckBox being a default button is not possible in JaavFX.
	 * 
	 * @param pDefault Not used.
	 */
	@Override
	public void setDefaultButton(boolean pDefault)
	{
		// TODO Setting a checkbox as default button is not possible in JavaFX.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelected(boolean pPressed)
	{
		resource.setSelected(pPressed);
	}
	
}	// JavaFXCheckBox
