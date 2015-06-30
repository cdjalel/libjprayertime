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

/* This holds the location info. */
public class PTLocation {
    public double degreeLat;   /* Latitude in decimal degree. */
    public double degreeLong;  /* Longitude in decimal degree. */
    public double gmtDiff;     /* GMT difference at regular time. */
    public int dst;            /* Daylight savings time switch (0 if not used).
                           Setting this to 1 should add 1 hour to all the
                           calculated prayer times */
    public double seaLevel;    /* Height above Sea level in meters */
    public double pressure;    /* Atmospheric pressure in millibars (the
                           astronomical standard value is 1010) */
    public double temperature; /* Temperature in Celsius degree (the astronomical
                           standard value is 10) */

    public PTLocation(double dLa, double dLo, double gmt, int dst, double seaL,
                      double pressure, double temp)
    {
        this.degreeLat = dLa;
        this.degreeLong = dLo;
        this.gmtDiff = gmt;
        this.dst = dst;
        this.seaLevel = seaL;
        this.pressure = pressure;
        this.temperature = temp;
    }

    public PTLocation(PTLocation loc)
    {
        this.degreeLong = loc.degreeLong;
        this.degreeLat = loc.degreeLat;
        this.gmtDiff = loc.gmtDiff;
        this.dst = loc.dst;
        this.seaLevel = loc.seaLevel;
        this.pressure = loc.pressure;
        this.temperature = loc.temperature;
    }
}
