package com.wgt.hotsausagenew.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"trans_id"},
        foreignKeys = {
        @ForeignKey(
                entity = UserModel.class,
                parentColumns = "user_id",
                childColumns = "user_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})
public class TransactionModel {
    private String time;
    private double sale;
    private double amount;
    private double discount;
    @Ignore
    private List<BillModel> billList;
    private int trans_id;
    private int user_id;
    private int date;
    private int month;
    private int year;
    private int sync_status;
    private int saver_status;

    public TransactionModel(String time, double sale, double amount, double discount,
                            int trans_id, int user_id, int date, int month, int year,
                            int sync_status, int saver_status) {
        this.time = time;
        this.sale = sale;
        this.amount = amount;
        this.discount = discount;
        this.trans_id = trans_id;
        this.user_id = user_id;
        this.date = date;
        this.month = month;
        this.year = year;
        this.sync_status = sync_status;
        this.saver_status = saver_status;
    }

    public TransactionModel(String time, double sale, double amount, double discount, List<BillModel> billList) {
        this.time = time;
        this.sale = sale;
        this.amount = amount;
        this.discount = discount;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<BillModel> getBillList() {
        return billList;
    }

    public void setBillList(List<BillModel> billList) {
        this.billList = billList;
    }

    public int getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(int trans_id) {
        this.trans_id = trans_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    public int getSaver_status() {
        return saver_status;
    }

    public void setSaver_status(int saver_status) {
        this.saver_status = saver_status;
    }
}
