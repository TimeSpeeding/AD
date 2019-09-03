package com.example.stationeryproject;

public class Adjustment {
    int itemId;
    String category;
    String Description;
    int qty;

    public int getItemId() {
        return itemId;
    }

    public String getCategory() {
        return category;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    String reason;
}
