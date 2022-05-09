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
 * --------------
 * PolarPlot.java
 * --------------
 * (C) Copyright 2004-2021, by Solution Engineering, Inc. and Contributors.
 *
 * Original Author:  Daniel Bridenbecker, Solution Engineering, Inc.;
 * Contributor(s):   David Gilbert;
 *                   Martin Hoeller (patches 1871902 and 2850344);
 * 
 */

package org.jfree.chart.plot;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import org.jfree.chart.ChartElementVisitor;

import org.jfree.chart.legend.LegendItem;
import org.jfree.chart.legend.LegendItemCollection;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickType;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.renderer.PolarItemRenderer;
import org.jfree.chart.text.TextUtils;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.api.RectangleInsets;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.internal.PaintUtils;
import org.jfree.chart.internal.Args;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.chart.internal.SerialUtils;
import org.jfree.data.Range;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;

/**
 * Plots data that is in (theta, radius) pairs where theta equal to zero is 
 * due north and increases clockwise.
 */
public class PolarPlot extends Plot implements ValueAxisPlot, Zoomable,
        RendererChangeListener, Cloneable, Serializable {

    private PolarPlotProduct2 polarPlotProduct2 = new PolarPlotProduct2();

	private PolarPlotProduct polarPlotProduct = new PolarPlotProduct();

	/** For serialization. */
    private static final long serialVersionUID = 3794383185924179525L;

    /** The default margin. */
    private static final int DEFAULT_MARGIN = 20;

    /** The annotation margin. */
    private static final double ANNOTATION_MARGIN = 7.0;

    /** The default angle tick unit size. */
    public static final double DEFAULT_ANGLE_TICK_UNIT_SIZE = 45.0;

    /** The default angle offset. */
    public static final double DEFAULT_ANGLE_OFFSET = -90.0;

    /** The default grid line stroke. */
    public static final Stroke DEFAULT_GRIDLINE_STROKE = new BasicStroke(
            0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
            0.0f, new float[]{2.0f, 2.0f}, 0.0f);

    /** The default grid line paint. */
    public static final Paint DEFAULT_GRIDLINE_PAINT = Color.GRAY;

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundle.getBundle("org.jfree.chart.plot.LocalizationBundle");

    /** The angles that are marked with gridlines. */
    private List<ValueTick> angleTicks;

    /** The range axis (used for the y-values). */
    private Map<Integer, ValueAxis> axes;

    /** The axis locations. */
    private final Map<Integer, PolarAxisLocation> axisLocations;

    /** Storage for the datasets. */
    private Map<Integer, XYDataset> datasets;

    /** Storage for the renderers. */
    private Map<Integer, PolarItemRenderer> renderers;

    /**
     * The tick unit that controls the spacing between the angular grid lines.
     */
    private TickUnit angleTickUnit;

    /**
     * A flag that controls whether the radial minor grid-lines are visible.
     */
    private boolean radiusMinorGridlinesVisible;

    /** The annotations for the plot. */
    private List<String> cornerTextItems = new ArrayList<>();

    /**
     * An optional collection of legend items that can be returned by the
     * getLegendItems() method.
     */
    private LegendItemCollection fixedLegendItems;

    /**
     * Storage for the mapping between datasets/renderers and range axes.  The
     * keys in the map are Integer objects, corresponding to the dataset
     * index.  The values in the map are List<Integer> instances (corresponding 
     * to the axis indices).  If the map contains no
     * entry for a dataset, it is assumed to map to the primary domain axis
     * (index = 0).
     */
    private final Map<Integer, List<Integer>> datasetToAxesMap;

    /**
     * Default constructor.
     */
    public PolarPlot() {
        this(null, null, null);
    }

   /**
     * Creates a new plot.
     *
     * @param dataset  the dataset ({@code null} permitted).
     * @param radiusAxis  the radius axis ({@code null} permitted).
     * @param renderer  the renderer ({@code null} permitted).
     */
    public PolarPlot(XYDataset dataset, ValueAxis radiusAxis, PolarItemRenderer renderer) {
        super();
        this.datasets = new HashMap<>();
        this.datasets.put(0, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
        this.angleTickUnit = new NumberTickUnit(DEFAULT_ANGLE_TICK_UNIT_SIZE);

        this.axes = new HashMap<>();
        this.datasetToAxesMap = new TreeMap<>();
        this.axes.put(0, radiusAxis);
        if (radiusAxis != null) {
            radiusAxis.setPlot(this);
            radiusAxis.addChangeListener(this);
        }

        // define the default locations for up to 8 axes...
        this.axisLocations = new HashMap<>();
        this.axisLocations.put(0, PolarAxisLocation.EAST_ABOVE);
        this.axisLocations.put(1, PolarAxisLocation.NORTH_LEFT);
        this.axisLocations.put(2, PolarAxisLocation.WEST_BELOW);
        this.axisLocations.put(3, PolarAxisLocation.SOUTH_RIGHT);
        this.axisLocations.put(4, PolarAxisLocation.EAST_BELOW);
        this.axisLocations.put(5, PolarAxisLocation.NORTH_RIGHT);
        this.axisLocations.put(6, PolarAxisLocation.WEST_ABOVE);
        this.axisLocations.put(7, PolarAxisLocation.SOUTH_LEFT);

        this.renderers = new HashMap<>();
        this.renderers.put(0, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }

        polarPlotProduct.setAngleOffset2(DEFAULT_ANGLE_OFFSET);
        polarPlotProduct.setCounterClockwise(false);
        polarPlotProduct2.setAngleGridlinesVisible2(true);
        polarPlotProduct2.setAngleGridlineStroke2(DEFAULT_GRIDLINE_STROKE);
        polarPlotProduct2.setAngleGridlinePaint2(DEFAULT_GRIDLINE_PAINT);

        polarPlotProduct2.setRadiusGridlinesVisible2(true);
        this.radiusMinorGridlinesVisible = true;
        polarPlotProduct2.setRadiusGridlineStroke2(DEFAULT_GRIDLINE_STROKE);
        polarPlotProduct2.setRadiusGridlinePaint2(DEFAULT_GRIDLINE_PAINT);
        polarPlotProduct.setMargin2(DEFAULT_MARGIN);
    }

    /**
     * Returns the plot type as a string.
     *
     * @return A short string describing the type of plot.
     */
    @Override
    public String getPlotType() {
       return PolarPlot.localizationResources.getString("Polar_Plot");
    }

    /**
     * Returns the primary axis for the plot.
     *
     * @return The primary axis (possibly {@code null}).
     *
     * @see #setAxis(ValueAxis)
     */
    public ValueAxis getAxis() {
        return getAxis(0);
    }

    /**
     * Returns an axis for the plot.
     *
     * @param index  the axis index.
     *
     * @return The axis ({@code null} possible).
     *
     * @see #setAxis(int, ValueAxis)
     */
    public ValueAxis getAxis(int index) {
        return this.axes.get(index);
    }

    /**
     * Sets the primary axis for the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param axis  the new primary axis ({@code null} permitted).
     */
    public void setAxis(ValueAxis axis) {
        setAxis(0, axis);
    }

    /**
     * Sets an axis for the plot and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis ({@code null} permitted).
     *
     * @see #getAxis(int)
     */
    public void setAxis(int index, ValueAxis axis) {
        setAxis(index, axis, true);
    }

    /**
     * Sets an axis for the plot and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis ({@code null} permitted).
     * @param notify  notify listeners?
     *
     * @see #getAxis(int)
     */
    public void setAxis(int index, ValueAxis axis, boolean notify) {
        ValueAxis existing = getAxis(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        if (axis != null) {
            axis.setPlot(this);
        }
        this.axes.put(index, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the location of the primary axis.
     *
     * @return The location (never {@code null}).
     *
     * @see #setAxisLocation(PolarAxisLocation)
     */
    public PolarAxisLocation getAxisLocation() {
        return getAxisLocation(0);
    }

    /**
     * Returns the location for an axis.
     *
     * @param index  the axis index.
     *
     * @return The location (possibly {@code null}).
     *
     * @see #setAxisLocation(int, PolarAxisLocation)
     */
    public PolarAxisLocation getAxisLocation(int index) {
        return this.axisLocations.get(index);
    }

    /**
     * Sets the location of the primary axis and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location ({@code null} not permitted).
     *
     * @see #getAxisLocation()
     */
    public void setAxisLocation(PolarAxisLocation location) {
        // delegate argument checks...
        setAxisLocation(0, location, true);
    }

    /**
     * Sets the location of the primary axis and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location ({@code null} not permitted).
     * @param notify  notify listeners?
     *
     * @see #getAxisLocation()
     */
    public void setAxisLocation(PolarAxisLocation location, boolean notify) {
        // delegate...
        setAxisLocation(0, location, notify);
    }

    /**
     * Sets the location for an axis and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param index  the axis index.
     * @param location  the location ({@code null} not permitted).
     *
     * @see #getAxisLocation(int)
     */
    public void setAxisLocation(int index, PolarAxisLocation location) {
        // delegate...
        setAxisLocation(index, location, true);
    }

    /**
     * Sets the axis location for an axis and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index  the axis index.
     * @param location  the location ({@code null} not permitted).
     * @param notify  notify listeners?
     */
    public void setAxisLocation(int index, PolarAxisLocation location,
            boolean notify) {
        Args.nullNotPermitted(location, "location");
        this.axisLocations.put(index, location);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the number of domain axes.
     *
     * @return The axis count.
     **/
    public int getAxisCount() {
        return this.axes.size();
    }

    /**
     * Returns the primary dataset for the plot.
     *
     * @return The primary dataset (possibly {@code null}).
     *
     * @see #setDataset(XYDataset)
     */
    public XYDataset getDataset() {
        return getDataset(0);
    }

    /**
     * Returns the dataset with the specified index, if any.
     *
     * @param index  the dataset index.
     *
     * @return The dataset (possibly {@code null}).
     *
     * @see #setDataset(int, XYDataset)
     */
    public XYDataset getDataset(int index) {
        return this.datasets.get(index);
    }

    /**
     * Sets the primary dataset for the plot, replacing the existing dataset
     * if there is one, and sends a {@code link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param dataset  the dataset ({@code null} permitted).
     *
     * @see #getDataset()
     */
    public void setDataset(XYDataset dataset) {
        setDataset(0, dataset);
    }

    /**
     * Sets a dataset for the plot, replacing the existing dataset at the same
     * index if there is one, and sends a {@code link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param index  the dataset index.
     * @param dataset  the dataset ({@code null} permitted).
     *
     * @see #getDataset(int)
     */
    public void setDataset(int index, XYDataset dataset) {
        XYDataset existing = getDataset(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.datasets.put(index, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }

        // send a dataset change event to self...
        DatasetChangeEvent event = new DatasetChangeEvent(this, dataset);
        datasetChanged(event);
    }

    /**
     * Returns the number of datasets.
     *
     * @return The number of datasets.
     */
    public int getDatasetCount() {
        return this.datasets.size();
    }

    /**
     * Returns the index of the specified dataset, or {@code -1} if the
     * dataset does not belong to the plot.
     *
     * @param dataset  the dataset ({@code null} not permitted).
     *
     * @return The index.
     */
    public int indexOf(XYDataset dataset) {
        for (Entry<Integer, XYDataset> entry : this.datasets.entrySet()) {
            if (entry.getValue() == dataset) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Returns the primary renderer.
     *
     * @return The renderer (possibly {@code null}).
     *
     * @see #setRenderer(PolarItemRenderer)
     */
    public PolarItemRenderer getRenderer() {
        return getRenderer(0);
    }

    /**
     * Returns the renderer at the specified index, if there is one.
     *
     * @param index  the renderer index.
     *
     * @return The renderer (possibly {@code null}).
     *
     * @see #setRenderer(int, PolarItemRenderer)
     */
    public PolarItemRenderer getRenderer(int index) {
        return this.renderers.get(index);
    }

    /**
     * Sets the primary renderer, and notifies all listeners of a change to the
     * plot.  If the renderer is set to {@code null}, no data items will
     * be drawn for the corresponding dataset.
     *
     * @param renderer  the new renderer ({@code null} permitted).
     *
     * @see #getRenderer()
     */
    public void setRenderer(PolarItemRenderer renderer) {
        setRenderer(0, renderer);
    }

    /**
     * Sets a renderer and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param index  the index.
     * @param renderer  the renderer.
     *
     * @see #getRenderer(int)
     */
    public void setRenderer(int index, PolarItemRenderer renderer) {
        setRenderer(index, renderer, true);
    }

    /**
     * Sets a renderer and, if requested, sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param index  the index.
     * @param renderer  the renderer.
     * @param notify  notify listeners?
     *
     * @see #getRenderer(int)
     */
    public void setRenderer(int index, PolarItemRenderer renderer,
                            boolean notify) {
        PolarItemRenderer existing = getRenderer(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.renderers.put(index, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the tick unit that controls the spacing of the angular grid
     * lines.
     *
     * @return The tick unit (never {@code null}).
     */
    public TickUnit getAngleTickUnit() {
        return this.angleTickUnit;
    }

    /**
     * Sets the tick unit that controls the spacing of the angular grid
     * lines, and sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param unit  the tick unit ({@code null} not permitted).
     */
    public void setAngleTickUnit(TickUnit unit) {
        Args.nullNotPermitted(unit, "unit");
        this.angleTickUnit = unit;
        fireChangeEvent();
    }

    /**
     * Returns the offset that is used for all angles.
     *
     * @return The offset for the angles.
     */
    public double getAngleOffset() {
        return this.polarPlotProduct.getAngleOffset();
    }

    /**
     * Sets the offset that is used for all angles and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * This is useful to let 0 degrees be at the north, east, south or west
     * side of the chart.
     *
     * @param offset The offset
     */
    public void setAngleOffset(double offset) {
        polarPlotProduct.setAngleOffset(offset, this);
    }

    /**
     * Get the direction for growing angle degrees.
     *
     * @return {@code true} if angle increases counterclockwise,
     *         {@code false} otherwise.
     */
    public boolean isCounterClockwise() {
        return this.polarPlotProduct.getCounterClockwise();
    }

    /**
     * Sets the flag for increasing angle degrees direction.
     *
     * {@code true} for counterclockwise, {@code false} for
     * clockwise.
     *
     * @param counterClockwise The flag.
     */
    public void setCounterClockwise(boolean counterClockwise)
    {
        polarPlotProduct.setCounterClockwise(counterClockwise);
    }

    /**
     * Returns a flag that controls whether or not the angle labels are visible.
     *
     * @return A boolean.
     *
     * @see #setAngleLabelsVisible(boolean)
     */
    public boolean isAngleLabelsVisible() {
        return this.polarPlotProduct2.getAngleLabelsVisible();
    }

    /**
     * Sets the flag that controls whether or not the angle labels are visible,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param visible  the flag.
     *
     * @see #isAngleLabelsVisible()
     */
    public void setAngleLabelsVisible(boolean visible) {
        polarPlotProduct2.setAngleLabelsVisible(visible, this);
    }

    /**
     * Returns the font used to display the angle labels.
     *
     * @return A font (never {@code null}).
     *
     * @see #setAngleLabelFont(Font)
     */
    public Font getAngleLabelFont() {
        return this.polarPlotProduct2.getAngleLabelFont();
    }

    /**
     * Sets the font used to display the angle labels and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param font  the font ({@code null} not permitted).
     *
     * @see #getAngleLabelFont()
     */
    public void setAngleLabelFont(Font font) {
        polarPlotProduct2.setAngleLabelFont(font, this);
    }

    /**
     * Returns the paint used to display the angle labels.
     *
     * @return A paint (never {@code null}).
     *
     * @see #setAngleLabelPaint(Paint)
     */
    public Paint getAngleLabelPaint() {
        return this.polarPlotProduct2.getAngleLabelPaint();
    }

    /**
     * Sets the paint used to display the angle labels and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param paint  the paint ({@code null} not permitted).
     */
    public void setAngleLabelPaint(Paint paint) {
        polarPlotProduct2.setAngleLabelPaint(paint, this);
    }

    /**
     * Returns {@code true} if the angular gridlines are visible, and
     * {@code false} otherwise.
     *
     * @return {@code true} or {@code false}.
     *
     * @see #setAngleGridlinesVisible(boolean)
     */
    public boolean isAngleGridlinesVisible() {
        return this.polarPlotProduct2.getAngleGridlinesVisible();
    }

    /**
     * Sets the flag that controls whether or not the angular grid-lines are
     * visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     *
     * @param visible  the new value of the flag.
     *
     * @see #isAngleGridlinesVisible()
     */
    public void setAngleGridlinesVisible(boolean visible) {
        polarPlotProduct2.setAngleGridlinesVisible(visible, this);
    }

    /**
     * Returns the stroke for the grid-lines (if any) plotted against the
     * angular axis.
     *
     * @return The stroke (possibly {@code null}).
     *
     * @see #setAngleGridlineStroke(Stroke)
     */
    public Stroke getAngleGridlineStroke() {
        return this.polarPlotProduct2.getAngleGridlineStroke();
    }

    /**
     * Sets the stroke for the grid lines plotted against the angular axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * <p>
     * If you set this to {@code null}, no grid lines will be drawn.
     *
     * @param stroke  the stroke ({@code null} permitted).
     *
     * @see #getAngleGridlineStroke()
     */
    public void setAngleGridlineStroke(Stroke stroke) {
        polarPlotProduct2.setAngleGridlineStroke(stroke, this);
    }

    /**
     * Returns the paint for the grid lines (if any) plotted against the
     * angular axis.
     *
     * @return The paint (possibly {@code null}).
     *
     * @see #setAngleGridlinePaint(Paint)
     */
    public Paint getAngleGridlinePaint() {
        return this.polarPlotProduct2.getAngleGridlinePaint();
    }

    /**
     * Sets the paint for the grid lines plotted against the angular axis.
     * <p>
     * If you set this to {@code null}, no grid lines will be drawn.
     *
     * @param paint  the paint ({@code null} permitted).
     *
     * @see #getAngleGridlinePaint()
     */
    public void setAngleGridlinePaint(Paint paint) {
        polarPlotProduct2.setAngleGridlinePaint(paint, this);
    }

    /**
     * Returns {@code true} if the radius axis grid is visible, and
     * {@code false} otherwise.
     *
     * @return {@code true} or {@code false}.
     *
     * @see #setRadiusGridlinesVisible(boolean)
     */
    public boolean isRadiusGridlinesVisible() {
        return this.polarPlotProduct2.getRadiusGridlinesVisible();
    }

    /**
     * Sets the flag that controls whether or not the radius axis grid lines
     * are visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     *
     * @param visible  the new value of the flag.
     *
     * @see #isRadiusGridlinesVisible()
     */
    public void setRadiusGridlinesVisible(boolean visible) {
        polarPlotProduct2.setRadiusGridlinesVisible(visible, this);
    }

    /**
     * Returns the stroke for the grid lines (if any) plotted against the
     * radius axis.
     *
     * @return The stroke (possibly {@code null}).
     *
     * @see #setRadiusGridlineStroke(Stroke)
     */
    public Stroke getRadiusGridlineStroke() {
        return this.polarPlotProduct2.getRadiusGridlineStroke();
    }

    /**
     * Sets the stroke for the grid lines plotted against the radius axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * <p>
     * If you set this to {@code null}, no grid lines will be drawn.
     *
     * @param stroke  the stroke ({@code null} permitted).
     *
     * @see #getRadiusGridlineStroke()
     */
    public void setRadiusGridlineStroke(Stroke stroke) {
        polarPlotProduct2.setRadiusGridlineStroke(stroke, this);
    }

    /**
     * Returns the paint for the grid lines (if any) plotted against the radius
     * axis.
     *
     * @return The paint (possibly {@code null}).
     *
     * @see #setRadiusGridlinePaint(Paint)
     */
    public Paint getRadiusGridlinePaint() {
        return this.polarPlotProduct2.getRadiusGridlinePaint();
    }

    /**
     * Sets the paint for the grid lines plotted against the radius axis and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * <p>
     * If you set this to {@code null}, no grid lines will be drawn.
     *
     * @param paint  the paint ({@code null} permitted).
     *
     * @see #getRadiusGridlinePaint()
     */
    public void setRadiusGridlinePaint(Paint paint) {
        polarPlotProduct2.setRadiusGridlinePaint(paint, this);
    }

    /**
     * Return the current value of the flag indicating if radial minor
     * grid-lines will be drawn or not.
     *
     * @return Returns {@code true} if radial minor grid-lines are drawn.
     */
    public boolean isRadiusMinorGridlinesVisible() {
        return this.radiusMinorGridlinesVisible;
    }

    /**
     * Set the flag that determines if radial minor grid-lines will be drawn,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param flag {@code true} to draw the radial minor grid-lines,
     *             {@code false} to hide them.
     */
    public void setRadiusMinorGridlinesVisible(boolean flag) {
        this.radiusMinorGridlinesVisible = flag;
        fireChangeEvent();
    }

    /**
     * Returns the margin around the plot area.
     *
     * @return The actual margin in pixels.
     */
    public int getMargin() {
        return this.polarPlotProduct.getMargin();
    }

    /**
     * Set the margin around the plot area and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param margin The new margin in pixels.
     */
    public void setMargin(int margin) {
        polarPlotProduct.setMargin(margin, this);
    }

    /**
     * Returns the fixed legend items, if any.
     *
     * @return The legend items (possibly {@code null}).
     *
     * @see #setFixedLegendItems(LegendItemCollection)
     */
    public LegendItemCollection getFixedLegendItems() {
        return this.fixedLegendItems;
    }

    /**
     * Sets the fixed legend items for the plot.  Leave this set to
     * {@code null} if you prefer the legend items to be created
     * automatically.
     *
     * @param items  the legend items ({@code null} permitted).
     *
     * @see #getFixedLegendItems()
     */
    public void setFixedLegendItems(LegendItemCollection items) {
        this.fixedLegendItems = items;
        fireChangeEvent();
    }

    /**
     * Add text to be displayed in the lower right hand corner and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param text  the text to display ({@code null} not permitted).
     *
     * @see #removeCornerTextItem(String)
     */
    public void addCornerTextItem(String text) {
        Args.nullNotPermitted(text, "text");
        this.cornerTextItems.add(text);
        fireChangeEvent();
    }

    /**
     * Remove the given text from the list of corner text items and
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param text  the text to remove ({@code null} ignored).
     *
     * @see #addCornerTextItem(String)
     */
    public void removeCornerTextItem(String text) {
        boolean removed = this.cornerTextItems.remove(text);
        if (removed) {
            fireChangeEvent();
        }
    }

    /**
     * Clear the list of corner text items and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @see #addCornerTextItem(String)
     * @see #removeCornerTextItem(String)
     */
    public void clearCornerTextItems() {
        if (!this.cornerTextItems.isEmpty()) {
            this.cornerTextItems.clear();
            fireChangeEvent();
        }
    }

    /**
     * Generates a list of tick values for the angular tick marks.
     *
     * @return A list of {@link NumberTick} instances.
     */
    protected List<ValueTick> refreshAngleTicks() {
        List<ValueTick> ticks = new ArrayList<>();
        for (double currentTickVal = 0.0; currentTickVal < 360.0;
                currentTickVal += this.angleTickUnit.getSize()) {
            TextAnchor ta = polarPlotProduct.calculateTextAnchor(currentTickVal);
            NumberTick tick = new NumberTick(currentTickVal,
                this.angleTickUnit.valueToString(currentTickVal),
                ta, TextAnchor.CENTER, 0.0);
            ticks.add(tick);
        }
        return ticks;
    }

    /**
     * Calculate the text position for the given degrees.
     *
     * @param angleDegrees  the angle in degrees.
     * 
     * @return The optimal text anchor.
     */
    protected TextAnchor calculateTextAnchor(double angleDegrees) {
        return polarPlotProduct.calculateTextAnchor(angleDegrees);
    }

    /**
     * Maps a dataset to a particular axis.  All data will be plotted
     * against axis zero by default, no mapping is required for this case.
     *
     * @param index  the dataset index (zero-based).
     * @param axisIndex  the axis index.
     */
    public void mapDatasetToAxis(int index, int axisIndex) {
        List<Integer> axisIndices = new ArrayList<>(1);
        axisIndices.add(axisIndex);
        mapDatasetToAxes(index, axisIndices);
    }

    /**
     * Maps the specified dataset to the axes in the list.  Note that the
     * conversion of data values into Java2D space is always performed using
     * the first axis in the list.
     *
     * @param index  the dataset index (zero-based).
     * @param axisIndices  the axis indices ({@code null} permitted).
     */
    public void mapDatasetToAxes(int index, List<Integer> axisIndices) {
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = index;
        this.datasetToAxesMap.put(key, new ArrayList<>(axisIndices));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
    }

    /**
     * This method is used to perform argument checking on the list of
     * axis indices passed to mapDatasetToAxes().
     *
     * @param indices  the list of indices ({@code null} permitted).
     */
    private void checkAxisIndices(List<Integer> indices) {
        // axisIndices can be:
        // 1.  null;
        // 2.  non-empty, containing only Integer objects that are unique.
        if (indices == null) {
            return;  // OK
        }
        if (indices.isEmpty()) {
            throw new IllegalArgumentException("Empty list not permitted.");
        }
        Set<Integer> set = new HashSet<>();
        for (Integer i : indices) {
            if (set.contains(i)) {
                throw new IllegalArgumentException("Indices must be unique.");
            }
            set.add(i);
        }
    }

    /**
     * Returns the axis for a dataset.
     *
     * @param index  the dataset index.
     *
     * @return The axis.
     */
    public ValueAxis getAxisForDataset(int index) {
        ValueAxis valueAxis;
        List<Integer> axisIndices = this.datasetToAxesMap.get(index);
        if (axisIndices != null) {
            // the first axis in the list is used for data <--> Java2D
            Integer axisIndex = axisIndices.get(0);
            valueAxis = getAxis(axisIndex);
        }
        else {
            valueAxis = getAxis(0);
        }
        return valueAxis;
    }

    /**
     * Returns the index of the given axis.
     *
     * @param axis  the axis.
     *
     * @return The axis index or -1 if axis is not used in this plot.
     */
    public int getAxisIndex(ValueAxis axis) {
        for (Entry<Integer, ValueAxis> entry : this.axes.entrySet()) {
            if (axis.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        // try the parent plot
        Plot parent = getParent();
        if (parent instanceof PolarPlot) {
            PolarPlot p = (PolarPlot) parent;
            return p.getAxisIndex(axis);
        }
        return -1;
    }

    /**
     * Returns the index of the specified renderer, or {@code -1} if the
     * renderer is not assigned to this plot.
     *
     * @param renderer  the renderer ({@code null} not permitted).
     *
     * @return The renderer index.
     */
    public int getIndexOf(PolarItemRenderer renderer) {
        Args.nullNotPermitted(renderer, "renderer");
        for (Entry<Integer, PolarItemRenderer> entry : this.renderers.entrySet()) {
            if (renderer.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Receives a chart element visitor.  Many plot subclasses will override
     * this method to handle their subcomponents.
     * 
     * @param visitor  the visitor ({@code null} not permitted).
     */
    @Override
    public void receive(ChartElementVisitor visitor) {
        // FIXME: handle axes and renderers
        visitor.visit(this);
    }

    /**
     * Draws the plot on a Java 2D graphics device (such as the screen or a
     * printer).
     * <P>
     * This plot relies on a {@link PolarItemRenderer} to draw each
     * item in the plot.  This allows the visual representation of the data to
     * be changed easily.
     * <P>
     * The optional info argument collects information about the rendering of
     * the plot (dimensions, tooltip information etc).  Just pass in
     * {@code null} if you do not need this information.
     *
     * @param g2  the graphics device.
     * @param area  the area within which the plot (including axes and
     *              labels) should be drawn.
     * @param anchor  the anchor point ({@code null} permitted).
     * @param parentState  ignored.
     * @param info  collects chart drawing information ({@code null}
     *              permitted).
     */
    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
            PlotState parentState, PlotRenderingInfo info) {

        // if the plot area is too small, just return...
        boolean b1 = (area.getWidth() <= MINIMUM_WIDTH_TO_DRAW);
        boolean b2 = (area.getHeight() <= MINIMUM_HEIGHT_TO_DRAW);
        if (b1 || b2) {
            return;
        }

        // record the plot area...
        if (info != null) {
            info.setPlotArea(area);
        }

        // adjust the drawing area for the plot insets (if any)...
        RectangleInsets insets = getInsets();
        insets.trim(area);

        Rectangle2D dataArea = area;
        if (info != null) {
            info.setDataArea(dataArea);
        }

        // draw the plot background and axes...
        drawBackground(g2, dataArea);
        int axisCount = this.axes.size();
        AxisState state = null;
        for (int i = 0; i < axisCount; i++) {
            ValueAxis axis = getAxis(i);
            if (axis != null) {
                PolarAxisLocation location = this.axisLocations.get(i);
                AxisState s = polarPlotProduct.drawAxis(axis, location, g2, dataArea);
                if (i == 0) {
                    state = s;
                }
            }
        }

        // now for each dataset, get the renderer and the appropriate axis
        // and render the dataset...
        Shape originalClip = g2.getClip();
        Composite originalComposite = g2.getComposite();

        g2.clip(dataArea);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                getForegroundAlpha()));
        this.angleTicks = refreshAngleTicks();
        drawGridlines(g2, dataArea, this.angleTicks, state.getTicks());
        render(g2, dataArea, info);
        g2.setClip(originalClip);
        g2.setComposite(originalComposite);
        drawOutline(g2, dataArea);
        drawCornerTextItems(g2, dataArea);
    }

    /**
     * Draws the corner text items.
     *
     * @param g2  the drawing surface.
     * @param area  the area.
     */
    protected void drawCornerTextItems(Graphics2D g2, Rectangle2D area) {
        if (this.cornerTextItems.isEmpty()) {
            return;
        }

        g2.setColor(Color.BLACK);
        double width = 0.0;
        double height = 0.0;
        for (String msg : this.cornerTextItems) {
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D bounds = TextUtils.getTextBounds(msg, g2, fm);
            width = Math.max(width, bounds.getWidth());
            height += bounds.getHeight();
        }

        double xadj = ANNOTATION_MARGIN * 2.0;
        double yadj = ANNOTATION_MARGIN;
        width += xadj;
        height += yadj;

        double x = area.getMaxX() - width;
        double y = area.getMaxY() - height;
        g2.drawRect((int) x, (int) y, (int) width, (int) height);
        x += ANNOTATION_MARGIN;
        for (String msg : this.cornerTextItems) {
            Rectangle2D bounds = TextUtils.getTextBounds(msg, g2,
                    g2.getFontMetrics());
            y += bounds.getHeight();
            g2.drawString(msg, (int) x, (int) y);
        }
    }

    /**
     * Draws the axis with the specified index.
     *
     * @param axis  the axis.
     * @param location  the axis location.
     * @param g2  the graphics target.
     * @param plotArea  the plot area.
     *
     * @return The axis state.
     */
    protected AxisState drawAxis(ValueAxis axis, PolarAxisLocation location,
            Graphics2D g2, Rectangle2D plotArea) {

        return polarPlotProduct.drawAxis(axis, location, g2, plotArea);
    }

    /**
     * Draws a representation of the data within the dataArea region, using the
     * current m_Renderer.
     *
     * @param g2  the graphics device.
     * @param dataArea  the region in which the data is to be drawn.
     * @param info  an optional object for collection dimension
     *              information ({@code null} permitted).
     */
    protected void render(Graphics2D g2, Rectangle2D dataArea,
            PlotRenderingInfo info) {

        // now get the data and plot it (the visual representation will depend
        // on the m_Renderer that has been set)...
        boolean hasData = false;
        int datasetCount = this.datasets.size();
        for (int i = datasetCount - 1; i >= 0; i--) {
            XYDataset dataset = getDataset(i);
            if (dataset == null) {
                continue;
            }
            PolarItemRenderer renderer = getRenderer(i);
            if (renderer == null) {
                continue;
            }
            if (!DatasetUtils.isEmptyOrNull(dataset)) {
                hasData = true;
                int seriesCount = dataset.getSeriesCount();
                for (int series = 0; series < seriesCount; series++) {
                    renderer.drawSeries(g2, dataArea, info, this, dataset,
                            series);
                }
            }
        }
        if (!hasData) {
            drawNoDataMessage(g2, dataArea);
        }
    }

    /**
     * Draws the gridlines for the plot, if they are visible.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param angularTicks  the ticks for the angular axis.
     * @param radialTicks  the ticks for the radial axis.
     */
    protected void drawGridlines(Graphics2D g2, Rectangle2D dataArea,
            List<ValueTick> angularTicks, List<ValueTick> radialTicks) {

        PolarItemRenderer renderer = getRenderer();
        // no renderer, no gridlines...
        if (renderer == null) {
            return;
        }

        // draw the domain grid lines, if any...
        if (isAngleGridlinesVisible()) {
            Stroke gridStroke = getAngleGridlineStroke();
            Paint gridPaint = getAngleGridlinePaint();
            if ((gridStroke != null) && (gridPaint != null)) {
                renderer.drawAngularGridLines(g2, this, angularTicks,
                        dataArea);
            }
        }

        // draw the radius grid lines, if any...
        if (isRadiusGridlinesVisible()) {
            Stroke gridStroke = getRadiusGridlineStroke();
            Paint gridPaint = getRadiusGridlinePaint();
            if ((gridStroke != null) && (gridPaint != null)) {
                List<ValueTick> ticks = buildRadialTicks(radialTicks);
                renderer.drawRadialGridLines(g2, this, getAxis(), ticks, dataArea);
            }
        }
    }

    /**
     * Create a list of ticks based on the given list and plot properties.
     * Only ticks of a specific type may be in the result list.
     *
     * @param allTicks A list of all available ticks for the primary axis.
     *        {@code null} not permitted.
     * @return Ticks to use for radial gridlines.
     */
    protected List<ValueTick> buildRadialTicks(List<ValueTick> allTicks) {
        List<ValueTick> ticks = new ArrayList<>();
        for (ValueTick tick : allTicks) {
            if (isRadiusMinorGridlinesVisible() || TickType.MAJOR.equals(tick.getTickType())) {
                ticks.add(tick);
            }
        }
        return ticks;
    }

    /**
     * Zooms the axis ranges by the specified percentage about the anchor point.
     *
     * @param percent  the amount of the zoom.
     */
    @Override
    public void zoom(double percent) {
        for (int axisIdx = 0; axisIdx < getAxisCount(); axisIdx++) {
            final ValueAxis axis = getAxis(axisIdx);
            if (axis != null) {
                if (percent > 0.0) {
                    double radius = axis.getUpperBound();
                    double scaledRadius = radius * percent;
                    axis.setUpperBound(scaledRadius);
                    axis.setAutoRange(false);
                } else {
                    axis.setAutoRange(true);
                }
            }
        }
    }

    /**
     * A utility method that returns a list of datasets that are mapped to a
     * particular axis.
     *
     * @param axisIndex  the axis index ({@code null} not permitted).
     *
     * @return A list of datasets.
     */
    private List<XYDataset> getDatasetsMappedToAxis(Integer axisIndex) { 
        Args.nullNotPermitted(axisIndex, "axisIndex");
        List<XYDataset> result = new ArrayList<>();
       for (Entry<Integer, XYDataset> entry : this.datasets.entrySet()) {
            List<Integer> mappedAxes = this.datasetToAxesMap.get(entry.getKey());
            if (mappedAxes == null) {
                if (axisIndex.equals(ZERO)) {
                    result.add(getDataset(entry.getKey()));
                }
            } else {
                if (mappedAxes.contains(axisIndex)) {
                    result.add(getDataset(entry.getKey()));
                }
            }
        }
        return result;
    }

    /**
     * Returns the range for the specified axis.
     *
     * @param axis  the axis.
     *
     * @return The range.
     */
    @Override
    public Range getDataRange(ValueAxis axis) {
        Range result = null;
        List<XYDataset> mappedDatasets = new ArrayList<>();
        int axisIndex = getAxisIndex(axis);
        if (axisIndex >= 0) {
            mappedDatasets = getDatasetsMappedToAxis(axisIndex);
        }

        // iterate through the datasets that map to the axis and get the union
        // of the ranges.
        for (XYDataset dataset : mappedDatasets) {
            if (dataset != null) {
                // FIXME better ask the renderer instead of DatasetUtilities
                result = Range.combine(result, DatasetUtils.findRangeBounds(dataset));
            }
        }

        return result;
    }

    /**
     * Receives notification of a change to the plot's m_Dataset.
     * <P>
     * The axis ranges are updated if necessary.
     *
     * @param event  information about the event (not used here).
     */
    @Override
    public void datasetChanged(DatasetChangeEvent event) {
        for (int i = 0; i < this.axes.size(); i++) {
            final ValueAxis axis = (ValueAxis) this.axes.get(i);
            if (axis != null) {
                axis.configure();
            }
        }
        if (getParent() != null) {
            getParent().datasetChanged(event);
        }
        else {
            super.datasetChanged(event);
        }
    }

    /**
     * Notifies all registered listeners of a property change.
     * <P>
     * One source of property change events is the plot's m_Renderer.
     *
     * @param event  information about the property change.
     */
    @Override
    public void rendererChanged(RendererChangeEvent event) {
        fireChangeEvent();
    }

    /**
     * Returns the legend items for the plot.  Each legend item is generated by
     * the plot's m_Renderer, since the m_Renderer is responsible for the visual
     * representation of the data.
     *
     * @return The legend items.
     */
    @Override
    public LegendItemCollection getLegendItems() {
        if (this.fixedLegendItems != null) {
            return this.fixedLegendItems;
        }
        LegendItemCollection result = new LegendItemCollection();
        int count = this.datasets.size();
        for (int datasetIndex = 0; datasetIndex < count; datasetIndex++) {
            XYDataset dataset = getDataset(datasetIndex);
            PolarItemRenderer renderer = getRenderer(datasetIndex);
            if (dataset != null && renderer != null) {
                int seriesCount = dataset.getSeriesCount();
                for (int i = 0; i < seriesCount; i++) {
                    LegendItem item = renderer.getLegendItem(i);
                    result.add(item);
                }
            }
        }
        return result;
    }

    /**
     * Tests this plot for equality with another object.
     *
     * @param obj  the object ({@code null} permitted).
     *
     * @return {@code true} or {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PolarPlot)) {
            return false;
        }
        PolarPlot that = (PolarPlot) obj;
        if (!this.axes.equals(that.axes)) {
            return false;
        }
        if (!this.axisLocations.equals(that.axisLocations)) {
            return false;
        }
        if (!this.renderers.equals(that.renderers)) {
            return false;
        }
        if (!this.angleTickUnit.equals(that.angleTickUnit)) {
            return false;
        }
        if (this.polarPlotProduct2.getAngleGridlinesVisible() != that.polarPlotProduct2.getAngleGridlinesVisible()) {
            return false;
        }
        if (this.polarPlotProduct.getAngleOffset() != that.polarPlotProduct.getAngleOffset())
        {
            return false;
        }
        if (this.polarPlotProduct.getCounterClockwise() != that.polarPlotProduct.getCounterClockwise())
        {
            return false;
        }
        if (this.polarPlotProduct2.getAngleLabelsVisible() != that.polarPlotProduct2.getAngleLabelsVisible()) {
            return false;
        }
        if (!this.polarPlotProduct2.getAngleLabelFont().equals(that.polarPlotProduct2.getAngleLabelFont())) {
            return false;
        }
        if (!PaintUtils.equal(this.polarPlotProduct2.getAngleLabelPaint(), that.polarPlotProduct2.getAngleLabelPaint())) {
            return false;
        }
        if (!Objects.equals(this.polarPlotProduct2.getAngleGridlineStroke(), that.polarPlotProduct2.getAngleGridlineStroke())) {
            return false;
        }
        if (!PaintUtils.equal(
            this.polarPlotProduct2.getAngleGridlinePaint(), that.polarPlotProduct2.getAngleGridlinePaint()
        )) {
            return false;
        }
        if (this.polarPlotProduct2.getRadiusGridlinesVisible() != that.polarPlotProduct2.getRadiusGridlinesVisible()) {
            return false;
        }
        if (!Objects.equals(this.polarPlotProduct2.getRadiusGridlineStroke(), that.polarPlotProduct2.getRadiusGridlineStroke())) {
            return false;
        }
        if (!PaintUtils.equal(this.polarPlotProduct2.getRadiusGridlinePaint(),
                that.polarPlotProduct2.getRadiusGridlinePaint())) {
            return false;
        }
        if (this.radiusMinorGridlinesVisible !=
            that.radiusMinorGridlinesVisible) {
            return false;
        }
        if (!this.cornerTextItems.equals(that.cornerTextItems)) {
            return false;
        }
        if (this.polarPlotProduct.getMargin() != that.polarPlotProduct.getMargin()) {
            return false;
        }
        if (!Objects.equals(this.fixedLegendItems, that.fixedLegendItems)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Returns a clone of the plot.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  this can occur if some component of
     *         the plot cannot be cloned.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        PolarPlot clone = (PolarPlot) super.clone();
		clone.polarPlotProduct2 = (PolarPlotProduct2) this.polarPlotProduct2.clone();
		clone.polarPlotProduct = (PolarPlotProduct) this.polarPlotProduct.clone();
        clone.axes = CloneUtils.clone(this.axes);
        for (int i = 0; i < this.axes.size(); i++) {
            ValueAxis axis = (ValueAxis) this.axes.get(i);
            if (axis != null) {
                ValueAxis clonedAxis = (ValueAxis) axis.clone();
                clone.axes.put(i, clonedAxis);
                clonedAxis.setPlot(clone);
                clonedAxis.addChangeListener(clone);
            }
        }

        // the datasets are not cloned, but listeners need to be added...
        clone.datasets = CloneUtils.clone(this.datasets);
        for (int i = 0; i < clone.datasets.size(); ++i) {
            XYDataset d = getDataset(i);
            if (d != null) {
                d.addChangeListener(clone);
            }
        }

        clone.renderers = CloneUtils.clone(this.renderers);
        for (int i = 0; i < this.renderers.size(); i++) {
            PolarItemRenderer renderer2 = (PolarItemRenderer) this.renderers.get(i);
            if (renderer2 instanceof PublicCloneable) {
                PublicCloneable pc = (PublicCloneable) renderer2;
                PolarItemRenderer rc = (PolarItemRenderer) pc.clone();
                clone.renderers.put(i, rc);
                rc.setPlot(clone);
                rc.addChangeListener(clone);
            }
        }

        clone.cornerTextItems = new ArrayList<>(this.cornerTextItems);

        return clone;
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtils.writeStroke(this.polarPlotProduct2.getAngleGridlineStroke(), stream);
        SerialUtils.writePaint(this.polarPlotProduct2.getAngleGridlinePaint(), stream);
        SerialUtils.writeStroke(this.polarPlotProduct2.getRadiusGridlineStroke(), stream);
        SerialUtils.writePaint(this.polarPlotProduct2.getRadiusGridlinePaint(), stream);
        SerialUtils.writePaint(this.polarPlotProduct2.getAngleLabelPaint(), stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {

        stream.defaultReadObject();
        polarPlotProduct2.setAngleGridlineStroke2(SerialUtils.readStroke(stream));
        polarPlotProduct2.setAngleGridlinePaint2(SerialUtils.readPaint(stream));
        polarPlotProduct2.setRadiusGridlineStroke2(SerialUtils.readStroke(stream));
        polarPlotProduct2.setRadiusGridlinePaint2(SerialUtils.readPaint(stream));
        polarPlotProduct2.setAngleLabelPaint2(SerialUtils.readPaint(stream));

        int rangeAxisCount = this.axes.size();
        for (int i = 0; i < rangeAxisCount; i++) {
            Axis axis = (Axis) this.axes.get(i);
            if (axis != null) {
                axis.setPlot(this);
                axis.addChangeListener(this);
            }
        }
        int datasetCount = this.datasets.size();
        for (int i = 0; i < datasetCount; i++) {
            Dataset dataset = (Dataset) this.datasets.get(i);
            if (dataset != null) {
                dataset.addChangeListener(this);
            }
        }
        int rendererCount = this.renderers.size();
        for (int i = 0; i < rendererCount; i++) {
            PolarItemRenderer renderer = (PolarItemRenderer) this.renderers.get(i);
            if (renderer != null) {
                renderer.addChangeListener(this);
            }
        }
    }

    /**
     * This method is required by the {@link Zoomable} interface, but since
     * the plot does not have any domain axes, it does nothing.
     *
     * @param factor  the zoom factor.
     * @param state  the plot state.
     * @param source  the source point (in Java2D coordinates).
     */
    @Override
    public void zoomDomainAxes(double factor, PlotRenderingInfo state,
                               Point2D source) {
        // do nothing
    }

    /**
     * This method is required by the {@link Zoomable} interface, but since
     * the plot does not have any domain axes, it does nothing.
     *
     * @param factor  the zoom factor.
     * @param state  the plot state.
     * @param source  the source point (in Java2D coordinates).
     * @param useAnchor  use source point as zoom anchor?
     */
    @Override
    public void zoomDomainAxes(double factor, PlotRenderingInfo state,
                               Point2D source, boolean useAnchor) {
        // do nothing
    }

    /**
     * This method is required by the {@link Zoomable} interface, but since
     * the plot does not have any domain axes, it does nothing.
     *
     * @param lowerPercent  the new lower bound.
     * @param upperPercent  the new upper bound.
     * @param state  the plot state.
     * @param source  the source point (in Java2D coordinates).
     */
    @Override
    public void zoomDomainAxes(double lowerPercent, double upperPercent,
                               PlotRenderingInfo state, Point2D source) {
        // do nothing
    }

    /**
     * Multiplies the range on the range axis/axes by the specified factor.
     *
     * @param factor  the zoom factor.
     * @param state  the plot state.
     * @param source  the source point (in Java2D coordinates).
     */
    @Override
    public void zoomRangeAxes(double factor, PlotRenderingInfo state,
                              Point2D source) {
        zoom(factor);
    }

    /**
     * Multiplies the range on the range axis by the specified factor.
     *
     * @param factor  the zoom factor.
     * @param info  the plot rendering info.
     * @param source  the source point (in Java2D space).
     * @param useAnchor  use source point as zoom anchor?
     *
     * @see #zoomDomainAxes(double, PlotRenderingInfo, Point2D, boolean)
     */
    @Override
    public void zoomRangeAxes(double factor, PlotRenderingInfo info,
                              Point2D source, boolean useAnchor) {
        // get the source coordinate - this plot has always a VERTICAL
        // orientation
        final double sourceX = source.getX();

        for (int axisIdx = 0; axisIdx < getAxisCount(); axisIdx++) {
            final ValueAxis axis = getAxis(axisIdx);
            if (axis != null) {
                if (useAnchor) {
                    double anchorX = axis.java2DToValue(sourceX,
                            info.getDataArea(), RectangleEdge.BOTTOM);
                    axis.resizeRange(factor, anchorX);
                }
                else {
                    axis.resizeRange(factor);
                }
            }
        }
    }

    /**
     * Zooms in on the range axes.
     *
     * @param lowerPercent  the new lower bound.
     * @param upperPercent  the new upper bound.
     * @param state  the plot state.
     * @param source  the source point (in Java2D coordinates).
     */
    @Override
    public void zoomRangeAxes(double lowerPercent, double upperPercent,
                              PlotRenderingInfo state, Point2D source) {
        zoom((upperPercent + lowerPercent) / 2.0);
    }

    /**
     * Returns {@code false} always.
     *
     * @return {@code false} always.
     */
    @Override
    public boolean isDomainZoomable() {
        return false;
    }

    /**
     * Returns {@code true} to indicate that the range axis is zoomable.
     *
     * @return {@code true}.
     */
    @Override
    public boolean isRangeZoomable() {
        return true;
    }

    /**
     * Returns the orientation of the plot.
     *
     * @return The orientation.
     */
    @Override
    public PlotOrientation getOrientation() {
        return PlotOrientation.HORIZONTAL;
    }

    /**
     * Translates a (theta, radius) pair into Java2D coordinates.  If
     * {@code radius} is less than the lower bound of the axis, then
     * this method returns the centre point.
     *
     * @param angleDegrees  the angle in degrees.
     * @param radius  the radius.
     * @param axis  the axis.
     * @param dataArea  the data area.
     *
     * @return A point in Java2D space.
     */
    public Point translateToJava2D(double angleDegrees, double radius,
            ValueAxis axis, Rectangle2D dataArea) {

        return polarPlotProduct.translateToJava2D(angleDegrees, radius, axis, dataArea);

    }

}
