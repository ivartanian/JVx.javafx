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
package com.sibvisions.rad.ui.javafx.impl.container;

import javax.rad.ui.container.IGroupPanel;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;

import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;

/**
 * The {@link JavaFXGroupPanel} is the JavaFX specific implementation of
 * {@link IGroupPanel}.
 * 
 * @author Robert Zenz
 * @see IGroupPanel
 * @see TitledPane
 */
public class JavaFXGroupPanel extends JavaFXAbstractForwardingContainer<TitledPane> implements IGroupPanel
{
	/**
	 * The value for the horizontal alignment of the text.
	 * 
	 * @see javax.rad.ui.IAlignmentConstants
	 */
	private int horizontalAlignment = ALIGN_DEFAULT;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXGroupPanel}.
	 */
	public JavaFXGroupPanel()
	{
		super(new TitledPane());
		
		resource.setCollapsible(false);
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
	 * Vertical alignment is not available on JavaFX.
	 * 
	 * @return always {@link javax.rad.ui.IAlignmentConstants#ALIGN_TOP}.
	 */
	@Override
	public int getVerticalAlignment()
	{
		// TODO This method is a stub because vertical alignment is not possible.
		return ALIGN_TOP;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
		
		// TODO Vertically aligning the group title is not possible.
		resource.setAlignment(FXAlignmentUtil.alignmentsToPos(horizontalAlignment, ALIGN_TOP));
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
	 * Vertical alignment is not available on JaavFX.
	 * 
	 * @param pVerticalAlignment the value, which is ignored.
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		// TODO This method is a stub because vertical alignment is not possible.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutInternal(JavaFXILayoutContainerHybrid pHybrid)
	{
		resource.setContent((Node)pHybrid.getResource());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets if the underlying {@link TitledPane} is collapsible.
	 *
	 * @return {@code true} if it is collapsible.
	 * @see TitledPane#isCollapsible()
	 */
	public boolean isCollapsible()
	{
		return resource.isCollapsible();
	}
	
	/**
	 * Sets if the underlying {@link TitledPane} is collapsible.
	 *
	 * @param pCollapsible {@code true} if it should be collapsible.
	 * @see TitledPane#setCollapsible(boolean)
	 */
	public void setCollapsible(boolean pCollapsible)
	{
		resource.setCollapsible(pCollapsible);
	}
	
}	// JavaFXGroupPanel
