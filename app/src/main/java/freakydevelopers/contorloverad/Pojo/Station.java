package freakydevelopers.contorloverad.Pojo;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 1/18/2018.
 */

public class Station implements Serializable {
    private String stationName;
    private String stationCode;

    public Station() {
    }

    public Station(String stationName, String stationCode) {
        this.stationName = stationName;
        this.stationCode = stationCode;
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
}
