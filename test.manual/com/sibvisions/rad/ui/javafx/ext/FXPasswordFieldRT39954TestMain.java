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

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FXPasswordFieldRT39954TestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXPasswordFieldRT39954 passwordField = new FXPasswordFieldRT39954();
		passwordField.setText("password");
		
		TextField maskCharTextField = new TextField(Character.toString(passwordField.getMaskChar()));
		passwordField.maskCharProperty().bind(maskCharTextField.textProperty());
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(99);
		controlPane.getChildren().add(new Label("Mask Character"));
		controlPane.getChildren().add(maskCharTextField);
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(passwordField);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		BorderPane.setMargin(passwordField, new Insets(10, 10, 10, 10));
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX PositioningPane Test");
		primaryStage.show();
	}
}
