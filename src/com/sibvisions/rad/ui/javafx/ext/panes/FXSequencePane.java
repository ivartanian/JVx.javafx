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
package com.sibvisions.rad.ui.javafx.ext.panes;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * The {@link FXSequencePane} lays out its children in a sequence.
 * 
 * @author Robert Zenz
 */
public class FXSequencePane extends Pane
{
	/** The {@link Orientation} property. */
	private ObjectProperty<Orientation> orientation;
	
	/**
	 * Creates a new instance of {@link FXSequencePane}.
	 */
	public FXSequencePane()
	{
		orientation = new SimpleObjectProperty<>(Orientation.HORIZONTAL);
		orientation.addListener(this::onOrientationChanged);
	}
	
	/**
	 * Gets the orientation.
	 *
	 * @return the orientation.
	 */
	public Orientation getOrientation()
	{
		return orientation.get();
	}
	
	/**
	 * The orientation property.
	 *
	 * @return the orientation property.
	 */
	public ObjectProperty<Orientation> orientationProperty()
	{
		return orientation;
	}
	
	/**
	 * Sets the orientation.
	 *
	 * @param pOrientation the new orientation.
	 */
	public void setOrientation(Orientation pOrientation)
	{
		orientation.set(pOrientation);
	}
	
	/**
	 * Layout children.
	 */
	@Override
	protected void layoutChildren()
	{
		List<Node> managedNodes = getManagedChildren();
		
		if (managedNodes.isEmpty())
		{
			return;
		}
		
		double stretchSize = getStretchSize();
		double nodeSize = getSize() / managedNodes.size();
		double position = 0;
		
		Insets padding = getPadding();
		if (padding != null)
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				position = padding.getLeft();
			}
			else
			{
				position = padding.getTop();
			}
		}
		
		for (Node node : managedNodes)
		{
			resizeRelocateChild(node, position, nodeSize, stretchSize);
			position = position + nodeSize;
		}
	}
	
	/**
	 * Gets the size.
	 *
	 * @return the size.
	 */
	private double getSize()
	{
		Insets padding = getPadding();
		
		if (orientation.get() == Orientation.HORIZONTAL)
		{
			double width = getWidth();
			
			if (padding != null)
			{
				width = width - padding.getLeft() - padding.getRight();
			}
			
			return width;
		}
		else
		{
			double height = getHeight();
			
			if (padding != null)
			{
				height = height - padding.getTop() - padding.getBottom();
			}
			
			return height;
		}
	}
	
	/**
	 * Gets the stretch size.
	 *
	 * @return the stretch size.
	 */
	private double getStretchSize()
	{
		Insets padding = getPadding();
		
		if (orientation.get() == Orientation.HORIZONTAL)
		{
			double height = getHeight();
			
			if (padding != null)
			{
				height = height - padding.getTop() - padding.getBottom();
			}
			
			return height;
		}
		else
		{
			double width = getWidth();
			
			if (padding != null)
			{
				width = width - padding.getLeft() - padding.getRight();
			}
			
			return width;
		}
	}
	
	/**
	 * On orientation changed.
	 *
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onOrientationChanged(ObservableValue<? extends Orientation> pObservable, Orientation pOldValue, Orientation pNewValue)
	{
		requestLayout();
	}
	
	/**
	 * Resizes and relocates the child.
	 *
	 * @param pNode the node.
	 * @param pPosition the position.
	 * @param pNodeSize the node size.
	 * @param pStretchSize the stretch size.
	 */
	private void resizeRelocateChild(Node pNode, double pPosition, double pNodeSize, double pStretchSize)
	{
		double startPosition = 0;
		Insets padding = getPadding();
		
		if (orientation.get() == Orientation.HORIZONTAL)
		{
			if (padding != null)
			{
				startPosition = padding.getTop();
			}
			
			pNode.resizeRelocate(snapPosition(pPosition), snapPosition(startPosition), snapSize(pNodeSize), snapSize(pStretchSize));
		}
		else
		{
			if (padding != null)
			{
				startPosition = padding.getLeft();
			}
			
			pNode.resizeRelocate(snapPosition(startPosition), snapPosition(pPosition), snapSize(pStretchSize), snapSize(pNodeSize));
		}
	}
	
}
