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
 * XYBarRendererTest.java
 * ----------------------
 * (C) Copyright 2003-2022, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer.xy;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.legend.LegendItem;
import org.jfree.chart.TestUtils;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XYBarRenderer} class.
 */
public class XYBarRendererTest {

    /**
     * Test that the equals() method distinguishes all fields.
     */
    @Test
    public void testEquals() {

        // default instances
        XYBarRenderer r1 = new XYBarRenderer();
        XYBarRenderer r2 = new XYBarRenderer();
        assertEquals(r1, r2);
        assertEquals(r2, r1);

        // setBase()
        r1.setBase(1.0);
        assertNotEquals(r1, r2);
        r2.setBase(1.0);
        assertEquals(r1, r2);

        // setUseYInterval
        r1.setUseYInterval(!r1.getUseYInterval());
        assertNotEquals(r1, r2);
        r2.setUseYInterval(!r2.getUseYInterval());
        assertEquals(r1, r2);

        // setMargin()
        r1.setMargin(0.10);
        assertNotEquals(r1, r2);
        r2.setMargin(0.10);
        assertEquals(r1, r2);

        // setDrawBarOutline()
        r1.setDrawBarOutline(!r1.isDrawBarOutline());
        assertNotEquals(r1, r2);
        r2.setDrawBarOutline(!r2.isDrawBarOutline());
        assertEquals(r1, r2);

        // setGradientPaintTransformer()
        r1.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_HORIZONTAL));
        assertNotEquals(r1, r2);
        r2.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_HORIZONTAL));
        assertEquals(r1, r2);

        // legendBar
        r1.setLegendBar(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertNotEquals(r1, r2);
        r2.setLegendBar(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertEquals(r1, r2);

        // positiveItemLabelFallbackPosition
        r1.setPositiveItemLabelPositionFallback(new ItemLabelPosition());
        assertNotEquals(r1, r2);
        r2.setPositiveItemLabelPositionFallback(new ItemLabelPosition());
        assertEquals(r1, r2);

        // negativeItemLabelFallbackPosition
        r1.setNegativeItemLabelPositionFallback(new ItemLabelPosition());
        assertNotEquals(r1, r2);
        r2.setNegativeItemLabelPositionFallback(new ItemLabelPosition());
        assertEquals(r1, r2);

        // barPainter
        r1.setBarPainter(new GradientXYBarPainter(0.11, 0.22, 0.33));
        assertNotEquals(r1, r2);
        r2.setBarPainter(new GradientXYBarPainter(0.11, 0.22, 0.33));
        assertEquals(r1, r2);

        // shadowsVisible
        r1.setShadowVisible(false);
        assertNotEquals(r1, r2);
        r2.setShadowVisible(false);
        assertEquals(r1, r2);

        // shadowXOffset
        r1.setShadowXOffset(3.3);
        assertNotEquals(r1, r2);
        r2.setShadowXOffset(3.3);
        assertEquals(r1, r2);

        // shadowYOffset
        r1.setShadowYOffset(3.3);
        assertNotEquals(r1, r2);
        r2.setShadowYOffset(3.3);
        assertEquals(r1, r2);

    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashcode() {
        XYBarRenderer r1 = new XYBarRenderer();
        XYBarRenderer r2 = new XYBarRenderer();
        assertEquals(r1, r2);
        int h1 = r1.hashCode();
        int h2 = r2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Confirm that cloning works.
     * 
     * @throws java.lang.CloneNotSupportedException
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        XYBarRenderer r1 = new XYBarRenderer();
        Rectangle2D rect = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        r1.setLegendBar(rect);
        XYBarRenderer r2 = CloneUtils.clone(r1);
        assertNotSame(r1, r2);
        assertSame(r1.getClass(), r2.getClass());
        assertEquals(r1, r2);

        // check independence
        rect.setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(r1, r2);
        r2.setLegendBar(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertEquals(r1, r2);
    }

    /**
     * Verify that this class implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        XYBarRenderer r1 = new XYBarRenderer();
        assertTrue(r1 instanceof PublicCloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        XYBarRenderer r1 = new XYBarRenderer();
        XYBarRenderer r2 = TestUtils.serialised(r1);
        assertEquals(r1, r2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization2() {
        XYBarRenderer r1 = new XYBarRenderer();
        r1.setPositiveItemLabelPositionFallback(new ItemLabelPosition());
        XYBarRenderer r2 = TestUtils.serialised(r1);
        assertEquals(r1, r2);
    }

    /**
     * Check that the renderer is calculating the domain bounds correctly.
     */
    @Test
    public void testFindDomainBounds() {
        XYSeriesCollection<String> dataset
                = RendererXYPackageUtils.createTestXYSeriesCollection();
        JFreeChart chart = ChartFactory.createXYBarChart("Test Chart", "X",
                false, "Y", dataset, PlotOrientation.VERTICAL, false, false,
                false);
        XYPlot<String> plot = (XYPlot) chart.getPlot();
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);
        Range bounds = domainAxis.getRange();
        assertFalse(bounds.contains(0.3));
        assertTrue(bounds.contains(0.5));
        assertTrue(bounds.contains(2.5));
        assertFalse(bounds.contains(2.8));
    }

    /**
     * A test for the findDomainBounds method to ensure it correctly accounts 
     * for the series visibility.
     */
    @Test
    public void testFindDomainBounds2() {
        XYIntervalSeries<String> s1 = new XYIntervalSeries<>("S1");
        s1.add(1.0, 0.5, 1.5, 10.0, 9.5, 10.5);
        s1.add(2.0, 1.9, 2.1, 20.0, 19.8, 20.3);
        XYIntervalSeries<String> s2 = new XYIntervalSeries<>("S2");
        s2.add(3.0, 2.5, 3.5, 30.0, 29.5, 30.5);
        s2.add(4.0, 3.9, 4.1, 9.0, 9.0, 9.0);
        XYIntervalSeriesCollection<String> dataset = new XYIntervalSeriesCollection<>();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        
        XYBarRenderer renderer = new XYBarRenderer();
        Range r = renderer.findDomainBounds(dataset);
        assertEquals(0.5, r.getLowerBound(), EPSILON);
        assertEquals(4.1, r.getUpperBound(), EPSILON);
        
        renderer.setSeriesVisible(1, Boolean.FALSE);
        r = renderer.findDomainBounds(dataset);
        assertEquals(0.5, r.getLowerBound(), EPSILON);
        assertEquals(2.1, r.getUpperBound(), EPSILON);
    }

    private static final double EPSILON = 0.0000000001;

    /**
     * A simple test for the findRangeBounds() method.
     */
    @Test
    public void testFindRangeBounds() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] startx = {0.9, 1.8, 2.7, 3.6};
        double[] endx = {1.1, 2.2, 3.3, 4.4};
        double[] y = {1.0, 2.0, 3.0, 4.0};
        double[] starty = {0.9, 1.8, 2.7, 3.6};
        double[] endy = {1.1, 2.2, 3.3, 4.4};
        double[][] data = new double[][] {x, startx, endx, y, starty, endy};
        dataset.addSeries("Series 1", data);
        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setUseYInterval(true);
        Range r = renderer.findRangeBounds(dataset);
        assertEquals(0.9, r.getLowerBound(), EPSILON);
        assertEquals(4.4, r.getUpperBound(), EPSILON);

        renderer.setUseYInterval(false);
        r = renderer.findRangeBounds(dataset);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(4.0, r.getUpperBound(), EPSILON);
    }

    /**
     * A test for the findRangeBounds method to ensure it correctly accounts 
     * for the series visibility.
     */
    @Test
    public void testFindRangeBounds2() {
        XYIntervalSeries<String> s1 = new XYIntervalSeries<>("S1");
        s1.add(1.0, 0.5, 1.5, 10.0, 9.5, 10.5);
        s1.add(2.0, 1.9, 2.1, 20.0, 19.8, 20.3);
        XYIntervalSeries<String> s2 = new XYIntervalSeries<>("S2");
        s2.add(3.0, 2.5, 3.5, 30.0, 29.5, 30.5);
        s2.add(4.0, 3.9, 4.1, 9.0, 9.0, 9.0);
        XYIntervalSeriesCollection<String> dataset = new XYIntervalSeriesCollection<>();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        
        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setUseYInterval(false);
        Range r = renderer.findRangeBounds(dataset);
        assertEquals(9.0, r.getLowerBound(), EPSILON);
        assertEquals(30.0, r.getUpperBound(), EPSILON);
        
        renderer.setSeriesVisible(1, Boolean.FALSE);
        r = renderer.findRangeBounds(dataset);
        assertEquals(10.0, r.getLowerBound(), EPSILON);
        assertEquals(20.0, r.getUpperBound(), EPSILON);
    }

    /**
     * A check for the datasetIndex and seriesIndex fields in the LegendItem
     * returned by the getLegendItem() method.
     */
    @Test
    public void testGetLegendItemSeriesIndex() {
        XYSeriesCollection<String> d1 = new XYSeriesCollection<>();
        XYSeries<String> s1 = new XYSeries<>("S1");
        s1.add(1.0, 1.1);
        XYSeries<String> s2 = new XYSeries<>("S2");
        s2.add(1.0, 1.1);
        d1.addSeries(s1);
        d1.addSeries(s2);

        XYSeriesCollection<String> d2 = new XYSeriesCollection<>();
        XYSeries<String> s3 = new XYSeries<>("S3");
        s3.add(1.0, 1.1);
        XYSeries<String> s4 = new XYSeries<>("S4");
        s4.add(1.0, 1.1);
        XYSeries<String> s5 = new XYSeries<>("S5");
        s5.add(1.0, 1.1);
        d2.addSeries(s3);
        d2.addSeries(s4);
        d2.addSeries(s5);

        XYBarRenderer r = new XYBarRenderer();
        XYPlot<String> plot = new XYPlot<>(new XYBarDataset<>(d1, 1.0),
                new NumberAxis("x"), new NumberAxis("y"), r);
        plot.setDataset(1, new XYBarDataset<>(d2, 2.0));
        JFreeChart chart = new JFreeChart(plot);
        LegendItem li = r.getLegendItem(1, 2);
        assertEquals("S5", li.getLabel());
        assertEquals(1, li.getDatasetIndex());
        assertEquals(2, li.getSeriesIndex());
    }


}
