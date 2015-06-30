/************************************************************************
 * ------------
 * Description:
 * ------------
 *  Copyright (c) 2003-2006, 2009 Arabeyes, Thamer Mahmoud
 *  Copyright (c) 2015, Djalel Chefrour, porting to Java.
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

import java.lang.Math;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import org.arabeyes.prayertime.*;


public class DemoPrayerTime {

    public static void main(String [ ] args)
    {

        int i;
        String cityName = "Souk Ahras";
        double qibla;

        PTLocation loc = new PTLocation(36.28639, 7.95111, 1, 0, 697, 1010, 10); 
        // http://dateandtime.info/citycoordinates.php?id=2479215

        Method conf = new Method();
        conf.setMethod(Method.MUSLIM_LEAGUE);
        conf.round = 0;
        
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        /* Call the main function to fill the Prayer times */
        Prayer prayer = new Prayer();
        PrayerTime[] ptList = prayer.getPrayerTimes(loc, conf, date);

        /* Call functions for other prayer times and qibla */
        PrayerTime imsaak = prayer.getImsaak(loc, conf, date);
        PrayerTime nextFajr = prayer.getNextDayFajr(loc, conf, date);
        PrayerTime nextImsaak = prayer.getNextDayImsaak(loc, conf, date);
        qibla = Angle.getNorthQibla(loc);

        /* Show the results */
        System.out.printf ("\nPrayer schedule for: %s on %s\n", cityName, 
                (new SimpleDateFormat("dd-MMM-yyyy")).format(date));

        Angle angle = new Angle(loc.degreeLat);
        System.out.printf("\nLatitude\t=  %d° %2d\' %4.1f\" %c", 
                Math.abs(angle.deg), Math.abs(angle.min), Math.abs(angle.sec),
                (loc.degreeLat>=0) ? 'N' : 'S');

        angle.decimal2Dms(loc.degreeLong);
        System.out.printf ("\nLongitude\t=  %d° %d\' %4.1f\" %c",
                Math.abs(angle.deg), Math.abs(angle.min), Math.abs(angle.sec),
                (loc.degreeLong>=0) ? 'E' : 'W');

        angle.decimal2Dms(qibla);
        System.out.printf("\nQibla\t\t=  %d° %d\' %4.1f\" %c of true North\n",
                Math.abs(angle.deg), Math.abs(angle.min), Math.abs(angle.sec),
                (qibla>=0) ? 'W' : 'E');

        System.out.printf ("\n\n");

        for (i = 0; i < Prayer.NB_PRAYERS; i++)
            System.out.printf (" %3d:%02d:%02d%c",
                    ptList[i].hour, ptList[i].minute, ptList[i].second,
                    (ptList[i].isExtreme != 0) ? '*' : ' ' );

        System.out.printf ("\n\n");
        System.out.printf("Tomorrow's Fajr:\t%3d:%02d\n", nextFajr.hour, nextFajr.minute);
        System.out.printf("Tomorrow's Imsaak:\t%3d:%02d\n", nextImsaak.hour, nextImsaak.minute);
        System.out.printf("Today's Imsaak:\t\t%3d:%02d\n\n", imsaak.hour, imsaak.minute);
    }
}

