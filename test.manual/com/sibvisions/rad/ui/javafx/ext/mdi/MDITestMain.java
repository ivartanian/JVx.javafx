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
package com.sibvisions.rad.ui.javafx.ext.mdi;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.mdi.FXInternalWindow.State;
import com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers.FXDesktopWindowManager;
import com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers.FXSingleWindowManager;
import com.sibvisions.rad.ui.javafx.ext.mdi.windowmanagers.FXTabWindowManager;
import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;
import com.sibvisions.rad.ui.javafx.ext.util.FXSceneLocker;

public class MDITestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXDesktopPane pane = new FXDesktopPane();
		FXSceneLocker.makeLockRoot(pane);
		
		Button addWindowButton = new Button("Add window");
		addWindowButton.setOnAction((pEvent) ->
		{
			pane.getWindows().add(createWindow("My Test Window #" + pane.getWindows().size()));
		});
		
		Button addModalWindowButton = new Button("Add modal window");
		addModalWindowButton.setOnAction((pEvent) ->
		{
			FXInternalWindow window = createWindow("My Modal Window #" + pane.getWindows().size());
			window.setModal(true);
			
			pane.getWindows().add(window);
		});
		
		Button updateActiveWindowButton = new Button("Update active window");
		updateActiveWindowButton.setOnAction((pEvent) ->
		{
			pane.getWindowManager().updateActiveWindow();
		});
		
		ComboBox<String> modeComboBox = new ComboBox<>();
		modeComboBox.getItems().addAll("Desktop", "Tabs", "Single");
		modeComboBox.getSelectionModel().select("Desktop");
		modeComboBox.getSelectionModel().selectedItemProperty().addListener((pObservable, pOldValue, pNewValue) ->
		{
			switch (pNewValue)
			{
				case "Tabs":
					pane.setWindowManager(new FXTabWindowManager());
					break;
					
				case "Single":
					pane.setWindowManager(new FXSingleWindowManager());
					break;
					
				case "Desktop":
				default:
					pane.setWindowManager(new FXDesktopWindowManager());
					
			}
		});
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(6);
		controlPane.addChild(addWindowButton);
		controlPane.addChild(addModalWindowButton);
		controlPane.addChild(updateActiveWindowButton);
		controlPane.addChild(modeComboBox);
		
		BorderPane root = new BorderPane();
		root.setPrefSize(640, 480);
		root.setTop(controlPane);
		root.setCenter(pane);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX MDI Test");
		primaryStage.show();
		
		addWindowButton.fire();
	}
	
	private static Label createLabel(String pText, String pColor)
	{
		Label label = new Label(pText);
		label.setAlignment(Pos.CENTER);
		label.setCursor(Cursor.HAND);
		label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setOnMousePressed(pAction -> label.getParent().setStyle(label.getStyle()));
		label.setStyle("-fx-background-color: #" + pColor + ";");
		label.setTextAlignment(TextAlignment.CENTER);
		
		return label;
	}
	
	private static FXInternalWindow createWindow(String pTitle)
	{
		FXFormPane formPane = new FXFormPane();
		formPane.setNewlineCount(3);
		formPane.addChild(createLabel("Red", "ff0000"));
		formPane.addChild(createLabel("Green", "00ff00"));
		formPane.addChild(createLabel("Blue", "0000ff"));
		formPane.addChild(createLabel("Red/Green", "ffff00"));
		formPane.addChild(createLabel("Green/Blue", "00ffff"));
		formPane.addChild(createLabel("Blue/Red", "ff00ff"));
		formPane.addChild(createLabel("Half Red", "880000"));
		formPane.addChild(createLabel("Half Green", "008800"));
		formPane.addChild(createLabel("Half Blue", "000088"));
		formPane.addChild(createLabel("Half Red/Green", "888800"));
		formPane.addChild(createLabel("Half Green/Blue", "008888"));
		formPane.addChild(createLabel("Half Blue/Red", "880088"));
		
		FXInternalWindow window = new FXInternalWindow(pTitle);
		window.setMinSize(256, 128);
		
		window.setIcon(new Image(MDITestMain.class.getResourceAsStream("/com/sibvisions/rad/ui/javafx/images/emblem-favorite.png")));
		window.setContent(formPane);
		
		ToggleButton resizeableButton = new ToggleButton("Resizeable");
		resizeableButton.setSelected(window.isResizeable());
		window.resizeableProperty().bindBidirectional(resizeableButton.selectedProperty());
		formPane.addChild(resizeableButton);
		
		ToggleButton borderlessButton = new ToggleButton("Borderless");
		borderlessButton.setSelected(window.isBorderless());
		window.borderlessProperty().bindBidirectional(borderlessButton.selectedProperty());
		formPane.addChild(borderlessButton);
		
		ToggleButton decoratedButton = new ToggleButton("Decorated");
		decoratedButton.setSelected(window.isDecorated());
		window.decoratedProperty().bindBidirectional(decoratedButton.selectedProperty());
		formPane.addChild(decoratedButton);
		
		ToggleButton closableButton = new ToggleButton("Closeable");
		closableButton.setSelected(window.isCloseable());
		window.closeableProperty().bindBidirectional(closableButton.selectedProperty());
		formPane.addChild(closableButton);
		
		ToggleButton maximizableButton = new ToggleButton("Maximizable");
		maximizableButton.setSelected(window.isMaximizeable());
		window.maximizeableProperty().bindBidirectional(maximizableButton.selectedProperty());
		formPane.addChild(maximizableButton);
		
		ToggleButton minimizableButton = new ToggleButton("Minimizable");
		minimizableButton.setSelected(window.isMinimizeable());
		window.minimizeableProperty().bindBidirectional(minimizableButton.selectedProperty());
		formPane.addChild(minimizableButton);
		
		ToggleButton movableButton = new ToggleButton("Movable");
		movableButton.setSelected(window.isMoveable());
		window.moveableProperty().bindBidirectional(movableButton.selectedProperty());
		formPane.addChild(movableButton);
		
		ComboBox<State> stateComboBox = new ComboBox<>();
		stateComboBox.getItems().addAll(State.values());
		stateComboBox.getSelectionModel().select(window.getState());
		window.stateProperty().bindBidirectional(stateComboBox.valueProperty());
		formPane.addChild(stateComboBox);
		
		ToggleButton modalButton = new ToggleButton("Modal");
		modalButton.setSelected(window.isModal());
		window.modalProperty().bindBidirectional(modalButton.selectedProperty());
		formPane.addChild(modalButton);
		
		ToggleButton activeButton = new ToggleButton("Active");
		activeButton.setSelected(window.isActive());
		window.activeProperty().bindBidirectional(activeButton.selectedProperty());
		formPane.addChild(activeButton);
		
		Button toFrontButton = new Button("To front");
		toFrontButton.setOnAction(pActionEvent -> window.toFront());
		formPane.addChild(toFrontButton);
		
		Button closeButton = new Button("Close");
		closeButton.setDefaultButton(true);
		closeButton.setOnAction(pActionEvent -> window.close());
		formPane.addChild(closeButton);
		
		return window;
	}
}
