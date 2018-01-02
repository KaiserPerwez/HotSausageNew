package com.wgt.hotsausagenew.dialog;

import android.app.Dialog;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wgt.hotsausagenew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 29-12-2017.
 */

public class DeleteDataDialogUtil {
    String itemName;
    Dialog dialog;
    DeletionSelected deletionSelectedListener;

    @BindView(R.id.tv_data)
    TextView tv_data;
    @BindView(R.id.yes_layout)
    LinearLayout yes_layout;
    @BindView(R.id.cancel_layout)
    LinearLayout cancel_layout;

    public DeleteDataDialogUtil(String itemName, DeletionSelected deletionSelectedListener, Dialog dialog) {
        this.itemName = itemName;
        this.deletionSelectedListener = deletionSelectedListener;
        this.dialog = dialog;
        ButterKnife.bind(this, dialog);
        tv_data.setText("Delete " + itemName + " ?");
    }

    @OnClick(R.id.cancel_layout)
    public void cancelDialog() {
        dialog.dismiss();
    }

    @OnClick(R.id.yes_layout)
    public void acceptDeletion() {
        deletionSelectedListener.onDeletionSelected(itemName);
        cancelDialog();
    }


    public interface DeletionSelected {
        public void onDeletionSelected(String itemName);
    }
}
