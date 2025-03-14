package api.postcode;

/**
 * Represents a postcode result from the postcodes.io API. This is used to display information
 * about a location.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class PostcodeResult {
    private String postcode;
    private int quality;
    private int eastings;
    private int northings;
    private String country;
    private String nhs_ha;
    private double longitude;
    private double latitude;
    private String european_electoral_region;
    private String primary_care_trust;
    private String region;
    private String lsoa;
    private String msoa;
    private String incode;
    private String outcode;
    private String parliamentary_constituency;
    private String parliamentary_constituency_2024;
    private String admin_district;
    private String parish;
    private String admin_county;
    private String date_of_introduction;
    private String admin_ward;
    private String ced;
    private String ccg;
    private String nuts;
    private String pfa;
    private CodeData codes;
    private double distance;

    // Getters:
    public String getPostcode() { return postcode; }
    public int getQuality() { return quality; }
    public int getEastings() { return eastings; }
    public int getNorthings() { return northings; }
    public String getCountry() { return country; }
    public String getNhs_ha() { return nhs_ha; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
    public String getEuropean_electoral_region() { return european_electoral_region; }
    public String getPrimary_care_trust() { return primary_care_trust; }
    public String getRegion() { return region; }
    public String getLsoa() { return lsoa; }
    public String getMsoa() { return msoa; }
    public String getIncode() { return incode; }
    public String getOutcode() { return outcode; }
    public String getParliamentary_constituency() { return parliamentary_constituency; }
    public String getParliamentary_constituency_2024() { return parliamentary_constituency_2024; }
    public String getAdmin_district() { return admin_district; }
    public String getParish() { return parish; }
    public String getAdmin_county() { return admin_county; }
    public String getDate_of_introduction() { return date_of_introduction; }
    public String getAdmin_ward() { return admin_ward; }
    public String getCed() { return ced; }
    public String getCcg() { return ccg; }
    public String getNuts() { return nuts; }
    public String getPfa() { return pfa; }
    public CodeData getCodes() { return codes; }
    public double getDistance() { return distance; }

    @Override
    public String toString() {
        return "PostcodeResult{" +
            "postcode='" + postcode + '\'' +
            ", country='" + country + '\'' +
            ", region='" + region + '\'' +
            '}';
    }
}
