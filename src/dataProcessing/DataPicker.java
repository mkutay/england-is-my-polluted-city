package dataProcessing;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Class to dynamically load pollution data from both pollutant and year using a Java properties file.
 *
 * @author Matthias Loong and Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class DataPicker {
    private static final Properties pollutantPatterns = new Properties();
    private static final String DATA_FOLDER = "UKAirPollutionData/";
    private static final String PROPERTIES_FILE = "src/csvpatterns.properties";
    private static final String USER_DIR = System.getProperty("user.dir");

    // Cache for available years for each pollutant:
    private final HashMap<Pollutant, List<Integer>> availableYearsCache;

    /**
     * Constructor to load the properties file.
     * @author Mehmet Kutay Bozkurt
     */
    public DataPicker() {
        loadProperties();
        this.availableYearsCache = new HashMap<>();
    }
    
    /**
     * Method that takes in the year and pollutant requested and returns the corresponding dataset.
     * @param year The year requested as an integer (e.g 2023).
     * @param pollutant The pollutant as an enum of Pollutant.
     * @return The loaded DataSet object containing all pollution data for the specified pollutant and year.
     * @author Matthias Loong
     */
    public DataSet getPollutantData(int year, Pollutant pollutant) {
        String pollutantPattern = pollutantPatterns.getProperty(pollutant.toString());
        if (pollutantPattern == null) {
            throw new IllegalArgumentException("Pollutant pattern does not exist for pollutant: " + pollutant);
        }

        System.out.println("Pollutant that is being loaded: " + pollutant);
        System.out.println("Pollutant pattern: " + pollutantPattern);

        String pollutantCSVFilename = String.format(pollutantPattern, year);
        DataLoader loader = new DataLoader();
        DataSet dataSet = loader.loadDataFile(USER_DIR + "/" + DATA_FOLDER + pollutant + "/" + pollutantCSVFilename);

        return dataSet;
    }
    
    /**
     * Load CSV patterns from properties file.
     * @author Mehmet Kutay Bozkurt
     */
    private void loadProperties() {
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
            pollutantPatterns.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: ", e);
        }
    }
    
    /**
     * Get a list of available years for a specific pollutant.
     * @param pollutant The pollutant to check for available years.
     * @return An array of available years.
     * @author Mehmet Kutay Bozkurt
     */
    public List<Integer> getAvailableYears(Pollutant pollutant) {
        String pollutantPattern = pollutantPatterns.getProperty(pollutant.toString());
        if (pollutantPattern == null) {
            throw new IllegalArgumentException("Pollutant pattern does not exist for pollutant: " + pollutant);
        }

        if (availableYearsCache.containsKey(pollutant)) {
            return availableYearsCache.get(pollutant);
        }
        
        List<Integer> years = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(DATA_FOLDER + pollutant))) {
            paths.filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .forEach(filename -> {
                    // Replace the string format "%s" in pollutantPattern with "(\d+)" (meaning multiple digits) to make it regex compatible.
                    // Then extract the year from the filename.
                    Matcher matcher = Pattern.compile(String.format(pollutantPattern, "(\\d+)")).matcher(filename);
                    if (matcher.matches()) {
                        years.add(Integer.parseInt(matcher.group(1)));
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to get available years: ", e);
        }

        availableYearsCache.put(pollutant, years);
        
        return years;
    }
}
