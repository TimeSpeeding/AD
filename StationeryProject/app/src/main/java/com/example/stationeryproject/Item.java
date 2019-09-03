package com.example.stationeryproject;

public class Item {
    int id;
    String itemName;
    int qty;
    String unit;
    int safetyQty;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Item(int id,String itemName,String category,int qty,int safetyQty,String unit) {
        this.id = id;
        this.itemName=itemName;
        this.category=category;
        this.qty=qty;
        this.safetyQty=safetyQty;
        this.unit=unit;
    }

    String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getSafetyQty() {
        return safetyQty;
    }

    public void setSafetyQty(int safetyQty) {
        this.safetyQty = safetyQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
