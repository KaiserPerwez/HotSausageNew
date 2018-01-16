package com.wgt.hotsausagenew.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Admin on 26-12-2017.
 */
@Entity(indices = {@Index(
        value = {"username"},
        unique = true)
})
public class UserModel {

    @PrimaryKey
    public int user_id;
    @NonNull
    public String username;
    @NonNull
    public String password;
    @NonNull
    public String site;

    public UserModel(int user_id, String username, String password, String site) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.site = site;
    }
}
