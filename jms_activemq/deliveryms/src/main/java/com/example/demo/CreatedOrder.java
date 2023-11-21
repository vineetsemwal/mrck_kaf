package com.example.demo;

public class CreatedOrder {
    private int orderId;
    private double price;
    private String itemName;
    private int itemsUnit;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemsUnit() {
        return itemsUnit;
    }

    public void setItemsUnit(int itemsUnit) {
        this.itemsUnit = itemsUnit;
    }

    @Override
    public String toString() {
        return "CreatedOrder{" +
                "orderId=" + orderId +
                ", price=" + price +
                ", itemName='" + itemName + '\'' +
                ", itemsUnit=" + itemsUnit +
                '}';
    }
}
