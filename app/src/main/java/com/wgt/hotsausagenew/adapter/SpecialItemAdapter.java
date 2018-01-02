package com.wgt.hotsausagenew.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.model.SpecialItemModel;

import java.util.ArrayList;
import java.util.List;

public class SpecialItemAdapter extends BaseAdapter {

    private Context context;
    private List<SpecialItemModel> specialItemsList;

    public SpecialItemAdapter(Context context, List<SpecialItemModel> specialItemsList) {
        this.context = context;
        if(specialItemsList == null) {
            this.specialItemsList = new ArrayList<>();
        } else {
            this.specialItemsList = specialItemsList;
        }
    }

    @Override
    public int getCount() {
        return specialItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return specialItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        LayoutInflater inflater = null;
        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dialog_add_data_list_item, null);

            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.dialog_add_data_layout);
            layout.setWeightSum(2);
            TextView third_col = (TextView) convertView.findViewById(R.id.dialog_listitem_col_3);
            third_col.setVisibility(View.GONE);

            holder = new ViewHolder();
            holder.desc = (TextView) convertView.findViewById(R.id.dialog_listitem_col_1);
            holder.price = (TextView) convertView.findViewById(R.id.dialog_listitem_col_2);
            //holder.layout = (LinearLayout) convertView.findViewById(R.id.special_single_item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SpecialItemModel specialItem = (SpecialItemModel) getItem(position);
        holder.desc.setText(specialItem.getProd());
        holder.price.setText(Double.toString(specialItem.getRate()));
       /* holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onSpecialItemSelected(key, specialItem.getItemName());
                }
            }
        });*/
        return convertView;
    }


    private class ViewHolder {
        TextView desc, price;
        //LinearLayout layout;
    }


    //======================USER_DEFINED_AREA========================//

    public boolean addItem(SpecialItemModel item) {
        for(SpecialItemModel si : specialItemsList) {
            if (si.getProd().equals(item.getProd())){
                Toast.makeText(context, "Item : "+item.getProd()+" already exists.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        specialItemsList.add(item);
        notifyDataSetChanged();
        return true;
    }
}
