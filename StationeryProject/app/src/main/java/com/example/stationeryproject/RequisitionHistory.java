package com.example.stationeryproject;

public class RequisitionHistory {
    int requestId;
    String applicant;
    String requestDate;
    String description;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    int qty;
    String unit;

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public RequisitionHistory(int id, String applicant, String requestDate, String description, int qty, String unit)
    {
        this.requestId=id;
        this.applicant=applicant;
        this.requestDate=requestDate;
        this.description=description;
        this.qty=qty;
        this.unit=unit;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
