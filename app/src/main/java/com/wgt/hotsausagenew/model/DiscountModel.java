package com.wgt.hotsausagenew.model;

public class DiscountModel {
    private String itemName, itemDesc;
    private double value;

    public DiscountModel(String itemName, String itemDesc, double value) {
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.value = value;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return itemName+" : "+value+" : "+itemDesc;
    }
}
