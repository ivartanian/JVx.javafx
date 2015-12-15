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
package com.sibvisions.rad.ui.javafx.impl;

import java.util.HashMap;
import java.util.Map;

import javax.rad.ui.ICursor;

import javafx.scene.Cursor;

/**
 * The {@link JavaFXCursor} is the JavaFX specific implementation of
 * {@link ICursor}.
 * 
 * @author Robert Zenz
 * @see ICursor
 * @see Cursor
 */
public class JavaFXCursor extends JavaFXResource<Cursor> implements ICursor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The list of available cursors. */
	private static final Map<Integer, JavaFXCursor> CURSORS = new HashMap<>();
	
	/** The list of available cursors by name. */
	private static final Map<String, JavaFXCursor> CURSORS_BY_NAME = new HashMap<>();
	
	/** The name of the cursor that is used in CSS. */
	private String cssName;
	
	/** The name. */
	private String name;
	
	/** The type. */
	private int type;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	static
	{
		CURSORS.put(Integer.valueOf(ICursor.CROSSHAIR_CURSOR), new JavaFXCursor("CROSSHAIR_CURSOR", "crosshair", ICursor.CROSSHAIR_CURSOR, Cursor.CROSSHAIR));
		CURSORS.put(Integer.valueOf(ICursor.DEFAULT_CURSOR), new JavaFXCursor("DEFAULT_CURSOR", "default", ICursor.DEFAULT_CURSOR, Cursor.DISAPPEAR));
		CURSORS.put(Integer.valueOf(ICursor.E_RESIZE_CURSOR), new JavaFXCursor("E_RESIZE_CURSOR", "e-resize", ICursor.E_RESIZE_CURSOR, Cursor.E_RESIZE));
		CURSORS.put(Integer.valueOf(ICursor.HAND_CURSOR), new JavaFXCursor("HAND_CURSOR", "hand", ICursor.HAND_CURSOR, Cursor.HAND));
		CURSORS.put(Integer.valueOf(ICursor.MOVE_CURSOR), new JavaFXCursor("MOVE_CURSOR", "move", ICursor.MOVE_CURSOR, Cursor.MOVE));
		CURSORS.put(Integer.valueOf(ICursor.N_RESIZE_CURSOR), new JavaFXCursor("N_RESIZE_CURSOR", "n-resize", ICursor.N_RESIZE_CURSOR, Cursor.N_RESIZE));
		CURSORS.put(Integer.valueOf(ICursor.NE_RESIZE_CURSOR), new JavaFXCursor("NE_RESIZE_CURSOR", "ne-resize", ICursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE));
		CURSORS.put(Integer.valueOf(ICursor.NW_RESIZE_CURSOR), new JavaFXCursor("NW_RESIZE_CURSOR", "nw-resize", ICursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE));
		CURSORS.put(Integer.valueOf(ICursor.S_RESIZE_CURSOR), new JavaFXCursor("S_RESIZE_CURSOR", "s-resize", ICursor.S_RESIZE_CURSOR, Cursor.S_RESIZE));
		CURSORS.put(Integer.valueOf(ICursor.SE_RESIZE_CURSOR), new JavaFXCursor("SE_RESIZE_CURSOR", "se-resize", ICursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE));
		CURSORS.put(Integer.valueOf(ICursor.SW_RESIZE_CURSOR), new JavaFXCursor("SW_RESIZE_CURSOR", "sw-resize", ICursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE));
		CURSORS.put(Integer.valueOf(ICursor.TEXT_CURSOR), new JavaFXCursor("TEXT_CURSOR", "text", ICursor.TEXT_CURSOR, Cursor.TEXT));
		CURSORS.put(Integer.valueOf(ICursor.W_RESIZE_CURSOR), new JavaFXCursor("W_RESIZE_CURSOR", "w-resize", ICursor.W_RESIZE_CURSOR, Cursor.W_RESIZE));
		CURSORS.put(Integer.valueOf(ICursor.WAIT_CURSOR), new JavaFXCursor("WAIT_CURSOR", "wait", ICursor.WAIT_CURSOR, Cursor.WAIT));
		
		for (JavaFXCursor cursor : CURSORS.values())
		{
			CURSORS_BY_NAME.put(cursor.getName(), cursor);
		}
	}
	
	/**
	 * Creates a new instance of {@link JavaFXCursor}.
	 *
	 * @param pName the name.
	 * @param pCSSName the css name.
	 * @param pType the type.
	 * @param pCursor the cursor.
	 */
	public JavaFXCursor(String pName, String pCSSName, int pType, Cursor pCursor)
	{
		super(pCursor);
		
		cssName = pCSSName;
		name = pName;
		type = pType;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getType()
	{
		return type;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the cursor.
	 *
	 * @param pName the name.
	 * @return the cursor.
	 */
	public static JavaFXCursor getCursor(String pName)
	{
		return CURSORS_BY_NAME.get(pName);
	}
	
	/**
	 * Gets the cursor.
	 *
	 * @param pType the type.
	 * @return the cursor.
	 */
	public static JavaFXCursor getCursor(int pType)
	{
		return CURSORS.get(Integer.valueOf(pType));
	}
	
	/**
	 * Gets the name of the cursor as it is used in CSS.
	 * 
	 * @return the CSS name.
	 */
	public String getCSSName()
	{
		return cssName;
	}
	
}	// JavaFXCursor
