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

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * The {@link FXBorderPane} is a {@link BorderPane} extension that does ignore
 * the maximum size of its children and does not have a minimum size.
 * 
 * @author Robert Zenz
 */
public class FXBorderPane extends BorderPane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXBorderPane}.
	 */
	public FXBorderPane()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link FXBorderPane}.
	 *
	 * @param pCenter the center {@link Node}. Can be {@code null}.
	 */
	public FXBorderPane(Node pCenter)
	{
		super(pCenter);
	}
	
	/**
	 * Creates a new instance of {@link FXBorderPane}.
	 *
	 * @param pCenter the center {@link Node}. Can be {@code null}.
	 * @param pTop the top {@link Node}. Can be {@code null}.
	 * @param pRight the right {@link Node}. Can be {@code null}.
	 * @param pBottom the bottom {@link Node}. Can be {@code null}.
	 * @param pLeft the left {@link Node}. Can be {@code null}.
	 */
	public FXBorderPane(Node pCenter, Node pTop, Node pRight, Node pBottom, Node pLeft)
	{
		super(pCenter, pTop, pRight, pBottom, pLeft);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxHeight(double pWidth)
	{
		return Double.MAX_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMaxWidth(double pHeight)
	{
		return Double.MAX_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinHeight(double pWidth)
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinWidth(double pHeight)
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		Insets padding = getPadding();
		if (padding == null)
		{
			padding = Insets.EMPTY;
		}
		
		Bounds baseBounds = new BoundingBox(
				snapPosition(padding.getLeft()),
				snapPosition(padding.getTop()),
				snapPosition(getWidth() - padding.getLeft() - padding.getRight()),
				snapPosition(getHeight() - padding.getTop() - padding.getBottom()));
				
		double fromTop = layoutTop(baseBounds);
		double fromBottom = layoutBottom(baseBounds);
		double fromLeft = layoutLeft(baseBounds, fromTop, fromBottom);
		double fromRight = layoutRight(baseBounds, fromTop, fromBottom);
		
		layoutCenter(baseBounds, fromTop, fromBottom, fromLeft, fromRight);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the margins of the given {@link Node}, or if that {@link Node} does
	 * not have any margins, returns {@link Insets#EMPTY}.
	 * 
	 * @param pNode the {@link Node} for which to get the margins.
	 * @return the margins of the {@link Node}. {@link Insets#EMPTY} if it
	 *         doesn't have nay.
	 */
	private Insets getMarginSafe(Node pNode)
	{
		Insets margin = getMargin(pNode);
		if (margin != null)
		{
			return margin;
		}
		else
		{
			return Insets.EMPTY;
		}
	}
	
	/**
	 * Lays out the {@link #getBottom() bottom node}.
	 * 
	 * @param pBaseBounds the {@link Bounds} of the surrounding border.
	 * @return the amount of space needed by the bottom node.
	 */
	private double layoutBottom(Bounds pBaseBounds)
	{
		Node node = getBottom();
		
		if (node != null && node.isManaged())
		{
			Insets margin = getMarginSafe(node);
			
			double height = node.prefHeight(pBaseBounds.getWidth() - margin.getLeft() - margin.getRight());
			
			node.resizeRelocate(
					snapPosition(pBaseBounds.getMinX() + margin.getLeft()),
					snapPosition(pBaseBounds.getMaxY() - margin.getBottom() - height),
					snapSize(pBaseBounds.getWidth() - margin.getLeft() - margin.getRight()),
					snapSize(height));
					
			return margin.getBottom() + height + margin.getTop();
		}
		
		return 0;
	}
	
	/**
	 * Lays out the {@link #getCenter() center node}.
	 * 
	 * @param pBaseBounds the {@link Bounds} of the surrounding border.
	 * @param pFromTop the amount of space needed by the {@link #getTop() top
	 *            node}.
	 * @param pFromBottom the amount of space needed by the {@link #getBottom()
	 *            bottom node}.
	 * @param pFromLeft the amount of space needed by the {@link #getLeft() left
	 *            node}.
	 * @param pFromRight the amount of space needed by the {@link #getRight()
	 *            right node}.
	 */
	private void layoutCenter(Bounds pBaseBounds, double pFromTop, double pFromBottom, double pFromLeft, double pFromRight)
	{
		Node node = getCenter();
		
		if (node != null && node.isManaged())
		{
			Insets margin = getMarginSafe(node);
			
			node.resizeRelocate(
					snapPosition(pBaseBounds.getMinX() + pFromLeft + margin.getLeft()),
					snapPosition(pBaseBounds.getMinY() + pFromTop + margin.getTop()),
					snapSize(pBaseBounds.getWidth() - pFromRight - pFromLeft - margin.getLeft() - margin.getRight()),
					snapSize(pBaseBounds.getHeight() - pFromBottom - pFromTop - margin.getTop() - margin.getBottom()));
		}
	}
	
	/**
	 * Lays out the {@link #getLeft() left node}.
	 * 
	 * @param pBaseBounds the {@link Bounds} of the surrounding border.
	 * @param pFromTop the amount of space needed by the {@link #getTop() top
	 *            node}.
	 * @param pFromBottom the amount of space needed by the {@link #getBottom()
	 *            bottom node}.
	 * @return the amount of space needed by the left node.
	 */
	private double layoutLeft(Bounds pBaseBounds, double pFromTop, double pFromBottom)
	{
		Node node = getLeft();
		
		if (node != null && node.isManaged())
		{
			Insets margin = getMarginSafe(node);
			
			double width = node.prefWidth(pBaseBounds.getMaxY() - pFromBottom - pFromTop);
			
			node.resizeRelocate(
					snapPosition(pBaseBounds.getMinX() + margin.getLeft()),
					snapPosition(pBaseBounds.getMinY() + pFromTop + margin.getTop()),
					snapSize(width),
					snapSize(pBaseBounds.getHeight() - pFromBottom - pFromTop - margin.getTop() - margin.getBottom()));
					
			return margin.getLeft() + width + margin.getRight();
		}
		
		return 0;
	}
	
	/**
	 * Lays out the {@link #getRight() right node}.
	 * 
	 * @param pBaseBounds the {@link Bounds} of the surrounding border.
	 * @param pFromTop the amount of space needed by the {@link #getTop() top
	 *            node}.
	 * @param pFromBottom the amount of space needed by the {@link #getBottom()
	 *            bottom node}.
	 * @return the amount of space needed by the right node.
	 */
	private double layoutRight(Bounds pBaseBounds, double pFromTop, double pFromBottom)
	{
		Node node = getRight();
		
		if (node != null && node.isManaged())
		{
			Insets margin = getMarginSafe(node);
			
			double width = node.prefWidth(pBaseBounds.getMaxY() - pFromBottom - pFromTop);
			
			node.resizeRelocate(
					snapPosition(pBaseBounds.getMaxX() - margin.getRight() - width),
					snapPosition(pBaseBounds.getMinY() + pFromTop + margin.getTop()),
					snapSize(width),
					snapSize(pBaseBounds.getHeight() - pFromBottom - pFromTop - margin.getTop() - margin.getBottom()));
					
			return margin.getRight() + width + margin.getLeft();
		}
		
		return 0;
	}
	
	/**
	 * Lays out the {@link #getTop() top node}.
	 * 
	 * @param pBaseBounds the {@link Bounds} of the surrounding border.
	 * @return the amount of space needed by the top node.
	 */
	private double layoutTop(Bounds pBaseBounds)
	{
		Node node = getTop();
		
		if (node != null && node.isManaged())
		{
			Insets margin = getMarginSafe(node);
			
			double height = node.prefHeight(pBaseBounds.getWidth() - margin.getLeft() - margin.getRight());
			
			node.resizeRelocate(
					snapPosition(pBaseBounds.getMinX() + margin.getLeft()),
					snapPosition(pBaseBounds.getMinY() + margin.getTop()),
					snapSize(pBaseBounds.getWidth() - margin.getLeft() - margin.getRight()),
					snapSize(height));
					
			return margin.getTop() + height + margin.getBottom();
		}
		
		return 0;
	}
	
}	// FXBorderPane
