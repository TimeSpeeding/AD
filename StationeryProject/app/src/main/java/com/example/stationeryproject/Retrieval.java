package com.example.stationeryproject;

public class Retrieval{
    int id;
    String description;
    String dep;
    int scheduledQty,actualQty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Retrieval(int id,String dep, String description, int scheduledQty, int actualQty)
    {
       this.description=description;
       this.scheduledQty=scheduledQty;
       this.actualQty=actualQty;
        this.id=id;
        this.dep=dep;
    }

    public String getDep() {
        return dep;
    }
    public void setDep(String dep) {
        this.dep = dep;
    }

    public int getActualQty() {
        return actualQty;
    }

    public void setActualQty(int actualQty) {
        this.actualQty = actualQty;
    }

    public int getScheduledQty() {
        return scheduledQty;
    }

    public void setScheduledQty(int scheduledQty) {
        this.scheduledQty = scheduledQty;
    }

    public String getDescription() {
        return description;
    }

    public void setDesciption(String desciption) {
        this.description = desciption;
    }
}
