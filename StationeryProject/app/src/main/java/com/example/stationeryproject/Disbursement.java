package com.example.stationeryproject;

public class Disbursement extends Retrieval{
   int deliveryQty;

    public Disbursement( int id,String dep, String description, int scheduledQty, int actualQty, int deliveryQty)
    {
      super(id,dep,description,scheduledQty,actualQty);
      this.deliveryQty=deliveryQty;
    }

    public int getDeliveryQty() {
        return deliveryQty;
    }

    public void setDeliveryQty(int deliveryQty) {
        this.deliveryQty = deliveryQty;
    }
}
