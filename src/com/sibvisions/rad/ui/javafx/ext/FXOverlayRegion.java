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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * The {@link FXOverlayRegion} allows to overlay a {@link Pane} over another.
 * The {@link #overlayPaneProperty() overlay pane} is always pushed in front of
 * the {@link #mainPaneProperty() main pane} whenever it has children, if it has
 * no children it is automatically pushed to the background.
 * 
 * @author Robert Zenz
 */
public class FXOverlayRegion extends Region
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property for the main {@link Pane}. */
	private ObjectProperty<Pane> mainPane;
	
	/** The property for the overlay {@link Pane}. */
	private ObjectProperty<Pane> overlayPane;
	
	/** The listener for when the children of the overlay pane change. */
	private ListChangeListener<Node> overlayChildrenChangedListener;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXOverlayRegion}.
	 */
	public FXOverlayRegion()
	{
		super();
		
		overlayChildrenChangedListener = this::onOverlayChildrenChanged;
		
		mainPane = new SimpleObjectProperty<>();
		mainPane.addListener(this::onMainPaneChanged);
		
		overlayPane = new SimpleObjectProperty<>();
		overlayPane.addListener(this::onOverlayPaneChanged);
	}
	
	/**
	 * Creates a new instance of {@link FXOverlayRegion}.
	 *
	 * @param pMainPane the main pane.
	 * @param pOverlayPane the overlay pane.
	 */
	public FXOverlayRegion(Pane pMainPane, Pane pOverlayPane)
	{
		this();
		
		mainPane.set(pMainPane);
		overlayPane.set(pOverlayPane);
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
		if (mainPane.get() != null && mainPane.get().isManaged())
		{
			mainPane.get().resizeRelocate(0, 0, getWidth(), getHeight());
		}
		
		if (overlayPane.get() != null && overlayPane.get().isManaged())
		{
			overlayPane.get().resizeRelocate(0, 0, getWidth(), getHeight());
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the main {@link Pane}.
	 * 
	 * @return the main {@link Pane}.
	 */
	public Pane getMainPane()
	{
		return mainPane.get();
	}
	
	/**
	 * Gets the overlay {@link Pane}.
	 * 
	 * @return the overlay {@link Pane}.
	 */
	public Pane getOverlayPane()
	{
		return overlayPane.get();
	}
	
	/**
	 * Gets the property of the main {@link Pane}.
	 * 
	 * @return the property of main {@link Pane}.
	 */
	public ObjectProperty<Pane> mainPaneProperty()
	{
		return mainPane;
	}
	
	/**
	 * Gets the property of the overlay {@link Pane}.
	 * 
	 * @return the property of overlay {@link Pane}.
	 */
	public ObjectProperty<Pane> overlayPaneProperty()
	{
		return overlayPane;
	}
	
	/**
	 * Sets the main {@link Pane}.
	 * 
	 * @param pMainPane the main {@link Pane}.
	 */
	public void setMainPane(Pane pMainPane)
	{
		mainPane.set(pMainPane);
	}
	
	/**
	 * Sets the overlay {@link Pane}.
	 * 
	 * @param pOverlayPane the overlay {@link Pane}.
	 */
	public void setOverlayPane(Pane pOverlayPane)
	{
		overlayPane.set(pOverlayPane);
	}
	
	/**
	 * Updates the order of the main and overlay {@link Pane}.
	 */
	protected void updatePaneOrder()
	{
		if (overlayPane.get() != null)
		{
			if (overlayPane.get().getChildren().isEmpty())
			{
				overlayPane.get().toBack();
			}
			else
			{
				overlayPane.get().toFront();
			}
		}
	}
	
	/**
	 * Invoked if the children of the {@link #overlayPaneProperty() overlay
	 * pane} change.
	 * <p>
	 * Updates the order of the panes.
	 * 
	 * @param pChange the change.s
	 */
	private void onOverlayChildrenChanged(Change<? extends Node> pChange)
	{
		updatePaneOrder();
	}
	
	/**
	 * Invoked if the {@link #mainPaneProperty() main pane} changes.
	 * 
	 * @param pObservable the observableValue.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onMainPaneChanged(ObservableValue<? extends Pane> pObservable, Pane pOldValue, Pane pNewValue)
	{
		if (pOldValue != null)
		{
			getChildren().remove(pOldValue);
		}
		
		if (pNewValue != null)
		{
			getChildren().add(pNewValue);
		}
		
		updatePaneOrder();
	}
	
	/**
	 * Invoked if the {@link #overlayPaneProperty() overlay pane} changes.
	 * 
	 * @param pObservable the observableValue.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onOverlayPaneChanged(ObservableValue<? extends Pane> pObservable, Pane pOldValue, Pane pNewValue)
	{
		if (pOldValue != null)
		{
			pOldValue.getChildren().removeListener(overlayChildrenChangedListener);
			getChildren().remove(pOldValue);
		}
		
		if (pNewValue != null)
		{
			getChildren().add(pNewValue);
			pNewValue.getChildren().addListener(overlayChildrenChangedListener);
		}
		
		updatePaneOrder();
	}
	
}	// FXOverlayRegion
