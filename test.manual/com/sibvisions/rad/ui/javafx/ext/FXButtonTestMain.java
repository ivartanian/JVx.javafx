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
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FXButtonTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXButton button = new FXButton("Some text", new ImageView(new Image("http://upload.wikimedia.org/wikipedia/commons/6/6f/Icon-designed_using_vim.png")));
		
		ComboBox<ContentDisplay> contentBox = new ComboBox<>();
		contentBox.getItems().addAll(ContentDisplay.values());
		contentBox.setValue(button.getContentDisplay());
		button.contentDisplayProperty().bind(contentBox.valueProperty());
		
		ComboBox<HPos> hPosBox = new ComboBox<>();
		hPosBox.getItems().addAll(HPos.values());
		hPosBox.setValue(button.getRelativeHorizontalTextAlignment());
		button.relativeHorizontalTextAlignment().bind(hPosBox.valueProperty());
		
		ComboBox<VPos> vPosBox = new ComboBox<>();
		vPosBox.getItems().addAll(VPos.values());
		vPosBox.setValue(button.getRelativeVerticalTextAlignment());
		button.relativeVerticalTextAlignment().bind(vPosBox.valueProperty());
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(99);
		controlPane.getChildren().add(new Label("Content Display"));
		controlPane.getChildren().add(contentBox);
		controlPane.getChildren().add(new Label("Horizontal Text Alignment"));
		controlPane.getChildren().add(hPosBox);
		controlPane.getChildren().add(new Label("Vertical Text Alignment"));
		controlPane.getChildren().add(vPosBox);
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(button);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FXButton Test");
		primaryStage.show();
	}
}
