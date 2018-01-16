package com.wgt.hotsausagenew.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.model.TransactionModel;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionAdapter extends BaseAdapter {

    private List<TransactionModel> listOfTrans;
    private Context context;

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    //to disAllowed parentListView to accept touch events, when we touch on the inner listView
    private ListView parentListView;

    public TransactionAdapter(List<TransactionModel> listOfTrans, Context context) {
        this.listOfTrans = listOfTrans;
        this.context = context;
    }

    public void setParentListView(ListView parentListView) {
        this.parentListView = parentListView;
    }

    @Override
    public int getCount() {
        return listOfTrans.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfTrans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = null;
        FoldingCell cell = (FoldingCell) convertView;

        if(cell == null) {
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            cell = (FoldingCell) inflater.inflate(R.layout.transaction_view, null);
            holder = new ViewHolder(cell);
            holder.transaction_bill_list.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(parentListView != null) {
                            ((ViewParent)parentListView).requestDisallowInterceptTouchEvent(true);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if(parentListView != null) {
                            ((ViewParent)parentListView).requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    return false;
                }
            });
            cell.setTag(holder);
        }else {
            if (unfoldedIndexes.contains(position)) { // to remember their state
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            holder = (ViewHolder) cell.getTag();
        }
        final FoldingCell toggleCell = cell;
        TransactionModel singleTrans = (TransactionModel) listOfTrans.get(position);
        TransactionBillAdapter billingAdapter = new TransactionBillAdapter(context, singleTrans.getBillList());

        holder.expanded_time_value.setText(singleTrans.getTime());
        holder.expanded_sale_value.setText(Double.toString(singleTrans.getSale()));
        holder.tV_time.setText(singleTrans.getTime());
        holder.tV_sale.setText(Double.toString(singleTrans.getSale()));

        holder.tV_amount.setText(Double.toString(singleTrans.getAmount()));
        holder.tV_discount.setText(Double.toString(singleTrans.getDiscount()));

        holder.transaction_bill_list.setAdapter(billingAdapter);
        holder.transaction_expanded_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCell.toggle(false);
                registerToggle(position);
            }
        });

        return cell;
    }
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    class ViewHolder {
        @BindView(R.id.tV_time) TextView tV_time;
        @BindView(R.id.tV_sale) TextView tV_sale;
        @BindView(R.id.tV_amount) TextView tV_amount;
        @BindView(R.id.tV_discount) TextView tV_discount;
        @BindView(R.id.expanded_time_value) TextView expanded_time_value;
        @BindView(R.id.expanded_sale_value) TextView expanded_sale_value;
        @BindView(R.id.transaction_bill_list) ListView transaction_bill_list;
        @BindView(R.id.transaction_expanded_view) LinearLayout transaction_expanded_view;
        public ViewHolder(FoldingCell cell) {
            ButterKnife.bind(this,cell);
        }
    }
}
