package org.jfree.chart.renderer.xy;


import org.jfree.chart.internal.Args;
import java.io.Serializable;

public class XYBarRendererProduct3 implements Serializable, Cloneable {
	private double base;
	private boolean useYInterval;
	private double margin;
	private XYBarPainter barPainter;
	private boolean shadowsVisible;
	private double barAlignmentFactor;

	public double getBase() {
		return base;
	}

	public void setBase2(double base) {
		this.base = base;
	}

	public boolean getUseYInterval() {
		return useYInterval;
	}

	public void setUseYInterval2(boolean useYInterval) {
		this.useYInterval = useYInterval;
	}

	public double getMargin() {
		return margin;
	}

	public void setMargin2(double margin) {
		this.margin = margin;
	}

	public XYBarPainter getBarPainter() {
		return barPainter;
	}

	public void setBarPainter2(XYBarPainter barPainter) {
		this.barPainter = barPainter;
	}

	public boolean getShadowsVisible() {
		return shadowsVisible;
	}

	public void setShadowsVisible(boolean shadowsVisible) {
		this.shadowsVisible = shadowsVisible;
	}

	public double getBarAlignmentFactor() {
		return barAlignmentFactor;
	}

	public void setBarAlignmentFactor2(double barAlignmentFactor) {
		this.barAlignmentFactor = barAlignmentFactor;
	}

	/**
	* Sets the base value for the bars and sends a  {@link RendererChangeEvent} to all registered listeners.  The base value is not used if the dataset's y-interval is being used to determine the bar length.
	* @param base   the new base value.
	* @see #getBase()
	* @see #getUseYInterval()
	*/
	public void setBase(double base, XYBarRenderer xYBarRenderer) {
		this.base = base;
		xYBarRenderer.fireChangeEvent();
	}

	/**
	* Sets the flag that determines whether the y-interval from the dataset is used to calculate the length of each bar, and sends a {@link RendererChangeEvent}  to all registered listeners.
	* @param use   the flag.
	* @see #getUseYInterval()
	*/
	public void setUseYInterval(boolean use, XYBarRenderer xYBarRenderer) {
		if (this.useYInterval != use) {
			this.useYInterval = use;
			xYBarRenderer.fireChangeEvent();
		}
	}

	/**
	* Sets the percentage amount by which the bars are trimmed and sends a {@link RendererChangeEvent}  to all registered listeners.
	* @param margin   the new margin.
	* @see #getMargin()
	*/
	public void setMargin(double margin, XYBarRenderer xYBarRenderer) {
		this.margin = margin;
		xYBarRenderer.fireChangeEvent();
	}

	/**
	* Sets the bar painter and sends a  {@link RendererChangeEvent}  to all registered listeners.
	* @param painter   the painter ( {@code  null}  not permitted).
	*/
	public void setBarPainter(XYBarPainter painter, XYBarRenderer xYBarRenderer) {
		Args.nullNotPermitted(painter, "painter");
		this.barPainter = painter;
		xYBarRenderer.fireChangeEvent();
	}

	/**
	* Sets the flag that controls whether or not the renderer draws shadows for the bars, and sends a {@link RendererChangeEvent}  to all registered listeners.
	* @param visible   the new flag value.
	*/
	public void setShadowVisible(boolean visible, XYBarRenderer xYBarRenderer) {
		this.shadowsVisible = visible;
		xYBarRenderer.fireChangeEvent();
	}

	/**
	* Sets the bar alignment factor and sends a  {@link RendererChangeEvent} to all registered listeners.  If the alignment factor is outside the range 0.0 to 1.0, no alignment will be performed by the renderer.
	* @param factor   the factor.
	*/
	public void setBarAlignmentFactor(double factor, XYBarRenderer xYBarRenderer) {
		this.barAlignmentFactor = factor;
		xYBarRenderer.fireChangeEvent();
	}

	public Object clone() throws CloneNotSupportedException {
		return (XYBarRendererProduct3) super.clone();
	}
}