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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * The {@link FXSceneLocker} is a static helper utility that allows to to lock a
 * {@link Scene}, meaning that only events that belong to the locking
 * {@link Node} or its children are processed.
 * <p>
 * If a {@link Scene} is locked by multiple {@link Node}s, that last one does
 * receive events.
 * 
 * @author Robert Zenz
 */
public final class FXSceneLocker
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Object} used as marker for the lock root. */
	private static final Object LOCK_ROOT = new Object();
	
	/** The {@link LockManager} that is used. */
	private static LockManager lockManager = new LockManager();
	
	/** The {@link HardLockManager} that is used. */
	private static HardLockManager hardLockManager = new HardLockManager();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance needed.
	 */
	private FXSceneLocker()
	{
		// Not needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a lock based on the given {@link Node}.
	 * 
	 * @param pNode the locking {@link Node}.
	 */
	public static void addLock(Node pNode)
	{
		if (pNode == null)
		{
			return;
		}
		
		lockManager.addLockingNode(pNode);
	}
	
	/**
	 * Adds a lock to the given {@link Scene}.
	 * 
	 * @param pScene the {@link Scene}.
	 * @param pNode the locking {@link Node}.
	 */
	public static void addLock(Scene pScene, Node pNode)
	{
		if (pNode == null)
		{
			return;
		}
		
		if (pScene == null)
		{
			return;
		}
		
		lockManager.addLockingNode(pScene, pNode);
	}
	
	/**
	 * Adds a hard lock for the given {@link Node}.
	 * <p>
	 * A hard lock in this case means that the next lock root and all its
	 * children will not be able to receive any mouse or key events.
	 * 
	 * @param pNode the locking {@link Node}.
	 */
	public static void addHardLock(Node pNode)
	{
		if (pNode == null)
		{
			return;
		}
		
		Node lockRoot = getLockRoot(pNode);
		if (lockRoot == null)
		{
			lockRoot = pNode.getScene().getRoot();
		}
		
		hardLockManager.addLock(pNode, lockRoot);
	}
	
	/**
	 * Checks if the given {@link Node} is lock root.
	 * <p>
	 * The lock root is the boundary for the lock, the lock does only affect the
	 * {@link Node}s between the locking {@link Node} and the lock root.
	 * 
	 * @param pNode the {@link Node} to check.
	 * @return {@code true} if the given {@link Node} is the lock root.
	 */
	public static boolean isLockRoot(Node pNode)
	{
		return pNode.getProperties().get(FXSceneLocker.class) == LOCK_ROOT;
	}
	
	/**
	 * Makes the given {@link Node} a lock root.
	 * <p>
	 * The lock root is the boundary for the lock, the lock does only affect the
	 * {@link Node}s between the locking {@link Node} and the lock root.
	 * 
	 * @param pNode the {@link Node}.
	 */
	public static void makeLockRoot(Node pNode)
	{
		pNode.getProperties().put(FXSceneLocker.class, LOCK_ROOT);
	}
	
	/**
	 * Removes a lock based on the given {@link Node}.
	 * 
	 * @param pNode the locking {@link Node}.
	 */
	public static void removeLock(Node pNode)
	{
		if (pNode == null)
		{
			return;
		}
		
		lockManager.removeLockingNode(pNode);
	}
	
	/**
	 * Removes a lock from the given {@link Scene}.
	 * 
	 * @param pScene the {@link Scene}.
	 * @param pNode the locking {@link Node}.
	 */
	public static void removeLock(Scene pScene, Node pNode)
	{
		if (pNode == null)
		{
			return;
		}
		
		if (pScene == null)
		{
			return;
		}
		
		lockManager.removeLockingNode(pScene, pNode);
	}
	
	/**
	 * Removes the lock root from the given {@link Node}.
	 * <p>
	 * The lock root is the boundary for the lock, the lock does only affect the
	 * {@link Node}s between the locking {@link Node} and the lock root.
	 * 
	 * @param pNode the {@link Node}.
	 */
	public static void removeLockRoot(Node pNode)
	{
		pNode.getProperties().remove(FXSceneLocker.class);
	}
	
	/**
	 * Removes the hard lock for the given {@link Node}.
	 * 
	 * @param pNode the locking {@link Node}.
	 */
	public static void removeHardLock(Node pNode)
	{
		if (pNode == null)
		{
			return;
		}
		
		hardLockManager.removeLock(pNode);
	}
	
	/**
	 * Searches for a lock root upwards from the given {@link Node}.
	 * 
	 * @param pNode the {@link Node} where to start.
	 * @return the lock root. {@code null} if there is none.
	 */
	private static Node getLockRoot(Node pNode)
	{
		if (pNode != null)
		{
			if (isLockRoot(pNode))
			{
				return pNode;
			}
			
			if (pNode.getParent() != null)
			{
				return getLockRoot(pNode.getParent());
			}
		}
		
		return null;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link HardLockManager} manages all aspect of hard locks.
	 * 
	 * @author Robert Zenz
	 */
	private static final class HardLockManager
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The internal state. */
		private WeakHashMap<Node, WeakReference<Node>> state;
		
		/** The mouse filter that is used. */
		private EventHandler<MouseEvent> mouseFilter;
		
		/** The key filter that is used. */
		private EventHandler<KeyEvent> keyFilter;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link HardLockManager}.
		 */
		public HardLockManager()
		{
			keyFilter = this::keyFilter;
			mouseFilter = this::mouseFilter;
			
			state = new WeakHashMap<>(1);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Adds a hard lock to the given lock root {@link Node}.
		 * 
		 * @param pLockingNode the locking {@link Node}. This is used as
		 *            reference and the lock can only be lifted with this
		 *            {@link Node}.
		 * @param pLockRoot the lock root {@link Node}. Meaning this
		 *            {@link Node} and all its children will be locked.
		 */
		public void addLock(Node pLockingNode, Node pLockRoot)
		{
			state.put(pLockingNode, new WeakReference<>(pLockRoot));
			
			pLockRoot.addEventFilter(KeyEvent.ANY, keyFilter);
			pLockRoot.addEventFilter(MouseEvent.ANY, mouseFilter);
		}
		
		/**
		 * Removes the lock for the given locking {@link Node}.
		 * 
		 * @param pLockingNode the locking {@link Node}.
		 */
		public void removeLock(Node pLockingNode)
		{
			WeakReference<Node> lockRootReference = state.get(pLockingNode);
			
			if (pLockingNode != null)
			{
				Node lockRoot = lockRootReference.get();
				
				if (lockRoot != null)
				{
					lockRoot.removeEventFilter(KeyEvent.ANY, keyFilter);
					lockRoot.removeEventFilter(MouseEvent.ANY, mouseFilter);
				}
			}
		}
		
		/**
		 * Invoked a mouse event occurred. Consumes all events.
		 * 
		 * @param pMouseEvent the event.
		 */
		private void mouseFilter(MouseEvent pMouseEvent)
		{
			pMouseEvent.consume();
		}
		
		/**
		 * Invoked a key event occurred. Consumes all events.
		 * 
		 * @param pKeyEvent the event.
		 */
		private void keyFilter(KeyEvent pKeyEvent)
		{
			pKeyEvent.consume();
		}
		
	}	// HardLockManager
	
	/**
	 * The {@link LockManager} manages a {@link Scene}-{@link Node} lock
	 * relationship, by having {@link WeakReference}s to both.
	 * 
	 * @author Robert Zenz
	 */
	private static final class LockManager
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The filter for filtering mouse events. */
		private EventHandler<MouseEvent> mouseFilter;
		
		/** The {@link Map} that maps {@link Node}s to {@link Scene}s. */
		private WeakHashMap<Node, WeakReference<Scene>> nodeToScene;
		
		/** The state of all locks. */
		private WeakHashMap<Scene, List<WeakReference<Node>>> state;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link LockManager}.
		 */
		public LockManager()
		{
			mouseFilter = this::mouseFilter;
			
			nodeToScene = new WeakHashMap<>(1);
			
			state = new WeakHashMap<>(1);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Adds the given {@link Node} as locking {@link Node} for its
		 * {@link Scene}.
		 * 
		 * @param pNode the locking {@link Node}.
		 */
		public void addLockingNode(Node pNode)
		{
			if (pNode.getScene() != null)
			{
				addLockingNode(pNode.getScene(), pNode);
			}
		}
		
		/**
		 * Adds the given {@link Node} as locking {@link Node} for the given
		 * {@link Scene}.
		 * 
		 * @param pScene the {@link Scene} to lock.
		 * @param pNode the locking {@link Node}.
		 */
		public void addLockingNode(Scene pScene, Node pNode)
		{
			nodeToScene.put(pNode, new WeakReference<>(pScene));
			
			List<WeakReference<Node>> lockingNodes = state.get(pScene);
			
			if (lockingNodes == null)
			{
				lockingNodes = new ArrayList<>(1);
				lockingNodes.add(new WeakReference<>(pNode));
				state.put(pScene, lockingNodes);
				
				pScene.addEventFilter(MouseEvent.ANY, mouseFilter);
			}
			else
			{
				lockingNodes.add(new WeakReference<>(pNode));
				
				if (lockingNodes.size() == 1)
				{
					pScene.addEventFilter(MouseEvent.ANY, mouseFilter);
				}
			}
		}
		
		/**
		 * Removes the given locking {@link Node}.
		 * 
		 * @param pNode the locking {@link Node}.
		 */
		public void removeLockingNode(Node pNode)
		{
			WeakReference<Scene> sceneReference = nodeToScene.get(pNode);
			
			if (sceneReference != null)
			{
				Scene scene = sceneReference.get();
				
				if (scene != null)
				{
					removeLock(scene, pNode);
				}
			}
		}
		
		/**
		 * Removes the given locking {@link Node}.
		 * 
		 * @param pScene the {@link Scene}.
		 * @param pNode the locking {@link Node}.
		 */
		public void removeLockingNode(Scene pScene, Node pNode)
		{
			List<WeakReference<Node>> lockingNodes = state.get(pScene);
			
			if (lockingNodes != null && !lockingNodes.isEmpty())
			{
				for (int index = 0; index < lockingNodes.size(); index++)
				{
					WeakReference<Node> weakNode = lockingNodes.get(index);
					
					if (weakNode != null)
					{
						if (pNode == weakNode.get())
						{
							lockingNodes.remove(index);
							
							if (lockingNodes.isEmpty())
							{
								pScene.removeEventFilter(MouseEvent.ANY, mouseFilter);
							}
						}
					}
				}
			}
			else
			{
				pScene.removeEventFilter(MouseEvent.ANY, mouseFilter);
			}
			
			nodeToScene.remove(pNode);
		}
		
		/**
		 * Gets the locking {@link Node} for the given {@link Scene}.
		 * 
		 * @param pScene the {@link Scene}.
		 * @return the locking {@link Node}. {@code null} if there is none.
		 */
		private Node getLockingNode(Scene pScene)
		{
			List<WeakReference<Node>> lockingNodes = state.get(pScene);
			
			if (lockingNodes != null && !lockingNodes.isEmpty())
			{
				for (int index = lockingNodes.size() - 1; index >= 0; index--)
				{
					WeakReference<Node> weakNode = lockingNodes.get(index);
					
					if (weakNode != null)
					{
						Node node = weakNode.get();
						
						if (node != null)
						{
							return node;
						}
					}
					
					lockingNodes.remove(index);
				}
			}
			
			return null;
		}
		
		/**
		 * Filters mouse events.
		 * 
		 * @param pMouseEvent the event.
		 */
		private void mouseFilter(MouseEvent pMouseEvent)
		{
			Object source = pMouseEvent.getSource();
			
			if (source instanceof Scene)
			{
				Scene scene = (Scene)source;
				
				if (scene != null)
				{
					Node lockingNode = lockManager.getLockingNode(scene);
					
					if (lockingNode == null)
					{
						scene.removeEventFilter(MouseEvent.ANY, mouseFilter);
						return;
					}
					
					EventTarget target = pMouseEvent.getTarget();
					
					if (target instanceof Node)
					{
						Node node = (Node)target;
						
						// Get the lock root of the parent.
						// In case that the locking node is a lock root, we'll
						// ignore it and walk the hierarchy upwards to find
						// one higher up. Locking a lock root doesn't make any
						// sense, because it would do nothing.
						Node lockRoot = getLockRoot(lockingNode.getParent());
						
						if (!NodeUtil.isSameOrChild(lockingNode, node) && (lockRoot == null || NodeUtil.isSameOrChild(lockRoot, node)))
						{
							pMouseEvent.consume();
						}
					}
					else
					{
						Bounds bounds = lockingNode.localToScene(lockingNode.getLayoutBounds());
						
						if (!bounds.contains(pMouseEvent.getSceneX(), pMouseEvent.getSceneY()))
						{
							pMouseEvent.consume();
						}
					}
				}
			}
		}
		
	}	// LockManager
	
}	// FXSceneLocker
