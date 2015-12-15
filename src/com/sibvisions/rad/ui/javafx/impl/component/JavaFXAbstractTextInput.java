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

import javax.rad.ui.IColor;
import javax.rad.ui.component.ITextField;

import javafx.scene.control.TextInputControl;

/**
 * {@link JavaFXAbstractTextInput} is the JavaFX specific abstract base class
 * for text input components, like a text field, password field or text area.
 * <p>
 * The abstract base class is necessary as JavaFX only knows the
 * {@link TextInputControl} from which all text input controls are inheriting.
 * 
 * @author Robert Zenz
 * @param <C> the {@link TextInputControl} component.
 * @see ITextField
 * @see TextInputControl
 */
public abstract class JavaFXAbstractTextInput<C extends TextInputControl> extends JavaFXComponent<C> implements ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXAbstractTextInput}.
	 *
	 * @param resource the resource.
	 */
	protected JavaFXAbstractTextInput(C resource)
	{
		super(resource);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText()
	{
		return resource.getText();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBorderVisible()
	{
		return !styleContainer.isSet("-fx-text-box-border");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEditable()
	{
		return resource.isEditable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectAll()
	{
		resource.selectAll();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBorderVisible(boolean pVisible)
	{
		if (pVisible)
		{
			styleContainer.clear("-fx-text-box-border");
			styleContainer.clear("-fx-focus-color");
		}
		else
		{
			styleContainer.set("-fx-text-box-border", "transparent");
			styleContainer.set("-fx-focus-color", "transparent");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEditable(boolean pEditable)
	{
		resource.setEditable(pEditable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String pText)
	{
		resource.setText(pText);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(IColor pBackground)
	{
		backgroundColor = pBackground;
		
		styleContainer.setInnerBackground(backgroundColor);
	}
	
}	// JavaFXAbstractTextInput
