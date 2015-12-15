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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * A {@link TextArea} extension that allows to set the horizontal alignment of
 * the text.
 * <p>
 * <a href="https://javafx-jira.kenai.com/browse/RT-39960">RT-39960</a>
 * 
 * @author Robert Zenz
 */
public class FXTextArea extends TextArea
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The alignment of the text. */
	private ObjectProperty<HPos> alignment;
	
	/** The cached text node that is used. */
	private Text cachedTextNode;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXTextArea}.
	 */
	public FXTextArea()
	{
		this("");
	}
	
	/**
	 * Creates a new instance of {@link FXTextArea}.
	 *
	 * @param pText the text.
	 */
	public FXTextArea(String pText)
	{
		super(pText);
		
		alignment = new SimpleObjectProperty<>(HPos.LEFT);
		alignment.addListener(pAlignmentObservable -> setTextAlignment());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren()
	{
		// Make sure to set the alignment. It's theoretically possible that
		// the alignment has been set before the text node was available.
		// In which case the alignment would not be applied if we wouldn't
		// do it right now.
		setTextAlignment();
		
		super.layoutChildren();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the alignment property.
	 * 
	 * @return the alignment property.
	 */
	public ObjectProperty<HPos> alignmentProperty()
	{
		return alignment;
	}
	
	/**
	 * Gets the alignment.
	 *
	 * @return the alignment.
	 */
	public HPos getAlignment()
	{
		return alignment.get();
	}
	
	/**
	 * Sets the alignment.
	 *
	 * @param pAlignment the new alignment.
	 */
	public void setAlignment(HPos pAlignment)
	{
		alignment.set(pAlignment);
	}
	
	/**
	 * Sets the text alignment to the text node (if available).
	 */
	private void setTextAlignment()
	{
		if (cachedTextNode == null)
		{
			cachedTextNode = (Text)lookup(".text-area *.text");
		}
		
		if (cachedTextNode != null)
		{
			cachedTextNode.setStyle("-fx-text-alignment: " + alignment.get().toString().toLowerCase() + ";");
		}
	}
	
}	// FXTextArea
