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
package com.sibvisions.rad.ui.javafx.impl.layout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IFormLayout;

import javafx.geometry.Insets;
import javafx.scene.Node;

import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane;
import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane.Anchor;
import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane.Constraints;
import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane.HorizontalAlignment;
import com.sibvisions.rad.ui.javafx.ext.panes.FXFormPane.VerticalAlignment;
import com.sibvisions.rad.ui.javafx.impl.JavaFXInsets;
import com.sibvisions.rad.ui.javafx.impl.JavaFXResource;

/**
 * The {@link JavaFXFormLayout} is the JavaFX specific implementation of
 * {@link IFormLayout}.
 * 
 * @author Robert Zenz
 * @see IFormLayout
 * @see FXFormPane
 */
public class JavaFXFormLayout extends JavaFXAbstractLayoutContainerHybrid<FXFormPane, IFormLayout.IConstraints> implements IFormLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Store/Cache for the anchor wrappers. */
	private Map<Anchor, WeakReference<JavaFXAnchor>> anchors;
	
	/** Store/Cache for the constraints wrappers. */
	private Map<Constraints, WeakReference<JavaFXConstraints>> constraints;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JavaFXFormLayout}.
	 */
	public JavaFXFormLayout()
	{
		super(new FXFormPane());
		
		anchors = new HashMap<>();
		constraints = new HashMap<>();
		
		horizontalGap = (int)resource.getHGap();
		verticalGap = (int)resource.getVGap();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(IComponent pComponent, Object pConstraints, int pIndex)
	{
		if (pConstraints instanceof JavaFXConstraints)
		{
			resource.addChild(pIndex, (Node)pComponent.getResource(), (Constraints)((JavaFXConstraints)pConstraints).getResource());
		}
		else if (NEWLINE.equals(pConstraints))
		{
			resource.addChildInNewLine(pIndex, (Node)pComponent.getResource());
		}
		else
		{
			resource.addChild(pIndex, (Node)pComponent.getResource());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor createAnchor(IAnchor pRelatedAnchor)
	{
		return getAnchor(new FXFormPane.Anchor((FXFormPane.Anchor)pRelatedAnchor.getResource()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor createAnchor(IAnchor pRelatedAnchor, IAnchor pSecondRelatedAnchor, float pRelativePosition)
	{
		return getAnchor(new FXFormPane.Anchor((FXFormPane.Anchor)pRelatedAnchor.getResource(), (FXFormPane.Anchor)pSecondRelatedAnchor.getResource(), pRelativePosition));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor createAnchor(IAnchor pRelatedAnchor, int pPosition)
	{
		return getAnchor(new FXFormPane.Anchor((FXFormPane.Anchor)pRelatedAnchor.getResource(), pPosition));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor getBottomAnchor()
	{
		return getAnchor(resource.getBottomAnchor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor getBottomMarginAnchor()
	{
		return getAnchor(resource.getBottomMarginAnchor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IConstraints getConstraints(IAnchor pTopAnchor, IAnchor pLeftAnchor, IAnchor pBottomAnchor, IAnchor pRightAnchor)
	{
		return getConstraints(new FXFormPane.Constraints(
				pTopAnchor == null ? null : (FXFormPane.Anchor)pTopAnchor.getResource(),
				pLeftAnchor == null ? null : (FXFormPane.Anchor)pLeftAnchor.getResource(),
				pBottomAnchor == null ? null : (FXFormPane.Anchor)pBottomAnchor.getResource(),
				pRightAnchor == null ? null : (FXFormPane.Anchor)pRightAnchor.getResource()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IConstraints getConstraints(IComponent pComp)
	{
		return getConstraints(resource.getConstraint((Node)pComp.getResource()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IConstraints getConstraints(int pColumn, int pRow)
	{
		return getConstraints(resource.createConstraint(pColumn, pRow));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IConstraints getConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
	{
		return getConstraints(resource.createConstraint(pBeginColumn, pBeginRow, pEndColumn, pEndRow));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHorizontalAlignment()
	{
		switch (resource.getHorizontalAlignment())
		{
			case CENTER:
				return ALIGN_CENTER;
				
			case LEFT:
				return ALIGN_LEFT;
				
			case RIGHT:
				return ALIGN_RIGHT;
				
			case STRETCH:
				return ALIGN_STRETCH;
				
			default:
				return ALIGN_STRETCH;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor[] getHorizontalAnchors()
	{
		return getAnchors(resource.getHorizontalAnchors());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor getLeftAnchor()
	{
		return getAnchor(resource.getLeftAnchor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor getLeftMarginAnchor()
	{
		return getAnchor(resource.getLeftMarginAnchor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IInsets getMargins()
	{
		return new JavaFXInsets(resource.getMargins());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNewlineCount()
	{
		return resource.getNewlineCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor getRightAnchor()
	{
		return getAnchor(resource.getRightAnchor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor getRightMarginAnchor()
	{
		return getAnchor(resource.getRightMarginAnchor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor getTopAnchor()
	{
		return getAnchor(resource.getTopAnchor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor getTopMarginAnchor()
	{
		return getAnchor(resource.getTopMarginAnchor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVerticalAlignment()
	{
		switch (resource.getVerticalAlignment())
		{
			case BOTTOM:
				return ALIGN_BOTTOM;
				
			case CENTER:
				return ALIGN_CENTER;
				
			case STRETCH:
				return ALIGN_STRETCH;
				
			case TOP:
				return ALIGN_TOP;
				
			default:
				return ALIGN_STRETCH;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAnchor[] getVerticalAnchors()
	{
		return getAnchors(resource.getVerticalAnchors());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReverseOrderNeeded()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(IComponent pComponent)
	{
		resource.removeChild((Node)pComponent.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAll()
	{
		resource.removeAllChildren();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConstraints(IComponent pComp, IConstraints pConstraints)
	{
		resource.setConstraint((Node)pComp.getResource(), (FXFormPane.Constraints)pConstraints.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		switch (pHorizontalAlignment)
		{
			case ALIGN_CENTER:
				resource.setHorizontalAlignment(HorizontalAlignment.CENTER);
				break;
				
			case ALIGN_DEFAULT:
				resource.setHorizontalAlignment(HorizontalAlignment.STRETCH);
				break;
				
			case ALIGN_LEFT:
				resource.setHorizontalAlignment(HorizontalAlignment.LEFT);
				break;
				
			case ALIGN_RIGHT:
				resource.setHorizontalAlignment(HorizontalAlignment.RIGHT);
				break;
				
			case ALIGN_STRETCH:
				resource.setHorizontalAlignment(HorizontalAlignment.STRETCH);
				break;
				
			default:
				throw new IllegalArgumentException("Invalid alignment " + pHorizontalAlignment);
				
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMargins(IInsets pMargins)
	{
		resource.setMargins((Insets)pMargins.getResource());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNewlineCount(int pNewlineCount)
	{
		resource.setNewlineCount(pNewlineCount);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		switch (pVerticalAlignment)
		{
			case ALIGN_BOTTOM:
				resource.setVerticalAlignment(VerticalAlignment.BOTTOM);
				break;
				
			case ALIGN_CENTER:
				resource.setVerticalAlignment(VerticalAlignment.CENTER);
				break;
				
			case ALIGN_DEFAULT:
				resource.setVerticalAlignment(VerticalAlignment.STRETCH);
				break;
				
			case ALIGN_STRETCH:
				resource.setVerticalAlignment(VerticalAlignment.STRETCH);
				break;
				
			case ALIGN_TOP:
				resource.setVerticalAlignment(VerticalAlignment.TOP);
				break;
				
			default:
				throw new IllegalArgumentException("Invalid alignment " + pVerticalAlignment);
				
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateGaps()
	{
		resource.setHGap(horizontalGap);
		resource.setVGap(verticalGap);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link IAnchor} for the given {@link Anchor}.
	 * 
	 * @param pAnchor the {@link Anchor}.
	 * @return the {@link IAnchor}.
	 */
	private IAnchor getAnchor(Anchor pAnchor)
	{
		if (pAnchor == null)
		{
			return null;
		}
		
		if (anchors.containsKey(pAnchor))
		{
			JavaFXAnchor anchor = anchors.get(pAnchor).get();
			
			if (anchor != null)
			{
				return anchor;
			}
		}
		
		JavaFXAnchor anchor = new JavaFXAnchor(this, pAnchor);
		
		anchors.put(pAnchor, new WeakReference<>(anchor));
		
		return anchor;
	}
	
	/**
	 * Gets the {@link IAnchor}s for the given {@link Anchor}s.
	 * 
	 * @param pAnchors the {@link Anchor}s.
	 * @return the {@link IAnchor}s.
	 */
	private IAnchor[] getAnchors(Anchor... pAnchors)
	{
		List<IAnchor> convertedAnchors = new ArrayList<>();
		
		for (Anchor anchor : pAnchors)
		{
			convertedAnchors.add(getAnchor(anchor));
		}
		
		return convertedAnchors.toArray(new IAnchor[convertedAnchors.size()]);
	}
	
	/**
	 * Gets the {@link IConstraints} for the given {@link Constraints}.
	 * 
	 * @param pConstraints the {@link Constraints}.
	 * @return the {@link IConstraints}.
	 */
	private IConstraints getConstraints(Constraints pConstraints)
	{
		if (pConstraints == null)
		{
			return null;
		}
		
		if (constraints.containsKey(pConstraints))
		{
			JavaFXConstraints constraint = constraints.get(pConstraints).get();
			
			if (constraint != null)
			{
				return constraint;
			}
		}
		
		JavaFXConstraints constraint = new JavaFXConstraints(this, pConstraints);
		
		constraints.put(pConstraints, new WeakReference<>(constraint));
		
		return constraint;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link JavaFXAnchor} is the JavaFX specific implementation of
	 * {@link IAnchor}.
	 * 
	 * @author Robert Zenz
	 */
	public static class JavaFXAnchor extends JavaFXResource<FXFormPane.Anchor> implements IAnchor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The border {@link JavaFXAnchor}. */
		private JavaFXAnchor borderAnchor;
		
		/**
		 * The {@link JavaFXFormLayout} this {@link JavaFXAnchor} belongs to.
		 */
		private JavaFXFormLayout layout;
		
		/** The related {@link JavaFXAnchor}. */
		private JavaFXAnchor relatedAnchor;
		
		/** The second related {@link JavaFXAnchor}. */
		private JavaFXAnchor secondRelatedAnchor;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link JavaFXAnchor}.
		 *
		 * @param pLayout the layout.
		 * @param resource the resource.
		 */
		public JavaFXAnchor(JavaFXFormLayout pLayout, Anchor resource)
		{
			super(resource);
			
			layout = pLayout;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getAbsolutePosition()
		{
			return (int)Math.round(resource.getAbsolutePosition());
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IAnchor getBorderAnchor()
		{
			if (borderAnchor == null)
			{
				borderAnchor = new JavaFXAnchor(layout, resource.getBorderAnchor());
			}
			
			return borderAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IFormLayout getLayout()
		{
			return layout;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getOrientation()
		{
			switch (resource.getOrientation())
			{
				case HORIZONTAL:
					return HORIZONTAL;
					
				case VERTICAL:
					return VERTICAL;
					
				default:
					return HORIZONTAL;
					
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getPosition()
		{
			return (int)Math.round(resource.getPosition());
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IAnchor getRelatedAnchor()
		{
			if (relatedAnchor == null)
			{
				relatedAnchor = new JavaFXAnchor(layout, resource.getRelatedAnchor());
			}
			
			return relatedAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getRelativePosition()
		{
			return (float)resource.getRelativePosition();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IAnchor getSecondRelatedAnchor()
		{
			if (secondRelatedAnchor == null)
			{
				secondRelatedAnchor = new JavaFXAnchor(layout, resource.getSecondRelatedAnchor());
			}
			
			return secondRelatedAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAutoSize()
		{
			return resource.isAutoSize();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setAutoSize(boolean pAutoSize)
		{
			resource.setAutoSize(pAutoSize);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setPosition(int pPosition)
		{
			resource.setPosition(pPosition);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setRelativePosition(float pRelativePosition)
		{
			resource.setRelativePosition(pRelativePosition);
		}
		
	} // JavaFXAnchor
	
	/**
	 * The {@link JavaFXConstraints} is the JavaFX specific implementation of
	 * {@link IConstraints}.
	 * 
	 * @author Robert Zenz
	 */
	public static class JavaFXConstraints extends JavaFXResource<FXFormPane.Constraints> implements IConstraints
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The bottom {@link IAnchor}. */
		private IAnchor bottomAnchor;
		
		/** The {@link JavaFXFormLayout} this constraint belongs to. */
		private JavaFXFormLayout layout;
		
		/** The left {@link IAnchor}. */
		private IAnchor leftAnchor;
		
		/** The right {@link IAnchor}. */
		private IAnchor rightAnchor;
		
		/** The top {@link IAnchor}. */
		private IAnchor topAnchor;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link JavaFXConstraints}.
		 *
		 * @param pLayout the layout.
		 * @param pConstraint the constraint.
		 */
		public JavaFXConstraints(JavaFXFormLayout pLayout, FXFormPane.Constraints pConstraint)
		{
			super(pConstraint);
			
			layout = pLayout;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IAnchor getBottomAnchor()
		{
			if (bottomAnchor == null && resource.getBottomAnchor() != null)
			{
				bottomAnchor = new JavaFXAnchor(layout, resource.getBottomAnchor());
			}
			
			return bottomAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IAnchor getLeftAnchor()
		{
			if (leftAnchor == null && resource.getLeftAnchor() != null)
			{
				leftAnchor = new JavaFXAnchor(layout, resource.getLeftAnchor());
			}
			
			return leftAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IAnchor getRightAnchor()
		{
			if (rightAnchor == null && resource.getRightAnchor() != null)
			{
				rightAnchor = new JavaFXAnchor(layout, resource.getRightAnchor());
			}
			
			return rightAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public IAnchor getTopAnchor()
		{
			if (topAnchor == null && resource.getTopAnchor() != null)
			{
				topAnchor = new JavaFXAnchor(layout, resource.getTopAnchor());
			}
			
			return topAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setBottomAnchor(IAnchor pBottomAnchor)
		{
			bottomAnchor = pBottomAnchor;
			
			if (bottomAnchor != null)
			{
				resource.setBottomAnchor((Anchor)bottomAnchor.getResource());
			}
			else
			{
				resource.setBottomAnchor(null);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setLeftAnchor(IAnchor pLeftAnchor)
		{
			leftAnchor = pLeftAnchor;
			
			if (leftAnchor != null)
			{
				resource.setLeftAnchor((Anchor)leftAnchor.getResource());
			}
			else
			{
				resource.setLeftAnchor(null);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setRightAnchor(IAnchor pRightAnchor)
		{
			rightAnchor = pRightAnchor;
			
			if (rightAnchor != null)
			{
				resource.setRightAnchor((Anchor)rightAnchor.getResource());
			}
			else
			{
				resource.setRightAnchor(null);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setTopAnchor(IAnchor pTopAnchor)
		{
			topAnchor = pTopAnchor;
			
			if (topAnchor != null)
			{
				resource.setTopAnchor((Anchor)topAnchor.getResource());
			}
			else
			{
				resource.setTopAnchor(null);
			}
		}
		
	} // JavaFXConstraints
	
}
