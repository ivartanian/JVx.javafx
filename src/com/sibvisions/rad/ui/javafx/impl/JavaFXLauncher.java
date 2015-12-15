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

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.rad.application.IApplication;
import javax.rad.application.IFileHandleReceiver;
import javax.rad.application.ILauncher;
import javax.rad.application.genui.UILauncher;
import javax.rad.io.FileHandle;
import javax.rad.io.IFileHandle;
import javax.rad.ui.IFactory;
import javax.rad.ui.IRectangle;
import javax.rad.ui.layout.IBorderLayout;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.ApplicationUtil;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXFrame;
import com.sibvisions.rad.ui.javafx.impl.container.JavaFXPanel;
import com.sibvisions.util.FileViewer;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.FileUtil;

/**
 * The {@link JavaFXLauncher} is the JavaFX specific implementation of
 * {@link ILauncher}.
 * 
 * @author Robert Zenz
 * @see ILauncher
 */
public class JavaFXLauncher extends JavaFXFrame
                            implements ILauncher
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The property name for system-exit-on-error (boolean). */
	public static final String ARG_SYSTEMEXIT_ON_ERROR = "systemExitOnError";
	
	/** The property name of a custom application CSS file. */
	public static final String PARAM_APPLICATIONCSS = "applicationCss";
	
	/** The property name for a custom location and size. */
	public static final String PARAM_FRAMEBOUNDS = "framebounds";
	
	/** The {@link IApplication}. */
	private IApplication application;
	
	/** The configuration. */
	private ApplicationConfig config;
	
	/** The JavaFX application. */
	private Application javafxApplication;
	
	/** The launcher. */
	private UILauncher uilauncher;
	
	/** If {@code System.exit(0)} should be executed when disposing. */
	private boolean systemExitOnDispose = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXLauncher}.
	 *
	 * @param pApplication the {@link Application}.
	 * @param pStage the primary {@link Stage}.
	 * @param pFactory the {@link IFactory}.
	 * @param pParameter the {@link ApplicationParameters}.
	 */
	public JavaFXLauncher(Application pApplication, Stage pStage, IFactory pFactory, ApplicationParameters pParameter)
	{
		super(pStage);
		
		javafxApplication = pApplication;
		
		setFactory(pFactory);
		
		config = createConfig();
		config.configure(pParameter);
		
		applyCustomCSS();
		
		uilauncher = createUILauncher();
		
		// Use setParameter to remove the parameter, so that no one else after
		// us receives it.
		String sExitOnError = pParameter.setParameter(ARG_SYSTEMEXIT_ON_ERROR, null);
		boolean bExitOnError = Boolean.parseBoolean(sExitOnError);
		
		try
		{
			application = ApplicationUtil.createApplication(uilauncher, pParameter.getApplicationClassName());
			uilauncher.setTitle(application.getName());
			uilauncher.add(application, IBorderLayout.CENTER);
			
			if (!hasCustomSize())
			{
				uilauncher.pack();
			}
		}
		catch (Throwable th)
		{
			LoggerFactory.getInstance(JavaFXLauncher.class).error(th);
			
			// Add dummy content, for compatibility reason (e.g. custom handleException).
			toolBarPanel.add(new JavaFXPanel(new StackPane()), IBorderLayout.CENTER);
			
			if (!hasCustomSize())
			{
				pStage.setWidth(800);
				pStage.setHeight(600);
				
				pStage.centerOnScreen();
				
				uilauncher.pack();
			}
			
			uilauncher.setVisible(true);
			
			uilauncher.handleException(th);
			
			if (bExitOnError)
			{
				System.exit(1);
			}
			else
			{
				return;
			}
		}
		
		eventWindowClosing().setDefaultListener(this, "dispose");
		
		int iState = getState();
		
		if (iState == MAXIMIZED_BOTH
				|| iState == MAXIMIZED_HORIZ
				|| iState == MAXIMIZED_VERT)
		{
			setState(iState);
		}
		
		if (!resizeRelocate(pStage))
		{
			pStage.centerOnScreen();
		}
		
		uilauncher.setVisible(true);
		
		try
		{
			getFactory().invokeAndWait(() -> application.notifyVisible());
		}
		catch (Exception e)
		{
			LoggerFactory.getInstance(JavaFXLauncher.class).error(e);
			
			if (bExitOnError)
			{
				System.exit(1);
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelPendingThreads()
	{
		// Nothing to do.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		if (application != null)
		{
			try
			{
				application.notifyDestroy();
			}
			catch (Exception e)
			{
				// Ignore any exception and close the application anyway.
				LoggerFactory.getInstance(getClass()).info("Forced application destroy failed", e);
			}
		}
		
		super.dispose();
		
		Platform.exit();
		
		if (systemExitOnDispose)
		{
			System.exit(0);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEnvironmentName()
	{
		return ILauncher.ENVIRONMENT_DESKTOP;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getFileHandle(IFileHandleReceiver pFileHandleReceiver, String pTitle) throws Throwable
	{
		FileChooser fileChooser = new FileChooser();
		
		if (pTitle != null)
		{
			fileChooser.setTitle(pTitle);
		}
		
		List<File> files = fileChooser.showOpenMultipleDialog(getStage());
		
		if (files != null && !files.isEmpty())
		{
			for (File file : files)
			{
				pFileHandleReceiver.receiveFileHandle(new FileHandle(file));
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getParameter(String pName)
	{
		return config.getParameter(pName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRegistryKey(String pKey)
	{
		return config.getRegistryKey(pKey);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getResource()
	{
		return javafxApplication;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleException(Throwable pThrowable)
	{
		JavaFXFactory.showError(pThrowable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveFileHandle(IFileHandle pFileHandle, String pTitle) throws Throwable
	{
		FileChooser fileChooser = new FileChooser();
		
		if (pTitle != null)
		{
			fileChooser.setTitle(pTitle);
		}
		
		File file = fileChooser.showSaveDialog(getStage());
		
		if (file != null)
		{
			FileUtil.copy(pFileHandle.getInputStream(), true, new FileOutputStream(file), true);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameter(String pName, String pValue)
	{
		config.setParameter(pName, pValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRegistryKey(String pKey, String pValue)
	{
		config.setRegistryKey(pKey, pValue);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDocument(String pDocumentName, IRectangle pBounds, String pTarget) throws Throwable
	{
		FileViewer.open(pDocumentName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showFileHandle(IFileHandle pFileHandle) throws Throwable
	{
		showFileHandle(pFileHandle, null, "_blank");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showFileHandle(IFileHandle pFileHandle, IRectangle pBounds, String pTarget) throws Throwable
	{
		File file = null;
		
		if (pFileHandle instanceof FileHandle)
		{
			file = ((FileHandle)pFileHandle).getFile();
		}
		
		if (file == null)
		{
			file = FileUtil.getNotExistingFile(new File(System.getProperty("java.io.tmpdir"), pFileHandle.getFileName()));
			file.deleteOnExit();
			
			FileUtil.copy(pFileHandle.getInputStream(), true, new FileOutputStream(file), true);
		}
		
		FileViewer.open(file);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Applies a user-defined/custom stylesheet for the scene. The css file can
	 * be configured via launcher parameter {@link #PARAM_APPLICATIONCSS}.
	 */
	protected void applyCustomCSS()
	{
		String sCustomCSS = config.getParameter(PARAM_APPLICATIONCSS);
		
		if (sCustomCSS != null && !sCustomCSS.isEmpty())
		{
			getScene().getStylesheets().add(sCustomCSS);
		}
	}
	
	/**
	 * Creates the {@link ApplicationConfig}.
	 * 
	 * @return the {@link ApplicationConfig}.
	 */
	protected ApplicationConfig createConfig()
	{
		return new ApplicationConfig(this);
	}
	
	/**
	 * Creates the {@link UILauncher}.
	 * 
	 * @return the {@link UILauncher}.
	 */
	protected UILauncher createUILauncher()
	{
		return new UILauncher(this);
	}
	
	/**
	 * Gets the {@link UILauncher}.
	 * 
	 * @return the {@link UILauncher}
	 */
	protected UILauncher getUILauncher()
	{
		return uilauncher;
	}
	
	/**
	 * Resizes and relocates the {@link Stage} to the provided values in the
	 * {@link #PARAM_FRAMEBOUNDS} property, if any.
	 * 
	 * @param pStage the {@link Stage} to resize and relocate.
	 * @return {@code true} if resize and relocate was configured, {@code false}
	 *         otherwise.
	 */
	private boolean resizeRelocate(Stage pStage)
	{
		try
		{
			String bounds = System.getProperty(PARAM_FRAMEBOUNDS);
			
			if (bounds != null)
			{
				try
				{
					String[] splittedBounds = bounds.split(",");
					
					pStage.setX(Double.parseDouble(splittedBounds[0].trim()));
					pStage.setY(Double.parseDouble(splittedBounds[1].trim()));
					pStage.setWidth(Double.parseDouble(splittedBounds[2].trim()));
					pStage.setHeight(Double.parseDouble(splittedBounds[3].trim()));
					
					return true;
				}
				catch (Exception ex)
				{
					LoggerFactory.getInstance(JavaFXLauncher.class).error("Can't set frame bounds \"" + bounds + "\"!", ex);
				}
			}
		}
		catch (Exception se)
		{
			// Access not allowed.
		}
		
		return false;
	}
	
}	// JavaFXLauncher
