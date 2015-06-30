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

public class Method {
    private static final double DEF_NEAREST_LATITUDE = 48.5;
    private static final double DEF_IMSAAK_ANGLE = 1.5;

    // originally enum methods in .c 
    public static final int NONE = 0;
    public static final int EGYPT_SURVEY = 1;
    public static final int KARACHI_SHAF = 2;
    public static final int KARACHI_HANAF = 3;
    public static final int NORTH_AMERICA = 4;
    public static final int MUSLIM_LEAGUE = 5;
    public static final int UMM_ALQURRA = 6;
    public static final int FIXED_ISHAA = 7;
    public static final int EGYPT_NEW = 8;

    public double fajrAng;     /* Fajr angle */
    public double ishaaAng;    /* Ishaa angle */
    public double imsaakAng;   /* The angle difference between Imsaak and Fajr (
                           default is 1.5)*/
    public int fajrInv;        /* Fajr Interval is the amount of minutes between
                           Fajr and Shurooq (0 if not used) */
    public int ishaaInv;       /* Ishaa Interval is the amount if minutes between
                           Ishaa and Maghrib (0 if not used) */
    public int imsaakInv;      /* Imsaak Interval is the amount of minutes between
                           Imsaak and Fajr. The default is 10 minutes before
                           Fajr if Fajr Interval is set */
    public int round;          /* Method used for rounding seconds:
                           0: No Rounding. "Prayer.seconds" is set to the
                              amount of computed seconds.
                           1: Normal Rounding. If seconds are equal to
                              30 or above, add 1 minute. Sets
                              "Prayer.seconds" to zero.
                           2: Special Rounding. Similar to normal rounding
                              but we always round down for Shurooq and
                              Imsaak times. (default)
                           3: Aggressive Rounding. Similar to Special
                              Rounding but we add 1 minute if the seconds
                              value is equal to 1 second or more.  */
    public int mathhab;        /* Assr prayer shadow ratio:
                           1: Shaf'i (default)
                           2: Hanafi */
    public double nearestLat;  /* Latitude Used for the 'Nearest Latitude' extreme
                           methods. The default is 48.5 */
    public int extreme;        /* Extreme latitude calculation method  */
    /* 
       Supported methods for Extreme Latitude calculations (Method.extreme) -
       (see the file "./doc/method-info.txt" for details) :
      
       0:  none. if unable to calculate, leave as 99:99
       1:  Nearest Latitude: All prayers Always
       2:  Nearest Latitude: Fajr Ishaa Always
       3:  Nearest Latitude: Fajr Ishaa if invalid
       4:  Nearest Good Day: All prayers Always
       5:  Nearest Good Day: Fajr Ishaa if invalid (default)
       6:  1/7th of Night: Fajr Ishaa Always
       7:  1/7th of Night: Fajr Ishaa if invalid
       8:  1/7th of Day: Fajr Ishaa Always
       9:  1/7th of Day: Fajr Ishaa if invalid
       10: Half of the Night: Fajr Ishaa Always
       11: Half of the Night: Fajr Ishaa if invalid
       12: Minutes from Shorooq/Maghrib: Fajr Ishaa Always (e.g. Maghrib=Ishaa)
       13: Minutes from Shorooq/Maghrib: Fajr Ishaa If invalid
    */

    public int offset;         /* Enable Offsets switch (set this to 1 to
                           activate). This option allows you to add or
                           subtract any amount of minutes to the daily
                           computed prayer times based on values (in
                           minutes) for each prayer in the offList array */     
    public double[] offList;  /* For Example: If you want to add 30 seconds to
                           Maghrib and subtract 2 minutes from Ishaa:
                                offset = 1
                                offList[4] = 0.5
                                offList[5] = -2
                           ..and than call getPrayerTimes as usual. */

    public Method()
    {
        offList = new double[6];
        setMethod(0);
    }   

    public Method(Method m)
    {   
        this.fajrAng = m.fajrAng;
        this.ishaaAng = m.ishaaAng;
        this.imsaakAng = m.imsaakAng;
        this.fajrInv = m.fajrInv;
        this.ishaaInv = m.ishaaInv;
        this.imsaakInv = m.imsaakInv;
        this.round = m.round;
        this.mathhab = m.mathhab;
        this.nearestLat = m.nearestLat;
        this.extreme = m.extreme;
        this.offset = m.offset;
        this.offList = Arrays.copyOf(m.offList, m.offList.length);
    }

    /* This function is used to auto fill the Method structure with predefined
       data. The supported auto-fill methods for calculations at normal
       circumstances are:
    
      0: none. Set to default or 0
      1: Egyptian General Authority of Survey
      2: University of Islamic Sciences, Karachi (Shaf'i)
      3: University of Islamic Sciences, Karachi (Hanafi)
      4: Islamic Society of North America
      5: Muslim World League (MWL)
      6: Umm Al-Qurra, Saudi Arabia
      7: Fixed Ishaa Interval (always 90)
      8: Egyptian General Authority of Survey (Egypt)
      (see the file "./doc/method-info.txt" for more details)
    */
    public void setMethod(int n)
    {

        int i;
        fajrInv = 0; 
        ishaaInv = 0; 
        imsaakInv = 0;
        mathhab = 1;
        round = 2;
        nearestLat = DEF_NEAREST_LATITUDE;
        imsaakAng = DEF_IMSAAK_ANGLE;
        extreme = 5;
        offset = 0;
        for (i = 0; i < 6; i++) {
            offList[i] = 0; 
        }

        switch(n)
        {
            case NONE:
                fajrAng = 0.0;
                ishaaAng = 0.0;
                break;

            case EGYPT_SURVEY:
                fajrAng = 20;
                ishaaAng = 18;
                break;

            case KARACHI_SHAF:
                fajrAng = 18;
                ishaaAng = 18;
                break;

            case KARACHI_HANAF: 
                fajrAng = 18;
                ishaaAng = 18;
                mathhab = 2;
                break;

            case NORTH_AMERICA:
                fajrAng = 15;
                ishaaAng = 15;
                break;

            case MUSLIM_LEAGUE: 
                fajrAng = 18;
                ishaaAng = 17;
                break;

            case UMM_ALQURRA: 
                fajrAng = 19;
                ishaaAng = 0.0;
                ishaaInv = 90;
                break;

            case FIXED_ISHAA:
                fajrAng = 19.5;
                ishaaAng = 0.0;
                ishaaInv = 90;
                break;

            case EGYPT_NEW:
                fajrAng = 19.5;
                ishaaAng = 17.5;
                break;
        }
    }
}





