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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.rad.genui.UIFactoryManager;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;
import com.sibvisions.rad.ui.javafx.impl.JavaFXFactory;

public class FXImageChoiceBoxTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		UIFactoryManager.getFactoryInstance(JavaFXFactory.class);
		
		String[] allowedValues = new String[] { "A", "B", "C", "D" };
		String[] imageNames = new String[] {
				"/com/sibvisions/rad/ui/javafx/images/emblem-favorite.png",
				"/com/sibvisions/rad/ui/javafx/images/emblem-important.png",
				"/com/sibvisions/rad/ui/javafx/images/emblem-readonly.png",
				"/com/sibvisions/rad/ui/javafx/images/emblem-unreadable.png",
		};
		String defaultImageName = "/com/sibvisions/rad/ui/javafx/images/emblem-favorite.png";
		
		FXImageChoiceBox<String> choiceBox = new FXImageChoiceBox<>(allowedValues, imageNames, defaultImageName);
		
		Label valueLabel = new Label();
		valueLabel.setText((String) choiceBox.getValue());
		valueLabel.textProperty().bind(choiceBox.valueProperty());
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(6);
		controlPane.getChildren().add(valueLabel);
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(choiceBox);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FXImageChoiceBox Test");
		primaryStage.show();
	}
}
