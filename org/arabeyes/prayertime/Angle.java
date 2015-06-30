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

import java.lang.Math;

public class Angle {
    public static final int INVALID_TRIGGER = 1;
    public static final double CENTER_OF_SUN_ANGLE = -0.833370;    /* ..of sun's upper limb angle */
    public static final double EARTH_RADIUS = 6378140.0;         /* Equatorial radius in meters */
    //public static final double ALTITUDE_REFRACTION = 0.0347;
    public static final double DEG_TO_10_BASE = 1/15.0;

    private static final double KAABA_LAT = 21.423333;
    private static final double KAABA_LONG = 39.823333;

    public int deg;
    public int min;
    public double sec;

    public Angle(int d, int m, double s)
    {
        deg = d;
        min = m;
        sec = s;
    }

    public Angle(double decimal)
    {
        decimal2Dms(decimal);
    }

    public double dms2Decimal(char dir)
    {
        return dms2Decimal(deg, min, sec, dir);
    }

    public static double dms2Decimal (int deg, int min, double sec, char dir)
    {
        double sum = deg + ((min/60.0)+(sec/3600.0));

        if (dir == 'S' || dir == 'W' || dir == 's' || dir == 'w')
            return sum * (-1.0);
        return sum;
    } 
        
    public void decimal2Dms (double decimal)
    {
        double tempmin, tempsec, n1, n2;
        
        //tempmin = modf(decimal, &n1) * 60.0;
        n1 = (int)decimal;
        tempmin = (decimal - n1) * 60.0;

        //tempsec = modf(tempmin, &n2) * 60.0;
        n2 = (int)tempmin;
        tempsec = (tempmin - n2) * 60.0;

        deg = (int)n1;
        min = (int)n2;
        sec = tempsec;
    }

    public static double DEG_TO_RAD(double A)
    {
        return (A) * (Math.PI/180.0);
    }
    
    public static double RAD_TO_DEG(double A)
    {
        return (A) / (Math.PI/180.0);
    }

    /* Obtaining the direction of the shortest distance towards Qibla by using the
     * great circle formula */ 
    public static double getNorthQibla(PTLocation loc)
    {
        /* FIXIT: reduce DEG_TO_RAD usage */
        double num, denom;

        num = Math.sin(DEG_TO_RAD(loc.degreeLong) - DEG_TO_RAD(KAABA_LONG));
        denom = (Math.cos(DEG_TO_RAD(loc.degreeLat)) * 
                    Math.tan(DEG_TO_RAD(KAABA_LAT))) -
                (Math.sin(DEG_TO_RAD(loc.degreeLat)) * 
                    ((Math.cos((DEG_TO_RAD(loc.degreeLong) -
                        DEG_TO_RAD(KAABA_LONG))))));
        return RAD_TO_DEG (Math.atan2(num, denom));
    }
}
