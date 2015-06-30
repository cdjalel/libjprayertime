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

import java.util.Arrays;

public class AstroValues {
    private static final int NB_DAYS = 3;

    public double jd;      /* Astronomical Julian day (for local time with Delta-t) */
    public double[] dec;  /* Declination */
    public double[] ra;   /* Right Ascensions */
    public double[] sid;  /* Apparent sidereal time */
    public double[] dra;  /* Delta Right Ascensions */
    public double[] rsum; /* Sum of periodic values for radius vector R */

    public AstroValues()
    {
        dec = new double[NB_DAYS];
        ra = new double[NB_DAYS];
        sid = new double[NB_DAYS];
        dra = new double[NB_DAYS];
        rsum = new double[NB_DAYS];
    }

    public AstroValues(AstroValues v)
    {   
        this.jd = v.jd;
        this.dec = Arrays.copyOf(v.dec, v.dec.length);
        this.ra = Arrays.copyOf(v.ra, v.ra.length);
        this.sid = Arrays.copyOf(v.sid, v.sid.length);
        this.dra = Arrays.copyOf(v.dra, v.dra.length);
        this.rsum = Arrays.copyOf(v.rsum, v.rsum.length);
    }
}
