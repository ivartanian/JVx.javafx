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
package com.sibvisions.rad.ui.javafx.ext.mdi;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;

import com.sibvisions.rad.ui.javafx.ext.mdi.skin.FXDesktopPaneSkin;
import com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers.FXDesktopWindowManager;

/**
 * The {@link FXDesktopPane} is an desktop pane implementation that can host
 * {@link FXInternalWindow}s, which are managed by an {@link IFXWindowManager}.
 * <p>
 * It consists of two {@link Pane}s: The background pane which can be set, and
 * main window pane, which is provided by the window manager. The background
 * pane is always kept behind the window pane.
 * 
 * @author Robert Zenz
 */
public class FXDesktopPane extends Control
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The default style class. */
	public static final String DEFAULT_STYLE_CLASS = "desktop-pane";
	
	/** The {@link pane} that used for nodes. */
	private ObjectProperty<Pane> backgroundPane;
	
	/** If the navigation keys are enabled. */
	private BooleanProperty navigationKeysEnabled;
	
	/** The {@link EventHandler} for the window closed events. */
	private EventHandler<WindowClosedEvent> onWindowClosedListener;
	
	/** The {@link Map} of states of previous {@link IFXWindowManager}s. */
	private Map<Class<?>, Object> previousStates;
	
	/** The current {@link IFXwindowManager}. */
	private ObjectProperty<IFXWindowManager> windowManager;
	
	/** The main window {@link Pane}. */
	private Pane windowManagerPane;
	
	/** The {@link ObservableList} of {@link FXInternalWindow}s. */
	private ObservableList<FXInternalWindow> windows;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDesktopPane}.
	 */
	public FXDesktopPane()
	{
		this(new FXDesktopWindowManager());
	}
	
	/**
	 * Creates a new instance of {@link FXDesktopPane}.
	 *
	 * @param pWindowManager the {@link IFXWindowManager}.
	 */
	public FXDesktopPane(IFXWindowManager pWindowManager)
	{
		super();
		
		getStyleClass().setAll(DEFAULT_STYLE_CLASS);
		
		onWindowClosedListener = this::onWindowClosed;
		
		setCache(true);
		
		backgroundPane = new SimpleObjectProperty<>(null);
		backgroundPane.addListener(this::onBackgroundPaneChanged);
		
		navigationKeysEnabled = new SimpleBooleanProperty(true);
		
		previousStates = new HashMap<>();
		
		windowManager = new SimpleObjectProperty<>(pWindowManager);
		windowManager.addListener(this::onWindowManagerChanged);
		
		windows = new SimpleListProperty<>(FXCollections.observableArrayList());
		windows.addListener(this::onWindowListChanged);
		
		onWindowManagerChanged(null, null, windowManager.get());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Skin<?> createDefaultSkin()
	{
		return new FXDesktopPaneSkin(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		if (backgroundPane.get() != null && backgroundPane.get().isManaged())
		{
			backgroundPane.get().resizeRelocate(0, 0, getWidth(), getHeight());
		}
		
		windowManagerPane.resizeRelocate(0, 0, getWidth(), getHeight());
		windowManagerPane.toFront();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The property for the background {@link Pane}.
	 * 
	 * @return the property for the background {@link Pane}.
	 */
	public ObjectProperty<Pane> backgroundPaneProperty()
	{
		return backgroundPane;
	}
	
	/**
	 * Gets the currently active {@link FXInternalWindow}.
	 * 
	 * @return the currently active {@link FXInternalWindow}. {@code null} if
	 *         there either are no {@link FXInternalWindow}s or none are active.
	 * @see FXInternalWindow#activeProperty()
	 */
	public FXInternalWindow getActiveWindow()
	{
		for (FXInternalWindow window : windows)
		{
			if (window.isActive())
			{
				return window;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the current {@link IFXWindowManager}.
	 * 
	 * @return the current {@link IFXWindowManager}.
	 */
	public IFXWindowManager getWindowManager()
	{
		return windowManager.get();
	}
	
	/**
	 * Gets the {@link ObservableList} of {@link FXInternalWindow}s.
	 * 
	 * @return the {@link ObservableList} of {@link FXInternalWindow}s.
	 */
	public ObservableList<FXInternalWindow> getWindows()
	{
		return windows;
	}
	
	/**
	 * Gets if the navigation keys are enabled.
	 * 
	 * @return {@code true} if the navigation keys are enabled.
	 */
	public boolean isNavigationKeysEnabled()
	{
		return navigationKeysEnabled.get();
	}
	
	/**
	 * Gets the property for if the navigation keys are enabled.
	 * 
	 * @return the property for if the navigation keys are enabled.
	 */
	public BooleanProperty navigationKeysEnabledProeprty()
	{
		return navigationKeysEnabled;
	}
	
	/**
	 * Selects the next {@link FXInternalWindow} in the list.
	 */
	public void selectNextWindow()
	{
		if (getActiveWindow() == null || !getActiveWindow().isModal())
		{
			FXInternalWindow nextWindow = getNextWindow(windows.indexOf(getActiveWindow()));
			
			if (nextWindow != null && !nextWindow.isModal())
			{
				nextWindow.toFront();
				nextWindow.setActive(true);
			}
		}
	}
	
	/**
	 * Selects the previous {@link FXInternalWindow} in the list.
	 */
	public void selectPreviousWindow()
	{
		if (getActiveWindow() == null || !getActiveWindow().isModal())
		{
			FXInternalWindow previousWindow = getPreviousWindow(windows.indexOf(getActiveWindow()));
			
			if (previousWindow != null)
			{
				previousWindow.toFront();
				previousWindow.setActive(true);
			}
		}
	}
	
	/**
	 * Sets the background {@link Pane}.
	 * 
	 * @param pPane the background {@link Pane}.
	 */
	public void setBackgroundPane(Pane pPane)
	{
		backgroundPane.set(pPane);
	}
	
	/**
	 * Sets if the navigation keys are enabled.
	 * 
	 * @param pValue {@code true} if the navigation keys should be enabled.
	 */
	public void setNavigationKeysEnabled(boolean pValue)
	{
		navigationKeysEnabled.set(pValue);
	}
	
	/**
	 * Sets the {@link IFXWindowManager}.
	 * 
	 * @param pWindowManager the {@link IFXWindowManager}.
	 */
	public void setWindowManager(IFXWindowManager pWindowManager)
	{
		windowManager.set(pWindowManager);
	}
	
	/**
	 * Gets the property for the current {@link IFXWindowManager}.
	 * 
	 * @return the property for the current {@link IFXWindowManager}.
	 */
	public ObjectProperty<IFXWindowManager> windowManagerProperty()
	{
		return windowManager;
	}
	
	/**
	 * Gets the next {@link FXInternalWindow} based on the given index.
	 * 
	 * @param pCurrentIndex the current index.
	 * @return the next {@link FXInternalWindow}. {@code null} if there are no
	 *         {@link FXInternalWindow}s.
	 */
	private FXInternalWindow getNextWindow(int pCurrentIndex)
	{
		if (pCurrentIndex >= 0)
		{
			if (pCurrentIndex >= windows.size() - 1)
			{
				return windows.get(0);
			}
			else
			{
				return windows.get(pCurrentIndex + 1);
			}
		}
		else if (!windows.isEmpty())
		{
			return windows.get(0);
		}
		
		return null;
	}
	
	/**
	 * Gets the previous {@link FXInternalWindow} based on the given index.
	 * 
	 * @param pCurrentIndex the current index.
	 * @return the previous {@link FXInternalWindow}. {@code null} if there are
	 *         no {@link FXInternalWindow}s.
	 */
	private FXInternalWindow getPreviousWindow(int pCurrentIndex)
	{
		if (pCurrentIndex >= 0)
		{
			if (pCurrentIndex == 0)
			{
				return windows.get(windows.size() - 1);
			}
			else
			{
				return windows.get(pCurrentIndex - 1);
			}
		}
		else if (!windows.isEmpty())
		{
			return windows.get(windows.size() - 1);
		}
		
		return null;
	}
	
	/**
	 * Invoked if the background {@link Pane} property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onBackgroundPaneChanged(ObservableValue<? extends Pane> pObservable, Pane pOldValue, Pane pNewValue)
	{
		if (pOldValue != null)
		{
			getChildren().remove(pOldValue);
		}
		
		if (pNewValue != null)
		{
			getChildren().add(pNewValue);
		}
	}
	
	/**
	 * Invoked if a {@link FXInternalWindow} closes.
	 * 
	 * @param pWindowCloseEvent the event.
	 */
	private void onWindowClosed(WindowClosedEvent pWindowCloseEvent)
	{
		windows.remove(pWindowCloseEvent.getWindow());
		pWindowCloseEvent.consume();
	}
	
	/**
	 * INvoked if the list of {@link FXInternalWindow} changes.
	 * 
	 * @param pChange the change.
	 */
	private void onWindowListChanged(Change<? extends FXInternalWindow> pChange)
	{
		while (pChange.next())
		{
			for (FXInternalWindow window : pChange.getRemoved())
			{
				window.removeEventHandler(WindowClosedEvent.WINDOW_CLOSED, onWindowClosedListener);
				windowManager.get().removeWindow(window);
			}
			
			for (FXInternalWindow window : pChange.getAddedSubList())
			{
				window.addEventHandler(WindowClosedEvent.WINDOW_CLOSED, onWindowClosedListener);
				windowManager.get().addWindow(window);
			}
		}
		
		windowManager.get().updateActiveWindow();
	}
	
	/**
	 * Invoked if the window manager property changes.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onWindowManagerChanged(ObservableValue<? extends IFXWindowManager> pObservable, IFXWindowManager pOldValue, IFXWindowManager pNewValue)
	{
		getChildren().remove(windowManagerPane);
		
		if (pOldValue != null)
		{
			previousStates.put(pOldValue.getClass(), pOldValue.storeState());
			pOldValue.dispose();
		}
		
		windowManagerPane = pNewValue.getPane();
		
		getChildren().add(windowManagerPane);
		
		for (FXInternalWindow window : windows)
		{
			pNewValue.addWindow(window);
		}
		
		pNewValue.restoreState(previousStates.get(pNewValue.getClass()));
	}
	
}	// FXDesktopPane
