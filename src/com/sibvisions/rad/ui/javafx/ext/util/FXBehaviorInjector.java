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

import java.lang.reflect.Field;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

/**
 * The {@link FXBehaviorInjector} is a helper utility that allows to inject a
 * {@link BehaviorBase} into a {@link BehaviorSkinBase} by using reflection.
 * <p>
 * There are several skin classes that do not allow to set a custom behavior, so
 * it needs to be injected by using Reflection. However there are efforts to
 * make this obsolete,
 * <a href="https://javafx-jira.kenai.com/browse/RT-21598">RT-21598</a>.
 * 
 * @author Robert Zenz
 */
public final class FXBehaviorInjector
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance needed.
	 */
	private FXBehaviorInjector()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Injects the given {@link BehaviorBase behavior} into the given
	 * {@link BehaviorSkinBase skin} by using reflection.
	 * 
	 * @param pSkin the {@link BehaviorSkinBase skin}.
	 * @param pBehavior the {@link BehaviorBase behavior} to inject.
	 */
	public static void inject(BehaviorSkinBase<?, ?> pSkin, BehaviorBase<?> pBehavior)
	{
		try
		{
			Field behaviorField = BehaviorSkinBase.class.getDeclaredField("behavior");
			behaviorField.setAccessible(true);
			
			// The old behavior needs to be properly disposed, otherwise
			// the new behavior will not work at all.
			BehaviorBase<?> oldBehavior = (BehaviorBase<?>)behaviorField.get(pSkin);
			oldBehavior.dispose();
			
			behaviorField.set(pSkin, pBehavior);
		}
		catch (Exception e)
		{
			// TODO Injection failed, should be inform the user/client?
			e.printStackTrace();
		}
	}
	
}	// FXBehaviorInjector
