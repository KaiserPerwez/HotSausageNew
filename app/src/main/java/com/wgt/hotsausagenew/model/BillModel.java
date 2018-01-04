package com.wgt.hotsausagenew.model;

/**
 * Created by Admin on 27-12-2017.
 */

public class BillModel {
    private String product;
    private int quantity;
    private double rate;
    private int id;

    public BillModel() {
    }

    /*public BillModel(String product, int quantity, double rate) {
        this.product = product;
        this.quantity = quantity;
        this.rate = rate;
    }*/

    public BillModel(String product, int quantity, double rate, int id) {
        this.product = product;
        this.quantity = quantity;
        this.rate = rate;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
