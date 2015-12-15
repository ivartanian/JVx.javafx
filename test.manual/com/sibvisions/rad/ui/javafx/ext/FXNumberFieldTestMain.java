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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FXNumberFieldTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXNumberField numberField = new FXNumberField();
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(99);
		
		BorderPane root = new BorderPane();
		root.setTop(controlPane);
		root.setCenter(numberField);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		BorderPane.setMargin(numberField, new Insets(10, 10, 10, 10));
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX NumberField Test");
		primaryStage.show();
	}
}
