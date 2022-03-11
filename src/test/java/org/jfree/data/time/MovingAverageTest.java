/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2022, by David Gilbert and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ----------------------
 * MovingAverageTest.java
 * ----------------------
 * (C) Copyright 2003-2022, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.time;

import org.jfree.chart.date.MonthConstants;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link MovingAverage} class.
 */
public class MovingAverageTest {

    private static final double EPSILON = 0.0000000001;

    /**
     * A test for the values calculated from a time series.
     */
    @Test
    public void test1() {
        TimeSeries<String> source = createDailyTimeSeries1();
        TimeSeries<String> maverage = MovingAverage.createMovingAverage(source, 
                "Moving Average", 3, 3);

        // the moving average series has 7 items, the first three
        // days (11, 12, 13 August are skipped)
        assertEquals(7, maverage.getItemCount());
        double value = maverage.getValue(0).doubleValue();
        assertEquals(14.1, value, EPSILON);
        value = maverage.getValue(1).doubleValue();
        assertEquals(13.4, value, EPSILON);
        value = maverage.getValue(2).doubleValue();
        assertEquals(14.433333333333, value, EPSILON);
        value = maverage.getValue(3).doubleValue();
        assertEquals(14.933333333333, value, EPSILON);
        value = maverage.getValue(4).doubleValue();
        assertEquals(19.8, value, EPSILON);
        value = maverage.getValue(5).doubleValue();
        assertEquals(15.25, value, EPSILON);
        value = maverage.getValue(6).doubleValue();
        assertEquals(12.5, value, EPSILON);
    }

    /**
     * Creates a sample series.
     *
     * @return A sample series.
     */
    private TimeSeries<String> createDailyTimeSeries1() {
        TimeSeries<String> series = new TimeSeries<>("Series 1");
        series.add(new Day(11, MonthConstants.AUGUST, 2003), 11.2);
        series.add(new Day(13, MonthConstants.AUGUST, 2003), 13.8);
        series.add(new Day(17, MonthConstants.AUGUST, 2003), 14.1);
        series.add(new Day(18, MonthConstants.AUGUST, 2003), 12.7);
        series.add(new Day(19, MonthConstants.AUGUST, 2003), 16.5);
        series.add(new Day(20, MonthConstants.AUGUST, 2003), 15.6);
        series.add(new Day(25, MonthConstants.AUGUST, 2003), 19.8);
        series.add(new Day(27, MonthConstants.AUGUST, 2003), 10.7);
        series.add(new Day(28, MonthConstants.AUGUST, 2003), 14.3);
        return series;
    }

}
