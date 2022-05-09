package org.jfree.chart.plot;


import java.awt.Font;
import java.awt.Paint;
import java.awt.Color;
import java.awt.Stroke;
import org.jfree.chart.internal.Args;
import java.io.Serializable;

public class PolarPlotProduct2 implements Serializable, Cloneable {
	private boolean angleLabelsVisible = true;
	private Font angleLabelFont = new Font("SansSerif", Font.PLAIN, 12);
	private transient Paint angleLabelPaint = Color.BLACK;
	private boolean angleGridlinesVisible;
	private transient Stroke angleGridlineStroke;
	private transient Paint angleGridlinePaint;
	private boolean radiusGridlinesVisible;
	private transient Stroke radiusGridlineStroke;
	private transient Paint radiusGridlinePaint;

	public boolean getAngleLabelsVisible() {
		return angleLabelsVisible;
	}

	public Font getAngleLabelFont() {
		return angleLabelFont;
	}

	public Paint getAngleLabelPaint() {
		return angleLabelPaint;
	}

	public void setAngleLabelPaint2(Paint angleLabelPaint) {
		this.angleLabelPaint = angleLabelPaint;
	}

	public boolean getAngleGridlinesVisible() {
		return angleGridlinesVisible;
	}

	public void setAngleGridlinesVisible2(boolean angleGridlinesVisible) {
		this.angleGridlinesVisible = angleGridlinesVisible;
	}

	public Stroke getAngleGridlineStroke() {
		return angleGridlineStroke;
	}

	public void setAngleGridlineStroke2(Stroke angleGridlineStroke) {
		this.angleGridlineStroke = angleGridlineStroke;
	}

	public Paint getAngleGridlinePaint() {
		return angleGridlinePaint;
	}

	public void setAngleGridlinePaint2(Paint angleGridlinePaint) {
		this.angleGridlinePaint = angleGridlinePaint;
	}

	public boolean getRadiusGridlinesVisible() {
		return radiusGridlinesVisible;
	}

	public void setRadiusGridlinesVisible2(boolean radiusGridlinesVisible) {
		this.radiusGridlinesVisible = radiusGridlinesVisible;
	}

	public Stroke getRadiusGridlineStroke() {
		return radiusGridlineStroke;
	}

	public void setRadiusGridlineStroke2(Stroke radiusGridlineStroke) {
		this.radiusGridlineStroke = radiusGridlineStroke;
	}

	public Paint getRadiusGridlinePaint() {
		return radiusGridlinePaint;
	}

	public void setRadiusGridlinePaint2(Paint radiusGridlinePaint) {
		this.radiusGridlinePaint = radiusGridlinePaint;
	}

	/**
	* Sets the flag that controls whether or not the angle labels are visible, and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param visible   the flag.
	* @see #isAngleLabelsVisible()
	*/
	public void setAngleLabelsVisible(boolean visible, PolarPlot polarPlot) {
		if (this.angleLabelsVisible != visible) {
			this.angleLabelsVisible = visible;
			polarPlot.fireChangeEvent();
		}
	}

	/**
	* Sets the font used to display the angle labels and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param font   the font ( {@code  null}  not permitted).
	* @see #getAngleLabelFont()
	*/
	public void setAngleLabelFont(Font font, PolarPlot polarPlot) {
		Args.nullNotPermitted(font, "font");
		this.angleLabelFont = font;
		polarPlot.fireChangeEvent();
	}

	/**
	* Sets the paint used to display the angle labels and sends a {@link PlotChangeEvent}  to all registered listeners.
	* @param paint   the paint ( {@code  null}  not permitted).
	*/
	public void setAngleLabelPaint(Paint paint, PolarPlot polarPlot) {
		Args.nullNotPermitted(paint, "paint");
		this.angleLabelPaint = paint;
		polarPlot.fireChangeEvent();
	}

	/**
	* Sets the stroke for the grid lines plotted against the angular axis and sends a  {@link PlotChangeEvent}  to all registered listeners. <p> If you set this to  {@code  null} , no grid lines will be drawn.
	* @param stroke   the stroke ( {@code  null}  permitted).
	* @see #getAngleGridlineStroke()
	*/
	public void setAngleGridlineStroke(Stroke stroke, PolarPlot polarPlot) {
		this.angleGridlineStroke = stroke;
		polarPlot.fireChangeEvent();
	}

	/**
	* Sets the paint for the grid lines plotted against the angular axis. <p> If you set this to  {@code  null} , no grid lines will be drawn.
	* @param paint   the paint ( {@code  null}  permitted).
	* @see #getAngleGridlinePaint()
	*/
	public void setAngleGridlinePaint(Paint paint, PolarPlot polarPlot) {
		this.angleGridlinePaint = paint;
		polarPlot.fireChangeEvent();
	}

	/**
	* Sets the stroke for the grid lines plotted against the radius axis and sends a  {@link PlotChangeEvent}  to all registered listeners. <p> If you set this to  {@code  null} , no grid lines will be drawn.
	* @param stroke   the stroke ( {@code  null}  permitted).
	* @see #getRadiusGridlineStroke()
	*/
	public void setRadiusGridlineStroke(Stroke stroke, PolarPlot polarPlot) {
		this.radiusGridlineStroke = stroke;
		polarPlot.fireChangeEvent();
	}

	/**
	* Sets the paint for the grid lines plotted against the radius axis and sends a  {@link PlotChangeEvent}  to all registered listeners. <p> If you set this to  {@code  null} , no grid lines will be drawn.
	* @param paint   the paint ( {@code  null}  permitted).
	* @see #getRadiusGridlinePaint()
	*/
	public void setRadiusGridlinePaint(Paint paint, PolarPlot polarPlot) {
		this.radiusGridlinePaint = paint;
		polarPlot.fireChangeEvent();
	}

	/**
	* Sets the flag that controls whether or not the angular grid-lines are visible. <p> If the flag value is changed, a  {@link PlotChangeEvent}  is sent to all registered listeners.
	* @param visible   the new value of the flag.
	* @see #isAngleGridlinesVisible()
	*/
	public void setAngleGridlinesVisible(boolean visible, PolarPlot polarPlot) {
		if (this.angleGridlinesVisible != visible) {
			this.angleGridlinesVisible = visible;
			polarPlot.fireChangeEvent();
		}
	}

	/**
	* Sets the flag that controls whether or not the radius axis grid lines are visible. <p> If the flag value is changed, a  {@link PlotChangeEvent}  is sent to all registered listeners.
	* @param visible   the new value of the flag.
	* @see #isRadiusGridlinesVisible()
	*/
	public void setRadiusGridlinesVisible(boolean visible, PolarPlot polarPlot) {
		if (this.radiusGridlinesVisible != visible) {
			this.radiusGridlinesVisible = visible;
			polarPlot.fireChangeEvent();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (PolarPlotProduct2) super.clone();
	}
}