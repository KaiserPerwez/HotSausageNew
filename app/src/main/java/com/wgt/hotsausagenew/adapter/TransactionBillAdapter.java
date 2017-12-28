package com.wgt.hotsausagenew.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.model.BillModel;
import com.wgt.hotsausagenew.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionBillAdapter extends BaseAdapter {

    private Context context;
    private List<BillModel> list;

    public TransactionBillAdapter(Context context, List<BillModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater;
        if(convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.billing_item_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        BillModel bill = (BillModel) list.get(position);
        holder.tV_product.setText(bill.getProduct());
        holder.tV_quantity.setText(Constant.multiplySign+bill.getQuantity());
        holder.tV_amount.setText(Constant.poundSign+bill.getPrice());

        return convertView;

    }

    class ViewHolder {

        @BindView(R.id.tV_product) TextView tV_product;
        @BindView(R.id.tV_quantity) TextView tV_quantity;
        @BindView(R.id.tV_amount) TextView tV_amount;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this,itemView);
        }
    }
}
