package freakydevelopers.contorloverad.Pojo;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 1/18/2018.
 */

public class Train implements Serializable {

    private String trainName;
    private String trainNo;
    private TrainDay trainDay;
    private String linkToSchedule;

    public Train() {
    }

    public Train(String trainName, String trainNo, String runningDay, String linkToSchedule) {
        this.trainName = trainName;
        this.trainNo = trainNo;
        trainDay = new TrainDay(runningDay);
        this.linkToSchedule = linkToSchedule;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public TrainDay getTrainDay() {
        return trainDay;
    }

    public void setTrainDay(TrainDay trainDay) {
        this.trainDay = trainDay;
    }

    public String getLinkToSchedule() {
        return linkToSchedule;
    }

    public void setLinkToSchedule(String linkToSchedule) {
        this.linkToSchedule = linkToSchedule;
    }
}
