package org.jfree.chart.plot;


import org.jfree.chart.text.TextAnchor;
import java.awt.Point;
import org.jfree.chart.axis.ValueAxis;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.axis.AxisState;
import java.awt.Graphics2D;
import java.io.Serializable;

public class PolarPlotProduct implements Serializable, Cloneable {
	private double angleOffset;
	private boolean counterClockwise;
	private int margin;

	public double getAngleOffset() {
		return angleOffset;
	}

	public void setAngleOffset2(double angleOffset) {
		this.angleOffset = angleOffset;
	}

	public boolean getCounterClockwise() {
		return counterClockwise;
	}

	public void setCounterClockwise(boolean counterClockwise) {
		this.counterClockwise = counterClockwise;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin2(int margin) {
		this.margin = margin;
	}

	/**
	* Calculate the text position for the given degrees.
	* @param angleDegrees   the angle in degrees.
	* @return  The optimal text anchor.
	*/
	public TextAnchor calculateTextAnchor(double angleDegrees) {
		TextAnchor ta = TextAnchor.CENTER;
		double offset = this.angleOffset;
		while (offset < 0.0) {
			offset += 360.0;
		}
		double normalizedAngle = (((this.counterClockwise ? -1 : 1) * angleDegrees) + offset) % 360;
		while (this.counterClockwise && (normalizedAngle < 0.0)) {
			normalizedAngle += 360.0;
		}
		if (normalizedAngle == 0.0) {
			ta = TextAnchor.CENTER_LEFT;
		} else if (normalizedAngle > 0.0 && normalizedAngle < 90.0) {
			ta = TextAnchor.TOP_LEFT;
		} else if (normalizedAngle == 90.0) {
			ta = TextAnchor.TOP_CENTER;
		} else if (normalizedAngle > 90.0 && normalizedAngle < 180.0) {
			ta = TextAnchor.TOP_RIGHT;
		} else if (normalizedAngle == 180) {
			ta = TextAnchor.CENTER_RIGHT;
		} else if (normalizedAngle > 180.0 && normalizedAngle < 270.0) {
			ta = TextAnchor.BOTTOM_RIGHT;
		} else if (normalizedAngle == 270) {
			ta = TextAnchor.BOTTOM_CENTER;
		} else if (normalizedAngle > 270.0 && normalizedAngle < 360.0) {
			ta = TextAnchor.BOTTOM_LEFT;
		}
		return ta;
	}

	/**
	* Sets the offset that is used for all angles and sends a {@link PlotChangeEvent}  to all registered listeners. This is useful to let 0 degrees be at the north, east, south or west side of the chart.
	* @param offset  The offset
	*/
	public void setAngleOffset(double offset, PolarPlot polarPlot) {
		this.angleOffset = offset;
		polarPlot.fireChangeEvent();
	}

	/**
	* Translates a (theta, radius) pair into Java2D coordinates.  If {@code  radius}  is less than the lower bound of the axis, then this method returns the centre point.
	* @param angleDegrees   the angle in degrees.
	* @param radius   the radius.
	* @param axis   the axis.
	* @param dataArea   the data area.
	* @return  A point in Java2D space.
	*/
	public Point translateToJava2D(double angleDegrees, double radius, ValueAxis axis, Rectangle2D dataArea) {
		if (counterClockwise) {
			angleDegrees = -angleDegrees;
		}
		double radians = Math.toRadians(angleDegrees + this.angleOffset);
		double minx = dataArea.getMinX() + this.margin;
		double maxx = dataArea.getMaxX() - this.margin;
		double miny = dataArea.getMinY() + this.margin;
		double maxy = dataArea.getMaxY() - this.margin;
		double halfWidth = (maxx - minx) / 2.0;
		double halfHeight = (maxy - miny) / 2.0;
		double midX = minx + halfWidth;
		double midY = miny + halfHeight;
		double l = Math.min(halfWidth, halfHeight);
		Rectangle2D quadrant = new Rectangle2D.Double(midX, midY, l, l);
		double axisMin = axis.getLowerBound();
		double adjustedRadius = Math.max(radius, axisMin);
		double length = axis.valueToJava2D(adjustedRadius, quadrant, RectangleEdge.BOTTOM) - midX;
		float x = (float) (midX + Math.cos(radians) * length);
		float y = (float) (midY + Math.sin(radians) * length);
		int ix = Math.round(x);
		int iy = Math.round(y);
		Point p = new Point(ix, iy);
		return p;
	}

	/**
	* Set the margin around the plot area and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param margin  The new margin in pixels.
	*/
	public void setMargin(int margin, PolarPlot polarPlot) {
		this.margin = margin;
		polarPlot.fireChangeEvent();
	}

	/**
	* Draws the axis with the specified index.
	* @param axis   the axis.
	* @param location   the axis location.
	* @param g2   the graphics target.
	* @param plotArea   the plot area.
	* @return  The axis state.
	*/
	public AxisState drawAxis(ValueAxis axis, PolarAxisLocation location, Graphics2D g2, Rectangle2D plotArea) {
		double centerX = plotArea.getCenterX();
		double centerY = plotArea.getCenterY();
		double r = Math.min(plotArea.getWidth() / 2.0, plotArea.getHeight() / 2.0) - this.margin;
		double x = centerX - r;
		double y = centerY - r;
		Rectangle2D dataArea = null;
		AxisState result = null;
		if (location == PolarAxisLocation.NORTH_RIGHT) {
			dataArea = new Rectangle2D.Double(x, y, r, r);
			result = axis.draw(g2, centerX, plotArea, dataArea, RectangleEdge.RIGHT, null);
		} else if (location == PolarAxisLocation.NORTH_LEFT) {
			dataArea = new Rectangle2D.Double(centerX, y, r, r);
			result = axis.draw(g2, centerX, plotArea, dataArea, RectangleEdge.LEFT, null);
		} else if (location == PolarAxisLocation.SOUTH_LEFT) {
			dataArea = new Rectangle2D.Double(centerX, centerY, r, r);
			result = axis.draw(g2, centerX, plotArea, dataArea, RectangleEdge.LEFT, null);
		} else if (location == PolarAxisLocation.SOUTH_RIGHT) {
			dataArea = new Rectangle2D.Double(x, centerY, r, r);
			result = axis.draw(g2, centerX, plotArea, dataArea, RectangleEdge.RIGHT, null);
		} else if (location == PolarAxisLocation.EAST_ABOVE) {
			dataArea = new Rectangle2D.Double(centerX, centerY, r, r);
			result = axis.draw(g2, centerY, plotArea, dataArea, RectangleEdge.TOP, null);
		} else if (location == PolarAxisLocation.EAST_BELOW) {
			dataArea = new Rectangle2D.Double(centerX, y, r, r);
			result = axis.draw(g2, centerY, plotArea, dataArea, RectangleEdge.BOTTOM, null);
		} else if (location == PolarAxisLocation.WEST_ABOVE) {
			dataArea = new Rectangle2D.Double(x, centerY, r, r);
			result = axis.draw(g2, centerY, plotArea, dataArea, RectangleEdge.TOP, null);
		} else if (location == PolarAxisLocation.WEST_BELOW) {
			dataArea = new Rectangle2D.Double(x, y, r, r);
			result = axis.draw(g2, centerY, plotArea, dataArea, RectangleEdge.BOTTOM, null);
		}
		return result;
	}

	public Object clone() throws CloneNotSupportedException {
		return (PolarPlotProduct) super.clone();
	}
}