package com.wgt.hotsausagenew.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.model.DiscountModel;

import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends BaseAdapter {
    private Context context;
    private List<DiscountModel> list;

    public DiscountAdapter(Context context, List<DiscountModel> list) {
        this.context = context;
        if (list == null) {
            this.list = new ArrayList<>();
        } else {
            this.list = list;
        }
    }



    public void addNewDiscount(DiscountModel discount) {
        list.add(discount);
        notifyDataSetChanged();
    }
    public void deleteItem(String itemName) {
        for(int i=0; i<list.size();i++) {
            if (list.get(i).getProduct().equals(itemName)){
                list.remove(i);
            }
        }
        notifyDataSetChanged();
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
        LayoutInflater inflater = null;
        ViewHolder holder = null;
        if(convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dialog_add_data_list_item, null);
            holder = new ViewHolder();
            holder.itemName = convertView.findViewById(R.id.dialog_listitem_col_1);
            holder.itemValue = convertView.findViewById(R.id.dialog_listitem_col_2);
            holder.itemDesc = convertView.findViewById(R.id.dialog_listitem_col_3);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        DiscountModel discount = (DiscountModel) list.get(position);
        holder.itemName.setText(discount.getProduct());
        holder.itemValue.setText(Double.toString(discount.getRate()));
        holder.itemDesc.setText(discount.getProdDesc());

        return convertView;
    }

    class ViewHolder {
        TextView itemName, itemValue, itemDesc;
    }


}
