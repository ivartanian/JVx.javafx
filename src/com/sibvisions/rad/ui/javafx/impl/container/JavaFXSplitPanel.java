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

import javax.rad.ui.IComponent;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.ISplitPanel;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

import com.sibvisions.rad.ui.javafx.ext.panes.FXBorderPane;

/**
 * The {@link JavaFXSplitPanel} is the JavaFX specific implementation of
 * {@link ISplitPanel}.
 * 
 * @author Robert Zenz
 * @see ISplitPanel
 * @see SplitPane
 */
public class JavaFXSplitPanel extends JavaFXAbstractContainer<SplitPane> implements ISplitPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The alignment of the divider. */
	private int dividerAlignment = DIVIDER_RELATIVE;
	
	/** The first/left/top component in the panel. */
	private IComponent firstComponent;
	
	/** The container for the first component. */
	private FXBorderPane firstContainer;
	
	/** The second/right/bottom component in the panel. */
	private IComponent secondComponent;
	
	/** The container for the second component. */
	private FXBorderPane secondContainer;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXSplitPanel}.
	 */
	public JavaFXSplitPanel()
	{
		super(new SplitPane());
		
		resource.setDividerPositions(0.5d);
		
		// Containers are necessary as otherwise the SplitPane will misbehave,
		// for example the divider can't be moved anymore and some such.
		firstContainer = new FXBorderPane();
		secondContainer = new FXBorderPane();
		
		// Also note that the JavaFX SplitPane can contain a not specified number
		// of items.
		resource.getItems().add(firstContainer);
		resource.getItems().add(secondContainer);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDividerAlignment()
	{
		return dividerAlignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDividerPosition()
	{
		double size = getOrientationBasedSize();
		
		// Note that there should always be a divider.
		return (int)(resource.getDividerPositions()[0] * size);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IComponent getFirstComponent()
	{
		return firstComponent;
	}
	
	/**
	 * Does nothing.
	 * 
	 * @return {@code null}.
	 */
	@Override
	public ILayout<?> getLayout()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrientation()
	{
		if (resource.getOrientation() == Orientation.HORIZONTAL)
		{
			return SPLIT_LEFT_RIGHT;
		}
		else
		{
			return SPLIT_TOP_BOTTOM;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IComponent getSecondComponent()
	{
		return secondComponent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDividerAlignment(int pDividerAlignment)
	{
		dividerAlignment = pDividerAlignment;
		
		SplitPane.setResizableWithParent(firstContainer, Boolean.TRUE);
		SplitPane.setResizableWithParent(secondContainer, Boolean.TRUE);
		
		if (dividerAlignment == DIVIDER_TOP_LEFT)
		{
			SplitPane.setResizableWithParent(firstContainer, Boolean.FALSE);
		}
		else if (dividerAlignment == DIVIDER_BOTTOM_RIGHT)
		{
			SplitPane.setResizableWithParent(secondContainer, Boolean.FALSE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDividerPosition(int pDividerPosition)
	{
		double size = getOrientationBasedSize();
		
		if (size <= 0)
		{
			// HACK SplitPane does only accept a relative position of
			// the splitter in the range of 0-1. JVx API assumes and absolute
			// position from the left/top border.
			getFactory().invokeLater(() ->
			{
				setDividerPosition(pDividerPosition);
			});
		}
		else
		{
			// Note that there always should be a divider.
			resource.setDividerPosition(0, pDividerPosition / size);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFirstComponent(IComponent pComponent)
	{
		firstComponent = pComponent;
		
		if (pComponent != null)
		{
			firstContainer.setCenter((Node)pComponent.getResource());
		}
		else
		{
			firstContainer.setCenter(null);
		}
	}
	
	/**
	 * Not used by this container.
	 * 
	 * @param pLayout not used.
	 */
	@Override
	public void setLayout(@SuppressWarnings("rawtypes") ILayout pLayout)
	{
		// Not needed/used.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOrientation(int pOrientation)
	{
		if (pOrientation == SPLIT_LEFT_RIGHT)
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
	public void setSecondComponent(IComponent pComponent)
	{
		secondComponent = pComponent;
		
		if (pComponent != null)
		{
			secondContainer.setCenter((Node)pComponent.getResource());
		}
		else
		{
			secondContainer.setCenter(null);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addInternal(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (FIRST_COMPONENT.equals(pConstraints))
		{
			setFirstComponent(pComponent);
		}
		else if (SECOND_COMPONENT.equals(pConstraints))
		{
			setSecondComponent(pComponent);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isReverseAddOrderNeeded()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeAllInternal()
	{
		setFirstComponent(null);
		setSecondComponent(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeInternal(IComponent pComponent)
	{
		if (firstComponent == pComponent)
		{
			setFirstComponent(null);
		}
		else if (secondComponent == pComponent)
		{
			setSecondComponent(null);
		}
	}
	
	/**
	 * Gets the orientation based size.
	 *
	 * @return the orientation based size.
	 */
	private double getOrientationBasedSize()
	{
		if (resource.getOrientation() == Orientation.HORIZONTAL)
		{
			return resource.getWidth();
		}
		else
		{
			return resource.getHeight();
		}
	}
	
} // JavaFXSplitPanel
