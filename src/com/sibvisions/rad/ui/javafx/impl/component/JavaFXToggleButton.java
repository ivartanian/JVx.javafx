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

import javax.rad.ui.component.IToggleButton;

import javafx.scene.control.ToggleButton;

import com.sibvisions.rad.ui.javafx.ext.FXToggleButton;

/**
 * The {@link JavaFXToggleButton} is the JavaFX specific implementation of
 * {@link IToggleButton}.
 * 
 * @author Robert Zenz
 * @see IToggleButton
 * @see FXToggleButton
 */
public class JavaFXToggleButton extends JavaFXAbstractButtonBase<ToggleButton> implements IToggleButton
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXToggleButton}.
	 */
	public JavaFXToggleButton()
	{
		super(new FXToggleButton());
	}
	
	/**
	 * Creates a new instance of {@link JavaFXToggleButton}.
	 *
	 * @param pComponent the component to use.
	 */
	protected JavaFXToggleButton(ToggleButton pComponent)
	{
		super(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * A ToggleButton being a default button is not possible in JaavFX.
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
	 * A ToggleButton being a default button is not possible in JaavFX.
	 * 
	 * @param pDefault Not used.
	 */
	@Override
	public void setDefaultButton(boolean pDefault)
	{
		// TODO Setting a togglebutton as default button is not possible in JavaFX.
	}
	
	@Override
	public void setSelected(boolean pPressed)
	{
		resource.setSelected(pPressed);
	}
	
}	// JavaFXToggleButton
