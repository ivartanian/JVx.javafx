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

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.util.Duration;

/**
 * The {@link FXAnimator} is a helper class that allows to easily animate
 * {@link Node}s.
 * 
 * @author Robert Zenz
 */
public final class FXAnimator
{
	/** The {@link Point3D} that represents the X axis. */
	public static final Point3D X_AXIS = new Point3D(1, 0, 0);
	
	/** The {@link Point3D} that represents the Y axis. */
	public static final Point3D Y_AXIS = new Point3D(0, 1, 0);
	
	/** The {@link Point3D} that represents the Z axis. */
	public static final Point3D Z_AXIS = new Point3D(0, 0, 1);
	
	/**
	 * Not needed.
	 */
	private FXAnimator()
	{
		// No instance needed
	}
	
	/**
	 * Calculates the angle needed for the given {@link Node} to be edge-on with
	 * the user.
	 * 
	 * @param pNode the {@link Node}.
	 * @param pOrientation the {@link Orientation}.
	 * @return the angle needed for the {@link Node} the be viewed edge-on.
	 */
	public static double calculateEdgeAngle(Node pNode, Orientation pOrientation)
	{
		Scene scene = pNode.getScene();
		Camera camera = scene.getCamera();
		
		if (camera instanceof PerspectiveCamera)
		{
			PerspectiveCamera perspectiveCamera = (PerspectiveCamera)camera;
			
			double distanceFromCenter = getDistanceFromCenter(scene, pNode, pOrientation);
			double rotationFromCenter = getAngleFromCenter(scene, perspectiveCamera, distanceFromCenter);
			return 90 - rotationFromCenter;
		}
		
		return 90;
	}
	
	/**
	 * Creates a {@link RotateTransition} that is the second/end part of a flip
	 * animation.
	 * 
	 * @param pNode the {@link Node}.
	 * @param pOrientation the {@link Orientation} in which to rotate. Note that
	 *            {@link Orientation#HORIZONTAL} means from right to left.
	 * @param pRightLeftTopDown {@code true} if the animation should be from
	 *            right to left/top to down. {@code false} if it should be left
	 *            to right/bottom to up.
	 * @return the created {@link RotateTransition}.
	 */
	public static RotateTransition createFlipEndTransition(Node pNode, Orientation pOrientation, boolean pRightLeftTopDown)
	{
		double startingAngle = calculateEdgeAngle(pNode, pOrientation);
		
		RotateTransition endTransition = new RotateTransition(Duration.millis(466), pNode);
		endTransition.setAxis(getAxis(pOrientation));
		if (pRightLeftTopDown)
		{
			endTransition.setFromAngle(startingAngle + 180);
			endTransition.setToAngle(360);
		}
		else
		{
			
			endTransition.setFromAngle(startingAngle);
			endTransition.setToAngle(0);
		}
		endTransition.setInterpolator(Interpolator.EASE_OUT);
		
		return endTransition;
	}
	
	/**
	 * Creates a {@link RotateTransition} that is the first/start part of a flip
	 * animation.
	 * 
	 * @param pNode the {@link Node}.
	 * @param pOrientation the {@link Orientation} in which to rotate. Note that
	 *            {@link Orientation#HORIZONTAL} means from right to left.
	 * @param pRightLeftTopDown {@code true} if the animation should be from
	 *            right to left/top to down. {@code false} if it should be left
	 *            to right/bottom to up.
	 * @return the created {@link RotateTransition}.
	 */
	public static RotateTransition createFlipStartTransition(Node pNode, Orientation pOrientation, boolean pRightLeftTopDown)
	{
		double endingAngle = calculateEdgeAngle(pNode, pOrientation);
		
		RotateTransition transition = new RotateTransition(Duration.millis(466), pNode);
		transition.setAxis(getAxis(pOrientation));
		if (pRightLeftTopDown)
		{
			transition.setFromAngle(0);
			transition.setToAngle(endingAngle);
		}
		else
		{
			transition.setFromAngle(360);
			transition.setToAngle(endingAngle + 180);
		}
		transition.setInterpolator(Interpolator.EASE_IN);
		
		return transition;
	}
	
	/**
	 * Creates a simple shake animation.
	 * 
	 * @param pNode the {@link Node} to shake.
	 * @param pOrientation the {@link Orientation} of the movement.
	 * @param pStrength the strength (read distance) of the movement.
	 * @return the created shake animation.
	 */
	public static Timeline createShakeAnimation(Node pNode, Orientation pOrientation, double pStrength)
	{
		DoubleProperty property = pNode.translateXProperty();
		
		if (pOrientation == Orientation.VERTICAL)
		{
			property = pNode.translateYProperty();
		}
		
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(property, Double.valueOf(0))));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), new KeyValue(property, Double.valueOf(pStrength))));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), new KeyValue(property, Double.valueOf(-pStrength))));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), new KeyValue(property, Double.valueOf(pStrength))));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400), new KeyValue(property, Double.valueOf(-pStrength))));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(property, Double.valueOf(0))));
		
		return timeline;
	}
	
	/**
	 * Flips the given {@link Node} in the given direction until it is turned
	 * roughly 90 degrees, read it is not visible anymore. Then the first
	 * {@link Runnable} {@code pOnTippingPoint} will be executed which allows to
	 * swap the content or properties of the {@link Node} without the user
	 * seeing it. After that the rotation will continue back into its starting
	 * position, where {@code pOnFinished} will be called.
	 * <p>
	 * If there is a {@link PerspectiveCamera} is set on the {@link Scene}, this
	 * function will already choose the correct angle needed for the tipping
	 * point so that the user will not see the swap of the content.
	 * 
	 * @param pNode the {@link Node} to rotate.
	 * @param pOrientation the {@link Orientation} in which to rotate. Note that
	 *            {@link Orientation#HORIZONTAL} means from right to left.
	 * @param pRightLeftTopDown {@code true} if the animation should be from
	 *            right to left/top to down. {@code false} if it should be left
	 *            to right/bottom to up.
	 * @param pOnTippingPoint the {@link Runnable} to execute when the
	 *            {@link Node} is exactly edge on with the viewer. Can be
	 *            {@code null}.
	 * @param pOnFinished the {@link Runnable} to execute when the rotation has
	 *            finished. Can be {@code null}.
	 */
	public static void flip(Node pNode, Orientation pOrientation, boolean pRightLeftTopDown, Runnable pOnTippingPoint, Runnable pOnFinished)
	{
		double tippingPoint = calculateEdgeAngle(pNode, pOrientation);
		
		RotateTransition startTransition = createFlipStartTransition(pNode, pOrientation, pRightLeftTopDown);
		RotateTransition endTransition = createFlipEndTransition(pNode, pOrientation, pRightLeftTopDown);
		
		startTransition.setOnFinished(pActionEvent ->
		{
			if (pOnTippingPoint != null)
			{
				pOnTippingPoint.run();
			}
			
			pNode.setRotate(tippingPoint + 180);
			
			endTransition.play();
		});
		
		if (pOnFinished != null)
		{
			endTransition.setOnFinished(pActionEvent -> pOnFinished.run());
		}
		
		startTransition.play();
	}
	
	/**
	 * Gets the angle which the given distance from the center of the seen has
	 * as seen from the given {@link PerspectiveCamera}.
	 * 
	 * @param pScene the {@link Scene}.
	 * @param pPerspectiveCamera the {@link PerspectiveCamera}.
	 * @param pDistanceFromCenter the distance from the center.
	 * @return the angle.
	 */
	public static double getAngleFromCenter(Scene pScene, PerspectiveCamera pPerspectiveCamera, double pDistanceFromCenter)
	{
		return Math.toDegrees(Math.atan(pDistanceFromCenter / Math.abs(getCameraZPosition(pScene, pPerspectiveCamera))));
	}
	
	/**
	 * Returns the appropriate axis for the given {@link Orientation}.
	 * 
	 * @param pOrientation the {@link Orientation} for which to get the axis.
	 * @return the axis for the given {@link Orientation}.
	 */
	public static Point3D getAxis(Orientation pOrientation)
	{
		if (pOrientation == Orientation.HORIZONTAL)
		{
			return Y_AXIS;
		}
		else
		{
			return X_AXIS;
		}
	}
	
	/**
	 * Returns the Z position of the camera, basically meaning the distance
	 * relative to the {@link Scene}.
	 * 
	 * @param pScene the {@link Scene}.
	 * @param pPerspectiveCamera the {@link PerspectiveCamera} of which to get
	 *            the position.
	 * @return the Z position of the camera.
	 */
	public static double getCameraZPosition(Scene pScene, PerspectiveCamera pPerspectiveCamera)
	{
		final double tanOfHalfFOV = Math.tan(Math.toRadians(pPerspectiveCamera.getFieldOfView()) / 2.0);
		final double halfHeight = pScene.getHeight() / 2;
		final double focalLength = halfHeight / tanOfHalfFOV;
		return -1.0 * focalLength;
	}
	
	/**
	 * Returns the distance of the center of the {@link Node} from the center of
	 * the given {@link Scene} in the given {@link Orientation}.
	 * 
	 * @param pScene the {@link Scene}.
	 * @param pNode the {@link Node}.
	 * @param pOrientation the {@link Orientation}.
	 * @return the distance from the center.
	 */
	public static double getDistanceFromCenter(Scene pScene, Node pNode, Orientation pOrientation)
	{
		double sceneCenter = 0;
		if (pOrientation == Orientation.HORIZONTAL)
		{
			sceneCenter = pScene.getWidth() / 2;
		}
		else
		{
			sceneCenter = pScene.getHeight() / 2;
		}
		
		Bounds nodeBounds = pNode.localToScene(pNode.getBoundsInLocal());
		double nodeCenter = 0;
		if (pOrientation == Orientation.HORIZONTAL)
		{
			nodeCenter = nodeBounds.getMinX() + (nodeBounds.getWidth() / 2);
		}
		else
		{
			nodeCenter = nodeBounds.getMinY() + (nodeBounds.getHeight() / 2);
		}
		
		if (pOrientation == Orientation.HORIZONTAL)
		{
			return sceneCenter - nodeCenter;
		}
		else
		{
			return nodeCenter - sceneCenter;
		}
	}
	
	/**
	 * A simple shake animation.
	 * 
	 * @param pNode the {@link Node} to shake.
	 * @param pOrientation the {@link Orientation} of the movement.
	 * @param pStrength the strength (read distance) of the movement.
	 * @param pOnFinished the {@link Runnable} to execute when the rotation has
	 *            finished. Can be {@code null}.
	 */
	public static void shake(Node pNode, Orientation pOrientation, double pStrength, Runnable pOnFinished)
	{
		Timeline timeline = createShakeAnimation(pNode, pOrientation, pStrength);
		
		if (pOnFinished != null)
		{
			timeline.setOnFinished((pActionEvent) ->
			{
				pOnFinished.run();
			});
		}
		
		timeline.play();
	}
}
