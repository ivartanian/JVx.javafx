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

import javax.rad.ui.component.ITextArea;

import com.sibvisions.rad.ui.javafx.ext.FXTextArea;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link JavaFXTextArea} is the JavaFX specific implementation of
 * {@link ITextArea}. It allows to display and edit multiline text.
 * 
 * @author Robert Zenz
 * @see ITextArea
 * @see FXTextArea
 */
public class JavaFXTextArea extends JavaFXAbstractTextInput<FXTextArea> implements ITextArea
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
	
	/**
	 * Creates a new instance of {@link JavaFXTextArea}.
	 */
	public JavaFXTextArea()
	{
		super(new FXTextArea());
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
	public int getRows()
	{
		return resource.getPrefRowCount();
	}
	
	/**
	 * Vertical alignment is at the moment not available on JavaFX.
	 * <p>
	 * <a href="https://javafx-jira.kenai.com/browse/RT-39960">RT-39960</a>
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
	public boolean isWordWrap()
	{
		return resource.isWrapText();
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
		
		resource.setAlignment(FXAlignmentUtil.alignmentToHPos(horizontalAlignment));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRows(int pRows)
	{
		resource.setPrefRowCount(pRows);
	}
	
	/**
	 * Vertical alignment is at the moment not available on JavaFX.
	 * <p>
	 * <a href="https://javafx-jira.kenai.com/browse/RT-39960">RT-39960</a>
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
		
		// TODO The JavaFX TextArea does not support vertical alignment, RT-39960.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWordWrap(boolean pWordWrap)
	{
		resource.setWrapText(pWordWrap);
	}
	
}	// JavaFXTextArea
