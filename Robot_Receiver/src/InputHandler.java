import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.awt.MouseInfo;

/**
 * This class takes input data from the MQTT_Receiver class and 
 * uses them to simulate input events
 * @author Huy Nguyen
 */
public class InputHandler {
    
    private Robot input;
    
    /**
     * Constructor for the InputHandler class.
     * Creates a new input simulation object
     * @throws AWTException 
     */
    public InputHandler() throws AWTException {
        input = new Robot();
    }
    
    /**
     * Handles keypress/keyrelease input.
     * @param device Assures that the incoming data is for buttons
     * @param button the button involved.
     * @param state the state of the button.
     */
    public void simulateKeyInput(String device, String button, boolean state) {
        assert device != "Joystick" : "simulateKeyInput gets wrong device";
        switch(button) {
            case "UP":
                if(state) {input.keyPress(KeyEvent.VK_W);}
                else {input.keyRelease(KeyEvent.VK_W);}
                break;
            case "DOWN":
                if(state) {input.keyPress(KeyEvent.VK_S);}
                else {input.keyRelease(KeyEvent.VK_S);}
                break;
            case "LEFT":
                if(state) {input.keyPress(KeyEvent.VK_A);}
                else {input.keyRelease(KeyEvent.VK_A);}
                break;
            case "RIGHT":
                if(state) {input.keyPress(KeyEvent.VK_D);}
                else {input.keyRelease(KeyEvent.VK_D);}
                break;
            case "FIRE":
                if(state) {input.keyPress(KeyEvent.VK_SPACE);}
                else {input.keyRelease(KeyEvent.VK_SPACE);}
                break;
            case "SW3":
                if(state) {input.mousePress(KeyEvent.BUTTON1_MASK);}
                else {input.mouseRelease(KeyEvent.BUTTON1_MASK);}
                break;
            case "SW2":
                if(state) {input.mousePress(KeyEvent.BUTTON3_MASK);}
                else {input.mouseRelease(KeyEvent.BUTTON3_MASK);}
                break;
        }
    }
    
    /**
     * Handles mouse movement
     * @param device Assures that the data incoming is for mouse movement
     * @param yaw horizontal axis data
     * @param pitch vertical axis data
     */
    public void simulateMouseInput(String device, double yaw, double pitch) {
        assert device != "Mouse" : "simulateMouseInput gets wrong device";
        int x = MouseInfo.getPointerInfo().getLocation().x;
        x += yaw;
        int y = MouseInfo.getPointerInfo().getLocation().y;
        pitch = -(pitch*50);
        y += pitch;
        input.mouseMove(x, y);
        Main.logln("Changing mouse position by: " + x + " " + y);
        
    }
}
