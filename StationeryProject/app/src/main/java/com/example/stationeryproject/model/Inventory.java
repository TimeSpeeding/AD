
package com.example.stationeryproject.model;

import com.google.gson.annotations.SerializedName;

public class Inventory {

    @SerializedName("Category")
    private String category;
    @SerializedName("Description")
    private String description;
    @SerializedName("Id")
    private Long id;
    @SerializedName("Quantity")
    private Long quantity;
    @SerializedName("TransactionId")
    private Long transactionId;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

}
