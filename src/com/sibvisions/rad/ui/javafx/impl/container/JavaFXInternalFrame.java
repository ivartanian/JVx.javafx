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
package com.sibvisions.rad.ui.javafx.impl.container;

import javax.rad.genui.UIPoint;
import javax.rad.ui.IComponent;
import javax.rad.ui.IDimension;
import javax.rad.ui.IImage;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.util.TranslationMap;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.image.Image;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow.State;
import com.sibvisions.rad.ui.javafx.ext.mdi.WindowClosedEvent;
import com.sibvisions.rad.ui.javafx.ext.mdi.WindowClosingEvent;
import com.sibvisions.rad.ui.javafx.ext.mdi.WindowStateChangedEvent;
import com.sibvisions.rad.ui.javafx.ext.util.FXFrameWaitUtil;
import com.sibvisions.rad.ui.javafx.impl.JavaFXDimension;
import com.sibvisions.rad.ui.javafx.impl.JavaFXImage;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXILayoutContainerHybrid;

/**
 * The {@link JavaFXInternalFrame} is the JavaFX specific implementation of
 * {@link IInternalFrame}.
 * 
 * @author Robert Zenz
 * @see IInternalFrame
 * @see FXInternalWindow
 */
public class JavaFXInternalFrame extends JavaFXAbstractFrameBase<FXInternalWindow>
		implements IInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The parent {@link IDekstopPanel}. */
	private IDesktopPanel desktopPanel;
	
	/** The {@link IImage icon}. */
	private IImage icon;
	
	/** the skin. */
	private JavaFXTranslatableInternalWindowSkin skin;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXInternalFrame}.
	 *
	 * @param pDesktopPanel the desktop panel.
	 */
	public JavaFXInternalFrame(IDesktopPanel pDesktopPanel)
	{
		super(new FXInternalWindow());
		
		// Create and set the translatable skin.
		skin = new JavaFXTranslatableInternalWindowSkin(resource);
		resource.setSkin(skin);
		
		setVisible(false);
		
		// Disable clipping for the window, as the inner container is clipped
		// and we want to be able to have effects that expand beyond
		// the window boundaries (like a drop shadow).
		setClippingEnabled(false);
		
		desktopPanel = pDesktopPanel;
		
		resource.setContent((Parent)toolBarPanel.getResource());
		
		resource.activeProperty().addListener(this::onActiveChanged);
		resource.setOnClosing(this::onWindowClosing);
		resource.setOnClosed(this::onWindowClosed);
		resource.setOnStateChanged(this::onWindowStateChanged);
		resource.visibleProperty().addListener(this::onVisibleChanged);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getIconImage()
	{
		return icon;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIconImage(IImage pIconImage)
	{
		icon = pIconImage;
		
		if (icon != null)
		{
			resource.setIcon((Image)((JavaFXImage)icon).getResource());
		}
		else
		{
			resource.setIcon(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getState()
	{
		switch (resource.getState())
		{
			case MAXIMIZED:
				return MAXIMIZED_BOTH;
				
			case MINIMIZED:
				return ICONIFIED;
				
			case NORMAL:
			default:
				return NORMAL;
				
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setState(int pState)
	{
		switch (pState)
		{
			case ICONIFIED:
				resource.setState(State.MINIMIZED);
				break;
				
			case MAXIMIZED_BOTH:
			case MAXIMIZED_HORIZ:
			case MAXIMIZED_VERT:
				resource.setState(State.MAXIMIZED);
				break;
				
			case NORMAL:
			default:
				resource.setState(State.NORMAL);
				
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle()
	{
		return resource.getTitle();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTitle(String pTitle)
	{
		resource.setTitle(pTitle);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosed()
	{
		return resource.getParent() == null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close()
	{
		// Events are fired by the FXInternalWindow when it is closed.
		resource.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDisposed()
	{
		return resource.getParent() == null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		// Events are fired by the FXInternalWindow when it is disposed.
		resource.dispose();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIconifiable()
	{
		return resource.isMinimizeable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIconifiable(boolean pIconifiable)
	{
		resource.setMinimizable(pIconifiable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaximizable()
	{
		return resource.isMaximizeable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMaximizable(boolean pMaximizable)
	{
		resource.setMaximizeable(pMaximizable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isModal()
	{
		return resource.isModal();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModal(boolean pModal)
	{
		resource.setModal(pModal);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResizable()
	{
		return resource.isResizeable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResizable(boolean pResizable)
	{
		resource.setResizeable(pResizable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosable()
	{
		return resource.isCloseable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClosable(boolean pClosable)
	{
		resource.setCloseable(pClosable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TranslationMap getTranslation()
	{
		return skin.getTranslation();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(TranslationMap pTranslation)
	{
		skin.setTranslation(pTranslation);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive()
	{
		return resource.isFocused();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pack()
	{
		// TODO HACK Find a way without using invokeLater().
		// Some components (labels, buttons, etc.) are reporting a wrong size
		// the moment pack() is called form the JVx code.
		// "Sometime later" they report the correct size, hence
		// the invokeLater().
		FXFrameWaitUtil.runLater(() ->
		{
			resource.autosize();
			resource.requestLayout();
		}, 3);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void centerRelativeTo(IComponent pComponent)
	{
		// TODO HACK Find a way without using invokeLater().
		// pack() is invokeLater(), there for this also need to
		// be invokeLater().
		FXFrameWaitUtil.runLater(() ->
		{
			IDimension parentSize = null;
			
			if (resource.getParent() != null)
			{
				Bounds parentBounds = resource.getParent().getBoundsInParent();
				// TODO HACK Solves an off by one error which I can't place.
				parentSize = new JavaFXDimension(parentBounds.getWidth(), parentBounds.getHeight() + 1);
			}
			else
			{
				parentSize = desktopPanel.getSize();
			}
			
			IDimension size = getSize();
			
			int x = (parentSize.getWidth() - size.getWidth()) / 2;
			int y = (parentSize.getHeight() - size.getHeight()) / 2;
			
			setLocationRelativeTo(pComponent, new UIPoint(x, y));
		}, 3);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toBack()
	{
		resource.toBack();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toFront()
	{
		resource.toFront();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setLayoutInternal(JavaFXILayoutContainerHybrid pLayoutContainerHybrid)
	{
		if (toolBarPanel != null)
		{
			toolBarPanel.setLayoutInternal(pLayoutContainerHybrid);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Is called if the {@link FXInternalWindow#activeProperty()} changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onActiveChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (pNewValue.booleanValue())
		{
			fireWindowActivated();
		}
		else
		{
			fireWindowDeactivated();
		}
	}
	
	/**
	 * Is called if the {@link FXInternalWindow} is closing.
	 * 
	 * @param pWindowClosingEvent the {@link WindowClosingEvent}.
	 */
	private void onWindowClosing(WindowClosingEvent pWindowClosingEvent)
	{
		// We need to consume the event here, so that the FXInternalWindow
		// doesn't really close.
		pWindowClosingEvent.consume();
		
		fireWindowClosing();
	}
	
	/**
	 * Is called if the {@link FXInternalWindow} is closed.
	 * 
	 * @param pWindowClosedEvent the {@link WindowClosedEvent}.
	 */
	private void onWindowClosed(WindowClosedEvent pWindowClosedEvent)
	{
		fireWindowClosed();
	}
	
	/**
	 * Is called if the {@link FXInternalWindow} changes its state.
	 * 
	 * @param pWindowStateChangedEvent the {@link WindowStateChangedEvent}.
	 */
	private void onWindowStateChanged(WindowStateChangedEvent pWindowStateChangedEvent)
	{
		if (pWindowStateChangedEvent.getPreviousState() == State.MINIMIZED)
		{
			fireWindowDeiconified();
		}
		else if (pWindowStateChangedEvent.getNewState() == State.MINIMIZED)
		{
			fireWindowIconified();
		}
	}
	
	/**
	 * Is called if the {@link FXInternalWindow#visibleProperty()} changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onVisibleChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (pNewValue.booleanValue())
		{
			fireWindowOpened();
		}
	}
	
}	// JavaFXInternalFrame
