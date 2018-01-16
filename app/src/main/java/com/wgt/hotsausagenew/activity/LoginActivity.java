package com.wgt.hotsausagenew.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.database.AppDatabase;
import com.wgt.hotsausagenew.dialog.AddUserDialogUtil;
import com.wgt.hotsausagenew.model.TransactionModel;
import com.wgt.hotsausagenew.model.UserModel;
import com.wgt.hotsausagenew.network.SyncUser;
import com.wgt.hotsausagenew.receiver.UpdateReceiver;
import com.wgt.hotsausagenew.utils.ConnectionDetector;
import com.wgt.hotsausagenew.utils.Constant;
import com.wgt.hotsausagenew.utils.ToastUtil;
import com.wgt.hotsausagenew.utils.UpdateManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
        downloadUsersData(false);

        // register alarm Manager
        Intent intent = new Intent("com.wgt.hotsausagenew.receiver");
        sendBroadcast(intent);

        // call deleteTransaction to delete all old synced data
        deleteTrans();
    }


    @OnTouch(R.id.btn_login)
    public boolean btn_pressed(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.login_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.login_button);

            String uname = eT_username.getText().toString();
            String pass = eT_password.getText().toString();
            if (validateData(uname, pass)) {
                UserModel userModel = authenticateUser(uname, pass);
                if (userModel != null) {
                    //save userID to Constant, for further reference
                    Constant.setLoggedInUserId(userModel.user_id);
                    ToastUtil.showToastGeneric(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("username", userModel.username);
                    intent.putExtra("site", userModel.site);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_left); // animation
                    finish();

                } else {
                    ToastUtil.showToastGeneric(this, "ERROR : Login failed\nusername or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            /*List<TransactionModel> tr = AppDatabase.getDatabase(this).transactionDAO().getAllTrans();
            for (TransactionModel transactionModel : tr) {
                List<BillModel> bills = database.billDAO().getAllBillById(transactionModel.getTrans_id());
                transactionModel.setBillList(bills);
            }
            Gson gson = new Gson();
            String res = gson.toJson(tr);
            //res = " ";
            SyncTransaction syncTransaction = new SyncTransaction(this);
            syncTransaction.startSyncing();*/
        }
        return false;
    }


    @OnTouch({R.id.iV_update, R.id.tV_update})
    public boolean update_pressed(View view) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Checking network status...");
        dialog.show();
        ConnectionDetector detector = new ConnectionDetector(this, false);
        detector.execute();
        detector.setConnectionDetectorListener(new ConnectionDetector.ConnectionDetectorListener() {
            @Override
            public void onConnectionDetected(boolean status) {
                dialog.dismiss();
                if (!status) {
                    ToastUtil.showToastGeneric(LoginActivity.this, "ERROR : No active internet connection. \nUpdate Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateCall();
            }
        });

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

    @OnClick(R.id.tV_login)
    public void onTouchLoginSync() {
        ToastUtil.showToastGeneric(this, "Syncing users data...", Toast.LENGTH_SHORT).show();
        downloadUsersData(true);
    }

    @OnLongClick(R.id.tV_login)
    public boolean open_AddUserDialog_byAdminOnly() {
        String uname = eT_username.getText().toString();
        String pass = eT_password.getText().toString();

        UserModel userModel = authenticateUser(uname, pass);
        if (userModel != null && userModel.username.equals(Constant.ADMIN_USERNAME)) {
            AddUserDialogUtil addUserDialogUtil = new AddUserDialogUtil(this);
            addUserDialogUtil.showDialog();
            addUserDialogUtil.populateListView();
        }
        return true;
    }


    //-----------------------------------------------------User defined Functions---------------------------//

    // download users data
    private void downloadUsersData(final boolean notifyUser) {
        ConnectionDetector detector = new ConnectionDetector(this, notifyUser);
        detector.setConnectionDetectorListener(new ConnectionDetector.ConnectionDetectorListener() {
            @Override
            public void onConnectionDetected(boolean status) {
                // if no internet connection.
                if (!status) {
                    if (notifyUser) {
                        ToastUtil.showToastGeneric(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        //ToastUtil.showToastGeneric(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                // if there is active internet connection
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Downloading users data");
                progressDialog.setMessage("please wait");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                if (notifyUser) {
                    progressDialog.show();
                }

                SyncUser syncUser = new SyncUser(LoginActivity.this);
                syncUser.setSyncUserListener(SyncUser.KEY_DOWNLOAD_ALL_USERS, new SyncUser.SyncUserListener() {
                    @Override
                    public void onSuccess(int key, String response) {
                        if (notifyUser) {
                            progressDialog.dismiss();
                        }

                        final ProgressDialog psaving = new ProgressDialog(LoginActivity.this);
                        psaving.setTitle("Saving users data");
                        psaving.setMessage("please wait");
                        psaving.setCancelable(false);
                        psaving.setCanceledOnTouchOutside(false);
                        if (notifyUser) {
                            psaving.show();
                        }

                        database.userDAO().removeAllUsers();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (code.equals("1")) {  // success response from server
                                JSONArray jsonArray = new JSONArray(message);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    int id = Integer.parseInt(obj.getString("id"));
                                    String username = obj.getString("username");
                                    String pass = obj.getString("password");
                                    String site = obj.getString("site");

                                    UserModel userModel = new UserModel(id, username, pass, site);
                                    database.userDAO().addUser(userModel);
                                }
                            } else { // error response from server
                                ToastUtil.showToastGeneric(LoginActivity.this, "ERROR : " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            if (notifyUser) {
                                ToastUtil.showToastGeneric(LoginActivity.this, "ERROR : parsing error." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            if (notifyUser) {
                                ToastUtil.showToastGeneric(LoginActivity.this, "ERROR : parsing error." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (notifyUser) {
                            psaving.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(int key, VolleyError error) {
                        if (notifyUser) {
                            progressDialog.dismiss();
                            ToastUtil.showToastGeneric(LoginActivity.this, "ERROR : Failed to fetch user data\n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                syncUser.downloadAllUsers();
            }
        });
        detector.execute();

    }

    // App Updater
    private void updateCall() {
        // check if update file already exists.
        // if exists, it will be deleted
        File downloadFile = new File(
                this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                        File.separator + UpdateManager.DOWNLOADED_FILE_NAME
        );
        if (downloadFile.exists()) {
            boolean b = downloadFile.delete();
            ToastUtil.showToastGeneric(this, "Old Update file is deleted.", Toast.LENGTH_SHORT).show();
        }

        UpdateManager updateManager = new UpdateManager(this);
        long updateID = updateManager.startDownload();

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Downloading");
        pd.setMessage("please wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        UpdateReceiver updateReceiver = new UpdateReceiver(pd, this, updateID, downloadFile);
        registerReceiver(updateReceiver, intentFilter);
    }

    //initially one user will be inserted
    private void initialiseLoginTable() {

        // add some data
        List<UserModel> users = database.userDAO().getAllUser();
        int size = users.size();
        if (size == 0)//db userModel table is empty. So, add data
        {
            //TODO : user_id should be received from webservice
            database.userDAO().addUser(
                    new UserModel(0, "dev", "dev123", "devSite")
            );
        }
    }

    // authenticate users against Local DB
    private UserModel authenticateUser(String username, String password) {
        UserModel userModel = null;
        if (database != null) {
            userModel = database.userDAO().getUser(username, password);
        }
        return userModel;
    }

    private boolean validateData(String username, String password) {
        if (username.length() < 1) {
            ToastUtil.showToastGeneric(this, "ERROR : Provide Username", Toast.LENGTH_SHORT).show();
            eT_username.requestFocus();
            return false;
        } else if (password.length() < 1) {
            ToastUtil.showToastGeneric(this, "ERROR : Provide Password", Toast.LENGTH_SHORT).show();
            eT_password.requestFocus();
            return false;
        }
        return true;
    }

    private void deleteTrans() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        List<Integer> ids = new ArrayList<>();

        // get all synced transaction
        List<TransactionModel> trans = database.transactionDAO().getAllSyncedTrans();

        //get all synced old ids
        for (TransactionModel t : trans) {
            Date trans_date = getDate(t.getDate(), t.getMonth(), t.getYear());
            if (today.after(trans_date)) {
                ids.add(t.getTrans_id());
            }
        }

        //delete all those transactions
        for (int i : ids) {
            database.transactionDAO().deleteTransactionByID(i);
        }
        String a = "dd";
    }

    private Date getDate(int day, int month, int year) {
        String d = year + "/" + month + "/" + day;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
