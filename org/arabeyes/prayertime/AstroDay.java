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

enum SunEvent {
    SUNRISE,
    SUNSET
}

public class AstroDay {

    /*
    private static final double[][] DT = {
        {121, 112, 103, 95, 88},
        {82, 77, 72, 68, 63},
        {60, 56, 53, 51, 48},
        {46, 44, 42, 40, 38},
        {35, 33, 31, 29, 26},
        {24, 22, 20, 18, 16},
        {14, 12, 11, 10, 9},
        {8, 7, 7, 7, 7},
        {7, 7, 8, 8, 9},
        {9, 9, 9, 9, 10},
        {10, 10, 10, 10, 10},
        {10, 10, 11, 11, 11},
        {11, 11, 12, 12, 12},
        {12, 13, 13, 13, 14},
        {14, 14, 14, 15, 15},
        {15, 15, 15, 16, 16},
        {16, 16, 16, 16, 16},
        {16, 15, 15, 14, 13},
        {13.1, 12.5, 12.2, 12, 12},
        {12, 12, 12, 12, 11.9},
        {11.6, 11, 10.2, 9.2, 8.2},
        {7.1, 6.2, 5.6, 5.4, 5.3},
        {5.4, 5.6, 5.9, 6.2, 6.5},
        {6.8, 7.1, 7.3, 7.5, 7.6},
        {7.7, 7.3, 6.2, 5.2, 2.7},
        {1.4, -1.2, -2.8, -3.8, -4.8},
        {-5.5, -5.3, -5.6, -5.7, -5.9},
        {-6.0, -6.3, -6.5, -6.2, -4.7},
        {-2.8, -0.1, 2.6, 5.3, 7.7},
        {10.4, 13.3, 16, 18.2, 20.2},
        {21.1, 22.4, 23.5, 23.8, 24.3},
        {24, 23.9, 23.9, 23.7, 24},
        {24.3, 25.3, 26.2, 27.3, 28.2},
        {29.1, 30, 30.7, 31.4, 32.2},
        {33.1, 34, 35, 36.5, 38.3},
        {40.2, 42.2, 44.5, 46.5, 48.5},
        {50.5, 52.2, 53.8, 54.9, 55.8},
        {56.9, 58.3, 60, 61.6, 63}, /* 1990-1998 */
    /* };  */

    private static final double[] DT2 = {
        63.4673, 63.8285, 64.0908, 64.2998, 64.4734, /* 1999-2003 */
        64.5736, 64.7052, 64.8452, 65.1464, 65.4574, /* 2004-2008 */
        65.7768,                                     /* 2009 */
        66.5, 67.1, 68, 68, 69,                      /* 2010-2014 predictions */
        69, 70, 70                                   /* 2015-2017 predictions */
    };
 
    private static final double[][] L0 = {
        {175347046, 0, 0},
        {3341656, 4.6692568, 6283.07585},
        {34894, 4.6261, 12566.1517},
        {3497, 2.7441, 5753.3849},
        {3418, 2.8289, 3.5231},
        {3136, 3.6277, 77713.7715},
        {2676, 4.4181, 7860.4194},
        {2343, 6.1352, 3930.2097},
        {1324, 0.7425, 11506.7698},
        {1273, 2.0371, 529.691},
        {1199, 1.1096, 1577.3435},
        {990, 5.233, 5884.927},
        {902, 2.045, 26.298},
        {857, 3.508, 398.149},
        {780, 1.179, 5223.694},
        {753, 2.533, 5507.553},
        {505, 4.583, 18849.228},
        {492, 4.205, 775.523},
        {357, 2.92, 0.067},
        {317, 5.849, 11790.629},
        {284, 1.899, 796.298},
        {271, 0.315, 10977.079},
        {243, 0.345, 5486.778},
        {206, 4.806, 2544.314},
        {205, 1.869, 5573.143},
        {202, 2.458, 6069.777},
        {156, 0.833, 213.299},
        {132, 3.411, 2942.463},
        {126, 1.083, 20.775},
        {115, 0.645, 0.98},
        {103, 0.636, 4694.003},
        {102, 0.976, 15720.839},
        {102, 4.267, 7.114},
        {99, 6.21, 2146.17},
        {98, 0.68, 155.42},
        {86, 5.98, 161000.69},
        {85, 1.3, 6275.96},
        {85, 3.67, 71430.7},
        {80, 1.81, 17260.15},
        {79, 3.04, 12036.46},
        {75, 1.76, 5088.63},
        {74, 3.5, 3154.69},
        {74, 4.68, 801.82},
        {70, 0.83, 9437.76},
        {62, 3.98, 8827.39},
        {61, 1.82, 7084.9},
        {57, 2.78, 6286.6},
        {56, 4.39, 14143.5},
        {56, 3.47, 6279.55},
        {52, 0.19, 12139.55},
        {52, 1.33, 1748.02},
        {51, 0.28, 5856.48},
        {49, 0.49, 1194.45},
        {41, 5.37, 8429.24},
        {41, 2.4, 19651.05},
        {39, 6.17, 10447.39},
        {37, 6.04, 10213.29},
        {37, 2.57, 1059.38},
        {36, 1.71, 2352.87},
        {36, 1.78, 6812.77},
        {33, 0.59, 17789.85},
        {30, 0.44, 83996.85},
        {30, 2.74, 1349.87},
        {25, 3.16, 4690.48}
    };

    private static final double[][] L1 = {
        {628331966747.0, 0, 0},
        {206059, 2.678235, 6283.07585},
        {4303, 2.6351, 12566.1517},
        {425, 1.59, 3.523},
        {119, 5.796, 26.298},
        {109, 2.966, 1577.344},
        {93, 2.59, 18849.23},
        {72, 1.14, 529.69},
        {68, 1.87, 398.15},
        {67, 4.41, 5507.55},
        {59, 2.89, 5223.69},
        {56, 2.17, 155.42},
        {45, 0.4, 796.3},
        {36, 0.47, 775.52},
        {29, 2.65, 7.11},
        {21, 5.34, 0.98},
        {19, 1.85, 5486.78},
        {19, 4.97, 213.3},
        {17, 2.99, 6275.96},
        {16, 0.03, 2544.31},
        {16, 1.43, 2146.17},
        {15, 1.21, 10977.08},
        {12, 2.83, 1748.02},
        {12, 3.26, 5088.63},
        {12, 5.27, 1194.45},
        {12, 2.08, 4694},
        {11, 0.77, 553.57},
        {10, 1.3, 6286.6},
        {10, 4.24, 1349.87},
        {9, 2.7, 242.73},
        {9, 5.64, 951.72},
        {8, 5.3, 2352.87},
        {6, 2.65, 9437.76},
        {6, 4.67, 4690.48}
    };

    private static final double[][] L2 = {
        {52919, 0, 0},
        {8720, 1.0721, 6283.0758},
        {309, 0.867, 12566.152},
        {27, 0.05, 3.52},
        {16, 5.19, 26.3},
        {16, 3.68, 155.42},
        {10, 0.76, 18849.23},
        {9, 2.06, 77713.77},
        {7, 0.83, 775.52},
        {5, 4.66, 1577.34},
        {4, 1.03, 7.11},
        {4, 3.44, 5573.14},
        {3, 5.14, 796.3},
        {3, 6.05, 5507.55},
        {3, 1.19, 242.73},
        {3, 6.12, 529.69},
        {3, 0.31, 398.15},
        {3, 2.28, 553.57},
        {2, 4.38, 5223.69},
        {2, 3.75, 0.98}
    };

    private static final double[][] L3 = {
        {289, 5.844, 6283.076},
        {35, 0, 0},
        {17, 5.49, 12566.15},
        {3, 5.2, 155.42},
        {1, 4.72, 3.52},
        {1, 5.3, 18849.23},
        {1, 5.97, 242.73}   
    };

    private static final double[][] L4 = {
        {114, 3.142, 0},
        {8, 4.13, 6283.08},
        {1, 3.84, 12566.15}
    };

    private static final double[][] L5 = {
        {1, 3.14, 0}
    };

    private static final double[][] B0 = {
        {280, 3.199, 84334.662},
        {102, 5.422, 5507.553},
        {80, 3.88, 5223.69},
        {44, 3.7, 2352.87},
        {32, 4, 1577.34}
    };

    private static final double[][] B1 = {
        {9, 3.9, 5507.55},
        {6, 1.73, 5223.69}
    };

    private static final double[][] R0 = {
        {100013989, 0, 0},
        {1670700, 3.0984635, 6283.07585},
        {13956, 3.05525, 12566.1517},
        {3084, 5.1985, 77713.7715},
        {1628, 1.1739, 5753.3849},
        {1576, 2.8469, 7860.4194},
        {925, 5.453, 11506.77},
        {542, 4.564, 3930.21},
        {472, 3.661, 5884.927},
        {346, 0.964, 5507.553},
        {329, 5.9, 5223.694},
        {307, 0.299, 5573.143},
        {243, 4.273, 11790.629},
        {212, 5.847, 1577.344},
        {186, 5.022, 10977.079},
        {175, 3.012, 18849.228},
        {110, 5.055, 5486.778},
        {98, 0.89, 6069.78},
        {86, 5.69, 15720.84},
        {86, 1.27, 161000.69},
        {65, 0.27, 17260.15},
        {63, 0.92, 529.69},
        {57, 2.01, 83996.85},
        {56, 5.24, 71430.7},
        {49, 3.25, 2544.31},
        {47, 2.58, 775.52},
        {45, 5.54, 9437.76},
        {43, 6.01, 6275.96},
        {39, 5.36, 4694},
        {38, 2.39, 8827.39},
        {37, 0.83, 19651.05},
        {37, 4.9, 12139.55},
        {36, 1.67, 12036.46},
        {35, 1.84, 2942.46},
        {33, 0.24, 7084.9},
        {32, 0.18, 5088.63},
        {32, 1.78, 398.15},
        {28, 1.21, 6286.6},
        {28, 1.9, 6279.55},
        {26, 4.59, 10447.39}
    };

    private static final double[][] R1 = {
        {103019, 1.10749, 6283.07585},
        {1721, 1.0644, 12566.1517},
        {702, 3.142, 0},
        {32, 1.02, 18849.23},
        {31, 2.84, 5507.55},
        {25, 1.32, 5223.69},
        {18, 1.42, 1577.34},
        {10, 5.91, 10977.08},
        {9, 1.42, 6275.96},
        {9, 0.27, 5486.78}
    };

    private static final double[][] R2 = {
        {4359, 5.7846, 6283.0758},
        {124, 5.579, 12566.152},
        {12, 3.14, 0},
        {9, 3.63, 77713.77},
        {6, 1.87, 5573.14},
        {3, 5.47, 18849.23}

    };

    private static final double[][] R3 = {
        {145, 4.273, 6283.076},
        {7, 3.92, 12566.15}
    };

    private static final double[][] R4 = {
        {4, 2.56, 6283.08}
    };

    private static final double[][] PN = {
        {-171996, -174.2, 92025, 8.9},
        {-13187, -1.6, 5736, -3.1},
        {-2274, -0.2, 977, -0.5},
        {2062, 0.2, -895, 0.5},
        {1426, -3.4, 54, -0.1},
        {712, 0.1, -7, 0},
        {-517, 1.2, 224, -0.6},
        {-386, -0.4, 200, 0},
        {-301, 0, 129, -0.1},
        {217, -0.5, -95, 0.3},
        {-158, 0, 0, 0},
        {129, 0.1, -70, 0},
        {123, 0, -53, 0},
        {63, 0, 0, 0},
        {63, 0.1, -33, 0},
        {-59, 0, 26, 0},
        {-58, -0.1, 32, 0},
        {-51, 0, 27, 0},
        {48, 0, 0, 0},
        {46, 0, -24, 0},
        {-38, 0, 16, 0},
        {-31, 0, 13, 0},
        {29, 0, 0, 0},
        {29, 0, -12, 0},
        {26, 0, 0, 0},
        {-22, 0, 0, 0},
        {21, 0, -10, 0},
        {17, -0.1, 0, 0},
        {16, 0, -8, 0},
        {-16, 0.1, 7, 0},
        {-15, 0, 9, 0},
        {-13, 0, 7, 0},
        {-12, 0, 6, 0},
        {11, 0, 0, 0},
        {-10, 0, 5, 0},
        {-8, 0, 3, 0},
        {7, 0, -3, 0},
        {-7, 0, 0, 0},
        {-7, 0, 3, 0},
        {-7, 0, 3, 0},
        {6, 0, 0, 0},
        {6, 0, -3, 0},
        {6, 0, -3, 0},
        {-6, 0, 3, 0},
        {-6, 0, 3, 0},
        {5, 0, 0, 0},
        {-5, 0, 3, 0},
        {-5, 0, 3, 0},
        {-5, 0, 3, 0},
        {4, 0, 0, 0},
        {4, 0, 0, 0},
        {4, 0, 0, 0},
        {-4, 0, 0, 0},
        {-4, 0, 0, 0},
        {-4, 0, 0, 0},
        {3, 0, 0, 0},
        {-3, 0, 0, 0},
        {-3, 0, 0, 0},
        {-3, 0, 0, 0},
        {-3, 0, 0, 0},
        {-3, 0, 0, 0},
        {-3, 0, 0, 0},
        {-3, 0, 0, 0}
    };

    private static final int[][] COEFF = {
        {0, 0, 0, 0, 1},
        {-2, 0, 0, 2, 2},
        {0, 0, 0, 2, 2},
        {0, 0, 0, 0, 2},
        {0, 1, 0, 0, 0},
        {0, 0, 1, 0, 0},
        {-2, 1, 0, 2, 2},
        {0, 0, 0, 2, 1},
        {0, 0, 1, 2, 2},
        {-2, -1, 0, 2, 2},
        {-2, 0, 1, 0, 0},
        {-2, 0, 0, 2, 1},
        {0, 0, -1, 2, 2},
        {2, 0, 0, 0, 0},
        {0, 0, 1, 0, 1},
        {2, 0, -1, 2, 2},
        {0, 0, -1, 0, 1},
        {0, 0, 1, 2, 1},
        {-2, 0, 2, 0, 0},
        {0, 0, -2, 2, 1},
        {2, 0, 0, 2, 2},
        {0, 0, 2, 2, 2},
        {0, 0, 2, 0, 0},
        {-2, 0, 1, 2, 2},
        {0, 0, 0, 2, 0},
        {-2, 0, 0, 2, 0},
        {0, 0, -1, 2, 1},
        {0, 2, 0, 0, 0},
        {2, 0, -1, 0, 1},
        {-2, 2, 0, 2, 2},
        {0, 1, 0, 0, 1},
        {-2, 0, 1, 0, 1},
        {0, -1, 0, 0, 1},
        {0, 0, 2, -2, 0},
        {2, 0, -1, 2, 1},
        {2, 0, 1, 2, 2},
        {0, 1, 0, 2, 2},
        {-2, 1, 1, 0, 0},
        {0, -1, 0, 2, 2},
        {2, 0, 0, 2, 1},
        {2, 0, 1, 0, 0},
        {-2, 0, 2, 2, 2},
        {-2, 0, 1, 2, 1},
        {2, 0, -2, 0, 1},
        {2, 0, 0, 0, 1},
        {0, -1, 1, 0, 0},
        {-2, -1, 0, 2, 1},
        {-2, 0, 0, 0, 1},
        {0, 0, 2, 2, 1},
        {-2, 0, 2, 0, 1},
        {-2, 1, 0, 2, 1},
        {0, 0, 1, -2, 0},
        {-1, 0, 1, 0, 0},
        {-2, 1, 0, 0, 0},
        {1, 0, 0, 0, 0},
        {0, 0, 1, 2, 0},
        {0, 0, -2, 2, 2},
        {-1, -1, 1, 0, 0},
        {0, 1, 1, 0, 0},
        {0, -1, 1, 2, 2},
        {2, -1, -1, 2, 2},
        {0, 0, 3, 2, 2},
        {2, -1, 0, 2, 2}
    };

    private double dec;
    private double ra;
    private double sidtime;
    private double dra;
    private double rsum;
    
    private double limitAngle180(double L)
    {
        double F;
        L /= 180.0;
        F = L - Math.floor(L);
        if (F > 0)
            return 180 * F;
        else if (F < 0)
            return 180 - 180 * F;
        else return L;
    }

    private double limitAngle(double L)
    {
        double F;
        L /= 360.0;
        F = L - Math.floor(L);
        if (F > 0)
            return 360 * F;
        else if (F < 0)
            return 360 - 360 * F;
        else return L; 
    }

    /* Limit between 0 and 1 (fraction of day)*/
    private double limitAngle1(double L)
    {
        double F;
        F = L - Math.floor(L);
        if (F < 0)
            return F += 1;
        return F;
    }

    private double limitAngle180between(double L)
    {
        double F;
        L /= 360.0;
        F = (L - Math.floor(L)) * 360.0;
        if  (F < -180)
            F += 360;
        else if  (F > 180)
            F -= 360;
        return F;
    }

    private void computeAstroDay(double JD)
    {
        int i =0;
        double R, Gg, rGg, G;

        double tL, L;
        double tB, B;

        double D, M, M1, F, O;

        double U, E0, E, rE, lambda, rLambda, V0, V;

        double RAn, RAd, RA, DEC;

        double B0sum=0, B1sum=0;
        double R0sum=0, R1sum=0, R2sum=0, R3sum=0, R4sum=0;
        double L0sum=0, L1sum=0, L2sum=0, L3sum=0, L4sum=0, L5sum=0;

        double PNsum=0, psi=0, epsilon=0;
        double deltaPsi, deltaEps;

        double JC = (JD - 2451545)/36525.0;                                             
        double JM = JC/10.0; 
        double JM2 = Math.pow (JM, 2);
        double JM3 = Math.pow (JM, 3);
        double JM4 = Math.pow (JM, 4);
        double JM5 = Math.pow (JM, 5);

        /* FIXIT: By default, the getJulianDay function returns JDE rather then JD,
         * make sure this is accurate, and works right in last-day-of-year
         * circumstances.  */
        double JDE = JD;

        double T = (JDE - 2451545)/36525.0;

        for(i=0; i < 64; i++)
            L0sum += L0[i][0] * Math.cos(L0[i][1] + L0[i][2] * JM);
        for(i=0; i < 34; i++)
            L1sum += L1[i][0] * Math.cos(L1[i][1] + L1[i][2] * JM);
        for(i=0; i < 20; i++)
            L2sum += L2[i][0] * Math.cos(L2[i][1] + L2[i][2] * JM);
        for(i=0; i < 7; i++)
            L3sum += L3[i][0] * Math.cos(L3[i][1] + L3[i][2] * JM);
        for(i=0; i < 3; i++)
            L4sum += L4[i][0] * Math.cos(L4[i][1] + L4[i][2] * JM);
        L5sum = L5[0][0] * Math.cos(L5[0][1] + L5[0][2] * JM);


        tL = (L0sum + (L1sum * JM) + (L2sum * JM2) 
                + (L3sum * JM3) + (L4sum * JM4) 
                + (L5sum * JM5)) / Math.pow (10, 8);

        L = limitAngle(Angle.RAD_TO_DEG(tL));

        for(i=0; i<5; i++)
            B0sum += B0[i][0] * Math.cos(B0[i][1] + B0[i][2] * JM);
        for(i=0; i<2; i++)
            B1sum += B1[i][0] * Math.cos(B1[i][1] + B1[i][2] * JM);


        tB= (B0sum + (B1sum * JM)) / Math.pow (10, 8);
        B = Angle.RAD_TO_DEG(tB);


        for(i=0; i < 40; i++)
            R0sum += R0[i][0] * Math.cos(R0[i][1] + R0[i][2] * JM);
        for(i=0; i < 10; i++)
            R1sum += R1[i][0] * Math.cos(R1[i][1] + R1[i][2] * JM);
        for(i=0; i < 6; i++)
            R2sum += R2[i][0] * Math.cos(R2[i][1] + R2[i][2] * JM);
        for(i=0; i < 2; i++)
            R3sum += R3[i][0] * Math.cos(R3[i][1] + R3[i][2] * JM);
        R4sum = R4[0][0] * Math.cos(R4[0][1] + R4[0][2] * JM);

        R = (R0sum + (R1sum * JM) + (R2sum * JM2)
                + (R3sum * JM3) + (R4sum * JM4)) / Math.pow (10, 8);

        G = limitAngle((L + 180));
        Gg = -B;
        rGg = Angle.DEG_TO_RAD(Gg);
        /* Compute the fundamental arguments (p. 144) */
        D = 297.85036 + (445267.111480 * T) -  (0.0019142 * Math.pow (T, 2)) +
            (Math.pow (T, 3)/189474.0);
        M = 357.52772 + (35999.050340 * T) -  (0.0001603 * Math.pow (T, 2)) -  
            (Math.pow (T, 3)/300000.0);
        M1 = 134.96298 + (477198.867398 * T) +  (0.0086972 * Math.pow (T, 2)) +  
            (Math.pow (T, 3)/56250.0);
        F = 93.27191 + (483202.017538 * T) -  ( 0.0036825 * Math.pow (T, 2)) +  
            (Math.pow (T, 3)/327270.0);
        O = 125.04452 - (1934.136261 * T) + (0.0020708 * Math.pow (T, 2)) +  
            (Math.pow (T, 3)/450000.0);
        /* Add the terms (pp. 144-6) */
        for (i=0; i<63; i++) {
            PNsum += D  * COEFF[i][0];
            PNsum += M  * COEFF[i][1];
            PNsum += M1 * COEFF[i][2];
            PNsum += F  * COEFF[i][3];
            PNsum += O  * COEFF[i][4];
            psi     += (PN[i][0] + JC*PN[i][1])*Math.sin(Angle.DEG_TO_RAD(PNsum));
            epsilon += (PN[i][2] + JC*PN[i][3])*Math.cos(Angle.DEG_TO_RAD(PNsum));
            PNsum=0;
        }

        deltaPsi = psi/36000000.0;
        /* Nutation in obliquity */
        deltaEps = epsilon/36000000.0;

        /* The obliquity of the ecliptic (p. 147, 22.3) */
        U = JM/10.0;
        E0 = 84381.448 - 4680.93 * U - 1.55 * Math.pow(U,2) + 1999.25 * Math.pow(U,3) 
            - 51.38 * Math.pow(U,4)  - 249.67 * Math.pow(U,5) - 39.05 * Math.pow(U,6) + 7.12 
            * Math.pow(U,7) + 27.87 * Math.pow(U,8) + 5.79 * Math.pow(U,9) + 2.45 * Math.pow(U,10);
        /* Real/true obliquity (p. 147) */
        E = E0/3600.0 + deltaEps;
        rE = Angle.DEG_TO_RAD(E);

        lambda = G + deltaPsi + (-20.4898/(3600.0 * R));
        rLambda = Angle.DEG_TO_RAD(lambda);

        /* Mean Sidereal time (p. 88) */
        V0 = 280.46061837 + 360.98564736629 * ( JD - 2451545) +  
            0.000387933 * Math.pow(JC,2) - Math.pow(JC,3)/ 38710000.0;
        /* Apparent sidereal time */
        V = limitAngle(V0) + deltaPsi * Math.cos(rE);

        RAn = Math.sin(rLambda) * Math.cos(rE) - Math.tan(rGg) * Math.sin(rE);
        RAd = Math.cos(rLambda);
        RA = limitAngle(Angle.RAD_TO_DEG(Math.atan2(RAn,RAd)));

        DEC = Math.asin( Math.sin(rGg) * Math.cos(rE) + Math.cos(rGg) * Math.sin(rE) * 
                Math.sin(rLambda));

        this.ra = RA;
        this.dec = DEC;
        this.sidtime = V;
        this.dra = 0;
        this.rsum = R;
    }

    private void computeTopAstro(PTLocation loc, AstroValues astro, AstroValues topAstro)
    {
        int i;
        double lHour, SP, rlHour, rLat;
        double tU, tpCos, tpSin, tRA0 ,tRA ,tDEC;

        rLat = Angle.DEG_TO_RAD(loc.degreeLat);

        for (i=0; i<3; i++)
        {
            lHour = limitAngle(astro.sid[i] + loc.degreeLong - astro.ra[i]);
            rlHour = Angle.DEG_TO_RAD(lHour);

            SP = Angle.DEG_TO_RAD (8.794/(3600 * astro.rsum[i]));

            /* (p. 82, with b/a = 0.99664719) */
            tU = Math.atan (0.99664719 * Math.tan(rLat));

            tpSin = 0.99664719 * Math.sin(tU) + (loc.seaLevel/Angle.EARTH_RADIUS) * 
                Math.sin(rLat);

            tpCos = Math.cos(tU) + (loc.seaLevel/Angle.EARTH_RADIUS) * Math.cos(rLat);

            /* (p. 297, 40.2) */
            tRA0 = (((-tpCos) * Math.sin(SP) * Math.sin(rlHour)) / (Math.cos(astro.dec[i]) - 
                        tpCos * Math.sin(SP) * Math.cos(rlHour)));

            tRA = astro.ra[i] + Angle.RAD_TO_DEG(tRA0);

            /* (p. 297, 40.3) */
            tDEC = Angle.RAD_TO_DEG(Math.atan2((Math.sin(astro.dec[i]) - tpSin * Math.sin(SP)) * Math.cos(tRA0), 
                        Math.cos(astro.dec[i]) - tpCos * Math.sin(SP) * 
                        Math.cos(rlHour)));

            topAstro.ra[i] = tRA;
            topAstro.dec[i] = tDEC;
            topAstro.sid[i] = astro.sid[i];
            topAstro.dra[i] = tRA0;
            topAstro.rsum[i] = astro.rsum[i];
        }
    }

    private double getRefraction(PTLocation loc, double sunAlt)
    {
        double part1, part2;

        part1 = (loc.pressure/1010.0) * (283/(273 + loc.temperature));
        part2 = 1.02 / (Angle.RAD_TO_DEG(Math.tan(Angle.DEG_TO_RAD(sunAlt + (10.3/(sunAlt + 5.11))))) + 0.0019279);

        return (part1 * part2) / 60.0;
    }

    private double getRiseSet (PTLocation loc, AstroValues tastro, SunEvent type)
    {
        /* p. 101 */
        double lhour, M, sidG, ra0, ra2;
        double A, B, H, sunAlt, delM, tH, rDec, rLat, rB;
        double part1, part2, part3;

        rDec = Angle.DEG_TO_RAD(tastro.dec[1]);
        rLat = Angle.DEG_TO_RAD(loc.degreeLat);

        ra0=tastro.ra[0];
        ra2=tastro.ra[2];

        /* Compute the hour angle */
        part1 = Math.sin(Angle.DEG_TO_RAD(Angle.CENTER_OF_SUN_ANGLE)) - (Math.sin(rLat) * Math.sin(rDec));
        part2 = Math.cos(rLat) * Math.cos(rDec);
        part3 = part1 / part2;

        if  ( part3 < -Angle.INVALID_TRIGGER || part3 > Angle.INVALID_TRIGGER)
            return 99;

        lhour = limitAngle180 (( Angle.RAD_TO_DEG (Math.acos(part3))));

        /* Eastern Longitudes are positive throughout this file. */ 
        M = ((tastro.ra[1] - loc.degreeLong - tastro.sid[1]) / 360.0);

        if (type == SunEvent.SUNRISE)
            M = M - (lhour/360.0);
        else if (type == SunEvent.SUNSET)
            M = M + (lhour/360.0);

        M = limitAngle1(M);

        /* Sidereal time at Greenwich (p. 103) */ 
        sidG = limitAngle(tastro.sid[1] + 360.985647 * M);

        ra0 = tastro.ra[0];
        ra2 = tastro.ra[2];

        if (tastro.ra[1] > 350 && tastro.ra[2] < 10)
            ra2 += 360;
        if (tastro.ra[0] > 350 && tastro.ra[1] < 10)
            ra0 = 0;

        /* Interpolation of 1-day intervals (pp. 24-25) */
        A = tastro.ra[1] + (M * (( tastro.ra[1] - ra0) +
                    (ra2 - tastro.ra[1] ) +
                    (( ra2 - tastro.ra[1] ) -
                     ( tastro.ra[1]  -  ra0)) * M) / 2.0 );

        B = tastro.dec[1] + (M * ((tastro.dec[1] - tastro.dec[0]) + 
                    (tastro.dec[2] - tastro.dec[1]) + 
                    ((tastro.dec[2] - tastro.dec[1]) -  
                     (tastro.dec[1] - tastro.dec[0])) * M) / 2.0 );
        rB = Angle.DEG_TO_RAD(B);

        H = limitAngle180between(sidG + loc.degreeLong - A);

        tH =  Angle.DEG_TO_RAD(H) - tastro.dra[1];

        /* Airless Sun's altitude at local horizontal coordinates (p. 93, 13.6) */
        sunAlt = Angle.RAD_TO_DEG(Math.asin(  Math.sin(rLat) * Math.sin(rB) 
                    + Math.cos(rLat) * Math.cos(rB) 
                    * Math.cos(tH) ));

        sunAlt += getRefraction(loc, sunAlt);

        /* (p. 103) */
        delM = (sunAlt - Angle.CENTER_OF_SUN_ANGLE) / (360.0 * Math.cos(rB) * Math.cos(rLat)
                * Math.sin(tH));

        return  (M + delM) * 24.0;
    }

    /* FIXIT: Not used and not tested. */
    private double getYearFromJulian(double jd)
    {
        double tempJD;
        double F, Z, A, B, a, D, E;
        int C, month, year;

        tempJD = jd + 0.5;

        /* Add an extra 0.5 to compensate for day starts if different than 0h UT at
         * local time. */
        if ((jd - Math.floor(jd)) > 0)
        {
            tempJD -= (jd - Math.floor(jd));
            tempJD += 0.5;
        }

        Z = (int)tempJD;
        F = tempJD - Math.floor(tempJD);
        if (Z < 2299161)
            A = Z;
        else
        {
            a = (int)((Z-1867216.25)/36524.25);
            A = Z + 1 + a - (int)(a/4.0);
        }

        B = A + 1524;
        C = (int)((B - 122.1)/365.25);
        D = (int)(365.25 * C);
        E = (int)((B - D)/30.6001);

        if (E < 14)
            month = (int)E - 1;
        else if ( E == 14 || E == 15 )
            month = (int)E - 13;
        else month = 0; /* Bad month */

        if (month > 2)
            return C - 4716;
        else return C - 4715;

    }

    private static double computeDeltaT(double year)
    {
        int i;
        double tempdt;
        /* pp. 78-80 */
        double t = (year - 2000) / 100.0;
        if (year < 948)
            return 2177 + (497 * t) + (44.1 * Math.pow (t, 2));
        else if (year >= 1620 && year <= 1998)
            return 0; /* FIXIT: Support historical delta-t values for years before
                       * 1998. In the DT table above, each line represents 5 even
                       * years in the range 1620-1998. We should first complete the
                       * table to include data for both odd and even years. */
        else if ((year > 1998 && year < 2100) || year < 1620)
        {
            /* FIXIT: The "2017" found below this comment should be changed to
               reflect the last year added to the DT2 table. */
            if (year >= 1999 && year <= 2017) {
                i = (int)year-1999;
                return DT2[i];
            }
            /* FIXIT: As of 2007, the two formulas given by Meeus seems to be off by
               many seconds when compared to observed values found at
               <http://maia.usno.navy.mil>. The DT2 table overrides these values
               with observed and predicted ones for delta-t (on January, other
               months not yet supported). Extrapolated (and wrong) values are still
               used for years after 2017. */
            else tempdt = 102 + (102 * t) + (25.3 * Math.pow (t, 2));

            if (year >= 2000)
                return tempdt + (0.37 * (year - 2100));
            else return tempdt;
        }
        return 0;
    }

    public AstroDay()
    {

    }

    /* Returns the astronomical Julian day (for local time with delta-t) */
    public static double getJulianDay(Date date, double gmt)
    {
        double jdB=0, jdY, jdM, JD;
        int day, month, year;

        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(date);
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);

        jdY=year;
        jdM=month;

        if (month <= 2) {
            jdY--;
            jdM+=12;
        }

        if (year < 1)
            jdY++;

        if ((year > 1582) || ((year == 1582) &&
                    ((month > 10) ||
                     ((month == 10) && (day >= 4)))))
            jdB = 2 - Math.floor(jdY/100.0) + Math.floor((jdY/100.0)/4.0);

        JD = Math.floor(365.25 * (jdY + 4716.0)) + Math.floor(30.6001 * (jdM + 1))
            + (day + (-gmt)/24.0) + jdB - 1524.5 ;

        JD = JD + (computeDeltaT(year) / 86400.0);
        return JD;
    }

    /* Fills the structure "astro" with a list of 3-day values, then checks and
     * updates these values if cached. The variable "tastro/topAstro" holds the
     * topocentric values of the same structure. */
    public void getAstroValuesByDay(double julianDay, PTLocation loc, AstroValues astro, AstroValues topAstro)
    {
        if (astro.jd == julianDay-1)
        {
            /* Copy cached values */
            astro.ra[0] = astro.ra[1];
            astro.ra[1] = astro.ra[2];
            astro.dec[0] = astro.dec[1];
            astro.dec[1] = astro.dec[2];
            astro.sid[0] = astro.sid[1];
            astro.sid[1] = astro.sid[2];
            astro.dra[0] = astro.dra[1];
            astro.dra[1] = astro.dra[2];
            astro.rsum[0] = astro.rsum[1];
            astro.rsum[1] = astro.rsum[2];
            /* Compute next day values */
            computeAstroDay(julianDay+1);
            astro.ra[2] = this.ra;
            astro.dec[2] = this.dec;
            astro.sid[2] = this.sidtime;
            astro.dra[2] = this.dra;
            astro.rsum[2] = this.rsum;
        }
        else if (astro.jd == julianDay + 1)
        {
            /* Copy cached values */
            astro.ra[2] = astro.ra[1];
            astro.ra[1] = astro.ra[0];
            astro.dec[2] = astro.dec[1];
            astro.dec[1] = astro.dec[0];
            astro.sid[2] = astro.sid[1];
            astro.sid[1] = astro.sid[0];
            astro.dra[2] = astro.dra[1];
            astro.dra[1] = astro.dra[0];
            astro.rsum[2] = astro.rsum[1];
            astro.rsum[1] = astro.rsum[0];
            /* Compute previous day values */
            computeAstroDay(julianDay-1);
            astro.ra[0] = this.ra;
            astro.dec[0] = this.dec;
            astro.sid[0] = this.sidtime;
            astro.dra[0] = this.dra;
            astro.rsum[0] = this.rsum;
        } 
        else if (astro.jd != julianDay)
        {
            /* Compute 3 day values */
            computeAstroDay(julianDay-1);
            astro.ra[0] = this.ra;
            astro.dec[0] = this.dec;
            astro.sid[0] = this.sidtime;
            astro.dra[0] = this.dra;
            astro.rsum[0] = this.rsum;
            computeAstroDay(julianDay);
            astro.ra[1] = this.ra;
            astro.dec[1] = this.dec;
            astro.sid[1] = this.sidtime;
            astro.dra[1] = this.dra;
            astro.rsum[1] = this.rsum;
            computeAstroDay(julianDay+1);
            astro.ra[2] = this.ra;
            astro.dec[2] = this.dec;
            astro.sid[2] = this.sidtime;
            astro.dra[2] = this.dra;
            astro.rsum[2] = this.rsum;
        }

        astro.jd = julianDay;
        computeTopAstro(loc, astro, topAstro);
    }

    public double getTransit(double lon, AstroValues tastro)
    {
        double M, sidG;
        double ra0=tastro.ra[0], ra2=tastro.ra[2];
        double A, H;

        M = ((tastro.ra[1] - lon - tastro.sid[1]) / 360.0);
        M = limitAngle1(M);

        /* Sidereal time at Greenwich (p. 103) */ 
        sidG =  tastro.sid[1] + 360.985647 * M;

        if (tastro.ra[1] > 350 && tastro.ra[2] < 10)
            ra2 += 360;
        if (tastro.ra[0] > 350 && tastro.ra[1] < 10)
            ra0 = 0;

        /* Interpolation of 1-day intervals (pp. 24-25) */
        A = tastro.ra[1] + 
            (M * ((tastro.ra[1] - ra0) + ( ra2 - tastro.ra[1]) + 
                  (( ra2 - tastro.ra[1]) - (tastro.ra[1] - ra0)) * M) / 2.0 );

        H =  limitAngle180between(sidG + lon - A);

        return  24.0 * (M - (H/360.0));
    }

    public double getSunrise(PTLocation loc, AstroValues tastro)
    {
        return getRiseSet (loc, tastro, SunEvent.SUNRISE);
    }
    
    public double getSunset(PTLocation loc, AstroValues tastro)
    {
        return getRiseSet (loc, tastro, SunEvent.SUNSET);
    }
}
