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
package com.sibvisions.rad.ui.javafx.ext.behavior;

import com.sibvisions.rad.ui.javafx.ext.FXTimePicker;
import com.sun.javafx.scene.control.behavior.BehaviorBase;

/**
 * The default behavior for the {@link FXTimePicker}.
 * 
 * @author Robert Zenz
 */
public class FXTimePickerBehavior extends BehaviorBase<FXTimePicker>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTimePickerBehavior}.
	 *
	 * @param pControl the control.
	 */
	public FXTimePickerBehavior(FXTimePicker pControl)
	{
		super(pControl, null);
	}
	
}	// FXTimePickerBehavior
