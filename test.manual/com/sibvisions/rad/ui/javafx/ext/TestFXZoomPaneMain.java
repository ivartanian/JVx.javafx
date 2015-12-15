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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TestFXZoomPaneMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage pPrimaryStage) throws Exception
	{
		GridPane content = new GridPane();
		content.setHgap(2);
		content.setMaxSize(400, 120);
		content.setVgap(3);
		content.add(new Label("Welcome to this nice form!"), 0, 0, 3, 1);
		content.setStyle("-fx-background-color: #eaeaea;");
		
		content.add(new Label("Lastname"), 0, 1);
		content.add(new TextField(), 1, 1);
		content.add(new Label("Firstname"), 2, 1);
		content.add(new TextField(), 3, 1);
		
		content.add(new Label("Nickname"), 0, 2);
		content.add(new TextField(), 1, 2);
		
		content.add(new Button("Save"), 0, 3);
		content.add(new Button("Reset"), 1, 3);
		
		FXZoomRegion zoomRegion = new FXZoomRegion();
		zoomRegion.setContent(new BorderPane(content));
		
		Slider zoomSlider = new Slider(0, 10, zoomRegion.getZoom());
		zoomSlider.valueProperty().bindBidirectional(zoomRegion.zoomProperty());
		
		Button zoomResetButton = new Button("Reset");
		zoomResetButton.setOnAction((pActionEvent) -> zoomRegion.setZoom(1));
		
		GridPane controls = new GridPane();
		controls.add(new Label("Zoom"), 0, 0);
		controls.add(zoomSlider, 1, 0);
		controls.add(zoomResetButton, 2, 0);
		
		BorderPane root = new BorderPane();
		root.setPrefSize(640, 480);
		root.setTop(controls);
		root.setCenter(zoomRegion);
		
		root.addEventFilter(KeyEvent.KEY_PRESSED, (pKeyEvent ->
		{
			if (pKeyEvent.isControlDown() && pKeyEvent.isShiftDown() && pKeyEvent.getCode() == KeyCode.S)
			{
				pKeyEvent.consume();
			}
		}));
		
		pPrimaryStage.setScene(new Scene(root));
		pPrimaryStage.show();
	}
	
}
