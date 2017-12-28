package com.wgt.hotsausagenew.model;

public class DiscountModel {
    private String product, prodDesc;
    private double rate;

    public DiscountModel(String product, String prodDesc, double rate) {
        this.product = product;
        this.prodDesc = prodDesc;
        this.rate = rate;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return product + " : " + rate + " : " + prodDesc;
    }
}
