/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utlis;

import java.util.regex.Pattern;

/**
 *
 * @author vidur
 */
public class Validator {
    // Regular expression patterns for validation
    private static final String ID_PATTERN = "\\d+"; // Matches one or more digits
    private static final String DOUBLE_PATTERN = "-?\\d+(\\.\\d+)?"; // Matches a floating-point number
//    private static final String BARCODE_PATTERN = "\\d{12}"; // Matches a 12-digit barcode//    
    private static final String BARCODE_PATTERN = "\\d+"; // Matches a digit barcode

    private static final String NAME_PATTERN = ".+"; // Matches one or more characters
    
     public static boolean isValidProductID(String input) {
        return Pattern.matches(ID_PATTERN, input);
    }

    public static boolean isValidBarcode(String input) {
        return Pattern.matches(BARCODE_PATTERN, input); //unlimited barcode numbers
    }

    public static boolean isValidDouble(String input) {
        return Pattern.matches(DOUBLE_PATTERN, input);
    }

    public static boolean isValidName(String input) {
        return Pattern.matches(NAME_PATTERN, input);
    }

    public static boolean isValidEmail(String string) {
        return true;
    }

    public static boolean isValidPassword(String password) {
        return true;
    }
   
}
