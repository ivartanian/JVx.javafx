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
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXFluidFlowPaneTestMain extends Application
{
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		FXFluidFlowPane flowPane = new FXFluidFlowPane();
		flowPane.setStyle("-fx-background-color: #FFBBBB;");
		
		FXFluidFlowPane fixedFlowPane = new FXFluidFlowPane();
		fixedFlowPane.setStyle("-fx-background-color: #BBBBFF;");
		
		// Needed so that the flow pane can resize itself.
		VBox containerBox = new VBox();
		containerBox.setFillWidth(false);
		containerBox.getChildren().add(flowPane);
		containerBox.getChildren().add(new Label("This is a label to show you that\nthe FluidFlowPane does not overlap\nother elements unless necessary."));
		
		ComboBox<Orientation> orientationBox = new ComboBox<>();
		orientationBox.getItems().addAll(Orientation.values());
		orientationBox.setValue(flowPane.getOrientation());
		flowPane.orientationProperty().bind(orientationBox.valueProperty());
		fixedFlowPane.orientationProperty().bind(orientationBox.valueProperty());
		
		ComboBox<Pos> alignmentBox = new ComboBox<>();
		alignmentBox.getItems().addAll(Pos.values());
		alignmentBox.setValue(flowPane.getAlignment());
		flowPane.alignmentProperty().bind(alignmentBox.valueProperty());
		fixedFlowPane.alignmentProperty().bind(alignmentBox.valueProperty());
		
		ComboBox<HPos> inlineHPosBox = new ComboBox<>();
		inlineHPosBox.getItems().addAll(HPos.values());
		inlineHPosBox.setValue(flowPane.getInlineHPos());
		flowPane.inlineHPosProperty().bind(inlineHPosBox.valueProperty());
		fixedFlowPane.inlineHPosProperty().bind(inlineHPosBox.valueProperty());
		
		ComboBox<VPos> inlineVPosBox = new ComboBox<>();
		inlineVPosBox.getItems().addAll(VPos.values());
		inlineVPosBox.setValue(flowPane.getInlineVPos());
		flowPane.inlineVPosProperty().bind(inlineVPosBox.valueProperty());
		fixedFlowPane.inlineVPosProperty().bind(inlineVPosBox.valueProperty());
		
		CheckBox autoWrapCheckBox = new CheckBox("Auto Wrap");
		autoWrapCheckBox.setSelected(flowPane.isAutoWrap());
		flowPane.autoWrapProperty().bind(autoWrapCheckBox.selectedProperty());
		fixedFlowPane.autoWrapProperty().bind(autoWrapCheckBox.selectedProperty());
		
		Slider hGapSlider = new Slider(0, 100, flowPane.getHGap());
		flowPane.hGapProperty().bind(hGapSlider.valueProperty());
		fixedFlowPane.hGapProperty().bind(hGapSlider.valueProperty());
		
		Slider vGapSlider = new Slider(0, 100, flowPane.getVGap());
		flowPane.vGapProperty().bind(vGapSlider.valueProperty());
		fixedFlowPane.vGapProperty().bind(vGapSlider.valueProperty());
		
		CheckBox inlineStretchCheckBox = new CheckBox("Inline Stretch");
		inlineStretchCheckBox.setSelected(flowPane.isInlineStretch());
		flowPane.inlineStretchProperty().bind(inlineStretchCheckBox.selectedProperty());
		fixedFlowPane.inlineStretchProperty().bind(inlineStretchCheckBox.selectedProperty());
		
		CheckBox stretchHorizontalCheckBox = new CheckBox("Stretch Horizontal");
		stretchHorizontalCheckBox.setSelected(flowPane.isStretchHorizontal());
		flowPane.stretchHorizontalProperty().bind(stretchHorizontalCheckBox.selectedProperty());
		fixedFlowPane.stretchHorizontalProperty().bind(stretchHorizontalCheckBox.selectedProperty());
		
		CheckBox stretchVerticalCheckBox = new CheckBox("Stretch Vertical");
		stretchVerticalCheckBox.setSelected(flowPane.isStretchVertical());
		flowPane.stretchVerticalProperty().bind(stretchVerticalCheckBox.selectedProperty());
		fixedFlowPane.stretchVerticalProperty().bind(stretchVerticalCheckBox.selectedProperty());
		
		Button addButton = new Button("Add component");
		addButton.setOnAction((pActionEvent) ->
		{
			addComponent(flowPane);
			addComponent(fixedFlowPane);
		});
		
		Button removeButton = new Button("Remove last component");
		removeButton.setOnAction((pActionEvent) ->
		{
			if (!flowPane.getChildren().isEmpty())
			{
				flowPane.getChildren().remove(flowPane.getChildren().size() - 1);
			}
			if (!fixedFlowPane.getChildren().isEmpty())
			{
				fixedFlowPane.getChildren().remove(fixedFlowPane.getChildren().size() - 1);
			}
		});
		
		FXFormPane controlPane = new FXFormPane();
		controlPane.setNewlineCount(7);
		controlPane.getChildren().add(new Label("Orientation"));
		controlPane.getChildren().add(orientationBox);
		controlPane.getChildren().add(new Label("Alignment"));
		controlPane.getChildren().add(alignmentBox);
		controlPane.getChildren().add(addButton);
		controlPane.addChildInNewLine(new Label("Inline Horizontal Alignment"));
		controlPane.getChildren().add(inlineHPosBox);
		controlPane.getChildren().add(new Label("Inline Vertical Alignment"));
		controlPane.getChildren().add(inlineVPosBox);
		controlPane.getChildren().add(removeButton);
		controlPane.addChildInNewLine(new Label("Horizontal Gap"));
		controlPane.getChildren().add(hGapSlider);
		controlPane.getChildren().add(new Label("Vertical Gap"));
		controlPane.getChildren().add(vGapSlider);
		controlPane.getChildren().add(autoWrapCheckBox);
		controlPane.addChildInNewLine(inlineStretchCheckBox);
		controlPane.getChildren().add(stretchHorizontalCheckBox);
		controlPane.getChildren().add(stretchVerticalCheckBox);
		
		SplitPane splitPane = new SplitPane();
		splitPane.getItems().add(containerBox);
		splitPane.getItems().add(fixedFlowPane);
		
		BorderPane root = new BorderPane();
		containerBox.setPrefSize(800, 600);
		root.setTop(controlPane);
		root.setCenter(splitPane);
		BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("JavaFX FluidFlowPane Test");
		primaryStage.show();
		
		addButton.fire();
		addButton.fire();
		addButton.fire();
		addButton.fire();
	}
	
	private void addComponent(Pane pPane)
	{
		int type = pPane.getChildren().size() % 3;
		
		switch (type)
		{
			case 0:
				Label label = new Label("Label " + Integer.toString(pPane.getChildren().size()));
				label.setMinSize(32, 32);
				label.setStyle("-fx-background-color: #0099FF;");
				pPane.getChildren().add(label);
				break;
			
			case 1:
				Button button = new Button("Button " + Integer.toString(pPane.getChildren().size()));
				button.setPrefSize(64, 64);
				pPane.getChildren().add(button);
				break;
			
			case 2:
				CheckBox checkBox = new CheckBox("Some CheckBox " + Integer.toString(pPane.getChildren().size()));
				checkBox.setStyle("-fx-background-color: #99FF00;");
				pPane.getChildren().add(checkBox);
				break;
		
		}
	}
}
