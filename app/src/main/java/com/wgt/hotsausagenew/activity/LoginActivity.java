package com.wgt.hotsausagenew.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.database.AppDatabase;
import com.wgt.hotsausagenew.dialog.AddUserDialogUtil;
import com.wgt.hotsausagenew.model.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.tV_login)
    TextView tV_login;
    @BindView(R.id.eT_username)
    EditText eT_username;
    @BindView(R.id.eT_password)
    EditText eT_password;
    @BindView(R.id.iV_update)
    ImageView iV_update;
    @BindView(R.id.tV_update)
    TextView tV_update;

    @BindView(R.id.layout_login)
    ConstraintLayout login_Layout;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        login_Layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom)); // animation

        database = AppDatabase.getDatabase(getApplicationContext());//initialise database
        // cleanup for testing some initial data, if necessary
        //database.userDAO().removeAllUsers();

        //initialise Login table if empty
        initialiseLoginTable();
    }

    @OnTouch(R.id.btn_login)
    public boolean btn_pressed(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.login_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.login_button);
            //Toast.makeText(this, "Clicked login", Toast.LENGTH_SHORT).show();
            String uname = eT_username.getText().toString();
            String pass = eT_password.getText().toString();
            if (validateData(uname, pass)) {
                UserModel userModel = authenticateUser(uname, pass);
                if(userModel!=null){
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("username", userModel.username);
                    intent.putExtra("site", userModel.site);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_left); // animation
                    finish();

                } else {
                    Toast.makeText(this, "ERROR : Login failed\nusername or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
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

    @OnTouch(R.id.eT_password)
    public boolean togglePasswordVisibility(EditText editTextPwd, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() >= (editTextPwd.getWidth() - editTextPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here
                editTextPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_on, 0);
                editTextPwd.setTransformationMethod(null);
                editTextPwd.setSelection(editTextPwd.getText().length());
                return true;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() >= (editTextPwd.getWidth() - editTextPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here
                editTextPwd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                editTextPwd.setTransformationMethod(new PasswordTransformationMethod());
                editTextPwd.setSelection(editTextPwd.getText().length());
                return true;
            }
        }

        return false;
    }

    @OnLongClick(R.id.tV_login)
    public boolean open_AddUserDialog_byAdminOnly() {
        UserModel userModel = authenticateUser("AdminIndia", eT_password.getText().toString());
        // if(userModel!=null) {
        /*final Dialog hiddenDialog = new Dialog(this);// Create custom dialog object
        hiddenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hiddenDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hiddenDialog.setCancelable(true);
        hiddenDialog.setContentView(R.layout.dialog_add_data);
        hiddenDialog.show();*/

        AddUserDialogUtil addUserDialogUtil = new AddUserDialogUtil(this);
        addUserDialogUtil.showDialog();
        addUserDialogUtil.populateListView();
        //}
        return false;
    }


    //-----------------------------------------------------User defined Functions---------------------------//
    private void initialiseLoginTable() {

        // add some data
        List<UserModel> users = database.userDAO().getAllUser();
        int size = users.size();
        if (size == 0)//db userModel table is empty. So, add data
        {
            database.userDAO().addUser(
                    new UserModel("dev", "dev123", "devSite", 0)
                    //new UserModel("marketstreet", "market123", "marketSite", 0)
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

    private UserModel authenticateUser(String username, String password) {
        UserModel userModel = null;
        if (database != null) {
            userModel = database.userDAO().getUser(username, password);
        }
        return userModel;
    }

    private boolean validateData(String username, String password) {
        if (username.length()<1){
            Toast.makeText(this, "ERROR : Provide Username", Toast.LENGTH_SHORT).show();
            eT_username.requestFocus();
            return false;
        } else if (password.length() < 1){
            Toast.makeText(this, "ERROR : Provide Password", Toast.LENGTH_SHORT).show();
            eT_password.requestFocus();
            return false;
        }
        return true;
    }
}
