
package com.example.stationeryproject.model;

import com.google.gson.annotations.SerializedName;

public class Price {

    @SerializedName("Category")
    private String category;
    @SerializedName("Code")
    private String code;
    @SerializedName("Description")
    private String description;
    @SerializedName("Id")
    private Long id;
    @SerializedName("Price1")
    private Double price1;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Double getPrice1() {
        return price1;
    }

    public void setPrice1(Double price1) {
        this.price1 = price1;
    }

}
