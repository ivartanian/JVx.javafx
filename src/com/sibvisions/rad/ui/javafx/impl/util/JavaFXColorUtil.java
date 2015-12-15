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
package com.sibvisions.rad.ui.javafx.impl.util;

import java.util.HashMap;
import java.util.Map;

import javax.rad.ui.IColor;

import com.sibvisions.rad.ui.javafx.impl.JavaFXColor;

/**
 * The {@link JavaFXColorUtil} is a utility class for dealing with
 * {@link IColor}s.
 * 
 * @author Robert Zenz
 */
public final class JavaFXColorUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Map} of system colors. */
	private static Map<String, IColor> systemColors;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static
	{
		systemColors = new HashMap<>();
		systemColors.put(IColor.CONTROL_BACKGROUND, new JavaFXColor(0x0d, 0x0d, 0x0d, 0xff));
		systemColors.put(IColor.CONTROL_ALTERNATE_BACKGROUND, new JavaFXColor(0xf4, 0xf4, 0xf4, 0xff));
		systemColors.put(IColor.CONTROL_FOREGROUND, new JavaFXColor(0x00, 0x00, 0x00, 0xff));
		systemColors.put(IColor.CONTROL_ACTIVE_SELECTION_BACKGROUND, new JavaFXColor(0xcc, 0xe3, 0xf4, 0xff));
		systemColors.put(IColor.CONTROL_ACTIVE_SELECTION_FOREGROUND, new JavaFXColor(0x00, 0x00, 0x00, 0xff));
		systemColors.put(IColor.CONTROL_INACTIVE_SELECTION_BACKGROUND, new JavaFXColor(0x0a, 0x0a, 0x0a, 0xff));
		systemColors.put(IColor.CONTROL_INACTIVE_SELECTION_FOREGROUND, new JavaFXColor(0x00, 0x00, 0x00, 0xff));
		systemColors.put(IColor.CONTROL_MANDATORY_BACKGROUND, new JavaFXColor(255, 244, 210, 255));
		systemColors.put(IColor.CONTROL_READ_ONLY_BACKGROUND, new JavaFXColor(0x0d, 0x0d, 0x0d, 0xff));
		systemColors.put(IColor.INVALID_EDITOR_BACKGROUND, new JavaFXColor(209, 51, 51, 255));
	}
	
	/**
	 * No instance needed.
	 */
	private JavaFXColorUtil()
	{
		// Not needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the given {@link IColor} as system color.
	 * 
	 * @param pType the type of the system color.
	 * @param pColor the {@link IColor} to add.
	 */
	public static void addSystemColor(String pType, IColor pColor)
	{
		systemColors.put(pType, pColor);
	}
	
	/**
	 * Gets the {@link IColor system color} with the given type, returns
	 * {@code null} if there is none.
	 * 
	 * @param pType the type of the system color.
	 * @return the {@link IColor system color}, {@code null} if there is none.
	 */
	public static IColor getSystemColor(String pType)
	{
		return systemColors.get(pType);
	}
	
}	// JavaFXColorUtil
