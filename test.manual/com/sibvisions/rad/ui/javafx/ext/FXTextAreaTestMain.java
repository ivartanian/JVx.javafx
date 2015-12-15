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
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FXTextAreaTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXTextArea textArea = new FXTextArea("This is a text area.\nMultiple lines are supported.");
		
		ComboBox<HPos> alignmentBox = new ComboBox<>();
		alignmentBox.getItems().addAll(HPos.values());
		alignmentBox.setValue(textArea.getAlignment());
		textArea.alignmentProperty().bind(alignmentBox.valueProperty());
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(99);
		controlPane.getChildren().add(new Label("Alignment"));
		controlPane.getChildren().add(alignmentBox);
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(textArea);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX PositioningPane Test");
		primaryStage.show();
	}
}
