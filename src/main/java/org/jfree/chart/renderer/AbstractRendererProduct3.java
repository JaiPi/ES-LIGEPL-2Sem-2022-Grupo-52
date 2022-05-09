package org.jfree.chart.renderer;


import java.util.Map;
import org.jfree.chart.event.RendererChangeEvent;
import java.io.Serializable;

public class AbstractRendererProduct3 implements Serializable, Cloneable {
	private Map<Integer, Boolean> seriesVisibleMap;
	private boolean defaultSeriesVisible;

	public Map<Integer, Boolean> getSeriesVisibleMap() {
		return seriesVisibleMap;
	}

	public void setSeriesVisibleMap(Map<Integer, Boolean> seriesVisibleMap) {
		this.seriesVisibleMap = seriesVisibleMap;
	}

	public boolean getDefaultSeriesVisible() {
		return defaultSeriesVisible;
	}

	public void setDefaultSeriesVisible2(boolean defaultSeriesVisible) {
		this.defaultSeriesVisible = defaultSeriesVisible;
	}

	/**
	* Returns a boolean that indicates whether or not the specified series should be drawn.  In fact this method should be named  lookupSeriesVisible() to be consistent with the other series attributes and avoid confusion with the getSeriesVisible() method.
	* @param series   the series index.
	* @return  A boolean.
	*/
	public boolean isSeriesVisible(int series) {
		boolean result = this.defaultSeriesVisible;
		Boolean b = this.seriesVisibleMap.get(series);
		if (b != null) {
			result = b;
		}
		return result;
	}

	/**
	* Sets the default series visibility and, if requested, sends a  {@link RendererChangeEvent}  to all registered listeners.
	* @param visible   the visibility.
	* @param notify   notify listeners?
	* @see #getDefaultSeriesVisible()
	*/
	public void setDefaultSeriesVisible(boolean visible, boolean notify, AbstractRenderer abstractRenderer) {
		this.defaultSeriesVisible = visible;
		if (notify) {
			RendererChangeEvent e = new RendererChangeEvent(abstractRenderer, true);
			abstractRenderer.notifyListeners(e);
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (AbstractRendererProduct3) super.clone();
	}
}