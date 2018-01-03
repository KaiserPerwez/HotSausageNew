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
import com.wgt.hotsausagenew.adapter.SpecialItemAdapter;
import com.wgt.hotsausagenew.model.SpecialItemModel;
import com.wgt.hotsausagenew.model.UserModel;
import com.wgt.hotsausagenew.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by debasish on 02-01-2018.
 */

public class SpecialDialogUtil implements AdapterView.OnItemClickListener {
    private Context context;
    private String key;
    private SpecialItemAdapter adapter;

    private Dialog dialog;
    private SpecialDialogItemClickListener listener;

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


    public SpecialDialogUtil(Context context, String key) {
        this.context = context;
        this.key = key;
        adapter = new SpecialItemAdapter(context, Constant.getSpecialItemList(key));
        this.dialog = new Dialog(context);// Create custom dialog object
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_data);
        //hiddenDialog.show();

        ButterKnife.bind(this, dialog);
        modifyUiAsPerRequirement();
        dialog_listview.setAdapter(adapter);
        dialog_listview.setOnItemClickListener(this);
    }

    public void setSpecialDialogItemClickListener(SpecialDialogItemClickListener listener) {
        this.listener = listener;
    }

    private void modifyUiAsPerRequirement() {
        if (key.equals(Constant.SPECIAL_1)) {
            dialog_title.setText("SPECIAL 1");
        } else if (key.equals(Constant.SPECIAL_2)) {
            dialog_title.setText("SPECIAL 2");
        } else {
            dialog_title.setText("SPECIAL ITEMS");
        }

        dialog_et_col_3.setVisibility(View.GONE);
        dialog_header_col_3.setVisibility(View.GONE);

        dialog_header_col_1.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                2.0f
        ));
        dialog_header_col_2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                2.0f
        ));
        dialog_et_col_2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);


        dialog_et_col_1.setHint("Enter Product");
        dialog_et_col_2.setHint("Enter Price");

        dialog_header_col_1.setText("Product");
        dialog_header_col_2.setText("Price");


    }
    public void showDialog() {
        dialog.show();
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
    }

    @OnClick(R.id.dialog_btn_add_item)
    public void addSpecialItem() {

        SpecialItemModel specialItemModel = validateData();
        if (specialItemModel != null) {
            boolean s =  adapter.addItem(specialItemModel);
            if (s) {
                hideAddUserPanel();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog.dismiss();
        if (listener != null) {
            listener.onSpecialItemClicked(key, Constant.getItem(position, key));
        }
    }

    public interface SpecialDialogItemClickListener {
        void onSpecialItemClicked(String key, SpecialItemModel specialItem);
    }

    private SpecialItemModel validateData() {
        SpecialItemModel item = null;
        try {
            String name = dialog_et_col_1.getText().toString().trim();
            if (name.length() < 1) {
                dialog_et_col_1.requestFocus();
                throw new Exception("Provide Item Name.");
            }
            // validating name;  max 19 alphabet in total -> one space,  max 9 alphabet each word, max 2 words in total
            //total 19 alphabets
            if (name.length() > 19) {
                dialog_et_col_1.requestFocus();
                throw new Exception("name must be of max 19 characters");
            }
            String[] words = name.split(" ");
            int wordCount = words.length;
            // must have max 2 words
            if (wordCount > 2){
                dialog_et_col_1.requestFocus();
                throw new Exception("Item Name can have max 2 words");
            }

            // each word can have max 9 characters
            for (int i=0;i<wordCount;i++) {
                if (words[i].length()>9) {
                    dialog_et_col_1.requestFocus();
                    throw new Exception(words[i]+" exceeds 9 characters");
                }
            }

            double value = Double.parseDouble(dialog_et_col_2.getText().toString().trim());
            if (value < 1) {
                dialog_et_col_2.requestFocus();
                throw new Exception("Provide Item Value");
            }
            item = new SpecialItemModel(name, value);
        }catch (NumberFormatException e) {
            dialog_et_col_2.requestFocus();
            Toast.makeText(context, "ERROR : Provide correct Item Value.", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(context, "ERROR : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return item;
    }
}
