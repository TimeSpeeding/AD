package com.example.stationeryproject;

public class Request {
    int requestId;
    String applicant;
    String requestDate;
    String status;
    String description;
    String dep;
    int qty;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getApplicant() {
        return applicant;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getDescription() {
        return description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Request(int id, String applicant, String requestDate, String status,String dep)
    {
        this.requestId=id;
        this.applicant=applicant;
        this.requestDate=requestDate;
        this.status=status;
        this.dep=dep;
    }

    public Request(int id, String applicant, String requestDate, String description, int qty, String status,String dep)
    {
        this.description=description;
        this.qty=qty;
        this.requestId=id;
        this.applicant=applicant;
        this.requestDate=requestDate;
        this.status=status;
        this.dep=dep;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
