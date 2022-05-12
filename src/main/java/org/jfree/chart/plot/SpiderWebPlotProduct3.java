package org.jfree.chart.plot;


import java.awt.Font;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.internal.Args;
import java.io.Serializable;

public class SpiderWebPlotProduct3 implements Serializable, Cloneable {
	private Font labelFont;
	private CategoryItemLabelGenerator labelGenerator;

	public Font getLabelFont() {
		return labelFont;
	}

	public void setLabelFont2(Font labelFont) {
		this.labelFont = labelFont;
	}

	public CategoryItemLabelGenerator getLabelGenerator() {
		return labelGenerator;
	}

	public void setLabelGenerator2(CategoryItemLabelGenerator labelGenerator) {
		this.labelGenerator = labelGenerator;
	}

	/**
	* Sets the series label font and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param font   the font ( {@code  null}  not permitted).
	* @see #getLabelFont()
	*/
	public void setLabelFont(Font font, SpiderWebPlot spiderWebPlot) {
		Args.nullNotPermitted(font, "font");
		this.labelFont = font;
		spiderWebPlot.fireChangeEvent();
	}

	/**
	* Sets the label generator and sends a  {@link PlotChangeEvent}  to all registered listeners.
	* @param generator   the generator ( {@code  null}  not permitted).
	* @see #getLabelGenerator()
	*/
	public void setLabelGenerator(CategoryItemLabelGenerator generator) {
		Args.nullNotPermitted(generator, "generator");
		this.labelGenerator = generator;
	}

	public Object clone() throws CloneNotSupportedException {
		return (SpiderWebPlotProduct3) super.clone();
	}
}