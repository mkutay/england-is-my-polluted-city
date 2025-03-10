package dataProcessing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class to dynamically load pollution data from both pollutant and year using a Java properties file.
 *
 * @author Matthias Loong
 * @version 1.0
 */
public class DataPicker {
    private static final Properties pollutantPatterns = new Properties();
    private static final String DATA_FOLDER = "UKAirPollutionData/";

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
        if (pollutantPattern == null) {
            throw new IllegalArgumentException("Pollutant pattern does not exist for pollutant: " + pollutant);
        }
        System.out.println("Pollutant that is being loaded: " + pollutant);
        System.out.println("Pollutant pattern: " + pollutantPattern);

        String pollutantCSVFilename = String.format(pollutantPattern, year);
        DataLoader loader = new DataLoader();
        DataSet dataSet = loader.loadDataFile(DATA_FOLDER + pollutant + "/" + pollutantCSVFilename);

        return dataSet;
    }
}
