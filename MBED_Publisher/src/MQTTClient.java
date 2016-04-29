
import shed.mqtt.MQTT;
import shed.mqtt.MQTTException;
import shed.mqtt.OutboundPayload;

/**
 * This class creates an MQTT client and handles publishing messages to server.
 * @author Huy
 */

public class MQTTClient {

    private String topic;
    private String server;
    private int port;
    private MQTT mqtt;

    /**
     *
     * Create an instance of an MQTTClient that automatically 
     * connects to an MQTT server and then publish data to the topic defined
     * @param server The Server URL
     * @param port The Server PORT
     * @param topic The Topic used for posting
     */
    public MQTTClient(String server, int port, String topic) {
        this.topic = topic;
        this.server = server;
        this.port = port;

        try {
            Main.log("Connecting to ");
            printServerDetails();
            mqtt = new MQTT(this.server, this.port);
            mqtt.connect();
            Main.logln("Connected to MQTT Server.");
        } catch (Exception exe) {
            Main.logln("Failed to create MQTT connection.");
            exe.printStackTrace();
        }
    }

    /**
     * Publish a message to the predefined topic.
     * Handles only button input.
     * @param button The specific button involved
     * @param state The state of the button
     * @return Successfully or not
     */
    public boolean publishButtonPress(String button, boolean state) {
        String device = "Joystick";
        try {
            OutboundPayload payload = mqtt.publishData(topic + "/" + device);
            payload.writeUTF(device);
            payload.writeUTF(button);
            payload.writeBoolean(state);
            payload.close();
            
            //Terminal logging
            Main.logln("Publishing... " + device + button + state);
            return true;
        } 
        catch (MQTTException exe) {
            exe.printStackTrace();
            Main.logln("Error occurred when publishing data!");
        }
        return false;       
    }
    /**
     * Publish two messages to the predefined topic: yaw and pitch.
     * Handles only mouse simulation input.
     * @param yaw horizontal data
     * @param pitch vertical data
     * @return Successful or not
     */
    public boolean publishMouseData(Double yaw, Double pitch) {
        String device = "Mouse";
        try {
            OutboundPayload payloadYaw = mqtt.publishData(topic + "/" + device);
            payloadYaw.writeUTF(device);
            payloadYaw.writeDouble(yaw);
            payloadYaw.writeDouble(pitch);
            payloadYaw.close();
            Main.logln("Publishing... " + device + " " + yaw + " " + pitch);
            return true;
        } 
        catch (MQTTException exe) {
            exe.printStackTrace();
            Main.logln("Error occurred when publishing data!");
        }
        return false;  
    }
    
    /**
     * Publish a single string to the predefined topic.
     * Currently not used as no string outputs are used.
     *
     * @param payload The contents to publish
     * @return Successfully or not
     */
    @Deprecated
    public boolean publishString(String payload) {
        try {
            Main.logln("Publishing... " + payload);
            mqtt.publishString(topic, payload);
            return true;
        } 
        catch (MQTTException exe) {
            exe.printStackTrace();
            Main.logln("Error occurred when publishing data!");
            Main.logln("Payload = " + payload);
        }
        return false;
    }

    /**
     * Closes connections with the MQTT Server
     */
    public void close() {
        try {
            mqtt.close();
        } catch (MQTTException exe) {
            exe.printStackTrace();
            Main.logln("Failed to close connection with MQTT Server.");
        }
    }

    /**
     * Print out server connection details
     */
    public void printServerDetails() {
        Main.logln("Server URL: " + server + ":" + port + ", on topic '" + topic + "'");
    }
}
