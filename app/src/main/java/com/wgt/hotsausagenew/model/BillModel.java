package com.wgt.hotsausagenew.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Admin on 27-12-2017.
 */
@Entity(
        primaryKeys = {"trans_id", "product"},
        foreignKeys = {
                @ForeignKey(
                        entity = TransactionModel.class,
                        parentColumns = "trans_id",
                        childColumns = "trans_id",
                        onDelete = CASCADE,
                        onUpdate = CASCADE
                )
        }
)
public class BillModel {
    @NonNull
    private String product;
    private int quantity;
    private double amount;
    @NonNull
    private int trans_id;

    public BillModel(String product, int quantity, double amount, int trans_id) {
        this.product = product;
        this.quantity = quantity;
        this.amount = amount;
        this.trans_id = trans_id;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(int trans_id) {
        this.trans_id = trans_id;
    }
}
