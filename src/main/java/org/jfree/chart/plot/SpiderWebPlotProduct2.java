package org.jfree.chart.plot;


import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.urls.CategoryURLGenerator;
import java.io.Serializable;

public class SpiderWebPlotProduct2 implements Serializable, Cloneable {
	private boolean webFilled = true;
	private CategoryToolTipGenerator toolTipGenerator;
	private CategoryURLGenerator urlGenerator;

	public boolean getWebFilled() {
		return webFilled;
	}

	public CategoryToolTipGenerator getToolTipGenerator() {
		return toolTipGenerator;
	}

	public CategoryURLGenerator getUrlGenerator() {
		return urlGenerator;
	}

	/**
	* Sets the webFilled flag and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param flag   the flag.
	* @see #isWebFilled()
	*/
	public void setWebFilled(boolean flag, SpiderWebPlot spiderWebPlot) {
		this.webFilled = flag;
		spiderWebPlot.fireChangeEvent();
	}

	/**
	* Sets the tool tip generator for the plot and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param generator   the generator ( {@code  null}  permitted).
	* @see #getToolTipGenerator()
	*/
	public void setToolTipGenerator(CategoryToolTipGenerator generator, SpiderWebPlot spiderWebPlot) {
		this.toolTipGenerator = generator;
		spiderWebPlot.fireChangeEvent();
	}

	/**
	* Sets the URL generator for the plot and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param generator   the generator ( {@code  null}  permitted).
	* @see #getURLGenerator()
	*/
	public void setURLGenerator(CategoryURLGenerator generator, SpiderWebPlot spiderWebPlot) {
		this.urlGenerator = generator;
		spiderWebPlot.fireChangeEvent();
	}

	public Object clone() throws CloneNotSupportedException {
		return (SpiderWebPlotProduct2) super.clone();
	}
}