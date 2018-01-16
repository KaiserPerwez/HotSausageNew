package com.wgt.hotsausagenew.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.wgt.hotsausagenew.model.BillModel;

import java.util.List;

/**
 * Created by debasish on 08-01-2018.
 */

@Dao
public interface BillDAO {
    @Insert
    long addBill(BillModel bill);

    @Query("select * from BillModel")
    List<BillModel> getAllBill();

    @Query("select * from BillModel where trans_id = :id")
    List<BillModel> getAllBillById(int id);

    @Query("DELETE from BillModel where trans_id = :trans_id")
    int deleteBillById(int trans_id);
}
