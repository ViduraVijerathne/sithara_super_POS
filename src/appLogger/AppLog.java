/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appLogger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author vidur
 */
@Data // Lombok annotation to generate getters, setters, toString, hashCode, equals methods
@AllArgsConstructor // Lombok annotation to generate constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate no-args constructor
public class AppLog {

    private String time;
    private String header;
    private String body;
    private String who;
    private LogType type;

    public static AppLog parse(String line) {
        // Split the line by the separator (assuming you're using toString() method to write logs)
        String[] parts = line.split(", ");
        if (parts.length != 5) {
            System.err.println("Invalid log entry: " + line);
            return null;
        }

        // Parse individual parts
        String time = parts[0].replace("AppLog(time=", "");
        String header = parts[1].replace("header=", "");
        String body = parts[2].replace("body=", "");

        String who = parts[3].replace("who=", "");
        

        // Extracting log type string
        String typeString = parts[4];
        // Removing parentheses from typeString
        typeString = typeString.replace(")", "").replace("(", "");
        // Trim any leading/trailing spaces
        typeString = typeString.trim();

        // Parse the LogType
        LogType type = null;

        try {
            type = LogType.valueOf(typeString.replace("type=", ""));
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid log type: " + typeString);
        }

        // Create and return the AppLog object
        return new AppLog(time, header, body, who, type);
    }

}
