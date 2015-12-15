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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FXBorderPaneTestMain extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXBorderPane pane = new FXBorderPane();
		pane.setCenter(createLabel("Center", "#FF0000"));
		pane.setTop(createLabel("Top", "#00FF00"));
		pane.setLeft(createLabel("Left", "#0000FF"));
		pane.setBottom(createLabel("Bottom", "#FF00FF"));
		pane.setRight(createLabel("Right", "#FFFF00"));
		
		BorderPane.setMargin(pane.getCenter(), new Insets(10));
		BorderPane.setMargin(pane.getTop(), new Insets(10));
		BorderPane.setMargin(pane.getLeft(), new Insets(10));
		BorderPane.setMargin(pane.getBottom(), new Insets(10));
		BorderPane.setMargin(pane.getRight(), new Insets(10));
		
		BorderPane root = new BorderPane();
		root.setCenter(pane);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FXBorderPane Test");
		primaryStage.show();
	}
	
	private static Label createLabel(String pText, String pColor)
	{
		Label label = new Label(pText);
		label.setMaxSize(25, 25);
		label.setMinSize(128, 128);
		label.setStyle("-fx-background-color: " + pColor + ";");
		
		return label;
	}
}
