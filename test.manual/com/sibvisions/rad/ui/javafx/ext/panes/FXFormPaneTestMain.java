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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;

public class FXFormPaneTestMain extends Application
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
		FXFormPane formPane = new FXFormPane();
		
		formPane.addChild(createLabel("First 0, 0 / Butter", "fce94f"), formPane.createConstraint(0, 0));
		formPane.addChild(createLabel("Second 1, 0 / Orange", "fcaf3e"), formPane.createConstraint(1, 0));
		formPane.addChild(createLabel("Third 2, 0 / Chocolate", "e9b96e"), formPane.createConstraint(2, 0));
		formPane.addChild(createLabel("Fourth 3, 0 / Butter", "fce94f"), formPane.createConstraint(3, 0));
		formPane.addChild(createLabel("Right aligned -1, 0 / Chameleon", "8ae234"), formPane.createConstraint(-1, 0));
		
		formPane.addChild(createLabel("Next line 0, 1 / Sky Blue", "729fcf"), formPane.createConstraint(0, 1));
		formPane.addChild(createLabel("Right aligned -1, 1 / Plum", "ad7fa8"), formPane.createConstraint(-1, 1));
		
		formPane.addChild(createLabel("Next line 0, 2 / Sky Blue", "729fcf"), formPane.createConstraint(0, 2));
		formPane.addChild(createLabel("Right aligned -1, 2 / Plum", "ad7fa8"), formPane.createConstraint(-1, 2));
		
		formPane.addChild(createLabel("Centered 0, 3, -1, -4 / Aluminium", "eeeeec"), formPane.createConstraint(0, 3, -1, -4));
		
		formPane.addChild(createLabel("Bottom 0, -3 / Scarlet Red", "ef2929"), formPane.createConstraint(0, -3));
		formPane.addChild(createLabel("Bottom and Right aligned -1, -3 / Butter", "c4a000"), formPane.createConstraint(-1, -3));
		
		formPane.addChild(createLabel("Bottom 0, -2 / Scarlet Red", "ef2929"), formPane.createConstraint(0, -2));
		formPane.addChild(createLabel("Bottom and Right aligned -1, -2 / Butter", "c4a000"), formPane.createConstraint(-1, -2));
		
		formPane.addChild(createLabel("Bottom 0, -1 / Scarlet Red", "ef2929"), formPane.createConstraint(0, -1));
		formPane.addChild(createLabel("Bottom and Right aligned -1, -1 / Butter", "c4a000"), formPane.createConstraint(-1, -1));
		
		Slider hgapSlider = new Slider(0, 100, formPane.getHGap());
		formPane.hGapProperty().bind(hgapSlider.valueProperty());
		
		Slider vgapSlider = new Slider(0, 100, formPane.getVGap());
		formPane.vGapProperty().bind(vgapSlider.valueProperty());
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(7);
		controlPane.addChild(hgapSlider);
		controlPane.addChild(vgapSlider);
		
		BorderPane root = new BorderPane();
		root.setBottom(createLabel("- - Bottom - -", "888a85"));
		root.setCenter(formPane);
		root.setLeft(createLabel(" | \n\n | \n\n Left \n\n | \n\n | \n\n", "babdb6"));
		root.setRight(createLabel(" | \n\n | \n\n Right \n\n | \n\n | \n\n", "555753"));
		root.setTop(controlPane);
		BorderPane.setAlignment(root.getBottom(), Pos.CENTER);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		BorderPane.setAlignment(root.getLeft(), Pos.CENTER);
		BorderPane.setAlignment(root.getRight(), Pos.CENTER);
		BorderPane.setAlignment(root.getTop(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FormPane Test");
		primaryStage.show();
	}
}
