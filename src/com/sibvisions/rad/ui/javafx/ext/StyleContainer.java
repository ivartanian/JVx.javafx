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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The {@link StyleContainer} is a helper class that allows to easily manage the
 * {@link Node#styleProperty()} with a {@link Map} like interface.
 * 
 * @author Robert Zenz
 */
public class StyleContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property name for the background color. */
	protected static final String PROPERTY_BACKGROUND_COLOR = "-fx-background-color";
	
	/** The property name for the cursor. */
	protected static final String PROPERTY_CURSOR = "-fx-cursor";
	
	/** The property name for the font family. */
	protected static final String PROPERTY_FONT_FAMILY = "-fx-font-family";
	
	/** The property name for the font weight. */
	protected static final String PROPERTY_FONT_WEIGHT = "-fx-font-weight";
	
	/** The property name for the font style. */
	protected static final String PROPERTY_FONT_STYLE = "-fx-font-style";
	
	/** The property name for the font size. */
	protected static final String PROPERTY_FONT_SIZE = "-fx-font-size";
	
	/** The property name for the inner background color. */
	protected static final String PROPERTY_INNER_BACKGROUND = "-fx-control-inner-background";
	
	/** The property name for the text fill color. */
	protected static final String PROPERTY_TEXT_FILL = "-fx-text-fill";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The value to append after the declarations. */
	private String after;
	
	/**
	 * If automatic updates (whenever a value is set or cleared) are enabled.
	 */
	private boolean automaticUpdatesEnabled;
	
	/** The value to append before the declarations. */
	private String before;
	
	/** The {@link Map} that holds declarations. */
	private Map<String, String> declarations = new HashMap<>();
	
	/** The {@link Map} that holds associated objects. */
	private Map<String, Object> objects = new HashMap<>();
	
	/** The parent {@link MenuItem}. */
	private MenuItem parentMenuItem;
	
	/** The parent {@link Node}. */
	private Node parentNode;
	
	/** The string representation of this. */
	private String style;
	
	/** The listeners for when an update occurs. */
	private List<WeakReference<Runnable>> updateListeners;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link StyleContainer}.
	 */
	public StyleContainer()
	{
		automaticUpdatesEnabled = true;
		style = "";
		updateListeners = new ArrayList<>();
	}
	
	/**
	 * Creates a new instance of {@link StyleContainer}.
	 *
	 * @param pParent the parent.
	 */
	public StyleContainer(MenuItem pParent)
	{
		this();
		
		parentMenuItem = pParent;
	}
	
	/**
	 * Creates a new instance of {@link StyleContainer}.
	 *
	 * @param pParent the parent.
	 */
	public StyleContainer(Node pParent)
	{
		this();
		
		parentNode = pParent;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return style;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds an {@link Runnable update listener} which is invoked everytime the
	 * style is updated.
	 * 
	 * @param pListener the {@link Runnable listener}.
	 */
	public void addUpdateListener(Runnable pListener)
	{
		updateListeners.add(new WeakReference<Runnable>(pListener));
	}
	
	/**
	 * Clears all set properties from this {@link StyleContainer}, including the
	 * set {@link #setAfter(String)} and {@link #setBefore(String)} values.
	 */
	public void clear()
	{
		declarations.clear();
		objects.clear();
		
		after = null;
		before = null;
		
		if (automaticUpdatesEnabled)
		{
			updateStyle();
		}
	}
	
	/**
	 * Clears the given property (if any).
	 * 
	 * @param pPropertyName the name of the property.
	 */
	public void clear(String pPropertyName)
	{
		declarations.remove(pPropertyName);
		objects.remove(pPropertyName);
		
		if (automaticUpdatesEnabled)
		{
			updateStyle();
		}
	}
	
	/**
	 * Gets the string to be appended after the set declarations.
	 * 
	 * @return the string to be appended after the set declarations.
	 */
	public String getAfter()
	{
		return after;
	}
	
	/**
	 * Gets the value of the background color property.
	 * 
	 * @return the {@link Color} of the background color property. {@code null}
	 *         if it is not set.
	 */
	public Color getBackgroundColor()
	{
		return (Color)getObject(PROPERTY_BACKGROUND_COLOR);
	}
	
	/**
	 * Gets the string to be appended before the set declarations.
	 * 
	 * @return the string to be appended before the set declarations.
	 */
	public String getBefore()
	{
		return before;
	}
	
	/**
	 * Gets the value of the cursor property.
	 * 
	 * @return the {@link Cursor} of the cursor property. {@code null} if it is
	 *         not set.
	 */
	public Cursor getCursor()
	{
		String cursor = (String)getObject(PROPERTY_CURSOR);
		
		if (cursor != null)
		{
			return Cursor.cursor(cursor.toUpperCase().replace('-', '_'));
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Gets the value of the font/text fill property.
	 * 
	 * @return the {@link Font} of the font/text fill property. {@code null} if
	 *         it is not set.
	 */
	public Font getFont()
	{
		Font font = (Font)getObject(PROPERTY_FONT_FAMILY);
		
		if (font == null)
		{
			font = (Font)getObject(PROPERTY_FONT_SIZE);
		}
		if (font == null)
		{
			font = (Font)getObject(PROPERTY_FONT_STYLE);
		}
		if (font == null)
		{
			font = (Font)getObject(PROPERTY_FONT_WEIGHT);
		}
		
		return font;
	}
	
	/**
	 * Gets the value of the foreground color property.
	 * 
	 * @return the {@link Color} of the foreground color property. {@code null}
	 *         if it is not set.
	 */
	public Color getForeground()
	{
		return (Color)getObject(PROPERTY_TEXT_FILL);
	}
	
	/**
	 * Gets the value of the inner background property.
	 * 
	 * @return the {@link Color} of the inner background property. {@code null}
	 *         if it is not set.
	 */
	public Color getInnerBackground()
	{
		return (Color)getObject(PROPERTY_INNER_BACKGROUND);
	}
	
	/**
	 * Gets the object associated with the given property name.
	 * 
	 * @param pPropertyName the name of the property.
	 * @return the associated object. {@code null} if there is none.
	 */
	public Object getObject(String pPropertyName)
	{
		return objects.get(pPropertyName);
	}
	
	/**
	 * Gets the style as string.
	 * 
	 * @return the style as string.
	 */
	public String getStyle()
	{
		StringBuilder styleBuilder = new StringBuilder();
		
		if (before != null && before.length() > 0)
		{
			styleBuilder.append(before);
		}
		
		for (Map.Entry<String, String> entry : declarations.entrySet())
		{
			styleBuilder.append(entry.getKey());
			styleBuilder.append(": ");
			styleBuilder.append(entry.getValue());
			styleBuilder.append(";\n");
		}
		
		if (after != null && after.length() > 0)
		{
			styleBuilder.append(after);
		}
		
		return styleBuilder.toString();
	}
	
	/**
	 * Gets the value of the given property name.
	 * 
	 * @param pPropertyName the name of the property.
	 * @return the value. {@code null} if there is none.
	 */
	public String getValue(String pPropertyName)
	{
		return declarations.get(pPropertyName);
	}
	
	/**
	 * Gets if the property with the given name is set.
	 * 
	 * @param pPropertyName the name of the property.
	 * @return {@code true} if the property is set.
	 */
	public boolean isSet(String pPropertyName)
	{
		return declarations.containsKey(pPropertyName);
	}
	
	/**
	 * Removes the given {@link Runnable listener}.
	 * 
	 * @param pListener the {@link Runnable listener} to remove.
	 */
	public void removeUpdateListener(Runnable pListener)
	{
		Iterator<WeakReference<Runnable>> iterator = updateListeners.iterator();
		
		while (iterator.hasNext())
		{
			Runnable listener = iterator.next().get();
			
			if (listener != null && listener == pListener)
			{
				iterator.remove();
			}
		}
		
		removeDeadUpdateListeners();
	}
	
	/**
	 * Sets the property with the given name to the given value.
	 * 
	 * @param pPropertyName the name of the property.
	 * @param pValue the value of the property.
	 */
	public void set(String pPropertyName, String pValue)
	{
		set(pPropertyName, pValue, null);
	}
	
	/**
	 * Sets the property with the given name to the given value.
	 * 
	 * @param pPropertyName the name of the property.
	 * @param pValue the value of the property.
	 * @param pObject the associated object.
	 */
	public void set(String pPropertyName, String pValue, Object pObject)
	{
		declarations.put(pPropertyName, pValue);
		objects.put(pPropertyName, pObject);
		
		if (automaticUpdatesEnabled)
		{
			updateStyle();
		}
	}
	
	/**
	 * Sets the string to be appended after the set declarations.
	 * 
	 * @param pAfter the string to be appended after the set declarations.
	 */
	public void setAfter(String pAfter)
	{
		after = pAfter;
		
		if (automaticUpdatesEnabled)
		{
			updateStyle();
		}
	}
	
	/**
	 * Sets the background color property.
	 * 
	 * @param pColor the {@link Color} to set. {@code null} to clear the
	 *            property.
	 */
	public void setBackground(Color pColor)
	{
		if (pColor != null)
		{
			set(PROPERTY_BACKGROUND_COLOR, toHexString(pColor), pColor);
		}
		else
		{
			clear(PROPERTY_BACKGROUND_COLOR);
		}
	}
	
	/**
	 * Sets the string to be appended before the set declarations.
	 * 
	 * @param pBefore the string to be appended before the set declarations.
	 */
	public void setBefore(String pBefore)
	{
		before = pBefore;
		
		if (automaticUpdatesEnabled)
		{
			updateStyle();
		}
	}
	
	/**
	 * Sets the cursor property.
	 * 
	 * @param pCursor the {@link Cursor} to set. {@code null} to clear the
	 *            property.
	 */
	public void setCursor(Cursor pCursor)
	{
		if (pCursor != null)
		{
			set(PROPERTY_CURSOR, pCursor.toString().toLowerCase().replace('_', '-'), pCursor);
		}
		else
		{
			clear(PROPERTY_CURSOR);
		}
	}
	
	/**
	 * Sets the font property.
	 * 
	 * @param pFont the {@link Font} to set. {@code null} to clear the property.
	 */
	public void setFont(Font pFont)
	{
		setAutomaticUpdatesEnabled(false);
		
		clear(PROPERTY_FONT_FAMILY);
		clear(PROPERTY_FONT_SIZE);
		clear(PROPERTY_FONT_STYLE);
		
		if (pFont != null)
		{
			if (pFont.getFamily() != null && pFont.getFamily().length() > 0)
			{
				setQuoted(PROPERTY_FONT_FAMILY, pFont.getFamily(), pFont);
			}
			if (pFont.getSize() > 0)
			{
				set(PROPERTY_FONT_SIZE, String.format(Locale.ENGLISH, "%#.2f", Double.valueOf(pFont.getSize())) + "px", pFont);
			}
			if (pFont.getStyle() != null && !pFont.getStyle().isEmpty())
			{
				if (pFont.getStyle().contains("Bold"))
				{
					set(PROPERTY_FONT_WEIGHT, "bold", pFont);
				}
				if (pFont.getStyle().contains("Italic"))
				{
					set(PROPERTY_FONT_STYLE, "italic", pFont);
				}
			}
		}
		
		setAutomaticUpdatesEnabled(true);
		updateStyle();
	}
	
	/**
	 * Sets the foreground color property.
	 * 
	 * @param pColor the {@link Color} to set. {@code null} to clear the
	 *            property.
	 */
	public void setForeground(Color pColor)
	{
		if (pColor != null)
		{
			set(PROPERTY_TEXT_FILL, toHexString(pColor), pColor);
		}
		else
		{
			clear(PROPERTY_TEXT_FILL);
		}
	}
	
	/**
	 * Sets the inner background color property.
	 * 
	 * @param pColor the {@link Color} to set. {@code null} to clear the
	 *            property.
	 */
	public void setInnerBackground(Color pColor)
	{
		if (pColor != null)
		{
			set(PROPERTY_INNER_BACKGROUND, toHexString(pColor), pColor);
		}
		else
		{
			clear(PROPERTY_INNER_BACKGROUND);
		}
	}
	
	/**
	 * Sets the given {@link MenuItem} as new parent. If there is an old parent,
	 * styles are removed from it.
	 * 
	 * @param pMenuItem the {@link MenuItem} as new parent.
	 */
	public void setParent(MenuItem pMenuItem)
	{
		removeStyle();
		
		parentMenuItem = pMenuItem;
		parentNode = null;
		
		updateStyle();
	}
	
	/**
	 * Sets the given {@link Node} as new parent. If there is an old parent,
	 * styles are removed from it.
	 * 
	 * @param pNode the {@link Node} as new parent.
	 */
	public void setParent(Node pNode)
	{
		removeStyle();
		
		parentMenuItem = null;
		parentNode = pNode;
		
		updateStyle();
	}
	
	/**
	 * Sets the property with the given name to the given value, the value will
	 * be quoted.
	 * 
	 * @param pPropertyName the name of the property.
	 * @param pValue the value of the property.
	 */
	public void setQuoted(String pPropertyName, String pValue)
	{
		setQuoted(pPropertyName, pValue, null);
	}
	
	/**
	 * Sets the property with the given name to the given value, the value will
	 * be quoted.
	 * 
	 * @param pPropertyName the name of the property.
	 * @param pValue the value of the property.
	 * @param pObject the associated object.
	 */
	public void setQuoted(String pPropertyName, String pValue, Object pObject)
	{
		set(pPropertyName, "\"" + pValue + "\"", pObject);
	}
	
	/**
	 * Removes any set style from the parent, if any.
	 */
	public void removeStyle()
	{
		if (parentNode != null)
		{
			parentNode.setStyle(getStyle());
		}
		else if (parentMenuItem != null)
		{
			parentMenuItem.setStyle(getStyle());
		}
	}
	
	/**
	 * Notifies all {@link #updateListeners}.
	 */
	protected void notifyUpdateListeners()
	{
		for (WeakReference<Runnable> weakListener : updateListeners)
		{
			Runnable listener = weakListener.get();
			
			if (listener != null)
			{
				listener.run();
			}
		}
	}
	
	/**
	 * Removes all dead {@link Runnable listener}s.
	 */
	protected void removeDeadUpdateListeners()
	{
		Iterator<WeakReference<Runnable>> iterator = updateListeners.iterator();
		
		while (iterator.hasNext())
		{
			Runnable listener = iterator.next().get();
			
			if (listener == null)
			{
				iterator.remove();
			}
		}
	}
	
	/**
	 * Sets if the automatic updates are enabled.
	 * <p>
	 * An automatic update in this case means that every set/clear will be
	 * directly propagated to the parent. If you want to bulk set/clear
	 * properties, disable them, enable them afterwards and call
	 * {@link #updateStyle()} once.
	 * 
	 * @param pEnabled {@code true} if they should be enabled.
	 */
	protected void setAutomaticUpdatesEnabled(boolean pEnabled)
	{
		automaticUpdatesEnabled = pEnabled;
	}
	
	/**
	 * Converts the given {@link Color} to the hex-representation, including
	 * leading number sign.
	 * 
	 * @param pColor the {@link Color}.
	 * @return the hex representation, including leading number sign.
	 */
	protected String toHexString(Color pColor)
	{
		if ((pColor.getRed() < 1 && pColor.getRed() > 0)
				|| (pColor.getGreen() < 1 && pColor.getGreen() > 0)
				|| (pColor.getBlue() < 1 && pColor.getBlue() > 0))
		{
			return String.format("#%02X%02X%02X",
					Integer.valueOf((int)(pColor.getRed() * 255)),
					Integer.valueOf((int)(pColor.getGreen() * 255)),
					Integer.valueOf((int)(pColor.getBlue() * 255)));
		}
		else
		{
			return String.format("#%02X%02X%02X",
					Integer.valueOf((int)(pColor.getRed()) * 255),
					Integer.valueOf((int)(pColor.getGreen()) * 255),
					Integer.valueOf((int)(pColor.getBlue()) * 255));
		}
	}
	
	/**
	 * Updates the parent with all changes.
	 */
	protected void updateStyle()
	{
		if (parentNode != null)
		{
			parentNode.setStyle(getStyle());
		}
		else if (parentMenuItem != null)
		{
			parentMenuItem.setStyle(getStyle());
		}
		
		notifyUpdateListeners();
	}
	
}	// StyleContainer
