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

import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.TableCell;

/**
 * The {@link FXRowFocusingCell} sets the pseudo class {@code focused-row} on
 * its parent row whenever it receives the focus.
 * 
 * @author Robert Zenz
 * @param <S> The type of the TableView generic type (i.e.
 *            {@code S == TableView<S>}). This should also match with the first
 *            generic type in TableColumn.
 * @param <T> The type of the item contained within the Cell.
 */
public class FXRowFocusingCell<S, T> extends TableCell<S, T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link PseudoClass} used to mark the parent row. */
	private static final PseudoClass FOCUSED_ROW_CLASS = PseudoClass.getPseudoClass("focused-row");
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXRowFocusingCell}.
	 */
	public FXRowFocusingCell()
	{
		focusedProperty().addListener(this::onFocusChange);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Checks if the same row was focused by walking through all {@link Node}s
	 * in the {@link #getParent()} and checking if one of them is focused.
	 * 
	 * @return {@code true} if there is another focused {@link Node sibling}.
	 */
	private boolean isSameRowFocused()
	{
		for (Node node : getParent().getChildrenUnmodifiable())
		{
			if (node != this && node.isFocused())
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Invoked of the focus of the cell changes.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onFocusChange(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		if (getParent() != null)
		{
			if (pNewValue.booleanValue() || !isSameRowFocused())
			{
				getParent().pseudoClassStateChanged(FOCUSED_ROW_CLASS, pNewValue.booleanValue());
			}
		}
		
	}
	
}	// FXRowFocusingCell
