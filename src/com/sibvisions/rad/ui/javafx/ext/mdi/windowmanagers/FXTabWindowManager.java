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
package com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import org.eclipse.fx.ui.controls.tabpane.DndTabPane;
import org.eclipse.fx.ui.controls.tabpane.DndTabPaneFactory;
import org.eclipse.fx.ui.controls.tabpane.DndTabPaneFactory.FeedbackType;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow.State;
import com.sibvisions.rad.ui.javafx.ext.mdi.IFXWindowManager;
import com.sibvisions.rad.ui.javafx.ext.mdi.WindowModalChangedEvent;
import com.sibvisions.rad.ui.javafx.ext.skin.FXTabPaneSkinRT40150;

/**
 * The {@link FXTabWindowManager} is an {@link IFXWindowManager} implementation
 * that uses {@link Tab}s to represent single {@link FXInternalWindow}s.
 * 
 * @author Robert Zenz
 */
public class FXTabWindowManager implements IFXWindowManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link StackPane} that contains the {@link DndTabPane}. */
	private StackPane containerPane;
	
	/**
	 * The {@link EventHandler} for when the modal state of a
	 * {@link FXInternalWindow} changes.
	 */
	private EventHandler<WindowModalChangedEvent> windowModalChangedHandler;
	
	/** The {@link DndTabPane}. */
	private DndTabPane tabPane;
	
	/** A {@link Map} that maps {@link FXInternalWindow}s to {@link Tab}s. */
	private Map<FXInternalWindow, Tab> windowsToTabs;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTabWindowManager}.
	 */
	public FXTabWindowManager()
	{
		windowModalChangedHandler = this::onWindowModalChanged;
		
		tabPane = new DndTabPane();
		containerPane = new StackPane(tabPane);
		FXTabPaneSkinRT40150 skin = new FXTabPaneSkinRT40150(tabPane);
		DndTabPaneFactory.setup(FeedbackType.MARKER, containerPane, skin);
		tabPane.setSkin(skin);
		
		tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		
		windowsToTabs = new HashMap<>();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addWindow(FXInternalWindow pWindow)
	{
		Tab tab = windowToTab(pWindow);
		windowsToTabs.put(pWindow, tab);
		tabPane.getTabs().add(tab);
		
		tabPane.getSelectionModel().select(tab);
		
		updateModalTabs();
		
		pWindow.addEventHandler(WindowModalChangedEvent.WINDOW_MODAL_CHANGED, windowModalChangedHandler);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		for (Tab tab : tabPane.getTabs())
		{
			FXInternalWindow window = getWindow(tab);
			
			window.setBorderless(false);
			window.setDecorated(true);
		}
		
		tabPane.getTabs().clear();
		windowsToTabs.clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pane getPane()
	{
		return containerPane;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeWindow(FXInternalWindow pWindow)
	{
		tabPane.getTabs().remove(windowsToTabs.remove(pWindow));
		
		updateModalTabs();
		
		pWindow.removeEventHandler(WindowModalChangedEvent.WINDOW_MODAL_CHANGED, windowModalChangedHandler);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState(Object pState)
	{
		// NOOP
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object storeState()
	{
		// NOOP
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateActiveWindow()
	{
		for (Tab tab : tabPane.getTabs())
		{
			getWindow(tab).setActive(tab.isSelected());
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the last modal {@link Tab}.
	 * 
	 * @return the last modal {@link Tab}.
	 */
	private Tab getLastModalTab()
	{
		List<Tab> tabs = tabPane.getTabs();
		
		for (int index = tabs.size() - 1; index >= 0; index--)
		{
			Tab tab = tabs.get(index);
			FXInternalWindow window = getWindow(tab);
			
			if (window.isModal())
			{
				return tab;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the {@link FXInternalWindow} from the given {@link Tab}.
	 * 
	 * @param pTab the {@link Tab} which contains the {@link FXInternalWindow}.
	 * @return the {@link FXInternalWindow}.
	 */
	private FXInternalWindow getWindow(Tab pTab)
	{
		return (FXInternalWindow) ((Pane) pTab.getContent()).getChildren().get(0);
	}
	
	/**
	 * Updates all tabs taking into account if there is a modal tab or not.
	 */
	private void updateModalTabs()
	{
		Tab modalTab = getLastModalTab();
		
		if (modalTab == null)
		{
			for (Tab tab : tabPane.getTabs())
			{
				tab.setDisable(false);
			}
			
			Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
			
			if ((selectedTab == null || selectedTab.getTabPane() == null) && !tabPane.getTabs().isEmpty())
			{
				tabPane.getSelectionModel().select(tabPane.getTabs().get(tabPane.getTabs().size() - 1));
			}
		}
		else
		{
			for (Tab tab : tabPane.getTabs())
			{
				tab.setDisable(tab != modalTab);
			}
			
			tabPane.getSelectionModel().select(modalTab);
		}
	}
	
	/**
	 * Gets invoked if a {@link Tab} is to be closed.
	 * 
	 * @param pEvent the event.
	 */
	private void onTabCloseRequest(Event pEvent)
	{
		// Consume the event. If the window is actually closed, it will be
		// removed via the removeWindow function.
		pEvent.consume();
		
		Tab tab = (Tab) pEvent.getSource();
		FXInternalWindow window = getWindow(tab);
		
		window.close();
	}
	
	/**
	 * Gets invoked if a {@link FXInternalWindow} changes its modal state.
	 * 
	 * @param pWindowModalChangedEvent the event.
	 */
	private void onWindowModalChanged(WindowModalChangedEvent pWindowModalChangedEvent)
	{
		updateModalTabs();
	}
	
	/**
	 * Creates a {@link Tab} that contains the given {@link FXInternalWindow}.
	 * 
	 * @param pWindow the {@link FXInternalWindow} that is used as base.
	 * @return the {@link Tab}.
	 */
	private Tab windowToTab(FXInternalWindow pWindow)
	{
		pWindow.setState(State.NORMAL);
		
		pWindow.setBorderless(true);
		pWindow.setDecorated(false);
		pWindow.zoomReset();
		
		Tab tab = new Tab(pWindow.getTitle());
		tab.setContent(new StackPane(pWindow));
		tab.setOnCloseRequest(this::onTabCloseRequest);
		tab.textProperty().bind(pWindow.titleProperty());
		tab.closableProperty().bind(pWindow.closeableProperty());
		
		FXImageRegion tabIcon = new FXImageRegion();
		tabIcon.imageProperty().bind(pWindow.iconProperty());
		tab.setGraphic(tabIcon);
		
		return tab;
	}
	
}	// FXTabWindowManager
