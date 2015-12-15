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
package com.sibvisions.rad.ui.javafx.ext.skin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import com.sibvisions.rad.ui.javafx.ext.FXCustomComboBox;
import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;

/**
 * The default skin for the {@link FXCustomComboBox}.
 * 
 * @param <T> The type of the value.
 * @author Robert Zenz
 */
public class FXCustomComboBoxSkin<T> extends ComboBoxPopupControl<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link TextField} that is used for displaying the value. */
	private TextField displayTextField;
	
	/** The cached {@link Node} that is the popup. */
	private Node popupContent;
	
	/** The container for the popup {@link Node}. */
	private Pane popupContainer;
	
	/** The container for the content of the popup. */
	private BorderPane popupContentContainer;
	
	/** The {@link TextField} in the popup. */
	private TextField popupTextField;
	
	/** The skinnable element. */
	private FXCustomComboBox<T> skinnable;
	
	/** The listener that is invoked if the value of the provider changes. */
	private ChangeListener<T> valueChanged;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FXCustomComboBoxSkin}.
	 *
	 * @param pComboBox the combo box.
	 * @param pBehavior the behavior.
	 */
	public FXCustomComboBoxSkin(FXCustomComboBox<T> pComboBox, ComboBoxBaseBehavior<T> pBehavior)
	{
		super(pComboBox, pBehavior);
		
		valueChanged = this::onValueChanged;
		
		skinnable = pComboBox;
		
		displayTextField = new TextField();
		displayTextField.textProperty().addListener(this::onTextChanged);
		displayTextField.editableProperty().bind(skinnable.editableProperty());
		
		popupContainer = new StackPane();
		popupContainer.getStyleClass().add("popup");
		popupContainer.getStylesheets().add(FXCustomComboBox.DEFAULT_STYLE);
		
		popupContentContainer = new BorderPane();
		popupContentContainer.getStyleClass().add("popup-content-container");
		popupContentContainer.getStylesheets().add(FXCustomComboBox.DEFAULT_STYLE);
		
		popupTextField = new TextField("Value");
		popupTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
		popupTextField.textProperty().bindBidirectional(displayTextField.textProperty());
		
		Pane topContainer = new Pane(popupTextField);
		
		popupContentContainer.setTop(topContainer);
		popupContainer.getChildren().add(popupContentContainer);
		
		BorderPane.setMargin(topContainer, new Insets(0, 0, 8, 0));
		
		skinnable.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
		skinnable.valueProperty().addListener(valueChanged);
		
		onValueChanged(null, null, skinnable.getValue());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		skinnable.valueProperty().removeListener(valueChanged);
		
		super.dispose();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node getDisplayNode()
	{
		return displayTextField;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PopupControl getPopup()
	{
		if (popup == null)
		{
			super.getPopup();
			
			popup.addEventFilter(KeyEvent.KEY_PRESSED, this::onPopupKeyPressed);
		}
		
		return super.getPopup();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hide()
	{
		super.hide();
		
		if (skinnable.getPopupProvider() != null)
		{
			skinnable.getPopupProvider().hidePopup();
		}
		
		popupContentContainer.setCenter(null);
		popupContent = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void show()
	{
		if (skinnable.getPopupProvider() != null)
		{
			popupContent = skinnable.getPopupProvider().showPopup();
			popupContentContainer.setCenter(popupContent);
		}
		
		popupTextField.setPrefSize(skinnable.getWidth(), displayTextField.getHeight());
		
		super.show();
		
		repositionPopup();
		
		popupTextField.requestFocus();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Node getPopupContent()
	{
		return popupContainer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren(double pX, double pY, double pW, double pH)
	{
		super.layoutChildren(pX, pY, pW, pH);
		
		repositionPopup();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TextField getEditor()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StringConverter<T> getConverter()
	{
		return null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked if a key is pressed on the popup.
	 * <p>
	 * Closes the popup if necessary.
	 * 
	 * @param pKeyEvent the event.
	 */
	private void onPopupKeyPressed(KeyEvent pKeyEvent)
	{
		if ((pKeyEvent.getCode() == KeyCode.ESCAPE && ((FXCustomComboBox<?>)getSkinnable()).isClosePopupOnEscapeKey())
				|| (pKeyEvent.getCode() == KeyCode.ENTER && ((FXCustomComboBox<?>)getSkinnable()).isClosePopupOnEnterKey()))
		{
			pKeyEvent.consume();
			getSkinnable().hide();
		}
	}
	
	/**
	 * Invoked if a key is pressed on the {@link #popupTextField} or
	 * {@link #displayTextField}.
	 * <p>
	 * Filters the up and down keys and forwards that to the provider.
	 * 
	 * @param pKeyEvent the event.
	 */
	private void onKeyPressed(KeyEvent pKeyEvent)
	{
		if ((pKeyEvent.getCode() == KeyCode.DOWN || pKeyEvent.getCode() == KeyCode.UP) && !skinnable.isShowing())
		{
			pKeyEvent.consume();
			skinnable.show();
			return;
		}
		
		if (pKeyEvent.getCode() == KeyCode.DOWN)
		{
			pKeyEvent.consume();
			
			skinnable.getPopupProvider().selectNext();
		}
		else if (pKeyEvent.getCode() == KeyCode.UP)
		{
			pKeyEvent.consume();
			
			skinnable.getPopupProvider().selectPrevious();
		}
	}
	
	/**
	 * Invoked if the text of the {@link #displayTextField} changes.
	 * <p>
	 * Propagates the change to the {@link FXAbstractPopupProvider}.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onTextChanged(ObservableValue<? extends String> pObservable, String pOldValue, String pNewValue)
	{
		if (skinnable.getPopupProvider() != null)
		{
			skinnable.setValue(skinnable.getPopupProvider().fromString(pNewValue));
		}
	}
	
	/**
	 * Invoked if the value of the {@link FXAbstractPopupProvider} changes.
	 * <p>
	 * Propagates the change to the {@link #displayTextField}.
	 * 
	 * @param pObservable the observable.
	 * @param pOldValue the old value.
	 * @param pNewValue the new value.
	 */
	private void onValueChanged(ObservableValue<? extends T> pObservable, T pOldValue, T pNewValue)
	{
		if (skinnable.getPopupProvider() != null)
		{
			displayTextField.setText(skinnable.getPopupProvider().toString(pNewValue));
		}
		else if (pNewValue != null)
		{
			displayTextField.setText(pNewValue.toString());
		}
		else
		{
			displayTextField.setText("");
		}
	}
	
	/**
	 * Repositions the popup so that it is exactly over the combobox.
	 */
	private void repositionPopup()
	{
		if (popup != null)
		{
			Point2D displayTextFieldScreenPosition = displayTextField.localToScreen(0, 0);
			// TODO Guessing the size of the drop shadow.
			Point2D popupTextFieldPosition = popupTextField.localToScene(-12, -4);
			Point2D popupLocation = displayTextFieldScreenPosition.subtract(popupTextFieldPosition);
			
			popup.setAnchorX(popupLocation.getX());
			popup.setAnchorY(popupLocation.getY());
		}
	}
	
}   // FXCustomComboBoxSkin
