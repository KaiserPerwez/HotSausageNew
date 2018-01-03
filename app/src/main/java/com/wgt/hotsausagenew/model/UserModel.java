package com.wgt.hotsausagenew.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Admin on 26-12-2017.
 */
@Entity(indices = {@Index(
        value = {"username", "password"},
        unique = true)
})
public class UserModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    public String username;
    @NonNull
    public String password;
    @NonNull
    public String site;
    public int sync_status;//0--> NO_SYNC,  1--> SYNC_DOWN from server,  2-->SYNC_UP to server

    public UserModel(String username, String password, String site, int sync_status) {
        this.username = username;
        this.password = password;
        this.site = site;
        this.sync_status = sync_status;
    }

}
