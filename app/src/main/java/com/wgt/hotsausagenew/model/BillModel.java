package com.wgt.hotsausagenew.model;

/**
 * Created by Admin on 27-12-2017.
 */

public class BillModel {
    private String product;
    private int quantity;
    private double price;

    public BillModel() {
    }

    public BillModel(String product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
