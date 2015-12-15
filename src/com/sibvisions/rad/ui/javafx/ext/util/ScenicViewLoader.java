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
package com.sibvisions.rad.ui.javafx.ext.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * The {@link ScenicViewLoader} allows to load ScenicView dynamically.
 * 
 * @author Robert Zenz
 */
public final class ScenicViewLoader
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance needed.
	 */
	private ScenicViewLoader()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Attaches ScenicView to the given {@link Scene}, and opens it if the given
	 * {@link KeyCombination} is pressed.
	 * <p>
	 * This is done by installing an {@link EventHandler} as filter on the
	 * {@link Scene}.
	 * 
	 * @param pScene the {@link Scene}.
	 * @param pKeyCombination the {@link KeyCombination}.
	 * @return {@code true} if it could be successfully attached. {@code false}
	 *         if either the given {@link Scene} or {@link KeyCombination} was
	 *         {@code null}, or ScenicView could not be loaded.
	 */
	public static boolean attach(Scene pScene, KeyCombination pKeyCombination)
	{
		if (pScene == null || pKeyCombination == null)
		{
			return false;
		}
		
		Class<?> scenicView = loadScenicView(pScene.getClass().getClassLoader());
		
		if (scenicView != null)
		{
			pScene.addEventFilter(KeyEvent.KEY_PRESSED, new ScenicViewKeyHandler(pScene, scenicView, pKeyCombination));
			return true;
		}
		
		return false;
	}
	
	/**
	 * Loads the ScenicView class.
	 * 
	 * @param pClassLoader the {@link ClassLoader} to use.
	 * @return the ScenicView class. {@code null} if it could not be loaded.
	 */
	private static Class<?> loadScenicView(ClassLoader pClassLoader)
	{
		if (pClassLoader == null)
		{
			return null;
		}
		
		try
		{
			return pClassLoader.loadClass("org.scenicview.ScenicView");
		}
		catch (ClassNotFoundException e)
		{
			// Ignore the exception.
		}
		
		return null;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link ScenicViewKeyHandler} is an {@link EventHandler}
	 * implementation that allows to show ScenicView.
	 * 
	 * @author Robert Zenz
	 */
	private static final class ScenicViewKeyHandler implements EventHandler<KeyEvent>
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link KeyCombination} that triggers ScenicView. */
		private KeyCombination keyCombination;
		
		/** The {@link Scene}. */
		private Scene scene;
		
		/** The {@link Class} of ScenicView. */
		private Class<?> scenicViewClass;
		
		/** The show-method. */
		private Method showMethod;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ScenicViewKeyHandler}.
		 *
		 * @param pScene the {@link Scene}.
		 * @param pScenicViewClass the {@link Class} of ScenicView.
		 * @param pKeyCombination the {@link KeyCombination} that triggers
		 *            ScenicView.
		 */
		public ScenicViewKeyHandler(Scene pScene, Class<?> pScenicViewClass, KeyCombination pKeyCombination)
		{
			scene = pScene;
			scenicViewClass = pScenicViewClass;
			keyCombination = pKeyCombination;
			
			try
			{
				showMethod = scenicViewClass.getDeclaredMethod("show", Scene.class);
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				// Should not happen.
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void handle(KeyEvent pEvent)
		{
			if (keyCombination.match(pEvent))
			{
				try
				{
					showMethod.invoke(null, scene);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					// Ignore any exception.
				}
			}
		}
		
	}	// ScenicViewKeyHandler
	
}	// ScenicViewLoader
