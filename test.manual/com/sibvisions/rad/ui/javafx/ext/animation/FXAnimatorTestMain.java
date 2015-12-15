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
package com.sibvisions.rad.ui.javafx.ext.animation;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;
import com.sibvisions.rad.ui.javafx.ext.panes.FXPositioningPane;

public class FXAnimatorTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		Label frontLabel = new Label("This is the front.");
		frontLabel.setStyle("-fx-background-color: #8888CC;");
		
		Label backLabel = new Label("This is the darkside.");
		backLabel.setStyle("-fx-background-color: #CC8888;");
		
		TitledPane flippingPane = new TitledPane("Front", frontLabel);
		flippingPane.setPrefSize(200, 200);
		flippingPane.setEffect(new DropShadow());
		
		FXPositioningPane containerPane = new FXPositioningPane();
		containerPane.getChildren().add(flippingPane);
		
		ComboBox<Pos> positionComboBox = new ComboBox<>();
		positionComboBox.getItems().addAll(Pos.values());
		positionComboBox.valueProperty().addListener((pObservable) ->
		{
			containerPane.move(flippingPane, positionComboBox.getValue());
		});
		
		ComboBox<Orientation> orientationComboBox = new ComboBox<>();
		orientationComboBox.getItems().addAll(Orientation.values());
		orientationComboBox.setValue(Orientation.HORIZONTAL);
		
		CheckBox rightLeftTopDownCheckBox = new CheckBox("Right to Left/Top to Down");
		rightLeftTopDownCheckBox.setSelected(true);
		
		Button flipButton = new Button("Flip");
		flipButton.setOnAction((pActionEvent) ->
		{
			FXAnimator.flip(flippingPane, orientationComboBox.getValue(), rightLeftTopDownCheckBox.isSelected(), () ->
			{
				if (flippingPane.getText().equals("Front"))
				{
					flippingPane.setText("Back");
					flippingPane.setContent(backLabel);
				}
					else
					{
						flippingPane.setText("Front");
						flippingPane.setContent(frontLabel);
					}
				}, null);
		});
		
		Slider shakeStrengthSlider = new Slider(0, 250, 100);
		
		ComboBox<Orientation> orientationShakeComboBox = new ComboBox<>();
		orientationShakeComboBox.getItems().addAll(Orientation.values());
		orientationShakeComboBox.setValue(Orientation.HORIZONTAL);
		
		Button shakeButton = new Button("Shake");
		shakeButton.setOnAction((pActionEvent) ->
		{
			FXAnimator.shake(flippingPane, orientationShakeComboBox.getValue(), shakeStrengthSlider.getValue(), null);
		});
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.addChild(new Label("Position of group"), controlPane.createConstraint(0, 0));
		controlPane.addChild(positionComboBox, controlPane.createConstraint(1, 0));
		controlPane.addChild(new Label("Flip Orientation"), controlPane.createConstraint(2, 0));
		controlPane.addChild(orientationComboBox, controlPane.createConstraint(3, 0));
		controlPane.addChild(rightLeftTopDownCheckBox, controlPane.createConstraint(4, 0));
		controlPane.addChild(flipButton, controlPane.createConstraint(5, 0));
		
		controlPane.addChild(new Label("Strength"), controlPane.createConstraint(0, 1));
		controlPane.addChild(shakeStrengthSlider, controlPane.createConstraint(1, 1));
		controlPane.addChild(orientationShakeComboBox, controlPane.createConstraint(3, 1));
		controlPane.addChild(shakeButton, controlPane.createConstraint(5, 1));
		
		BorderPane root = new BorderPane();
		root.setPrefSize(640, 480);
		root.setCenter(containerPane);
		root.setTop(controlPane);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.setFieldOfView(60);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.getScene().setCamera(camera);
		primaryStage.setTitle("JavaFX FXAnimator Test");
		primaryStage.show();
	}
}
