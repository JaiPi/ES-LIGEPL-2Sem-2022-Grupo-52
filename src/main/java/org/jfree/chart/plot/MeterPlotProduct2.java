package org.jfree.chart.plot;


import org.jfree.data.Range;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Paint;
import org.jfree.chart.internal.Args;
import java.awt.geom.Arc2D;
import java.io.Serializable;

public class MeterPlotProduct2 implements Serializable, Cloneable {
	private DialShape shape;
	private int meterAngle;
	private Range range;
	private boolean drawBorder;

	public DialShape getShape() {
		return shape;
	}

	public void setShape(DialShape shape) {
		this.shape = shape;
	}

	public int getMeterAngle() {
		return meterAngle;
	}

	public void setMeterAngle2(int meterAngle) {
		this.meterAngle = meterAngle;
	}

	public Range getRange() {
		return range;
	}

	public void setRange2(Range range) {
		this.range = range;
	}

	public boolean getDrawBorder() {
		return drawBorder;
	}

	/**
	* Fills an arc on the dial between the given values.
	* @param g2   the graphics device.
	* @param area   the plot area.
	* @param minValue   the minimum data value.
	* @param maxValue   the maximum data value.
	* @param paint   the background paint ( {@code  null}  not permitted).
	* @param dial   a flag that indicates whether the arc represents the whole dial.
	*/
	public void fillArc(Graphics2D g2, Rectangle2D area, double minValue, double maxValue, Paint paint, boolean dial) {
		Args.nullNotPermitted(paint, "paint");
		double startAngle = valueToAngle(maxValue);
		double endAngle = valueToAngle(minValue);
		double extent = endAngle - startAngle;
		double x = area.getX();
		double y = area.getY();
		double w = area.getWidth();
		double h = area.getHeight();
		int joinType = Arc2D.OPEN;
		if (this.shape == DialShape.PIE) {
			joinType = Arc2D.PIE;
		} else if (this.shape == DialShape.CHORD) {
			if (dial && this.meterAngle > 180) {
				joinType = Arc2D.CHORD;
			} else {
				joinType = Arc2D.PIE;
			}
		} else if (this.shape == DialShape.CIRCLE) {
			joinType = Arc2D.PIE;
			if (dial) {
				extent = 360;
			}
		} else {
			throw new IllegalStateException("DialShape not recognised.");
		}
		g2.setPaint(paint);
		Arc2D.Double arc = new Arc2D.Double(x, y, w, h, startAngle, extent, joinType);
		g2.fill(arc);
	}

	/**
	* Sets the dial shape and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param shape   the shape ( {@code  null}  not permitted).
	* @see #getDialShape()
	*/
	public void setDialShape(DialShape shape, MeterPlot meterPlot) {
		Args.nullNotPermitted(shape, "shape");
		this.shape = shape;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the angle (in degrees) for the whole range of the dial and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param angle   the angle (in degrees, in the range 1-360).
	* @see #getMeterAngle()
	*/
	public void setMeterAngle(int angle, MeterPlot meterPlot) {
		if (angle < 1 || angle > 360) {
			throw new IllegalArgumentException("Invalid 'angle' (" + angle + ")");
		}
		this.meterAngle = angle;
		meterPlot.fireChangeEvent();
	}

	/**
	* Sets the range for the dial and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param range   the range ( {@code  null}  not permitted and zero-length ranges not permitted).
	* @see #getRange()
	*/
	public void setRange(Range range, MeterPlot meterPlot) {
		Args.nullNotPermitted(range, "range");
		if (!(range.getLength() > 0.0)) {
			throw new IllegalArgumentException("Range length must be positive.");
		}
		this.range = range;
		meterPlot.fireChangeEvent();
	}

	/**
	* Translates a data value to an angle on the dial.
	* @param value   the value.
	* @return  The angle on the dial.
	*/
	public double valueToAngle(double value) {
		value = value - this.range.getLowerBound();
		double baseAngle = 180 + ((this.meterAngle - 180) / 2.0);
		return baseAngle - ((value / this.range.getLength()) * this.meterAngle);
	}

	/**
	* Sets the flag that controls whether or not a rectangular border is drawn around the plot area and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param draw   the flag.
	* @see #getDrawBorder()
	*/
	public void setDrawBorder(boolean draw, MeterPlot meterPlot) {
		this.drawBorder = draw;
		meterPlot.fireChangeEvent();
	}

	public Object clone() throws CloneNotSupportedException {
		return (MeterPlotProduct2) super.clone();
	}
}