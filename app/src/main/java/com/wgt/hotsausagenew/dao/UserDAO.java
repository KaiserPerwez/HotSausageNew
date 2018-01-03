package com.wgt.hotsausagenew.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wgt.hotsausagenew.model.UserModel;

import java.util.List;

/**
 * Created by Admin on 26-12-2017.
 */

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(UserModel... userModel);

    @Query("select * from usermodel where username = :userName and password = :passWord limit 1")
    public UserModel getUser(String userName, String passWord);

    @Query("select * from usermodel")
    public List<UserModel> getAllUser();

    @Query("delete from usermodel")
    void removeAllUsers();

    @Query("delete from usermodel where username = :userName")
    void deleteUser(String userName);

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateUser(UserModel userModel);


}
