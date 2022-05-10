package org.jfree.chart.renderer.category;


import org.jfree.chart.labels.ItemLabelPosition;
import java.io.Serializable;

public class BarRendererProduct implements Serializable, Cloneable {
	private ItemLabelPosition positiveItemLabelPositionFallback;
	private ItemLabelPosition negativeItemLabelPositionFallback;

	public ItemLabelPosition getPositiveItemLabelPositionFallback() {
		return positiveItemLabelPositionFallback;
	}

	public void setPositiveItemLabelPositionFallback2(ItemLabelPosition positiveItemLabelPositionFallback) {
		this.positiveItemLabelPositionFallback = positiveItemLabelPositionFallback;
	}

	public ItemLabelPosition getNegativeItemLabelPositionFallback() {
		return negativeItemLabelPositionFallback;
	}

	public void setNegativeItemLabelPositionFallback2(ItemLabelPosition negativeItemLabelPositionFallback) {
		this.negativeItemLabelPositionFallback = negativeItemLabelPositionFallback;
	}

	/**
	* Sets the fallback position for positive item labels that don't fit within a bar, and sends a  {@link RendererChangeEvent}  to all registered listeners.
	* @param position   the position ( {@code  null}  permitted).
	* @see #getPositiveItemLabelPositionFallback()
	*/
	public void setPositiveItemLabelPositionFallback(ItemLabelPosition position, BarRenderer barRenderer) {
		this.positiveItemLabelPositionFallback = position;
		barRenderer.fireChangeEvent();
	}

	/**
	* Sets the fallback position for negative item labels that don't fit within a bar, and sends a  {@link RendererChangeEvent}  to all registered listeners.
	* @param position   the position ( {@code  null}  permitted).
	* @see #getNegativeItemLabelPositionFallback()
	*/
	public void setNegativeItemLabelPositionFallback(ItemLabelPosition position, BarRenderer barRenderer) {
		this.negativeItemLabelPositionFallback = position;
		barRenderer.fireChangeEvent();
	}

	public Object clone() throws CloneNotSupportedException {
		return (BarRendererProduct) super.clone();
	}
}