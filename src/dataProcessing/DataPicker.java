package dataProcessing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class to dynamically load pollution data from both pollutant and year using a Java properties file.
 *
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class DataPicker {
    private static final Properties pollutantPatterns = new Properties();

    /**
     * Method that takes in the year and pollutant requested and returns the corresponding dataset.
     * @param year The year requested as an integer (e.g 2023).
     * @param pollutant The pollutant as an enum of Pollutant.
     * @return The loaded DataSet object containing all pollution data for the specified pollutant and year.
     */
    public static DataSet getPollutantData(int year, Pollutant pollutant) {
        // Load CSV patterns from properties file.
        try (FileInputStream input = new FileInputStream("src/csvpatterns.properties")) {
            pollutantPatterns.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: ", e);
        }

        String pollutantPattern = pollutantPatterns.getProperty(pollutant.toString());
        System.out.println(pollutant);
        System.out.println(pollutantPattern);
        if (pollutantPattern == null) {
            throw new IllegalArgumentException("Pollutant Pattern Does Not Exist: " + pollutant);
        }
        String pollutantCSVFilename = String.format(pollutantPattern, year);
        DataLoader loader = new DataLoader();
        DataSet dataSet = loader.loadDataFile("UKAirPollutionData/" + pollutant + "/" + pollutantCSVFilename);

        return dataSet;
    }
}
