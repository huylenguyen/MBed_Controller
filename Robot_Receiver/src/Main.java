
import java.awt.AWTException;
import java.util.Scanner;
import shed.mqtt.MQTTException;

/**
 * This is the main method for the Robot_Receiver package. 
 * Sets up the MQTT connection and input simulation.
 * Also handles termination of program with the key "q"
 * @author Huy
 */
public class Main {
    private static Scanner scan = new Scanner(System.in);
    private static final char TERMINATION_KEY = 'q'; //Keypress to exit program
    private static MQTT_Receiver mqtt;
    
    
    public static void main(String[] args) throws MQTTException, AWTException {
        (mqtt = new MQTT_Receiver()).run();
        
        //Awaiting keypress
        logln("Press " + TERMINATION_KEY + " to end program.");
        while (scan.next().charAt(0) != TERMINATION_KEY) {
            logln(" > ");
        }
        
        //Program termination
        scan.close();
        mqtt.close();
        System.exit(0);
    }
    
    public void terminateProgram() {
        
    }
    
    public static void logln(Object o) {
        System.out.println(o);
    }

    public static void log(Object o) {
        System.out.print(o);
    }
}
