package com.wgt.hotsausagenew.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.wgt.hotsausagenew.dao.BillDAO;
import com.wgt.hotsausagenew.dao.TransactionDAO;
import com.wgt.hotsausagenew.dao.UserDAO;
import com.wgt.hotsausagenew.model.BillModel;
import com.wgt.hotsausagenew.model.TransactionModel;
import com.wgt.hotsausagenew.model.UserModel;

/**
 * Created by Admin on 26-12-2017.
 */

@Database(entities = {UserModel.class, TransactionModel.class, BillModel.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, "HotSausageNewDatabase")
//Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            // For testing, allow queries on the main thread.
                            // Don't do this on a real app!
                            .allowMainThreadQueries()
                            // recreate the database if necessary
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract UserDAO userDAO();

    public abstract TransactionDAO transactionDAO();

    public abstract BillDAO billDAO();
}
