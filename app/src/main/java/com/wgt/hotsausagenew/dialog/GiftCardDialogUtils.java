package com.wgt.hotsausagenew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.model.BillModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by debasish on 02-01-2018.
 */

public class GiftCardDialogUtils {

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
    LinearLayout dialog_list_panel;
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
    LinearLayout dialog_header;
    @BindView(R.id.dialog_listview)
    ListView dialog_listview;
    private GiftCardSelectedListener listener;
    private Context context;
    private Dialog dialog;

    public GiftCardDialogUtils(GiftCardSelectedListener listener, Context context) {
        this.listener = listener;
        this.context = context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_data);
        ButterKnife.bind(this, dialog);

        modifyUiAsPerRequirement();
    }
    public void showDialog() {
        dialog.show();
    }
    @OnClick(R.id.dialog_btn_add_item)
    public void addSpecialItem() {
        BillModel model = validateData();
        if (model != null) {
            listener.onGiftCardSelectedListener(model);
            dialog.dismiss();
        }
    }

    private BillModel validateData() {
        BillModel billModel = null;
        try {
            String name = dialog_et_col_1.getText().toString();
            if (name.length() < 1){
                dialog_et_col_1.requestFocus();
                throw new Exception("Provide Item Name");
            }
            double price = Double.parseDouble(dialog_et_col_2.getText().toString());
            if (price < 1) {
                dialog_et_col_2.requestFocus();
                throw new Exception("Provide correct Price");
            }
            billModel = new BillModel(name, 1, price, R.id.btn_gift_sale);
        }catch (NumberFormatException e) {
            dialog_et_col_2.requestFocus();
            Toast.makeText(context, "ERROR : Provide correct price", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(context, "ERROR : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return billModel;
    }

    private void modifyUiAsPerRequirement() {
        dialog_title.setText("GIFT CARD");
        dialog_et_col_1.setHint("Enter Item Name");
        dialog_et_col_2.setHint("Enter Price");
        dialog_et_col_2.setInputType(InputType.TYPE_CLASS_NUMBER);

        dialog_et_col_3.setVisibility(View.GONE);
        dialog_iV_add.setVisibility(View.GONE);
        dialog_list_panel.setVisibility(View.GONE);
        dialog_add_panel.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.dialog_btn_cancel_item)
    public void hideAddUserPanel() {
        dialog.dismiss();
    }

    public interface GiftCardSelectedListener {
        void onGiftCardSelectedListener(BillModel bill);
    }
}
