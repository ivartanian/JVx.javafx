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
package com.sibvisions.rad.ui.javafx.impl.container;

import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow;
import com.sibvisions.rad.ui.javafx.ext.mdi.skin.FXInternalWindowSkin;

/**
 * The {@link JavaFXTranslatableInternalWindowSkin} is a
 * {@link FXInternalWindowSkin} implementation that allows to be translated.
 * 
 * @author Robert Zenz
 */
public class JavaFXTranslatableInternalWindowSkin extends FXInternalWindowSkin
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link TranslationMap} used. */
	private TranslationMap translation;
	
	/** The original text of the autosize menu item. */
	private String originalAutoSizeMenuItemText;
	
	/** The original text of the close button. */
	private String originalCloseButtonText;
	
	/** The original text of the close menu item. */
	private String originalCloseMenuItemText;
	
	/** The original text of the maximize button. */
	private String originalMaximizeButtonText;
	
	/** The original text of the maximize menu item. */
	private String originalMaximizeMenuItemText;
	
	/** The original text of the minimize button. */
	private String originalMinimizeButtonText;
	
	/** The original text of the minimize menu item. */
	private String originalMinimizeMenuItemText;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXTranslatableInternalWindowSkin}.
	 *
	 * @param pInternalWindow the internal window.
	 */
	public JavaFXTranslatableInternalWindowSkin(FXInternalWindow pInternalWindow)
	{
		super(pInternalWindow);
		
		originalAutoSizeMenuItemText = getAutoSizeMenuItem().getText();
		originalCloseButtonText = getCloseButton().getText();
		originalCloseMenuItemText = getCloseMenuItem().getText();
		originalMaximizeButtonText = getMaximizeButton().getText();
		originalMaximizeMenuItemText = getMaximizeMenuItem().getText();
		originalMinimizeButtonText = getMinimizeButton().getText();
		originalMinimizeMenuItemText = getMinimizeMenuItem().getText();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link TranslationMap}.
	 *
	 * @return the {@link TranslationMap}. May be {@code null}.
	 */
	public TranslationMap getTranslation()
	{
		return translation;
	}
	
	/**
	 * Sets the {@link TranslationMap}.
	 *
	 * @param pTranslation the {@link TranslationMap}. May be {@code null}.
	 */
	public void setTranslation(TranslationMap pTranslation)
	{
		translation = pTranslation;
		
		update();
	}
	
	/**
	 * Updates the text in the skin with the currently set
	 * {@link TranslationMap}.
	 */
	public void update()
	{
		if (translation != null)
		{
			getAutoSizeMenuItem().setText(translation.translate(originalAutoSizeMenuItemText));
			getCloseButton().setText(translation.translate(originalCloseButtonText));
			getCloseMenuItem().setText(translation.translate(originalCloseMenuItemText));
			getMaximizeButton().setText(translation.translate(originalMaximizeButtonText));
			getMaximizeMenuItem().setText(translation.translate(originalMaximizeMenuItemText));
			getMinimizeButton().setText(translation.translate(originalMinimizeButtonText));
			getMinimizeMenuItem().setText(translation.translate(originalMinimizeMenuItemText));
		}
		else
		{
			getAutoSizeMenuItem().setText(originalAutoSizeMenuItemText);
			getCloseButton().setText(originalCloseButtonText);
			getCloseMenuItem().setText(originalCloseMenuItemText);
			getMaximizeButton().setText(originalMaximizeButtonText);
			getMaximizeMenuItem().setText(originalMaximizeMenuItemText);
			getMinimizeButton().setText(originalMinimizeButtonText);
			getMinimizeMenuItem().setText(originalMinimizeMenuItemText);
		}
	}
	
}	// JavaFXTranslatableInternalWindowSkin
