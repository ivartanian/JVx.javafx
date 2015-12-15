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
package com.sibvisions.rad.ui.javafx.impl.component;

import javax.rad.ui.IComponent;
import javax.rad.ui.IDimension;
import javax.rad.ui.IImage;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import com.sibvisions.rad.ui.javafx.ext.StyleContainer;
import com.sibvisions.rad.ui.javafx.ext.util.NodeUtil;
import com.sibvisions.rad.ui.javafx.impl.JavaFXDimension;
import com.sibvisions.rad.ui.javafx.impl.JavaFXImage;
import com.sibvisions.rad.ui.javafx.impl.JavaFXPoint;
import com.sibvisions.rad.ui.javafx.impl.JavaFXRectangle;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@link JavaFXComponent} is the JavaFX specific implementation of
 * {@link IComponent} and serves as base for most components.
 * 
 * @author Robert Zenz
 * @param <C> the type of the component.
 * @see IComponent
 * @see Region
 */
public class JavaFXComponent<C extends Region> extends JavaFXAbstractComponentBase<C> implements IComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The {@link Rectangle} that is used to clip the current component.
	 *
	 * @see #disableClipping()
	 * @see #enableClipping()
	 * @see Region#clipProperty()
	 */
	private Rectangle clipper;
	
	/** The {@link ChangeListener} for when the focus changes. */
	private ChangeListener<? super Boolean> focusListener;
	
	/** The tab/focus index. */
	private Integer tabIndex;
	
	/** The {@link Tooltip} of this component. */
	private Tooltip tooltip;
	
	/** The tooltip text. */
	private String tooltipText;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXComponent}.
	 *
	 * @param pResource the resource/component.
	 */
	public JavaFXComponent(C pResource)
	{
		super(pResource);
		
		focusListener = this::onFocusChanged;
		
		if (pResource != null)
		{
			// Make sure that the component can expand as far as possible.
			// Without setting the maximum size, components will determine their
			// maximum size on their own and will, f.e., not fill the center of
			// the border layout.
			pResource.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
			
			createAndAttachClipper();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage capture(int pWidth, int pHeight)
	{
		return new JavaFXImage(getName(), resource.snapshot(null, new WritableImage(pWidth, pHeight)));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRectangle getBounds()
	{
		return new JavaFXRectangle(resource.getLayoutX(), resource.getLayoutY(), resource.getWidth(), resource.getHeight());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getLocation()
	{
		return new JavaFXPoint(resource.getLayoutX(), resource.getLayoutY());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint getLocationRelativeTo(IComponent pComponent)
	{
		IPoint point = pComponent.getLocation();
		
		return new JavaFXPoint(resource.getLayoutX() - point.getX(), resource.getLayoutY() - point.getY());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDimension getMaximumSize()
	{
		return new JavaFXDimension(NodeUtil.getMaxWidth(resource), NodeUtil.getMaxHeight(resource));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDimension getMinimumSize()
	{
		return new JavaFXDimension(NodeUtil.getMinWidth(resource), NodeUtil.getMinHeight(resource));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return resource.getId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDimension getPreferredSize()
	{
		return new JavaFXDimension(NodeUtil.getPrefWidth(resource), NodeUtil.getPrefHeight(resource));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDimension getSize()
	{
		return new JavaFXDimension(resource.getWidth(), resource.getHeight());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getTabIndex()
	{
		return tabIndex;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolTipText()
	{
		return tooltipText;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled()
	{
		return !resource.isDisabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFocusable()
	{
		return resource.isFocusTraversable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaximumSizeSet()
	{
		return resource.getMaxWidth() != -1 && resource.getMaxHeight() != -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMinimumSizeSet()
	{
		return resource.getMinWidth() != -1 && resource.getMinHeight() != -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPreferredSizeSet()
	{
		return resource.getPrefWidth() != -1 && resource.getPrefHeight() != -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible()
	{
		return resource.isVisible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestFocus()
	{
		resource.requestFocus();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBounds(IRectangle pBounds)
	{
		resource.resizeRelocate(pBounds.getX(), pBounds.getY(), pBounds.getWidth(), pBounds.getHeight());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean pEnable)
	{
		resource.setDisable(!pEnable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocusable(boolean pFocusable)
	{
		resource.setFocusTraversable(pFocusable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocation(IPoint pLocation)
	{
		resource.relocate(pLocation.getX(), pLocation.getY());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocationRelativeTo(IComponent pComponent, IPoint pLocation)
	{
		int x = pLocation.getX();
		int y = pLocation.getY();
		
		if (pComponent != null)
		{
			x = x + pComponent.getLocation().getX();
			y = y + pComponent.getLocation().getY();
		}
		
		resource.relocate(x, y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMaximumSize(IDimension pMaximumSize)
	{
		if (pMaximumSize != null)
		{
			resource.setMaxSize(pMaximumSize.getWidth(), pMaximumSize.getHeight());
		}
		else
		{
			resource.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMinimumSize(IDimension pMinimumSize)
	{
		if (pMinimumSize != null)
		{
			resource.setMinSize(pMinimumSize.getWidth(), pMinimumSize.getHeight());
		}
		else
		{
			resource.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String pName)
	{
		resource.setId(pName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPreferredSize(IDimension pPreferredSize)
	{
		if (pPreferredSize != null)
		{
			resource.setPrefSize(pPreferredSize.getWidth(), pPreferredSize.getHeight());
		}
		else
		{
			resource.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(IDimension pSize)
	{
		if (pSize != null)
		{
			resource.resize(pSize.getWidth(), pSize.getHeight());
		}
		else
		{
			resource.autosize();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(Integer pTabIndex)
	{
		tabIndex = pTabIndex;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(String pText)
	{
		tooltipText = pText;
		
		if (!StringUtil.isEmpty(tooltipText))
		{
			if (tooltip == null)
			{
				tooltip = new Tooltip();
				Tooltip.install(resource, tooltip);
			}
			
			tooltip.setText(tooltipText);
		}
		else if (tooltip != null)
		{
			Tooltip.install(resource, null);
			tooltip = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		resource.setManaged(pVisible);
		resource.setVisible(pVisible);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the component moved listener.
	 */
	@Override
	protected void addComponentMovedListener()
	{
		resource.layoutXProperty().addListener(this::onComponentMoved);
		resource.layoutYProperty().addListener(this::onComponentMoved);
	}
	
	/**
	 * Adds the component resized listener.
	 */
	@Override
	protected void addComponentResizedListener()
	{
		resource.widthProperty().addListener(this::onComponentResized);
		resource.heightProperty().addListener(this::onComponentResized);
	}
	
	/**
	 * Adds the focus listener.
	 */
	@Override
	protected void addFocusListener()
	{
		resource.focusedProperty().removeListener(focusListener);
		resource.focusedProperty().addListener(focusListener);
	}
	
	/**
	 * Adds the key pressed listener.
	 */
	@Override
	protected void addKeyPressedListener()
	{
		resource.setOnKeyPressed(this::fireKeyPressedEvent);
	}
	
	/**
	 * Adds the key released listener.
	 */
	@Override
	protected void addKeyReleasedListener()
	{
		resource.setOnKeyReleased(this::fireKeyReleasedEvent);
	}
	
	/**
	 * Adds the key typed listener.
	 */
	@Override
	protected void addKeyTypedListener()
	{
		resource.setOnKeyTyped(this::fireKeyTypedEvent);
	}
	
	/**
	 * Adds the mouse clicked listener.
	 */
	@Override
	protected void addMouseClickedListener()
	{
		resource.setOnMouseClicked(this::fireMouseClickedEvent);
	}
	
	/**
	 * Adds the mouse entered listener.
	 */
	@Override
	protected void addMouseEnteredListener()
	{
		resource.setOnMouseEntered(this::fireMouseEnteredEvent);
	}
	
	/**
	 * Adds the mouse exited listener.
	 */
	@Override
	protected void addMouseExitedListener()
	{
		resource.setOnMouseExited(this::fireMouseExitedEvent);
	}
	
	/**
	 * Adds the mouse pressed listener.
	 */
	@Override
	protected void addMousePressedListener()
	{
		resource.setOnMousePressed(this::fireMousePressedEvent);
	}
	
	/**
	 * Adds the mouse released listener.
	 */
	@Override
	protected void addMouseReleasedListener()
	{
		resource.setOnMouseReleased(this::fireMouseReleasedEvent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResource(C pResource)
	{
		if (resource != null)
		{
			resource.getProperties().remove(StyleContainer.class);
		}
		
		super.setResource(pResource);
		
		// setParent does removed the style from the old parent, if any.
		styleContainer.setParent(resource);
		
		if (resource != null)
		{
			resource.getProperties().put(StyleContainer.class, styleContainer);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets if the clipping is enabled or disabled.
	 * <p>
	 * If clipping is enabled, the component is restrained to its bounds and can
	 * not draw outside of these bounds.
	 * 
	 * @param pEnabled {@code true} to enable clipping.
	 * @see #isClippingEnabled()
	 * @see Region#clipProperty()
	 */
	public void setClippingEnabled(boolean pEnabled)
	{
		if (pEnabled)
		{
			resource.setClip(clipper);
		}
		else
		{
			resource.setClip(null);
		}
	}
	
	/**
	 * Gets whether clipping is enabled or disabled.
	 * <p>
	 * If clipping is enabled, the component is restrained to its bounds and can
	 * not draw outside of these bounds.
	 * 
	 * @return {@code true} if clipping is enabled.
	 * @see #setClippingEnabled(boolean)
	 * @see Region#clipProperty()
	 */
	public boolean isClippingEnabled()
	{
		return resource.getClip() != null;
	}
	
	/**
	 * Creates {@link Rectangle} that is used for clipping and attaches it to
	 * the current
	 * {@link com.sibvisions.rad.ui.javafx.impl.JavaFXResource#resource}.
	 */
	protected void createAndAttachClipper()
	{
		clipper = new Rectangle();
		clipper.rotateProperty().bind(resource.rotateProperty());
		clipper.rotationAxisProperty().bind(resource.rotationAxisProperty());
		clipper.heightProperty().bind(resource.heightProperty());
		clipper.widthProperty().bind(resource.widthProperty());
		resource.setClip(clipper);
	}
	
	/**
	 * On component moved.
	 *
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onComponentMoved(ObservableValue<? extends Number> pObservableValue, Number pOldValue, Number pNewValue)
	{
		fireComponentMovedEvent();
	}
	
	/**
	 * On component resized.
	 *
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onComponentResized(ObservableValue<? extends Number> pObservableValue, Number pOldValue, Number pNewValue)
	{
		fireComponentResizedEvent();
	}
	
	/**
	 * On focus changed.
	 *
	 * @param pObservableValue the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onFocusChanged(ObservableValue<? extends Boolean> pObservableValue, Boolean pOldValue, Boolean pNewValue)
	{
		if (pNewValue.booleanValue())
		{
			fireFocusGainedEvent();
		}
		else
		{
			fireFocusLostEvent();
		}
	}
	
} // JavaFXComponent
