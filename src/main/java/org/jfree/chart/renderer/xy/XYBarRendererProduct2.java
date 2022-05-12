package org.jfree.chart.renderer.xy;


import org.jfree.chart.labels.ItemLabelPosition;
import java.io.Serializable;

public class XYBarRendererProduct2 implements Serializable, Cloneable {
	private ItemLabelPosition positiveItemLabelPositionFallback;
	private ItemLabelPosition negativeItemLabelPositionFallback;

	public ItemLabelPosition getPositiveItemLabelPositionFallback() {
		return positiveItemLabelPositionFallback;
	}

	public ItemLabelPosition getNegativeItemLabelPositionFallback() {
		return negativeItemLabelPositionFallback;
	}

	/**
	* Sets the fallback position for positive item labels that don't fit within a bar, and sends a  {@link RendererChangeEvent}  to all registered listeners.
	* @param position   the position ( {@code  null}  permitted).
	* @see #getPositiveItemLabelPositionFallback()
	*/
	public void setPositiveItemLabelPositionFallback(ItemLabelPosition position, XYBarRenderer xYBarRenderer) {
		this.positiveItemLabelPositionFallback = position;
		xYBarRenderer.fireChangeEvent();
	}

	/**
	* Sets the fallback position for negative item labels that don't fit within a bar, and sends a  {@link RendererChangeEvent}  to all registered listeners.
	* @param position   the position ( {@code  null}  permitted).
	* @see #getNegativeItemLabelPositionFallback()
	*/
	public void setNegativeItemLabelPositionFallback(ItemLabelPosition position, XYBarRenderer xYBarRenderer) {
		this.negativeItemLabelPositionFallback = position;
		xYBarRenderer.fireChangeEvent();
	}

	public Object clone() throws CloneNotSupportedException {
		return (XYBarRendererProduct2) super.clone();
	}
}