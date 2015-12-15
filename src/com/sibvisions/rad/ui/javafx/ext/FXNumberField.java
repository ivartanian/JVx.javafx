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

import java.text.ParseException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import com.sibvisions.util.type.NumberUtil;

/**
 * A {@link TextField} extension that only works for numbers.
 * 
 * @author Robert Zenz
 * @see NumberUtil
 */
public class FXNumberField extends TextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link NumberUtil} used for formatting the text. */
	private NumberUtil numberUtil;
	
	/** The format of the text representation. */
	private StringProperty format;
	
	/** The number. */
	private ObjectProperty<Number> number;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXNumberField}.
	 */
	public FXNumberField()
	{
		numberUtil = new NumberUtil();
		
		setAlignment(Pos.TOP_RIGHT);
		
		format = new SimpleStringProperty(numberUtil.getNumberPattern());
		format.addListener(this::onFormatChanged);
		
		number = new SimpleObjectProperty<>();
		number.addListener(this::onNumberChanged);
		
		addEventFilter(KeyEvent.KEY_TYPED, this::onKeyTyped);
		textProperty().addListener(this::onTextChanged);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property for the format string.
	 * 
	 * @return the property for the format string.
	 */
	public StringProperty formatProperty()
	{
		return format;
	}
	
	/**
	 * Gets the format string.
	 * 
	 * @return the format string.
	 */
	public String getFormat()
	{
		return format.get();
	}
	
	/**
	 * Gets the current number.
	 * 
	 * @return the current number. {@code null} if the text is empty.
	 */
	public Number getNumber()
	{
		return number.get();
	}
	
	/**
	 * Gets the property for the current number.
	 * 
	 * @return the property for the current number.
	 */
	public ObjectProperty<Number> numberProperty()
	{
		return number;
	}
	
	/**
	 * Sets the format string.
	 * 
	 * @param pFormat the format string.
	 */
	public void setFormat(String pFormat)
	{
		format.set(pFormat);
	}
	
	/**
	 * Sets the current number.
	 * 
	 * @param pNumber the current number.
	 */
	public void setNumber(Number pNumber)
	{
		number.set(pNumber);
	}
	
	/**
	 * Invoked if a key is typed and does prevent key events from happening if
	 * they would malform the number.
	 * <p>
	 * Even though there is an additional safe-guard at the text property, we
	 * should try to reduce such events.
	 * 
	 * @param pKeyEvent the key event.
	 */
	private void onKeyTyped(KeyEvent pKeyEvent)
	{
		String text = pKeyEvent.getCharacter();
		
		if (getCaretPosition() > 0)
		{
			text = getText(0, getCaretPosition()) + text;
		}
		if (getCaretPosition() < getText().length())
		{
			text = text + getText(getCaretPosition(), getText().length());
		}
		
		try
		{
			numberUtil.parse(text);
		}
		catch (ParseException e)
		{
			// Ignore the exception and consume the event, because that is not
			// a valid number according to our format.
			pKeyEvent.consume();
		}
	}
	
	/**
	 * Invoked if the format string changes and forwards the new value to the
	 * {@link NumberUtil}.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onFormatChanged(ObservableValue<? extends String> pObservable, String pOldValue, String pNewValue)
	{
		numberUtil.setNumberPattern(pNewValue);
	}
	
	/**
	 * Invoked if the number changes and does update the text..
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onNumberChanged(ObservableValue<? extends Number> pObservable, Number pOldValue, Number pNewValue)
	{
		if (pNewValue == null)
		{
			setText("");
		}
		else
		{
			setText(numberUtil.format(pNewValue));
		}
	}
	
	/**
	 * Invoked if the text changes and does update the text. If the new text is
	 * not well formed it will be reverted to the old value.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onTextChanged(ObservableValue<? extends String> pObservable, String pOldValue, String pNewValue)
	{
		try
		{
			setNumber(numberUtil.parse(pNewValue));
		}
		catch (ParseException e)
		{
			setText(pOldValue);
		}
	}
	
}	// FXNumberField
