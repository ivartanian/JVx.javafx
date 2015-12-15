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

import javafx.scene.control.DatePicker;

/**
 * The {@link FXDatePickerRT40362} is an extension of the {@link DatePicker}
 * that provides a {@link #commit()} method, for updating the
 * {@link #valueProperty()}.
 * <p>
 * See <a href="https://javafx-jira.kenai.com/browse/RT-40362">RT-40362</a> for
 * details.
 * 
 * @author Robert Zenz
 */
public class FXDatePickerRT40362 extends DatePicker
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXDatePickerRT40362}.
	 */
	public FXDatePickerRT40362()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Commits any pending changes from the editor to the
	 * {@link #valueProperty()}.
	 */
	public void commit()
	{
		setValue(getConverter().fromString(getEditor().getText()));
	}
	
}	// FXDatePickerRT40362
