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

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;

/**
 * The {@link FXFluidFlowPane} is a {@link Pane} extension that allows to layout
 * its children in a flow-like manner.
 * <p>
 * Compared to the default flow pane available in JavaFX, the
 * {@link FXFluidFlowPane} does not have a preference for the width at which it
 * wraps its children, instead if wraps them as needed.
 * 
 * @author Robert Zenz
 */
public class FXFluidFlowPane extends Pane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The alignment of the children inside this container. */
	private ObjectProperty<Pos> alignment;
	
	/** If wrapping should happen automatically. */
	private BooleanProperty autoWrap;
	
	/** The previously calculated preferred height. */
	private double calculatedPrefHeight;
	
	/** The previously calculated preferred size. */
	private Dimension2D calculatedPrefSize;
	
	/** The previously calculated preferred width. */
	private double calculatedPrefWidth;
	
	/** The horizontal gap between nodes. */
	private DoubleProperty hGap;
	
	/** The inline horizontal alignment for the components in the row/column. */
	private ObjectProperty<HPos> inlineHPos;
	
	/** If components should be stretched to fill the whole row/column. */
	private BooleanProperty inlineStretch;
	
	/** The inlined vertical alignment for the components in the row/column. */
	private ObjectProperty<VPos> inlineVPos;
	
	/** The {@link Orientation} of the flow. */
	private ObjectProperty<Orientation> orientation;
	
	/** The previous height of this. */
	private double previousHeight;
	
	/** The previous width of this. */
	private double previousWidth;
	
	/** The {@link OrientationProxy} that is used. */
	private OrientationProxy proxy;
	
	/** If the layout should be stretched horizontally. */
	private BooleanProperty stretchHorizontal;
	
	/** If the layout should be stretched vertically. */
	private BooleanProperty stretchVertical;
	
	/** The vertical gap between nodes. */
	private DoubleProperty vGap;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXFluidFlowPane}.
	 */
	public FXFluidFlowPane()
	{
		super();
		
		proxy = new OrientationProxy();
		
		alignment = new SimpleObjectProperty<>(Pos.CENTER);
		alignment.addListener(pAlignmentObservable -> requestLayout());
		
		autoWrap = new SimpleBooleanProperty(true);
		autoWrap.addListener(pAutoWrapObservable -> requestLayout());
		
		hGap = new SimpleDoubleProperty(5);
		hGap.addListener(pHGapObservable -> requestLayout());
		
		vGap = new SimpleDoubleProperty(5);
		vGap.addListener(pVGapObservable -> requestLayout());
		
		inlineHPos = new SimpleObjectProperty<>(HPos.CENTER);
		inlineHPos.addListener(pInlineHPosObservable -> requestLayout());
		
		inlineStretch = new SimpleBooleanProperty(false);
		inlineStretch.addListener(pInlineStretchObservable -> requestLayout());
		
		inlineVPos = new SimpleObjectProperty<>(VPos.CENTER);
		inlineVPos.addListener(pInlineVPosObservable -> requestLayout());
		
		orientation = new SimpleObjectProperty<>(Orientation.HORIZONTAL);
		orientation.addListener(pOrientationObservable -> requestLayout());
		
		stretchHorizontal = new SimpleBooleanProperty(false);
		stretchHorizontal.addListener(pStretchHorizontalObservable -> requestLayout());
		
		stretchVertical = new SimpleBooleanProperty(false);
		stretchVertical.addListener(pStretchVerticalObservable -> requestLayout());
	}
	
	/**
	 * Creates a new instance of {@link FXFluidFlowPane}.
	 *
	 * @param children the children to add.
	 */
	public FXFluidFlowPane(Node... children)
	{
		this();
		
		getChildren().addAll(children);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Orientation getContentBias()
	{
		return orientation.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinHeight(double pWidth)
	{
		Insets padding = getPaddingSafe();
		
		double minHeight = NodeUtil.getMaxMinHeight(getManagedChildren());
		minHeight = minHeight + padding.getTop() + padding.getBottom();
		
		return minHeight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinWidth(double pHeight)
	{
		Insets padding = getPaddingSafe();
		
		double minWidth = NodeUtil.getMaxMinWidth(getManagedChildren());
		minWidth = minWidth + padding.getLeft() + padding.getRight();
		
		return minWidth;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double pWidth)
	{
		calculatePreferredSize(pWidth, getHeight());
		
		previousHeight = getHeight();
		
		if (orientation.get() == Orientation.VERTICAL)
		{
			calculatedPrefHeight = NodeUtil.getSumPrefHeight(getManagedChildren(), isSnapToPixel());
			calculatedPrefHeight = calculatedPrefHeight + calculateGap(vGap.get(), getManagedChildren().size());
			
			Insets padding = getPaddingSafe();
			
			calculatedPrefHeight = calculatedPrefHeight + padding.getTop() + padding.getBottom();
			calculatedPrefWidth = calculatedPrefWidth + padding.getLeft() + padding.getRight();
		}
		
		return calculatedPrefHeight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double pHeight)
	{
		calculatePreferredSize(getWidth(), pHeight);
		
		previousWidth = getWidth();
		
		if (orientation.get() == Orientation.HORIZONTAL)
		{
			calculatedPrefWidth = NodeUtil.getSumPrefWidth(getManagedChildren(), isSnapToPixel());
			calculatedPrefWidth = calculatedPrefWidth + calculateGap(hGap.get(), getManagedChildren().size());
			
			Insets padding = getPaddingSafe();
			
			calculatedPrefHeight = calculatedPrefHeight + padding.getTop() + padding.getBottom();
			calculatedPrefWidth = calculatedPrefWidth + padding.getLeft() + padding.getRight();
		}
		
		return calculatedPrefWidth;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		Dimension2D actualLayoutSize = doLayout(getWidth(), getHeight(), true);
		
		boolean secondPassRequired = false;
		
		if (orientation.get() == Orientation.HORIZONTAL)
		{
			if ((calculatedPrefWidth > 0 && actualLayoutSize.getWidth() > calculatedPrefWidth)
					|| (calculatedPrefHeight > 0 && actualLayoutSize.getHeight() != calculatedPrefHeight))
			{
				secondPassRequired = true;
			}
		}
		else
		{
			if ((calculatedPrefWidth > 0 && actualLayoutSize.getWidth() != calculatedPrefWidth)
					|| (calculatedPrefHeight > 0 && actualLayoutSize.getHeight() > calculatedPrefHeight))
			{
				secondPassRequired = true;
			}
		}
		
		if ((previousWidth > 0 && previousWidth != getWidth()) || (previousHeight > 0 && previousHeight != getHeight()))
		{
			secondPassRequired = true;
		}
		
		if (secondPassRequired)
		{
			Platform.runLater(this::requestLayout);
		}
		
		previousWidth = getWidth();
		previousHeight = getHeight();
		
		calculatedPrefSize = null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property for the {@link Pos} of the components within the bounds
	 * of this.
	 * 
	 * @return the property for the {@link Pos} of the components.
	 */
	public ObjectProperty<Pos> alignmentProperty()
	{
		return alignment;
	}
	
	/**
	 * Gets the property for if components should be automatically wrapped into
	 * new lines or columns.
	 * 
	 * @return the property for the autowrap.
	 */
	public BooleanProperty autoWrapProperty()
	{
		return autoWrap;
	}
	
	/**
	 * Gets the alignment/{@link Pos} of the components within the bounds of
	 * this.
	 * 
	 * @return the alignment/{@link Pos}.
	 */
	public Pos getAlignment()
	{
		return alignment.get();
	}
	
	/**
	 * Gets the horizontal gap between components.
	 * 
	 * @return the horizontal gap.
	 */
	public double getHGap()
	{
		return hGap.get();
	}
	
	/**
	 * Gets the {@link HPos horizontal position} of the components within a row.
	 * 
	 * @return the {@link HPos horizontal position}.
	 */
	public HPos getInlineHPos()
	{
		return inlineHPos.get();
	}
	
	/**
	 * Gets the {@link VPos vertical position} of the components within a row.
	 * 
	 * @return the {@link VPos vertical position}.
	 */
	public VPos getInlineVPos()
	{
		return inlineVPos.get();
	}
	
	/**
	 * Gets the {@link Orientation}, meaning if the components are layed out in
	 * rows or columns.
	 * 
	 * @return the {@link Orientation}.
	 */
	public Orientation getOrientation()
	{
		return orientation.get();
	}
	
	/**
	 * Gets the vertical gap between components.
	 * 
	 * @return the vertical gap.
	 */
	public double getVGap()
	{
		return vGap.get();
	}
	
	/**
	 * Gets the property for the horizontal gap between components.
	 * 
	 * @return the property for the horizontal gap.
	 */
	public DoubleProperty hGapProperty()
	{
		return hGap;
	}
	
	/**
	 * Gets the property for the {@link HPos horizontal position} of the
	 * components within a row.
	 * 
	 * @return the property for the {@link HPos horizontal position}.
	 */
	public ObjectProperty<HPos> inlineHPosProperty()
	{
		return inlineHPos;
	}
	
	/**
	 * Gets the property for if components should be stretched to fill the
	 * height of the row or width of a column.
	 * 
	 * @return the property for if components should be stretched.
	 */
	public BooleanProperty inlineStretchProperty()
	{
		return inlineStretch;
	}
	
	/**
	 * Gets the property for the {@link VPos vertical position} of the
	 * components within a row.
	 * 
	 * @return the property for the {@link VPos vertical position}.
	 */
	public ObjectProperty<VPos> inlineVPosProperty()
	{
		return inlineVPos;
	}
	
	/**
	 * Gets if the components are automatically wrapped into new lines or
	 * columns.
	 * 
	 * @return {@code true} if the components are automatically wrapped.
	 */
	public boolean isAutoWrap()
	{
		return autoWrap.get();
	}
	
	/**
	 * Gets if components should be stretched to fill the height of the row or
	 * width of a column.
	 * 
	 * @return {@code true} if components should be stretched.
	 */
	public boolean isInlineStretch()
	{
		return inlineStretch.get();
	}
	
	/**
	 * Gets if the layout should be stretched horizontally.
	 * 
	 * @return {@code true} if the layout should be stretched horizontally.
	 */
	public boolean isStretchHorizontal()
	{
		return stretchHorizontal.get();
	}
	
	/**
	 * Gets if the layout should be stretched vertically.
	 * 
	 * @return {@code true} if the layout should be stretched vertically.
	 */
	public boolean isStretchVertical()
	{
		return stretchVertical.get();
	}
	
	/**
	 * Gets the property for the {@link Orientation}, meaning if the components
	 * are layed out in rows or columns.
	 * 
	 * @return the property for the {@link Orientation}.
	 */
	public ObjectProperty<Orientation> orientationProperty()
	{
		return orientation;
	}
	
	/**
	 * Sets the alignment/{@link Pos} of the components within the bounds of
	 * this.
	 * 
	 * @param pAlignment the alignment/{@link Pos}.
	 */
	public void setAlignment(Pos pAlignment)
	{
		alignment.set(pAlignment);
	}
	
	/**
	 * Sets if the components are automatically wrapped into new lines or
	 * columns.
	 * 
	 * @param pAutoWrap {@code true} if the components are automatically
	 *            wrapped.
	 */
	public void setAutoWrap(boolean pAutoWrap)
	{
		autoWrap.set(pAutoWrap);
	}
	
	/**
	 * Sets the horizontal gap between components.
	 * 
	 * @param pHGap the horizontal gap.
	 */
	public void setHGap(double pHGap)
	{
		hGap.set(pHGap);
	}
	
	/**
	 * Sets the {@link HPos horizontal position} of the components within a row.
	 * 
	 * @param pHPos the {@link HPos horizontal position}.
	 */
	public void setInlineHPos(HPos pHPos)
	{
		inlineHPos.set(pHPos);
	}
	
	/**
	 * Sets if components should be stretched to fill the height of the row or
	 * width of a column.
	 * 
	 * @param pInlineStretch {@code true} if components should be stretched.
	 */
	public void setInlineStretch(boolean pInlineStretch)
	{
		inlineStretch.set(pInlineStretch);
	}
	
	/**
	 * Sets the {@link VPos vertical position} of the components within a row.
	 * 
	 * @param pVPos the {@link VPos vertical position}.
	 */
	public void setInlineVPos(VPos pVPos)
	{
		inlineVPos.set(pVPos);
	}
	
	/**
	 * Sets the {@link Orientation}, meaning if the components are layed out in
	 * rows or columns.
	 * 
	 * @param pOrientation the {@link Orientation}.
	 */
	public void setOrientation(Orientation pOrientation)
	{
		orientation.set(pOrientation);
	}
	
	/**
	 * Sets if the layout should be stretched horizontally.
	 * 
	 * @param pStretchHorizontal {@code true} if the layout should be stretched
	 *            horizontally.
	 */
	public void setStretchHorizontal(boolean pStretchHorizontal)
	{
		stretchHorizontal.set(pStretchHorizontal);
	}
	
	/**
	 * Sets if the layout should be stretched vertically.
	 * 
	 * @param pStretchVertical {@code true} if the layout should be stretched
	 *            vertically.
	 */
	public void setStretchVertical(boolean pStretchVertical)
	{
		stretchVertical.set(pStretchVertical);
	}
	
	/**
	 * Sets the vertical gap between components.
	 * 
	 * @param pVGap the vertical gap.
	 */
	public void setVGap(double pVGap)
	{
		vGap.set(pVGap);
	}
	
	/**
	 * Gets the property for if the layout should be stretched horizontally.
	 * 
	 * @return the property for if the layout should be stretched horizontally.
	 */
	public BooleanProperty stretchHorizontalProperty()
	{
		return stretchHorizontal;
	}
	
	/**
	 * Gets the property for if the layout should be stretched vertically.
	 * 
	 * @return the property for if the layout should be stretched vertically.
	 */
	public BooleanProperty stretchVerticalProperty()
	{
		return stretchVertical;
	}
	
	/**
	 * Gets the property for the vertical gap between components.
	 * 
	 * @return the property for the vertical gap.
	 */
	public DoubleProperty vGapProperty()
	{
		return vGap;
	}
	
	/**
	 * Performs the layout.
	 * 
	 * @param pWidth the width.
	 * @param pHeight the height.
	 * @param pPositionChildren if the children should also be directly
	 *            positioned. {@code true} for the actual layout pass and
	 *            {@code false} if you only want to calculate the size.
	 * @return the size of the layout.
	 */
	protected Dimension2D doLayout(double pWidth, double pHeight, boolean pPositionChildren)
	{
		Insets padding = getPaddingSafe();
		
		double x = 0;
		double y = 0;
		double width = proxy.getWidth(pWidth, pHeight);
		width = width - proxy.getWidth(padding.getLeft() + padding.getRight(), padding.getTop() + padding.getBottom());
		
		double layoutWidth = 0;
		double layoutHeight = 0;
		
		List<LayoutRow> rows = new ArrayList<>();
		LayoutRow currentRow = new LayoutRow();
		rows.add(currentRow);
		
		for (Node child : getManagedChildren())
		{
			double childPrefWidth = snapPosition(proxy.getPrefWidth(child));
			
			if ((x + childPrefWidth) > width
					&& autoWrap.get()
					&& !proxy.isStretchedInSameDirection())
			{
				double currentRowHeight = currentRow.getProxiedMaxPrefHeight();
				
				layoutHeight = snapPosition(layoutHeight + currentRowHeight + proxy.getVGap());
				
				y = snapPosition(y + currentRowHeight + proxy.getVGap());
				x = 0;
				
				currentRow = new LayoutRow();
				rows.add(currentRow);
			}
			
			currentRow.add(child);
			
			if (pPositionChildren)
			{
				child.autosize();
				proxy.relocate(child, x, y);
			}
			
			x = x + childPrefWidth;
			
			if (x > layoutWidth)
			{
				layoutWidth = x;
			}
			
			x = snapPosition(x + proxy.getHGap());
		}
		
		layoutHeight = layoutHeight + snapPosition(currentRow.getProxiedMaxPrefHeight());
		
		Dimension2D layoutSize = proxy.createDimension(layoutWidth, layoutHeight);
		
		if (pPositionChildren)
		{
			doAlignment(layoutSize, rows);
		}
		
		return layoutSize;
	}
	
	/**
	 * Calculates the sum of all gaps for the given child count.
	 * 
	 * @param pBaseGapValue the size of the gap.
	 * @param pChildCount the count of children.
	 * @return the sum of all gaps for the given child count.
	 */
	private double calculateGap(double pBaseGapValue, int pChildCount)
	{
		if (pBaseGapValue != 0 && pChildCount > 1)
		{
			return pBaseGapValue * (pChildCount - 1);
		}
		
		return 0;
	}
	
	/**
	 * Calculates the preferred size of this if needed.
	 * 
	 * @param pWidth the width.
	 * @param pHeight the height.
	 */
	private void calculatePreferredSize(double pWidth, double pHeight)
	{
		if (calculatedPrefSize == null)
		{
			calculatedPrefSize = doLayout(pWidth, pHeight, false);
			
			calculatedPrefHeight = calculatedPrefSize.getHeight();
			calculatedPrefWidth = calculatedPrefSize.getWidth();
		}
	}
	
	/**
	 * Performs the alignment of the children.
	 * 
	 * @param pLayoutSize the size of the layout.
	 * @param pRows the rows.
	 */
	private void doAlignment(Dimension2D pLayoutSize, List<LayoutRow> pRows)
	{
		doStretchSameDirection(pRows);
		doAlignmentInLayout(pLayoutSize, pRows);
		doStretchOtherDirection(pLayoutSize, pRows);
		doAlignmentInContainer(pLayoutSize);
	}
	
	/**
	 * Aligns all children according to the {@link #alignmentProperty()} inside
	 * the container.
	 * 
	 * @param pLayoutSize the size of the layout.
	 */
	private void doAlignmentInContainer(Dimension2D pLayoutSize)
	{
		Insets padding = getPaddingSafe();
		
		double height = getHeight();
		double width = getWidth();
		
		if (pLayoutSize.getWidth() < width || pLayoutSize.getHeight() < height)
		{
			double offsetX = padding.getLeft();
			double offsetY = padding.getTop();
			
			if (!stretchHorizontal.get())
			{
				switch (alignment.get())
				{
					case BASELINE_CENTER:
					case BOTTOM_CENTER:
					case CENTER:
					case TOP_CENTER:
						// To be honest, I have no idea why I need to add
						// the top value here. Smells in the first moment like
						// I managed to switch top/left at some point, but as
						// you can see further down below a similar hack is not
						// needed for the y direction. That rules out that I
						// swapped something somwhere, or at least I believe so.
						offsetX = (width - pLayoutSize.getWidth()) / 2 + padding.getTop();
						break;
						
					case BASELINE_LEFT:
					case BOTTOM_LEFT:
					case CENTER_LEFT:
					case TOP_LEFT:
						offsetX = padding.getLeft();
						break;
						
					case BASELINE_RIGHT:
					case BOTTOM_RIGHT:
					case CENTER_RIGHT:
					case TOP_RIGHT:
						offsetX = width - pLayoutSize.getWidth() - padding.getRight();
						break;
						
					default:
						// Nothing...
						
				}
			}
			
			if (!stretchVertical.get())
			{
				switch (alignment.get())
				{
					case BOTTOM_CENTER:
					case BOTTOM_LEFT:
					case BOTTOM_RIGHT:
						offsetY = height - pLayoutSize.getHeight() - padding.getBottom();
						break;
						
					case CENTER:
					case CENTER_LEFT:
					case CENTER_RIGHT:
						offsetY = (height - pLayoutSize.getHeight()) / 2 - padding.getTop();
						break;
						
					case TOP_CENTER:
					case TOP_LEFT:
					case TOP_RIGHT:
						offsetY = padding.getTop();
						break;
						
					case BASELINE_CENTER:
					case BASELINE_LEFT:
					case BASELINE_RIGHT:
						// TODO Baseline alignment.
						offsetY = 0;
						break;
						
					default:
						// Nothing...
						
				}
			}
			
			offsetX = snapPosition(offsetX);
			offsetY = snapPosition(offsetY);
			
			for (Node child : getManagedChildren())
			{
				child.relocate(child.getLayoutX() + offsetX, child.getLayoutY() + offsetY);
			}
		}
	}
	
	/**
	 * Aligns the children.
	 * 
	 * @param pLayoutSize the layout size.
	 * @param pRows the rows.
	 */
	private void doAlignmentInLayout(Dimension2D pLayoutSize, List<LayoutRow> pRows)
	{
		for (LayoutRow row : pRows)
		{
			doAlignmentInRow(row);
		}
		
		for (LayoutRow row : pRows)
		{
			double rowWidth = row.getWidth();
			double rowHeight = row.getHeight();
			
			double offsetX = 0;
			double offsetY = 0;
			
			if (rowWidth < pLayoutSize.getWidth() && orientation.get() == Orientation.HORIZONTAL)
			{
				switch (alignment.get())
				{
					case BASELINE_CENTER:
					case BOTTOM_CENTER:
					case CENTER:
					case TOP_CENTER:
						offsetX = (pLayoutSize.getWidth() - rowWidth) / 2;
						break;
						
					case BASELINE_LEFT:
					case BOTTOM_LEFT:
					case CENTER_LEFT:
					case TOP_LEFT:
						offsetX = 0;
						break;
						
					case BASELINE_RIGHT:
					case BOTTOM_RIGHT:
					case CENTER_RIGHT:
					case TOP_RIGHT:
						offsetX = pLayoutSize.getWidth() - rowWidth;
						break;
						
					default:
						// Nothing...
						
				}
			}
			
			if (rowHeight < pLayoutSize.getHeight() && orientation.get() == Orientation.VERTICAL)
			{
				switch (alignment.get())
				{
					case BOTTOM_CENTER:
					case BOTTOM_LEFT:
					case BOTTOM_RIGHT:
						offsetY = pLayoutSize.getHeight() - rowHeight;
						break;
						
					case CENTER:
					case CENTER_LEFT:
					case CENTER_RIGHT:
						offsetY = (pLayoutSize.getHeight() - rowHeight) / 2;
						break;
						
					case TOP_CENTER:
					case TOP_LEFT:
					case TOP_RIGHT:
						offsetY = 0;
						break;
						
					case BASELINE_CENTER:
					case BASELINE_LEFT:
					case BASELINE_RIGHT:
						// TODO Baseline alignment.
						offsetY = 0;
						break;
						
					default:
						// Nothing...
						
				}
			}
			
			for (Node child : row.getNodes())
			{
				child.relocate(child.getLayoutX() + offsetX, child.getLayoutY() + offsetY);
			}
		}
	}
	
	/**
	 * Aligns the children inside the row/column.
	 * 
	 * @param pRow the row.
	 */
	private void doAlignmentInRow(LayoutRow pRow)
	{
		double rowWidth = snapPosition(pRow.getMaxWidth());
		double rowHeight = snapPosition(pRow.getMaxHeight());
		
		for (Node child : pRow.getNodes())
		{
			if (inlineStretch.get())
			{
				double width = child.getLayoutBounds().getWidth();
				double height = child.getLayoutBounds().getHeight();
				
				if (orientation.get() == Orientation.HORIZONTAL)
				{
					height = rowHeight;
				}
				else
				{
					width = rowWidth;
				}
				
				child.resize(width, height);
			}
			else
			{
				double offsetX = 0;
				double offsetY = 0;
				
				if (orientation.get() == Orientation.HORIZONTAL)
				{
					switch (inlineVPos.get())
					{
						case BOTTOM:
							offsetY = rowHeight - NodeUtil.getPrefHeight(child);
							break;
							
						case CENTER:
							offsetY = (rowHeight - NodeUtil.getPrefHeight(child)) / 2;
							break;
							
						case TOP:
							offsetY = 0;
							break;
							
						case BASELINE:
							// TODO Baseline alignment.
							offsetY = 0;
							break;
							
						default:
							// Nothing...
							
					}
					
					offsetY = snapPosition(offsetY);
				}
				else
				{
					switch (inlineHPos.get())
					{
						case CENTER:
							offsetX = (rowWidth - NodeUtil.getPrefWidth(child)) / 2;
							break;
							
						case LEFT:
							offsetX = 0;
							break;
							
						case RIGHT:
							offsetX = rowWidth - NodeUtil.getPrefWidth(child);
							break;
							
						default:
							// Nothing...
							
					}
					
					offsetX = snapPosition(offsetX);
				}
				
				child.relocate(child.getLayoutX() + offsetX, child.getLayoutY() + offsetY);
			}
		}
	}
	
	/**
	 * Stretches the children in the other direction as the direction of the
	 * container, if required.
	 * 
	 * @param pLayoutSize the size of the layout.
	 * @param pRows the rows.
	 */
	private void doStretchOtherDirection(Dimension2D pLayoutSize, List<LayoutRow> pRows)
	{
		Insets padding = getPaddingSafe();
		
		double widthFactor = 1;
		double heightFactor = 1;
		
		if (orientation.get() == Orientation.HORIZONTAL
				&& stretchVertical.get())
		{
			double height = getHeight() - padding.getTop() - padding.getBottom();
			heightFactor = height / pLayoutSize.getHeight();
		}
		
		if (orientation.get() == Orientation.VERTICAL
				&& stretchHorizontal.get())
		{
			double width = getWidth() - padding.getLeft() - padding.getRight();
			widthFactor = width / pLayoutSize.getWidth();
		}
		
		for (LayoutRow row : pRows)
		{
			for (Node node : row.getNodes())
			{
				node.resizeRelocate(
						node.getLayoutX() * widthFactor,
						node.getLayoutY() * heightFactor,
						node.getLayoutBounds().getWidth() * widthFactor,
						node.getLayoutBounds().getHeight() * heightFactor);
			}
		}
	}
	
	/**
	 * Stretches the children in the same direction as the direction of the
	 * container, if required.
	 * 
	 * @param pRows the rows.
	 */
	private void doStretchSameDirection(List<LayoutRow> pRows)
	{
		Insets padding = getPaddingSafe();
		
		LayoutRow row = pRows.get(0);
		
		if (orientation.get() == Orientation.HORIZONTAL
				&& stretchHorizontal.get())
		{
			double current = padding.getLeft();
			double width = getWidth() - padding.getLeft() - padding.getRight();
			double childWidth = snapSize(width / row.getNodes().size());
			
			for (Node node : row.getNodes())
			{
				node.resizeRelocate(
						current,
						node.getLayoutY(),
						childWidth,
						node.getLayoutBounds().getHeight());
						
				current = snapPosition(current + childWidth);
			}
		}
		
		if (orientation.get() == Orientation.VERTICAL
				&& stretchVertical.get())
		{
			double current = padding.getTop();
			double height = getHeight() - padding.getTop() - padding.getBottom();
			double childHeight = snapSize(height / row.getNodes().size());
			
			for (Node node : row.getNodes())
			{
				node.resizeRelocate(
						node.getLayoutX(),
						current,
						node.getLayoutBounds().getWidth(),
						childHeight);
						
				current = snapPosition(current + childHeight);
			}
		}
	}
	
	/**
	 * Gets the {@link #getPadding()}, if there is no padding set,
	 * {@link Insets#EMPTY} will be returned.
	 * 
	 * @return the {@link #getPadding()} or {@link Insets#EMPTY}.
	 */
	private Insets getPaddingSafe()
	{
		Insets padding = getPadding();
		
		if (padding != null)
		{
			return padding;
		}
		else
		{
			return Insets.EMPTY;
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple container the encapsulates one row/column of the layout.
	 * 
	 * @author Robert Zenz
	 */
	private final class LayoutRow
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link List} of {@link Node}s that represent this row. */
		private List<Node> nodes;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link LayoutRow}.
		 */
		public LayoutRow()
		{
			nodes = new ArrayList<>();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Adds the given {@link Node} to this row.
		 * 
		 * @param pNode the {@link Node} to add.
		 */
		public void add(Node pNode)
		{
			nodes.add(pNode);
		}
		
		/**
		 * Gets the height of this row.
		 * 
		 * @return the height.
		 */
		public double getHeight()
		{
			return NodeUtil.getSumPrefHeight(nodes, isSnapToPixel()) + calculateGap(vGap.get(), nodes.size());
		}
		
		/**
		 * Gets the max height of this row.
		 * 
		 * @return the max height.
		 */
		public double getMaxHeight()
		{
			double maxPrefHeight = NodeUtil.getMaxPrefHeight(nodes);
			
			if (orientation.get() == Orientation.VERTICAL)
			{
				maxPrefHeight = maxPrefHeight + calculateGap(vGap.get(), nodes.size());
			}
			
			return maxPrefHeight;
		}
		
		/**
		 * Gets the max width of this row.
		 * 
		 * @return the max width.
		 */
		public double getMaxWidth()
		{
			double width = NodeUtil.getMaxPrefWidth(nodes);
			
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				width = width + calculateGap(hGap.get(), nodes.size());
			}
			
			return width;
		}
		
		/**
		 * Gets the {@link List} of {@link Node}s that make up this row.
		 * 
		 * @return the {@link Node}s.
		 */
		public List<Node> getNodes()
		{
			return nodes;
		}
		
		/**
		 * Gets the maximum preferred height based on the current orientation.
		 * 
		 * @return the maximum preferred height.
		 */
		public double getProxiedMaxPrefHeight()
		{
			return proxy.getMaxPrefHeight(nodes);
		}
		
		/**
		 * Gets the width of this row.
		 * 
		 * @return the width.
		 */
		public double getWidth()
		{
			return NodeUtil.getSumPrefWidth(nodes, isSnapToPixel()) + calculateGap(hGap.get(), nodes.size());
		}
		
	}	// LayoutRow
	
	/**
	 * The {@link OrientationProxy} is a simple helper utility that wraps the
	 * logic if width or height should be used based on the {@link Orientation}.
	 * 
	 * @author Robert Zenz
	 */
	private final class OrientationProxy
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link OrientationProxy}.
		 */
		public OrientationProxy()
		{
			// Empty on purpose.
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a {@link Dimension2D} from the given values.
		 * 
		 * @param pWidth the width.
		 * @param pHeight the height.
		 * @return the {@link Dimension2D}.
		 */
		public Dimension2D createDimension(double pWidth, double pHeight)
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				return new Dimension2D(pWidth, pHeight);
			}
			else
			{
				return new Dimension2D(pHeight, pWidth);
			}
		}
		
		/**
		 * Gets the horizontal gap.
		 * 
		 * @return the horizontal gap.
		 */
		public double getHGap()
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				return hGap.get();
			}
			else
			{
				return vGap.get();
			}
		}
		
		/**
		 * Gets the maximum preferred height.
		 * 
		 * @param pNodes the {@link Node}s.
		 * @return the maximum preferred height.
		 */
		public double getMaxPrefHeight(Iterable<Node> pNodes)
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				return NodeUtil.getMaxPrefHeight(pNodes);
			}
			else
			{
				return NodeUtil.getMaxPrefWidth(pNodes);
			}
		}
		
		/**
		 * Gets the preferred width.
		 * 
		 * @param pNode the {@link Node}s.
		 * @return the preferred width.
		 */
		public double getPrefWidth(Node pNode)
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				return NodeUtil.getPrefWidth(pNode);
			}
			else
			{
				return NodeUtil.getPrefHeight(pNode);
			}
		}
		
		/**
		 * Gets the vertical gap.
		 * 
		 * @return the vertical gap.
		 */
		public double getVGap()
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				return vGap.get();
			}
			else
			{
				return hGap.get();
			}
		}
		
		/**
		 * Gets the width.
		 * 
		 * @param pWidth the width.
		 * @param pHeight the height.
		 * @return the width.
		 */
		public double getWidth(double pWidth, double pHeight)
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				if (pWidth < 0)
				{
					return FXFluidFlowPane.this.getWidth();
				}
				else
				{
					return pWidth;
				}
			}
			else
			{
				if (pHeight < 0)
				{
					return FXFluidFlowPane.this.getHeight();
				}
				else
				{
					return pHeight;
				}
			}
		}
		
		/**
		 * Gets if the stretch is set for the same direction as the layout.
		 * 
		 * @return {@code true} if it stretched in the same direction as the
		 *         layout.
		 */
		public boolean isStretchedInSameDirection()
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				return stretchHorizontal.get();
			}
			else
			{
				return stretchVertical.get();
			}
		}
		
		/**
		 * Relocates the given {@link Node} to the given coordinates.
		 * 
		 * @param pNode the {@link Node}.
		 * @param x the x coordinate.
		 * @param y the y coordinate.
		 */
		public void relocate(Node pNode, double x, double y)
		{
			if (orientation.get() == Orientation.HORIZONTAL)
			{
				pNode.relocate(x, y);
			}
			else
			{
				pNode.relocate(y, x);
			}
		}
		
	}	// OrientationProxy
	
}	// FXFluidFlowPane
