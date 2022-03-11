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
 * -------------------------------------------
 * StandardCategoryItemLabelGeneratorTest.java
 * -------------------------------------------
 * (C) Copyright 2003-2022, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link StandardCategoryItemLabelGenerator} class.
 */
public class StandardCategoryItemLabelGeneratorTest {

    /**
     * Some checks for the generalLabel() method.
     */
    @Test
    public void testGenerateLabel() {
        StandardCategoryItemLabelGenerator g
                = new StandardCategoryItemLabelGenerator("{2}",
                new DecimalFormat("0.000"));
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        dataset.addValue(1.0, "R0", "C0");
        dataset.addValue(2.0, "R0", "C1");
        dataset.addValue(3.0, "R1", "C0");
        dataset.addValue(null, "R1", "C1");
        String s = g.generateLabel(dataset, 0, 0);
        assertTrue(s.startsWith("1"));
        assertTrue(s.endsWith("000"));

        // try a null value
        s = g.generateLabel(dataset, 1, 1);
        assertEquals("-", s);
    }

    /**
     * Some checks for the equals() method.
     */
    @Test
    public void testEquals() {
        StandardCategoryItemLabelGenerator g1
                = new StandardCategoryItemLabelGenerator();
        StandardCategoryItemLabelGenerator g2
                = new StandardCategoryItemLabelGenerator();
        assertEquals(g1, g2);
        assertEquals(g2, g1);

        g1 = new StandardCategoryItemLabelGenerator("{0}",
                new DecimalFormat("0.000"));
        assertNotEquals(g1, g2);
        g2 = new StandardCategoryItemLabelGenerator("{0}",
                new DecimalFormat("0.000"));
        assertEquals(g1, g2);

        g1 = new StandardCategoryItemLabelGenerator("{1}",
                new DecimalFormat("0.000"));
        assertNotEquals(g1, g2);
        g2 = new StandardCategoryItemLabelGenerator("{1}",
                new DecimalFormat("0.000"));
        assertEquals(g1, g2);

        g1 = new StandardCategoryItemLabelGenerator("{2}",
                new SimpleDateFormat("d-MMM"));
        assertNotEquals(g1, g2);
        g2 = new StandardCategoryItemLabelGenerator("{2}",
                new SimpleDateFormat("d-MMM"));
        assertEquals(g1, g2);

    }

    /**
     * Simple check that hashCode is implemented.
     */
    @Test
    public void testHashCode() {
        StandardCategoryItemLabelGenerator g1
                = new StandardCategoryItemLabelGenerator();
        StandardCategoryItemLabelGenerator g2
                = new StandardCategoryItemLabelGenerator();
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        StandardCategoryItemLabelGenerator g1
                = new StandardCategoryItemLabelGenerator();
        StandardCategoryItemLabelGenerator g2 = CloneUtils.clone(g1);
        assertNotSame(g1, g2);
        assertSame(g1.getClass(), g2.getClass());
        assertEquals(g1, g2);
    }

    /**
     * Check to ensure that this class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        StandardCategoryItemLabelGenerator g1
                = new StandardCategoryItemLabelGenerator();
        assertTrue(g1 instanceof PublicCloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        StandardCategoryItemLabelGenerator g1
                = new StandardCategoryItemLabelGenerator("{2}",
                DateFormat.getInstance());
        StandardCategoryItemLabelGenerator g2 = TestUtils.serialised(g1);
        assertEquals(g1, g2);
    }

    /**
     * A test for bug 1481087.
     */
    @Test
    public void testEquals1481087() {
        StandardCategoryItemLabelGenerator g1
                = new StandardCategoryItemLabelGenerator("{0}",
                new DecimalFormat("0.00"));
        StandardCategoryToolTipGenerator g2
                = new StandardCategoryToolTipGenerator("{0}",
                new DecimalFormat("0.00"));
        assertNotEquals(g1, g2);
    }

}
