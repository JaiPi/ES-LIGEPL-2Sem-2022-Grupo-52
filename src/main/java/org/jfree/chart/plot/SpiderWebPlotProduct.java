package org.jfree.chart.plot;


import org.jfree.chart.internal.Args;
import java.io.Serializable;

public class SpiderWebPlotProduct implements Serializable, Cloneable {
	private double headPercent;
	private double interiorGap;

	public double getHeadPercent() {
		return headPercent;
	}

	public void setHeadPercent2(double headPercent) {
		this.headPercent = headPercent;
	}

	public double getInteriorGap() {
		return interiorGap;
	}

	public void setInteriorGap2(double interiorGap) {
		this.interiorGap = interiorGap;
	}

	/**
	* Sets the head percent and sends a  {@link PlotChangeEvent}  to all registered listeners.  Note that 0.10 is 10 percent.
	* @param percent   the percent (must be greater than zero).
	* @see #getHeadPercent()
	*/
	public void setHeadPercent(double percent, SpiderWebPlot spiderWebPlot) {
		Args.requireNonNegative(percent, "percent");
		this.headPercent = percent;
		spiderWebPlot.fireChangeEvent();
	}

	/**
	* Sets the interior gap and sends a  {@link PlotChangeEvent}  to all registered listeners. This controls the space between the edges of the plot and the plot area itself (the region where the axis labels appear).
	* @param percent   the gap (as a percentage of the available drawing space).
	* @see #getInteriorGap()
	*/
	public void setInteriorGap(double percent, SpiderWebPlot spiderWebPlot) {
		if ((percent < 0.0) || (percent > SpiderWebPlot.MAX_INTERIOR_GAP)) {
			throw new IllegalArgumentException("Percentage outside valid range.");
		}
		if (this.interiorGap != percent) {
			this.interiorGap = percent;
			spiderWebPlot.fireChangeEvent();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (SpiderWebPlotProduct) super.clone();
	}
}