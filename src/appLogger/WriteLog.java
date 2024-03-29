/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appLogger;

/**
 *
 * @author vidur
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WriteLog {

    public static final String fileName = "C:\\SitharaSuper\\log\\"+getFileName();

    public static String getFileName() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Format the date as YYYY_MM_DD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String formattedDate = currentDate.format(formatter);

        // Return the formatted date as the file name
        return formattedDate+".log";
    }
    public static String getFileName(LocalDate currentDate) {
        // Get the current date
        

        // Format the date as YYYY_MM_DD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String formattedDate = currentDate.format(formatter);

        // Return the formatted date as the file name
        return formattedDate+".log";
    }
    
    public static void log(AppLog appLog){
        try {
            // Create FileWriter in append mode
            FileWriter writer = new FileWriter(fileName, true);

            // Write the data to the file
            writer.write(appLog.toString()+ "\n");
            // Close the writer
            writer.close();

            System.out.println("Data appended to the file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending the data to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public  static List<AppLog> readLog(LocalDate currentDate){
        List<AppLog> logs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\SitharaSuper\\log\\"+getFileName(currentDate)))) {
            String line;
            while ((line = br.readLine()) != null) {
                logs.add(AppLog.parse(line));
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the log file: " + e.getMessage());
            e.printStackTrace();
        }

        return logs;
    }
    public  static List<AppLog> readLog(){
        List<AppLog> logs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                logs.add(AppLog.parse(line));
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the log file: " + e.getMessage());
            e.printStackTrace();
        }

        return logs;
    }
}
