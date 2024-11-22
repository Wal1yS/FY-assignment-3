import java.util.Scanner;
 /** This is our main class "SmartHomeManagementSystem" where
 * we read command from the console and execute it
 * @author Valerii Tiniakov */
public class SmartHomeManagementSystem {
     /**
      * const variable for number of heaters
      */
     public static final int NUM_OF_HEATERS = 4;
     /**
      * const variable for number of cameras
      */
     public static final int NUM_OF_CAMERAS = 2;
     /**
      * const variable for number of lights
      */
     public static final int NUM_OF_LIGHTS = 4;
     /**
      * const variable for default camera angle
      */
     public static final int DEFAULT_ANGLE = 45;
     /**
      * const variable for default heater temperature
      */
     public static final int DEFAULT_TEMPERATURE = 20;
     /**
      * const variable with number of arguments of command type:
      * {command} {deviceName} {deviceId}
      */
     public static final int NUMBER_OF_ARG1 = 3;
     /**
      * const variable with number of arguments of command type:
      * {command} {deviceName} {deviceId} {attribute}
      */
     public static final int NUMBER_OF_ARG2 = 4;
     /**
      * const variable for getting 1st argument of command from separated string
      */
     public static final int ARG1 = 0;
     /**
      * const variable for getting 2nd argument of command from separated string
      */
     public static final int ARG2 = 1;
     /**
      * const variable for getting 3rd argument of command from separated string
      */
     public static final int ARG3 = 2;
     /**
      * const variable for getting 4th argument of command from separated string
      */
     public static final int ARG4 = 3;
     /**
      * array of smart devices of type Light
      */
     private static Light[] light = new Light[NUM_OF_LIGHTS];
     /**
      * array of smart devices of type Camera
      */
     private static Camera[] camera = new Camera[NUM_OF_CAMERAS];
     /**
      * array of smart devices of type Heater
      */
     private static Heater[] heater = new Heater[NUM_OF_HEATERS];

     /**
      * main function where the command processes and executes
      *
      * @param args - arguments
      */
     public static void main(String[] args) {
         for (int i = 0; i < NUM_OF_LIGHTS; i++) {
             light[i] = new Light(SmartDevice.Status.ON, false, Light.BrightnessLevel.LOW, Light.LightColor.YELLOW);
             light[i].setDeviceId(i);
         }
         for (int i = 0; i < NUM_OF_CAMERAS; i++) {
             camera[i] = new Camera(SmartDevice.Status.ON, false, false, DEFAULT_ANGLE);
             camera[i].setDeviceId(i + NUM_OF_LIGHTS);
         }
         for (int i = 0; i < NUM_OF_HEATERS; i++) {
             heater[i] = new Heater(SmartDevice.Status.ON, DEFAULT_TEMPERATURE);
             heater[i].setDeviceId(i + NUM_OF_LIGHTS + NUM_OF_CAMERAS);
         }
         Scanner scanner = new Scanner(System.in);
         String line = scanner.nextLine();
         while (!line.equals("end")) {
             String[] partscomm = line.split(" ");
             if ((partscomm[ARG1].equals("DisplayAllStatus")) && (partscomm.length == 1)) {
                 for (int i = 0; i < NUM_OF_LIGHTS; i++) {
                     String str = light[i].displayStatus();
                     System.out.println(str);
                 }
                 for (int i = 0; i < NUM_OF_CAMERAS; i++) {
                     String str = camera[i].displayStatus();
                     System.out.println(str);
                 }
                 for (int i = 0; i < NUM_OF_HEATERS; i++) {
                     String str = heater[i].displayStatus();
                     System.out.println(str);
                 }
                 line = scanner.nextLine();
                 continue;
             }
             if (((partscomm.length == NUMBER_OF_ARG1)
                     && (checkCommand(partscomm[ARG1], partscomm[ARG2], partscomm[ARG3], "st")))
                     || ((partscomm.length == NUMBER_OF_ARG2)
                     && (checkCommand(partscomm[ARG1], partscomm[ARG2], partscomm[ARG3], partscomm[ARG4])))) {
                 if (checkDevice(partscomm[ARG2], partscomm[ARG3])) {
                     switch (partscomm[ARG1]) {
                         case "TurnOn":
                             System.out.println(caseTurnOn(partscomm[ARG2], partscomm[ARG3]));
                             break;
                         case "TurnOff":
                             System.out.println(caseTurnOff(partscomm[ARG2], partscomm[ARG3]));
                             break;
                         case "StartCharging":
                             System.out.println(caseStartCharging(partscomm[ARG2], partscomm[ARG3]));
                             break;
                         case "StopCharging":
                             System.out.println(caseStopCharging(partscomm[ARG2], partscomm[ARG3]));
                             break;
                         case "SetTemperature":
                             System.out.println(caseSetTemperature(partscomm[ARG2],
                                     partscomm[ARG3], Integer.parseInt(partscomm[ARG4])));
                             break;
                         case "SetBrightness":
                             System.out.println(caseSetBrightnessLevel(partscomm[ARG2],
                                     partscomm[ARG3], partscomm[ARG4]));
                             break;
                         case "SetColor":
                             System.out.println(caseSetColor(partscomm[ARG2],
                                     partscomm[ARG3], partscomm[ARG4]));
                             break;
                         case "SetAngle":
                             System.out.println(caseSetAngle(partscomm[ARG2],
                                     partscomm[ARG3], Integer.parseInt(partscomm[ARG4])));
                             break;
                         case "StartRecording":
                             System.out.println(caseStartRecording(partscomm[ARG2], partscomm[ARG3]));
                             break;
                         case "StopRecording":
                             System.out.println(caseStopRecording(partscomm[ARG2], partscomm[ARG3]));
                             break;
                         default:
                             break;
                     }
                 } else {
                     System.out.println("The smart device was not found");
                 }
             } else {
                 System.out.println("Invalid command");
             }
             line = scanner.nextLine();
         }
     }

     /**
      * function that is checking command for invalid command name or non-integer values
      * in id or attribute
      *
      * @param command   - the command itself, like "TurnOn" etc.
      * @param name      - the name of smart device
      * @param id        - id of smart device
      * @param attribute - the attribute of command, like temperature or light color
      * @return true or false, depend on command structure validity
      */
     public static boolean checkCommand(String command, String name, String id, String attribute) {
         if (command.equals("TurnOn") || command.equals("TurnOff") || command.equals("StartCharging")
                 || command.equals("StopCharging") || command.equals("StartRecording")
                 || command.equals("StopRecording")) {
             return (!name.isEmpty() && !id.isEmpty() && id.matches("-?\\d+") && attribute.equals("st"));
         } else if (command.equals("SetTemperature") || command.equals("SetBrightness")
                 || command.equals("SetColor") || command.equals("SetAngle")) {
             if ((command.equals("SetTemperature") || command.equals("SetAngle"))
                     && (name != null && id != null && attribute != null && id.matches("-?\\d+")
                     && !(attribute.equals("st")) && attribute.matches("-?\\d+"))) {
                 return true;
             }
             if ((command.equals("SetBrightness") || command.equals("SetColor"))
                     && (name != null && id != null && attribute != null && id.matches("-?\\d+")
                     && !(attribute.equals("st")))) {
                 return true;
             }
             return false;
         }
         return false;
     }

     /**
      * function that is checking name and id of smart device for correctness
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return true or false, depend on name and id validity
      */
     public static boolean checkDevice(String name, String id) {
         if (name.equals("Camera") || name.equals("Heater") || name.equals("Light")) {
             switch (name) {
                 case "Camera":
                     for (int i = 0; i < NUM_OF_CAMERAS; i++) {
                         if (camera[i].getDeviceId() == Integer.parseInt(id)) {
                             return true;
                         }
                     }
                     return false;
                 case "Heater":
                     for (int i = 0; i < NUM_OF_HEATERS; i++) {
                         if (heater[i].getDeviceId() == Integer.parseInt(id)) {
                             return true;
                         }
                     }
                     return false;
                 case "Light":
                     for (int i = 0; i < NUM_OF_LIGHTS; i++) {
                         if (light[i].getDeviceId() == Integer.parseInt(id)) {
                             return true;
                         }
                     }
                     return false;
                 default:
                     return false;
             }
         }
         return false;
     }

     /**
      * function that is checking smart device for being turn on already
      * before turning it on.
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return true or false, depend on was it turn on already or not
      */
     public static boolean checkForTurnOn(String name, String id) {
         if (name.equals("Camera")) {
             if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].turnOn()) {
                 return true;
             }
         } else if (name.equals("Heater")) {
             if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].turnOn()) {
                 return true;
             }
         } else if (name.equals("Light")) {
             if (light[Integer.parseInt(id)].turnOn()) {
                 return true;
             }
         }
         return false;
     }

     /**
      * function that is checking smart device for being turn off already
      * before turning it off.
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return true or false, depend on was it turn off already or not
      */
     public static boolean checkForTurnOff(String name, String id) {
         if (name.equals("Camera")) {
             if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].turnOff()) {
                 return true;
             }
         } else if (name.equals("Heater")) {
             if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].turnOff()) {
                 return true;
             }
         } else if (name.equals("Light")) {
             if (light[Integer.parseInt(id)].turnOff()) {
                 return true;
             }
         }
         return false;
     }

     /**
      * function that is turning device on if it passes the checker:
      * {@link #checkForTurnOn(String, String)}
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return message for completed command or error message
      */
     public static String caseTurnOn(String name, String id) {
         if (checkForTurnOn(name, id)) {
             return name + " " + id + " is on";
         } else {
             return name + " " + id + " is already on";
         }
     }

     /**
      * function that is turning device off if it passes the checker:
      * {@link #checkForTurnOff(String, String)}
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return message for completed command or error message
      */
     public static String caseTurnOff(String name, String id) {
         if (checkForTurnOff(name, id)) {
             return name + " " + id + " is off";
         } else {
             return name + " " + id + " is already off";
         }
     }

     /**
      * function that is checking if smart device is chargeable
      *
      * @param name - the name of smart device
      * @return true if it is or false if it isn't
      */
     public static boolean isChargeable(String name) {
         if (name.equals("Heater")) {
             return false;
         }
         return true;
     }

     /**
      * function that is checking smart device for being charging now
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return true or false, depend on is it charging now or not
      */
     public static boolean checkForStartCharging(String name, String id) {
         if (name.equals("Camera")) {
             if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].startCharging()) {
                 return true;
             }
         } else if (name.equals("Light")) {
             if (light[Integer.parseInt(id)].startCharging()) {
                 return true;
             }
         }
         return false;
     }

     /**
      * function that is checking smart device for being not charging now
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return true or false, depend on is it not charging now
      */
     public static boolean checkForStopCharging(String name, String id) {
         if (name.equals("Camera")) {
             if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].stopCharging()) {
                 return true;
             }
         } else if (name.equals("Light")) {
             if (light[Integer.parseInt(id)].stopCharging()) {
                 return true;
             }
         }
         return false;
     }

     /**
      * function that is starting charging the device if it passes the checker:
      * {@link #checkForStartCharging(String, String)}
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return message for completed command or error message
      */
     public static String caseStartCharging(String name, String id) {
         if (isChargeable(name)) {
             if (checkForStartCharging(name, id)) {
                 return name + " " + id + " is charging";
             } else {
                 return name + " " + id + " is already charging";
             }
         } else {
             return name + " " + id + " is not chargeable";
         }
     }

     /**
      * function that is stopping charging the device if it passes the checker:
      * {@link #checkForStopCharging(String, String)}
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return message for completed command or error message
      */
     public static String caseStopCharging(String name, String id) {
         if (isChargeable(name)) {
             if (checkForStopCharging(name, id)) {
                 return name + " " + id + " stopped charging";
             } else {
                 return name + " " + id + " is not charging";
             }
         } else {
             return name + " " + id + " is not chargeable";
         }
     }

     /**
      * function that is checking smart device for being heater, being turn on
      * and the valid temperature attribute. If all is ok, function set a new temperature for a device
      *
      * @param name        - the name of smart device
      * @param id          - id of smart device
      * @param temperature - int value for temperature that is need to be set
      * @return message for completed command or error message
      */
     public static String caseSetTemperature(String name, String id, int temperature) {
         switch (name) {
             case "Camera":
                 if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].isOn()) {
                     return name + " " + id + " is not a heater";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Light":
                 if (light[Integer.parseInt(id)].isOn()) {
                     return name + " " + id + " is not a heater";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Heater":
                 if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].isOn()) {
                     if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].setTemperature(temperature)) {
                         return name + " " + id + " temperature is set to " + temperature;
                     } else {
                         return "Heater " + id + " temperature should be in the range [15, 30]";
                     }
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             default:
                 return " ";
         }
     }

     /**
      * function that is checking smart device for being light, being turn on
      * and the valid brightness level attribute. If all is ok, function set a new brightness level for a device
      *
      * @param name            - the name of smart device
      * @param id              - id of smart device
      * @param brightnessLevel - the brightness level that is need to be set
      * @return message for completed command or error message
      */
     public static String caseSetBrightnessLevel(String name, String id, String brightnessLevel) {
         switch (name) {
             case "Camera":
                 if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].isOn()) {
                     return name + " " + id + " is not a light";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Light":
                 if (light[Integer.parseInt(id)].isOn()) {
                     switch (brightnessLevel) {
                         case "HIGH":
                             light[Integer.parseInt(id)].setBrightnessLevel(Light.BrightnessLevel.HIGH);
                             return name + " " + id + " brightness level is set to " + Light.BrightnessLevel.HIGH;
                         case "MEDIUM":
                             light[Integer.parseInt(id)].setBrightnessLevel(Light.BrightnessLevel.MEDIUM);
                             return name + " " + id + " brightness level is set to " + Light.BrightnessLevel.MEDIUM;
                         case "LOW":
                             light[Integer.parseInt(id)].setBrightnessLevel(Light.BrightnessLevel.LOW);
                             return name + " " + id + " brightness level is set to " + Light.BrightnessLevel.LOW;
                         default:
                             return "The brightness can only be one of \"LOW\", \"MEDIUM\", or \"HIGH\"";
                     }
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Heater":
                 if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].isOn()) {
                     return name + " " + id + " is not a light";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             default:
                 return " ";
         }
     }

     /**
      * function that is checking smart device for being light, being turn on
      * and the valid color attribute. If all is ok, function set a new color for a device
      *
      * @param name  - the name of smart device
      * @param id    - id of smart device
      * @param color - the color that is need to be set
      * @return message for completed command or error message
      */
     public static String caseSetColor(String name, String id, String color) {
         switch (name) {
             case "Camera":
                 if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].isOn()) {
                     return name + " " + id + " is not a light";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Light":
                 if (light[Integer.parseInt(id)].isOn()) {
                     switch (color) {
                         case "YELLOW":
                             light[Integer.parseInt(id)].setLightColor(Light.LightColor.YELLOW);
                             return name + " " + id + " color is set to " + Light.LightColor.YELLOW;
                         case "WHITE":
                             light[Integer.parseInt(id)].setLightColor(Light.LightColor.WHITE);
                             return name + " " + id + " color is set to " + Light.LightColor.WHITE;
                         default:
                             return "The light color can only be \"YELLOW\" or \"WHITE\"";
                     }
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Heater":
                 if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].isOn()) {
                     return name + " " + id + " is not a light";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             default:
                 return " ";
         }
     }

     /**
      * function that is checking smart device for being camera, being turn on
      * and the valid angle attribute. If all is ok, function set a new angle for a device
      *
      * @param name  - the name of smart device
      * @param id    - id of smart device
      * @param angle - the int value with angle that is need to be set
      * @return message for completed command or error message
      */
     public static String caseSetAngle(String name, String id, int angle) {
         switch (name) {
             case "Camera":
                 if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].isOn()) {
                     if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].setCameraAngle(angle)) {
                         return name + " " + id + " angle is set to " + angle;
                     } else {
                         return "Camera " + id + " angle should be in the range [-60, 60]";
                     }
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Light":
                 if (light[Integer.parseInt(id)].isOn()) {
                     return name + " " + id + " is not a camera";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Heater":
                 if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].isOn()) {
                     return name + " " + id + " is not a camera";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             default:
                 return "";
         }
     }

     /**
      * function that is checking smart device for being camera, being turn on
      * and being recording already. If all is ok, function make camera starts recording
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return message for completed command or error message
      */
     public static String caseStartRecording(String name, String id) {
         switch (name) {
             case "Camera":
                 if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].isOn()) {
                     if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].startRecording()) {
                         return name + " " + id + " started recording";
                     } else {
                         return name + " " + id + " is already recording";
                     }
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Light":
                 if (light[Integer.parseInt(id)].isOn()) {
                     return name + " " + id + " is not a camera";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Heater":
                 if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].isOn()) {
                     return name + " " + id + " is not a camera";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             default:
                 return "";
         }
     }

     /**
      * function that is checking smart device for being camera, being turn on
      * and being not recording already. If all is ok, function make camera stops recording
      *
      * @param name - the name of smart device
      * @param id   - id of smart device
      * @return message for completed command or error message
      */
     public static String caseStopRecording(String name, String id) {
         switch (name) {
             case "Camera":
                 if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].isOn()) {
                     if (camera[Integer.parseInt(id) - NUM_OF_LIGHTS].stopRecording()) {
                         return name + " " + id + " stopped recording";
                     } else {
                         return name + " " + id + " is not recording";
                     }
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Light":
                 if (light[Integer.parseInt(id)].isOn()) {
                     return name + " " + id + " is not a camera";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             case "Heater":
                 if (heater[Integer.parseInt(id) - NUM_OF_LIGHTS - NUM_OF_CAMERAS].isOn()) {
                     return name + " " + id + " is not a camera";
                 } else {
                     return "You can't change the status of the " + name + " " + id + " while it is off";
                 }
             default:
                 return "";
         }
     }
     /** Constructor*/
     public SmartHomeManagementSystem() { };
}
/** Interface with turnOff, turnOn and isOn methods that will be used in:
 * @see SmartDevice */
interface Controllable {
    /** Function that turning off a smart device
     * @return true or false, depend on device status */
    boolean turnOff();
    /** Function that turning on a smart device
     * @return true or false, depend on device status */
    boolean turnOn();
    /** Function that is checking the status of the smart
     * @return true or false, depend on device status */
    boolean isOn();
}

/** Interface with startCharging, stopCharging and isCharging methods that will be used in:
 * @see Light
 * @see Camera
 * */
interface Chargeable {
    /** Function that checking the current charging status of the devise
     * @return true or false, depend on device charging status*/
    boolean isCharging();
    /** Function that makes device start charging
     * @return true or false, depend on device charging status*/
    boolean startCharging();
    /** Function that makes device stop charging
     * @return true or false, depend on device charging status */
    boolean stopCharging();
}
/** Abstract class that is a parent-class to all smart devices, includes main methods and
 * fields that every smart devise should have*/
abstract class SmartDevice implements Controllable {
    /** enum with 2 possible status for every smart device*/
    enum Status {
        /** When device is turned off */
        OFF,
        /** When device is turned on */
        ON;
    }
    /** variable for containing the status of smart device*/
    private Status status;
    /** variable for containing the id of smart device*/
    private int deviceId;
    /** variable for containing the number of smart device*/
    private static int numberOfDevices;

    /** constructor for setting values in smart device
     * @param status - status of device*/
    public SmartDevice(Status status) {
        this.status = status;
    }
    /** Function that showing the full status of smart device
     * @return message with devise full status*/
    public String displayStatus() {
        return this.deviceId + " is " + this.status;
    }
    /** Function that getting smart device id
     * @return smart device id*/
    public int getDeviceId() {
        return deviceId;
    }
    /** Function that setting id for a smart device
     * @param deviceId - id of device*/
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
    /** Function that getting smart device status
     * @return smart device status*/
    public Status getStatus() {
        return status;
    }
    /** Function that setting new status for a smart device
     * @param status - status of device*/
    public void setStatus(Status status) {
        this.status = status;
    }
    /** Function that turning off a smart device after checking if it turned off already by using:
     * {@link #setStatus(Status)}
     * @return true or false, depend on device status*/
    public boolean turnOff() {
        if (!(this.isOn())) {
            return false;
        }
        this.setStatus(Status.OFF);
        return true;
    }
    /** Function that turning on a smart device after checking if it turned on already by using:
     * {@link #setStatus(Status)}
     * @return true or false, depend on device status*/
    public boolean turnOn() {
        if (this.isOn()) {
            return false;
        }
        this.setStatus(Status.ON);
        return true;
    }
    /** Function that is checking the status of the smart device by using:
     * {@link #getStatus()}
     * @return true or false, depend on device status*/
    public boolean isOn() {
        if (this.getStatus() == Status.ON) {
            return true;
        }
        return false;
    }
    /** Function that is checking the status of the smart device by using:
     * {@link #isOn()}
     * @return true or false, depend on device status*/
    public boolean checkStatusAccess() {
        return this.isOn();
    }
}
/** Class that extends all SmartDevice variables and method, but specify them
 * and add new one for Heater especially*/
class Heater extends SmartDevice {
    /** variable for temperature of the Heater*/
    private int temperature;
    /** const variable for max temperature of the Heater*/
    static final int MAX_HEATER_TEMP = 30;
    /** const variable for min temperature of the Heater*/
    static final int MIN_HEATER_TEMP = 15;

    /** constructor for setting values in Heater
     * @param status - status of device
     * @param temperature - temperature of heater*/
    public Heater(Status status, int temperature) {
        super(status);
        this.temperature = temperature;
    }
    /** Function that getting heater temperature
     * @return heater temperature*/
    public int getTemperature() {
        return this.temperature;
    }
    /** Function that setting a new temperature for a heater after checking temperature validity
     * @return true or false, depend on temperature validity
     * @param temperature - temperature of heater that is need to be set*/
    public boolean setTemperature(int temperature) {
        if ((temperature <= MAX_HEATER_TEMP) && (temperature >= MIN_HEATER_TEMP)) {
            this.temperature = temperature;
            return true;
        }
        return false;
    }
    /** Function that showing the full status of heater
     * @return message with heater full status*/
    public String displayStatus() {
        return "Heater " + super.displayStatus() + " and the temperature is " + this.temperature + ".";
    }

}
/** Class that extends all SmartDevice variables and method, but specify them
 * and add new one for Camera especially*/
class Camera extends SmartDevice implements Chargeable {
    /** const variable for max angle of the Camera*/
    static final int MAX_CAMERA_ANGLE = 60;
    /** const variable for min angle of the Camera*/
    static final int MIN_CAMERA_ANGLE = -60;
    /** variable for keeping the information about camera charging status */
    private boolean charging;
    /** variable for keeping the information about camera recording status */
    private boolean recording;
    /** variable for keeping the information about camera angle */
    private int angle;
    /** constructor for setting values in Camera
     * @param status - status of device
     * @param charging - device charging status
     * @param recording - device recording status
     * @param angle - camera angle that is need to be set*/
    public Camera(Status status, boolean charging, boolean recording, int angle) {
        super(status);
        this.charging = charging;
        this.recording = recording;
        this.angle = angle;
    }
    /** Function that getting camera angle
     * @return camera angle*/
    public int getAngle() {
        return angle;
    }
    /** Function that setting a new angle for a camera after checking angle validity
     * @return true or false, depend on angle validity
     * @param angle - camera angle that is need to be set*/
    public boolean setCameraAngle(int angle) {
        if ((angle >= MIN_CAMERA_ANGLE) && (angle <= MAX_CAMERA_ANGLE)) {
            this.angle = angle;
            return true;
        }
        return false;
    }
    /** Function that makes camera start recording after checking if it is recording already
     * {@link #isRecording()}
     * @return true or false, depend on camera recording status*/
    public boolean startRecording() {
        if (this.isRecording()) {
            return false;
        }
        this.recording = true;
        return true;
    }
    /** Function that makes camera stop recording after checking if it is not recording already
     * {@link #isRecording()}
     * @return true or false, depend on camera recording status*/
    public boolean stopRecording() {
        if (this.isRecording()) {
            this.recording = false;
            return true;
        }
        return false;
    }
    /** Function that checking the current recording status of the camera
     * @return true or false, depends on camera recording status*/
    public boolean isRecording() {
        if (recording) {
            return true;
        }
        return false;
    }
    /** Function that checking the current charging status of the camera
     * @return true or false, depends on camera charging status*/
    public boolean isCharging() {
        if (charging) {
            return true;
        }
        return false;
    }
    /** Function that makes camera start charging after checking if it is charging already
     * {@link #isCharging()}
     * @return true or false, depend on camera charging status*/
    public boolean startCharging() {
        if (this.isCharging()) {
            return false;
        }
        this.charging = true;
        return true;
    }
    /** Function that makes camera stop charging after checking if it is not charging already
     * {@link #isCharging()}
     * @return true or false, depend on camera charging status*/
    public boolean stopCharging() {
        if (this.isCharging()) {
            this.charging = false;
            return true;
        }
        return false;
    }
    /** Function that showing the full status of camera
     * @return message with camera full status*/
    public String displayStatus() {
        return "Camera " + super.displayStatus() + ", the angle is " + this.angle + ", the charging status is "
                + this.charging + ", and the recording status is " + this.recording + ".";
    }
}
/** Class that extends all SmartDevice variables and method, but specify them
 * and add new one for Light especially*/
class Light extends SmartDevice implements Chargeable {
    /** enum with 2 possible status of light color*/
    enum LightColor {
        /** When Light color is set to white */
        WHITE,
        /** When Light color is set to yellow */
        YELLOW;
    }
    /** enum with 3 possible status of brightness level*/
    enum BrightnessLevel {
        /** When Light brightness level is set to high */
        HIGH,
        /** When Light brightness level is set to medium */
        MEDIUM,
        /** When Light brightness level is set to low */
        LOW;
    }
    /** variable for keeping the information about light charging status */
    private boolean charging;
    /** variable for keeping the information about light brightness level */
    private BrightnessLevel brightnessLevel;
    /** variable for keeping the information about light color */
    private LightColor lightColor;

    /** constructor for setting values in Light
     * @param status - status of device
     * @param charging - device charging status
     * @param brightnessLevel - light brightness level
     * @param lightColor - light color*/
    public Light(Status status, boolean charging, BrightnessLevel brightnessLevel, LightColor lightColor) {
        super(status);
        this.charging = charging;
        this.brightnessLevel = brightnessLevel;
        this.lightColor = lightColor;
    }
    /** Function that getting light color of Light
     * @return light color*/
    public LightColor getLightColor() {
        return lightColor;
    }
    /** Function that setting a new light color for a light
     * @param lightColor - light color that is need to be set */
    public void setLightColor(LightColor lightColor) {
        this.lightColor = lightColor;
    }
    /** Function that getting brightness level of Light
     * @return brightness level*/
    public BrightnessLevel getBrightnessLevel() {
        return brightnessLevel;
    }
    /** Function that setting a new brightness level for a light
     * @param brightnessLevel - light brightness level that is need to be set*/
    public void setBrightnessLevel(BrightnessLevel brightnessLevel) {
        this.brightnessLevel = brightnessLevel;
    }
    /** Function that checking the current charging status of the light
     * @return true or false, depends on light charging status*/
    public boolean isCharging() {
        if (charging) {
            return true;
        }
        return false;
    }
    /** Function that makes light start charging after checking if it is charging already
     * {@link #isCharging()}
     * @return true or false, depend on light charging status*/
    public boolean startCharging() {
        if (this.isCharging()) {
            return false;
        }
        this.charging = true;
        return true;
    }
    /** Function that makes light stop charging after checking if it is not charging already
     * {@link #isCharging()}
     * @return true or false, depend on light charging status*/
    public boolean stopCharging() {
        if (this.isCharging()) {
            this.charging = false;
            return true;
        }
        return false;
    }
    /** Function that showing the full status of light
     * @return message with light full status*/
    public String displayStatus() {
        return "Light " + super.displayStatus() + ", the color is " + this.lightColor + ", the charging status is "
                + this.charging + ", and the brightness level is " + this.brightnessLevel + ".";
    }
}

