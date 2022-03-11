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
 * -------------
 * YearTest.java
 * -------------
 * (C) Copyright 2001-2022, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.time;

import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Consumer;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Year} class.
 */
public class YearTest {

    /**
     * Check that a Year instance is equal to itself.
     *
     * SourceForge Bug ID: 558850.
     */
    @Test
    public void testEqualsSelf() {
        Year year = new Year();
        assertEquals(year, year);
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        Year year1 = new Year(2002);
        Year year2 = new Year(2002);
        assertEquals(year1, year2);

        year1 = new Year(1999);
        assertNotEquals(year1, year2);
        year2 = new Year(1999);
        assertEquals(year1, year2);
    }

    /**
     * In GMT, the end of 2001 is java.util.Date(1009843199999L).  Use this to
     * check the year constructor.
     */
    @Test
    public void testDateConstructor1() {

        TimeZone zone = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(zone);
        Date d1 = new Date(1009843199999L);
        Date d2 = new Date(1009843200000L);
        Year y1 = new Year(d1, zone, Locale.getDefault());
        Year y2 = new Year(d2, zone, Locale.getDefault());

        assertEquals(2001, y1.getYear());
        assertEquals(1009843199999L, y1.getLastMillisecond(cal));

        assertEquals(2002, y2.getYear());
        assertEquals(1009843200000L, y2.getFirstMillisecond(cal));

    }

    /**
     * In Los Angeles, the end of 2001 is java.util.Date(1009871999999L).  Use
     * this to check the year constructor.
     */
    @Test
    public void testDateConstructor2() {

        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(zone);
        Year y1 = new Year(new Date(1009871999999L), zone, Locale.getDefault());
        Year y2 = new Year(new Date(1009872000000L), zone, Locale.getDefault());

        assertEquals(2001, y1.getYear());
        assertEquals(1009871999999L, y1.getLastMillisecond(cal));

        assertEquals(2002, y2.getYear());
        assertEquals(1009872000000L, y2.getFirstMillisecond(cal));

    }

    /**
     * If a thread-local calendar was set, the Date constructor should use it.
     */
    @Test
    public void testDateConstructorWithThreadLocalCalendar() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setThreadLocalCalendarInstance(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testDateConstructorWithCustomCalendar(3, calendarSetup);
        testDateConstructorWithCustomCalendar(4, calendarSetup);
    }

    /**
     * If a calendar prototype was set, the Date constructor should use it.
     */
    @Test
    public void testDateConstructorWithCalendarPrototype() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setCalendarInstancePrototype(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testDateConstructorWithCustomCalendar(3, calendarSetup);
        testDateConstructorWithCustomCalendar(4, calendarSetup);
    }

    private void testDateConstructorWithCustomCalendar(int hoursOffset, Consumer<Integer> calendarSetup) {
        try {
            calendarSetup.accept(hoursOffset);
            long ms = -3_600_000L * hoursOffset;
            Year y = new Year(new Date(ms));
            assertEquals(1970, y.getYear());
            assertEquals(ms, y.getFirstMillisecond());
        } finally {
            // reset everything, to avoid affecting other tests
            RegularTimePeriod.setThreadLocalCalendarInstance(null);
            RegularTimePeriod.setCalendarInstancePrototype(null);
        }
    }

    /**
     * If a thread-local calendar was set, the year constructor should use it.
     */
    @Test
    public void testYearConstructorWithThreadLocalCalendar() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setThreadLocalCalendarInstance(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testYearConstructorWithCustomCalendar(3, calendarSetup);
        testYearConstructorWithCustomCalendar(4, calendarSetup);
    }

    /**
     * If a calendar prototype was set, the year constructor should use it.
     */
    @Test
    public void testYearConstructorWithCalendarPrototype() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setCalendarInstancePrototype(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testYearConstructorWithCustomCalendar(3, calendarSetup);
        testYearConstructorWithCustomCalendar(4, calendarSetup);
    }

    private void testYearConstructorWithCustomCalendar(int hoursOffset, Consumer<Integer> calendarSetup) {
        try {
            calendarSetup.accept(hoursOffset);
            long ms = -3_600_000L * hoursOffset;
            Year y = new Year(1970);
            assertEquals(1970, y.getYear());
            assertEquals(ms, y.getFirstMillisecond());
        } finally {
            // reset everything, to avoid affecting other tests
            RegularTimePeriod.setThreadLocalCalendarInstance(null);
            RegularTimePeriod.setCalendarInstancePrototype(null);
        }
    }

    /**
     * Set up a year equal to 1900.  Request the previous year, it should be
     * null.
     */
    @Test
    public void testMinuss9999Previous() {
        Year current = new Year(-9999);
        Year previous = (Year) current.previous();
        assertNull(previous);
    }

    /**
     * Set up a year equal to 1900.  Request the next year, it should be 1901.
     */
    @Test
    public void test1900Next() {
        Year current = new Year(1900);
        Year next = (Year) current.next();
        assertEquals(1901, next.getYear());
    }

    /**
     * Set up a year equal to 9999.  Request the previous year, it should be
     * 9998.
     */
    @Test
    public void test9999Previous() {
        Year current = new Year(9999);
        Year previous = (Year) current.previous();
        assertEquals(9998, previous.getYear());
    }

    /**
     * Set up a year equal to 9999.  Request the next year, it should be null.
     */
    @Test
    public void test9999Next() {
        Year current = new Year(9999);
        Year next = (Year) current.next();
        assertNull(next);
    }

    /**
     * Tests the year string parser.
     */
    @Test
    public void testParseYear() {

        Year year = null;

        // test 1...
        try {
            year = Year.parseYear("2000");
        }
        catch (TimePeriodFormatException e) {
            year = new Year(1900);
        }
        assertEquals(2000, year.getYear());

        // test 2...
        try {
            year = Year.parseYear(" 2001 ");
        }
        catch (TimePeriodFormatException e) {
            year = new Year(1900);
        }
        assertEquals(2001, year.getYear());

        // test 3...
        try {
            year = Year.parseYear("99");
        }
        catch (TimePeriodFormatException e) {
            year = new Year(1900);
        }
        assertEquals(99, year.getYear());

    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        Year y1 = new Year(1999);
        Year y2 = TestUtils.serialised(y1);
        assertEquals(y1, y2);
    }

    /**
     * The {@link Year} class is immutable, so should not be {@link Cloneable}.
     */
    @Test
    public void testNotCloneable() {
        Year y = new Year(1999);
        assertFalse(y instanceof Cloneable);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashcode() {
        Year y1 = new Year(1988);
        Year y2 = new Year(1988);
        assertEquals(y1, y2);
        int h1 = y1.hashCode();
        int h2 = y2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Some checks for the getFirstMillisecond() method.
     */
    @Test
    public void testGetFirstMillisecond() {
        Locale saved = Locale.getDefault();
        Locale.setDefault(Locale.UK);
        TimeZone savedZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Year y = new Year(1970);
        // TODO: Check this result...
        assertEquals(-3600000L, y.getFirstMillisecond());
        Locale.setDefault(saved);
        TimeZone.setDefault(savedZone);
    }

    /**
     * Some checks for the getFirstMillisecond(TimeZone) method.
     */
    @Test
    public void testGetFirstMillisecondWithTimeZone() {
        Year y = new Year(1950);
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(zone);
        assertEquals(-631123200000L, y.getFirstMillisecond(cal));

        // try null calendar
        boolean pass = false;
        try {
            y.getFirstMillisecond((Calendar) null);
        }
        catch (NullPointerException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getFirstMillisecond(TimeZone) method.
     */
    @Test
    public void testGetFirstMillisecondWithCalendar() {
        Year y = new Year(2001);
        GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
        assertEquals(978307200000L, y.getFirstMillisecond(calendar));

        // try null calendar
        boolean pass = false;
        try {
            y.getFirstMillisecond((Calendar) null);
        }
        catch (NullPointerException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getLastMillisecond() method.
     */
    @Test
    public void testGetLastMillisecond() {
        Locale saved = Locale.getDefault();
        Locale.setDefault(Locale.UK);
        TimeZone savedZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Year y = new Year(1970);
        // TODO: Check this result...
        assertEquals(31532399999L, y.getLastMillisecond());
        Locale.setDefault(saved);
        TimeZone.setDefault(savedZone);
    }

    /**
     * Some checks for the getLastMillisecond(TimeZone) method.
     */
    @Test
    public void testGetLastMillisecondWithTimeZone() {
        Year y = new Year(1950);
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(zone);
        assertEquals(-599587200001L, y.getLastMillisecond(cal));

        // try null calendar
        boolean pass = false;
        try {
            y.getLastMillisecond((Calendar) null);
        }
        catch (NullPointerException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getLastMillisecond(TimeZone) method.
     */
    @Test
    public void testGetLastMillisecondWithCalendar() {
        Year y = new Year(2001);
        GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
        assertEquals(1009843199999L, y.getLastMillisecond(calendar));

        // try null calendar
        boolean pass = false;
        try {
            y.getLastMillisecond((Calendar) null);
        }
        catch (NullPointerException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    /**
     * Some checks for the getSerialIndex() method.
     */
    @Test
    public void testGetSerialIndex() {
        Year y = new Year(2000);
        assertEquals(2000L, y.getSerialIndex());
    }

    @Test
    public void testNext() {
        Year y = new Year(2000);
        y = (Year) y.next();
        assertEquals(2001, y.getYear());
        y = (Year) y.previous();
        assertEquals(2000, y.getYear());
        y = new Year(9999);
        assertNull(y.next());
    }

    /**
     * If a thread-local calendar was set, next() and previous() should use its time zone.
     */
    @Test
    public void testNextPreviousWithThreadLocalCalendar() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setThreadLocalCalendarInstance(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testNextPreviousWithCustomCalendar(3, calendarSetup);
        testNextPreviousWithCustomCalendar(4, calendarSetup);
    }

    /**
     * If a calendar prototype was set, next() should use its time zone.
     */
    @Test
    public void testNextPreviousWithCalendarPrototype() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setCalendarInstancePrototype(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testNextPreviousWithCustomCalendar(3, calendarSetup);
        testNextPreviousWithCustomCalendar(4, calendarSetup);
    }

    private void testNextPreviousWithCustomCalendar(int hoursOffset, Consumer<Integer> calendarSetup) {
        try {
            calendarSetup.accept(hoursOffset);
            long ms = -hoursOffset * 3_600_000L;
            Year y = new Year(new Date(ms));
            y = (Year) y.next();
            assertEquals(1971, y.getYear());
            assertEquals(ms + 86_400_000L * 365, y.getFirstMillisecond());
            y = (Year) y.previous();
            assertEquals(1970, y.getYear());
            assertEquals(ms, y.getFirstMillisecond());
        } finally {
            // reset everything, to avoid affecting other tests
            RegularTimePeriod.setThreadLocalCalendarInstance(null);
            RegularTimePeriod.setCalendarInstancePrototype(null);
        }
    }

    /**
     * Some checks for the getStart() method.
     */
    @Test
    public void testGetStart() {
        Locale saved = Locale.getDefault();
        Locale.setDefault(Locale.ITALY);
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.set(2006, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Year y = new Year(2006);
        assertEquals(cal.getTime(), y.getStart());
        Locale.setDefault(saved);
    }

    /**
     * Some checks for the getEnd() method.
     */
    @Test
    public void testGetEnd() {
        Locale saved = Locale.getDefault();
        Locale.setDefault(Locale.ITALY);
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.set(2006, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Year y = new Year(2006);
        assertEquals(cal.getTime(), y.getEnd());
        Locale.setDefault(saved);
    }

}
