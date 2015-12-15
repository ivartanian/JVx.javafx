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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Skin;

import com.sibvisions.rad.ui.javafx.ext.skin.FXCustomComboBoxSkin;
import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;

/**
 * The {@link FXCustomComboBox} is a {@link ComboBoxBase} extension that allows
 * to easily facilitate the possibility to have different popup controls.
 * <p>
 * The intermediate layer is the {@link IFXComboBoxPopupProvider}, that provides
 * the necessary methods for creating a custom popup control.
 * 
 * @author Robert Zenz
 * @param <T> the type of the value.
 */
public class FXCustomComboBox<T> extends ComboBoxBase<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The path to the default style sheet. */
	public static final String DEFAULT_STYLE = "/com/sibvisions/rad/ui/javafx/ext/css/fxcustomcombobox.css";
	
	/** The default class name used for styling. */
	public static final String DEFAULT_STYLE_CLASS = "custom-combo-box";
	
	/** The property for the {@link IFXComboBoxPopupProvider}. */
	protected ObjectProperty<IFXComboBoxPopupProvider<T>> popupProvider;
	
	/**
	 * The property used for if the popup should be closed when the enter key is
	 * pressed.
	 */
	private BooleanProperty closePopupOnEnterKey;
	
	/**
	 * The property used for if the popup should be closed when the escape key
	 * is pressed.
	 */
	private BooleanProperty closePopupOnEscapeKey;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXCustomComboBox}.
	 */
	public FXCustomComboBox()
	{
		super();
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		
		closePopupOnEscapeKey = new SimpleBooleanProperty(true);
		
		closePopupOnEnterKey = new SimpleBooleanProperty(true);
		
		popupProvider = new SimpleObjectProperty<>();
		popupProvider.addListener(this::onPopupProviderChanged);
		
		setEditable(true);
	}
	
	/**
	 * Creates a new instance of {@link FXCustomComboBox}.
	 *
	 * @param pPopupProvider the {@link IFXComboBoxPopupProvider}.
	 */
	public FXCustomComboBox(IFXComboBoxPopupProvider<T> pPopupProvider)
	{
		this();
		
		popupProvider.set(pPopupProvider);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Skin<?> createDefaultSkin()
	{
		return new FXCustomComboBoxSkin<>(this, new ComboBoxBaseBehavior<>(this, null));
	}
	
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
	 * Gets the property for if the popup should be closed when the enter key is
	 * pressed.
	 * 
	 * @return the property for if the popup should be closed when the return
	 *         key is pressed.
	 * @see #isClosePopupOnEnterKey()
	 * @see #setClosePopupOnEnterKey(boolean)
	 */
	public BooleanProperty closePopupOnEnterKeyProperty()
	{
		return closePopupOnEnterKey;
	}
	
	/**
	 * Gets the property for if the popup should be closed when the escape key
	 * is pressed.
	 * 
	 * @return the property for if the popup should be closed when the escape
	 *         key is pressed.
	 * @see #isClosePopupOnEscapeKey()
	 * @see #setClosePopupOnEscapeKey(boolean)
	 */
	public BooleanProperty closePopupOnEscapeKeyProperty()
	{
		return closePopupOnEscapeKey;
	}
	
	/**
	 * Gets the {@link IFXComboBoxPopupProvider}.
	 * 
	 * @return the {@link IFXComboBoxPopupProvider}.
	 */
	public IFXComboBoxPopupProvider<T> getPopupProvider()
	{
		return popupProvider.get();
	}
	
	/**
	 * Gets if the popup should be closed when the enter key is pressed.
	 * 
	 * @return {@code true} if the popup should be closed when the enter key is
	 *         pressed.
	 * @see #closePopupOnEnterKeyProperty()
	 * @see #setClosePopupOnEnterKey(boolean)
	 */
	public boolean isClosePopupOnEnterKey()
	{
		return closePopupOnEnterKey.get();
	}
	
	/**
	 * Gets if the popup should be closed when the escape key is pressed.
	 * 
	 * @return {@code true} if the popup should be closed when the escape key is
	 *         pressed.
	 * @see #closePopupOnEscapeKeyProperty()
	 * @see #setClosePopupOnEscapeKey(boolean)
	 */
	public boolean isClosePopupOnEscapeKey()
	{
		return closePopupOnEscapeKey.get();
	}
	
	/**
	 * Gets the property for the {@link IFXComboBoxPopupProvider}.
	 * 
	 * @return the property for the {@link IFXComboBoxPopupProvider}.
	 */
	public ObjectProperty<IFXComboBoxPopupProvider<T>> popupProviderProperty()
	{
		return popupProvider;
	}
	
	/**
	 * Sets if the popup should be closed when the enter key is pressed.
	 * 
	 * @param pClosePopupOnEnterKey {@code true} if the popup should be closed
	 *            when the enter key is pressed.
	 * @see #closePopupOnEnterKeyProperty()
	 * @see #isClosePopupOnEnterKey()
	 */
	public void setClosePopupOnEnterKey(boolean pClosePopupOnEnterKey)
	{
		closePopupOnEnterKey.set(pClosePopupOnEnterKey);
	}
	
	/**
	 * Sets if the popup should be closed when the escape key is pressed.
	 * 
	 * @param pClosePopupOnEscapeKey {@code true} if the popup should be closed
	 *            when the escape key is pressed.
	 * @see #closePopupOnEscapeKeyProperty()
	 * @see #isClosePopupOnEscapeKey()
	 */
	public void setClosePopupOnEscapeKey(boolean pClosePopupOnEscapeKey)
	{
		closePopupOnEscapeKey.set(pClosePopupOnEscapeKey);
	}
	
	/**
	 * Sets the given {@link IFXComboBoxPopupProvider}.
	 * 
	 * @param pPopupProvider the {@link IFXComboBoxPopupProvider}.
	 */
	public void setPopupProvider(IFXComboBoxPopupProvider<T> pPopupProvider)
	{
		popupProvider.set(pPopupProvider);
	}
	
	/**
	 * Invoked if the {@link IFXComboBoxPopupProvider} changed.
	 * <p>
	 * Calls the {@link IFXComboBoxPopupProvider#init(FXCustomComboBox)} method.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onPopupProviderChanged(ObservableValue<? extends IFXComboBoxPopupProvider<T>> pObservable, IFXComboBoxPopupProvider<T> pOldValue,
			IFXComboBoxPopupProvider<T> pNewValue)
	{
		if (pNewValue != null)
		{
			pNewValue.init(this);
		}
	}
	
}	// FXCustomComboBox
