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
 * MonthTest.java
 * --------------
 * (C) Copyright 2001-2022, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.time;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Consumer;

import org.jfree.chart.TestUtils;
import org.jfree.chart.date.MonthConstants;
import org.jfree.chart.date.SerialDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Month} class.
 */
public class MonthTest {

    /** A month. */
    private Month jan1900;

    /** A month. */
    private Month feb1900;

    /** A month. */
    private Month nov9999;

    /** A month. */
    private Month dec9999;

    /**
     * Common test setup.
     */
    @BeforeEach
    public void setUp() {
        this.jan1900 = new Month(MonthConstants.JANUARY, 1900);
        this.feb1900 = new Month(MonthConstants.FEBRUARY, 1900);
        this.nov9999 = new Month(MonthConstants.NOVEMBER, 9999);
        this.dec9999 = new Month(MonthConstants.DECEMBER, 9999);
    }

    /**
     * Check that a Month instance is equal to itself.
     *
     * SourceForge Bug ID: 558850.
     */
    @Test
    public void testEqualsSelf() {
        Month month = new Month();
        assertEquals(month, month);
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        Month m1 = new Month(MonthConstants.MAY, 2002);
        Month m2 = new Month(MonthConstants.MAY, 2002);
        assertEquals(m1, m2);
    }

    /**
     * In GMT, the end of Feb 2000 is java.util.Date(951,868,799,999L).  Use
     * this to check the Month constructor.
     */
    @Test
    public void testDateConstructor1() {

        TimeZone zone = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(zone);
        Month m1 = new Month(new Date(951868799999L), zone, Locale.getDefault());
        Month m2 = new Month(new Date(951868800000L), zone, Locale.getDefault());

        assertEquals(MonthConstants.FEBRUARY, m1.getMonth());
        assertEquals(951868799999L, m1.getLastMillisecond(cal));

        assertEquals(MonthConstants.MARCH, m2.getMonth());
        assertEquals(951868800000L, m2.getFirstMillisecond(cal));

    }

    /**
     * In Auckland, the end of Feb 2000 is java.util.Date(951,821,999,999L).
     * Use this to check the Month constructor.
     */
    @Test
    public void testDateConstructor2() {

        TimeZone zone = TimeZone.getTimeZone("Pacific/Auckland");
        Calendar cal = Calendar.getInstance(zone);
        Month m1 = new Month(new Date(951821999999L), zone, Locale.getDefault());
        Month m2 = new Month(new Date(951822000000L), zone, Locale.getDefault());

        assertEquals(MonthConstants.FEBRUARY, m1.getMonth());
        assertEquals(951821999999L, m1.getLastMillisecond(cal));

        assertEquals(MonthConstants.MARCH, m2.getMonth());
        assertEquals(951822000000L, m2.getFirstMillisecond(cal));

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
            Month m = new Month(new Date(ms));
            assertEquals(1970, m.getYear().getYear());
            assertEquals(1, m.getMonth());
            assertEquals(ms, m.getFirstMillisecond());
        } finally {
            // reset everything, to avoid affecting other tests
            RegularTimePeriod.setThreadLocalCalendarInstance(null);
            RegularTimePeriod.setCalendarInstancePrototype(null);
        }
    }

    /**
     * If a thread-local calendar was set, the (int, int) month-year constructor should use it.
     */
    @Test
    public void testMonthIntYearConstructorWithThreadLocalCalendar() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setThreadLocalCalendarInstance(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testMonthIntYearConstructorWithCustomCalendar(3, calendarSetup);
        testMonthIntYearConstructorWithCustomCalendar(4, calendarSetup);
    }

    /**
     * If a calendar prototype was set, the the (int, int) month-year constructor should use it.
     */
    @Test
    public void testMonthIntYearConstructorWithCalendarPrototype() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setCalendarInstancePrototype(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testMonthIntYearConstructorWithCustomCalendar(3, calendarSetup);
        testMonthIntYearConstructorWithCustomCalendar(4, calendarSetup);
    }

    private void testMonthIntYearConstructorWithCustomCalendar(int hoursOffset, Consumer<Integer> calendarSetup) {
        try {
            calendarSetup.accept(hoursOffset);
            Month m = new Month(1, 1970);
            assertEquals(1970, m.getYear().getYear());
            assertEquals(1, m.getMonth());
            assertEquals(-3_600_000L * hoursOffset, m.getFirstMillisecond());
        } finally {
            // reset everything, to avoid affecting other tests
            RegularTimePeriod.setThreadLocalCalendarInstance(null);
            RegularTimePeriod.setCalendarInstancePrototype(null);
        }
    }

    /**
     * If a thread-local calendar was set, the (int, Year) month-year constructor should use it.
     */
    @Test
    public void testMonthYearConstructorWithThreadLocalCalendar() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setThreadLocalCalendarInstance(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testMonthYearConstructorWithCustomCalendar(3, calendarSetup);
        testMonthYearConstructorWithCustomCalendar(4, calendarSetup);
    }

    /**
     * If a calendar prototype was set, the the (int, Year) month-year constructor should use it.
     */
    @Test
    public void testMonthYearConstructorWithCalendarPrototype() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setCalendarInstancePrototype(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testMonthYearConstructorWithCustomCalendar(3, calendarSetup);
        testMonthYearConstructorWithCustomCalendar(4, calendarSetup);
    }

    private void testMonthYearConstructorWithCustomCalendar(int hoursOffset, Consumer<Integer> calendarSetup) {
        try {
            calendarSetup.accept(hoursOffset);
            Month m = new Month(1, new Year(1970));
            assertEquals(1970, m.getYear().getYear());
            assertEquals(1, m.getMonth());
            assertEquals(-3_600_000L * hoursOffset, m.getFirstMillisecond());
        } finally {
            // reset everything, to avoid affecting other tests
            RegularTimePeriod.setThreadLocalCalendarInstance(null);
            RegularTimePeriod.setCalendarInstancePrototype(null);
        }
    }

    /**
     * Set up a month equal to Jan 1900.  Request the previous month, it should
     * be null.
     */
    @Test
    public void testJan1900Previous() {
        Month previous = (Month) this.jan1900.previous();
        assertNull(previous);
    }

    /**
     * Set up a month equal to Jan 1900.  Request the next month, it should be
     * Feb 1900.
     */
    @Test
    public void testJan1900Next() {
        Month next = (Month) this.jan1900.next();
        assertEquals(this.feb1900, next);
    }

    /**
     * Set up a month equal to Dec 9999.  Request the previous month, it should
     * be Nov 9999.
     */
    @Test
    public void testDec9999Previous() {
        Month previous = (Month) this.dec9999.previous();
        assertEquals(this.nov9999, previous);
    }

    /**
     * Set up a month equal to Dec 9999.  Request the next month, it should be
     * null.
     */
    @Test
    public void testDec9999Next() {
        Month next = (Month) this.dec9999.next();
        assertNull(next);
    }

    /**
     * Tests the string parsing code...
     */
    @Test
    public void testParseMonth() {
        Month month = null;

        // test 1...
        try {
            month = Month.parseMonth("1990-01");
        }
        catch (TimePeriodFormatException e) {
            month = new Month(1, 1900);
        }
        assertEquals(1, month.getMonth());
        assertEquals(1990, month.getYear().getYear());

        // test 2...
        try {
            month = Month.parseMonth("02-1991");
        }
        catch (TimePeriodFormatException e) {
            month = new Month(1, 1900);
        }
        assertEquals(2, month.getMonth());
        assertEquals(1991, month.getYear().getYear());

        // test 3...
        try {
            String monthName = SerialDate.DATE_FORMAT_SYMBOLS.getMonths()[2];
            month = Month.parseMonth(monthName + " 1993");
        }
        catch (TimePeriodFormatException e) {
            month = new Month(1, 1900);
        }
        assertEquals(3, month.getMonth());
        assertEquals(1993, month.getYear().getYear());

    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        Month m1 = new Month(12, 1999);
        Month m2 = TestUtils.serialised(m1);
        assertEquals(m1, m2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashcode() {
        Month m1 = new Month(2, 2003);
        Month m2 = new Month(2, 2003);
        assertEquals(m1, m2);
        int h1 = m1.hashCode();
        int h2 = m2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * The {@link Month} class is immutable, so should not be {@link Cloneable}.
     */
    @Test
    public void testNotCloneable() {
        Month m = new Month(2, 2003);
        assertFalse(m instanceof Cloneable);
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
        Month m = new Month(3, 1970);
        assertEquals(5094000000L, m.getFirstMillisecond());
        Locale.setDefault(saved);
        TimeZone.setDefault(savedZone);
    }

    /**
     * Some checks for the getFirstMillisecond(TimeZone) method.
     */
    @Test
    public void testGetFirstMillisecondWithTimeZone() {
        Month m = new Month(2, 1950);
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(zone);
        assertEquals(-628444800000L, m.getFirstMillisecond(cal));

        // try null calendar
        boolean pass = false;
        try {
            m.getFirstMillisecond((Calendar) null);
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
        Month m = new Month(1, 2001);
        GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
        assertEquals(978307200000L, m.getFirstMillisecond(calendar));

        // try null calendar
        boolean pass = false;
        try {
            m.getFirstMillisecond((Calendar) null);
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
        Month m = new Month(3, 1970);
        assertEquals(7772399999L, m.getLastMillisecond());
        Locale.setDefault(saved);
        TimeZone.setDefault(savedZone);
    }

    /**
     * Some checks for the getLastMillisecond(TimeZone) method.
     */
    @Test
    public void testGetLastMillisecondWithTimeZone() {
        Month m = new Month(2, 1950);
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(zone);
        assertEquals(-626025600001L, m.getLastMillisecond(cal));

        // try null calendar
        boolean pass = false;
        try {
            m.getLastMillisecond((Calendar) null);
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
        Month m = new Month(3, 2001);
        GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
        assertEquals(986083199999L, m.getLastMillisecond(calendar));

        // try null calendar
        boolean pass = false;
        try {
            m.getLastMillisecond((Calendar) null);
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
        Month m = new Month(1, 2000);
        assertEquals(24001L, m.getSerialIndex());
        m = new Month(1, 1900);
        assertEquals(22801L, m.getSerialIndex());
    }

    @Test
    public void testNextPrevious() {
        Month m = new Month(12, 2000);
        m = (Month) m.next();
        assertEquals(new Year(2001), m.getYear());
        assertEquals(1, m.getMonth());
        m = (Month) m.previous();
        assertEquals(new Year(2000), m.getYear());
        assertEquals(12, m.getMonth());
        m = new Month(12, 9999);
        assertNull(m.next());
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
            Month m = new Month(new Date(ms));
            m = (Month) m.next();
            assertEquals(1970, m.getYear().getYear());
            assertEquals(2, m.getMonth());
            assertEquals(ms + 86_400_000L * 31, m.getFirstMillisecond());
            m = (Month) m.previous();
            assertEquals(1970, m.getYear().getYear());
            assertEquals(1, m.getMonth());
            assertEquals(ms, m.getFirstMillisecond());
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
        cal.set(2006, Calendar.MARCH, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Month m = new Month(3, 2006);
        assertEquals(cal.getTime(), m.getStart());
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
        cal.set(2006, Calendar.JANUARY, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Month m = new Month(1, 2006);
        assertEquals(cal.getTime(), m.getEnd());
        Locale.setDefault(saved);
    }

}
