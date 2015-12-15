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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * The {@link FXPositioningPane} is a {@link Pane} extension that allows to
 * define a starting position for the children that are added.
 * <p>
 * Usage is rather simple:
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	PositioningPane pane = new Pane();
 * 	pane.setPosition(Pos.BOTTOM_RIGHT);
 * 	
 * 	pane.getChildren().add(new Label("Will appear in the lower right corner."));
 * 	
 * 	// Or if you want to move an already added child.
 * 	pane.move(pane.getChildren().get(0), Pos.BOTTOM_LEFT);
 * }
 * </pre>
 * 
 * @author Robert Zenz
 */
public class FXPositioningPane extends Pane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The property if the children should only be autosized if they are
	 * positioned.
	 */
	private BooleanProperty autoSizeOnlyOnPositioning;
	
	/** The {@link Map} of newly added children that need to be positioned. */
	private Map<Node, Pos> childrenToPosition;
	
	/** The property if the listener is enabled. */
	private BooleanProperty listenerEnabled;
	
	/** The property of the horizontal position. */
	private ObjectProperty<Pos> position;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXPositioningPane}.
	 */
	public FXPositioningPane()
	{
		this(Pos.CENTER);
	}
	
	/**
	 * Creates a new instance of {@link FXPositioningPane}.
	 * 
	 * @param pPosition the position of the children.
	 */
	public FXPositioningPane(Pos pPosition)
	{
		super();
		
		autoSizeOnlyOnPositioning = new SimpleBooleanProperty(false);
		
		listenerEnabled = new SimpleBooleanProperty(true);
		
		position = new SimpleObjectProperty<>(pPosition);
		
		childrenToPosition = Collections.synchronizedMap(new HashMap<>());
		getChildren().addListener(this::onChildrenListChanged);
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
		if (!autoSizeOnlyOnPositioning.get())
		{
			autosizeChildren();
		}
		
		positionChildren();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property for if the children should only be autosized if they
	 * are positioned.
	 * 
	 * @return the property.
	 */
	public BooleanProperty autoSizeOnlyOnPositioningProperty()
	{
		return autoSizeOnlyOnPositioning;
	}
	
	/**
	 * Do not move the given {@link Node child} on the next layout pass. This
	 * method only has an effect if the given {@link Node child} is already
	 * scheduled to be moved.
	 * 
	 * @param pChild the {@link Node child}.
	 */
	public void doNotMove(Node pChild)
	{
		childrenToPosition.remove(pChild);
	}
	
	/**
	 * Gets the position.
	 *
	 * @return the position.
	 * 		
	 * @see #positionProperty()
	 * @see #setPosition(Pos)
	 */
	public Pos getPosition()
	{
		return position.get();
	}
	
	/**
	 * Gets if the children should only be autosized if they are positioned.
	 * 
	 * @return {@code true} if the children will only be autosized if they are
	 *         positioned.
	 */
	public boolean isAutoSizeOnlyOnPositioning()
	{
		return autoSizeOnlyOnPositioning.get();
	}
	
	/**
	 * Gets if the listener on the children list is enabled.
	 * 
	 * @return {@code true} if the listener is enabled.
	 */
	public boolean isListenerEnabled()
	{
		return listenerEnabled.get();
	}
	
	/**
	 * Gets the property for if the listener on the children list is enabled.
	 * <p>
	 * The listener in this case makes sure that added children are positioned.
	 * You can disable it if you want to change the children without having them
	 * being repositioned.
	 * 
	 * @return the property.
	 */
	public BooleanProperty listenerEnabled()
	{
		return listenerEnabled;
	}
	
	/**
	 * Moves the given child (which needs to be added) to the given {@link Pos
	 * position}.
	 * 
	 * @param pChild the child to move.
	 * @param pPos the new {@link Pos position}.
	 */
	public void move(Node pChild, Pos pPos)
	{
		if (getChildren().contains(pChild))
		{
			childrenToPosition.put(pChild, pPos);
			requestLayout();
		}
	}
	
	/**
	 * Gets the position property which defines where newly added children will
	 * be located.
	 *
	 * @return the object property for the position.
	 */
	public ObjectProperty<Pos> positionProperty()
	{
		return position;
	}
	
	/**
	 * Sets if children should only be autosized if they are positioned.
	 * 
	 * @param pAutoSizeOnlyOnPositioning {@code true} if children should only be
	 *            autosized when they are positioned. {@code false} if they
	 *            should always be autosized.
	 */
	public void setAutoSizeOnlyOnPositioning(boolean pAutoSizeOnlyOnPositioning)
	{
		autoSizeOnlyOnPositioning.set(pAutoSizeOnlyOnPositioning);
	}
	
	/**
	 * Sets if the listener on the list of children is enabled or not.
	 * 
	 * @param pEnabled {@code true} if it should be enabled.
	 */
	public void setListenerEnabled(boolean pEnabled)
	{
		listenerEnabled.set(pEnabled);
	}
	
	/**
	 * Sets the position.
	 *
	 * @param pPos the new position.
	 */
	public void setPosition(Pos pPos)
	{
		position.set(pPos);
	}
	
	/**
	 * Sends the given child to the front without changing its position.
	 * 
	 * @param pChild the child to bring to the front.
	 */
	public void toFront(Node pChild)
	{
		setListenerEnabled(false);
		if (getChildren().indexOf(pChild) != getChildren().size() - 1)
		{
			getChildren().remove(pChild);
			getChildren().add(pChild);
		}
		setListenerEnabled(true);
	}
	
	/**
	 * Autosizes all children.
	 */
	protected void autosizeChildren()
	{
		for (Node child : getManagedChildren())
		{
			child.autosize();
		}
	}
	
	/**
	 * Positions the given child at the given position.
	 * 
	 * @param pChild the child to position.
	 * @param pPosition the position.
	 */
	protected void positionChild(Node pChild, Pos pPosition)
	{
		if (autoSizeOnlyOnPositioning.get())
		{
			pChild.autosize();
		}
		
		layoutInArea(pChild, 0, 0, getWidth(), getHeight(), 0, Insets.EMPTY, false, false, pPosition.getHpos(), pPosition.getVpos());
	}
	
	/**
	 * Positions all children that need to be positioned.
	 */
	protected void positionChildren()
	{
		for (Node child : getManagedChildren())
		{
			if (child.isManaged() && childrenToPosition.containsKey(child))
			{
				Pos childPosition = childrenToPosition.get(child);
				
				positionChild(child, childPosition);
				
				childrenToPosition.remove(child);
			}
		}
	}
	
	/**
	 * Is called whenever the list of children changes.
	 * 
	 * @param pChange the change.
	 */
	private void onChildrenListChanged(Change<? extends Node> pChange)
	{
		if (listenerEnabled.get())
		{
			while (pChange.next())
			{
				if (pChange.wasAdded())
				{
					for (Node child : pChange.getAddedSubList())
					{
						childrenToPosition.put(child, position.get());
					}
				}
				
				if (pChange.wasRemoved())
				{
					for (Node child : pChange.getAddedSubList())
					{
						childrenToPosition.remove(child);
					}
				}
			}
		}
	}
	
}	// FXPositioningPane
