package com.example.stationeryproject;

public class StationeryNeeded {

    String description;
    int scheduledQty;
    int actualQty;

    public StationeryNeeded(String description, int scheduledQty, int actualQty) {

        this.description = description;
        this.scheduledQty=scheduledQty;
        this.actualQty=actualQty;
    }


    public String getDescription() {
        return description;
    }

    public int getScheduledQty() {
        return scheduledQty;
    }

    public int getActualQty() {
        return actualQty;
    }

    public void setActualQty(int actualQty) {
        this.actualQty = actualQty;
    }

    public void setScheduledQty(int scheduledQty) {
        this.scheduledQty = scheduledQty;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
