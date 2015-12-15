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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FXSelectableLabelTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		BorderPane root = new BorderPane();
		root.setBottom(new FXSelectableLabel("Selectable"));
		root.setCenter(new FXSelectableLabel("Selectable"));
		root.setLeft(new FXSelectableLabel("Selectable"));
		root.setRight(new FXSelectableLabel("Selectable"));
		root.setTop(new FXSelectableLabel("Selectable"));
		BorderPane.setAlignment(root.getBottom(), Pos.CENTER);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		BorderPane.setAlignment(root.getLeft(), Pos.CENTER);
		BorderPane.setAlignment(root.getRight(), Pos.CENTER);
		BorderPane.setAlignment(root.getTop(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FXSelectableLabel Test");
		primaryStage.show();
	}
	
}
