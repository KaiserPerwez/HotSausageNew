package com.wgt.hotsausagenew.model;

import java.util.List;

public class TransactionModel {
    private String time;
    private double sale;
    private double amount;
    private double discounts;
    private List<BillModel> billList;

    public TransactionModel(String time, double sale, double amount, double discounts, List<BillModel> billList) {
        this.time = time;
        this.sale = sale;
        this.amount = amount;
        this.discounts = discounts;
        this.billList = billList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscounts() {
        return discounts;
    }

    public void setDiscounts(double discounts) {
        this.discounts = discounts;
    }

    public List<BillModel> getBillList() {
        return billList;
    }

    public void setBillList(List<BillModel> billList) {
        this.billList = billList;
    }
}
