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
package com.sibvisions.rad.ui.javafx.ext.control.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.rad.util.TranslationMap;

/**
 * The {@link FXTranslationHelper} is a helper utility which encapsulates most
 * of the boiler-plate code needed for the translation support.
 * 
 * @author Robert Zenz
 */
public class FXTranslationHelper
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property for if the translation is enabled. */
	private BooleanProperty enabled;
	
	/** The property for the {@link TranslationMap}. */
	private ObjectProperty<TranslationMap> map;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTranslationHelper}.
	 */
	public FXTranslationHelper()
	{
		enabled = new SimpleBooleanProperty(true);
		
		map = new SimpleObjectProperty<>();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the property for if the translation is enabled or not.
	 *
	 * @return the property for if the translation is enabled or not.
	 */
	public BooleanProperty enabledProperty()
	{
		return enabled;
	}
	
	/**
	 * Gets the {@link TranslationMap translation map}.
	 *
	 * @return the {@link TranslationMap translation map}.
	 */
	public TranslationMap getMap()
	{
		return map.get();
	}
	
	/**
	 * Gets if the translation is enabled.
	 *
	 * @return {@code true} if the translation is enabled.
	 */
	public boolean isEnabled()
	{
		return enabled.get();
	}
	
	/**
	 * Gets the property for the {@link TranslationMap translation map}.
	 * 
	 * @return the property for the {@link TranslationMap translation map}.
	 */
	public ObjectProperty<TranslationMap> mapProperty()
	{
		return map;
	}
	
	/**
	 * Sets if the translation is enabled.
	 *
	 * @param pTranslationEnabled {@code true} if the translation should be
	 *            enabled.
	 */
	public void setEnabled(boolean pTranslationEnabled)
	{
		enabled.set(pTranslationEnabled);
	}
	
	/**
	 * Sets the {@link TranslationMap translation map}.
	 *
	 * @param pTranslationMap the new {@link TranslationMap translation map}.
	 */
	public void setMap(TranslationMap pTranslationMap)
	{
		map.set(pTranslationMap);
	}
	
	/**
	 * Translates the given text if there is a {@link #mapProperty() map} set
	 * and this is {@link #enabledProperty() enabled}.
	 * 
	 * @param pText the text to translated.
	 * @return the translated text.
	 */
	public String translate(String pText)
	{
		if (enabled.get() && map.get() != null)
		{
			return map.get().translate(pText);
		}
		
		return pText;
	}
	
}	// FXTranslationHelper
