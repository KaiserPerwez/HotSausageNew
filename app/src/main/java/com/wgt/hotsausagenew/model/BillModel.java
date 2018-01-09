package com.wgt.hotsausagenew.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Admin on 27-12-2017.
 */
@Entity(
        primaryKeys = {"id", "product"},
        foreignKeys = {
                @ForeignKey(
                        entity = TransactionModel.class,
                        parentColumns = "id",
                        childColumns = "id",
                        onDelete = CASCADE,
                        onUpdate = CASCADE
                )
        }
)
public class BillModel {
    @NonNull
    private String product;
    private int quantity;
    private double rate;
    @NonNull
    private int id;

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
