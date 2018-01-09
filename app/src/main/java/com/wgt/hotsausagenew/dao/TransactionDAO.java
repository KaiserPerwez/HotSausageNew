package com.wgt.hotsausagenew.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wgt.hotsausagenew.model.TransactionModel;

import java.util.List;

/**
 * Created by debasish on 08-01-2018.
 */

@Dao
public interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    long addTransaction(TransactionModel trans);

    @Query("SELECT * from TransactionModel")
    List<TransactionModel> getAllTrans();

    @Query("SELECT * from TransactionModel where date = :date and month = :month and year = :year")
    List<TransactionModel> getAllTransByDate(int date, int month, int year);
}
