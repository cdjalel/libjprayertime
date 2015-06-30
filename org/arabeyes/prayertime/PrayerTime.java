/************************************************************************
 * ------------
 * Description:
 * ------------
 *  Copyright (c) 2003-2006, 2009 Arabeyes, Thamer Mahmoud
 *  Copyright (c) 2015, Djalel Chefrour conversion to Java.
 *
 *  A full featured Muslim Prayer Times calculator
 *
 *  Most of the astronomical values and formulas used in this file are based
 *  upon a subset of the VSOP87 planetary theory developed by Jean Meeus. Page
 *  and formula numbers in-line below are references to his book: Astronomical
 *  Algorithms. Willmann-Bell, second edition, 1998.
 *
 * (www.arabeyes.org - under LGPL license - see COPYING file)
 ************************************************************************/

package org.arabeyes.prayertime;

/* This class holds the prayer time output for a single prayer. */
public class PrayerTime {
    public int hour;       /* prayer time hour */
    public int minute;     /* prayer time minute */
    public int second;     /* prayer time second */
    public int isExtreme;  /* Extreme calculation status. The 'getPrayerTimes'
                       function sets this variable to 1 to indicate that
                       this particular prayer time has been calculated
                       through extreme latitude methods and NOT by
                       conventional means of calculation. */
    public PrayerTime()
    {
        hour = 0;
        minute = 0;
        second = 0;
        isExtreme = 0;
    }

    public PrayerTime(int h, int m, int s)
    {
        hour = h;
        minute = m;
        second = s;
        isExtreme = 0;
    }

    public PrayerTime(PrayerTime pt)
    {
        this.hour = pt.hour;
        this.minute = pt.minute;
        this.second = pt.second;
        this.isExtreme = pt.isExtreme;
    }
}
