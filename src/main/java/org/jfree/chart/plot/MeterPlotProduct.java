package org.jfree.chart.plot;


import java.awt.Paint;
import java.awt.Font;
import org.jfree.chart.internal.Args;
import java.io.Serializable;

public class MeterPlotProduct implements Serializable, Cloneable {
	private transient Paint tickPaint;
	private String units;
	private Font valueFont;
	private transient Paint valuePaint;
	private boolean valueVisible = true;
	private transient Paint dialOutlinePaint;
	private transient Paint dialBackgroundPaint;
	private transient Paint needlePaint;
	private boolean tickLabelsVisible;
	private Font tickLabelFont;
	private transient Paint tickLabelPaint;

	public Paint getTickPaint() {
		return tickPaint;
	}

	public void setTickPaint2(Paint tickPaint) {
		this.tickPaint = tickPaint;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits2(String units) {
		this.units = units;
	}

	public Font getValueFont() {
		return valueFont;
	}

	public void setValueFont2(Font valueFont) {
		this.valueFont = valueFont;
	}

	public Paint getValuePaint() {
		return valuePaint;
	}

	public void setValuePaint2(Paint valuePaint) {
		this.valuePaint = valuePaint;
	}

	public boolean getValueVisible() {
		return valueVisible;
	}

	public Paint getDialOutlinePaint() {
		return dialOutlinePaint;
	}

	public void setDialOutlinePaint2(Paint dialOutlinePaint) {
		this.dialOutlinePaint = dialOutlinePaint;
	}

	public Paint getDialBackgroundPaint() {
		return dialBackgroundPaint;
	}

	public void setDialBackgroundPaint2(Paint dialBackgroundPaint) {
		this.dialBackgroundPaint = dialBackgroundPaint;
	}

	public Paint getNeedlePaint() {
		return needlePaint;
	}

	public void setNeedlePaint2(Paint needlePaint) {
		this.needlePaint = needlePaint;
	}

	public boolean getTickLabelsVisible() {
		return tickLabelsVisible;
	}

	public void setTickLabelsVisible2(boolean tickLabelsVisible) {
		this.tickLabelsVisible = tickLabelsVisible;
	}

	public Font getTickLabelFont() {
		return tickLabelFont;
	}

	public void setTickLabelFont2(Font tickLabelFont) {
		this.tickLabelFont = tickLabelFont;
	}

	public Paint getTickLabelPaint() {
		return tickLabelPaint;
	}

	public void setTickLabelPaint2(Paint tickLabelPaint) {
		this.tickLabelPaint = tickLabelPaint;
	}

	/**
	* Sets the paint used to draw the tick labels around the dial and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param paint   the paint ( {@code  null}  not permitted).
	* @see #getTickPaint()
	*/
	public void setTickPaint(Paint paint, MeterPlot meterPlot) {
		Args.nullNotPermitted(paint, "paint");
		this.tickPaint = paint;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the paint used to display the needle and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param paint   the paint ( {@code  null}  not permitted).
	* @see #getNeedlePaint()
	*/
	public void setNeedlePaint(Paint paint, MeterPlot meterPlot) {
		Args.nullNotPermitted(paint, "paint");
		this.needlePaint = paint;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the tick label paint and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param paint   the paint ( {@code  null}  not permitted).
	* @see #getTickLabelPaint()
	*/
	public void setTickLabelPaint(Paint paint, MeterPlot meterPlot) {
		Args.nullNotPermitted(paint, "paint");
		if (!this.tickLabelPaint.equals(paint)) {
			this.tickLabelPaint = paint;
			meterPlot.fireChangeEvent();
		}
	}

	/**
	* Sets the paint used to display the value label and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param paint   the paint ( {@code  null}  not permitted).
	* @see #getValuePaint()
	*/
	public void setValuePaint(Paint paint, MeterPlot meterPlot) {
		Args.nullNotPermitted(paint, "paint");
		this.valuePaint = paint;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the paint used to fill the dial background.  Set this to {@code  null}  for no background.
	* @param paint   the paint ( {@code  null}  permitted).
	* @see #getDialBackgroundPaint()
	*/
	public void setDialBackgroundPaint(Paint paint, MeterPlot meterPlot) {
		this.dialBackgroundPaint = paint;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the dial outline paint and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param paint   the paint.
	* @see #getDialOutlinePaint()
	*/
	public void setDialOutlinePaint(Paint paint, MeterPlot meterPlot) {
		this.dialOutlinePaint = paint;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the units for the dial and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param units   the units ( {@code  null}  permitted).
	* @see #getUnits()
	*/
	public void setUnits(String units, MeterPlot meterPlot) {
		this.units = units;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the font used to display the value label and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param font   the font ( {@code  null}  not permitted).
	* @see #getValueFont()
	*/
	public void setValueFont(Font font, MeterPlot meterPlot) {
		Args.nullNotPermitted(font, "font");
		this.valueFont = font;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the flag that controls whether or not the value is visible and sends a change event to all registered listeners.
	* @param valueVisible   the new flag value.
	* @see #isValueVisible()
	* @since  1.5.4
	*/
	public void setValueVisible(boolean valueVisible, MeterPlot meterPlot) {
		this.valueVisible = valueVisible;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the flag that controls whether or not the tick labels are visible and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param visible   the flag.
	* @see #getTickLabelsVisible()
	*/
	public void setTickLabelsVisible(boolean visible, MeterPlot meterPlot) {
		if (this.tickLabelsVisible != visible) {
			this.tickLabelsVisible = visible;
			meterPlot.fireChangeEvent();
		}
	}

	/**
	* Sets the tick label font and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param font   the font ( {@code  null}  not permitted).
	* @see #getTickLabelFont()
	*/
	public void setTickLabelFont(Font font, MeterPlot meterPlot) {
		Args.nullNotPermitted(font, "font");
		if (!this.tickLabelFont.equals(font)) {
			this.tickLabelFont = font;
			meterPlot.fireChangeEvent();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (MeterPlotProduct) super.clone();
	}
}