package org.jfree.chart.renderer;


import java.util.Map;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.internal.Args;
import java.io.Serializable;

public class AbstractRendererProduct implements Serializable, Cloneable {
	private Map<Integer, ItemLabelPosition> negativeItemLabelPositionMap;
	private ItemLabelPosition defaultNegativeItemLabelPosition;

	public Map<Integer, ItemLabelPosition> getNegativeItemLabelPositionMap() {
		return negativeItemLabelPositionMap;
	}

	public void setNegativeItemLabelPositionMap(Map<Integer, ItemLabelPosition> negativeItemLabelPositionMap) {
		this.negativeItemLabelPositionMap = negativeItemLabelPositionMap;
	}

	public ItemLabelPosition getDefaultNegativeItemLabelPosition() {
		return defaultNegativeItemLabelPosition;
	}

	public void setDefaultNegativeItemLabelPosition2(ItemLabelPosition defaultNegativeItemLabelPosition) {
		this.defaultNegativeItemLabelPosition = defaultNegativeItemLabelPosition;
	}

	/**
	* Returns the item label position for all negative values in a series.
	* @param series   the series index (zero-based).
	* @return  The item label position (never  {@code  null} ).
	* @see #setSeriesNegativeItemLabelPosition(int,ItemLabelPosition)
	*/
	public ItemLabelPosition getSeriesNegativeItemLabelPosition(int series) {
		ItemLabelPosition position = this.negativeItemLabelPositionMap.get(series);
		if (position == null) {
			position = this.defaultNegativeItemLabelPosition;
		}
		return position;
	}

	/**
	* Sets the default negative item label position and, if requested, sends a {@link RendererChangeEvent}  to all registered listeners.
	* @param position   the position ( {@code  null}  not permitted).
	* @param notify   notify registered listeners?
	* @see #getDefaultNegativeItemLabelPosition()
	*/
	public void setDefaultNegativeItemLabelPosition(ItemLabelPosition position, boolean notify,
			AbstractRenderer abstractRenderer) {
		Args.nullNotPermitted(position, "position");
		this.defaultNegativeItemLabelPosition = position;
		if (notify) {
			abstractRenderer.fireChangeEvent();
		}
	}

	/**
	* Sets the item label position for negative values in a series and (if requested) sends a  {@link RendererChangeEvent}  to all registered listeners.
	* @param series   the series index (zero-based).
	* @param position   the position ( {@code  null}  permitted).
	* @param notify   notify registered listeners?
	* @see #getSeriesNegativeItemLabelPosition(int)
	*/
	public void setSeriesNegativeItemLabelPosition(int series, ItemLabelPosition position, boolean notify,
			AbstractRenderer abstractRenderer) {
		this.negativeItemLabelPositionMap.put(series, position);
		if (notify) {
			abstractRenderer.fireChangeEvent();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (AbstractRendererProduct) super.clone();
	}
}