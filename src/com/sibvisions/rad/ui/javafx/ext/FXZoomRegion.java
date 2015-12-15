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
package com.sibvisions.rad.ui.javafx.ext;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * The {@link FXZoomRegion} is a simple container that allows that its content
 * is zoomed in and out.
 * 
 * @author Robert Zenz
 */
public class FXZoomRegion extends Region implements IFXZoomable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Defines when the scrollbars are visible.
	 */
	public enum ScrollBarVisibility
	{
		/** Always show the scrollbars. */
		ALWAYS,
		
		/** Never show the scrollbars. */
		NEVER,
		
		/** Only show the scrollbars if the control is zoomed in. */
		ONLY_IF_ZOOMED_IN,
		
		/** Only show the scrollbars if the control is zoomed out. */
		ONLY_IF_ZOOMED_OUT
		
	}	// ScrollBarVisibility    
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Rectangle} used to clip the {@link #clippingContainer}. */
	private Rectangle clip;
	
	/** The {@link StackPane} used for clipping the {@link #content}. */
	private StackPane clippingContainer;
	
	/** The property for the content that is displayed. */
	private ObjectProperty<Node> content;
	
	/** The horizontal {@link ScrollBar}. */
	private ScrollBar horizontalScrollBar;
	
	/** The last x position of the mouse. Used for dragging. */
	private double lastMousePositionX;
	
	/** The last y position of the mouse. Used for dragging. */
	private double lastMousePositionY;
	
	/** The {@link ScrollBarVisibility} property. */
	private ObjectProperty<ScrollBarVisibility> scrollBarVisibility;
	
	/** The vertical {@link ScrollBar}. */
	private ScrollBar verticalScrollBar;
	
	/** The {@link FXZoomableHelper} that is used. */
	private FXZoomableHelper zoomHelper;
	
	/** The {@link StackPane} that holds the {@link #content} and is scaled. */
	private StackPane zoomablePane;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXZoomRegion}.
	 */
	public FXZoomRegion()
	{
		this(0, Double.MAX_VALUE);
	}
	
	/**
	 * Creates a new instance of {@link FXZoomRegion}.
	 *
	 * @param pMinZoomValue the minimum zoom value.
	 * @param pMaxZoomValue the maximum zoom value.
	 */
	public FXZoomRegion(double pMinZoomValue, double pMaxZoomValue)
	{
		super();
		
		clippingContainer = new StackPane();
		getChildren().add(clippingContainer);
		
		content = new SimpleObjectProperty<>();
		content.addListener(this::onContentChanged);
		
		horizontalScrollBar = new ScrollBar();
		horizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
		
		getChildren().add(horizontalScrollBar);
		
		scrollBarVisibility = new SimpleObjectProperty<>(ScrollBarVisibility.ONLY_IF_ZOOMED_IN);
		
		verticalScrollBar = new ScrollBar();
		verticalScrollBar.setOrientation(Orientation.VERTICAL);
		
		getChildren().add(verticalScrollBar);
		
		zoomHelper = new FXZoomableHelper(pMinZoomValue, pMaxZoomValue);
		zoomHelper.zoomProperty().addListener(this::onZoomChanged);
		
		zoomablePane = new StackPane();
		clippingContainer.getChildren().add(zoomablePane);
		
		clip = new Rectangle();
		clip.widthProperty().bind(clippingContainer.widthProperty());
		clip.heightProperty().bind(clippingContainer.heightProperty());
		clippingContainer.setClip(clip);
		
		horizontalScrollBar.valueProperty().bindBidirectional(zoomablePane.translateXProperty());
		verticalScrollBar.valueProperty().bindBidirectional(zoomablePane.translateYProperty());
		
		addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
		addEventFilter(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
		addEventFilter(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
		addEventFilter(ScrollEvent.SCROLL, this::onScroll);
		
		update();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZoom()
	{
		return zoomHelper.getZoom();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZoomFactor()
	{
		return zoomHelper.getZoomFactor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomEnabled()
	{
		return zoomHelper.isZoomEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomInPossible()
	{
		return zoomHelper.isZoomInPossible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomOutPossible()
	{
		return zoomHelper.isZoomOutPossible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZoomResetPossible()
	{
		return zoomHelper.isZoomResetPossible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoom(double pZoom)
	{
		zoomHelper.setZoom(pZoom);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoomEnabled(boolean pZoomEnabled)
	{
		zoomHelper.setZoomEnabled(pZoomEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZoomFactor(double pZoomFactor)
	{
		zoomHelper.setZoomFactor(pZoomFactor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoom(int pSteps)
	{
		zoomHelper.zoom(pSteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BooleanProperty zoomEnabledProperty()
	{
		return zoomHelper.zoomEnabledProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleProperty zoomFactorProperty()
	{
		return zoomHelper.zoomFactorProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomIn()
	{
		zoomHelper.zoomIn();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomIn(int pSteps)
	{
		zoomHelper.zoomIn(pSteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomInPossibleProperty()
	{
		return zoomHelper.zoomInPossibleProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomOut()
	{
		zoomHelper.zoomOut();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomOut(int pSteps)
	{
		zoomHelper.zoomOut(pSteps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomOutPossibleProperty()
	{
		return zoomHelper.zoomOutPossibleProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleProperty zoomProperty()
	{
		return zoomHelper.zoomProperty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomReset()
	{
		zoomHelper.zoomReset();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadOnlyBooleanProperty zoomResetPossibleProperty()
	{
		return zoomHelper.zoomOutPossibleProperty();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		// TODO HACK Scrollbars are rotated by scaling.
		// We will rotate the scrollbars, otherwise they would be "the wrong way
		// round", meaning the left would be right and right would be left.
		if (horizontalScrollBar.getScaleX() != -1)
		{
			horizontalScrollBar.setScaleX(-1);
			
			verticalScrollBar.setScaleY(-1);
			
			// Rotate the "thumb" of the scrollbar by itself too, as it is
			// has (most likely) a gradient and we want to make sure that it
			// looks correctly.
			verticalScrollBar.lookup(".scroll-bar *.thumb").setScaleY(-1);
		}
		
		double width = getWidth();
		double height = getHeight();
		
		if (horizontalScrollBar.isVisible() && horizontalScrollBar.isManaged())
		{
			horizontalScrollBar.autosize();
			height = height - horizontalScrollBar.getHeight();
		}
		
		if (verticalScrollBar.isVisible() && verticalScrollBar.isManaged())
		{
			verticalScrollBar.autosize();
			width = width - verticalScrollBar.getWidth();
		}
		
		horizontalScrollBar.resizeRelocate(0, height, width, horizontalScrollBar.getHeight());
		verticalScrollBar.resizeRelocate(width, 0, verticalScrollBar.getWidth(), height);
		
		clippingContainer.resizeRelocate(0, 0, width, height);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property for the content.
	 * <p>
	 * The content is the {@link Node} that is displayed inside this
	 * {@link FXZoomRegion}. Note that the content will be stretched to the size
	 * of this control, very similar to how a {@link StackPane} behaves. It will
	 * also be resized if the scrollbars become visible or hidden.
	 * 
	 * @return the property for the content.
	 * @see #getContent()
	 * @see #setContent(Node)
	 */
	public ObjectProperty<Node> contentProperty()
	{
		return content;
	}
	
	/**
	 * Gets the content.
	 * 
	 * @return the content.
	 * @see #contentProperty()
	 * @see #setContent(Node)
	 */
	public Node getContent()
	{
		return content.get();
	}
	
	/**
	 * Gets the property for the visibility of the scrollbars.
	 * 
	 * @return the the property for the visibility of the scrollbars.
	 * @see #scrollBarVisibilityProperty()
	 * @see #setScrollBarVisibility(ScrollBarVisibility)
	 * @see ScrollBarVisibility
	 */
	public ScrollBarVisibility getScrollBarVisibility()
	{
		return scrollBarVisibility.get();
	}
	
	/**
	 * Resets the {@link #zoomProperty()} to 1, meaning not zoomed.
	 */
	public void reset()
	{
		zoomHelper.zoomProperty().set(1);
		
		zoomablePane.setTranslateX(0);
		zoomablePane.setTranslateY(0);
		
		update();
	}
	
	/**
	 * Gets the property for the visibility of the scrollbars.
	 * 
	 * @return the property for the visibility of the scrollbars.
	 * @see #getScrollBarVisibility()
	 * @see #setScrollBarVisibility(ScrollBarVisibility)
	 * @see ScrollBarVisibility
	 */
	public ObjectProperty<ScrollBarVisibility> scrollBarVisibilityProperty()
	{
		return scrollBarVisibility;
	}
	
	/**
	 * Sets the {@link Node content}.
	 * 
	 * @param pContent the {@link Node content}.
	 * @see #contentProperty()
	 * @see #getContent()
	 */
	public void setContent(Node pContent)
	{
		content.set(pContent);
	}
	
	/**
	 * Sets the visibility of the scrollbars.
	 * 
	 * @param pScrollBarVisibility the visibility of the scrollbars.
	 * @see #getScrollBarVisibility()
	 * @see #scrollBarVisibilityProperty()
	 * @see ScrollBarVisibility
	 */
	public void setScrollBarVisibility(ScrollBarVisibility pScrollBarVisibility)
	{
		scrollBarVisibility.set(pScrollBarVisibility);
	}
	
	/**
	 * Zooms in or out the given number of steps, keeping the given point in
	 * focus. One step equals the set zoom factor.
	 * 
	 * @param pSteps the steps to zoom in or out.
	 * @param pFocusPointX the x coordinate.
	 * @param pFocusPointY the y coordinate.
	 */
	public void zoom(int pSteps, double pFocusPointX, double pFocusPointY)
	{
		Bounds zoomablePaneBounds = zoomablePane.getBoundsInParent();
		
		double deltaX = pFocusPointX - (zoomablePaneBounds.getWidth() / 2 + zoomablePaneBounds.getMinX());
		double deltaY = pFocusPointY - (zoomablePaneBounds.getHeight() / 2 + zoomablePaneBounds.getMinY());
		
		double oldZoom = zoomHelper.getZoom();
		
		zoom(pSteps);
		
		zoomablePane.setTranslateX(zoomablePane.getTranslateX() - deltaX * (zoomHelper.getZoom() / oldZoom - 1));
		zoomablePane.setTranslateY(zoomablePane.getTranslateY() - deltaY * (zoomHelper.getZoom() / oldZoom - 1));
		
		update();
	}
	
	/**
	 * Determines if the scrollbars are visible.
	 * 
	 * @return {@code true} if they are visible.
	 */
	private boolean areScrollBarsVisible()
	{
		switch (scrollBarVisibility.get())
		{
			case ALWAYS:
				return true;
				
			case ONLY_IF_ZOOMED_IN:
				return zoomHelper.getZoom() > 1.0;
				
			case ONLY_IF_ZOOMED_OUT:
				return zoomHelper.getZoom() < 1.0;
				
			case NEVER:
			default:
				return false;
				
		}
	}
	
	/**
	 * Invoked if the {@link #content} changes.
	 * <p>
	 * Sets the new content (if any) into the {@link #zoomablePane}.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onContentChanged(ObservableValue<? extends Node> pObservable, Node pOldValue, Node pNewValue)
	{
		if (pOldValue != null)
		{
			zoomablePane.getChildren().remove(pOldValue);
		}
		
		if (pNewValue != null)
		{
			zoomablePane.getChildren().add(pNewValue);
			requestLayout();
		}
	}
	
	/**
	 * Invoked if a key is pressed.
	 * 
	 * @param pKeyEvent the event.
	 */
	private void onKeyPressed(KeyEvent pKeyEvent)
	{
		if (pKeyEvent.isControlDown())
		{
			switch (pKeyEvent.getCode())
			{
				case ADD:
					pKeyEvent.consume();
					zoomIn();
					break;
					
				case SUBTRACT:
					pKeyEvent.consume();
					zoomOut();
					break;
					
				case NUMPAD0:
					pKeyEvent.consume();
					reset();
					break;
					
				default:
					// Nothing to be done.
					
			}
		}
		
	}
	
	/**
	 * Invoked if a mouse is dragged.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onMouseDragged(MouseEvent pMouseEvent)
	{
		if ((pMouseEvent.isControlDown() && pMouseEvent.isPrimaryButtonDown())
				|| pMouseEvent.isMiddleButtonDown())
		{
			pMouseEvent.consume();
			
			if (!pMouseEvent.isStillSincePress())
			{
				double deltaX = pMouseEvent.getScreenX() - lastMousePositionX;
				double deltaY = pMouseEvent.getScreenY() - lastMousePositionY;
				
				zoomablePane.setTranslateX(zoomablePane.getTranslateX() + deltaX);
				zoomablePane.setTranslateY(zoomablePane.getTranslateY() + deltaY);
				
				update();
			}
			
			lastMousePositionX = pMouseEvent.getScreenX();
			lastMousePositionY = pMouseEvent.getScreenY();
		}
	}
	
	/**
	 * Invoked if a mouse is pressed.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onMousePressed(MouseEvent pMouseEvent)
	{
		lastMousePositionX = pMouseEvent.getScreenX();
		lastMousePositionY = pMouseEvent.getScreenY();
		
		if (pMouseEvent.isControlDown())
		{
			pMouseEvent.consume();
		}
	}
	
	/**
	 * Invoked if the mouse wheel scrolls.
	 * 
	 * @param pScrollEvent the event.
	 */
	private void onScroll(ScrollEvent pScrollEvent)
	{
		if (pScrollEvent.isControlDown())
		{
			pScrollEvent.consume();
			
			double turns = pScrollEvent.getDeltaY() / pScrollEvent.getMultiplierY();
			Point2D mousePoint = sceneToLocal(pScrollEvent.getSceneX(), pScrollEvent.getSceneY());
			
			zoom((int)turns, mousePoint.getX(), mousePoint.getY());
		}
	}
	
	/**
	 * Invoked if the value of the {@link #zoom} property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onZoomChanged(ObservableValue<? extends Number> pObservable, Number pOldValue, Number pNewValue)
	{
		zoomablePane.setScaleX(pNewValue.doubleValue());
		zoomablePane.setScaleY(pNewValue.doubleValue());
		
		update();
	}
	
	/**
	 * Updates the complete control.
	 */
	private void update()
	{
		double width = zoomablePane.getWidth();
		double height = zoomablePane.getHeight();
		
		double zoomedWidth = width * zoomHelper.getZoom();
		double zoomedHeight = height * zoomHelper.getZoom();
		
		double leftEdge = (zoomedWidth - width) / 2;
		double rightEdge = (width - zoomedWidth) / 2;
		double topEdge = (zoomedHeight - height) / 2;
		double bottomEdge = (height - zoomedHeight) / 2;
		
		if (zoomHelper.getZoom() > 1.0)
		{
			horizontalScrollBar.setMin(rightEdge);
			horizontalScrollBar.setMax(leftEdge);
			
			verticalScrollBar.setMin(bottomEdge);
			verticalScrollBar.setMax(topEdge);
			
			zoomablePane.setTranslateX(Math.max(rightEdge, Math.min(leftEdge, zoomablePane.getTranslateX())));
			zoomablePane.setTranslateY(Math.max(bottomEdge, Math.min(topEdge, zoomablePane.getTranslateY())));
		}
		else
		{
			horizontalScrollBar.setMin(leftEdge);
			horizontalScrollBar.setMax(rightEdge);
			
			verticalScrollBar.setMin(topEdge);
			verticalScrollBar.setMax(bottomEdge);
			
			zoomablePane.setTranslateX(Math.max(leftEdge, Math.min(rightEdge, zoomablePane.getTranslateX())));
			zoomablePane.setTranslateY(Math.max(topEdge, Math.min(bottomEdge, zoomablePane.getTranslateY())));
		}
		
		boolean wasVisible = horizontalScrollBar.isVisible();
		boolean scrollBarsVisible = areScrollBarsVisible();
		
		horizontalScrollBar.setVisible(scrollBarsVisible);
		horizontalScrollBar.setManaged(scrollBarsVisible);
		verticalScrollBar.setVisible(scrollBarsVisible);
		verticalScrollBar.setManaged(scrollBarsVisible);
		
		if (!wasVisible && scrollBarsVisible)
		{
			horizontalScrollBar.autosize();
			verticalScrollBar.autosize();
			
			zoomablePane.setTranslateX(zoomablePane.getTranslateX() + (verticalScrollBar.getWidth() / 2) * zoomHelper.getZoom());
			zoomablePane.setTranslateY(zoomablePane.getTranslateY() + (horizontalScrollBar.getHeight() / 2) * zoomHelper.getZoom());
		}
	}
	
}	// FXZoomRegion
