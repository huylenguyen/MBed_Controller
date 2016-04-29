
import java.util.Scanner;

/**
 * Main method for the MBED package.
 * Handles threading for the MBEDHandler and program termination.
 * @author Huy
 */
public class Main {

    private static Scanner scan = new Scanner(System.in);
    private static final char TERMINATION_KEY = 'q'; //Keypress to exit program
    private static MBEDHandler mbed;
    
    public static void main(String[] args) {

        (mbed = new MBEDHandler()).start();
        programExit();

    }
    
    public static void programExit() {
        //Program termination condition
        logln("");
        logln("Scanning keyboard input for termination key prompt: " + TERMINATION_KEY);
        
        //Awaiting keypress
        logln("Press " + TERMINATION_KEY + " to end program.");
        while (scan.next().charAt(0) != TERMINATION_KEY) {
            logln(" > ");
        }
        
        //Program termination
        logln("TERMINATION KEY received, closing program.");
        mbed.mouseStop();
        mbed.closeMQTT();
        scan.close();
        System.exit(0);        
    }
    
    public static void logln(Object o) {
        System.out.println(o);
    }

    public static void log(Object o) {
        System.out.print(o);
    }
}
