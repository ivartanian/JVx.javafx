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

import java.util.Date;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;
import com.sibvisions.rad.ui.javafx.ext.panes.FXPositioningPane;

public class FXPositioningPaneTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXPositioningPane positioningPane = new FXPositioningPane();
		positioningPane.setPrefSize(640, 480);
		
		ComboBox<Pos> posBox = new ComboBox<>();
		posBox.getItems().addAll(Pos.values());
		posBox.setValue(positioningPane.getPosition());
		positioningPane.positionProperty().bind(posBox.valueProperty());
		
		Button addButton = new Button("New child");
		addButton.setOnAction((pActionEvent) -> {
			positioningPane.getChildren().clear();
			positioningPane.getChildren().add(new Label("This is a new label: " + new Date()));
		});
		
		Button moveButton = new Button("Move child");
		moveButton.setOnAction((pActionEvent) -> {
			positioningPane.move(positioningPane.getChildren().get(0), positioningPane.getPosition());
		});
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(99);
		controlPane.getChildren().add(new Label("Position"));
		controlPane.getChildren().add(posBox);
		controlPane.getChildren().add(addButton);
		controlPane.getChildren().add(moveButton);
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(positioningPane);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX PositioningPane Test");
		primaryStage.show();
		
		addButton.fire();
	}
}
