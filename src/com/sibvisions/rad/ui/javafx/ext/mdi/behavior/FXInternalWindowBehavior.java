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
package com.sibvisions.rad.ui.javafx.ext.mdi.behavior;

import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow.State;
import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;
import com.sun.javafx.scene.control.behavior.BehaviorBase;

/**
 * The default {@link FXInternalWindow} behavior. It provides drag and resize
 * support.
 * 
 * @author Robert Zenz
 */
public class FXInternalWindowBehavior extends BehaviorBase<FXInternalWindow>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The last {@link Point2D position} of the mouse when the
	 * {@link FXInternalWindow} is dragged.
	 */
	private Point2D lastDragPosition;
	
	/**
	 * The last {@link Point2D position} of the mouse when the
	 * {@link FXInternalWindow} is resized.
	 */
	private Point2D lastResizePosition;
	
	/**
	 * The current resize position, giving into which direction it can be
	 * resized currently.
	 */
	private Pos resizePos;
	
	/** The {@link Node} used for dragging the {@link FXInternalWindow}. */
	private Node titleBar;
	
	/** The {@link ContextMenu} of the window. */
	private ContextMenu windowMenu;
	
	/** The {@link EventHandler} for the mouse pressed event when resizing. */
	private EventHandler<MouseEvent> windowMousePressedFilter;
	
	/** The {@link EventHandler} for the mouse moved event when resizing. */
	private EventHandler<MouseEvent> windowResizedMouseMovedFilter;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXInternalWindowBehavior}.
	 *
	 * @param pInternalWindow the internal window.
	 */
	public FXInternalWindowBehavior(FXInternalWindow pInternalWindow)
	{
		super(pInternalWindow, null);
		
		windowResizedMouseMovedFilter = this::onResizingMouseMovedFilter;
		windowMousePressedFilter = this::onMousePressedFilter;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		// Dragging
		titleBar.setOnMouseDragged(null);
		titleBar.setOnMousePressed(null);
		titleBar.setOnMouseReleased(null);
		
		// Other
		titleBar.setOnMouseClicked(null);
		
		// Resizing
		FXInternalWindow internalWindow = getControl();
		
		internalWindow.setOnMouseDragged(null);
		internalWindow.setOnMouseExited(null);
		internalWindow.setOnMousePressed(null);
		internalWindow.setOnMouseReleased(null);
		
		internalWindow.removeEventFilter(MouseEvent.MOUSE_MOVED, windowResizedMouseMovedFilter);
		internalWindow.removeEventFilter(MouseEvent.MOUSE_PRESSED, windowMousePressedFilter);
		
		super.dispose();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Initializes this {@link FXInternalWindowBehavior} with the given
	 * {@link Node} as handle for dragging.
	 * 
	 * @param pTitleBar the {@link Node} used for dragging and other operations.
	 * @param pWindowMenu the {@link ContextMenu} of the window.
	 */
	public void init(Node pTitleBar, ContextMenu pWindowMenu)
	{
		titleBar = pTitleBar;
		windowMenu = pWindowMenu;
		
		titleBar.setOnMouseClicked(this::onTitleBarMouseClicked);
		
		setupDragging();
		setupResizing();
	}
	
	/**
	 * Converts the given {@link Pos} to the resizing mouse cursor.
	 * 
	 * @param pPos the {@link Pos} to convert.
	 * @return the mouse cursor.
	 */
	private Cursor convertPosToResizeCursor(Pos pPos)
	{
		if (pPos == null)
		{
			return Cursor.DEFAULT;
		}
		
		switch (pPos)
		{
			case BOTTOM_CENTER:
			case TOP_CENTER:
				return Cursor.V_RESIZE;
				
			case BOTTOM_LEFT:
				return Cursor.SW_RESIZE;
				
			case BOTTOM_RIGHT:
				return Cursor.SE_RESIZE;
				
			case CENTER_LEFT:
			case CENTER_RIGHT:
				return Cursor.H_RESIZE;
				
			case TOP_LEFT:
				return Cursor.NW_RESIZE;
				
			case TOP_RIGHT:
				return Cursor.NE_RESIZE;
				
			default:
				return Cursor.DEFAULT;
		}
	}
	
	/**
	 * Gets the corrected scene coordinates.
	 * 
	 * @param pMouseEvent the event.
	 * @return the corrected scene coordinates.
	 */
	private Point2D getCorrectSceneCoordinates(MouseEvent pMouseEvent)
	{
		return NodeUtil.correctScaling(getControl().getParent(), pMouseEvent.getSceneX(), pMouseEvent.getSceneY());
	}
	
	/**
	 * Gets the resize direction based on the given coordinates.
	 * 
	 * @param pX the x coordinate.
	 * @param pY the y coordinate.
	 * @return the resize direction.
	 */
	private Pos getResizeDirection(double pX, double pY)
	{
		double edgeSize = getControl().getEdgeSize();
		
		// Make sure that at least a third of the height/width can be used
		// to resize the window vertically/horizontally.
		double thirdWindowSize = Math.min(getControl().getWidth(), getControl().getHeight()) / 3;
		if (edgeSize > thirdWindowSize)
		{
			edgeSize = thirdWindowSize;
		}
		
		double rightEdgeStart = getControl().getWidth() - edgeSize;
		double bottomEdgeStart = getControl().getHeight() - edgeSize;
		
		if (pX < edgeSize)
		{
			if (pY < edgeSize)
			{
				return Pos.TOP_LEFT;
			}
			else if (pY < bottomEdgeStart)
			{
				return Pos.CENTER_LEFT;
			}
			else
			{
				return Pos.BOTTOM_LEFT;
			}
		}
		else if (pX < rightEdgeStart)
		{
			if (pY < edgeSize)
			{
				return Pos.TOP_CENTER;
			}
			else if (pY > bottomEdgeStart)
			{
				return Pos.BOTTOM_CENTER;
			}
		}
		else
		{
			if (pY < edgeSize)
			{
				return Pos.TOP_RIGHT;
			}
			else if (pY < bottomEdgeStart)
			{
				return Pos.CENTER_RIGHT;
			}
			else
			{
				return Pos.BOTTOM_RIGHT;
			}
		}
		
		return null;
	}
	
	/**
	 * Tests if the given coordinates (in scene space) are inside of the parent
	 * of the current {@link FXInternalWindow}.
	 * 
	 * @param pSceneX the x coordinate (scene space).
	 * @param pSceneY the y coordinate (scene space).
	 * @return {@code true} if the given coordinates are inside the parent.
	 */
	private boolean isInsideParent(double pSceneX, double pSceneY)
	{
		Parent parent = getControl().getParent();
		Bounds parentBounds = parent.getLayoutBounds();
		parentBounds = parent.localToScene(parentBounds);
		
		return parentBounds.contains(pSceneX, pSceneY);
	}
	
	/**
	 * Tests if given coordinates (parent space) are inside the current
	 * {@link FXInternalWindow}.
	 * 
	 * @param pX the x coordinate.
	 * @param pY the y coordinate.
	 * @return {@code true} if the given coordinates are inside the current
	 *         {@link FXInternalWindow}.
	 */
	private boolean isInsideWindow(double pX, double pY)
	{
		Bounds bounds = getControl().getLayoutBounds();
		
		Insets padding = getControl().getInsets();
		if (padding == null)
		{
			padding = Insets.EMPTY;
		}
		
		bounds = new BoundingBox(
				bounds.getMinX() + padding.getLeft(),
				bounds.getMinY() + padding.getTop(),
				bounds.getWidth() - padding.getLeft() - padding.getRight() - 1,
				bounds.getHeight() - padding.getTop() - padding.getBottom() - 1);
		
		return bounds.contains(pX, pY);
	}
	
	/**
	 * Invoked during dragging if the mouse is dragged.
	 * <p>
	 * Moves the {@link FXInternalWindow} according to the mouse movement.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onDraggingMouseDragged(MouseEvent pMouseEvent)
	{
		if (lastDragPosition != null)
		{
			pMouseEvent.consume();
			
			Point2D corrected = getCorrectSceneCoordinates(pMouseEvent);
			
			Parent windowParent = getControl().getParent();
			
			if (windowParent.localToScene(windowParent.getLayoutBounds()).contains(corrected))
			{
				double x = getControl().getLayoutX();
				double y = getControl().getLayoutY();
				double diffX = corrected.getX() - lastDragPosition.getX();
				double diffY = corrected.getY() - lastDragPosition.getY();
				
				getControl().setCache(true);
				titleBar.setCursor(Cursor.MOVE);
				getControl().relocate(x + diffX, y + diffY);
			
				lastDragPosition = corrected;
			}
		}
		else if (lastDragPosition != null && !pMouseEvent.isStillSincePress())
		{
			lastDragPosition = getCorrectSceneCoordinates(pMouseEvent);
		}
	}
	
	/**
	 * Invoked during dragging if the mouse is pressed.
	 * <p>
	 * Activates dragging of the {@link FXInternalWindow}.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onDraggingMousePressed(MouseEvent pMouseEvent)
	{
		if (getControl().isMoveable() && pMouseEvent.isPrimaryButtonDown())
		{
			pMouseEvent.consume();
			
			lastDragPosition = getCorrectSceneCoordinates(pMouseEvent);
		}
	}
	
	/**
	 * Invoked during dragging if the mouse is released.
	 * <p>
	 * Stops dragging of the {@link FXInternalWindow}.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onDraggingMouseReleased(MouseEvent pMouseEvent)
	{
		getControl().setCache(false);
		
		lastDragPosition = null;
		titleBar.setCursor(Cursor.DEFAULT);
	}
	
	/**
	 * Invoked if the mouse is pressed.
	 * <p>
	 * Raises the {@link FXInternalWindow}, if that is activated.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onMousePressedFilter(MouseEvent pMouseEvent)
	{
		if (getControl().isRaiseOnClick())
		{
			getControl().toFront();
			getControl().setActive(true);
		}
	}
	
	/**
	 * Invoked during resizing if the mouse is dragged.
	 * <p>
	 * Resizes the {@link FXInternalWindow} according to the mouse position.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onResizingMouseDragged(MouseEvent pMouseEvent)
	{
		if (lastDragPosition == null && lastResizePosition != null && resizePos != null)
		{
			if (isInsideParent(pMouseEvent.getSceneX(), pMouseEvent.getSceneY()))
			{
				Point2D corrected = getCorrectSceneCoordinates(pMouseEvent);
				
				double x = getControl().getLayoutX();
				double y = getControl().getLayoutY();
				double width = getControl().getWidth();
				double height = getControl().getHeight();
				
				double diffX = corrected.getX() - lastResizePosition.getX();
				double diffY = corrected.getY() - lastResizePosition.getY();
				
				if (resizePos == Pos.TOP_LEFT || resizePos == Pos.CENTER_LEFT || resizePos == Pos.BOTTOM_LEFT)
				{
					width = sanitizeWidth(width - diffX);
					
					if (width != getControl().getWidth())
					{
						x = x + diffX;
					}
				}
				else if (resizePos == Pos.TOP_RIGHT || resizePos == Pos.CENTER_RIGHT || resizePos == Pos.BOTTOM_RIGHT)
				{
					width = sanitizeWidth(width + diffX);
				}
				
				if (resizePos == Pos.TOP_LEFT || resizePos == Pos.TOP_CENTER || resizePos == Pos.TOP_RIGHT)
				{
					height = sanitizeHeight(height - diffY);
					
					if (height != getControl().getHeight())
					{
						y = y + diffY;
					}
				}
				else if (resizePos == Pos.BOTTOM_LEFT || resizePos == Pos.BOTTOM_CENTER || resizePos == Pos.BOTTOM_RIGHT)
				{
					height = sanitizeHeight(height + diffY);
				}
				
				getControl().resizeRelocate(x, y, width, height);
				lastResizePosition = getCorrectSceneCoordinates(pMouseEvent);
			}
		}
	}
	
	/**
	 * Invoked during resizing if the mouse has left the
	 * {@link FXInternalWindow}.
	 * <p>
	 * Resets the mouse pointer if the {@link FXInternalWindow} is not currently
	 * being resized.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onResizingMouseExited(MouseEvent pMouseEvent)
	{
		if (lastResizePosition == null)
		{
			resizePos = null;
			getControl().setCursor(null);
		}
	}
	
	/**
	 * Invoked during resizing if the mouse is moved.
	 * <p>
	 * Sets the correct mouse cursor.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onResizingMouseMovedFilter(MouseEvent pMouseEvent)
	{
		if (lastDragPosition == null && !getControl().isBorderless() && getControl().isActive())
		{
			if (getControl().isResizeable() && !isInsideWindow(pMouseEvent.getX(), pMouseEvent.getY()))
			{
				resizePos = getResizeDirection(pMouseEvent.getX(), pMouseEvent.getY());
				resizePos = sanitizeResizeDirection(resizePos);
				
				getControl().setCursor(convertPosToResizeCursor(resizePos));
			}
			else
			{
				resizePos = null;
				getControl().setCursor(null);
			}
		}
	}
	
	/**
	 * Invoked during resizing if the mouse is pressed.
	 * <p>
	 * Activates resizing of the {@link FXInternalWindow}.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onResizingMousePressed(MouseEvent pMouseEvent)
	{
		if (lastDragPosition == null && pMouseEvent.isPrimaryButtonDown() && getControl().isResizeable() && !getControl().isBorderless())
		{
			lastResizePosition = getCorrectSceneCoordinates(pMouseEvent);
		}
	}
	
	/**
	 * Invoked during resizing if the mouse is released.
	 * <p>
	 * Stops resizing of the {@link FXInternalWindow}.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onResizingMouseReleased(MouseEvent pMouseEvent)
	{
		lastResizePosition = null;
		getControl().setCursor(Cursor.DEFAULT);
	}
	
	/**
	 * Invoked if the titlebar is clicked.
	 * <p>
	 * Maximizes/Restores the window if the titlebar is double clicked and also
	 * shows the window menu.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onTitleBarMouseClicked(MouseEvent pMouseEvent)
	{
		if (getControl().isMaximizeable() && pMouseEvent.getButton() == MouseButton.PRIMARY && pMouseEvent.getClickCount() == 2)
		{
			pMouseEvent.consume();
			
			windowMenu.hide();
			
			if (getControl().getState() == State.MAXIMIZED)
			{
				getControl().setState(State.NORMAL);
			}
			else
			{
				getControl().setState(State.MAXIMIZED);
			}
		}
		else if (pMouseEvent.getButton() == MouseButton.SECONDARY)
		{
			pMouseEvent.consume();
			
			windowMenu.hide();
			windowMenu.show(titleBar, pMouseEvent.getScreenX(), pMouseEvent.getScreenY());
		}
		else
		{
			windowMenu.hide();
		}
	}
	
	/**
	 * Sanitizes the given height by making sure that is greater or equal to
	 * zero or the minimum height, and less or equal the maximum height.
	 * 
	 * @param pHeight the value to sanitize.
	 * @return the sanitized value.
	 */
	private double sanitizeHeight(double pHeight)
	{
		double sanitizedHeight = pHeight;
		
		sanitizedHeight = Math.max(sanitizedHeight, getControl().getMinHeight());
		sanitizedHeight = Math.max(sanitizedHeight, 0);
		
		if (getControl().getMaxHeight() > 0 && Double.isFinite(getControl().getMaxHeight()))
		{
			sanitizedHeight = Math.min(sanitizedHeight, getControl().getMaxHeight());
		}
		
		return sanitizedHeight;
	}
	
	/**
	 * Sanitizes the given resize direction, for example making sure that the
	 * window can't be resized in a way that would not be possible in its
	 * current state.
	 * 
	 * @param pResizeDirection the resize direction.
	 * @return the sanitized resize direction.
	 */
	private Pos sanitizeResizeDirection(Pos pResizeDirection)
	{
		if (pResizeDirection != null && getControl().getState() == State.MINIMIZED)
		{
			switch (pResizeDirection)
			{
				case BOTTOM_LEFT:
				case TOP_LEFT:
					return Pos.CENTER_LEFT;
					
				case BOTTOM_RIGHT:
				case TOP_RIGHT:
					return Pos.CENTER_RIGHT;
					
				case BOTTOM_CENTER:
				case TOP_CENTER:
					return null;
					
				default:
					return pResizeDirection;
					
			}
		}
		
		return pResizeDirection;
	}
	
	/**
	 * Sanitizes the given width by making sure that is greater or equal to zero
	 * or the minimum width, and less or equal the maximum width.
	 * 
	 * @param pWidth the value to sanitize.
	 * @return the sanitized value.
	 */
	private double sanitizeWidth(double pWidth)
	{
		double sanitizedWidth = pWidth;
		
		sanitizedWidth = Math.max(sanitizedWidth, getControl().getMinWidth());
		sanitizedWidth = Math.max(sanitizedWidth, 0);
		
		if (getControl().getMaxWidth() > 0)
		{
			sanitizedWidth = Math.min(sanitizedWidth, getControl().getMaxWidth());
		}
		
		return sanitizedWidth;
	}
	
	/**
	 * Sets up the logic needed for dragging the {@link FXInternalWindow}.
	 */
	private void setupDragging()
	{
		titleBar.setOnMouseDragged(this::onDraggingMouseDragged);
		titleBar.setOnMousePressed(this::onDraggingMousePressed);
		titleBar.setOnMouseReleased(this::onDraggingMouseReleased);
	}
	
	/**
	 * Sets up the logic needed for resizing the {@link FXInternalWindow}.
	 */
	private void setupResizing()
	{
		FXInternalWindow internalWindow = getControl();
		
		internalWindow.setOnMouseDragged(this::onResizingMouseDragged);
		internalWindow.setOnMouseExited(this::onResizingMouseExited);
		internalWindow.setOnMousePressed(this::onResizingMousePressed);
		internalWindow.setOnMouseReleased(this::onResizingMouseReleased);
		
		internalWindow.addEventFilter(MouseEvent.MOUSE_MOVED, windowResizedMouseMovedFilter);
		internalWindow.addEventFilter(MouseEvent.MOUSE_PRESSED, windowMousePressedFilter);
	}
	
}	// FXInternalWindowBehavior
