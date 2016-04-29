import java.awt.AWTException;
import shed.mqtt.MQTT;
import shed.mqtt.InboundPayload;
import shed.mqtt.MQTTDataCallback;
import shed.mqtt.MQTTException;

/**
 * This class handles subscription to a MQTT server.
 * Incoming messages are analysed and passed onto the InputSimulation class
 * @author Huy Nguyen
 */
public class MQTT_Receiver {
    //Server and topic information
    private static final String HOST = "tcp://doughnut.kent.ac.uk";
    private static final String TOPIC = "huy";
    private static final int PORT = 1883; //Default port for mqtt (1883)    
    //Client ID
    private static final String ID = "MQTT_Receiver";
    //The object for communicating with the MQTT server
    private MQTT mqtt;
    //The object for handling Input simulation
    private InputHandler input;

    /**
     * Connects to an MQTT server and initializes input simulation
     * @throws MQTTException on failure to connect.
     * @throws java.awt.AWTException
     */
    public MQTT_Receiver() throws AWTException, MQTTException {
        mqtt = new MQTT(this.HOST, this.PORT);
        Main.logln("Connecting to MQTT server");
        mqtt.connect();
        Main.logln("Successfully connected");
        input = new InputHandler();
        Main.logln("Initialising input simulation");
    }
    
    /**
     * Sets up listeners and subscribes to a topic
     * @throws MQTTException Connection to client failed
     */
    public void run() throws MQTTException {
        setupListener();
        Main.logln("Listeners successfully initialised");
        mqtt.subscribe(this.TOPIC + "/#");
        Main.logln("Subscribed to topic: " + this.TOPIC + "/#");
    }
    
    /**
     * Set up a listener for incoming messages on subscribed topics
     * @param callback The handler for received data.
     */
    public void setupListener(MQTTDataCallback callback) {
        mqtt.setCallback(callback);
    }
    
    /**
     * Setup a listener to subscribed topics.
     * This listener handles input specifically for data coming from the 
     * MBEDHandler class in the MBED package.
     * Passes the reading onto the InputHandler class.
     */
    public void setupListener() {      
        setupListener( (String topic, InboundPayload payload) -> {
            //Listens for button presses/releases.
            if (topic.contains("Joystick")) {
                //Assume the message contains (String, String, boolean)
                String device = payload.readUTF();
                String button = payload.readUTF();
                boolean state = payload.readBoolean();
                input.simulateKeyInput(device, button, state);
                Main.logln("Toggling " + button + " " + state);
            }
            //Listens for mouse input data.
            if (topic.contains("Mouse")) {
                //Assume the message contains (String, double, double)
                String device = payload.readUTF();
                double yaw = payload.readDouble();
                double pitch = payload.readDouble();
                input.simulateMouseInput(device, yaw, pitch);
            }
        });
    }
    
    /**
     * Closes the connection to MQTT server
     * @throws MQTTException Failed to close connection with server
     */
    public void close() throws MQTTException {
        mqtt.close();
    }
    
    /**
     * Print out server connection details
     */
    public void printServerDetails() {
        Main.logln("Server URL: " + HOST + ":" + PORT + ", on topic '" + TOPIC + "'");
    }
}
