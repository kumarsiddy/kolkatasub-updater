package freakydevelopers.contorloverad.Pojo;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 1/18/2018.
 */

public class Station implements Serializable {
    private String stationName;
    private String stationCode;
    private int stationId;
    private int distance;
    private String arrival;
    private int datePlus;

    public Station() {
    }

    public Station(String stationName, String stationCode, int stationId, int distance, String arrival, int datePlus) {
        this.stationName = stationName;
        this.stationCode = stationCode;
        this.stationId = stationId;
        this.distance = distance;
        this.arrival = arrival;
        if (datePlus == 1)
            this.datePlus = 0;
        else if (datePlus == 2)
            this.datePlus = 1;
        else if (datePlus == 3)
            this.datePlus = 2;
        else if (datePlus == 4)
            this.datePlus = 3;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public int getDatePlus() {
        return datePlus;
    }

    public void setDatePlus(int datePlus) {
        this.datePlus = datePlus;
    }
}
