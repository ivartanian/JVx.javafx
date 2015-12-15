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
package com.sibvisions.rad.ui.javafx.ext.util;

import javax.rad.ui.IAlignmentConstants;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.TextAlignment;

/**
 * The {@link FXAlignmentUtil} is a simple helper utility that allows to convert
 * between the constants in {@link IAlignmentConstants} and various JavaFX
 * classes, like {@link Pos}.
 * 
 * @author Robert Zenz
 */
public final class FXAlignmentUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance needed.
	 */
	private FXAlignmentUtil()
	{
		// Not needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Converts the values of the given {@link IAlignmentConstants} to
	 * {@link Pos}.
	 * 
	 * @param pAlignments the {@link IAlignmentConstants}.
	 * @return the appropriate {@link Pos}.
	 */
	public static Pos alignmentsToPos(IAlignmentConstants pAlignments)
	{
		return alignmentsToPos(pAlignments.getHorizontalAlignment(), pAlignments.getVerticalAlignment());
	}
	
	/**
	 * Converts the values of the given {@link IAlignmentConstants} to
	 * {@link Pos}.
	 * 
	 * @param pAlignments the {@link IAlignmentConstants}.
	 * @param pDefaultPos the default {@link Pos}.
	 * @return the appropriate {@link Pos}, or the given default {@link Pos}.
	 */
	public static Pos alignmentsToPos(IAlignmentConstants pAlignments, Pos pDefaultPos)
	{
		return alignmentsToPos(pAlignments.getHorizontalAlignment(), pAlignments.getVerticalAlignment(), pDefaultPos);
	}
	
	/**
	 * Converts the given alignment values to {@link Pos}.
	 * 
	 * @param pHorizontalAlignment the horizontal alignment.
	 * @param pVerticalAlignment the vertical alignment.
	 * @return the appropriate {@link Pos}.
	 */
	public static Pos alignmentsToPos(int pHorizontalAlignment, int pVerticalAlignment)
	{
		return alignmentsToPos(pHorizontalAlignment, pVerticalAlignment, Pos.BASELINE_LEFT);
	}
	
	/**
	 * Converts the given alignment values to {@link Pos}.
	 * 
	 * @param pHorizontalAlignment the horizontal alignment.
	 * @param pVerticalAlignment the vertical alignment.
	 * @param pDefaultPos the default {@link Pos} to return.
	 * @return the appropriate {@link Pos}, or the given default {@link Pos}.
	 */
	public static Pos alignmentsToPos(int pHorizontalAlignment, int pVerticalAlignment, Pos pDefaultPos)
	{
		switch (pHorizontalAlignment)
		{
			case IAlignmentConstants.ALIGN_CENTER:
			case IAlignmentConstants.ALIGN_STRETCH:
				switch (pVerticalAlignment)
				{
					case IAlignmentConstants.ALIGN_BOTTOM:
						return Pos.BOTTOM_CENTER;
						
					case IAlignmentConstants.ALIGN_CENTER:
					case IAlignmentConstants.ALIGN_STRETCH:
						return Pos.CENTER;
						
					case IAlignmentConstants.ALIGN_TOP:
						return Pos.TOP_CENTER;
						
					default:
						return Pos.CENTER_LEFT;
				}
				
			case IAlignmentConstants.ALIGN_LEFT:
				switch (pVerticalAlignment)
				{
					case IAlignmentConstants.ALIGN_BOTTOM:
						return Pos.BOTTOM_LEFT;
						
					case IAlignmentConstants.ALIGN_CENTER:
					case IAlignmentConstants.ALIGN_STRETCH:
						return Pos.CENTER_LEFT;
						
					case IAlignmentConstants.ALIGN_TOP:
						return Pos.TOP_LEFT;
						
					default:
						return Pos.CENTER_LEFT;
				}
				
			case IAlignmentConstants.ALIGN_RIGHT:
				switch (pVerticalAlignment)
				{
					case IAlignmentConstants.ALIGN_BOTTOM:
						return Pos.BOTTOM_RIGHT;
						
					case IAlignmentConstants.ALIGN_CENTER:
					case IAlignmentConstants.ALIGN_STRETCH:
						return Pos.CENTER_RIGHT;
						
					case IAlignmentConstants.ALIGN_TOP:
						return Pos.TOP_RIGHT;
						
					default:
						return Pos.CENTER_RIGHT;
				}
				
			default:
				break;
		}
		
		return pDefaultPos;
	}
	
	/**
	 * Converts the given alignment value to {@link ContentDisplay}.
	 * 
	 * @param pAlignment the alignment.
	 * @return the appropriate {@link ContentDisplay}.
	 */
	public static ContentDisplay alignmentToContentDisplay(int pAlignment)
	{
		switch (pAlignment)
		{
			case IAlignmentConstants.ALIGN_BOTTOM:
				return ContentDisplay.BOTTOM;
				
			case IAlignmentConstants.ALIGN_CENTER:
				return ContentDisplay.CENTER;
				
			case IAlignmentConstants.ALIGN_DEFAULT:
				return ContentDisplay.LEFT;
				
			case IAlignmentConstants.ALIGN_STRETCH:
				return ContentDisplay.CENTER;
				
			case IAlignmentConstants.ALIGN_TOP:
				return ContentDisplay.TOP;
				
			default:
				// Ignore...
				
		}
		
		switch (pAlignment)
		{
			case IAlignmentConstants.ALIGN_CENTER:
				return ContentDisplay.CENTER;
				
			case IAlignmentConstants.ALIGN_DEFAULT:
				return ContentDisplay.LEFT;
				
			case IAlignmentConstants.ALIGN_LEFT:
				return ContentDisplay.LEFT;
				
			case IAlignmentConstants.ALIGN_RIGHT:
				return ContentDisplay.RIGHT;
				
			case IAlignmentConstants.ALIGN_STRETCH:
				return ContentDisplay.CENTER;
				
			default:
				// Ignore...
				
		}
		
		return ContentDisplay.LEFT;
	}
	
	/**
	 * Converts the given {@link IAlignmentConstants} to {@link HPos}.
	 * 
	 * @param pAlignmentConstants the {@link IAlignmentConstants}
	 * @return the appropriate {@link HPos}.
	 */
	public static HPos alignmentToHPos(IAlignmentConstants pAlignmentConstants)
	{
		return alignmentToHPos(pAlignmentConstants.getHorizontalAlignment());
	}
	
	/**
	 * Converts the given {@link IAlignmentConstants} to {@link HPos}.
	 * 
	 * @param pAlignmentConstants the {@link IAlignmentConstants}
	 * @param pDefaultValue the default {@link HPos}.
	 * @return the appropriate {@link HPos}, or the default {@link HPos}.
	 */
	public static HPos alignmentToHPos(IAlignmentConstants pAlignmentConstants, HPos pDefaultValue)
	{
		return alignmentToHPos(pAlignmentConstants.getHorizontalAlignment(), pDefaultValue);
	}
	
	/**
	 * Converts the given horizontal alignment to {@link HPos}.
	 * 
	 * @param pHorizontalAlignment the horizontal alignment.
	 * @return the appropriate {@link HPos}.
	 */
	public static HPos alignmentToHPos(int pHorizontalAlignment)
	{
		return alignmentToHPos(pHorizontalAlignment, HPos.LEFT);
	}
	
	/**
	 * Converts the given horizontal alignment to {@link HPos}.
	 * 
	 * @param pHorizontalAlignment the horizontal alignment.
	 * @param pDefaultValue the default value to use.
	 * @return the appropriate {@link HPos}, or the given default value.
	 */
	public static HPos alignmentToHPos(int pHorizontalAlignment, HPos pDefaultValue)
	{
		switch (pHorizontalAlignment)
		{
			case IAlignmentConstants.ALIGN_LEFT:
				return HPos.LEFT;
				
			case IAlignmentConstants.ALIGN_CENTER:
				return HPos.CENTER;
				
			case IAlignmentConstants.ALIGN_RIGHT:
				return HPos.RIGHT;
				
			default:
				return pDefaultValue;
				
		}
	}
	
	/**
	 * Converts the given alignment to {@link Pos}.
	 * 
	 * @param pAlignment the alignment.
	 * @return the appropriate {@link Pos}.
	 */
	public static Pos alignmentToPos(int pAlignment)
	{
		return alignmentToPos(pAlignment, Pos.CENTER);
	}
	
	/**
	 * Converts the given alignment to {@link Pos}.
	 * 
	 * @param pAlignment the alignment.
	 * @param pDefaultValue the default {@link Pos}.
	 * @return the appropriate {@link Pos}, or the default {@link Pos}.
	 */
	public static Pos alignmentToPos(int pAlignment, Pos pDefaultValue)
	{
		switch (pAlignment)
		{
			case IAlignmentConstants.ALIGN_BOTTOM:
				return Pos.BOTTOM_CENTER;
				
			case IAlignmentConstants.ALIGN_CENTER:
				return Pos.CENTER;
				
			case IAlignmentConstants.ALIGN_DEFAULT:
				return pDefaultValue;
				
			case IAlignmentConstants.ALIGN_STRETCH:
				return Pos.CENTER;
				
			case IAlignmentConstants.ALIGN_TOP:
				return Pos.TOP_CENTER;
				
			default:
				// Ignore...
				
		}
		
		switch (pAlignment)
		{
			case IAlignmentConstants.ALIGN_CENTER:
				return Pos.CENTER;
				
			case IAlignmentConstants.ALIGN_DEFAULT:
				return pDefaultValue;
				
			case IAlignmentConstants.ALIGN_LEFT:
				return Pos.CENTER_LEFT;
				
			case IAlignmentConstants.ALIGN_RIGHT:
				return Pos.CENTER_RIGHT;
				
			case IAlignmentConstants.ALIGN_STRETCH:
				return Pos.CENTER;
				
			default:
				// Ignore...
				
		}
		
		return pDefaultValue;
	}
	
	/**
	 * Converts the given horizontal alignment to {@link TextAlignment}.
	 * 
	 * @param pHorizontalAlignment the alignment.
	 * @return the appropriate {@link TextAlignment}.
	 */
	public static TextAlignment alignmentToTextAlignments(int pHorizontalAlignment)
	{
		switch (pHorizontalAlignment)
		{
			case IAlignmentConstants.ALIGN_LEFT:
				return TextAlignment.LEFT;
				
			case IAlignmentConstants.ALIGN_CENTER:
				return TextAlignment.CENTER;
				
			case IAlignmentConstants.ALIGN_RIGHT:
				return TextAlignment.RIGHT;
				
			case IAlignmentConstants.ALIGN_STRETCH:
				return TextAlignment.JUSTIFY;
				
			default:
				return TextAlignment.LEFT;
		}
	}
	
	/**
	 * Converts the given {@link IAlignmentConstants} to {@link VPos}.
	 * 
	 * @param pAlignmentConstants the {@link IAlignmentConstants}
	 * @return the appropriate {@link VPos}.
	 */
	public static VPos alignmentToVPos(IAlignmentConstants pAlignmentConstants)
	{
		return alignmentToVPos(pAlignmentConstants.getHorizontalAlignment());
	}
	
	/**
	 * Converts the given {@link IAlignmentConstants} to {@link VPos}.
	 * 
	 * @param pAlignmentConstants the {@link IAlignmentConstants}
	 * @param pDefaultValue the default {@link VPos}.
	 * @return the appropriate {@link VPos}, or the default {@link VPos}.
	 */
	public static VPos alignmentToVPos(IAlignmentConstants pAlignmentConstants, VPos pDefaultValue)
	{
		return alignmentToVPos(pAlignmentConstants.getHorizontalAlignment(), pDefaultValue);
	}
	
	/**
	 * Converts the given vertical alignment to {@link VPos}.
	 * 
	 * @param pVerticalAlignment the vertical alignment.
	 * @return the appropriate {@link VPos}.
	 */
	public static VPos alignmentToVPos(int pVerticalAlignment)
	{
		return alignmentToVPos(pVerticalAlignment, VPos.TOP);
	}
	
	/**
	 * Converts the given vertical alignment to {@link VPos}.
	 * 
	 * @param pVerticalAlignment the vertical alignment.
	 * @param pDefaultValue the default value to use.
	 * @return the appropriate {@link VPos}, or the given default value.
	 */
	public static VPos alignmentToVPos(int pVerticalAlignment, VPos pDefaultValue)
	{
		switch (pVerticalAlignment)
		{
			case IAlignmentConstants.ALIGN_TOP:
				return VPos.TOP;
				
			case IAlignmentConstants.ALIGN_CENTER:
				return VPos.CENTER;
				
			case IAlignmentConstants.ALIGN_BOTTOM:
				return VPos.BOTTOM;
				
			default:
				return pDefaultValue;
				
		}
	}
	
}	// FXAlignmentUtil
