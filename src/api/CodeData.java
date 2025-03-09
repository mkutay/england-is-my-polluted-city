package api;

/**
 * Represents the code data from the postcodes.io API. This is used to display information
 * about a location.
 * 
 * @author Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class CodeData {
    private String admin_district;
    private String admin_county;
    private String admin_ward;
    private String parish;
    private String parliamentary_constituency;
    private String parliamentary_constituency_2024;
    private String ccg;
    private String ccg_id;
    private String ced;
    private String nuts;
    private String lsoa;
    private String msoa;
    private String lau2;
    private String pfa;

    // Getters:
    public String getAdmin_district() { return admin_district; }
    public String getAdmin_county() { return admin_county; }
    public String getAdmin_ward() { return admin_ward; }
    public String getParish() { return parish; }
    public String getParliamentary_constituency() { return parliamentary_constituency; }
    public String getParliamentary_constituency_2024() { return parliamentary_constituency_2024; }
    public String getCcg() { return ccg; }
    public String getCcg_id() { return ccg_id; }
    public String getCed() { return ced; }
    public String getNuts() { return nuts; }
    public String getLsoa() { return lsoa; }
    public String getMsoa() { return msoa; }
    public String getLau2() { return lau2; }
    public String getPfa() { return pfa; }

    @Override
    public String toString() {
        return "CodeData{" +
            "admin_district='" + admin_district + '\'' +
            ", parliamentary_constituency='" + parliamentary_constituency + '\'' +
            ", admin_ward='" + admin_ward + '\'' +
            '}';
    }
}
