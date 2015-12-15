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
package com.sibvisions.rad.ui.javafx.impl.layout;

import javax.rad.ui.IComponent;
import javax.rad.ui.layout.IFlowLayout;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFluidFlowPane;
import com.sibvisions.rad.ui.javafx.ext.util.FXAlignmentUtil;

/**
 * The {@link JavaFXFlowLayout} is the JavaFX specific implementation of
 * {@link IFlowLayout}. It allows to layout components in a sequential manner.
 * 
 * @author Robert Zenz
 * @see IFlowLayout
 * @see FXFluidFlowPane
 */
public class JavaFXFlowLayout extends JavaFXAbstractLayoutContainerHybrid<FXFluidFlowPane, Object> implements IFlowLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The value for the alignment of the components. */
	private int componentAlignment = ALIGN_DEFAULT;
	
	/** The value for the horizontal alignment. */
	private int horizontalAlignment = ALIGN_DEFAULT;
	
	/** The value for the vertical alignment. */
	private int verticalAlignment = ALIGN_DEFAULT;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXFlowLayout}.
	 */
	public JavaFXFlowLayout()
	{
		super(new FXFluidFlowPane());
		
		// Default values.
		resource.setAlignment(Pos.CENTER);
		resource.setAutoWrap(false);
		
		horizontalGap = (int)resource.getHGap();
		verticalGap = (int)resource.getVGap();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pIndex >= 0)
		{
			resource.getChildren().add(pIndex, (Node)pComponent.getResource());
		}
		else
		{
			resource.getChildren().add((Node)pComponent.getResource());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getComponentAlignment()
	{
		return componentAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getConstraints(IComponent pComp)
	{
		// This layout doesn't use constraints.
		return null;
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
	public int getOrientation()
	{
		if (resource.getOrientation() == Orientation.HORIZONTAL)
		{
			return HORIZONTAL;
		}
		else
		{
			return VERTICAL;
		}
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
	public boolean isAutoWrap()
	{
		return resource.isAutoWrap();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(IComponent pComponent)
	{
		resource.getChildren().remove(pComponent.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAll()
	{
		resource.getChildren().clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAutoWrap(boolean pAutoWrap)
	{
		resource.setAutoWrap(pAutoWrap);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setComponentAlignment(int pComponentAlignment)
	{
		componentAlignment = pComponentAlignment;
		
		resource.setInlineStretch(componentAlignment == ALIGN_STRETCH);
		resource.setInlineHPos(FXAlignmentUtil.alignmentToHPos(componentAlignment));
		resource.setInlineVPos(FXAlignmentUtil.alignmentToVPos(componentAlignment));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConstraints(IComponent pComp, Object pConstraints)
	{
		// This layout doesn't use constraints.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
		
		resource.setStretchHorizontal(horizontalAlignment == ALIGN_STRETCH);
		resource.setAlignment(FXAlignmentUtil.alignmentsToPos(horizontalAlignment, verticalAlignment));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOrientation(int pOrientation)
	{
		if (pOrientation == HORIZONTAL)
		{
			resource.setOrientation(Orientation.HORIZONTAL);
		}
		else
		{
			resource.setOrientation(Orientation.VERTICAL);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
		
		resource.setStretchVertical(verticalAlignment == ALIGN_STRETCH);
		resource.setAlignment(FXAlignmentUtil.alignmentsToPos(horizontalAlignment, verticalAlignment));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateGaps()
	{
		resource.setHGap(horizontalGap);
		resource.setVGap(verticalGap);
	}
	
}	// JavaFXFlowLayout
