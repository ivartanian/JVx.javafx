/*
 * Copyright 2014 SIB Visions GmbH
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.ui.IDimension;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.IInsets;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;
import javax.rad.ui.InvokeLaterThread;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;
import javax.rad.ui.celleditor.IChoiceCellEditor;
import javax.rad.ui.celleditor.IDateCellEditor;
import javax.rad.ui.celleditor.IImageViewer;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.ui.celleditor.INumberCellEditor;
import javax.rad.ui.celleditor.ITextCellEditor;
import javax.rad.ui.component.IButton;
import javax.rad.ui.component.ICheckBox;
import javax.rad.ui.component.IIcon;
import javax.rad.ui.component.ILabel;
import javax.rad.ui.component.IPasswordField;
import javax.rad.ui.component.IRadioButton;
import javax.rad.ui.component.ITextArea;
import javax.rad.ui.component.ITextField;
import javax.rad.ui.component.IToggleButton;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IGroupPanel;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.container.IPanel;
import javax.rad.ui.container.IScrollPanel;
import javax.rad.ui.container.ISplitPanel;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.container.IWindow;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.IChart;
import javax.rad.ui.control.IEditor;
import javax.rad.ui.control.ITable;
import javax.rad.ui.control.ITree;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIKeyEvent;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.ui.layout.IBorderLayout;
import javax.rad.ui.layout.IFlowLayout;
import javax.rad.ui.layout.IFormLayout;
import javax.rad.ui.layout.IGridLayout;
import javax.rad.ui.menu.ICheckBoxMenuItem;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;
import javax.rad.ui.menu.ISeparator;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import com.sibvisions.rad.ui.AbstractFactory;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXCheckBoxCellEditor;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXChoiceCellEditor;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXDateCellEditor;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXImageViewer;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXLinkedCellEditor;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXNumberCellEditor;
import com.sibvisions.rad.ui.javafx.ext.celleditor.FXTextCellEditor;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXAbstractComponentBase;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXButton;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXCheckBox;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXComponent;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXIcon;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXLabel;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXPasswordField;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXRadioButton;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXTextArea;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXTextField;
import com.sibvisions.rad.ui.javafx.impl.component.JavaFXToggleButton;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXControlContainer;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXDesktopPanel;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXFrame;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXGroupPanel;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXInternalFrame;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXPanel;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXScrollPanel;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXSplitPanel;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXTabsetPanel;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXToolBar;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXToolBarPanel;
import com.sibvisions.rad.ui.javafx.impl.control.JavaFXCellFormat;
import com.sibvisions.rad.ui.javafx.impl.control.JavaFXChart;
import com.sibvisions.rad.ui.javafx.impl.control.JavaFXEditor;
import com.sibvisions.rad.ui.javafx.impl.control.JavaFXTable;
import com.sibvisions.rad.ui.javafx.impl.control.JavaFXTree;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXBorderLayout;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXFlowLayout;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXFormLayout;
import com.sibvisions.rad.ui.javafx.impl.layout.JavaFXGridLayout;
import com.sibvisions.rad.ui.javafx.impl.menu.JavaFXCheckBoxMenuItem;
import com.sibvisions.rad.ui.javafx.impl.menu.JavaFXMenu;
import com.sibvisions.rad.ui.javafx.impl.menu.JavaFXMenuBar;
import com.sibvisions.rad.ui.javafx.impl.menu.JavaFXMenuItem;
import com.sibvisions.rad.ui.javafx.impl.menu.JavaFXSeparator;
import com.sibvisions.rad.ui.javafx.impl.util.JavaFXColorUtil;
import com.sibvisions.util.ThreadHandler;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.ResourceUtil;

/**
 * The {@link JavaFXFactory} is the JavaFX specific implementation of
 * {@link javax.rad.ui.IFactory}.
 * 
 * @author Robert Zenz
 * @see javax.rad.ui.IFactory
 * @see AbstractFactory
 */
public class JavaFXFactory extends AbstractFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Map} of default cell editors. */
	private Map<Class<?>, ICellEditor> cellEditors;
	
	/** The {@link Map} of image mappings. */
	private Map<String, String> imageMappings;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXFactory}.
	 */
	public JavaFXFactory()
	{
		super();
		
		cellEditors = new HashMap<>();
		imageMappings = new HashMap<>();
		
		JavaFXPlatformStarter.start();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBorderLayout createBorderLayout()
	{
		return new JavaFXBorderLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IButton createButton()
	{
		return setFactoryOnComponent(new JavaFXButton());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellFormat createCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, int pLeftIndent)
	{
		return new JavaFXCellFormat(pBackground, pForeground, pFont, pImage, pLeftIndent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IChart createChart()
	{
		return setFactoryOnComponent(new JavaFXChart());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICheckBox createCheckBox()
	{
		return setFactoryOnComponent(new JavaFXCheckBox());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICheckBoxCellEditor<?> createCheckBoxCellEditor()
	{
		return new FXCheckBoxCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICheckBoxMenuItem createCheckBoxMenuItem()
	{
		return setFactoryOnComponent(new JavaFXCheckBoxMenuItem());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IChoiceCellEditor<?> createChoiceCellEditor()
	{
		return new FXChoiceCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IColor createColor(int pRGBA)
	{
		return new JavaFXColor(pRGBA);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IComponent createCustomComponent(Object pCustomComponent)
	{
		JavaFXComponent<Region> comp = setFactoryOnComponent(new JavaFXComponent<>((Region)pCustomComponent));
		// We don't want to clip custom components.
		comp.setClippingEnabled(false);
		
		return comp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IContainer createCustomContainer(Object pCustomContainer)
	{
		if (pCustomContainer instanceof Control)
		{
			// Makes sense in some circumstances, e.g. ListView is a control but acts like a container.
			return setFactoryOnComponent(new JavaFXControlContainer((Control)pCustomContainer));
		}
		else
		{
			return setFactoryOnComponent(new JavaFXPanel((Pane)pCustomContainer));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDateCellEditor createDateCellEditor()
	{
		return new FXDateCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDesktopPanel createDesktopPanel()
	{
		return setFactoryOnComponent(new JavaFXDesktopPanel());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDimension createDimension(int pWidth, int pHeight)
	{
		return new JavaFXDimension(pWidth, pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IEditor createEditor()
	{
		return setFactoryOnComponent(new JavaFXEditor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFlowLayout createFlowLayout()
	{
		return new JavaFXFlowLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFont createFont(String pName, int pStyle, int pSize)
	{
		return new JavaFXFont(pName, pStyle, pSize);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFormLayout createFormLayout()
	{
		return new JavaFXFormLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFrame createFrame()
	{
		return setFactoryOnComponent(new JavaFXFrame());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGridLayout createGridLayout(int columns, int rows)
	{
		return new JavaFXGridLayout(columns, rows);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGroupPanel createGroupPanel()
	{
		return setFactoryOnComponent(new JavaFXGroupPanel());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IIcon createIcon()
	{
		return setFactoryOnComponent(new JavaFXIcon());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImageViewer<?> createImageViewer()
	{
		return new FXImageViewer();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IInsets createInsets(int pTop, int pLeft, int pBottom, int pRight)
	{
		return new JavaFXInsets(pTop, pLeft, pBottom, pRight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IInternalFrame createInternalFrame(IDesktopPanel pDesktop)
	{
		return setFactoryOnComponent(new JavaFXInternalFrame(pDesktop));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILabel createLabel()
	{
		return setFactoryOnComponent(new JavaFXLabel());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILinkedCellEditor createLinkedCellEditor()
	{
		return new FXLinkedCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMenu createMenu()
	{
		return setFactoryOnComponent(new JavaFXMenu());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMenuBar createMenuBar()
	{
		return setFactoryOnComponent(new JavaFXMenuBar());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMenuItem createMenuItem()
	{
		return setFactoryOnComponent(new JavaFXMenuItem());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public INumberCellEditor createNumberCellEditor()
	{
		return new FXNumberCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPanel createPanel()
	{
		return setFactoryOnComponent(new JavaFXPanel());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPasswordField createPasswordField()
	{
		return setFactoryOnComponent(new JavaFXPasswordField());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPoint createPoint(int pX, int pY)
	{
		return new JavaFXPoint(pX, pY);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPopupMenu createPopupMenu()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRadioButton createRadioButton()
	{
		return setFactoryOnComponent(new JavaFXRadioButton());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRectangle createRectangle(int pX, int pY, int pWidth, int pHeight)
	{
		return new JavaFXRectangle(pX, pY, pWidth, pHeight);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IScrollPanel createScrollPanel()
	{
		return setFactoryOnComponent(new JavaFXScrollPanel());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISeparator createSeparator()
	{
		return setFactoryOnComponent(new JavaFXSeparator());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISplitPanel createSplitPanel()
	{
		return setFactoryOnComponent(new JavaFXSplitPanel());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITable createTable()
	{
		return setFactoryOnComponent(new JavaFXTable());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITabsetPanel createTabsetPanel()
	{
		return setFactoryOnComponent(new JavaFXTabsetPanel());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITextArea createTextArea()
	{
		return setFactoryOnComponent(new JavaFXTextArea());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITextCellEditor createTextCellEditor()
	{
		return new FXTextCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITextField createTextField()
	{
		return setFactoryOnComponent(new JavaFXTextField());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IToggleButton createToggleButton()
	{
		return setFactoryOnComponent(new JavaFXToggleButton());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IToolBar createToolBar()
	{
		return setFactoryOnComponent(new JavaFXToolBar());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IToolBarPanel createToolBarPanel()
	{
		return setFactoryOnComponent(new JavaFXToolBarPanel());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITree createTree()
	{
		return setFactoryOnComponent(new JavaFXTree());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IWindow createWindow()
	{
		// TODO Creation of IWindow is currently not supported.
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getAvailableFontFamilyNames()
	{
		List<String> fontFamilies = Font.getFamilies();
		return Font.getFamilies().toArray(new String[fontFamilies.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellEditor getDefaultCellEditor(Class<?> pClass)
	{
		return cellEditors.get(pClass);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getImage(String pImageName)
	{
		InputStream stream = null;
		
		try
		{
			stream = ResourceUtil.getResourceAsStream(pImageName);
			
			if (stream != null)
			{
				return new JavaFXImage(pImageName, new Image(stream));
			}
		}
		finally
		{
			CommonUtil.close(stream);
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IImage getImage(String pImageName, byte[] pData)
	{
		InputStream stream = null;
		
		try
		{
			stream = new ByteArrayInputStream(pData);
			
			return new JavaFXImage(pImageName, new Image(stream));
		}
		finally
		{
			CommonUtil.close(stream);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getImageMapping(String pMappingName)
	{
		String imageName = imageMappings.get(pMappingName);
		
		if (imageName != null)
		{
			return imageName;
		}
		
		return pMappingName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getImageMappingNames()
	{
		return imageMappings.keySet().toArray(new String[imageMappings.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICursor getPredefinedCursor(int pType)
	{
		return JavaFXCursor.getCursor(pType);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IColor getSystemColor(String pType)
	{
		return JavaFXColorUtil.getSystemColor(pType);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICursor getSystemCustomCursor(String pCursorName)
	{
		String cursorName = pCursorName;
		int dotIndex = pCursorName.indexOf('.');
		
		if (dotIndex >= 0)
		{
			cursorName = cursorName.substring(0, dotIndex);
		}
		
		cursorName = cursorName.toUpperCase();
		
		return JavaFXCursor.getCursor(cursorName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invokeAndWait(Runnable pRunnable) throws Exception
	{
		if (Platform.isFxApplicationThread())
		{
			pRunnable.run();
		}
		else
		{
			// Using a FutureTask is the easiest method of waiting for something
			// to complete if it is run later.
			FutureTask<Object> task = new FutureTask<>(() ->
			{
				pRunnable.run();
				return null;
			});
			
			Platform.runLater(task);
			
			// Now wait for the task.
			task.get();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Thread invokeInThread(Runnable pRunnable)
	{
		return ThreadHandler.start(new InvokeLaterThread(this, pRunnable));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invokeLater(Runnable pRunnable)
	{
		Platform.runLater(pRunnable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultCellEditor(Class<?> pClass, ICellEditor pCellEditor)
	{
		cellEditors.put(pClass, pCellEditor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setImageMapping(String pMappingName, String pImageName)
	{
		imageMappings.put(pMappingName, pImageName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSystemColor(String pType, IColor pSystemColor)
	{
		JavaFXColorUtil.addSystemColor(pType, pSystemColor);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Compares the given {@link KeyEvent} with the given {@link Key}.
	 * 
	 * @param pKeyEvent the {@link KeyEvent}.
	 * @param pKey the {@link Key}.
	 * @return {@code true} if both can be considered equal.
	 */
	public static boolean compare(KeyEvent pKeyEvent, Key pKey)
	{
		KeyCode keyCode = keyToKeyCode(pKey);
		
		boolean ctrlRequired = (pKey.getModifiers() & UIKeyEvent.CTRL_MASK) == UIKeyEvent.CTRL_MASK;
		boolean altRequired = (pKey.getModifiers() & UIKeyEvent.ALT_MASK) == UIKeyEvent.ALT_MASK;
		boolean shiftRequired = (pKey.getModifiers() & UIKeyEvent.SHIFT_MASK) == UIKeyEvent.SHIFT_MASK;
		
		if (pKeyEvent.getCode() == keyCode
				&& ctrlRequired == pKeyEvent.isControlDown()
				&& altRequired == pKeyEvent.isAltDown()
				&& shiftRequired == pKeyEvent.isShiftDown())
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Creates an {@link UIKeyEvent} from the given information.
	 * 
	 * @param pEventSource the {@link IComponent event source}.
	 * @param pId the ID of the event.
	 * @param pKeyEvent the {@link KeyEvent} that is the basis.
	 * @return the {@link UIKeyEvent}.
	 */
	public static UIKeyEvent getUIKeyEvent(IComponent pEventSource, int pId, KeyEvent pKeyEvent)
	{
		int modifiers = 0;
		
		if (pKeyEvent.isAltDown())
		{
			modifiers = modifiers | UIKeyEvent.ALT_MASK;
		}
		if (pKeyEvent.isControlDown())
		{
			modifiers = modifiers | UIKeyEvent.CTRL_MASK;
		}
		if (pKeyEvent.isShiftDown())
		{
			modifiers = modifiers | UIKeyEvent.SHIFT_MASK;
		}
		
		KeyCode keyCode = pKeyEvent.getCode();
		
		// TODO There is no public API to get the character.
		@SuppressWarnings("deprecation")
		char character = keyCode.impl_getChar().charAt(0);
		
		if (keyCode == KeyCode.UNDEFINED)
		{
			character = pKeyEvent.getCharacter().charAt(0);
		}
		
		if (!pKeyEvent.isShiftDown() && keyCode.isLetterKey())
		{
			character = Character.toLowerCase(character);
		}
		
		int code = String.valueOf(character).codePointAt(0);
		
		return new UIKeyEvent(
				pEventSource,
				pId,
				System.currentTimeMillis(),
				modifiers,
				code,
				character);
	}
	
	/**
	 * Creates an {@link UIMouseEvent} from the given information.
	 * 
	 * @param pEventSource the {@link IComponent event source}.
	 * @param pId the ID of the event.
	 * @param pMouseEvent the {@link MouseEvent} that is the basis.
	 * @return the {@link UIMouseEvent}.
	 */
	public static UIMouseEvent getUIMouseEvent(IComponent pEventSource, int pId, MouseEvent pMouseEvent)
	{
		int modifiers = 0;
		
		if (pMouseEvent.isAltDown())
		{
			modifiers = modifiers | UIKeyEvent.ALT_MASK;
		}
		if (pMouseEvent.isControlDown())
		{
			modifiers = modifiers | UIKeyEvent.CTRL_MASK;
		}
		if (pMouseEvent.isShiftDown())
		{
			modifiers = modifiers | UIKeyEvent.SHIFT_MASK;
		}
		
		return new UIMouseEvent(
				pEventSource,
				pId,
				System.currentTimeMillis(),
				0,
				(int)pMouseEvent.getSceneX(),
				(int)pMouseEvent.getSceneY(),
				pMouseEvent.getClickCount(),
				pMouseEvent.isPopupTrigger());
	}
	
	/**
	 * Converts the given {@link Key} to a {@link KeyCode}.
	 * 
	 * @param pKey the {@link Key} to convert.
	 * @return the {@link KeyCode}.
	 */
	public static KeyCode keyToKeyCode(Key pKey)
	{
		if (pKey == null)
		{
			return null;
		}
		
		// TODO Converting a Key to a KeyCode is at the moment "problematic" at best.
		
		if (Character.isAlphabetic(pKey.getKeyChar()))
		{
			return KeyCode.getKeyCode(Character.toString(pKey.getKeyChar()).toUpperCase());
		}
		
		switch (pKey.getKeyCode())
		{
			case 112:
				return KeyCode.F1;
				
			case 113:
				return KeyCode.F2;
				
			case 114:
				return KeyCode.F3;
				
			case 115:
				return KeyCode.F4;
				
			case 116:
				return KeyCode.F5;
				
			case 117:
				return KeyCode.F6;
				
			case 118:
				return KeyCode.F7;
				
			case 119:
				return KeyCode.F8;
				
			case 120:
				return KeyCode.F9;
				
			case 121:
				return KeyCode.F10;
				
			case 122:
				return KeyCode.F11;
				
			case 123:
				return KeyCode.F12;
				
			default:
				// Ignore.
				
		}
		
		return null;
	}
	
	/**
	 * Converts the given {@link Key} to a {@link KeyCombination}.
	 * 
	 * @param pKey the {@link Key} to convert.
	 * @return the {@link KeyCombination}.
	 */
	public static KeyCombination keyToKeyCombination(Key pKey)
	{
		if (pKey == null)
		{
			return null;
		}
		
		KeyCode keyCode = keyToKeyCode(pKey);
		
		ModifierValue altModifier = ModifierValue.UP;
		ModifierValue ctrlModifier = ModifierValue.UP;
		ModifierValue shiftModifier = ModifierValue.UP;
		
		if ((pKey.getModifiers() & UIKeyEvent.ALT_MASK) == UIKeyEvent.ALT_MASK)
		{
			altModifier = ModifierValue.DOWN;
		}
		
		if ((pKey.getModifiers() & UIKeyEvent.CTRL_MASK) == UIKeyEvent.CTRL_MASK)
		{
			ctrlModifier = ModifierValue.DOWN;
		}
		
		if ((pKey.getModifiers() & UIKeyEvent.SHIFT_MASK) == UIKeyEvent.SHIFT_MASK)
		{
			shiftModifier = ModifierValue.DOWN;
		}
		
		return new KeyCodeCombination(keyCode, ctrlModifier, shiftModifier, altModifier, ModifierValue.UP, ModifierValue.UP);
	}
	
	/**
	 * Converts the given {@link Pos} the an {@link IAlignmentConstants}
	 * constant.
	 * 
	 * @param pPos the {@link Pos} to convert.
	 * @return one of the {@link IAlignmentConstants}.
	 * @see IAlignmentConstants#ALIGN_BOTTOM
	 * @see IAlignmentConstants#ALIGN_CENTER
	 * @see IAlignmentConstants#ALIGN_DEFAULT
	 * @see IAlignmentConstants#ALIGN_LEFT
	 * @see IAlignmentConstants#ALIGN_RIGHT
	 * @see IAlignmentConstants#ALIGN_STRETCH
	 * @see IAlignmentConstants#ALIGN_TOP
	 */
	public static int posToAlignment(Pos pPos)
	{
		int alignment = posToHorizontalAlignment(pPos);
		
		if (alignment == IAlignmentConstants.ALIGN_DEFAULT)
		{
			return posToVerticalAlignment(pPos);
		}
		
		return alignment;
	}
	
	/**
	 * Converts the given {@link Pos} the a horizontal
	 * {@link IAlignmentConstants} constant.
	 * 
	 * @param pPos the {@link Pos} to convert.
	 * @return one of the horizontal {@link IAlignmentConstants}.
	 * @see IAlignmentConstants#ALIGN_CENTER
	 * @see IAlignmentConstants#ALIGN_DEFAULT
	 * @see IAlignmentConstants#ALIGN_LEFT
	 * @see IAlignmentConstants#ALIGN_RIGHT
	 */
	public static int posToHorizontalAlignment(Pos pPos)
	{
		switch (pPos.getHpos())
		{
			case CENTER:
				return IAlignmentConstants.ALIGN_CENTER;
				
			case LEFT:
				return IAlignmentConstants.ALIGN_LEFT;
				
			case RIGHT:
				return IAlignmentConstants.ALIGN_RIGHT;
				
			default:
				return IAlignmentConstants.ALIGN_DEFAULT;
				
		}
	}
	
	/**
	 * Converts the given {@link Pos} the a vertical {@link IAlignmentConstants}
	 * constant.
	 * 
	 * @param pPos the {@link Pos} to convert.
	 * @return one of the vertical {@link IAlignmentConstants}.
	 * @see IAlignmentConstants#ALIGN_BOTTOM
	 * @see IAlignmentConstants#ALIGN_CENTER
	 * @see IAlignmentConstants#ALIGN_DEFAULT
	 * @see IAlignmentConstants#ALIGN_TOP
	 */
	public static int posToVerticalAlignment(Pos pPos)
	{
		switch (pPos.getVpos())
		{
			case BASELINE:
				return IAlignmentConstants.ALIGN_BOTTOM;
				
			case BOTTOM:
				return IAlignmentConstants.ALIGN_BOTTOM;
				
			case CENTER:
				return IAlignmentConstants.ALIGN_CENTER;
				
			case TOP:
				return IAlignmentConstants.ALIGN_TOP;
				
			default:
				return IAlignmentConstants.ALIGN_DEFAULT;
				
		}
	}
	
	/**
	 * Creates a dialog to show an exception.
	 * 
	 * @param pThrowable the exception
	 */
	public static void showError(Throwable pThrowable)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Application error occured!");
		alert.setContentText(pThrowable.getMessage());
		
		Label label = new Label("The exception stacktrace:");
		
		TextArea textArea = new TextArea(ExceptionUtil.dump(pThrowable, true));
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		GridPane gpanContent = new GridPane();
		gpanContent.add(label, 0, 0);
		gpanContent.add(textArea, 0, 1);
		
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		
		alert.getDialogPane().setExpandableContent(gpanContent);
		alert.getDialogPane().setPrefWidth(630);
		alert.getDialogPane().setExpanded(true);
		alert.getDialogPane().expandedProperty().addListener((c, o, n) ->
		{
			if (n.booleanValue())
			{
				alert.getDialogPane().setPrefHeight(0);
			}
			else
			{
				alert.getDialogPane().setPrefHeight(400);
			}
		});
		alert.showAndWait();
	}
	
	/**
	 * Sets this factory on the given component and returns the component.
	 * 
	 * @param <T> the type of the component.
	 * @param pComponent the component to set this factory on.
	 * @return the component.
	 */
	protected <T extends JavaFXAbstractComponentBase<?>> T setFactoryOnComponent(T pComponent)
	{
		pComponent.setFactory(this);
		return pComponent;
	}
	
}	// JavaFXFactory
