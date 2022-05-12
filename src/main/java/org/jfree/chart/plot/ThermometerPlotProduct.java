package org.jfree.chart.plot;


import java.io.Serializable;

public class ThermometerPlotProduct implements Serializable, Cloneable {
	private double[][] subrangeInfo = { { 0.0, 50.0, 0.0, 50.0 }, { 50.0, 75.0, 50.0, 75.0 },
			{ 75.0, 100.0, 75.0, 100.0 } };

	public double[][] getSubrangeInfo() {
		return subrangeInfo;
	}

	/**
	* Sets the subrangeInfo attribute of the ThermometerPlot object
	* @param range   the new rangeInfo value.
	* @param rangeLow   the new rangeInfo value
	* @param rangeHigh   the new rangeInfo value
	* @param displayLow   the new rangeInfo value
	* @param displayHigh   the new rangeInfo value
	*/
	public void setSubrangeInfo(int range, double rangeLow, double rangeHigh, double displayLow, double displayHigh,
			ThermometerPlot thermometerPlot) {
		if ((range >= 0) && (range < 3)) {
			setSubrange(range, rangeLow, rangeHigh);
			setDisplayRange(range, displayLow, displayHigh);
			thermometerPlot.setAxisRange();
			thermometerPlot.fireChangeEvent();
		}
	}

	/**
	* Sets the bounds for a subrange.
	* @param range   the range type.
	* @param low   the low value.
	* @param high   the high value.
	*/
	public void setSubrange(int range, double low, double high) {
		if ((range >= 0) && (range < 3)) {
			this.subrangeInfo[range][ThermometerPlot.RANGE_HIGH] = high;
			this.subrangeInfo[range][ThermometerPlot.RANGE_LOW] = low;
		}
	}

	/**
	* Sets the displayed bounds for a sub range.
	* @param range   the range type.
	* @param low   the low value.
	* @param high   the high value.
	*/
	public void setDisplayRange(int range, double low, double high) {
		if ((range >= 0) && (range < this.subrangeInfo.length) && ThermometerPlot.isValidNumber(high)
				&& ThermometerPlot.isValidNumber(low)) {
			if (high > low) {
				this.subrangeInfo[range][ThermometerPlot.DISPLAY_HIGH] = high;
				this.subrangeInfo[range][ThermometerPlot.DISPLAY_LOW] = low;
			} else {
				this.subrangeInfo[range][ThermometerPlot.DISPLAY_HIGH] = low;
				this.subrangeInfo[range][ThermometerPlot.DISPLAY_LOW] = high;
			}
		}
	}

	/**
	* Returns true if the value is in the specified range, and false otherwise.
	* @param subrange   the subrange.
	* @param value   the value to check.
	* @return  A boolean.
	*/
	public boolean inSubrange(int subrange, double value) {
		return (value > this.subrangeInfo[subrange][ThermometerPlot.RANGE_LOW]
				&& value <= this.subrangeInfo[subrange][ThermometerPlot.RANGE_HIGH]);
	}

	public Object clone() throws CloneNotSupportedException {
		return (ThermometerPlotProduct) super.clone();
	}
}