
import shed.mbed.AccelerometerData;
import shed.mbed.ButtonID;
import shed.mbed.MBed;
import shed.mbed.MagnetometerData;

/**
 * This class handles data output from the MBed to an MQTT server
 * Takes input from all buttons on the MBed and publishes them to MQTT server
 * as a String.
 *
 * @author Huy
 */
public class MBEDHandler {
    //MQTT server details
    private static final String HOST = "tcp://doughnut.kent.ac.uk";
    private static final String TOPIC = "huy";
    private static final int PORT = 1883; //Default port for mqtt (1883)

    private final MQTTClient client;
    private final MBed mbed;
    private final MouseSimulation mouse;

    /**
     * Constructor for the MBEDHandler class.
     * Initializes the MQTT client: connects to given server.
     */
    public MBEDHandler() {
        Main.logln("Started...");
        //Initialises connection to MBed device
        mbed = new MBed();
        mouse = new MouseSimulation();
        
        //Connects to MQTT server
        Main.logln("Attempting connection to MQTT Server.");
        client = new MQTTClient(HOST, PORT, TOPIC);
        assert client != null : "MQTTClient failed, cannot continue.";        
        Main.logln("Successfully connected.");

    }

    /**
     * Sets up and perform operations on the MBed. Sets up listeners and
     * establishes connection to an MQTT server.
     * Publishes button-related messages on button press/unpress.
     * Publishes mouse simulation data at a set interval.
     */
    public void start() {        
        //Sets up and handles listeners output
        Main.logln("Setting up button listeners...");
        for (ButtonID id : ButtonID.values()) {
            mbed.getButton(id).addListener(
                    isPressed -> {
                        client.publishButtonPress(id.toString(), isPressed);
                    });
            Main.logln("Listener set for " + id); //Temporary terminal logs for testing
        }
        Main.logln("Finished setting listeners.");
        
        //Sets up and handles mouse simulation output
        Main.logln("Simulating mouse movement...");
        Thread mouseSimulationThread = new Thread(mouse);
        mouseSimulationThread.start();
        Main.logln("Two-axis mouse simulation begun.");
    }
    
    /**
     * Closes the MQTT client
     */
    public void closeMQTT() {
        client.close();
    }
    
    /**
     * Stops the thread running mouse simulation data
     */
    public void mouseStop() {
        mouse.stop();
    }

    private class MouseSimulation implements Runnable {

        private static final int SLEEP_MS = 1000;
        private volatile boolean stopThread; // Safe way to stop a thread
        private double xAxisMag;
        private double yAxisMag;
        private double yAxisAcc;
        private double yaw;

        /**
         * Constructor for objects of class MouseSim
         */
        public MouseSimulation() {
            stopThread = false;
        }

        /**
         * Runs a simulation of the mouse using Accelerometer and Magnetometer
         * on the MBed board. Produces data at regular interval (SLEEP_MS) to
         * terminal and/or MQTT server. Currently produces a limited range of
         * data with high inaccuracy, hence deprecated.
         */
        @Deprecated
        public void run() {
            while (!stopThread) {
                AccelerometerData b = mbed.readBoardAccelerometer();
                yAxisAcc = b.getY();
                if (yAxisAcc < 0.1 && yAxisAcc > -0.1) {
                    yAxisAcc = 0.0;
                }

                MagnetometerData d = mbed.readBoardMagnetometer();
                xAxisMag = d.getX();
                yAxisMag = d.getY();
                yaw = 0;
                if (yAxisMag > 0) { //North
                    if (xAxisMag > 0) {
                        xAxisMag = xAxisMag - 7.5;
                        yaw = xAxisMag * 4.2;
                        yaw = Math.round(yaw);
                    } else {
                        yaw = xAxisMag * 6.1;
                        yaw = Math.round(yaw);
                    }
                }
//                if (yaw < 10 && yaw > -10) {
//                    yaw = 0.0;
//                }
                sleep(SLEEP_MS);
                publish();
            }

        }

        /**
         * Stops operations on the thread safely
         */
        public void stop() {
            stopThread = true;
        }

        /**
         * Publishes data to MQTT server
         */
        public void publish() {
            client.publishMouseData(this.getYaw(), this.getPitch());
        }

        /**
         * Produces mouse data as a string
         *
         * @return Returns the X and Y axis data as a string
         */
        public String getData() {
            return "Y:" + yAxisAcc + " Yaw:" + yaw;
        }

        public double getYaw() {
            return yaw;
        }

        public double getPitch() {
            return yAxisAcc;
        }

        /**
         * A simple support method for sleeping the program.
         *
         * @param millis The number of milliseconds to sleep for.
         */
        private void sleep(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ex) {
                // Nothing we can do.
            }
        }

    }

}
