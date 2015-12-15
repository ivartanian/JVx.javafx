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

import javafx.scene.Node;

/**
 * The {@link IFXComboBoxPopupProvider} provides the interface for plugging
 * custom popups into the {@link FXCustomComboBox}.
 * <p>
 * It also directly takes the role of a converted for the value.
 * 
 * @param <T> the type of the value.
 * @author Robert Zenz
 */
public interface IFXComboBoxPopupProvider<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Converts the given string to a value.
	 * 
	 * @param pString the string to convert.
	 * @return the value.
	 */
	public T fromString(String pString);
	
	/**
	 * Invoked if the popup should be hidden.
	 * <p>
	 * Implementations can run cleanup code in this method and can savely assume
	 * that the popup is no longer visible to the user. This method might be
	 * called multiple times, without {@link #showPopup()} being called in
	 * between.
	 */
	public void hidePopup();
	
	/**
	 * Initializes this provider.
	 * 
	 * @param pParentComboBox the parent {@link FXCustomComboBox}.
	 */
	public void init(FXCustomComboBox<T> pParentComboBox);
	
	/**
	 * Selects the next value.
	 */
	public void selectNext();
	
	/**
	 * Selects the previous value.
	 */
	public void selectPrevious();
	
	/**
	 * Invoked if the popup should be shown.
	 * <p>
	 * This method is only invoked once, when the popup is open.
	 *
	 * @return the {@link Node} that should be used as popup.
	 */
	public Node showPopup();
	
	/**
	 * Converts the given value to a string (for display).
	 * 
	 * @param pValue the value to convert.
	 * @return the string representation of the value.
	 */
	public String toString(T pValue);
	
}	// IFXComboBoxPopupProvider
