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

    @Query("SELECT * from TransactionModel where sync_status = 0")
    List<TransactionModel> getAllTransForSync();

    @Query("SELECT * from TransactionModel where sync_status = 1")
    List<TransactionModel> getAllSyncedTrans();

    @Query("UPDATE TransactionModel set sync_status = 1 where trans_id = :trans_id")
    void setSyncStatus(int trans_id);

    @Query("SELECT * from TransactionModel where date = :date and month = :month and year = :year")
    List<TransactionModel> getAllTransByDate(int date, int month, int year);

    @Query("DELETE from TransactionModel where trans_id = :trans_id")
    int deleteTransactionByID(int trans_id);
}
