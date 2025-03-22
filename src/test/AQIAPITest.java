package test;

import api.aqicn.AQICNAPI;
import api.aqicn.AQICNData;
import api.aqicn.AQIResponse;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests valid and invalid inputs for the AQI API
 * @author Matthias Loong
 */
public class AQIAPITest {
    //Test coordinates for London:
    private static final double VALID_LATITUDE = 51.508045;
    private static final double VALID_LONGITUDE = -0.128217;


    //Invalid coordinates:
    private static final double INVALID_LATITUDE = 0.0;
    private static final double INVALID_LONGITUDE = 0.0;





    @Test
    void testAQIAPIValidCoordinates(){
        try {
            AQICNData data = AQICNAPI.getPollutionData(VALID_LATITUDE, VALID_LONGITUDE).getData();
            String status = AQICNAPI.getPollutionData(VALID_LATITUDE, VALID_LONGITUDE).getStatus();
            assertNotNull(status, "Response should not be null.");
            assertEquals("ok", status, "Status should be ok.");
            //Test if the data is null
            assertNotNull(data, "Results should not be null.");
            //Test for pollutant values (must cast as a double for No2 value)
            double No2Value = (Double) data.getPollutantValues().getNo2().getIAQIValue();
            assertNotEquals(0, No2Value, "Value should not be 0");
            System.out.println(data);

        } catch (IOException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testAQIAPIInvalidCoordinates(){
        try {
            AQIResponse response = AQICNAPI.getPollutionData(INVALID_LATITUDE, INVALID_LONGITUDE);

            //Response should be null if 0 is entered as a value
            assertNull(response, "Response should be null for invalid coordinates.");


        } catch (IOException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
