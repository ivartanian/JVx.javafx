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
package com.sibvisions.rad.ui.javafx.ext;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

import com.sun.javafx.scene.control.skin.TextFieldSkin;

/**
 * A {@link PasswordField} extension that allows to set a custom mask character.
 * <p>
 * <a href="https://javafx-jira.kenai.com/browse/RT-39954">RT-39954</a>
 * 
 * @author Robert Zenz
 */
public class FXPasswordFieldRT39954 extends PasswordField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The default mask character. */
	private static final char DEFAULT_MASK_CHAR = '\u2022';
	
	/** The mask character. */
	private StringProperty maskChar;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXPasswordFieldRT39954}.
	 */
	public FXPasswordFieldRT39954()
	{
		super();
		
		maskChar = new SimpleStringProperty();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Skin<?> createDefaultSkin()
	{
		return new CustomMaskTextFieldSkin(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the mask character.
	 *
	 * @return the mask character.
	 */
	public char getMaskChar()
	{
		String maskCharValue = maskChar.get();
		
		if (maskCharValue == null || maskCharValue.isEmpty())
		{
			return DEFAULT_MASK_CHAR;
		}
		else
		{
			return maskCharValue.charAt(0);
		}
	}
	
	/**
	 * The property for the mask character. Only the first char of the value
	 * will be used. If the value is null or empty, the default will be used.
	 * 
	 * @return the property for the mask character.
	 */
	public StringProperty maskCharProperty()
	{
		return maskChar;
	}
	
	/**
	 * Sets the mask character.
	 *
	 * @param pMaskChar the new mask character.
	 */
	public void setMaskChar(char pMaskChar)
	{
		maskChar.set(Character.toString(pMaskChar));
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * An {@link TextFieldSkin} that allows to use the custom mask character.
	 */
	private final class CustomMaskTextFieldSkin extends TextFieldSkin
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link CustomMaskTextFieldSkin}.
		 *
		 * @param pTextField the text field.
		 */
		public CustomMaskTextFieldSkin(TextField pTextField)
		{
			super(pTextField);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected String maskText(String pTxt)
		{
			if (pTxt == null)
			{
				return null;
			}
			
			StringBuilder maskedText = new StringBuilder();
			char maskCharacter = getMaskChar();
			
			for (int idx = 0; idx < pTxt.length(); idx++)
			{
				maskedText.append(maskCharacter);
			}
			
			return maskedText.toString();
		}
		
	}	// CustomMaskTextFieldSkin
	
}	// FXPasswordFieldRT39954
