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

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.model.ui.ICellEditor;

import com.sibvisions.rad.ui.javafx.ext.celleditor.FXDateCellEditor;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXNumberCellEditor;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXTextCellEditor;

/**
 * The {@link FXControlUtil} is a utility class for working with controls.
 * 
 * @author Robert Zenz
 */
public final class FXControlUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Map} of classes to default {@link ICellEditor}s. */
	private static Map<Class<?>, ICellEditor> defaultCellEditors;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Static initializer.
	 */
	static
	{
		initDefaultCellEditors();
	}
	
	/**
	 * No instance needed.
	 */
	private FXControlUtil()
	{
		// Not needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Finds the default {@link ICellEditor} for the given {@link IDataType}.
	 * 
	 * @param pDataType the {@link IDataType}.
	 * @return the default {@link ICellEditor}. Guaranteed to be not
	 *         {@code null}, as it defaults to a {@link FXTextCellEditor}.
	 */
	public static ICellEditor findCellEditor(IDataType pDataType)
	{
		ICellEditor foundCellEditor = null;
		
		if (foundCellEditor == null)
		{
			foundCellEditor = pDataType.getCellEditor();
		}
		
		if (foundCellEditor == null && UIFactoryManager.getFactory() != null)
		{
			foundCellEditor = UIFactoryManager.getFactory().getDefaultCellEditor(pDataType.getTypeClass());
		}
		
		if (foundCellEditor == null && UIFactoryManager.getFactory() != null)
		{
			foundCellEditor = UIFactoryManager.getFactory().getDefaultCellEditor(null);
		}
		
		if (foundCellEditor == null)
		{
			foundCellEditor = FXControlUtil.getDefaultCellEditor(pDataType.getTypeClass());
		}
		
		if (foundCellEditor == null)
		{
			foundCellEditor = new FXTextCellEditor();
		}
		
		return foundCellEditor;
	}
	
	/**
	 * Gets the default {@link ICellEditor} for the given class.
	 * 
	 * @param pClass the class.
	 * @return the {@link ICellEditor}. {@code null} if there is none.
	 */
	public static ICellEditor getDefaultCellEditor(Class<?> pClass)
	{
		return defaultCellEditors.get(pClass);
	}
	
	/**
	 * Checks if the given {@link ColumnDefinition} is holding a number.
	 * 
	 * @param pColumnDefinition the {@link ColumnDefinition}.
	 * @return {@code true} if the given column holds a number.
	 */
	public static boolean isNumberColumn(ColumnDefinition pColumnDefinition)
	{
		return isNumberDataType(pColumnDefinition.getDataType());
	}
	
	/**
	 * Checks if the given column from the given {@link IDataRow} is holding a
	 * number.
	 * 
	 * @param pDataRow the {@link IDataRow}.
	 * @param pColumnName the column name.
	 * @return {@code true} if the given column holds a number.
	 */
	public static boolean isNumberColumn(IDataRow pDataRow, String pColumnName)
	{
		try
		{
			return isNumberColumn(pDataRow.getRowDefinition().getColumnDefinition(pColumnName));
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Checks if the given {@link IDataType} is holding a number.
	 * 
	 * @param pDataType the {@link IDataType}.
	 * @return {@code true} if the given datatype holds a number.
	 */
	public static boolean isNumberDataType(IDataType pDataType)
	{
		return Number.class.isAssignableFrom(pDataType.getTypeClass());
	}
	
	/**
	 * Checks if the given {@link ColumnDefinition} is holding a string.
	 * 
	 * @param pColumnDefinition the {@link ColumnDefinition}.
	 * @return {@code true} if the given column holds a string.
	 */
	public static boolean isStringColumn(ColumnDefinition pColumnDefinition)
	{
		return isStringDataType(pColumnDefinition.getDataType());
	}
	
	/**
	 * Checks if the given column from the given {@link IDataRow} is holding a
	 * string.
	 * 
	 * @param pDataRow the {@link IDataRow}.
	 * @param pColumnName the column name.
	 * @return {@code true} if the given column holds a string.
	 */
	public static boolean isStringColumn(IDataRow pDataRow, String pColumnName)
	{
		try
		{
			return isStringColumn(pDataRow.getRowDefinition().getColumnDefinition(pColumnName));
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Checks if the given {@link IDataType} is holding a string.
	 * 
	 * @param pDataType the {@link IDataType}.
	 * @return {@code true} if the given datatype holds a string.
	 */
	public static boolean isStringDataType(IDataType pDataType)
	{
		return String.class.isAssignableFrom(pDataType.getTypeClass());
	}
	
	/**
	 * Checks if the given {@link ColumnDefinition} is holding a timestamp.
	 * 
	 * @param pColumnDefinition the {@link ColumnDefinition}.
	 * @return {@code true} if the given column holds a timestamp.
	 */
	public static boolean isTimestampColumn(ColumnDefinition pColumnDefinition)
	{
		return isTimestampDataType(pColumnDefinition.getDataType());
	}
	
	/**
	 * Checks if the given column from the given {@link IDataRow} is holding a
	 * timestamp.
	 * 
	 * @param pDataRow the {@link IDataRow}.
	 * @param pColumnName the column name.
	 * @return {@code true} if the given column holds a timestamp.
	 */
	public static boolean isTimestampColumn(IDataRow pDataRow, String pColumnName)
	{
		try
		{
			return isTimestampColumn(pDataRow.getRowDefinition().getColumnDefinition(pColumnName));
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Checks if the given {@link IDataType} is holding a timestamp.
	 * 
	 * @param pDataType the {@link IDataType}.
	 * @return {@code true} if the given datatype holds a timestamp.
	 */
	public static boolean isTimestampDataType(IDataType pDataType)
	{
		return Timestamp.class.isAssignableFrom(pDataType.getTypeClass());
	}
	
	/**
	 * Sets the default {@link ICellEditor} for the given class.
	 * 
	 * @param pClass the class.
	 * @param pCellEditor the {@link ICellEditor}.
	 */
	public static void setDefaultCellEditor(Class<?> pClass, ICellEditor pCellEditor)
	{
		defaultCellEditors.put(pClass, pCellEditor);
	}
	
	/**
	 * Initializes the default {@link ICellEditors}.
	 */
	private static void initDefaultCellEditors()
	{
		defaultCellEditors = new HashMap<>();
		
		setDefaultCellEditor(new TimestampDataType().getTypeClass(), new FXDateCellEditor());
		setDefaultCellEditor(new BigDecimalDataType().getTypeClass(), new FXNumberCellEditor());
	}
	
}	// FXControlUtil
