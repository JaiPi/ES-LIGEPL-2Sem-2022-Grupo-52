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
 * --------------------
 * MillisecondTest.java
 * --------------------
 * (C) Copyright 2002-2022, by David Gilbert.
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

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Millisecond} class.
 */
public class MillisecondTest {

    /**
     * Check that a {@link Millisecond} instance is equal to itself.
     *
     * SourceForge Bug ID: 558850.
     */
    @Test
    public void testEqualsSelf() {
        Millisecond millisecond = new Millisecond();
        assertEquals(millisecond, millisecond);
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        Day day1 = new Day(29, MonthConstants.MARCH, 2002);
        Hour hour1 = new Hour(15, day1);
        Minute minute1 = new Minute(15, hour1);
        Second second1 = new Second(34, minute1);
        Millisecond milli1 = new Millisecond(999, second1);
        Day day2 = new Day(29, MonthConstants.MARCH, 2002);
        Hour hour2 = new Hour(15, day2);
        Minute minute2 = new Minute(15, hour2);
        Second second2 = new Second(34, minute2);
        Millisecond milli2 = new Millisecond(999, second2);
        assertEquals(milli1, milli2);
    }

    /**
     * In GMT, the 4.55:59.123pm on 21 Mar 2002 is
     * java.util.Date(1016729759123L).  Use this to check the Millisecond
     * constructor.
     */
    @Test
    public void testDateConstructor1() {
        TimeZone zone = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(zone);
        Locale locale = Locale.getDefault();  // locale should not matter here
        Millisecond m1 = new Millisecond(new Date(1016729759122L), zone,
                locale);
        Millisecond m2 = new Millisecond(new Date(1016729759123L), zone,
                locale);

        assertEquals(122, m1.getMillisecond());
        assertEquals(1016729759122L, m1.getLastMillisecond(cal));

        assertEquals(123, m2.getMillisecond());
        assertEquals(1016729759123L, m2.getFirstMillisecond(cal));
    }

    /**
     * In Tallinn, the 4.55:59.123pm on 21 Mar 2002 is
     * java.util.Date(1016722559123L).  Use this to check the Millisecond
     * constructor.
     */
    @Test
    public void testDateConstructor2() {
        TimeZone zone = TimeZone.getTimeZone("Europe/Tallinn");
        Calendar cal = Calendar.getInstance(zone);
        Locale locale = Locale.getDefault();  // locale should not matter here
        Millisecond m1 = new Millisecond(new Date(1016722559122L), zone,
                locale);
        Millisecond m2 = new Millisecond(new Date(1016722559123L), zone,
                locale);

        assertEquals(122, m1.getMillisecond());
        assertEquals(1016722559122L, m1.getLastMillisecond(cal));

        assertEquals(123, m2.getMillisecond());
        assertEquals(1016722559123L, m2.getFirstMillisecond(cal));
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
            Millisecond m = new Millisecond(new Date(0L));
            assertEquals(1970, m.getSecond().getMinute().getHour().getYear());
            assertEquals(1, m.getSecond().getMinute().getHour().getMonth());
            assertEquals(1, m.getSecond().getMinute().getHour().getDayOfMonth());
            assertEquals(hoursOffset, m.getSecond().getMinute().getHour().getHour());
            assertEquals(0, m.getSecond().getMinute().getMinute());
            assertEquals(0, m.getSecond().getSecond());
            assertEquals(0, m.getMillisecond());
            assertEquals(0L, m.getFirstMillisecond());
        } finally {
            // reset everything, to avoid affecting other tests
            RegularTimePeriod.setThreadLocalCalendarInstance(null);
            RegularTimePeriod.setCalendarInstancePrototype(null);
        }
    }

    /**
     * If a thread-local calendar was set, the millisecond-second constructor should use it.
     */
    @Test
    public void testMillisecondSecondConstructorWithThreadLocalCalendar() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setThreadLocalCalendarInstance(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testMillisecondSecondConstructorWithCustomCalendar(3, calendarSetup);
        testMillisecondSecondConstructorWithCustomCalendar(4, calendarSetup);
    }

    /**
     * If a calendar prototype was set, the millisecond-second constructor should use it.
     */
    @Test
    public void testMillisecondSecondConstructorWithCalendarPrototype() {
        Consumer<Integer> calendarSetup = hours -> RegularTimePeriod.setCalendarInstancePrototype(
                Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.ofHours(hours)))
        );
        testMillisecondSecondConstructorWithCustomCalendar(3, calendarSetup);
        testMillisecondSecondConstructorWithCustomCalendar(4, calendarSetup);
    }

    private void testMillisecondSecondConstructorWithCustomCalendar(int hoursOffset, Consumer<Integer> calendarSetup) {
        try {
            calendarSetup.accept(hoursOffset);
            Millisecond m = new Millisecond(0, new Second(new Date(0L)));
            assertEquals(1970, m.getSecond().getMinute().getHour().getYear());
            assertEquals(1, m.getSecond().getMinute().getHour().getMonth());
            assertEquals(1, m.getSecond().getMinute().getHour().getDayOfMonth());
            assertEquals(hoursOffset, m.getSecond().getMinute().getHour().getHour());
            assertEquals(0, m.getSecond().getMinute().getMinute());
            assertEquals(0, m.getSecond().getSecond());
            assertEquals(0, m.getMillisecond());
            assertEquals(0L, m.getFirstMillisecond());
        } finally {
            // reset everything, to avoid affecting other tests
            RegularTimePeriod.setThreadLocalCalendarInstance(null);
            RegularTimePeriod.setCalendarInstancePrototype(null);
        }
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        Millisecond m1 = new Millisecond();
        Millisecond m2 = TestUtils.serialised(m1);
        assertEquals(m1, m2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashcode() {
        Millisecond m1 = new Millisecond(599, 23, 45, 7, 9, 10, 2007);
        Millisecond m2 = new Millisecond(599, 23, 45, 7, 9, 10, 2007);
        assertEquals(m1, m2);
        int hash1 = m1.hashCode();
        int hash2 = m2.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * A test for bug report 943985 - the calculation for the middle
     * millisecond is incorrect for odd milliseconds.
     */
    @Test
    public void test943985() {
        Millisecond ms = new Millisecond(new java.util.Date(4));
        assertEquals(ms.getFirstMillisecond(), ms.getMiddleMillisecond());
        assertEquals(ms.getMiddleMillisecond(), ms.getLastMillisecond());
        ms = new Millisecond(new java.util.Date(5));
        assertEquals(ms.getFirstMillisecond(), ms.getMiddleMillisecond());
        assertEquals(ms.getMiddleMillisecond(), ms.getLastMillisecond());
    }

    /**
     * The {@link Millisecond} class is immutable, so should not be
     * {@link Cloneable}.
     */
    @Test
    public void testNotCloneable() {
        Millisecond m = new Millisecond(599, 23, 45, 7, 9, 10, 2007);
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
        Millisecond m = new Millisecond(500, 15, 43, 15, 1, 4, 2006);
        assertEquals(1143902595500L, m.getFirstMillisecond());
        Locale.setDefault(saved);
        TimeZone.setDefault(savedZone);
    }

    /**
     * Some checks for the getFirstMillisecond(TimeZone) method.
     */
    @Test
    public void testGetFirstMillisecondWithTimeZone() {
        Millisecond m = new Millisecond(500, 50, 59, 15, 1, 4, 1950);
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(zone);
        assertEquals(-623289609500L, m.getFirstMillisecond(cal));

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
        Millisecond m = new Millisecond(500, 55, 40, 2, 15, 4, 2000);
        GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
        assertEquals(955766455500L, m.getFirstMillisecond(calendar));

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
        Millisecond m = new Millisecond(750, 1, 1, 1, 1, 1, 1970);
        assertEquals(61750L, m.getLastMillisecond());
        Locale.setDefault(saved);
        TimeZone.setDefault(savedZone);
    }

    /**
     * Some checks for the getLastMillisecond(TimeZone) method.
     */
    @Test
    public void testGetLastMillisecondWithTimeZone() {
        Millisecond m = new Millisecond(750, 55, 1, 2, 7, 7, 1950);
        TimeZone zone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(zone);
        assertEquals(-614962684250L, m.getLastMillisecond(cal));

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
        Millisecond m = new Millisecond(250, 50, 45, 21, 21, 4, 2001);
        GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Frankfurt"));
        assertEquals(987889550250L, m.getLastMillisecond(calendar));

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
        Millisecond m = new Millisecond(500, 1, 1, 1, 1, 1, 2000);
        assertEquals(3155850061500L, m.getSerialIndex());
        m = new Millisecond(500, 1, 1, 1, 1, 1, 1900);
        // TODO: this must be wrong...
        assertEquals(176461500L, m.getSerialIndex());
    }

    @Test
    public void testNextPrevious() {
        Millisecond m = new Millisecond(555, 55, 30, 1, 12, 12, 2000);
        m = (Millisecond) m.next();
        assertEquals(2000, m.getSecond().getMinute().getHour().getYear());
        assertEquals(12, m.getSecond().getMinute().getHour().getMonth());
        assertEquals(12, m.getSecond().getMinute().getHour().getDayOfMonth());
        assertEquals(1, m.getSecond().getMinute().getHour().getHour());
        assertEquals(30, m.getSecond().getMinute().getMinute());
        assertEquals(55, m.getSecond().getSecond());
        assertEquals(556, m.getMillisecond());
        m = (Millisecond) m.previous();
        assertEquals(2000, m.getSecond().getMinute().getHour().getYear());
        assertEquals(12, m.getSecond().getMinute().getHour().getMonth());
        assertEquals(12, m.getSecond().getMinute().getHour().getDayOfMonth());
        assertEquals(1, m.getSecond().getMinute().getHour().getHour());
        assertEquals(30, m.getSecond().getMinute().getMinute());
        assertEquals(55, m.getSecond().getSecond());
        assertEquals(555, m.getMillisecond());
        m = new Millisecond(999, 59, 59, 23, 31, 12, 9999);
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
            Millisecond m = new Millisecond(new Date(0L));
            m = (Millisecond) m.next();
            assertEquals(1970, m.getSecond().getMinute().getHour().getYear());
            assertEquals(1, m.getSecond().getMinute().getHour().getMonth());
            assertEquals(1, m.getSecond().getMinute().getHour().getDayOfMonth());
            assertEquals(hoursOffset, m.getSecond().getMinute().getHour().getHour());
            assertEquals(0, m.getSecond().getMinute().getMinute());
            assertEquals(0, m.getSecond().getSecond());
            assertEquals(1L, m.getMillisecond());
            assertEquals(1L, m.getFirstMillisecond());
            m = (Millisecond) m.previous();
            assertEquals(1970, m.getSecond().getMinute().getHour().getYear());
            assertEquals(1, m.getSecond().getMinute().getHour().getMonth());
            assertEquals(1, m.getSecond().getMinute().getHour().getDayOfMonth());
            assertEquals(hoursOffset, m.getSecond().getMinute().getHour().getHour());
            assertEquals(0, m.getSecond().getMinute().getMinute());
            assertEquals(0, m.getSecond().getSecond());
            assertEquals(0L, m.getMillisecond());
            assertEquals(0L, m.getFirstMillisecond());
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
        cal.set(2006, Calendar.JANUARY, 16, 3, 47, 55);
        cal.set(Calendar.MILLISECOND, 555);
        Millisecond m = new Millisecond(555, 55, 47, 3, 16, 1, 2006);
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
        cal.set(2006, Calendar.JANUARY, 16, 3, 47, 55);
        cal.set(Calendar.MILLISECOND, 555);
        Millisecond m = new Millisecond(555, 55, 47, 3, 16, 1, 2006);
        assertEquals(cal.getTime(), m.getEnd());
        Locale.setDefault(saved);
    }

}
