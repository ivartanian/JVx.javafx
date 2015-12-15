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
package com.sibvisions.javafx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WaitCursorStackPaneTest extends Application
{
	private Scene scene;
	private StackPane glass;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		Button butWait = new Button("Show WAIT cursor");
		butWait.setOnAction(e -> 
		{
			glass.setVisible(true);
//			glass.setCursor(Cursor.WAIT);
			
			try
			{
				Thread.sleep(5000);
			}
			catch (Exception ex)
			{
				//ignore
			}
			finally
			{
//				glass.setCursor(Cursor.DEFAULT);
				glass.setVisible(false);
			}
		});
		
		BorderPane panButton = new BorderPane();
		panButton.setCenter(butWait);
		
		glass = new StackPane();
	    StackPane.setAlignment(butWait, Pos.BOTTOM_CENTER);
	    glass.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");
	    glass.setCursor(Cursor.WAIT);
	    glass.setVisible(false);
	    
	    final StackPane layout = new StackPane();
	    layout.getChildren().addAll(panButton, glass);

	    scene = new Scene(layout, 800, 600);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX FlowPane Test");
		primaryStage.show();
	}
	
}
