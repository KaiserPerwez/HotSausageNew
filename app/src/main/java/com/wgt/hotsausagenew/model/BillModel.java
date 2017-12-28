package com.wgt.hotsausagenew.model;

/**
 * Created by Admin on 27-12-2017.
 */

public class BillModel {
    private String product;
    private int quantity;
    private double rate;

    public BillModel() {
    }

    public BillModel(String product, int quantity, double rate) {
        this.product = product;
        this.quantity = quantity;
        this.rate = rate;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
