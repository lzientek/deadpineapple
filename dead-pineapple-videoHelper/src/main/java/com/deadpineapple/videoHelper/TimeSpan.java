package com.deadpineapple.videoHelper;

/**
 * Created by 15256 on 31/03/2016.
 */
public class TimeSpan {
    private int heures;
    private int minutes;
    private double secondes;

    public TimeSpan() {
        heures = 0;
        minutes = 0;
        secondes = 0;
    }

    public TimeSpan(int heures, int minutes, double secondes) {
        this.heures = heures;
        this.minutes = minutes;
        this.secondes = secondes;
    }

    /**
     * format de la chaine en 00:00:00.000
     *
     * @param time
     */
    public TimeSpan(String time) {
        String[] values = time.split(":");
        setHeures(Integer.parseInt(values[0]));
        setMinutes(Integer.parseInt(values[1]));
        setSecondes(Double.parseDouble(values[2]));
    }

    public int getHeures() {
        return heures;
    }

    public void setHeures(int heures) {
        if (heures >= 99 || heures < 0) {
            throw new IndexOutOfBoundsException("heures doit etre compris entre 0 et 99");
        }
        this.heures = heures;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        if (minutes >= 60 || minutes < 0) {
            throw new IndexOutOfBoundsException("minutes doit etre compris entre 0 et 23");
        }
        this.minutes = minutes;
    }

    public double getSecondes() {
        return secondes;
    }

    public void setSecondes(double secondes) {
        if (secondes >= 60 || secondes < 0) {
            throw new IndexOutOfBoundsException("secondes doit etre compris entre 0 et 59.999");
        }
        this.secondes = secondes;
    }

    public TimeSpan add(TimeSpan ts) {
        TimeSpan tsresult = new TimeSpan(getHeures(), getMinutes(), getSecondes());
        tsresult.addSecondes(ts.getSecondes());
        tsresult.addMinutes(ts.getMinutes());
        tsresult.addHeures(ts.getHeures());
        return tsresult;
    }

    public boolean isGreaterThan(TimeSpan timeSpan) {
        if (timeSpan.getHeures() < getHeures()) {
            return true;
        } else if (timeSpan.getHeures() == getHeures()
                && timeSpan.getMinutes() < getMinutes()) {
            return true;
        } else if (timeSpan.getHeures() == getHeures()
                && timeSpan.getMinutes() == getMinutes()
                && timeSpan.getSecondes() < getSecondes()) {
            return true;
        }
        return false;
    }

    public boolean isGreaterThanOrEqual(TimeSpan timeSpan) {
        return isEqual(timeSpan) || isGreaterThan(timeSpan);
    }

    public boolean isEqual(TimeSpan timeSpan) {
        return getHeures() == timeSpan.getHeures()
                && getMinutes() == timeSpan.getMinutes()
                && getSecondes() == timeSpan.getSecondes();
    }

    public void addSecondes(double secondes) {
        double newSecondes = this.getSecondes() + secondes;
        setSecondes(newSecondes % 60);
        addMinutes((int) (newSecondes / 60));
    }

    public void addMinutes(int minutes) {
        int newh = minutes + getMinutes();
        setMinutes(newh % 60);
        addHeures(newh / 60);
    }

    public void addHeures(int heures) {

        setHeures(getHeures() + heures);
    }

    @Override
    public String toString() {
        String result = "";
        if (heures == 0) {
            result += "00";
        } else if (heures < 10) {
            result += "0" + heures;
        } else if (heures <= 99) {
            result += heures;
        }
        result += ":";
        if (minutes == 0) {
            result += "00";
        } else if (minutes < 10) {
            result += "0" + minutes;
        } else if (heures < 60) {
            result += minutes;
        }
        result += ":";
        if (secondes == 0) {
            result += "00";
        } else if (secondes < 10) {
            result += "0" + Double.toString(secondes).replace(',', '.');
        } else if (secondes < 60) {
            result += Double.toString(secondes).replace(',', '.');
        }
        return result;
    }
}
