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

import java.time.LocalDateTime;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FXDateTimePickerTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		Label valueLabel = new Label("null");
		
		FXDateTimePicker dateTimePicker = new FXDateTimePicker();
		valueLabel.textProperty().bindBidirectional(dateTimePicker.valueProperty(), new LocalDateTimeStringConverter());
		
		FXDateTimeComboBox dateTimeComboBox = new FXDateTimeComboBox();
		dateTimeComboBox.valueProperty().bindBidirectional(dateTimePicker.valueProperty());
		
		FXFormPane root = new FXFormPane();
		root.addChild(dateTimePicker, root.createConstraint(0, 0, -1, -3));
		root.addChild(dateTimeComboBox, root.createConstraint(0, -2, -1, -2));
		root.addChild(new Label("Value: "), root.createConstraint(0, -1, 0, -1));
		root.addChild(valueLabel, root.createConstraint(1, -1, -1, -1));
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FXDateTimePicker Test");
		primaryStage.show();
		
		dateTimePicker.setValue(LocalDateTime.now().withNano(0));
	}
	
}
