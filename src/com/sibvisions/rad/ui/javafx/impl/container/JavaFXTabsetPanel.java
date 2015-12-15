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
package com.sibvisions.rad.ui.javafx.impl.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.IImage;
import javax.rad.ui.ILayout;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.event.TabsetHandler;
import javax.rad.ui.event.UITabsetEvent;
import javax.rad.ui.event.type.tabset.ITabActivatedListener;
import javax.rad.ui.event.type.tabset.ITabClosedListener;
import javax.rad.ui.event.type.tabset.ITabDeactivatedListener;
import javax.rad.ui.event.type.tabset.ITabMovedListener;

import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import org.eclipse.fx.ui.controls.tabpane.DndTabPane;
import org.eclipse.fx.ui.controls.tabpane.DndTabPaneFactory;
import org.eclipse.fx.ui.controls.tabpane.DndTabPaneFactory.FeedbackType;
import org.eclipse.fx.ui.controls.tabpane.TabDraggedEvent;

import com.sibvisions.rad.ui.javafx.ext.FXImageRegion;
import com.sibvisions.rad.ui.javafx.ext.behavior.FXTabPaneBehaviorRT40149;
import com.sibvisions.rad.ui.javafx.ext.skin.FXTabPaneSkinRT40150;

/**
 * The {@link JavaFXTabsetPanel} is the JavaFX specific implementation of
 * {@link ITabsetPanel}.
 * 
 * @author Robert Zenz
 */
public class JavaFXTabsetPanel extends JavaFXAbstractContainer<Pane> implements ITabsetPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The key that is used for storing the {@link TabInfo} in
	 * {@link Node#getProperties() properties} of the tab.
	 */
	private static final String TAB_INFO_KEY = "javafx.tab.info";
	
	/** The {@link TabsetHandler} for the event when a tab is activated. */
	private TabsetHandler<ITabActivatedListener> eventTabActivated;
	
	/** The {@link TabsetHandler} for the event when a tab is closed. */
	private TabsetHandler<ITabClosedListener> eventTabClosed;
	
	/** The {@link TabsetHandler} for the event when a tab is deactivated. */
	private TabsetHandler<ITabDeactivatedListener> eventTabDeactivated;
	
	/** The {@link TabsetHandler} for the event when a tab is moved. */
	private TabsetHandler<ITabMovedListener> eventTabMoved;
	
	/** Whether the next selection event should be ignored. */
	private boolean ignoreNextSelectionChanged;
	
	/** The actual {@link TabPane}. */
	private TabPane tabPane;
	
	/**
	 * The position where the tabs should be placed.
	 * 
	 * @see ITabsetPanel#PLACEMENT_BOTTOM
	 * @see ITabsetPanel#PLACEMENT_LEFT
	 * @see ITabsetPanel#PLACEMENT_RIGHT
	 * @see ITabsetPanel#PLACEMENT_TOP
	 */
	private int tabPlacement;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXTabsetPanel}.
	 */
	public JavaFXTabsetPanel()
	{
		this(new DndTabPane());
	}
	
	/**
	 * Creates a new instance of {@link JavaFXTabsetPanel} with a pre-defined
	 * {@link TabPane}.
	 * 
	 * @param pTabPane the {@link TabPane}
	 */
	protected JavaFXTabsetPanel(TabPane pTabPane)
	{
		// The DndTabPane requires that it is wrapped in a Pane,
		// that is needed for the animation/drag-marker to work.
		// Wrapping a "normal" TabPane in a StackPane does nothing, neither
		// good nor bad, so we simply wrap all out of simplicity reasons.
		super(new StackPane());
		
		tabPane = pTabPane;
		
		if (tabPane instanceof DndTabPane)
		{
			((DndTabPane)tabPane).setOnTabDragged(this::onTabDragged);
			
			FXTabPaneSkinRT40150 skin = new FXTabPaneSkinRT40150(tabPane);
			DndTabPaneFactory.setup(FeedbackType.MARKER, resource, skin);
			tabPane.setSkin(skin);
		}
		
		resource.getChildren().add(tabPane);
		
		tabPlacement = PLACEMENT_TOP;
		
		tabPane.getSelectionModel().selectedIndexProperty().addListener(this::onSelectionChanged);
		
		tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TabsetHandler<ITabActivatedListener> eventTabActivated()
	{
		if (eventTabActivated == null)
		{
			eventTabActivated = new TabsetHandler<>(ITabActivatedListener.class);
		}
		
		return eventTabActivated;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TabsetHandler<ITabClosedListener> eventTabClosed()
	{
		if (eventTabClosed == null)
		{
			eventTabClosed = new TabsetHandler<>(ITabClosedListener.class);
		}
		
		return eventTabClosed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TabsetHandler<ITabDeactivatedListener> eventTabDeactivated()
	{
		if (eventTabDeactivated == null)
		{
			eventTabDeactivated = new TabsetHandler<>(ITabDeactivatedListener.class);
		}
		
		return eventTabDeactivated;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TabsetHandler<ITabMovedListener> eventTabMoved()
	{
		if (eventTabMoved == null)
		{
			eventTabMoved = new TabsetHandler<>(ITabMovedListener.class);
		}
		
		return eventTabMoved;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getIconAt(int pIndex)
	{
		return getTabInfo(pIndex).getImage();
	}
	
	/**
	 * Always returns {@code null}, is not used by this container.
	 * 
	 * @return {@code null}
	 */
	@Override
	public ILayout<?> getLayout()
	{
		// Returns null on purpose, is not used.
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSelectedIndex()
	{
		return tabPane.getSelectionModel().getSelectedIndex();
	}
	
	/**
	 * Setting a different layout policy than
	 * {@link ITabsetPanel#TAB_LAYOUT_SCROLL} is not possible on JavaFX.
	 * <p>
	 * <a href="https://javafx-jira.kenai.com/browse/RT-40148">RT-40148</a>
	 * 
	 * @return always {@link ITabsetPanel#TAB_LAYOUT_SCROLL}.
	 */
	@Override
	public int getTabLayoutPolicy()
	{
		// TODO This method is a stub, because there is no other mode than SCROLL. RT-40148
		return TAB_LAYOUT_SCROLL;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabPlacement()
	{
		return tabPlacement;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTextAt(int pIndex)
	{
		return getTab(pIndex).getText();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosableAt(int pTabPosition)
	{
		return getTab(pTabPosition).isClosable();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDragable()
	{
		if (tabPane instanceof DndTabPane)
		{
			return ((DndTabPane)tabPane).isDraggingEnabled();
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabledAt(int pTabPosition)
	{
		return !getTab(pTabPosition).isDisabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNavigationKeysEnabled()
	{
		return ((FXTabPaneBehaviorRT40149)((FXTabPaneSkinRT40150)tabPane.getSkin()).getBehavior()).isPageUpDownEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClosableAt(int pTabPosition, boolean pClosable)
	{
		getTab(pTabPosition).setClosable(pClosable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDragable(boolean pDragable)
	{
		if (tabPane instanceof DndTabPane)
		{
			((DndTabPane)tabPane).setDraggingEnabled(pDragable);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabledAt(int pTabPosition, boolean pEnabled)
	{
		getTab(pTabPosition).setDisable(!pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIconAt(int pIndex, IImage pImage)
	{
		getTabInfo(pIndex).setImage(pImage);
		getTab(pIndex).setGraphic(new FXImageRegion((Image)pImage.getResource()));
	}
	
	/**
	 * Does nothing, is not used by this container.
	 * 
	 * @param pLayout not used.
	 */
	@Override
	public void setLayout(@SuppressWarnings("rawtypes") ILayout pLayout)
	{
		// Does nothing on purpose, is not used.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNavigationKeysEnabled(boolean pEnabled)
	{
		((FXTabPaneBehaviorRT40149)((FXTabPaneSkinRT40150)tabPane.getSkin()).getBehavior()).setPageUpDownEnable(pEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedIndex(int pIndex)
	{
		tabPane.getSelectionModel().select(pIndex);
	}
	
	/**
	 * Setting a different layout policy than
	 * {@link ITabsetPanel#TAB_LAYOUT_SCROLL} is not possible on JavaFX.
	 * <p>
	 * <a href="https://javafx-jira.kenai.com/browse/RT-40148">RT-40148</a>
	 * 
	 * @param pLayoutPolicy ignored.
	 */
	@Override
	public void setTabLayoutPolicy(int pLayoutPolicy)
	{
		// TODO This method is a stub, because there is no other mode than SCROLL. RT-40148
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabPlacement(int pPlacement)
	{
		tabPlacement = pPlacement;
		
		switch (tabPlacement)
		{
			case PLACEMENT_BOTTOM:
				tabPane.setSide(Side.BOTTOM);
				break;
				
			case PLACEMENT_LEFT:
				tabPane.setSide(Side.LEFT);
				break;
				
			case PLACEMENT_RIGHT:
				tabPane.setSide(Side.RIGHT);
				break;
				
			case PLACEMENT_TOP:
			default:
				tabPane.setSide(Side.TOP);
				
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTextAt(int pIndex, String pText)
	{
		getTab(pIndex).setText(pText);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addInternal(IComponent pComponent, Object pConstraints, int pIndex)
	{
		Tab tab = createTab((String)pConstraints, pComponent);
		
		tabPane.getTabs().add(tab);
		
		tab.getProperties().put(TAB_INFO_KEY, new FXTabInfo(tabPane.getTabs().indexOf(tab)));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isReverseAddOrderNeeded()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeAllInternal()
	{
		tabPane.getTabs().clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeInternal(IComponent pComponent)
	{
		tabPane.getTabs().remove(pComponent.getResource());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * A simple helper method that allows to fire the given
	 * {@link TabsetHandler} with the given values.
	 * 
	 * @param pHandler the {@link TabsetHandler} to fire.
	 * @param pId the type of the event.
	 * @param pOldIndex the old index.
	 * @param pNewIndex the new index.
	 */
	protected void fireEvent(TabsetHandler<?> pHandler, int pId, int pOldIndex, int pNewIndex)
	{
		if (pHandler != null)
		{
			UITabsetEvent event = new UITabsetEvent(
					eventSource,
					pId,
					System.currentTimeMillis(),
					0,
					pOldIndex,
					pNewIndex);
					
			pHandler.dispatchEvent(event);
		}
	}
	
	/**
	 * Fires the tab activated event.
	 * 
	 * @param pOldIndex the old index.
	 * @param pNewIndex the new index.
	 */
	protected void fireTabActivated(int pOldIndex, int pNewIndex)
	{
		fireEvent(eventTabActivated, UITabsetEvent.TABSET_ACTIVATED, pOldIndex, pNewIndex);
	}
	
	/**
	 * Fires the tab closed event.
	 * 
	 * @param pOldIndex the old index.
	 * @param pNewIndex the new index.
	 */
	protected void fireTabClosed(int pOldIndex, int pNewIndex)
	{
		fireEvent(eventTabClosed, UITabsetEvent.TABSET_CLOSED, pOldIndex, pNewIndex);
	}
	
	/**
	 * Fires the tab deactivated event.
	 * 
	 * @param pOldIndex the old index.
	 * @param pNewIndex the new index.
	 */
	protected void fireTabDeactivated(int pOldIndex, int pNewIndex)
	{
		fireEvent(eventTabDeactivated, UITabsetEvent.TABSET_DEACTIVATED, pOldIndex, pNewIndex);
	}
	
	/**
	 * Fires the tab moved event.
	 * 
	 * @param pOldIndex the old index.
	 * @param pNewIndex the new index.
	 */
	protected void fireTabMoved(int pOldIndex, int pNewIndex)
	{
		fireEvent(eventTabMoved, UITabsetEvent.TABSET_MOVED, pOldIndex, pNewIndex);
	}
	
	/**
	 * Creates a new {@link Tab}, ready setup for usage inside this control.
	 * 
	 * @param pText the test of the {@link Tab}.
	 * @param pComponent the {@link IComponent} which represents the tab.
	 * @return a new {@link Tab}.
	 */
	private Tab createTab(String pText, IComponent pComponent)
	{
		Tab tab = new Tab(pText);
		tab.setContent((Node)pComponent.getResource());
		tab.setClosable(false);
		tab.setOnCloseRequest(this::onTabCloseRequest);
		tab.setOnClosed(this::onTabClosed);
		
		return tab;
	}
	
	/**
	 * A simple helper method that returns the {@link Tab} at the given index.
	 * 
	 * @param pIndex the index.
	 * @return the {@link Tab}.
	 */
	private Tab getTab(int pIndex)
	{
		return tabPane.getTabs().get(pIndex);
	}
	
	/**
	 * A simple helper method that returns the {@link TabInfo} at the given
	 * index.
	 * 
	 * @param pIndex the index.
	 * @return the {@link TabInfo}.
	 */
	private FXTabInfo getTabInfo(int pIndex)
	{
		return getTabInfo(tabPane.getTabs().get(pIndex));
	}
	
	/**
	 * A simple helper method that returns the {@link TabInfo} of the given
	 * {@link Tab}.
	 * 
	 * @param pTab the {@link Tab}.
	 * @return the {@link TabInfo}.
	 */
	private FXTabInfo getTabInfo(Tab pTab)
	{
		return (FXTabInfo)pTab.getProperties().get(TAB_INFO_KEY);
	}
	
	/**
	 * Called if the selected tab changes.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old selected index.
	 * @param pNewValue the new selected index.
	 */
	private void onSelectionChanged(ObservableValue<? extends Number> pObservable, Number pOldValue, Number pNewValue)
	{
		if (!ignoreNextSelectionChanged)
		{
			fireTabDeactivated(pOldValue.intValue(), pOldValue.intValue());
			fireTabActivated(pOldValue.intValue(), pNewValue.intValue());
		}
		
		ignoreNextSelectionChanged = false;
	}
	
	/**
	 * Called if a {@link Tab} is closed.
	 * 
	 * @param pEvent the {@link Event} that occurred.
	 */
	private void onTabClosed(Event pEvent)
	{
		int index = getTabInfo((Tab)pEvent.getSource()).getIndex();
		
		fireTabClosed(index, index);
	}
	
	/**
	 * Called if a {@link Tab} is requested to be closed.
	 * 
	 * @param pEvent the {@link Event} that occurred.
	 */
	private void onTabCloseRequest(Event pEvent)
	{
		Tab tab = (Tab)pEvent.getSource();
		
		if (tab.isDisabled())
		{
			// We do not allow to close disabled tabs.
			pEvent.consume();
		}
	}
	
	/**
	 * Called if a {@link Tab} is dragged.
	 * 
	 * @param pEvent the {@link TabDraggedEvent} that occurred.
	 */
	private void onTabDragged(TabDraggedEvent pEvent)
	{
		getTabInfo((Tab)pEvent.getSource()).setIndex(pEvent.getToIndex());
		
		fireTabMoved(pEvent.getFromIndex(), pEvent.getToIndex());
		
		ignoreNextSelectionChanged = true;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple helper method that allows to store information about a
	 * {@link Tab}.
	 * 
	 * @author Robert Zenz
	 */
	protected static final class FXTabInfo
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link IImage} of the {@link Tab}. */
		private IImage image;
		
		/** The index of the {@link Tab}. */
		private int index;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link FXTabInfo}.
		 *
		 * @param pIndex the index.
		 */
		public FXTabInfo(int pIndex)
		{
			index = pIndex;
		}
		
		/**
		 * Gets the {@link IImage} of the {@link Tab}.
		 *
		 * @return the {@link IImage}.
		 */
		public IImage getImage()
		{
			return image;
		}
		
		/**
		 * Gets the index of the {@link Tab}.
		 *
		 * @return the index.
		 */
		public int getIndex()
		{
			return index;
		}
		
		/**
		 * Sets the {@link IImage} of the {@link Tab}.
		 *
		 * @param pImage the new {@link IImage}.
		 */
		public void setImage(IImage pImage)
		{
			image = pImage;
		}
		
		/**
		 * Sets the index of the {@link Tab}.
		 *
		 * @param pIndex the new index.
		 */
		public void setIndex(int pIndex)
		{
			index = pIndex;
		}
		
	}
	
}	// JavaFXTabsetPanel
