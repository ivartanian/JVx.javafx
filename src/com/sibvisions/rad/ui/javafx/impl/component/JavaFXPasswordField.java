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

import javax.rad.ui.component.IPasswordField;

import com.sibvisions.rad.ui.javafx.ext.FXPasswordFieldRT39954;

/**
 * The {@link JavaFXPasswordField} is the JavaFX specific implementation of
 * {@link IPasswordField}. It allows to type passwords (or any text) which will
 * not be echoed in the field.
 * 
 * @author Robert Zenz
 * @see IPasswordField
 * @see javafx.scene.control.PasswordField
 */
public class JavaFXPasswordField extends JavaFXTextField implements IPasswordField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXPasswordField}.
	 */
	public JavaFXPasswordField()
	{
		super(new FXPasswordFieldRT39954());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public char getEchoChar()
	{
		return ((FXPasswordFieldRT39954)resource).getMaskChar();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEchoChar(char pChar)
	{
		((FXPasswordFieldRT39954)resource).setMaskChar(pChar);
	}
	
}	// JavaFXPasswordField
