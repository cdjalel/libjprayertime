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
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Prayer {
    public static final int NB_PRAYERS = 6;

    private static final double DEF_IMSAAK_INTERVAL = 10;
    private static final double DEF_ROUND_SEC = 30 ;
    private static final double AGGRESSIVE_ROUND_SEC = 1;

    // originally enum exmethods .c
    private static final int NONE_EX = 0;
    private static final int LAT_ALL = 1;
    private static final int LAT_ALWAYS = 2;
    private static final int LAT_INVALID = 3;
    private static final int GOOD_ALL = 4;
    private static final int GOOD_INVALID = 5;
    private static final int SEVEN_NIGHT_ALWAYS = 6;
    private static final int SEVEN_NIGHT_INVALID = 7;
    private static final int SEVEN_DAY_ALWAYS = 8;
    private static final int SEVEN_DAY_INVALID = 9;
    private static final int HALF_ALWAYS = 10;
    private static final int HALF_INVALID = 11;
    private static final int MIN_ALWAYS = 12;
    private static final int MIN_INVALID = 13;
    private static final int GOOD_INVALID_SAME = 14;
    // originally enum salatType in .c 
    private static final int FAJR = 0;
    private static final int SHUROOQ = 1;
    private static final int ZUHR = 2;
    private static final int ASSR = 3;
    private static final int MAGHRIB = 4;
    private static final int ISHAA = 5;
    private static final int IMSAAK = 6;
    private static final int NEXTFAJR = 7;

    private int lastDayOfYear;
    private double julianDay;
    private AstroValues astroCache;


    private void getDayInfo(Date date, double gmt)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        lastDayOfYear = cal.get(Calendar.DAY_OF_YEAR);

        julianDay = AstroDay.getJulianDay(date, gmt);
    }

    private PrayerTime[] getPrayerTimesByDay(PTLocation loc, Method conf, int type)
    {
        int i, invalid;
        double zu, sh, mg, fj, is, ar;
        double lat, lon, dec;
        double[] tempPrayer = new double[NB_PRAYERS];
        PrayerTime[] pt = new PrayerTime[NB_PRAYERS];
        AstroValues tAstro = new AstroValues();
        AstroDay astroDay = new AstroDay();     // TODO null?

        lat = loc.degreeLat; 
        lon = loc.degreeLong;
        invalid = 0;

        /* Start by filling the tAstro structure with the appropriate astronomical
         * values for this day. We also pass the cache structure to update and check
         * if the actual values are already available. */
        astroDay.getAstroValuesByDay(julianDay, loc, astroCache, tAstro);
        dec = Angle.DEG_TO_RAD(tAstro.dec[1]);

        /* Get Prayer Times formulae results for this day of year and this
         * location. The results are NOT the actual prayer times */
        fj   = getFajIsh (lat, dec, conf.fajrAng);
        sh   = astroDay.getSunrise(loc, tAstro);
        zu   = getZuhr (lon, tAstro);
        ar   = getAssr (lat, dec, conf.mathhab);
        mg   = astroDay.getSunset(loc, tAstro);
        is   = getFajIsh (lat, dec, conf.ishaaAng);

        /* Calculate all prayer times as Base-10 numbers in Normal circumstances */ 
        /* Fajr */   
        if (fj == 99) {
            tempPrayer[0] = 99;
            invalid = 1;
        } 
        else tempPrayer[0] = zu - fj;

        if (sh == 99)
            invalid = 1;
        tempPrayer[1] = sh;

        tempPrayer[2] = zu;

        /* Assr */
        if (ar == 99) {
            tempPrayer[3] = 99;
            invalid = 1;
        } 
        else tempPrayer[3] = zu + ar;


        if (mg == 99)
            invalid = 1;
        tempPrayer[4] = mg;


        /* Ishaa */
        if (is == 99) {
            tempPrayer[5] = 99;
            invalid = 1;
        } 
        else tempPrayer[5] = zu + is;


        /* Calculate all prayer times as Base-10 numbers in Extreme Latitudes (if
         * needed) */

        /* Reset status of extreme switches */
        for (i=0; i<NB_PRAYERS; i++) {
            pt[i] = new PrayerTime();
        }

        if ((conf.extreme != NONE_EX) && !((conf.extreme == GOOD_INVALID || 
                        conf.extreme == LAT_INVALID ||
                        conf.extreme == SEVEN_NIGHT_INVALID ||
                        conf.extreme == SEVEN_DAY_INVALID ||
                        conf.extreme == HALF_INVALID) &&
                    (invalid == 0)))
        {
            double exdecPrev, exdecNext, degnLat;
            double exZu=99, exFj=99, exIs=99, exAr=99, exIm=99, exSh=99, exMg=99;
            double portion = 0;
            double nGoodDay = 0;
            int exinterval = 0;
            PTLocation exLoc = new PTLocation(loc);     // TODO null?
            AstroValues exAstroPrev;
            AstroValues exAstroNext;

            switch(conf.extreme)
            {
                /* Nearest Latitude (Method.nearestLat) */
                case LAT_ALL:
                case LAT_ALWAYS:
                case LAT_INVALID:

                    /* FIXIT: we cannot compute this when interval is set because
                     * angle==0 . Only the if-invalid methods would work */
                    exLoc.degreeLat = conf.nearestLat;
                    exFj = getFajIsh(conf.nearestLat, dec, conf.fajrAng);
                    exIm = getFajIsh(conf.nearestLat, dec, conf.imsaakAng);
                    exSh = astroDay.getSunrise(exLoc, tAstro);
                    exAr = getAssr(conf.nearestLat, dec, conf.mathhab);
                    exMg = astroDay.getSunset(exLoc, tAstro);
                    exIs = getFajIsh(conf.nearestLat, dec, conf.ishaaAng);


                    switch(conf.extreme)
                    {
                        case LAT_ALL:
                            tempPrayer[0] = zu - exFj;
                            tempPrayer[1] = exSh;
                            tempPrayer[3] = zu + exAr;
                            tempPrayer[4] = exMg;
                            tempPrayer[5] = zu + exIs;
                            pt[0].isExtreme = 1;
                            pt[1].isExtreme = 1;
                            pt[2].isExtreme = 1;
                            pt[3].isExtreme = 1;
                            pt[4].isExtreme = 1;
                            pt[5].isExtreme = 1;
                            break;

                        case LAT_ALWAYS:
                            tempPrayer[0] = zu - exFj;
                            tempPrayer[5] = zu + exIs;
                            pt[0].isExtreme = 1;
                            pt[5].isExtreme = 1;
                            break;

                        case LAT_INVALID:
                            if (tempPrayer[0] == 99) {
                                tempPrayer[0] = zu - exFj;
                                pt[0].isExtreme = 1;
                            }
                            if (tempPrayer[5] == 99) {
                                tempPrayer[5] = zu + exIs;
                                pt[5].isExtreme = 1;
                            }
                            break;
                    }
                    break;


                    /* Nearest Good Day */
                case GOOD_ALL:
                case GOOD_INVALID:
                case GOOD_INVALID_SAME:

                    exAstroPrev = new AstroValues(astroCache);
                    exAstroNext = new AstroValues(astroCache);  // TODO null?

                    /* Start by getting last or next nearest Good Day */
                    for(i=0; i <= lastDayOfYear; i++)
                    {

                        /* Last closest day */
                        nGoodDay = julianDay - i;
                        astroDay.getAstroValuesByDay(nGoodDay, loc, exAstroPrev, tAstro);
                        exdecPrev = Angle.DEG_TO_RAD(tAstro.dec[1]);
                        exFj = getFajIsh(lat, exdecPrev, conf.fajrAng);
                        if (exFj != 99)
                        {
                            exIs = getFajIsh(lat, exdecPrev, conf.ishaaAng);
                            if (exIs != 99)
                            {
                                exZu = getZuhr(lon, tAstro);
                                exSh = astroDay.getSunrise(loc, tAstro);
                                exAr = getAssr(lat, exdecPrev, conf.mathhab);
                                exMg = astroDay.getSunset(loc, tAstro);
                                break;
                            }
                        }

                        /* Next closest day */
                        nGoodDay = julianDay + i;
                        astroDay.getAstroValuesByDay(nGoodDay, loc, exAstroNext, tAstro);
                        exdecNext = Angle.DEG_TO_RAD(tAstro.dec[1]);
                        exFj = getFajIsh(lat, exdecNext, conf.fajrAng);
                        if (exFj != 99)
                        {
                            exIs = getFajIsh(lat, exdecNext, conf.ishaaAng);
                            if (exIs != 99)
                            {
                                exZu = getZuhr(lon, tAstro);
                                exSh = astroDay.getSunrise(loc, tAstro);
                                exAr = getAssr (lat, exdecNext, conf.mathhab);
                                exMg = astroDay.getSunset(loc, tAstro);
                                break;
                            }
                        }
                    }

                    switch(conf.extreme)
                    {
                        case GOOD_ALL:
                            tempPrayer[0] = exZu - exFj;
                            tempPrayer[1] = exSh;
                            tempPrayer[2] = exZu;
                            tempPrayer[3] = exZu + exAr;
                            tempPrayer[4] = exMg;
                            tempPrayer[5] = exZu + exIs;
                            for (i=0; i<NB_PRAYERS; i++)
                                pt[i].isExtreme = 1;
                            break;
                        case GOOD_INVALID:
                            if (tempPrayer[0] == 99) {
                                tempPrayer[0] = exZu - exFj;
                                pt[0].isExtreme = 1;
                            }
                            if (tempPrayer[5] == 99) {
                                tempPrayer[5] = exZu + exIs;
                                pt[5].isExtreme = 1;
                            }
                            break;
                        case GOOD_INVALID_SAME:
                            if ((tempPrayer[0] == 99) || (tempPrayer[5] == 99))
                            {
                                tempPrayer[0] = exZu - exFj;
                                pt[0].isExtreme = 1;
                                tempPrayer[5] = exZu + exIs;
                                pt[5].isExtreme = 1;
                            }
                            break;
                    }
                    break;

                case SEVEN_NIGHT_ALWAYS:
                case SEVEN_NIGHT_INVALID:
                case SEVEN_DAY_ALWAYS:
                case SEVEN_DAY_INVALID:
                case HALF_ALWAYS:
                case HALF_INVALID:

                    /* FIXIT: For clarity, we may need to move the HALF_* methods
                     * into their own separate case statement. */    
                    switch(conf.extreme)
                    {
                        case SEVEN_NIGHT_ALWAYS:
                        case SEVEN_NIGHT_INVALID:
                            portion = (24 - (tempPrayer[4] - tempPrayer[1])) * (1/7.0);
                            break;
                        case SEVEN_DAY_ALWAYS:
                        case SEVEN_DAY_INVALID:
                            portion = (tempPrayer[4] - tempPrayer[1]) * (1/7.0);
                            break;
                        case HALF_ALWAYS:
                        case HALF_INVALID:
                            portion = (24 - tempPrayer[4] - tempPrayer[1]) * (1/2.0);
                            break;
                    }


                    if (conf.extreme == SEVEN_NIGHT_INVALID ||
                            conf.extreme == SEVEN_DAY_INVALID ||
                            conf.extreme == HALF_INVALID)
                    {
                        if (tempPrayer[0] == 99) {
                            if (conf.extreme == HALF_INVALID)
                                tempPrayer[0] =  portion - (conf.fajrInv / 60.0);
                            else tempPrayer[0] = tempPrayer[1] - portion;
                            pt[0].isExtreme = 1;
                        }
                        if (tempPrayer[5] == 99) {
                            if  (conf.extreme == HALF_INVALID)
                                tempPrayer[5] = portion + (conf.ishaaInv / 60.0) ;
                            else tempPrayer[5] = tempPrayer[4] + portion;
                            pt[5].isExtreme = 1;
                        }
                    } else { /* for the always methods */

                        if (conf.extreme == HALF_ALWAYS) {
                            tempPrayer[0] = portion - (conf.fajrInv / 60.0);
                            tempPrayer[5] = portion + (conf.ishaaInv / 60.0) ;
                        }

                        else {
                            tempPrayer[0] = tempPrayer[1] - portion;
                            tempPrayer[5] = tempPrayer[4] + portion;
                        }
                        pt[0].isExtreme = 1;
                        pt[5].isExtreme = 1;
                    }
                    break;

                case MIN_ALWAYS:
                    /* Do nothing here because this is implemented through fajrInv and
                     * ishaaInv structure members */
                    tempPrayer[0] = tempPrayer[1];
                    tempPrayer[5] = tempPrayer[4];
                    pt[0].isExtreme = 1;
                    pt[5].isExtreme = 1;
                    break;

                case MIN_INVALID:
                    if (tempPrayer[0] == 99) {
                        exinterval = (int)(conf.fajrInv / 60.0);
                        tempPrayer[0] = tempPrayer[1] - exinterval;
                        pt[0].isExtreme = 1;
                    }
                    if (tempPrayer[5] == 99) {
                        exinterval = (int)(conf.ishaaInv / 60.0);
                        tempPrayer[5] = tempPrayer[4] + exinterval;
                        pt[5].isExtreme = 1;
                    }
                    break;
            } /* end switch */
        } /* end extreme */

        /* Apply intervals if set */
        if (conf.extreme != MIN_INVALID && 
                conf.extreme != HALF_INVALID &&
                conf.extreme != HALF_ALWAYS) 
        {
            if (conf.fajrInv != 0) {
                if (tempPrayer[1] != 99)
                    tempPrayer[0] = tempPrayer[1] - (conf.fajrInv / 60.0);
                else tempPrayer[0] = 99;
            }

            if (conf.ishaaInv != 0) {
                if (tempPrayer[4] != 99)
                    tempPrayer[5] = tempPrayer[4] + (conf.ishaaInv / 60.0);
                else tempPrayer[5] = 99;
            }
        }

        /* Final Step: Fill the Prayer array by doing decimal degree to
         * Prayer structure conversion */
        if (type == IMSAAK || type == NEXTFAJR) 
            base6hm(tempPrayer[0], loc, conf, pt[0], type);
        else {
            for (i=0; i<NB_PRAYERS; i++) 
                base6hm(tempPrayer[i], loc, conf, pt[i], i);
        }

        return pt;
    }

    private void base6hm(double bs, PTLocation loc, Method conf, PrayerTime pt, int type)
    {
        double min, sec;

        /* Set to 99 and return if prayer is invalid */
        if (bs == 99) {
            pt.hour = 99;
            pt.minute = 99;
            pt.second = 0;
            return;
        }

        /* Add offsets */
        if (conf.offset == 1) {
            if (type == IMSAAK || type == NEXTFAJR)
                bs += (conf.offList[0] / 60.0);
            else  bs += (conf.offList[type] / 60.0);
        }

        /* Fix after minus offsets before midnight */
        if (bs < 0) {
            while (bs < 0)
                bs = 24 + bs;
        }

        min = (bs - Math.floor(bs)) * 60;
        sec = (min - Math.floor(min)) * 60;

        /* Add rounding minutes */
        if (conf.round == 1)
        {
            if (sec >= DEF_ROUND_SEC)
                bs += 1/60.0;
            /* compute again */
            min = (bs - Math.floor(bs)) * 60;
            sec = 0;

        } else if (conf.round == 2 || conf.round == 3)
        {
            switch(type)
            {
                case FAJR:
                case ZUHR:
                case ASSR:
                case MAGHRIB:
                case ISHAA:
                case NEXTFAJR:

                    if (conf.round == 2) {
                        if (sec >= DEF_ROUND_SEC) {
                            bs += 1/60.0;
                            min = (bs - Math.floor(bs)) * 60;
                        }
                    } else if (conf.round == 3)
                    {
                        if (sec >= AGGRESSIVE_ROUND_SEC) {
                            bs += 1/60.0;
                            min = (bs - Math.floor(bs)) * 60;
                        }
                    }
                    sec = 0;
                    break;

                case SHUROOQ:
                case IMSAAK:
                    sec = 0;
                    break;
            }
        }

        /* Add daylight saving time and fix after midnight times */
        bs += loc.dst;
        if (bs >= 24)
            bs = bs % 24;

        pt.hour = (int)bs;
        pt.minute = (int)min;
        pt.second = (int)sec;
    }


    private double getFajIsh(double lat, double dec, double Ang)
    {
        double rlat = Angle.DEG_TO_RAD(lat);

        /* Compute the hour angle */
        double part1 = Math.sin(Angle.DEG_TO_RAD(-Ang)) - 
                            (Math.sin (rlat) * Math.sin (dec));
        double part2 = Math.cos (rlat) * Math.cos (dec);
        double part3 = part1 / part2;

        if ( part3 < -Angle.INVALID_TRIGGER || part3 > Angle.INVALID_TRIGGER)
            return 99;

        return Angle.DEG_TO_10_BASE * Angle.RAD_TO_DEG(Math.acos(part3));
    }

    private double getZuhr(double lon, AstroValues astro)
    {
        AstroDay astroDay = new AstroDay();     // TODO null?
        return astroDay.getTransit(lon, astro);
    }

    private double getAssr(double lat, double dec, int mathhab)
    {
        double part1, part2, part3, part4, ndec;
        double rlat = Angle.DEG_TO_RAD(lat);

        /* Reverse if at or near the southern hemisphere */    
        ndec = dec;
        if (lat < 0.0)
            ndec = -dec;
        part1 = mathhab + Math.tan(rlat - ndec);
        if (part1 < 1.0)
            part1 = mathhab - Math.tan(rlat - ndec);

        part2 = (Math.PI/2.0) - Math.atan(part1);
        /* Compute the hour angle */
        part3 = Math.sin(part2) - (Math.sin(rlat) * Math.sin(ndec));
        part4 = (part3 / (Math.cos(rlat) * Math.cos(ndec)));

        if ( part4 < -Angle.INVALID_TRIGGER || part4 > Angle.INVALID_TRIGGER)
            return 99;

        return Angle.DEG_TO_10_BASE * Angle.RAD_TO_DEG(Math.acos(part4));
    }

    public Prayer()
    {
        astroCache = new AstroValues();
    }


    /* "getPrayerTimes" fills the array of six prayer Times structures. 
       This array contains the prayer minutes and hours information
       like this:
        - Prayer[0].minute    is today's Fajr minutes
        - Prayer[1].hour      is today's Shorooq hours
        - ... and so on until...
        - Prayer[5].minute    is today's Ishaa minutes 
    */
    public PrayerTime[] getPrayerTimes(PTLocation loc, Method conf, Date date)
    {
        getDayInfo(date, loc.gmtDiff);
        return getPrayerTimesByDay(loc, conf, 0);
    }           
     
    /* Extended prayer times */
    public PrayerTime getImsaak(PTLocation loc, Method conf, Date date)
    {
        Method tmpConf = new Method(conf);      // TODO null?

        if (conf.fajrInv != 0) { 
            if (conf.imsaakInv == 0)
                tmpConf.fajrInv += DEF_IMSAAK_INTERVAL;
            else tmpConf.fajrInv += conf.imsaakInv;

        } else if (conf.imsaakInv != 0) {
            /* use an inv even if al-Fajr is computed (Indonesia?) */       
            tmpConf.offList[0] += (conf.imsaakInv * -1);
            tmpConf.offset = 1;
        } else { 
            tmpConf.fajrAng += conf.imsaakAng;
        }
 
        getDayInfo (date, loc.gmtDiff);
        PrayerTime[] pt = getPrayerTimesByDay(loc, tmpConf, IMSAAK);

        /* FIXIT: We probably need to check whether it's possible to compute
         * Imsaak normally for some extreme methods first */
        /* In case of an extreme Fajr time calculation use intervals for Imsaak and
         * compute again */
        if (pt[0].isExtreme != 0)
        {
            tmpConf = null;
            tmpConf = new Method(conf);

            if ( conf.imsaakInv == 0)
            {
                tmpConf.offList[0] -= DEF_IMSAAK_INTERVAL;
                tmpConf.offset = 1;
            } else
            {
                tmpConf.offList[0] -= conf.imsaakInv;
                tmpConf.offset = 1;
            }
            pt = null;
            pt = getPrayerTimesByDay(loc, tmpConf, IMSAAK);
        }

        return new PrayerTime(pt[0]);
    }

    public PrayerTime getNextDayImsaak(PTLocation loc, Method conf, Date date)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        return getImsaak(loc, conf, cal.getTime());
    }

    public PrayerTime getNextDayFajr(PTLocation loc, Method conf, Date date)
    {
        getDayInfo(date, loc.gmtDiff);
        julianDay++;
        PrayerTime[] pt = getPrayerTimesByDay(loc, conf, NEXTFAJR);

        return new PrayerTime(pt[0]);
    }

 
    /* utilities */
}
