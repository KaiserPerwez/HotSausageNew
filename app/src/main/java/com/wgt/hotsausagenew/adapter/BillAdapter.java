package com.wgt.hotsausagenew.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.model.BillModel;
import com.wgt.hotsausagenew.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 27-12-2017.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private List<BillModel> list;
    private RecyclerView rv;
    public BillAdapter() {
        this.list = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_item_view, parent, false);
        if (rv == null && parent instanceof RecyclerView)
            rv = (RecyclerView) parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BillModel billModel = list.get(position);
        holder.tV_product.setText(billModel.getProduct());
        holder.tV_quantity.setText(Constant.multiplySign + billModel.getQuantity());
        holder.tV_amount.setText(Constant.poundSign + billModel.getRate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    //-----------------------------------------------------User defined Functions---------------------------//
    public BillModel getItem(int position) {
        return list.get(position);
    }

    public void addItem(BillModel bill) {
        for (int position = 0; position < list.size(); position++) {
            BillModel item = list.get(position);  // item at i'th position
            if (item.getProduct().equals(bill.getProduct())) {
                // item matched ,increment quantity and price instead of adding it as new bill-item;
                item.setQuantity(item.getQuantity() + 1);
                item.setRate(item.getRate() + bill.getRate());
                notifyItemChanged(position);
                //rv.scrollToPosition(position);//allow auto-scroll to item at pos
                return;
            }
        }

        // nothing matched.. add new bill
        list.add(new BillModel(bill.getProduct(), 1, bill.getRate()));
        notifyItemInserted(list.size());
        //rv.scrollToPosition(list.size());//allow auto-scroll to item at pos
        return;
    }

    public void removeItem(int position) {
        list.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void removeAll() {
        int start = 0, count = list.size();
        list.clear();
        notifyItemRangeRemoved(start, count);
    }

    public void restoreDeletedItem(BillModel item, int position) {
        list.add(position, item);
        // notify the item added by position
        // to perform recycler view animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemInserted(position);
        rv.scrollToPosition(position);//allow auto-scroll to item at pos
    }
//-----------------------------------------------------------------------------------------------------//

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.foreground_view)
        public LinearLayout foreground_view;//made public to allow access from RecyclerItemTouchHelper.java in other package
        @BindView(R.id.tV_product)
        TextView tV_product;
        @BindView(R.id.tV_quantity)
        TextView tV_quantity;
        @BindView(R.id.tV_amount)
        TextView tV_amount;
        @BindView(R.id.background_view)
        LinearLayout background_view;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
