package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import api.postcode.PostcodeAPI;
import api.postcode.PostcodeResponse;
import api.postcode.PostcodeResult;

import java.util.List;
import java.io.IOException;

/**
 * Test class for the PostcodeAPI class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
class PostcodeAPITest {
    // Test coordinates for London:
    private static final double VALID_LATITUDE = 51.508045;
    private static final double VALID_LONGITUDE = -0.128217;
    
    // Test easting and northing coordinates for London area:
    private static final int VALID_EASTING = 530932;
    private static final int VALID_NORTHING = 179638;
    
    // Invalid coordinates:
    private static final double INVALID_LATITUDE = 0.0;
    private static final double INVALID_LONGITUDE = 0.0;
    
    // Invalid easting and northing coordinates:
    private static final int INVALID_EASTING = -1;
    private static final int INVALID_NORTHING = -1;
    
    @Test
    void testFetchPostcodesByLatitudeLongitude_ValidCoordinates() {
        try {
            PostcodeResponse response = PostcodeAPI.fetchPostcodesByLatitudeLongitude(VALID_LATITUDE, VALID_LONGITUDE);
            
            assertNotNull(response, "Response should not be null.");
            assertEquals(200, response.getStatus(), "Status should be 200.");
            
            List<PostcodeResult> results = response.getResult();
            assertNotNull(results, "Results should not be null.");
            assertFalse(results.isEmpty(), "Results should not be empty.");

            System.out.println(results);
            
            // Check that at least one result contains expected data for London.
            boolean foundLondon = results.stream()
                .anyMatch(result -> "England".equals(result.getCountry()) && result.getRegion() != null && result.getRegion().contains("London"));
            
            assertTrue(foundLondon, "Results should contain at least one entry for London.");
            
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    void testFetchPostcodesByLatitudeLongitude_InvalidCoordinates() {
        try {
            PostcodeResponse response = PostcodeAPI.fetchPostcodesByLatitudeLongitude(INVALID_LATITUDE, INVALID_LONGITUDE);
            
            if (response != null) {
                assertEquals(200, response.getStatus());
                
                List<PostcodeResult> results = response.getResult();
                assertTrue(results == null || results.isEmpty(), "Results should be null or empty for invalid coordinates.");
            } else {
                // If null response, test passes as we expect no results for invalid coordinates.
                assertTrue(true);
            }
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    void testFetchPostcodesByEastingNorthing_ValidCoordinates() {
        try {
            PostcodeResponse response = PostcodeAPI.fetchPostcodesByEastingNorthing(VALID_EASTING, VALID_NORTHING);
            
            assertNotNull(response, "Response should not be null.");
            assertEquals(200, response.getStatus(), "Status should be 200.");
            
            List<PostcodeResult> results = response.getResult();
            assertNotNull(results, "Results should not be null.");
            assertFalse(results.isEmpty(), "Results should not be empty.");
            
            // Check that at least one result contains expected data for London:
            boolean foundLondon = results.stream()
                .anyMatch(result -> "England".equals(result.getCountry()) && result.getRegion() != null && result.getRegion().contains("London"));
            
            assertTrue(foundLondon, "Results should contain at least one entry for London.");
            
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    void testFetchPostcodesByEastingNorthing_InvalidCoordinates() {
        try {
            PostcodeResponse response = PostcodeAPI.fetchPostcodesByEastingNorthing(INVALID_EASTING, INVALID_NORTHING);
            
            if (response != null) {
                assertEquals(200, response.getStatus());
                
                List<PostcodeResult> results = response.getResult();
                assertTrue(results == null || results.isEmpty(), "Results should be null or empty for invalid coordinates.");
            } else {
                // If null response, test passes as we expect no results for invalid coordinates.
                assertTrue(true);
            }
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    void testResponseContainsExpectedFields() {
        try {
            PostcodeResponse response = PostcodeAPI.fetchPostcodesByLatitudeLongitude(VALID_LATITUDE, VALID_LONGITUDE);
            
            assertNotNull(response, "Response should not be null.");
            List<PostcodeResult> results = response.getResult();
            assertFalse(results.isEmpty(), "Results should not be empty.");
            
            PostcodeResult result = results.get(0);
            
            assertNotNull(result.getPostcode(), "Postcode should not be null.");
            assertNotNull(result.getCountry(), "Country should not be null.");
            assertNotNull(result.getRegion(), "Region should not be null.");
            
            // Check geographic data is valid
            assertTrue(result.getLatitude() != 0, "Latitude should not be zero.");
            assertTrue(result.getLongitude() != 0, "Longitude should not be zero.");
            
        } catch (IOException | InterruptedException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}