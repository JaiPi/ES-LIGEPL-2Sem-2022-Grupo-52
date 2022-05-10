package org.jfree.chart.renderer.xy;


import java.io.Serializable;

public class XYBarRendererProduct implements Serializable, Cloneable {
	private double shadowXOffset;
	private double shadowYOffset;

	public double getShadowXOffset() {
		return shadowXOffset;
	}

	public void setShadowXOffset2(double shadowXOffset) {
		this.shadowXOffset = shadowXOffset;
	}

	public double getShadowYOffset() {
		return shadowYOffset;
	}

	public void setShadowYOffset2(double shadowYOffset) {
		this.shadowYOffset = shadowYOffset;
	}

	/**
	* Sets the x-offset for the bar shadow and sends a {@link RendererChangeEvent}  to all registered listeners.
	* @param offset   the offset.
	*/
	public void setShadowXOffset(double offset, XYBarRenderer xYBarRenderer) {
		this.shadowXOffset = offset;
		xYBarRenderer.fireChangeEvent();
	}

	/**
	* Sets the y-offset for the bar shadow and sends a {@link RendererChangeEvent}  to all registered listeners.
	* @param offset   the offset.
	*/
	public void setShadowYOffset(double offset, XYBarRenderer xYBarRenderer) {
		this.shadowYOffset = offset;
		xYBarRenderer.fireChangeEvent();
	}

	public Object clone() throws CloneNotSupportedException {
		return (XYBarRendererProduct) super.clone();
	}
}