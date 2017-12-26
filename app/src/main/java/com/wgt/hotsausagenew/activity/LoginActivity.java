package com.wgt.hotsausagenew.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.database.AppDatabase;
import com.wgt.hotsausagenew.model.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.eT_username)
    EditText eT_username;
    @BindView(R.id.eT_password)
    EditText eT_password;
    @BindView(R.id.iV_update)
    ImageView iV_update;
    @BindView(R.id.tV_update)
    TextView tV_update;
    private UserModel userModel;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        database = AppDatabase.getDatabase(getApplicationContext());//initialise database
        // cleanup for testing some initial data, if necessary
        //database.userDAO().removeAllUsers();

        //initialise Login table if empty
        initialiseLoginTable();
    }

    private void initialiseLoginTable() {

        // add some data
        List<UserModel> users = database.userDAO().getAllUser();
        int size = users.size();
        if (size == 0)//db userModel table is empty. So, add data
        {
            database.userDAO().addUser(
                    new UserModel("dev", "dev123", "devSite", 0),
                    new UserModel("marketstreet", "market123", "marketSite", 0)
//                ,
//                new UserModel("user","pwd","sitez",0),
//                new UserModel("user","pwd","sitez",0),
//                new UserModel("user","pwd","sitez",0),
//                new UserModel("user","pwd","sitez",0),
//                new UserModel("user","pwd","sitez",0),
//                new UserModel("user","pwd","sitez",0),
//                new UserModel("user","pwd","sitez",0)
            );
        }
//        else
//            {
//              UserModel  user = database.userDAO().getAllUser().get(0);
//              int id=user.id;
//user=database.userDAO().getUser("user","pwd");
//                 id=user.id;
//        }
    }

    @OnTouch(R.id.btn_login)
    public boolean btn_pressed(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.login_button_pressed);
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.login_button);
            Toast.makeText(this, "Clicked login", Toast.LENGTH_SHORT).show();
            authenticateUser(eT_username.getText().toString(), eT_password.getText().toString());
        }
        return false;
    }

    private void authenticateUser(String username, String password) {
        if (database != null) {
            userModel = database.userDAO().getUser(username, password);
            if (userModel == null) {
                Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "ID:" + userModel.id + " site:" + userModel.site, Toast.LENGTH_SHORT).show();
            //TODO: use data in next activities
        }
    }

    @OnTouch({R.id.iV_update, R.id.tV_update})
    public boolean update_pressed(View view) {
        Toast.makeText(this, "code to download latest apk from server and auto-install", Toast.LENGTH_SHORT).show();
        return false;
    }

    @OnTouch(R.id.layout_login)
    public boolean screen_Touched(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return false;
    }
}
