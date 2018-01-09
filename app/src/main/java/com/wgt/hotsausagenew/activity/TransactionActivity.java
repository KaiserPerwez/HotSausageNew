package com.wgt.hotsausagenew.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.adapter.TransactionAdapter;
import com.wgt.hotsausagenew.database.AppDatabase;
import com.wgt.hotsausagenew.model.BillModel;
import com.wgt.hotsausagenew.model.TransactionModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionActivity extends AppCompatActivity {
    @BindView(R.id.txt_tot_trans_value)
    TextView txt_tot_trans_value;
    @BindView(R.id.txt_total_amount_value)
    TextView txt_total_amount_value;
    private ListView listView;
    private List<BillModel> listOfBills;//for each transaction
    private List<TransactionModel> listOfTrans;
    private AppDatabase database;
    private int transCount;
    private double amountCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        ButterKnife.bind(this);
        database = AppDatabase.getDatabase(this);

        listOfBills = new ArrayList<>();
        listOfTrans = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int mnth = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        listOfTrans = database.transactionDAO().getAllTransByDate(date, mnth, year);
        transCount = listOfTrans.size();
        for (TransactionModel transactionModel : listOfTrans) {
            int id = transactionModel.getId();
            listOfBills = database.billDAO().getAllBillById(id);
            transactionModel.setBillList(listOfBills);

            //counting all amount
            for (BillModel billModel : listOfBills) {
                amountCount += billModel.getRate();
            }
        }

        txt_tot_trans_value.setText("" + transCount);
        txt_total_amount_value.setText("" + amountCount);

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
