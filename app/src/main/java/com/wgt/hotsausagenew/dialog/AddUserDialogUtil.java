package com.wgt.hotsausagenew.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.adapter.AddUserAdapter;
import com.wgt.hotsausagenew.database.AppDatabase;
import com.wgt.hotsausagenew.model.UserModel;
import com.wgt.hotsausagenew.networking.SyncUser;
import com.wgt.hotsausagenew.utils.ConnectionDetector;
import com.wgt.hotsausagenew.utils.Constant;
import com.wgt.hotsausagenew.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 29-12-2017.
 */

public class AddUserDialogUtil implements DeleteDataDialogUtil.DeletionSelected {
    Dialog hiddenDialog;
    Context ctx;
    AddUserAdapter addUserAdapter;
    List<UserModel> userModelList;
    @BindView(R.id.dialog_title)
    TextView dialog_title;

    @BindView(R.id.dialog_header_col_1)
    TextView dialog_header_col_1;
    @BindView(R.id.dialog_header_col_2)
    TextView dialog_header_col_2;
    @BindView(R.id.dialog_header_col_3)
    TextView dialog_header_col_3;


    @BindView(R.id.dialog_iV_add)
    ImageView dialog_iV_add;
    @BindView(R.id.dialog_add_panel)
    LinearLayout dialog_add_panel;
    @BindView(R.id.dialog_list_panel)
    LinearLayout dialog_header;
    @BindView(R.id.dialog_et_col_1)
    EditText dialog_et_col_1;
    @BindView(R.id.dialog_et_col_2)
    EditText dialog_et_col_2;
    @BindView(R.id.dialog_et_col_3)
    EditText dialog_et_col_3;
    @BindView(R.id.dialog_btn_add_item)
    Button dialog_btn_add_item;
    @BindView(R.id.dialog_btn_cancel_item)
    Button dialog_btn_cancel_item;
    @BindView(R.id.dialog_header)
    LinearLayout dialog_list_panel;
    @BindView(R.id.dialog_listview)
    ListView dialog_listview;

    public AddUserDialogUtil(Context ctx) {
        this.hiddenDialog = new Dialog(ctx);// Create custom dialog object
        hiddenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hiddenDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hiddenDialog.setCancelable(true);
        hiddenDialog.setContentView(R.layout.dialog_add_data);
        //hiddenDialog.show();

        this.ctx = ctx;
        ButterKnife.bind(this, hiddenDialog);
        modifyUiAsPerRequirement();
    }

    public void showDialog() {
        hiddenDialog.show();
    }

    @OnClick(R.id.dialog_iV_add)
    public void showAddUserPanel() {
        dialog_add_panel.setVisibility(View.VISIBLE);
        dialog_list_panel.setVisibility(View.GONE);
        dialog_iV_add.setVisibility(View.INVISIBLE);
        dialog_listview.setVisibility(View.GONE);
    }

    @OnClick(R.id.dialog_btn_cancel_item)
    public void hideAddUserPanel() {
        dialog_add_panel.setVisibility(View.GONE);
        dialog_list_panel.setVisibility(View.VISIBLE);
        dialog_iV_add.setVisibility(View.VISIBLE);
        dialog_listview.setVisibility(View.VISIBLE);
        dialog_et_col_1.setText("");
        dialog_et_col_2.setText("");
        dialog_et_col_3.setText("");
    }

    @OnClick(R.id.dialog_btn_add_item)
    public void addUserToListAndDatabase() {
        final UserModel user = validateData();
        if (user == null) {
            return;
        }

        ConnectionDetector detector = new ConnectionDetector(ctx, true);
        detector.execute();
        detector.setConnectionDetectorListener(new ConnectionDetector.ConnectionDetectorListener() {
            @Override
            public void onConnectionDetected(boolean status) {
                if (!status) {
                    ToastUtil.showToastGeneric(ctx, "No internet Connection.", Toast.LENGTH_SHORT).show();
                    return;
                }
                networkCall(user);
            }
        });
    }

    private void networkCall(UserModel user) {

        String username = dialog_et_col_1.getText().toString();
        final ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setTitle("Adding user : " + username);
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        SyncUser syncUser = new SyncUser(ctx);
        syncUser.setSyncUserListener(SyncUser.KEY_UPLOAD_SINGLE_USER, new SyncUser.SyncUserListener() {
            @Override
            public void onSuccess(int key, String response) {
                dialog.dismiss();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (code.equals("1")) { // success response
                        JSONArray jsonArray = new JSONArray(message);
                        JSONObject obj = jsonArray.getJSONObject(0);
                        int id = Integer.parseInt(obj.getString("id"));
                        String username = obj.getString("username");
                        String pass = obj.getString("password");
                        String site = obj.getString("site");

                        UserModel userModel = new UserModel(id, username, pass, site);
                        AppDatabase.getDatabase(ctx).userDAO().addUser(userModel);
                        ToastUtil.showToastGeneric(ctx, "User : " + username + ", successfully registered.", Toast.LENGTH_SHORT).show();
                        populateListView();
                        hideAddUserPanel();
                    } else { // error on server side
                        ToastUtil.showToastGeneric(ctx, "ERROR : " + message + "\nFailed to add user.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ToastUtil.showToastGeneric(ctx, "ERROR : data parsing error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    ToastUtil.showToastGeneric(ctx, "ERROR : data parsing error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int key, VolleyError error) {
                dialog.dismiss();
                ToastUtil.showToastGeneric(ctx, "ERROR : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        syncUser.uploadSingleUser(user);

    }

    private UserModel validateData() {
        String username = dialog_et_col_1.getText().toString();
        String pwd = dialog_et_col_2.getText().toString();
        String site = dialog_et_col_3.getText().toString();
        if (username.length() < 1) {
            ToastUtil.showToastGeneric(ctx, "ERROR : Provide Username", Toast.LENGTH_SHORT).show();
            dialog_et_col_1.requestFocus();
            return null;
        } else if (pwd.length() < 1) {
            ToastUtil.showToastGeneric(ctx, "ERROR : Provide Password", Toast.LENGTH_SHORT).show();
            dialog_et_col_2.requestFocus();
            return null;
        } else if (site.length() < 1) {
            ToastUtil.showToastGeneric(ctx, "ERROR : Provide Site", Toast.LENGTH_SHORT).show();
            dialog_et_col_3.requestFocus();
            return null;
        }
        return new UserModel(0, username, pwd, site);
    }

    private void modifyUiAsPerRequirement() {
        dialog_title.setText("User List");
        dialog_et_col_1.setHint("Enter Username");
        dialog_et_col_2.setHint("Enter Password");

        //to enable normal keyboard
        dialog_et_col_2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //to transform typed characters into bullets
        dialog_et_col_2.setTransformationMethod(new PasswordTransformationMethod());

        dialog_et_col_3.setHint("Enter Site");

        dialog_header_col_1.setText("Username");
        dialog_header_col_2.setText("Password");
        dialog_header_col_3.setText("Site");
    }

    public void populateListView() {
        //send list from database
        userModelList = new ArrayList<>();
        userModelList = AppDatabase.getDatabase(ctx).userDAO().getAllUser();

        addUserAdapter = new AddUserAdapter(userModelList, ctx);
        dialog_listview.setAdapter(addUserAdapter);
        dialog_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String itemToBeDeleted = (userModelList.get(position)).username;

                // username won't be deleted.
                if (itemToBeDeleted.equals(Constant.ADMIN_USERNAME)) {
                    ToastUtil.showToastGeneric(ctx, "Admin can't be deleted", Toast.LENGTH_SHORT).show();
                    return false;
                }
                final Dialog dialog = new Dialog(ctx);// Create custom dialog object
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_delete_alert);
                dialog.show();

                DeleteDataDialogUtil deleteDataDialogUtil = new DeleteDataDialogUtil(itemToBeDeleted, AddUserDialogUtil.this, dialog);
                return false;
            }
        });
    }

    @Override
    public void onDeletionSelected(final String itemName) {
        ConnectionDetector detector = new ConnectionDetector(ctx, true);
        detector.execute();
        detector.setConnectionDetectorListener(new ConnectionDetector.ConnectionDetectorListener() {
            @Override
            public void onConnectionDetected(boolean status) {
                if (!status) {
                    ToastUtil.showToastGeneric(ctx, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                deleteNetworkCall(itemName);
            }
        });
    }

    private void deleteNetworkCall(final String itemName) {
        final ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setTitle("Deleting user : " + itemName);
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        int id = AppDatabase.getDatabase(ctx).userDAO().getIdByUsername(itemName);
        SyncUser syncUser = new SyncUser(ctx);
        syncUser.setSyncUserListener(SyncUser.KEY_DELETE_SINGLE_USER, new SyncUser.SyncUserListener() {
            @Override
            public void onSuccess(int key, String response) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (code.equals("1")) {
                        AppDatabase.getDatabase(ctx).userDAO().deleteUser(itemName);
                        ToastUtil.showToastGeneric(ctx, "User : " + itemName + ", deleted successfully", Toast.LENGTH_SHORT).show();
                        populateListView();
                    } else {
                        ToastUtil.showToastGeneric(ctx, "ERROR : " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    ToastUtil.showToastGeneric(ctx, "PARSING ERROR : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int key, VolleyError error) {
                dialog.dismiss();
                ToastUtil.showToastGeneric(ctx, "NETWORK ERROR : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        syncUser.deleteSingleUserFromRemote(id);
    }
}
