package com.wgt.hotsausagenew.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ramotion.foldingcell.FoldingCell;
import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.adapter.TransactionAdapter;
import com.wgt.hotsausagenew.model.BillModel;
import com.wgt.hotsausagenew.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    private ListView listView;

    private String[] productList= {"Biryani","Pizza","Burger","Drink","Biryani","Pizza","Burger","Drink"};
    private int[] quantityList = {1,2,3,2,4,2,3,2};
    private double[] priceList= {20.3,10,21.2,22,3.21,23,44,23.22};
    private List<BillModel> listOfBills;//for each transaction

    private String[] times = {"12:22", "2:34", "10:00", "12:22", "2:34", "10:00", "12:22", "2:34", "10:00", "20:20"};
    private double[] amounts = {20.22, 33.30, 20.22, 33.30, 20.22, 33.30, 20.22, 33.30, 20.22, 33.30};
    private double[] discounts = {20.22, 33.30, 20.22, 33.30, 20.22, 33.30, 20.22, 33.30, 20.22, 33.30};
    private double[] sales = {20.22, 33.30, 20.22, 33.30, 20.22, 33.30, 20.22, 33.30, 20.22, 33.30};
    private List<TransactionModel> listOfTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        listOfBills = new ArrayList<>();
        listOfTrans = new ArrayList<>();

        for(int i=0; i<productList.length;i++) {
            listOfBills.add(new BillModel(productList[i], quantityList[i], priceList[i], 0));
        }
        for (int i=0; i<times.length;i++) {
            listOfTrans.add(new TransactionModel(times[i], sales[i], amounts[i], discounts[i], listOfBills));
        }

        final TransactionAdapter adapter = new TransactionAdapter(listOfTrans, this);
        listView = (ListView) findViewById(R.id.transaction_list);
        adapter.setParentListView(listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((FoldingCell)view).toggle(false);
                adapter.registerToggle(position);
            }
        });
    }
}
