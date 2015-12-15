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
package com.sibvisions.rad.ui.javafx.impl.animation;

import java.util.HashMap;
import java.util.Map;

import javax.rad.ui.IComponent;

import javafx.geometry.Orientation;
import javafx.scene.Node;

import com.sibvisions.rad.ui.javafx.ext.animation.FXAnimator;

/**
 * The {@link FXAnimator} is a helper class that allows to easily animate
 * {@link IComponent}s.
 * 
 * @author Robert Zenz
 */
public final class JavaFXAnimator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Not needed.
	 */
	private JavaFXAnimator()
	{
		// No instance needed.
	}
	
	/**
	 * Flips the given {@link IComponent} in the given direction until it is
	 * turned roughly 90 degrees, read it is not visible anymore. Then the first
	 * {@link Runnable} {@code pOnTippingPoint} will be executed which allows to
	 * swap the content or properties of the {@link IComponent} without the user
	 * seeing it. After that the rotation will continue back into its starting
	 * position, where {@code pOnFinished} will be called.
	 * <p>
	 * If there is a {@link javafx.scene.PerspectiveCamera} is set on the
	 * {@link javafx.scene.Scene}, this function will already choose the correct
	 * angle needed for the tipping point so that the user will not see the swap
	 * of the content.
	 * 
	 * @param pComponent the {@link IComponent} to rotate.
	 * @param pOrientation the {@link Orientation} in which to rotate. Note that
	 *            {@link Orientation#HORIZONTAL} means from right to left.
	 * @param pRightLeftTopDown {@code true} if the animation should be from
	 *            right to left/top to down. {@code false} if it should be left
	 *            to right/bottom to up.
	 * @param pOnTippingPoint the {@link Runnable} to execute when the
	 *            {@link IComponent} is exactly edge on with the viewer. Can be
	 *            {@code null}.
	 * @param pOnFinished the {@link Runnable} to execute when the rotation has
	 *            finished. Can be {@code null}.
	 * @see FXAnimator#flip(Node, Orientation, boolean, Runnable, Runnable)
	 */
	public static final void flip(IComponent pComponent, Orientation pOrientation, boolean pRightLeftTopDown, Runnable pOnTippingPoint, Runnable pOnFinished)
	{
		if (pComponent != null && pComponent.getResource() instanceof Node)
		{
			Node node = (Node)pComponent.getResource();
			
			Map<Node, NodeInfo> infos = new HashMap<>();
			
			gatherInfos(node, infos);
			
			FXAnimator.flip(node, pOrientation, pRightLeftTopDown, pOnTippingPoint, () ->
			{
				putInfos(node, infos);
				
				if (pOnFinished != null)
				{
					pOnFinished.run();
				}
			});
		}
	}
	
	/**
	 * Flips the {@link IComponent} downwards.
	 * 
	 * @param pComponent the {@link IComponent} to flip.
	 * @param pOnFlip the action to execute halfway during the flip, when the
	 *            {@link IComponent} is edge on with the screen.
	 * @see #flip(IComponent, Orientation, boolean, Runnable, Runnable)
	 */
	public static final void flipDown(IComponent pComponent, Runnable pOnFlip)
	{
		flip(pComponent, Orientation.VERTICAL, true, pOnFlip, null);
	}
	
	/**
	 * Flips the {@link IComponent} left.
	 * 
	 * @param pComponent the {@link IComponent} to flip.
	 * @param pOnFlip the action to execute halfway during the flip, when the
	 *            {@link IComponent} is edge on with the screen.
	 * @see #flip(IComponent, Orientation, boolean, Runnable, Runnable)
	 */
	public static final void flipLeft(IComponent pComponent, Runnable pOnFlip)
	{
		flip(pComponent, Orientation.HORIZONTAL, true, pOnFlip, null);
	}
	
	/**
	 * Flips the {@link IComponent} right.
	 * 
	 * @param pComponent the {@link IComponent} to flip.
	 * @param pOnFlip the action to execute halfway during the flip, when the
	 *            {@link IComponent} is edge on with the screen.
	 * @see #flip(IComponent, Orientation, boolean, Runnable, Runnable)
	 */
	public static final void flipRight(IComponent pComponent, Runnable pOnFlip)
	{
		flip(pComponent, Orientation.HORIZONTAL, false, pOnFlip, null);
	}
	
	/**
	 * Flips the {@link IComponent} upwards.
	 * 
	 * @param pComponent the {@link IComponent} to flip.
	 * @param pOnFlip the action to execute halfway during the flip, when the
	 *            {@link IComponent} is edge on with the screen.
	 * @see #flip(IComponent, Orientation, boolean, Runnable, Runnable)
	 */
	public static final void flipUp(IComponent pComponent, Runnable pOnFlip)
	{
		flip(pComponent, Orientation.VERTICAL, false, pOnFlip, null);
	}
	
	/**
	 * A simple shake animation.
	 * 
	 * @param pComponent the {@link IComponent} to shake.
	 * @param pOrientation the {@link Orientation} of the movement.
	 * @param pStrength the strength (read distance) of the movement.
	 * @param pOnFinished the {@link Runnable} to execute when the rotation has
	 *            finished. Can be {@code null}.
	 */
	public static void shake(IComponent pComponent, Orientation pOrientation, double pStrength, Runnable pOnFinished)
	{
		if (pComponent != null && pComponent.getResource() instanceof Node)
		{
			Node node = (Node)pComponent.getResource();
			
			FXAnimator.shake(node, pOrientation, pStrength, pOnFinished);
		}
	}
	
	/**
	 * Shakes the given component from left to right.
	 * 
	 * @param pComponent the {@link IComponent} to shake.
	 * @param pStrength the strength of the shake, read the distance to the
	 *            left/right.
	 */
	public static void shakeLeftRight(IComponent pComponent, double pStrength)
	{
		shake(pComponent, Orientation.HORIZONTAL, pStrength, null);
	}
	
	/**
	 * Shakes the given component from up to down.
	 * 
	 * @param pComponent the {@link IComponent} to shake.
	 * @param pStrength the strength of the shake, read the distance to the
	 *            up/down.
	 */
	public static void shakeUpDown(IComponent pComponent, double pStrength)
	{
		shake(pComponent, Orientation.VERTICAL, pStrength, null);
	}
	
	/**
	 * Gathers information from the given {@link Node} and all its parents and
	 * puts them into the given {@link Map}.
	 * 
	 * @param pNode the {@link Node}.
	 * @param pInfos the {@link Map}.
	 */
	private static void gatherInfos(Node pNode, Map<Node, NodeInfo> pInfos)
	{
		if (pNode != null)
		{
			pInfos.put(pNode, new NodeInfo(pNode));
			
			pNode.setCache(false);
			pNode.setClip(null);
			
			gatherInfos(pNode.getParent(), pInfos);
		}
	}
	
	/**
	 * Puts information from the given {@link Node} and all its parents from the
	 * given {@link Map}.
	 * 
	 * @param pNode the {@link Node}.
	 * @param pInfos the {@link Map}.
	 */
	private static void putInfos(Node pNode, Map<Node, NodeInfo> pInfos)
	{
		if (pNode != null)
		{
			NodeInfo info = pInfos.get(pNode);
			
			if (info != null)
			{
				pNode.setCache(info.isCached());
				pNode.setClip(info.getClip());
			}
			
			putInfos(pNode.getParent(), pInfos);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple container for some information.
	 * 
	 * @author Robert Zenz
	 */
	private static final class NodeInfo
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** If it is cached. */
		private boolean cached;
		
		/** The used clip. */
		private Node clip;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link NodeInfo}.
		 *
		 * @param pNode the {@link Node}.
		 */
		public NodeInfo(Node pNode)
		{
			cached = pNode.isCache();
			clip = pNode.getClip();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * If it is cached.
		 * 
		 * @return {@code true} if it is cached.
		 */
		public boolean isCached()
		{
			return cached;
		}
		
		/**
		 * Gets the clip.
		 *
		 * @return the clip.
		 */
		public Node getClip()
		{
			return clip;
		}
		
	}	// NodeInfo
	
}	// JavaFXAnimator
