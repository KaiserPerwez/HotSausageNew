package com.wgt.hotsausagenew.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.model.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 29-12-2017.
 */

public class AddUserAdapter extends BaseAdapter {

    List<UserModel> userModelList = null;
    Context ctx;

    public AddUserAdapter(List<UserModel> userModelList, Context ctx) {
        this.userModelList = userModelList;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return userModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return userModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userModelList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater;
        if (convertView == null) {
            inflater = (LayoutInflater) ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dialog_add_data_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserModel userModel = userModelList.get(position);
        holder.dialog_listitem_col_1.setText(userModel.username);
        holder.dialog_listitem_col_2.setText(userModel.password);
        holder.dialog_listitem_col_3.setText(userModel.site);
        return convertView;
    }

    //--------------------user defined-------------------//
    public void addItem(UserModel userModel) {
        for (int position = 0; position < userModelList.size(); position++) {
            UserModel item = userModelList.get(position);  // item at i'th position
            if (item.username.equals(userModel.username)) {
                // item matched ,increment quantity and price instead of adding it as new bill-item;
                Toast.makeText(ctx, "User already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        userModelList.add(userModel);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position < userModelList.size()) {
            userModelList.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeItem(UserModel userModel) {
        if (userModelList.contains(userModel)) {
            userModelList.remove(userModel);
            notifyDataSetChanged();
        }
    }

    public void removeItem(String itemName) {
        for (UserModel userModel :
                userModelList) {
            if (userModel.username.equalsIgnoreCase(itemName)) {
                userModelList.remove(userModel);
                notifyDataSetChanged();
                break;
            }
        }
    }

    ////////////-----------------------------------///////////////////////////
    class ViewHolder {
        @BindView(R.id.dialog_listitem_col_1)
        TextView dialog_listitem_col_1;
        @BindView(R.id.dialog_listitem_col_2)
        TextView dialog_listitem_col_2;
        @BindView(R.id.dialog_listitem_col_3)
        TextView dialog_listitem_col_3;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
