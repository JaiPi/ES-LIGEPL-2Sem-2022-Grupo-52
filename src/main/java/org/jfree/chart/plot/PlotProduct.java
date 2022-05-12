package org.jfree.chart.plot;


import java.awt.Image;
import org.jfree.chart.api.RectangleAlignment;
import org.jfree.chart.internal.Args;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Shape;
import java.io.Serializable;
import org.jfree.chart.api.PublicCloneable;

public class PlotProduct implements Serializable, PublicCloneable {
	private transient Image backgroundImage;
	private RectangleAlignment backgroundImageAlignment = RectangleAlignment.FILL;
	private float backgroundImageAlpha = 0.5f;

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage2(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public RectangleAlignment getBackgroundImageAlignment() {
		return backgroundImageAlignment;
	}

	public float getBackgroundImageAlpha() {
		return backgroundImageAlpha;
	}

	/**
	* Sets the background image for the plot and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param image   the image ( {@code  null}  permitted).
	* @see #getBackgroundImage()
	*/
	public void setBackgroundImage(Image image, Plot plot) {
		this.backgroundImage = image;
		plot.fireChangeEvent();
	}

	/**
	* Sets the alignment for the background image and sends a {@link PlotChangeEvent}  to all registered listeners.  
	* @param alignment   the alignment ( {@code  null}  not permitted).
	* @see #getBackgroundImageAlignment()
	*/
	public void setBackgroundImageAlignment(RectangleAlignment alignment, Plot plot) {
		Args.nullNotPermitted(alignment, "alignment");
		if (this.backgroundImageAlignment != alignment) {
			this.backgroundImageAlignment = alignment;
			plot.fireChangeEvent();
		}
	}

	/**
	* Sets the alpha transparency used when drawing the background image.
	* @param alpha   the alpha transparency (in the range 0.0f to 1.0f, where 0.0f is fully transparent, and 1.0f is fully opaque).
	* @throws IllegalArgumentException  if  {@code  alpha}  is not within the specified range.
	* @see #getBackgroundImageAlpha()
	*/
	public void setBackgroundImageAlpha(float alpha, Plot plot) {
		if (alpha < 0.0f || alpha > 1.0f) {
			throw new IllegalArgumentException("The 'alpha' value must be in the range 0.0f to 1.0f.");
		}
		if (this.backgroundImageAlpha != alpha) {
			this.backgroundImageAlpha = alpha;
			plot.fireChangeEvent();
		}
	}

	/**
	* Draws the background image (if there is one) aligned within the specified area.
	* @param g2   the graphics device.
	* @param area   the area.
	* @see #getBackgroundImage()
	* @see #getBackgroundImageAlignment()
	* @see #getBackgroundImageAlpha()
	*/
	public void drawBackgroundImage(Graphics2D g2, Rectangle2D area) {
		if (this.backgroundImage == null) {
			return;
		}
		Composite savedComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.backgroundImageAlpha));
		Rectangle2D dest = new Rectangle2D.Double(0.0, 0.0, this.backgroundImage.getWidth(null),
				this.backgroundImage.getHeight(null));
		this.backgroundImageAlignment.align(dest, area);
		Shape savedClip = g2.getClip();
		g2.clip(area);
		g2.drawImage(this.backgroundImage, (int) dest.getX(), (int) dest.getY(), (int) dest.getWidth() + 1,
				(int) dest.getHeight() + 1, null);
		g2.setClip(savedClip);
		g2.setComposite(savedComposite);
	}

	public Object clone() throws CloneNotSupportedException {
		return (PlotProduct) super.clone();
	}
}