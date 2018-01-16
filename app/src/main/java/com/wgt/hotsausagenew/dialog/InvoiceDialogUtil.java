package com.wgt.hotsausagenew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.utils.Constant;
import com.wgt.hotsausagenew.utils.LastTransactionPref;
import com.wgt.hotsausagenew.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 05-01-2018.
 */

public class InvoiceDialogUtil {

    @BindView(R.id.dialog_tv_total_bill)
    TextView dialog_tv_total_bill;
    @BindView(R.id.dialog_tv_discount)
    TextView dialog_tv_discount;
    @BindView(R.id.dialog_tv_payable)
    TextView dialog_tv_payable;
    @BindView(R.id.dialog_tv_received)
    TextView dialog_tv_received;
    @BindView(R.id.dialog_tv_deducted)
    TextView dialog_tv_deducted;
    @BindView(R.id.dialog_tv_returned)
    TextView dialog_tv_returned;
    @BindView(R.id.dialog_tv_payment_mode)
    TextView dialog_tv_payment_mode;
    @BindView(R.id.dialog_tv_last_transaction)
    TextView dialog_tv_last_transaction;
    private Dialog dialog;
    private Intent intent;
    private Context context;



    //constructor receives dialog
    public InvoiceDialogUtil(Dialog dialog, Intent intent, Context context) {
        this.dialog = dialog;
        this.intent = intent;
        this.context = context;
        ButterKnife.bind(this, dialog);
        modifyUiAsPerRequirement();
    }

    private void modifyUiAsPerRequirement() {
        if (intent != null) {
            try {
                String mode = intent.getStringExtra(Constant.KEY_PAYMENT_MODE_INTENT);
                float paid = intent.getFloatExtra(Constant.KEY_PAID_AMT_INTENT, 0);
                float payable = Float.parseFloat(intent.getStringExtra(Constant.KEY_PAYABLE_AMT_INTENT));
                float returned = paid - payable;
                dialog_tv_total_bill.setText(intent.getStringExtra(Constant.KEY_TOTAL_AMT_INTENT));
                dialog_tv_discount.setText(intent.getStringExtra(Constant.KEY_DISCOUNT_AMT_INTENT));
                dialog_tv_payable.setText("" + payable);

                if (mode.equals(Constant.PAYMENT_CASH)) {
                    dialog_tv_received.setText("" + paid);
                    dialog_tv_deducted.setText("" + payable);
                    dialog_tv_returned.setText("" + returned);
                } else {
                    dialog_tv_received.setText("" + payable);
                    dialog_tv_deducted.setText("" + payable);
                    dialog_tv_returned.setText("" + 0);
                }
                dialog_tv_payment_mode.setText("PAYMENT MODE : " + mode);
                dialog_tv_last_transaction.setText("" + new LastTransactionPref(context).getTime());
            } catch (NumberFormatException e) {
                ToastUtil.showToastGeneric(context, "ERROR : Data parsing error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showDialog() {
        dialog.show();
    }

}
