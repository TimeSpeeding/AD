package com.example.stationeryproject;

public class DisbursementDepEntity extends StationeryNeeded{
    int id;
    int deliveryQty;
    String status;
    String dep;

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public DisbursementDepEntity(int id, String description, int neededQty, int retrievalQty, int deliveryQty) {
        super(description,neededQty,retrievalQty);
        this.id=id;
        this.deliveryQty=deliveryQty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeliveryQty() {
        return deliveryQty;
    }
    public void setDeliveryQty(int deliveryQty) {
        this.deliveryQty = deliveryQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
