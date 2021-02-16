package gr.liakos.spearo;

import org.junit.Test;

import java.util.Calendar;

import gr.liakos.spearo.util.MoonPhaseUtil;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {


        Calendar cal = Calendar.getInstance();
        cal.set(2021, 0, 28);
        cal.set(Calendar.HOUR_OF_DAY, 12);

     //   System.out.println(MoonPhaseUtility.moonPhaseFromDate(cal.getTimeInMillis()));


  //      System.out.println(new SimpleDateFormat("EEEE, dd-MMMM-yyyy HH:mm zzzz").format(new Date()));
//
        MoonPhaseUtil mp = new MoonPhaseUtil(cal);
        System.out.printf("Current phase: %f%n", mp.getPhase());
        System.out.println("Phase index: " + mp.getPhaseIndex());
        System.out.println("Phase index: " + mp.getPhaseIndexString(mp.getPhaseIndex()));
        System.out.println("Moon Age: " + mp.getMoonAgeAsDays());
//
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(System.currentTimeMillis());
//        c.add(Calendar.DAY_OF_WEEK, -22);
//
//        for (int i=0; i< 33; i++){
//            c.add(Calendar.DAY_OF_WEEK, 1);
//            mp.updateCal(c);
//            System.out.format("%1$td-%1$tB,%1$tY  %1$tH:%1$tM:%1$tS  ",c);
//            System.out.printf("%f%n", mp.getPhase());
//        }


        //System.out.println( new SimpleDateFormat("EEEE, dd-MMMM-yyyy HH:mm zzzz").format(c.getTime()));

//        double JD = calendarToJD_BAD_WRONG(c);
//        double JD2 =calendarToJD(c);
//
//        System.out.println("Julian Date: " + JD);
//        System.out.println("Julian Date2: " + JD2);
//        System.out.println("Parsed Phase: " + phase(JD));
//        System.out.println("Parsed Phase2: " + phase(JD2));
//
//        c.add(Calendar.DAY_OF_WEEK, 1);
//        JD = calendarToJD_BAD_WRONG(c);
//        System.out.println("Parsed Phase +1 day: " + phase(JD));
//        System.out.printf("F 22-Jan-2008 2454488.0649868953 %.8f\n", phase(2454488.0649868953));
//        System.out.printf("N 07-Mar-2008 2454533.2179779503 %.8f\n", phase(2454533.2179779503));
//        System.out.printf("F 21-Mar-2008 2454547.2769324942 %.8f\n", phase(2454547.2769324942));
//        System.out.printf("N 27-Dec-2008 2454828.0159972804 %.8f\n", phase(2454828.0159972804));
//        System.out.printf("F 11-Jan-2009 2454842.6434176303 %.8f\n", phase(2454842.6434176303));
//        System.out.printf("N 07-Mar-2008 2454533.2179779503 %.8f\n", phase(2454533.2179779503));




    }
}