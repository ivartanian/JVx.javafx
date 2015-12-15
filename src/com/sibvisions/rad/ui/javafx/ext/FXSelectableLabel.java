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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * The {@link FXSelectableLabel} is a {@link Label} extension that allows to be
 * selected, similar to a radio button or check box.
 * 
 * @author Robert Zenz
 */
public class FXSelectableLabel extends Label
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The path to the default style sheet. */
	public static final String DEFAULT_STYLE = "/com/sibvisions/rad/ui/javafx/ext/css/fxselectablelabel.css";
	
	/** The default class name used for styling. */
	public static final String DEFAULT_STYLE_CLASS = "selectable-label";
	
	/** The {@link PseudoClass} used for the selected state. */
	private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
	
	/** The property used for the selected value. */
	private BooleanProperty selected;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXSelectableLabel}.
	 */
	public FXSelectableLabel()
	{
		this(null);
	}
	
	/**
	 * Creates a new instance of {@link FXSelectableLabel}.
	 *
	 * @param pText the text.
	 */
	public FXSelectableLabel(String pText)
	{
		this(pText, null);
	}
	
	/**
	 * Creates a new instance of {@link FXSelectableLabel}.
	 *
	 * @param pText the text.
	 * @param pGraphic the graphic.
	 */
	public FXSelectableLabel(String pText, Node pGraphic)
	{
		super(pText, pGraphic);
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		setFocusTraversable(true);
		
		selected = new SimpleBooleanProperty();
		selected.addListener(this::onSelectedChanged);
		
		addEventHandler(KeyEvent.KEY_TYPED, this::onKeyTyped);
		addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserAgentStylesheet()
	{
		return DEFAULT_STYLE;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets if this is selected.
	 * 
	 * @return {@code true} if this is selected.
	 */
	public boolean isSelected()
	{
		return selected.get();
	}
	
	/**
	 * Gets the property for the selected value.
	 * 
	 * @return the property for the selected value.
	 */
	public BooleanProperty selectedProperty()
	{
		return selected;
	}
	
	/**
	 * Sets if this is selected.
	 * 
	 * @param pSelected {@code true} to select this.
	 */
	public void setSelected(boolean pSelected)
	{
		selected.set(pSelected);
	}
	
	/**
	 * Invoked if a key is typed.
	 * <p>
	 * Triggers the selection if the key is a space.
	 * 
	 * @param pKeyEvent the event.
	 */
	private void onKeyTyped(KeyEvent pKeyEvent)
	{
		if (pKeyEvent.getCharacter().equals(" "))
		{
			selected.set(!selected.get());
		}
	}
	
	/**
	 * Invoked if a mouse is clicked.
	 * <p>
	 * Triggers the selection if the primary mouse button was clicked.
	 * 
	 * @param pMouseEvent the event.
	 */
	private void onMouseClicked(MouseEvent pMouseEvent)
	{
		if (pMouseEvent.getButton() == MouseButton.PRIMARY)
		{
			selected.set(!selected.get());
		}
		
		requestFocus();
	}
	
	/**
	 * Invoked of the value of the selected property changes.
	 * <p>
	 * Triggers the selected pseudoclass.
	 * 
	 * @param pObservable the observable value.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onSelectedChanged(ObservableValue<? extends Boolean> pObservable, Boolean pOldValue, Boolean pNewValue)
	{
		pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, pNewValue.booleanValue());
	}
	
}	// FXSelectableLabel
