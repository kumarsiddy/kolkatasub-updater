package freakydevelopers.contorloverad.Pojo;

import java.io.Serializable;

import static freakydevelopers.contorloverad.Utils.Constants.FRIDAY;
import static freakydevelopers.contorloverad.Utils.Constants.MONDAY;
import static freakydevelopers.contorloverad.Utils.Constants.SATURDAY;
import static freakydevelopers.contorloverad.Utils.Constants.SUNDAY;
import static freakydevelopers.contorloverad.Utils.Constants.THURSDAY;
import static freakydevelopers.contorloverad.Utils.Constants.TUESDAY;
import static freakydevelopers.contorloverad.Utils.Constants.WEDNESDAY;

/**
 * Created by PURUSHOTAM on 1/18/2018.
 */

public class TrainDay implements Serializable {
    private boolean mon, tue, wed, thu, fri, sat, sun;

    public TrainDay() {
    }

    //    M Tu W Th F Sa
    public TrainDay(String trainDay) {

        if (trainDay.equals("Daily")) {
            mon = true;
            tue = true;
            wed = true;
            thu = true;
            fri = true;
            sat = true;
            sun = true;
        } else {
            String[] days = trainDay.split(" ");
            for (String s : days) {
                if (!mon) {
                    mon = s.equals(MONDAY);
                }
                if (!tue) {
                    tue = s.equals(TUESDAY);
                }
                if (!wed) {
                    wed = s.equals(WEDNESDAY);
                }
                if (!thu) {
                    thu = s.equals(THURSDAY);
                }
                if (!fri) {
                    fri = s.equals(FRIDAY);
                }
                if (!sat) {
                    sat = s.equals(SATURDAY);
                }
                if (!sun) {
                    sun = s.equals(SUNDAY);
                }
            }
        }

    }

    public TrainDay(boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }
}
