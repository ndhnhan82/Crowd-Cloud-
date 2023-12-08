package model;

public class Favorite {

    private String location;
    private double lat;
    private double longitude;

    private String key;

    public Favorite(String location, double lat, double longitude) {
        this.location = location;
        this.lat = lat;
        this.longitude = longitude;
    }

    public Favorite(){

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "location='" + location + '\'' +
                ", lat=" + lat +
                ", longitude=" + longitude +
                ", key='" + key + '\'' +
                '}';
    }
}
