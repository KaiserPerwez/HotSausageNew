package com.wgt.hotsausagenew.model;

public class SpecialItemModel {
    private String prod;
    private double rate;

    public SpecialItemModel(String prod, double rate) {
        this.prod = prod;
        this.rate = rate;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
