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
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;

/**
 * A simple interface that declares that its implementor can control and
 * fine-tune the positions of its contents.
 * 
 * @author Robert Zenz
 */
public interface IFXContentAlignable
{
	
	/**
	 * Gets the content display.
	 *
	 * @return the content display.
	 */
	public ContentDisplay getContentDisplay();
	
	/**
	 * Gets the relative horizontal text alignment.
	 *
	 * @return the relative horizontal text alignment.
	 */
	public HPos getRelativeHorizontalTextAlignment();
	
	/**
	 * Gets the relative vertical text alignment.
	 *
	 * @return the relative vertical text alignment.
	 */
	public VPos getRelativeVerticalTextAlignment();
	
	/**
	 * Gets the property for the relative horizontal text alignment.
	 *
	 * @return the property for the relative horizontal text alignment.
	 */
	public ObjectProperty<HPos> relativeHorizontalTextAlignment();
	
	/**
	 * Gets the property for the relative vertical text alignment.
	 *
	 * @return the property for the relative vertical text alignment.
	 */
	public ObjectProperty<VPos> relativeVerticalTextAlignment();
	
	/**
	 * Sets the content display.
	 *
	 * @param pContentDisplay the new content display.
	 */
	public void setContentDisplay(ContentDisplay pContentDisplay);
	
	/**
	 * Sets the relative horizontal text alignment.
	 *
	 * @param pHPos the new relative horizontal text alignment.
	 */
	public void setRelativeHorizontalTextAlignment(HPos pHPos);
	
	/**
	 * Sets the relative vertical text alignment.
	 *
	 * @param pVPos the new relative vertical text alignment.
	 */
	public void setRelativeVerticalTextAlignment(VPos pVPos);
	
}
