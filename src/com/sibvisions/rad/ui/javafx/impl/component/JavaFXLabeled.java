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

import javax.rad.ui.component.ILabel;

import javafx.scene.control.Labeled;

import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * {@link JavaFXLabeled} is the JavaFX specific base class for everything that
 * inherits from {@link ILabel}.
 * <p>
 * The abstract base class is necessary as in JavaFX there is the
 * {@link Labeled} abstract class which is used as base for the components
 * instead of {@link javafx.scene.control.Label}.
 *
 * @param <C> the {@link Labeled} component.
 * @author Robert Zenz
 * @see ILabel
 * @see Labeled
 */
public class JavaFXLabeled<C extends Labeled> extends JavaFXComponent<C> implements ILabel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The value for the horizontal alignment of the text.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	protected int horizontalAlignment = ALIGN_DEFAULT;
	
	/**
	 * The value for the vertical alignment of the text.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	protected int verticalAlignment = ALIGN_DEFAULT;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXLabeled}.
	 *
	 * @param resource the resource that is supposed to be used.
	 */
	protected JavaFXLabeled(C resource)
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
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
	
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
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
		
		resource.setAlignment(FXAlignmentUtil.alignmentsToPos(horizontalAlignment, verticalAlignment));
		resource.setTextAlignment(FXAlignmentUtil.alignmentToTextAlignments(horizontalAlignment));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String pText)
	{
		resource.setText(pText);
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
	
}	// JavaFXLabeled
