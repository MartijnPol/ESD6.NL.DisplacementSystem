package domain;

import java.util.Date;

public class recievedCarTrackerRule {

    private String id;
    private double lat;
    private double lon;
    private Date date;
    private  Long mdriven;

    public recievedCarTrackerRule() {
    }

    public recievedCarTrackerRule(String id, double lat, double lon, Date date, Long mdriven) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
        this.mdriven = mdriven;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getMdriven() {
        return mdriven;
    }

    public void setMdriven(Long mdriven) {
        this.mdriven = mdriven;
    }
}
