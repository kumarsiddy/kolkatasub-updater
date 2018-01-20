package freakydevelopers.contorloverad.Pojo;

import java.io.Serializable;

/**
 * Created by PURUSHOTAM on 1/18/2018.
 */

public class Train implements Serializable {

    private String trainName;
    private String trainNo;
    private TrainDay trainDay;

    public Train() {
    }

    public Train(String trainName, String trainNo, String runningDay) {
        this.trainName = trainName;
        this.trainNo = trainNo;
        trainDay = new TrainDay(runningDay);
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

}
