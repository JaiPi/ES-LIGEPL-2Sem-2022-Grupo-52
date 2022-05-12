package org.jfree.chart.plot;


import java.awt.Paint;
import java.awt.Stroke;
import org.jfree.chart.internal.Args;
import java.io.Serializable;

public class FastScatterPlotProduct implements Serializable, Cloneable {
	private transient Paint paint;
	private boolean domainGridlinesVisible;
	private transient Stroke domainGridlineStroke;
	private transient Paint domainGridlinePaint;
	private boolean rangeGridlinesVisible;
	private transient Stroke rangeGridlineStroke;
	private transient Paint rangeGridlinePaint;

	public Paint getPaint() {
		return paint;
	}

	public void setPaint2(Paint paint) {
		this.paint = paint;
	}

	public boolean getDomainGridlinesVisible() {
		return domainGridlinesVisible;
	}

	public void setDomainGridlinesVisible2(boolean domainGridlinesVisible) {
		this.domainGridlinesVisible = domainGridlinesVisible;
	}

	public Stroke getDomainGridlineStroke() {
		return domainGridlineStroke;
	}

	public void setDomainGridlineStroke2(Stroke domainGridlineStroke) {
		this.domainGridlineStroke = domainGridlineStroke;
	}

	public Paint getDomainGridlinePaint() {
		return domainGridlinePaint;
	}

	public void setDomainGridlinePaint2(Paint domainGridlinePaint) {
		this.domainGridlinePaint = domainGridlinePaint;
	}

	public boolean getRangeGridlinesVisible() {
		return rangeGridlinesVisible;
	}

	public void setRangeGridlinesVisible2(boolean rangeGridlinesVisible) {
		this.rangeGridlinesVisible = rangeGridlinesVisible;
	}

	public Stroke getRangeGridlineStroke() {
		return rangeGridlineStroke;
	}

	public void setRangeGridlineStroke2(Stroke rangeGridlineStroke) {
		this.rangeGridlineStroke = rangeGridlineStroke;
	}

	public Paint getRangeGridlinePaint() {
		return rangeGridlinePaint;
	}

	public void setRangeGridlinePaint2(Paint rangeGridlinePaint) {
		this.rangeGridlinePaint = rangeGridlinePaint;
	}

	/**
	* Sets the color for the data points and sends a  {@link PlotChangeEvent} to all registered listeners.
	* @param paint   the paint ( {@code  null}  not permitted).
	* @see #getPaint()
	*/
	public void setPaint(Paint paint, FastScatterPlot fastScatterPlot) {
		Args.nullNotPermitted(paint, "paint");
		this.paint = paint;
		fastScatterPlot.fireChangeEvent();
	}

	/**
	* Sets the stroke for the grid lines plotted against the domain axis and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param stroke   the stroke ( {@code  null}  not permitted).
	* @see #getDomainGridlineStroke()
	*/
	public void setDomainGridlineStroke(Stroke stroke, FastScatterPlot fastScatterPlot) {
		Args.nullNotPermitted(stroke, "stroke");
		this.domainGridlineStroke = stroke;
		fastScatterPlot.fireChangeEvent();
	}

	/**
	* Sets the paint for the grid lines plotted against the domain axis and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param paint   the paint ( {@code  null}  not permitted).
	* @see #getDomainGridlinePaint()
	*/
	public void setDomainGridlinePaint(Paint paint, FastScatterPlot fastScatterPlot) {
		Args.nullNotPermitted(paint, "paint");
		this.domainGridlinePaint = paint;
		fastScatterPlot.fireChangeEvent();
	}

	/**
	* Sets the stroke for the grid lines plotted against the range axis and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param stroke   the stroke ( {@code  null}  permitted).
	* @see #getRangeGridlineStroke()
	*/
	public void setRangeGridlineStroke(Stroke stroke, FastScatterPlot fastScatterPlot) {
		Args.nullNotPermitted(stroke, "stroke");
		this.rangeGridlineStroke = stroke;
		fastScatterPlot.fireChangeEvent();
	}

	/**
	* Sets the paint for the grid lines plotted against the range axis and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param paint   the paint ( {@code  null}  not permitted).
	* @see #getRangeGridlinePaint()
	*/
	public void setRangeGridlinePaint(Paint paint, FastScatterPlot fastScatterPlot) {
		Args.nullNotPermitted(paint, "paint");
		this.rangeGridlinePaint = paint;
		fastScatterPlot.fireChangeEvent();
	}

	/**
	* Sets the flag that controls whether or not the domain grid-lines are visible.  If the flag value is changed, a  {@link PlotChangeEvent}  is sent to all registered listeners.
	* @param visible   the new value of the flag.
	* @see #getDomainGridlinePaint()
	*/
	public void setDomainGridlinesVisible(boolean visible, FastScatterPlot fastScatterPlot) {
		if (this.domainGridlinesVisible != visible) {
			this.domainGridlinesVisible = visible;
			fastScatterPlot.fireChangeEvent();
		}
	}

	/**
	* Sets the flag that controls whether or not the range axis grid lines are visible.  If the flag value is changed, a  {@link PlotChangeEvent}  is sent to all registered listeners.
	* @param visible   the new value of the flag.
	* @see #isRangeGridlinesVisible()
	*/
	public void setRangeGridlinesVisible(boolean visible, FastScatterPlot fastScatterPlot) {
		if (this.rangeGridlinesVisible != visible) {
			this.rangeGridlinesVisible = visible;
			fastScatterPlot.fireChangeEvent();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (FastScatterPlotProduct) super.clone();
	}
}