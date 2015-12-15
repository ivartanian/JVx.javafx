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
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;
import com.sibvisions.rad.ui.javafx.ext.util.FXSceneLocker;

public class FXSceneLockerTestMain extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXFormPane root = new FXFormPane();
		root.getChildren().add(createGroup("First Group"));
		root.getChildren().add(createGroup("Second Group"));
		root.getChildren().add(createGroup("Next Group"));
		root.getChildren().add(createGroup("Last Group"));
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FXNodeLocker Test");
		primaryStage.show();
	}
	
	private static Node createGroup(String pText)
	{
		Button hardButton = new Button("Hard lock this");
		hardButton.setOnAction((pActionEvent) ->
		{
			FXSceneLocker.addHardLock(hardButton);
			
			new Thread(() ->
			{
				try
				{
					Thread.sleep(5000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				Platform.runLater(() ->
				{
					FXSceneLocker.removeHardLock(hardButton);
				});
			}).start();
		});
		
		ToggleButton selectiveButton = new ToggleButton("Lock except this");
		
		FXFormPane group = new FXFormPane();
		group.addChild(createLabel("Red", "FF0000"));
		group.addChild(createLabel("Green", "00FF00"));
		group.addChild(createLabel("Blue", "0000FF"));
		group.addChild(createLabel("Black", "000000"));
		group.addChild(createLabel("White", "FFFFFF"));
		group.addChild(createLabel("Gray", "888888"));
		group.addChild(hardButton);
		group.addChild(selectiveButton);
		
		TitledPane titledPane = new TitledPane(pText, group);
		
		selectiveButton.setOnAction((pActionEvent) ->
		{
			if (selectiveButton.isSelected())
			{
				FXSceneLocker.addLock(selectiveButton.getScene(), titledPane);
			}
			else
			{
				FXSceneLocker.removeLock(selectiveButton.getScene(), titledPane);
			}
		});
		
		FXSceneLocker.makeLockRoot(titledPane);
		
		return titledPane;
	}
	
	private static Label createLabel(String pText, String pColor)
	{
		Label label = new Label(pText);
		label.setAlignment(Pos.CENTER);
		label.setCursor(Cursor.HAND);
		label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setOnMousePressed(pAction -> label.getParent().setStyle(label.getStyle()));
		label.setStyle("-fx-background-color: #" + pColor + ";");
		label.setTextAlignment(TextAlignment.CENTER);
		
		return label;
	}
	
}
