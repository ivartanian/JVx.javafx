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
package com.sibvisions.rad.ui.javafx.impl;

import java.lang.ref.WeakReference;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

import com.sibvisions.rad.ui.javafx.impl.component.JavaFXAbstractComponentBase;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;

/**
 * The {@link JavaFXUtil} proviodes various helper methods.
 * 
 * @author Robert Zenz
 */
public final class JavaFXUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link WeakReference} to the {@link Scene}. */
	private static WeakReference<Scene> wrefScene;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance needed.
	 */
	private JavaFXUtil()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets an application global cursor.
	 * 
	 * @param pComponent the component.
	 * @param pCursor the cursor.
	 */
	public static void setGlobalCursor(JavaFXAbstractComponentBase<?> pComponent, Cursor pCursor)
	{
		Scene scene = null;
		
		if (wrefScene != null)
		{
			scene = wrefScene.get();
		}
		
		if (scene == null)
		{
			Object oComponent = pComponent.getResource();
			
			if (oComponent instanceof Node)
			{
				scene = ((Node)oComponent).getScene();
			}
			else if (pComponent instanceof JavaFXLauncher)
			{
				scene = ((JavaFXLauncher)pComponent).getScene();
			}
			else
			{
				JavaFXAbstractComponentBase<?> comp = pComponent;
				
				while (comp != null && !(comp instanceof JavaFXComponent))
				{
					comp = (JavaFXAbstractComponentBase<?>)comp.getParent();
				}
				
				if (comp != null)
				{
					scene = ((Region)comp.getResource()).getScene();
				}
			}
		}
		
		if (pCursor == null)
		{
			scene.setCursor(Cursor.DEFAULT);
			
			wrefScene = null;
		}
		else
		{
			scene.setCursor(pCursor);
			
			if (wrefScene == null || scene != wrefScene.get())
			{
				wrefScene = new WeakReference<>(scene);
			}
		}
	}
	
}	// JavaFXUtil
