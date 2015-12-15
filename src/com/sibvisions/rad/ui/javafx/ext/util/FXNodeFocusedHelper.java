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
package com.sibvisions.rad.ui.javafx.ext.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;

/**
 * The {@link FXNodeFocusedHelper} is a helper class that allows to monitor
 * whether the given {@link #nodeProperty() node} is focused or one of its
 * children.
 * 
 * @author Robert Zenz
 * @see NodeUtil#isSameOrChild(Node, Node)
 */
public class FXNodeFocusedHelper
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property used to hold if the {@link Node} is focused or not. */
	private BooleanProperty focused;
	
	/**
	 * The read-only property used to hold if the {@link Node} is focused or
	 * not. This property is client facing.
	 */
	private ReadOnlyBooleanProperty focusedReadOnly;
	
	/** The {@link Node} that is watched. */
	private ObjectProperty<Node> node;
	
	/**
	 * The {@link ChangeListener} that listens for changes in the
	 * {@link Node#sceneProperty} of the {@link Node}.
	 */
	private ChangeListener<Scene> nodeSceneListener;
	
	/**
	 * The {@link ChangeListener} that listens for changes in the
	 * {@link Scene#focusOwnerProperty}.
	 */
	private ChangeListener<Node> sceneFocusOwnerListener;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXNodeFocusedHelper}.
	 */
	public FXNodeFocusedHelper()
	{
		sceneFocusOwnerListener = this::onSceneFocusOwnerChanged;
		nodeSceneListener = this::onNodeSceneChanged;
		
		focused = new SimpleBooleanProperty();
		focusedReadOnly = ReadOnlyBooleanProperty.readOnlyBooleanProperty(focused);
		
		node = new SimpleObjectProperty<>();
		node.addListener(this::onNodeChanged);
	}
	
	/**
	 * Creates a new instance of {@link FXNodeFocusedHelper}.
	 *
	 * @param pNode the {@link Node}.
	 */
	public FXNodeFocusedHelper(Node pNode)
	{
		this();
		
		node.set(pNode);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The property for if the current {@link Node} is focused or not.
	 * 
	 * @return the property for if the current {@link Node} is focused or not.
	 * @see #isFocused()
	 */
	public ReadOnlyBooleanProperty focusedProperty()
	{
		return focusedReadOnly;
	}
	
	/**
	 * Gets the monitored {@link Node}.
	 * 
	 * @return the {@link Node}.
	 */
	public Node getNode()
	{
		return node.get();
	}
	
	/**
	 * Gets if the current {@link Node} or one of its children is focused.
	 * 
	 * @return {@code true} if the current {@link Node} or one of its children
	 *         is focused. {@code false} if not, or if the current {@link Node}
	 *         is {@code null}.
	 */
	public boolean isFocused()
	{
		return focused.get();
	}
	
	/**
	 * Gets the property for the current {@link Node}.
	 * 
	 * @return the property for the current {@link Node}.
	 */
	public ObjectProperty<Node> nodeProperty()
	{
		return node;
	}
	
	/**
	 * Sets the monitored {@link Node}.
	 * 
	 * @param pNode the monitored {@link Node}. Can be {@code null}.
	 */
	public void setNode(Node pNode)
	{
		node.set(pNode);
	}
	
	/**
	 * Sets the property for if the current {@link Node} is focused or not.
	 * 
	 * @param pFocused {@code true} if it is focused.
	 */
	protected void setFocused(boolean pFocused)
	{
		focused.set(pFocused);
	}
	
	/**
	 * Attaches the necessary listeners to the given {@link Node}.
	 * 
	 * @param pNode the {@link Node}. Can be {@code null}.
	 */
	private void attachTo(Node pNode)
	{
		if (pNode != null)
		{
			attachTo(pNode.getScene());
			pNode.sceneProperty().removeListener(nodeSceneListener);
			pNode.sceneProperty().addListener(nodeSceneListener);
		}
	}
	
	/**
	 * Attaches the necessary listeners to the given {@link Scene}.
	 * 
	 * @param pScene the {@link Scene}. Can be {@code null}.
	 */
	private void attachTo(Scene pScene)
	{
		if (pScene != null)
		{
			pScene.focusOwnerProperty().removeListener(sceneFocusOwnerListener);
			pScene.focusOwnerProperty().addListener(sceneFocusOwnerListener);
		}
	}
	
	/**
	 * Removes the necessary listeners from the given {@link Node}.
	 * 
	 * @param pNode the {@link Node}. Can be {@code null}.
	 */
	private void detachFrom(Node pNode)
	{
		if (pNode != null)
		{
			pNode.sceneProperty().removeListener(nodeSceneListener);
			detachFrom(pNode.getScene());
		}
	}
	
	/**
	 * Removes the necessary listeners from the given {@link Scene}.
	 * 
	 * @param pScene the {@link Scene}. Can be {@code null}.
	 */
	private void detachFrom(Scene pScene)
	{
		if (pScene != null)
		{
			pScene.focusOwnerProperty().removeListener(sceneFocusOwnerListener);
		}
	}
	
	/**
	 * Invoked if the {@link #node node property} changes.
	 * <p>
	 * Updates all listeners and the {@link #focused focused property}.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onNodeChanged(ObservableValue<? extends Node> pObservable, Node pOldValue, Node pNewValue)
	{
		detachFrom(pOldValue);
		attachTo(pNewValue);
		
		updateFocusedProperty();
	}
	
	/**
	 * Invoked if the {@link Node#sceneProperty()} changes.
	 * <p>
	 * Updates all listeners and the {@link #focused focused property}.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onNodeSceneChanged(ObservableValue<? extends Scene> pObservable, Scene pOldValue, Scene pNewValue)
	{
		detachFrom(pOldValue);
		attachTo(pNewValue);
		
		updateFocusedProperty();
	}
	
	/**
	 * Invoked if the {@link Scene#focusOwnerProperty()} changes.
	 * <p>
	 * Updates the {@link #focused focused property}.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSceneFocusOwnerChanged(ObservableValue<? extends Node> pObservable, Node pOldValue, Node pNewValue)
	{
		setFocused(NodeUtil.isSameOrChild(node.get(), pNewValue));
	}
	
	/**
	 * Updates the {@link #focused focused property} based on the current state.
	 */
	private void updateFocusedProperty()
	{
		if (node.get() != null)
		{
			if (node.get().isFocused())
			{
				setFocused(true);
			}
			else
			{
				Scene scene = node.get().getScene();
				
				if (scene != null)
				{
					setFocused(NodeUtil.isSameOrChild(node.get(), scene.getFocusOwner()));
				}
				else
				{
					setFocused(false);
				}
			}
		}
		else
		{
			setFocused(false);
		}
	}
	
}	// FXNodeFocusedHelper
