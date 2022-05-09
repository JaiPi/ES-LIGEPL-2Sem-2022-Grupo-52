package org.jfree.chart.renderer;


import java.util.Map;
import java.io.Serializable;

public class AbstractRendererProduct2 implements Serializable, Cloneable {
	private Map<Integer, Boolean> seriesVisibleInLegendMap;
	private boolean defaultSeriesVisibleInLegend;

	public Map<Integer, Boolean> getSeriesVisibleInLegendMap() {
		return seriesVisibleInLegendMap;
	}

	public void setSeriesVisibleInLegendMap(Map<Integer, Boolean> seriesVisibleInLegendMap) {
		this.seriesVisibleInLegendMap = seriesVisibleInLegendMap;
	}

	public boolean getDefaultSeriesVisibleInLegend() {
		return defaultSeriesVisibleInLegend;
	}

	public void setDefaultSeriesVisibleInLegend2(boolean defaultSeriesVisibleInLegend) {
		this.defaultSeriesVisibleInLegend = defaultSeriesVisibleInLegend;
	}

	/**
	* Returns  {@code  true}  if the series should be shown in the legend, and  {@code  false}  otherwise.
	* @param series   the series index.
	* @return  A boolean.
	*/
	public boolean isSeriesVisibleInLegend(int series) {
		boolean result = this.defaultSeriesVisibleInLegend;
		Boolean b = this.seriesVisibleInLegendMap.get(series);
		if (b != null) {
			result = b;
		}
		return result;
	}

	/**
	* Sets the default visibility in the legend and, if requested, sends a  {@link RendererChangeEvent}  to all registered listeners.
	* @param visible   the visibility.
	* @param notify   notify listeners?
	* @see #getDefaultSeriesVisibleInLegend()
	*/
	public void setDefaultSeriesVisibleInLegend(boolean visible, boolean notify, AbstractRenderer abstractRenderer) {
		this.defaultSeriesVisibleInLegend = visible;
		if (notify) {
			abstractRenderer.fireChangeEvent();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (AbstractRendererProduct2) super.clone();
	}
}