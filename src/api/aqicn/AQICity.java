package api.aqicn;

import java.util.List;

/**
 * Represents data from the "City" field inside the data of a response
 * @author Matthias Loong
 * @version 1.0
 */
public class AQICity {
    private List<Double> geo;
    private String name;
    private String url;

    //Getters
    public List<Double> getGeo(){ return geo; }
    public String getName(){ return name; }
    public String getUrl(){ return url; }

    @Override
    public String toString(){
        return "AQICity{" +
                "geo=" + geo +
                ", name=" + name +
                ", url=" + url +
                '}';

    }

}


