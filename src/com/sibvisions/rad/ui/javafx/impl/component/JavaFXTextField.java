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

import javax.rad.ui.component.ITextField;

import javafx.scene.control.TextField;

import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link JavaFXTextField} is the JavaFX specific implementation of
 * {@link ITextField}. It allows users to enter text on a single line.
 * 
 * @author Robert Zenz
 * @see ITextField
 * @see TextField
 */
public class JavaFXTextField extends JavaFXAbstractTextInput<TextField> implements ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The value for the horizontal alignment of the text.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	private int horizontalAlignment = ALIGN_DEFAULT;
	
	/**
	 * The value for the vertical alignment of the text.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	private int verticalAlignment = ALIGN_DEFAULT;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXTextField}.
	 */
	public JavaFXTextField()
	{
		super(new TextField());
	}
	
	/**
	 * Creates a new instance of {@link JavaFXTextField}.
	 *
	 * @param pTextFieldComponent the text field component.
	 */
	protected JavaFXTextField(TextField pTextFieldComponent)
	{
		super(pTextFieldComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumns()
	{
		return resource.getPrefColumnCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumns(int pColumns)
	{
		resource.setPrefColumnCount(pColumns);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
		
		resource.setAlignment(FXAlignmentUtil.alignmentsToPos(horizontalAlignment, verticalAlignment));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
		
		resource.setAlignment(FXAlignmentUtil.alignmentsToPos(horizontalAlignment, verticalAlignment));
	}
	
}	// JavaFXTextField
