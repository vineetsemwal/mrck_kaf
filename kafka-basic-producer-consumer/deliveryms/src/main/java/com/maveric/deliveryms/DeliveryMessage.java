package com.maveric.deliveryms;

public class DeliveryMessage {
    private String orderID;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    private DeliveryStatus status;

    public DeliveryMessage(){}

    public DeliveryMessage(String orderID,DeliveryStatus status){
        this.orderID=orderID;
        this.status=status;
    }

}
