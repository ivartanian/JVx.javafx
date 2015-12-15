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
package com.sibvisions.rad.ui.javafx.ext.skin;

import javafx.scene.control.Spinner;

import com.sibvisions.rad.ui.javafx.ext.behavior.FXSpinnerBehaviorRT40623;
import com.sibvisions.rad.ui.javafx.ext.util.FXBehaviorInjector;
import com.sun.javafx.scene.control.behavior.SpinnerBehavior;
import com.sun.javafx.scene.control.skin.SpinnerSkin;

/**
 * The {@link FXSpinnerSkinRT40623} is a {@link SpinnerSkin} extensions that
 * allows to set a custom behavior.
 * 
 * @author Robert Zenz
 * @param <T> the type of the items.
 */
public class FXSpinnerSkinRT40623<T> extends SpinnerSkin<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXSpinnerSkinRT40623}.
	 *
	 * @param pSpinner the spinner.
	 */
	public FXSpinnerSkinRT40623(Spinner<T> pSpinner)
	{
		this(pSpinner, new FXSpinnerBehaviorRT40623<>(pSpinner));
	}
	
	/**
	 * Creates a new instance of {@link FXSpinnerSkinRT40623}.
	 *
	 * @param pSpinner the spinner.
	 * @param pBehavior the behavior.
	 */
	public FXSpinnerSkinRT40623(Spinner<T> pSpinner, SpinnerBehavior<T> pBehavior)
	{
		super(pSpinner);
		
		FXBehaviorInjector.inject(this, pBehavior);
	}
	
}	// FXSpinnerSkinRT40623
