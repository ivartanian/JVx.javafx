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

/**
 * A simple interface that allows to forward layout specific methods.
 * 
 * @author Robert Zenz
 */
public interface IFXLayoutForwarding
{
	/**
	 * Calculates the preferred height of this.
	 * 
	 * @param pWidth the width.
	 * @return the preferred height.
	 */
	public double calculatePreferredHeight(double pWidth);
	
	/**
	 * Calculates the preferred width of this.
	 * 
	 * @param pHeight the height.
	 * @return the preferred width.
	 */
	public double calculatePreferredWidth(double pHeight);
	
	/**
	 * Layouts all children.
	 */
	public void doLayout();
	
}	// IFXLayoutForwarding
