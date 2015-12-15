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
package com.sibvisions.rad.ui.javafx.ext.panes;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;
import com.sibvisions.rad.ui.javafx.ext.panes.FXSequencePane;

public class FXSequencePaneTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	private static Label createLabel(String pText, String pColor)
	{
		Label label = new Label(pText);
		label.setAlignment(Pos.CENTER);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setStyle("-fx-background-color: #" + pColor + ";");
		
		return label;
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXSequencePane fillerPane = new FXSequencePane();
		
		fillerPane.getChildren().add(createLabel("First", "fce94f"));
		fillerPane.getChildren().add(createLabel("Second", "fcaf3e"));
		fillerPane.getChildren().add(createLabel("Third", "e9b96e"));
		fillerPane.getChildren().add(createLabel("Fourth", "729fcf"));
		fillerPane.getChildren().add(createLabel("Right", "8ae234"));
		
		ComboBox<Orientation> orientationComboBox = new ComboBox<>();
		orientationComboBox.getItems().addAll(Orientation.values());
		orientationComboBox.setValue(fillerPane.getOrientation());
		fillerPane.orientationProperty().bind(orientationComboBox.valueProperty());
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(7);
		controlPane.addChild(new Label("Orientation"));
		controlPane.addChild(orientationComboBox);
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(fillerPane);
		BorderPane.setAlignment(root.getTop(), Pos.CENTER);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX SequencePane Test");
		primaryStage.show();
	}
}
