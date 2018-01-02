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
import com.wgt.hotsausagenew.adapter.DiscountAdapter;
import com.wgt.hotsausagenew.model.DiscountModel;
import com.wgt.hotsausagenew.model.SpecialItemModel;
import com.wgt.hotsausagenew.utils.Pref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by debasish on 02-01-2018.
 */

public class DiscountDialogUtil implements AdapterView.OnItemLongClickListener,
                                            AdapterView.OnItemClickListener, DeleteDataDialogUtil.DeletionSelected {

    private Dialog dialog;
    private DiscountAdapter adapter;
    private Context context;
    private Pref pref;
    private DiscountDialogListener listener;

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

    public DiscountDialogUtil(Context context) {
        this.context = context;
        pref = new Pref(context);
        adapter = new DiscountAdapter(context, pref.getDiscountsPref());

        this.dialog = new Dialog(context);// Create custom dialog object
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_data);

        ButterKnife.bind(this, dialog);
        modifyUiAsPerRequirement();
        dialog_listview.setAdapter(adapter);
        dialog_listview.setOnItemClickListener(this);
        dialog_listview.setOnItemLongClickListener(this);
    }

    private void modifyUiAsPerRequirement() {
        dialog_title.setText("DISCOUNTS");
        dialog_et_col_1.setHint("Enter Product");
        dialog_et_col_2.setHint("Enter Price");
        dialog_et_col_2.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog_et_col_3.setHint("Enter Description");

        dialog_header_col_1.setText("Product");
        dialog_header_col_2.setText("Price");
        dialog_header_col_3.setText("Description");
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
    public void addSpecialItem() {
        DiscountModel discountModel = null;
        if (validateData()) {
            discountModel = new DiscountModel(dialog_et_col_1.getText().toString().trim(),
                    dialog_et_col_3.getText().toString().trim(),
                    Double.parseDouble(dialog_et_col_2.getText().toString().trim())
            );
            boolean b = pref.saveDiscount(discountModel);
            if (b){
                adapter.addNewDiscount(discountModel);
                hideAddUserPanel();
            }
        }
    }

    public void showDialog() {
        dialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Dialog delDialog = new Dialog(context);// Create custom dialog object
        delDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        delDialog.setCancelable(true);
        delDialog.setContentView(R.layout.dialog_delete_alert);
        delDialog.show();

        String itemToBeDeleted = pref.getDiscountsPref().get(position).getProduct();
        DeleteDataDialogUtil deleteDataDialogUtil = new DeleteDataDialogUtil(itemToBeDeleted, this, delDialog);

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog.dismiss();
        listener.onDiscountselected(pref.getDiscountsPref().get(position));
    }

    private boolean validateData() {
        boolean success = false;
        try {
            String name = dialog_et_col_1.getText().toString().trim();
            String desc = dialog_et_col_3.getText().toString().trim();


            if (name == null || name.length() < 1) {
                dialog_et_col_1.requestFocus();
                throw new Exception("Provide Item Name");
            }
            double value = Double.parseDouble(dialog_et_col_2.getText().toString());
            if (value == 0 || value < 1) {
                dialog_et_col_2.requestFocus();
                throw new Exception("Provide Item Value");
            }
            if (desc == null || desc.length() < 1) {
                dialog_et_col_3.requestFocus();
                throw new Exception("Provide Item Description");
            }
            success = true;
        } catch (NumberFormatException e) {
            dialog_et_col_2.requestFocus();
            Toast.makeText(context, "ERROR : provide correct value", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "ERROR : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return success;
    }

    @Override
    public void onDeletionSelected(String itemName) {
        pref.deleteItem(itemName);
        adapter.deleteItem(itemName);
    }

    public interface DiscountDialogListener {
        void onDiscountselected(DiscountModel discount);
    }

    public void setDiscountDialogListener (DiscountDialogListener listener) {
        this.listener = listener;
    }
}
