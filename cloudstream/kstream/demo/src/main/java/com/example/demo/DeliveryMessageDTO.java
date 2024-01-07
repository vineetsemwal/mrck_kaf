package com.example.demo;

public class DeliveryMessageDTO {
    private String orderID;
    private String status;

    public DeliveryMessageDTO(){}

    public DeliveryMessageDTO(String orderId, String status){
        this.orderID=orderId;
        this.status=status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
