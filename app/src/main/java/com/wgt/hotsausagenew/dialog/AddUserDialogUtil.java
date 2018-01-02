package com.wgt.hotsausagenew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
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

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.adapter.AddUserAdapter;
import com.wgt.hotsausagenew.model.UserModel;

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
    }

    @OnClick(R.id.dialog_btn_add_item)
    public void addUserToListAndDatabase() {
        Toast.makeText(ctx, "Stored", Toast.LENGTH_SHORT).show();
        String username = dialog_et_col_1.getText().toString();
        String pwd = dialog_et_col_2.getText().toString();
        String site = dialog_et_col_3.getText().toString();

        UserModel newUser = new UserModel(username, pwd, site, 0);
        addUserAdapter.addItem(newUser);
        hideAddUserPanel();

    }

    private void modifyUiAsPerRequirement() {
        dialog_title.setText("User List");
        dialog_et_col_1.setHint("Enter Username");
        dialog_et_col_2.setHint("Enter Password");
        dialog_et_col_2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        dialog_et_col_3.setHint("Enter Site");

        dialog_header_col_1.setText("Username");
        dialog_header_col_2.setText("Password");
        dialog_header_col_3.setText("Site");
    }

    public void populateListView() {
        //send list from database
        userModelList = new ArrayList<>();
        userModelList.add(new UserModel("user 1", "pass 1", "site 1", 0));
        userModelList.add(new UserModel("user 2", "pass 2", "site 2", 0));
        userModelList.add(new UserModel("user 3", "pass 3", "site 3", 0));
        addUserAdapter = new AddUserAdapter(userModelList, ctx);
        dialog_listview.setAdapter(addUserAdapter);
        dialog_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(ctx);// Create custom dialog object
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_delete_alert);
                dialog.show();

                String itemToBeDeleted = (userModelList.get(position)).username;
                DeleteDataDialogUtil deleteDataDialogUtil = new DeleteDataDialogUtil(itemToBeDeleted, AddUserDialogUtil.this, dialog);
                return false;
            }
        });
    }

    @Override
    public void onDeletionSelected(String itemName) {
        addUserAdapter.removeItem(itemName);
    }
}
