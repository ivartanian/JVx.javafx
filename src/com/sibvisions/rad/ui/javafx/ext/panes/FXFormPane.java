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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;

/**
 * The {@link FXFormPane} is a {@link Pane} extension that allows to layout its
 * children in complex, form like layouts.
 * <p>
 * It has two operation modes. The first being a simply row/column based
 * approach that allows to add components like into any other {@link Pane}.
 * After {@link #newlineCountProperty() a certain amount of components in one
 * row}, a new row is started.
 * <p>
 * The second mode is that each {@link Constraints} is defined by hand, allowing
 * fine-grained control over the complete alyout and where what component is
 * placed.
 * 
 * @author Robert Zenz
 */
public class FXFormPane extends Pane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The {@link Constraints} that contain the {@link Anchor}s used for the
	 * border.
	 */
	private Constraints borderAnchors;
	
	/** If the bottom border is actually used. */
	private boolean bottomBorderUsed = false;
	
	/** The default {@link Anchor}s for the bottom. */
	private List<Anchor> bottomDefaultAnchors = new ArrayList<>();
	
	/**
	 * If the {@link Anchor}s that are target dependent need to be recalculated.
	 */
	private boolean calculateTargetDependentAnchors = false;
	
	/**
	 * A list of added child nodes, only used to get the correct "last"
	 * constraint.
	 */
	private List<Node> childrenForConstraintsOrder;
	
	/** The {@link Map} for mapping {@link Node}s to {@link Constraints}. */
	private Map<Node, Constraints> constraints = new HashMap<>();
	
	/** The property for the horizontal gap between components. */
	private DoubleProperty hgap;
	
	/** The horizontal alignment of the layout. */
	private ObjectProperty<HorizontalAlignment> horizontalAlignment;
	
	/** All vertical {@link Anchor}s. */
	private List<Anchor> horizontalAnchors = new ArrayList<>();
	
	/** If the left border is actually used. */
	private boolean leftBorderUsed = false;
	
	/** The default {@link Anchor}s for the left. */
	private List<Anchor> leftDefaultAnchors = new ArrayList<>();
	
	/**
	 * The {@link Constraints} that contain the {@link Anchor}s used for the
	 * margin.
	 */
	private Constraints marginAnchors;
	
	/** The (calculated) minimum height of the complete layout. */
	private double minimumHeight = 0;
	
	/** The (calculated) minimum width of the complete layout. */
	private double minimumWidth = 0;
	
	/**
	 * The property for the count on how many components should go into one line
	 * before a line break is inserted.
	 */
	private IntegerProperty newlineCount;
	
	/** The (calculated) preferred height of the complete layout. */
	private double preferredHeight = 0;
	
	/** The (calculated) preferred width of the complete layout. */
	private double preferredWidth = 0;
	
	/** If the right border is actually used. */
	private boolean rightBorderUsed = false;
	
	/** The default {@link Anchor}s for the right. */
	private List<Anchor> rightDefaultAnchors = new ArrayList<>();
	
	/** If the top border is actually used. */
	private boolean topBorderUsed = false;
	
	/** The default {@link Anchor}s for the top. */
	private List<Anchor> topDefaultAnchors = new ArrayList<>();
	
	/** The vertical alignment of the layout. */
	private ObjectProperty<VerticalAlignment> verticalAlignment;
	
	/** All vertical {@link Anchor}s. */
	private List<Anchor> verticalAnchors = new ArrayList<>();
	
	/** The property for the vertical gap between components. */
	private DoubleProperty vgap;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXFormPane}.
	 */
	public FXFormPane()
	{
		super();
		
		borderAnchors = new Constraints(
				new Anchor(this, Orientation.VERTICAL),
				new Anchor(this, Orientation.HORIZONTAL),
				new Anchor(this, Orientation.VERTICAL),
				new Anchor(this, Orientation.HORIZONTAL));
				
		childrenForConstraintsOrder = new ArrayList<>();
		
		hgap = new SimpleDoubleProperty(5);
		hgap.addListener(this::onPropertyChangedRequestLayout);
		
		horizontalAlignment = new SimpleObjectProperty<>(HorizontalAlignment.STRETCH);
		horizontalAlignment.addListener(this::onPropertyChangedRequestLayout);
		
		marginAnchors = new Constraints(
				new Anchor(borderAnchors.topAnchor, 10),
				new Anchor(borderAnchors.leftAnchor, 10),
				new Anchor(borderAnchors.bottomAnchor, -10),
				new Anchor(borderAnchors.rightAnchor, -10));
				
		newlineCount = new SimpleIntegerProperty(2);
		
		vgap = new SimpleDoubleProperty(5);
		vgap.addListener(this::onPropertyChangedRequestLayout);
		
		verticalAlignment = new SimpleObjectProperty<>(VerticalAlignment.STRETCH);
		verticalAlignment.addListener(this::onPropertyChangedRequestLayout);
		
		getChildren().addListener(this::onChildrenChanged);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinHeight(double width)
	{
		calculateAnchors();
		
		return minimumHeight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computeMinWidth(double height)
	{
		calculateAnchors();
		
		return minimumWidth;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefHeight(double width)
	{
		calculateAnchors();
		
		return preferredHeight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double computePrefWidth(double height)
	{
		calculateAnchors();
		
		return preferredWidth;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
		
		calculateAnchors();
		resizeAndRelocateChildren();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given {@link Node} as child to the pane.
	 * 
	 * @param pIndex the index of the child in the {@link #getChildren()
	 *            children list}.
	 * @param pNode the {@link Node} to add.
	 */
	public void addChild(int pIndex, Node pNode)
	{
		addChild(pIndex, pNode, null);
	}
	
	/**
	 * Adds the given {@link Node} as child to the pane.
	 * 
	 * @param pIndex the index of the child in the {@link #getChildren()
	 *            children list}.
	 * @param pNode the {@link Node} to add.
	 * @param pConstraint the {@link Constraints} for the child.
	 */
	public void addChild(int pIndex, Node pNode, Constraints pConstraint)
	{
		Constraints constraint = pConstraint;
		
		if (constraint == null)
		{
			constraint = getNextConstraints();
		}
		
		if (constraint == null)
		{
			throw new IllegalArgumentException("Constraint " + constraint + " is not allowed!");
		}
		
		if (constraint.getLeftAnchor().getFormPane() != this
				|| constraint.getRightAnchor().getFormPane() != this
				|| constraint.getTopAnchor().getFormPane() != this
				|| constraint.getBottomAnchor().getFormPane() != this)
		{
			throw new IllegalArgumentException("Constraint " + constraint + " has anchors for another layout!");
		}
		
		constraints.put(pNode, constraint);
		
		if (pIndex >= 0)
		{
			getChildren().add(pIndex, pNode);
		}
		else
		{
			getChildren().add(pNode);
		}
		
		requestLayout();
	}
	
	/**
	 * Adds the given {@link Node} as child to the pane.
	 * 
	 * @param pNode the {@link Node} to add.
	 */
	public void addChild(Node pNode)
	{
		addChild(-1, pNode, null);
	}
	
	/**
	 * Adds the given {@link Node} as child to the pane.
	 * 
	 * @param pNode the {@link Node} to add.
	 * @param pConstraint the {@link Constraints} for the child.
	 */
	public void addChild(Node pNode, Constraints pConstraint)
	{
		addChild(-1, pNode, pConstraint);
	}
	
	/**
	 * Adds the given {@link Node} as child to the pane in a new line.
	 * 
	 * @param pIndex the index of the child in the {@link #getChildren()
	 *            children list}.
	 * @param pNode the {@link Node} to add.
	 */
	public void addChildInNewLine(int pIndex, Node pNode)
	{
		Constraints constraint = null;
		
		if (getChildren().size() >= 1)
		{
			Constraints constraintsBefore = getPreviousConstraints();
			
			if (constraintsBefore != null)
			{
				int row = topDefaultAnchors.indexOf(constraintsBefore.topAnchor) / 2;
				constraint = createConstraint(0, row + 1);
			}
		}
		
		addChild(pIndex, pNode, constraint);
	}
	
	/**
	 * Adds the given {@link Node} as child to the pane in a new line.
	 * 
	 * @param pNode the {@link Node} to add.
	 */
	public void addChildInNewLine(Node pNode)
	{
		addChildInNewLine(-1, pNode);
	}
	
	/**
	 * Adds the given {@link Node}s as children to the pane.
	 * 
	 * @param pNodes the {@link Node}s to add.
	 */
	public void addChildren(Node... pNodes)
	{
		if (pNodes != null)
		{
			for (Node node : pNodes)
			{
				addChild(node);
			}
		}
	}
	
	/**
	 * Creates {@link Constraints} for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the {@link Constraints}.
	 */
	public Constraints createConstraint(int pColumn, int pRow)
	{
		return createConstraint(pColumn, pRow, pColumn, pRow);
	}
	
	/**
	 * Creates {@link Constraints} for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the end column.
	 * @param pEndRow the end row.
	 * @return the {@link Constraints}.
	 */
	public Constraints createConstraint(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
	{
		Anchor[] left = createDefaultAnchors(leftDefaultAnchors, rightDefaultAnchors, marginAnchors.leftAnchor, marginAnchors.rightAnchor, pBeginColumn, hgap.get());
		Anchor[] right = left;
		
		if (pBeginColumn != pEndColumn)
		{
			right = createDefaultAnchors(leftDefaultAnchors, rightDefaultAnchors, marginAnchors.leftAnchor, marginAnchors.rightAnchor, pEndColumn, hgap.get());
		}
		
		Anchor[] top = createDefaultAnchors(topDefaultAnchors, bottomDefaultAnchors, marginAnchors.topAnchor, marginAnchors.bottomAnchor, pBeginRow, vgap.get());
		Anchor[] bottom = top;
		
		if (pBeginRow != pEndRow)
		{
			bottom = createDefaultAnchors(topDefaultAnchors, bottomDefaultAnchors, marginAnchors.topAnchor, marginAnchors.bottomAnchor, pEndRow, vgap.get());
		}
		
		return new Constraints(top[0], left[0], bottom[1], right[1]);
	}
	
	/**
	 * Gets the bottom {@link Anchor}.
	 * 
	 * @return the bottom {@link Anchor}.
	 */
	public Anchor getBottomAnchor()
	{
		return borderAnchors.bottomAnchor;
	}
	
	/**
	 * Gets the bottom {@link Anchor} used for margins.
	 * 
	 * @return the bottom {@link Anchor} used for margins.
	 */
	public Anchor getBottomMarginAnchor()
	{
		return marginAnchors.bottomAnchor;
	}
	
	/**
	 * Gets the {@link Constraints} for the given {@link Node}.
	 * 
	 * @param pNode the {@link Node}.
	 * @return the {@link Constraints} for the given {@link Node}. {@code null}
	 *         if the {@link Node} is not part of this pane.
	 */
	public Constraints getConstraint(Node pNode)
	{
		return constraints.get(pNode);
	}
	
	/**
	 * Gets the horizontal gap between components.
	 * 
	 * @return the horizontal gap between components.
	 */
	public double getHGap()
	{
		return hgap.get();
	}
	
	/**
	 * Gets the horizontal alignment of the layout.
	 * 
	 * @return the horizontal alignment of the layout.
	 */
	public HorizontalAlignment getHorizontalAlignment()
	{
		return horizontalAlignment.get();
	}
	
	/**
	 * Gets all horizontal {@link Anchor}s.
	 * 
	 * @return all horizontal {@link Anchor}s.
	 */
	public Anchor[] getHorizontalAnchors()
	{
		return horizontalAnchors.toArray(new Anchor[horizontalAnchors.size()]);
	}
	
	/**
	 * Gets the left {@link Anchor}.
	 * 
	 * @return the left {@link Anchor}.
	 */
	public Anchor getLeftAnchor()
	{
		return borderAnchors.leftAnchor;
	}
	
	/**
	 * Gets the left {@link Anchor} used for margins.
	 * 
	 * @return the left {@link Anchor} used for margins.
	 */
	public Anchor getLeftMarginAnchor()
	{
		return marginAnchors.leftAnchor;
	}
	
	/**
	 * Gets the margins.
	 * 
	 * @return the margins.
	 */
	public Insets getMargins()
	{
		return new Insets(marginAnchors.topAnchor.position, marginAnchors.leftAnchor.position, -marginAnchors.bottomAnchor.position, -marginAnchors.rightAnchor.position);
	}
	
	/**
	 * Gets the count of how many components should be in one line.
	 * 
	 * @return the count of how many components should be in one line.
	 */
	public int getNewlineCount()
	{
		return newlineCount.get();
	}
	
	/**
	 * Gets the right {@link Anchor}.
	 * 
	 * @return the right{@link Anchor}.
	 */
	public Anchor getRightAnchor()
	{
		return borderAnchors.rightAnchor;
	}
	
	/**
	 * Gets the right {@link Anchor} used for margins.
	 * 
	 * @return the right {@link Anchor} used for margins.
	 */
	public Anchor getRightMarginAnchor()
	{
		return marginAnchors.rightAnchor;
	}
	
	/**
	 * Gets the top {@link Anchor}.
	 * 
	 * @return the top {@link Anchor}.
	 */
	public Anchor getTopAnchor()
	{
		return borderAnchors.topAnchor;
	}
	
	/**
	 * Gets the top {@link Anchor} used for margins.
	 * 
	 * @return the top {@link Anchor} used for margins.
	 */
	public Anchor getTopMarginAnchor()
	{
		return marginAnchors.topAnchor;
	}
	
	/**
	 * Gets the vertical alignment of the layout.
	 * 
	 * @return the vertical alignment.
	 */
	public VerticalAlignment getVerticalAlignment()
	{
		return verticalAlignment.get();
	}
	
	/**
	 * Gets all vertical {@link Anchor}s.
	 * 
	 * @return all vertical {@link Anchor}s.
	 */
	public Anchor[] getVerticalAnchors()
	{
		return verticalAnchors.toArray(new Anchor[verticalAnchors.size()]);
	}
	
	/**
	 * Gets the vertical gap between the components.
	 * 
	 * @return the vertical gap between the components.
	 */
	public double getVGap()
	{
		return vgap.get();
	}
	
	/**
	 * Gets the property for the horizontal gap between components.
	 * 
	 * @return the property for the horizontal gap between components.
	 */
	public DoubleProperty hGapProperty()
	{
		return hgap;
	}
	
	/**
	 * Gets the property for the horizontal alignment.
	 * 
	 * @return the property for the horizontal alignment.
	 */
	public ObjectProperty<HorizontalAlignment> horizontalAlignmentProperty()
	{
		return horizontalAlignment;
	}
	
	/**
	 * Gets the property for the count of how many components should be in one
	 * line.
	 * 
	 * @return the property for the count of how many components should be in
	 *         one line.
	 */
	public IntegerProperty newlineCountProperty()
	{
		return newlineCount;
	}
	
	/**
	 * Removes all children from this {@link Pane}.
	 */
	public void removeAllChildren()
	{
		getChildren().clear();
		constraints.clear();
		
		requestLayout();
	}
	
	/**
	 * Removes the given child from this {@link Pane}.
	 * 
	 * @param pNode the child to remove.
	 */
	public void removeChild(Node pNode)
	{
		getChildren().remove(pNode);
		constraints.remove(pNode);
		
		requestLayout();
	}
	
	/**
	 * Sets the given {@link Constraints} for the given child.
	 * 
	 * @param pNode the child for which to set new {@link Constraints}.
	 * @param pConstraint the {@link Constraints} to set.
	 * @throws IllegalArgumentException if the given {@link Node} is not a child
	 *             of this {@link FXFormPane}.
	 */
	public void setConstraint(Node pNode, Constraints pConstraint)
	{
		if (constraints.containsKey(pNode))
		{
			constraints.put(pNode, pConstraint);
		}
		else
		{
			throw new IllegalArgumentException("Node " + pNode + " has no constraints to overwrite!");
		}
	}
	
	/**
	 * Sets the horizontal gap between components.
	 * 
	 * @param pGap the horizontal gap between components.
	 */
	public void setHGap(double pGap)
	{
		hgap.set(pGap);
	}
	
	/**
	 * Sets the horizontal alignment of the layout.
	 * 
	 * @param pHorizontalAlignment the new horizontal alignment.
	 */
	public void setHorizontalAlignment(HorizontalAlignment pHorizontalAlignment)
	{
		horizontalAlignment.set(pHorizontalAlignment);
	}
	
	/**
	 * Sets the margins.
	 * 
	 * @param pMargins the margins to set.
	 */
	public void setMargins(Insets pMargins)
	{
		if (pMargins == null)
		{
			marginAnchors.topAnchor.position = 0;
			marginAnchors.leftAnchor.position = 0;
			marginAnchors.bottomAnchor.position = 0;
			marginAnchors.rightAnchor.position = 0;
		}
		else
		{
			marginAnchors.topAnchor.position = pMargins.getTop();
			marginAnchors.leftAnchor.position = pMargins.getLeft();
			marginAnchors.bottomAnchor.position = -pMargins.getBottom();
			marginAnchors.rightAnchor.position = -pMargins.getRight();
		}
	}
	
	/**
	 * sets the count of how many components should be in one line.
	 * 
	 * @param pNewlineCount the count of how many components should be in one
	 *            line.
	 */
	public void setNewlineCount(int pNewlineCount)
	{
		newlineCount.set(pNewlineCount);
	}
	
	/**
	 * Sets the vertical alignment of the layout.
	 * 
	 * @param pVerticalAlignment the vertical alignment.
	 */
	public void setVerticalAlignment(VerticalAlignment pVerticalAlignment)
	{
		verticalAlignment.set(pVerticalAlignment);
	}
	
	/**
	 * Sets the vertical gap between components.
	 * 
	 * @param pGap the new vertical gap between components.
	 */
	public void setVGap(double pGap)
	{
		vgap.set(pGap);
	}
	
	/**
	 * Gets the property for the vertical gap between components.
	 * 
	 * @return the property for the vertical gap between components.
	 */
	public DoubleProperty vGapProperty()
	{
		return vgap;
	}
	
	/**
	 * Gets the property for the vertical alignment.
	 * 
	 * @return the property for the vertical alignment.
	 */
	public ObjectProperty<VerticalAlignment> verticalAlignmentProperty()
	{
		return verticalAlignment;
	}
	
	/**
	 * Calculates all {@link Anchor}s.
	 */
	private void calculateAnchors()
	{
		// reset border anchors
		borderAnchors.leftAnchor.position = 0;
		borderAnchors.rightAnchor.position = 0;
		borderAnchors.topAnchor.position = 0;
		borderAnchors.bottomAnchor.position = 0;
		
		// reset preferred size;
		preferredWidth = 0;
		preferredHeight = 0;
		
		// reset minimum size;
		minimumWidth = 0;
		minimumHeight = 0;
		
		// reset List of Anchors;
		horizontalAnchors.clear();
		verticalAnchors.clear();
		
		clearAutoSizeAnchors();
		initAutoSizeAnchors();
		calculateAutoSizeAnchors();
		calculateSizes();
		
		calculateTargetDependentAnchors = true;
		calculateTargetDependentAnchors();
	}
	
	/**
	 * Calculates the auto-size {@link Anchor}s.
	 * 
	 * @param pLeftTopAnchor the left/top {@link Anchor}.
	 * @param pRightBottomAnchor the right/bottom {@link Anchor}.
	 * @param pPreferredSize the preferred size.
	 * @param pAutoSizeCount the auto size count.
	 */
	private void calculateAutoSize(Anchor pLeftTopAnchor, Anchor pRightBottomAnchor, double pPreferredSize, double pAutoSizeCount)
	{
		List<Anchor> anchors = getAutoSizeAnchorsBetween(pLeftTopAnchor, pRightBottomAnchor);
		int size = anchors.size();
		
		if (size == pAutoSizeCount)
		{
			double fixedSize = pRightBottomAnchor.getAbsolutePosition() - pLeftTopAnchor.getAbsolutePosition();
			for (Anchor anchor : anchors)
			{
				fixedSize += anchor.position;
			}
			
			double diffSize = (pPreferredSize - fixedSize + size - 1) / size;
			for (Anchor anchor : anchors)
			{
				if (diffSize > -anchor.position)
				{
					anchor.position = -diffSize;
				}
				anchor.firstCalculation = false;
			}
		}
		
		anchors = getAutoSizeAnchorsBetween(pRightBottomAnchor, pLeftTopAnchor);
		size = anchors.size();
		
		if (size == pAutoSizeCount)
		{
			double fixedSize = pRightBottomAnchor.getAbsolutePosition() - pLeftTopAnchor.getAbsolutePosition();
			for (Anchor anchor : anchors)
			{
				fixedSize -= anchor.position;
			}
			
			double diffSize = (pPreferredSize - fixedSize + size - 1) / size;
			for (Anchor anchor : anchors)
			{
				if (diffSize > anchor.position)
				{
					anchor.position = diffSize;
				}
				anchor.firstCalculation = false;
			}
		}
	}
	
	/**
	 * Calculates all auto-size {@link Anchor}s.
	 */
	private void calculateAutoSizeAnchors()
	{
		int autoSizeCount = 1;
		
		do
		{
			calculateAutoSizeAnchors(autoSizeCount);
			
			autoSizeCount = finishAutoSizeCalculation();
		}
		while (autoSizeCount > 0 && autoSizeCount < Integer.MAX_VALUE);
	}
	
	/**
	 * Calculates all auto-size {@link Anchor}s.
	 * 
	 * @param pAutoSizeCount the auto size count.
	 */
	private void calculateAutoSizeAnchors(int pAutoSizeCount)
	{
		for (Node child : getChildren())
		{
			if (child.isManaged())
			{
				Constraints constraint = getConstraint(child);
				Dimension2D preferredSize = NodeUtil.getPrefSize(child);
				
				calculateAutoSize(constraint.leftAnchor, constraint.rightAnchor, preferredSize.getWidth(), pAutoSizeCount);
				calculateAutoSize(constraint.topAnchor, constraint.bottomAnchor, preferredSize.getHeight(), pAutoSizeCount);
			}
		}
	}
	
	/**
	 * Calculates the relative {@link Anchor}.
	 * 
	 * @param pLeftTopAnchor the left/top {@link Anchor}.
	 * @param pRightBottomAnchor the right/bottom {@link Anchor}.
	 * @param pPreferredSize the preferred size.
	 */
	private void calculateRelativeAnchor(Anchor pLeftTopAnchor, Anchor pRightBottomAnchor, double pPreferredSize)
	{
		if (pLeftTopAnchor.relative)
		{
			Anchor rightBottom = pRightBottomAnchor.getRelativeAnchor();
			if (rightBottom != null && rightBottom != pLeftTopAnchor)
			{
				double pref = rightBottom.getAbsolutePosition() - pRightBottomAnchor.getAbsolutePosition() + pPreferredSize;
				double size = rightBottom.relatedAnchor.getAbsolutePosition() - pLeftTopAnchor.relatedAnchor.getAbsolutePosition();
				
				double pos = pref - size;
				if (pos < 0)
				{
					pos /= 2;
				}
				else
				{
					pos -= pos / 2;
				}
				if (rightBottom.firstCalculation || pos > rightBottom.position)
				{
					rightBottom.firstCalculation = false;
					rightBottom.position = pos;
				}
				pos = pref - size - pos;
				if (pLeftTopAnchor.firstCalculation || pos > -pLeftTopAnchor.position)
				{
					pLeftTopAnchor.firstCalculation = false;
					pLeftTopAnchor.position = -pos;
				}
			}
		}
		else if (pRightBottomAnchor.relative)
		{
			Anchor leftTop = pLeftTopAnchor.getRelativeAnchor();
			if (leftTop != null && leftTop != pRightBottomAnchor)
			{
				double pref = pLeftTopAnchor.getAbsolutePosition() - leftTop.getAbsolutePosition() + pPreferredSize;
				double size = pRightBottomAnchor.relatedAnchor.getAbsolutePosition() - leftTop.relatedAnchor.getAbsolutePosition();
				
				double pos = size - pref;
				if (pos < 0)
				{
					pos -= pos / 2;
				}
				else
				{
					pos /= 2;
				}
				if (leftTop.firstCalculation || pos < leftTop.position)
				{
					leftTop.firstCalculation = false;
					leftTop.position = pos;
				}
				pos = pref - size - pos;
				if (pRightBottomAnchor.firstCalculation || pos > -pRightBottomAnchor.position)
				{
					pRightBottomAnchor.firstCalculation = false;
					pRightBottomAnchor.position = -pos;
				}
			}
		}
	}
	
	/**
	 * Calculates all relative {@link Anchor}s.
	 */
	private void calculateRelativeAnchors()
	{
		for (Node child : getChildren())
		{
			if (child.isManaged())
			{
				Constraints constraint = getConstraint(child);
				Dimension2D preferredSize = NodeUtil.getPrefSize(child);
				
				calculateRelativeAnchor(constraint.leftAnchor, constraint.rightAnchor, preferredSize.getWidth());
				calculateRelativeAnchor(constraint.topAnchor, constraint.bottomAnchor, preferredSize.getHeight());
			}
		}
	}
	
	/**
	 * Calculates the size of the layout.
	 */
	private void calculateSizes()
	{
		leftBorderUsed = false;
		rightBorderUsed = false;
		topBorderUsed = false;
		bottomBorderUsed = false;
		
		double leftWidth = 0;
		double rightWidth = 0;
		double topHeight = 0;
		double bottomHeight = 0;
		
		// calculate preferredSize.
		for (Node child : getChildren())
		{
			if (child.isManaged())
			{
				Constraints constraint = getConstraint(child);
				
				Dimension2D preferredSize = NodeUtil.getPrefSize(child);
				Dimension2D minimumSize = NodeUtil.getMinSize(child);
				
				if (constraint.rightAnchor.getBorderAnchor() == borderAnchors.leftAnchor)
				{
					double w = constraint.rightAnchor.getAbsolutePosition();
					if (w > leftWidth)
					{
						leftWidth = w;
					}
					leftBorderUsed = true;
				}
				if (constraint.leftAnchor.getBorderAnchor() == borderAnchors.rightAnchor)
				{
					double w = -constraint.leftAnchor.getAbsolutePosition();
					if (w > rightWidth)
					{
						rightWidth = w;
					}
					rightBorderUsed = true;
				}
				if (constraint.bottomAnchor.getBorderAnchor() == borderAnchors.topAnchor)
				{
					double h = constraint.bottomAnchor.getAbsolutePosition();
					if (h > topHeight)
					{
						topHeight = h;
					}
					topBorderUsed = true;
				}
				if (constraint.topAnchor.getBorderAnchor() == borderAnchors.bottomAnchor)
				{
					double h = -constraint.topAnchor.getAbsolutePosition();
					if (h > bottomHeight)
					{
						bottomHeight = h;
					}
					bottomBorderUsed = true;
				}
				if (constraint.leftAnchor.getBorderAnchor() == borderAnchors.leftAnchor && constraint.rightAnchor.getBorderAnchor() == borderAnchors.rightAnchor)
				{
					double w = constraint.leftAnchor.getAbsolutePosition() - constraint.rightAnchor.getAbsolutePosition() +
							preferredSize.getWidth();
					if (w > preferredWidth)
					{
						preferredWidth = w;
					}
					w = constraint.leftAnchor.getAbsolutePosition() - constraint.rightAnchor.getAbsolutePosition() +
							minimumSize.getWidth();
					if (w > minimumWidth)
					{
						minimumWidth = w;
					}
					leftBorderUsed = true;
					rightBorderUsed = true;
				}
				if (constraint.topAnchor.getBorderAnchor() == borderAnchors.topAnchor && constraint.bottomAnchor.getBorderAnchor() == borderAnchors.bottomAnchor)
				{
					double h = constraint.topAnchor.getAbsolutePosition() - constraint.bottomAnchor.getAbsolutePosition() +
							preferredSize.getHeight();
					if (h > preferredHeight)
					{
						preferredHeight = h;
					}
					h = constraint.topAnchor.getAbsolutePosition() - constraint.bottomAnchor.getAbsolutePosition() +
							minimumSize.getHeight();
					if (h > minimumHeight)
					{
						minimumHeight = h;
					}
					topBorderUsed = true;
					bottomBorderUsed = true;
				}
			}
		}
		if (leftWidth != 0 && rightWidth != 0)
		{
			double w = leftWidth + rightWidth + hgap.get();
			if (w > preferredWidth)
			{
				preferredWidth = w;
			}
			if (w > minimumWidth)
			{
				minimumWidth = w;
			}
		}
		else if (leftWidth != 0)
		{
			double w = leftWidth - marginAnchors.rightAnchor.position;
			if (w > preferredWidth)
			{
				preferredWidth = w;
			}
			if (w > minimumWidth)
			{
				minimumWidth = w;
			}
		}
		else
		{
			double w = rightWidth + marginAnchors.leftAnchor.position;
			if (w > preferredWidth)
			{
				preferredWidth = w;
			}
			if (w > minimumWidth)
			{
				minimumWidth = w;
			}
		}
		if (topHeight != 0 && bottomHeight != 0)
		{
			double h = topHeight + bottomHeight + vgap.get();
			if (h > preferredHeight)
			{
				preferredHeight = h;
			}
			if (h > minimumHeight)
			{
				minimumHeight = h;
			}
		}
		else if (topHeight != 0)
		{
			double h = topHeight - marginAnchors.bottomAnchor.position;
			if (h > preferredHeight)
			{
				preferredHeight = h;
			}
			if (h > minimumHeight)
			{
				minimumHeight = h;
			}
		}
		else
		{
			double h = bottomHeight + marginAnchors.topAnchor.position;
			if (h > preferredHeight)
			{
				preferredHeight = h;
			}
			if (h > minimumHeight)
			{
				minimumHeight = h;
			}
		}
	}
	
	/**
	 * Calculates all target dependent {@link Anchor}s.
	 */
	private void calculateTargetDependentAnchors()
	{
		if (calculateTargetDependentAnchors)
		{
			Insets ins = getInsets();
			
			double widthMod = ins.getLeft() + ins.getRight();
			double heightMod = ins.getTop() + ins.getBottom();
			
			// set border anchors
			Dimension2D size = new Dimension2D(getWidth() - widthMod, getHeight() - heightMod);
			Dimension2D minSize = new Dimension2D(getMinWidth() - widthMod, getMinHeight() - heightMod);
			Dimension2D maxSize = new Dimension2D(getMaxWidthSafe() - widthMod, getMaxHeightSafe() - heightMod);
			
			if (horizontalAlignment.get() == HorizontalAlignment.STRETCH || (leftBorderUsed && rightBorderUsed))
			{
				if (minSize.getWidth() > size.getWidth())
				{
					borderAnchors.leftAnchor.position = 0;
					borderAnchors.rightAnchor.position = minSize.getWidth();
				}
				else if (maxSize.getWidth() < size.getWidth())
				{
					switch (horizontalAlignment.get())
					{
						case LEFT:
							borderAnchors.leftAnchor.position = 0;
							break;
							
						case RIGHT:
							borderAnchors.leftAnchor.position = size.getWidth() - maxSize.getWidth();
							break;
							
						default:
							borderAnchors.leftAnchor.position = (size.getWidth() - maxSize.getWidth()) / 2;
							
					}
					borderAnchors.rightAnchor.position = borderAnchors.leftAnchor.position + maxSize.getWidth();
				}
				else
				{
					borderAnchors.leftAnchor.position = 0;
					borderAnchors.rightAnchor.position = size.getWidth();
				}
			}
			else
			{
				if (preferredWidth > size.getWidth())
				{
					borderAnchors.leftAnchor.position = 0;
				}
				else
				{
					switch (horizontalAlignment.get())
					{
						case LEFT:
							borderAnchors.leftAnchor.position = 0;
							break;
							
						case RIGHT:
							borderAnchors.leftAnchor.position = size.getWidth() - preferredWidth;
							break;
							
						default:
							borderAnchors.leftAnchor.position = (size.getWidth() - preferredWidth) / 2;
							
					}
				}
				borderAnchors.rightAnchor.position = borderAnchors.leftAnchor.position + preferredWidth;
			}
			if (verticalAlignment.get() == VerticalAlignment.STRETCH || (topBorderUsed && bottomBorderUsed))
			{
				if (minSize.getHeight() > size.getHeight())
				{
					borderAnchors.topAnchor.position = 0;
					borderAnchors.bottomAnchor.position = minSize.getHeight();
				}
				else if (maxSize.getHeight() < size.getHeight())
				{
					switch (verticalAlignment.get())
					{
						case TOP:
							borderAnchors.topAnchor.position = 0;
							break;
							
						case BOTTOM:
							borderAnchors.topAnchor.position = size.getHeight() - maxSize.getHeight();
							break;
							
						default:
							borderAnchors.topAnchor.position = (size.getHeight() - maxSize.getHeight()) / 2;
							
					}
					borderAnchors.bottomAnchor.position = borderAnchors.topAnchor.position + maxSize.getHeight();
				}
				else
				{
					borderAnchors.topAnchor.position = 0;
					borderAnchors.bottomAnchor.position = size.getHeight();
				}
			}
			else
			{
				if (preferredHeight > size.getHeight())
				{
					borderAnchors.topAnchor.position = 0;
				}
				else
				{
					switch (verticalAlignment.get())
					{
						case TOP:
							borderAnchors.topAnchor.position = 0;
							break;
							
						case BOTTOM:
							borderAnchors.topAnchor.position = size.getHeight() - preferredHeight;
							break;
							
						default:
							borderAnchors.topAnchor.position = (size.getHeight() - preferredHeight) / 2;
							
					}
				}
				borderAnchors.bottomAnchor.position = borderAnchors.topAnchor.position + preferredHeight;
			}
			
			borderAnchors.leftAnchor.position += ins.getLeft();
			borderAnchors.rightAnchor.position += ins.getLeft();
			borderAnchors.topAnchor.position += ins.getTop();
			borderAnchors.bottomAnchor.position += ins.getTop();
			
			calculateRelativeAnchors();
			
			calculateTargetDependentAnchors = false;
		}
	}
	
	/**
	 * Clears the given {@link Anchor}.
	 * 
	 * @param pAnchor the {@link Anchor} to clear.
	 */
	private void clearAutoSize(Anchor pAnchor)
	{
		pAnchor.relative = pAnchor.autoSize;
		pAnchor.autoSizeCalculated = false;
		pAnchor.firstCalculation = true;
		
		if (pAnchor.autoSize)
		{
			pAnchor.position = 0;
		}
		
	}
	
	/**
	 * Clears all auto-size {@link Anchor}s.
	 */
	private void clearAutoSizeAnchors()
	{
		for (Node child : getChildren())
		{
			Constraints constraint = getConstraint(child);
			
			clearAutoSize(constraint.leftAnchor);
			clearAutoSize(constraint.rightAnchor);
			clearAutoSize(constraint.topAnchor);
			clearAutoSize(constraint.bottomAnchor);
			
			if (!horizontalAnchors.contains(constraint.leftAnchor))
			{
				horizontalAnchors.add(constraint.leftAnchor);
			}
			if (!horizontalAnchors.contains(constraint.rightAnchor))
			{
				horizontalAnchors.add(constraint.rightAnchor);
			}
			if (!verticalAnchors.contains(constraint.topAnchor))
			{
				verticalAnchors.add(constraint.topAnchor);
			}
			if (!verticalAnchors.contains(constraint.bottomAnchor))
			{
				verticalAnchors.add(constraint.bottomAnchor);
			}
		}
	}
	
	/**
	 * Creates the default {@link Anchor}s.
	 * 
	 * @param pLeftTopDefaultAnchors the top/left default {@link Anchor}.
	 * @param pRightBottomDefaultAnchors the bottom/right default {@link Anchor}
	 *            .
	 * @param pLeftTopAnchor the top/left default {@link Anchor}.
	 * @param pRightBottomAnchor the bottom/right default {@link Anchor}.
	 * @param pColumnOrRow the column or row.
	 * @param pGap the gap.
	 * @return the default {@link Anchor}.
	 */
	private Anchor[] createDefaultAnchors(
			List<Anchor> pLeftTopDefaultAnchors,
			List<Anchor> pRightBottomDefaultAnchors,
			Anchor pLeftTopAnchor,
			Anchor pRightBottomAnchor,
			int pColumnOrRow,
			double pGap)
	{
		List<Anchor> defaultAnchors;
		Anchor anchor;
		double gap;
		boolean rightBottom = pColumnOrRow < 0;
		int columnOrRow = pColumnOrRow;
		
		if (rightBottom)
		{
			columnOrRow = (-columnOrRow - 1) * 2;
			defaultAnchors = pRightBottomDefaultAnchors;
			anchor = pRightBottomAnchor;
			gap = -pGap;
		}
		else
		{
			columnOrRow *= 2;
			defaultAnchors = pLeftTopDefaultAnchors;
			anchor = pLeftTopAnchor;
			gap = pGap;
		}
		int size = defaultAnchors.size();
		while (columnOrRow >= size)
		{
			if (size == 0)
			{
				defaultAnchors.add(anchor);
			}
			else
			{
				defaultAnchors.add(new Anchor(defaultAnchors.get(size - 1), gap));
			}
			defaultAnchors.add(new Anchor(defaultAnchors.get(size)));
			size = defaultAnchors.size();
		}
		if (rightBottom)
		{
			return new Anchor[] { defaultAnchors.get(columnOrRow + 1), defaultAnchors.get(columnOrRow) };
		}
		else
		{
			return new Anchor[] { defaultAnchors.get(columnOrRow), defaultAnchors.get(columnOrRow + 1) };
		}
	}
	
	/**
	 * Finishes the calculation of the auto-size {@link Anchor}s.
	 * 
	 * @return the count of remaining auto-size {@link Anchor}s.
	 */
	private int finishAutoSizeCalculation()
	{
		int autoSizeCount = Integer.MAX_VALUE;
		
		for (Node child : getChildren())
		{
			if (child.isManaged())
			{
				Constraints constraint = getConstraint(child);
				
				int count = finishAutoSizeCalculation(constraint.leftAnchor, constraint.rightAnchor);
				if (count > 0 && count < autoSizeCount)
				{
					autoSizeCount = count;
				}
				count = finishAutoSizeCalculation(constraint.rightAnchor, constraint.leftAnchor);
				if (count > 0 && count < autoSizeCount)
				{
					autoSizeCount = count;
				}
				count = finishAutoSizeCalculation(constraint.topAnchor, constraint.bottomAnchor);
				if (count > 0 && count < autoSizeCount)
				{
					autoSizeCount = count;
				}
				count = finishAutoSizeCalculation(constraint.bottomAnchor, constraint.topAnchor);
				if (count > 0 && count < autoSizeCount)
				{
					autoSizeCount = count;
				}
			}
		}
		
		return autoSizeCount;
	}
	
	/**
	 * Finishes the calculation of the auto-size {@link Anchor}s.
	 * 
	 * @param pLeftTopAnchor the left/top {@link Anchor}.
	 * @param pRightBottomAnchor the right/bottom {@link Anchor}.
	 * @return the count of remaining auto-size {@link Anchor}s.
	 */
	private int finishAutoSizeCalculation(Anchor pLeftTopAnchor, Anchor pRightBottomAnchor)
	{
		List<Anchor> anchors = getAutoSizeAnchorsBetween(pLeftTopAnchor, pRightBottomAnchor);
		int count = anchors.size();
		
		for (Anchor anchor : anchors)
		{
			if (!anchor.firstCalculation)
			{
				anchor.autoSizeCalculated = true;
				count--;
			}
		}
		return count;
	}
	
	/**
	 * Gets all auto-size {@link Anchor}s between the given two {@link Anchor}s.
	 * 
	 * @param pStartAnchor the start {@link Anchor}.
	 * @param pEndAnchor the end {@link Anchor}.
	 * @return all auto-size {@link Anchor}s between the given two.
	 */
	private List<Anchor> getAutoSizeAnchorsBetween(Anchor pStartAnchor, Anchor pEndAnchor)
	{
		List<Anchor> anchorsBuffer = new ArrayList<>();
		Anchor nextRelatedAnchor = pStartAnchor;
		
		while (nextRelatedAnchor != null && nextRelatedAnchor != pEndAnchor)
		{
			if (nextRelatedAnchor.autoSize && !nextRelatedAnchor.autoSizeCalculated)
			{
				anchorsBuffer.add(nextRelatedAnchor);
			}
			nextRelatedAnchor = nextRelatedAnchor.relatedAnchor;
		}
		if (nextRelatedAnchor == null)
		{
			anchorsBuffer.clear();
		}
		return anchorsBuffer;
	}
	
	/**
	 * Gets the maximum height.
	 * 
	 * @return the maximum height. Guaranteed to be not zero or below.
	 */
	private double getMaxHeightSafe()
	{
		if (getMaxHeight() >= 0)
		{
			return getMaxHeight();
		}
		
		return Double.MAX_VALUE;
	}
	
	/**
	 * Gets the maximum width.
	 * 
	 * @return the maximum width. Guaranteed to be not zero or below.
	 */
	private double getMaxWidthSafe()
	{
		if (getMaxWidth() >= 0)
		{
			return getMaxWidth();
		}
		
		return Double.MAX_VALUE;
	}
	
	/**
	 * Gets the next {@link Constraints} based on the current layout.
	 * 
	 * @return the next {@link Constraints}.
	 */
	private Constraints getNextConstraints()
	{
		if (getChildren().size() >= 1)
		{
			Constraints constraintsBefore = getPreviousConstraints();
			
			// Might happen if the client decides to add directly to the children
			// list instead of using the addChild(...) methods.
			if (constraintsBefore != null)
			{
				int column = leftDefaultAnchors.indexOf(constraintsBefore.leftAnchor) / 2 + 1;
				int row = topDefaultAnchors.indexOf(constraintsBefore.topAnchor) / 2;
				
				if (column % newlineCount.get() == 0)
				{
					return createConstraint(0, row + 1);
				}
				else
				{
					return createConstraint(column, row);
				}
			}
		}
		
		// First children to be added. Or at least the first with constraints.
		return createConstraint(0, 0);
	}
	
	/**
	 * Gets the previous {@link Constraints} based on the current layout.
	 * 
	 * @return the previous {@link Constraints}.
	 */
	private Constraints getPreviousConstraints()
	{
		for (int index = childrenForConstraintsOrder.size() - 1; index >= 0; index--)
		{
			Constraints previousConstraints = getConstraint(childrenForConstraintsOrder.get(index));
			
			if (previousConstraints != null)
			{
				return previousConstraints;
			}
		}
		
		return null;
	}
	
	/**
	 * Initializes the auto-size {@link Anchor}s between the given two
	 * {@link Anchor}s.
	 * 
	 * @param pStartAnchor the start {@link Anchor}.
	 * @param pEndAnchor the end {@link Anchor}.
	 */
	private void initAutoSize(Anchor pStartAnchor, Anchor pEndAnchor)
	{
		List<Anchor> anchors = getAutoSizeAnchorsBetween(pStartAnchor, pEndAnchor);
		
		for (Anchor anchor : anchors)
		{
			anchor.relative = false;
			
			if (!anchor.relatedAnchor.autoSize && anchor.secondRelatedAnchor == null)
			{
				anchor.position = -anchor.relatedAnchor.position;
			}
			else
			{
				anchor.position = 0;
			}
		}
	}
	
	/**
	 * Initializes all auto-size {@link Anchor}s.
	 */
	private void initAutoSizeAnchors()
	{
		for (Node child : getChildren())
		{
			Constraints constraint = getConstraint(child);
			
			initAutoSize(constraint.leftAnchor, constraint.rightAnchor);
			initAutoSize(constraint.rightAnchor, constraint.leftAnchor);
			initAutoSize(constraint.topAnchor, constraint.bottomAnchor);
			initAutoSize(constraint.bottomAnchor, constraint.topAnchor);
		}
	}
	
	/**
	 * Resizes and relocates the children based on the calculated {@link Anchor}
	 * s.
	 */
	private void resizeAndRelocateChildren()
	{
		for (Node child : getChildren())
		{
			if (child.isManaged())
			{
				Constraints constraint = getConstraint(child);
				
				double x = snapPosition(constraint.leftAnchor.getAbsolutePosition());
				double width = snapSize(constraint.rightAnchor.getAbsolutePosition() - constraint.leftAnchor.getAbsolutePosition());
				double y = snapPosition(constraint.topAnchor.getAbsolutePosition());
				double height = snapSize(constraint.bottomAnchor.getAbsolutePosition() - constraint.topAnchor.getAbsolutePosition());
				
				child.resizeRelocate(x, y, width, height);
			}
		}
	}
	
	/**
	 * Invoked if the list of children changed.
	 * <p>
	 * Updates the constraints as necessary.
	 * 
	 * @param pChange the change.
	 */
	private void onChildrenChanged(Change<? extends Node> pChange)
	{
		while (pChange.next())
		{
			if (pChange.wasAdded())
			{
				for (Node child : pChange.getAddedSubList())
				{
					childrenForConstraintsOrder.add(child);
					if (!constraints.containsKey(child))
					{
						constraints.put(child, getNextConstraints());
					}
				}
			}
			
			if (pChange.wasRemoved())
			{
				for (Node child : pChange.getRemoved())
				{
					childrenForConstraintsOrder.remove(child);
					constraints.remove(child);
				}
			}
		}
	}
	
	/**
	 * Invokes directly {@link #requestLayout()}.
	 * 
	 * @param pObservable the {@link Observable}.
	 */
	private void onPropertyChangedRequestLayout(Observable pObservable)
	{
		requestLayout();
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * Represents a location in the layout.
	 * 
	 * @author Robert Zenz
	 */
	public static class Anchor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** If this should be automatically sized. */
		private boolean autoSize;
		
		/** If this has been automatically sized. */
		private boolean autoSizeCalculated;
		
		/** If the first calculation has occurred. */
		private boolean firstCalculation;
		
		/** The associated {@link FXFormPane}. */
		private FXFormPane formPane;
		
		/** The {@link Orientation}. */
		private Orientation orientation;
		
		/** The position in the layout. */
		private double position;
		
		/** The related {@link Anchor}. */
		private Anchor relatedAnchor;
		
		/** If this should be relative to another {@link Anchor}. */
		private boolean relative;
		
		/** The relative position in the layout. */
		private double relativePosition;
		
		/** The "other" related {@link Anchor}. */
		private Anchor secondRelatedAnchor;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link Anchor}.
		 *
		 * @param pRelatedAnchor the related anchor.
		 */
		public Anchor(Anchor pRelatedAnchor)
		{
			formPane = pRelatedAnchor.formPane;
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			secondRelatedAnchor = null;
			autoSize = true;
			position = 0;
			relativePosition = 0.5f;
		}
		
		/**
		 * Creates a new instance of {@link Anchor}.
		 *
		 * @param pRelatedAnchor the related anchor.
		 * @param pSecondRelatedAnchor the second related anchor.
		 */
		public Anchor(Anchor pRelatedAnchor, Anchor pSecondRelatedAnchor)
		{
			this(pRelatedAnchor, pSecondRelatedAnchor, 0.5d);
		}
		
		/**
		 * Creates a new instance of {@link Anchor}.
		 *
		 * @param pRelatedAnchor the related anchor.
		 * @param pSecondRelatedAnchor the second related anchor.
		 * @param pRelativePosition the relative position.
		 */
		public Anchor(Anchor pRelatedAnchor, Anchor pSecondRelatedAnchor, double pRelativePosition)
		{
			formPane = pRelatedAnchor.formPane;
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			secondRelatedAnchor = pSecondRelatedAnchor;
			autoSize = false;
			position = 0;
			setRelativePosition(pRelativePosition);
		}
		
		/**
		 * Creates a new instance of {@link Anchor}.
		 *
		 * @param pRelatedAnchor the related anchor.
		 * @param pPosition the position.
		 */
		public Anchor(Anchor pRelatedAnchor, double pPosition)
		{
			formPane = pRelatedAnchor.formPane;
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			secondRelatedAnchor = null;
			autoSize = false;
			position = pPosition;
			relativePosition = 0.5f;
		}
		
		/**
		 * Creates a new instance of {@link Anchor}.
		 *
		 * @param pLayout the layout.
		 * @param pOrientation the orientation.
		 */
		private Anchor(FXFormPane pLayout, Orientation pOrientation)
		{
			formPane = pLayout;
			orientation = pOrientation;
			relatedAnchor = null;
			secondRelatedAnchor = null;
			autoSize = false;
			position = 0;
			relativePosition = 0.5f;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the absolute position.
		 * 
		 * @return the absolute position.
		 */
		public double getAbsolutePosition()
		{
			if (relatedAnchor == null)
			{
				return position;
			}
			else if (secondRelatedAnchor == null)
			{
				return relatedAnchor.getAbsolutePosition() + position;
			}
			else
			{
				double pos = relatedAnchor.getAbsolutePosition();
				return pos + ((secondRelatedAnchor.getAbsolutePosition() - pos) * relativePosition);
			}
		}
		
		/**
		 * Gets the {@link Anchor} that is the border of the layout.
		 * 
		 * @return the {@link Anchor} that is the border of the layout.
		 */
		public Anchor getBorderAnchor()
		{
			Anchor borderAnchor = this;
			while (borderAnchor.relatedAnchor != null)
			{
				borderAnchor = borderAnchor.relatedAnchor;
			}
			return borderAnchor;
		}
		
		/**
		 * Gets the associated {@link FXFormPane}.
		 * 
		 * @return the associated {@link FXFormPane}.
		 */
		public FXFormPane getFormPane()
		{
			return formPane;
		}
		
		/**
		 * Gets the {@link Orientation}.
		 * 
		 * @return the {@link Orientation}.
		 */
		public Orientation getOrientation()
		{
			return orientation;
		}
		
		/**
		 * Gets the position.
		 * 
		 * @return the position.
		 */
		public double getPosition()
		{
			return position;
		}
		
		/**
		 * Gets the related {@link Anchor}.
		 * 
		 * @return the related {@link Anchor}.
		 */
		public Anchor getRelatedAnchor()
		{
			return relatedAnchor;
		}
		
		/**
		 * Gets the relative {@link Anchor}.
		 * 
		 * @return the relative {@link Anchor}.
		 */
		public Anchor getRelativeAnchor()
		{
			Anchor relativeAnchor = this;
			while (relativeAnchor != null && !relativeAnchor.relative)
			{
				relativeAnchor = relativeAnchor.relatedAnchor;
			}
			return relativeAnchor;
		}
		
		/**
		 * Gets the relative position.
		 * 
		 * @return the relative position.
		 */
		public double getRelativePosition()
		{
			return relativePosition;
		}
		
		/**
		 * Gets the second related {@link Anchor}.
		 * 
		 * @return the second related {@link Anchor}.
		 */
		public Anchor getSecondRelatedAnchor()
		{
			return secondRelatedAnchor;
		}
		
		/**
		 * Gets if this {@link Anchor} is an auto-size {@link Anchor}.
		 * 
		 * @return {@code true} if this {@link Anchor} is an auto-size
		 *         {@link Anchor}.
		 */
		public boolean isAutoSize()
		{
			return autoSize;
		}
		
		/**
		 * Gets if this {@link Anchor} is a border {@link Anchor}.
		 * 
		 * @return {@code true} if this {@link Anchor} is a border
		 *         {@link Anchor}.
		 */
		public boolean isBorderAnchor()
		{
			return relatedAnchor == null;
		}
		
		/**
		 * Gets if this {@link Anchor} is relative.
		 * 
		 * @return {@code true} if this {@link Anchor} is relative.
		 */
		public boolean isRelative()
		{
			return relative;
		}
		
		/**
		 * Sets if this {@link Anchor} is an auto-size {@link Anchor}.
		 * 
		 * @param pAutoSize {@code true} if this {@link Anchor} should an
		 *            auto-size {@link Anchor}.
		 */
		public void setAutoSize(boolean pAutoSize)
		{
			autoSize = pAutoSize;
		}
		
		/**
		 * Sets the position.
		 * 
		 * @param pPosition the position.
		 */
		public void setPosition(int pPosition)
		{
			if (relatedAnchor == null)
			{
				throw new IllegalArgumentException("Position of border anchor may not be set!");
			}
			else
			{
				position = pPosition;
			}
		}
		
		/**
		 * Sets the related {@link Anchor}.
		 * 
		 * @param pRelatedAnchor the related {@link Anchor}.
		 */
		public void setRelatedAnchor(Anchor pRelatedAnchor)
		{
			if (formPane != pRelatedAnchor.formPane || orientation != pRelatedAnchor.orientation)
			{
				throw new IllegalArgumentException("The related anchor must have the same layout and the same orientation!");
			}
			else if (hasCycleReference(pRelatedAnchor))
			{
				throw new IllegalArgumentException("The related anchor has a cycle reference to this anchor!");
			}
			else
			{
				relatedAnchor = pRelatedAnchor;
			}
		}
		
		/**
		 * Sets the relative position.
		 * 
		 * @param pRelativePosition the relative position.
		 */
		public void setRelativePosition(double pRelativePosition)
		{
			if (relatedAnchor == null)
			{
				throw new IllegalArgumentException("Relative position of border anchor may not be set!");
			}
			else if (pRelativePosition < 0)
			{
				relativePosition = 0;
			}
			else if (pRelativePosition > 1)
			{
				relativePosition = 1;
			}
			else
			{
				relativePosition = pRelativePosition;
			}
		}
		
		/**
		 * Sets the second related {@link Anchor}.
		 * 
		 * @param pSecondRelatedAnchor the second related {@link Anchor}.
		 */
		public void setSecondRelatedAnchor(Anchor pSecondRelatedAnchor)
		{
			if (formPane != pSecondRelatedAnchor.formPane || orientation != pSecondRelatedAnchor.orientation)
			{
				throw new IllegalArgumentException("The related anchor must have the same layout and the same orientation!");
			}
			else if (hasCycleReference(pSecondRelatedAnchor))
			{
				throw new IllegalArgumentException("The related anchor has a cycle reference to this anchor!");
			}
			else
			{
				secondRelatedAnchor = pSecondRelatedAnchor;
			}
		}
		
		/**
		 * If this {@link Anchor} might refer to itself at some point.
		 * 
		 * @param pRelatedAnchor the related {@link Anchor}.
		 * @return {@code true} if this {@link Anchor} might refer to itself at
		 *         some point.
		 */
		private boolean hasCycleReference(Anchor pRelatedAnchor)
		{
			Anchor nextRelatedAnchor = pRelatedAnchor;
			
			do
			{
				if (nextRelatedAnchor == this)
				{
					return true;
				}
				nextRelatedAnchor = nextRelatedAnchor.relatedAnchor;
			}
			while (nextRelatedAnchor != null);
			
			return false;
		}
		
	}	// Anchor
	
	/**
	 * Represents constraints, a combination of four {@link Anchor}s.
	 * 
	 * @author Robert Zenz
	 */
	public static class Constraints implements Cloneable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The bottom {@link Anchor}. */
		private Anchor bottomAnchor;
		
		/** The left {@link Anchor}. */
		private Anchor leftAnchor;
		
		/** The right {@link Anchor}. */
		private Anchor rightAnchor;
		
		/** The top {@link Anchor}. */
		private Anchor topAnchor;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link Constraints}.
		 *
		 * @param pTopAnchor the top {@link Anchor}.
		 * @param pLeftAnchor the left {@link Anchor}.
		 */
		public Constraints(Anchor pTopAnchor, Anchor pLeftAnchor)
		{
			this(pTopAnchor, pLeftAnchor, null, null);
		}
		
		/**
		 * Creates a new instance of {@link Constraints}.
		 *
		 * @param pTopAnchor the top {@link Anchor}.
		 * @param pLeftAnchor the left {@link Anchor}.
		 * @param pBottomAnchor the bottom {@link Anchor}.
		 * @param pRightAnchor the right {@link Anchor}.
		 */
		public Constraints(Anchor pTopAnchor, Anchor pLeftAnchor, Anchor pBottomAnchor, Anchor pRightAnchor)
		{
			// TODO Replace this.
			Anchor tempLeftAnchor = pLeftAnchor;
			Anchor tempRightAnchor = pRightAnchor;
			Anchor tempTopAnchor = pTopAnchor;
			Anchor tempBottomAnchor = pBottomAnchor;
			
			if (pLeftAnchor == null && pRightAnchor != null)
			{
				tempLeftAnchor = new Anchor(pRightAnchor);
			}
			else if (pRightAnchor == null && pLeftAnchor != null)
			{
				tempRightAnchor = new Anchor(pLeftAnchor);
			}
			
			if (pTopAnchor == null && pBottomAnchor != null)
			{
				tempTopAnchor = new Anchor(pBottomAnchor);
			}
			else if (pBottomAnchor == null && pTopAnchor != null)
			{
				tempBottomAnchor = new Anchor(pTopAnchor);
			}
			
			setLeftAnchor(tempLeftAnchor);
			setRightAnchor(tempRightAnchor);
			setTopAnchor(tempTopAnchor);
			setBottomAnchor(tempBottomAnchor);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the bottom {@link Anchor}.
		 * 
		 * @return the bottom {@link Anchor}.
		 */
		public Anchor getBottomAnchor()
		{
			return bottomAnchor;
		}
		
		/**
		 * Gets the left {@link Anchor}.
		 * 
		 * @return the left {@link Anchor}.
		 */
		public Anchor getLeftAnchor()
		{
			return leftAnchor;
		}
		
		/**
		 * Gets the right {@link Anchor}.
		 * 
		 * @return the right {@link Anchor}.
		 */
		public Anchor getRightAnchor()
		{
			return rightAnchor;
		}
		
		/**
		 * Gets the top {@link Anchor}.
		 * 
		 * @return the top {@link Anchor}.
		 */
		public Anchor getTopAnchor()
		{
			return topAnchor;
		}
		
		/**
		 * Sets the bottom {@link Anchor}.
		 * 
		 * @param pBottomAnchor the bottom {@link Anchor}.
		 */
		public void setBottomAnchor(Anchor pBottomAnchor)
		{
			if (pBottomAnchor == null && topAnchor != null)
			{
				bottomAnchor = new Anchor(topAnchor);
			}
			else if (pBottomAnchor.orientation == Orientation.HORIZONTAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as bottom anchor!");
			}
			else
			{
				bottomAnchor = pBottomAnchor;
			}
		}
		
		/**
		 * Sets the left {@link Anchor}.
		 * 
		 * @param pLeftAnchor the left {@link Anchor}.
		 */
		public void setLeftAnchor(Anchor pLeftAnchor)
		{
			if (pLeftAnchor == null && rightAnchor != null)
			{
				leftAnchor = new Anchor(rightAnchor);
			}
			else if (pLeftAnchor.orientation == Orientation.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as left anchor!");
			}
			else
			{
				leftAnchor = pLeftAnchor;
			}
		}
		
		/**
		 * Sets the right {@link Anchor}.
		 * 
		 * @param pRightAnchor the right {@link Anchor}.
		 */
		public void setRightAnchor(Anchor pRightAnchor)
		{
			if (pRightAnchor == null && leftAnchor != null)
			{
				rightAnchor = new Anchor(leftAnchor);
			}
			else if (pRightAnchor.orientation == Orientation.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as right anchor!");
			}
			else
			{
				rightAnchor = pRightAnchor;
			}
		}
		
		/**
		 * Sets the top {@link Anchor}.
		 * 
		 * @param pTopAnchor the top {@link Anchor}.
		 */
		public void setTopAnchor(Anchor pTopAnchor)
		{
			if (pTopAnchor == null && bottomAnchor != null)
			{
				topAnchor = new Anchor(bottomAnchor);
			}
			else if (pTopAnchor.orientation == Orientation.HORIZONTAL)
			{
				throw new IllegalArgumentException("A horizontal anchor can not be used as top anchor!");
			}
			else
			{
				topAnchor = pTopAnchor;
			}
		}
		
	}	// Constraints
	
	/**
	 * The horizontal alignment.
	 * 
	 * @author Robert Zenz
	 */
	public enum HorizontalAlignment
	{
		/** Center aligned. */
		CENTER,
		
		/** Left aligned. */
		LEFT,
		
		/** Right aligned. */
		RIGHT,
		
		/** Stretched. */
		STRETCH
		
	}	// HorizontalAlignment
	
	/**
	 * The vertical alignment.
	 * 
	 * @author Robert Zenz
	 */
	public enum VerticalAlignment
	{
		/** Bottom aligned. */
		BOTTOM,
		
		/** Center aligned. */
		CENTER,
		
		/** Stretched. */
		STRETCH,
		
		/** Top aligned. */
		TOP
		
	}	// VerticalAlignment
	
}	// FXFormPane
