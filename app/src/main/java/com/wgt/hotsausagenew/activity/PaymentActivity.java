package com.wgt.hotsausagenew.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import com.wgt.hotsausagenew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PaymentActivity extends AppCompatActivity {

    //row 1 and 2
    @BindView(R.id.tV_total_amt)
    TextView tV_total_amt;
    @BindView(R.id.tV_total)
    TextView tV_total;
    @BindView(R.id.tV_round_to_next_pound)
    TextView tV_round_to_next_pound;
    @BindView(R.id.tV_pound_5)
    TextView tV_pound_5;
    @BindView(R.id.tV_pound_10)
    TextView tV_pound_10;
    @BindView(R.id.tV_pound_20)
    TextView tV_pound_20;

    //calculator views
    @BindView(R.id.calc_0)
    Button calc_0;
    @BindView(R.id.calc_1)
    Button calc_1;
    @BindView(R.id.calc_2)
    Button calc_2;
    @BindView(R.id.calc_3)
    Button calc_3;
    @BindView(R.id.calc_4)
    Button calc_4;
    @BindView(R.id.calc_5)
    Button calc_5;
    @BindView(R.id.calc_6)
    Button calc_6;
    @BindView(R.id.calc_7)
    Button calc_7;
    @BindView(R.id.calc_8)
    Button calc_8;
    @BindView(R.id.calc_9)
    Button calc_9;
    @BindView(R.id.calc_period)
    Button calc_period;
    @BindView(R.id.calc_clear)
    Button calc_clear;

    //base row 3
    @BindView(R.id.btn_card)
    Button btn_card;
    @BindView(R.id.tV_amt_paid)
    TextView tV_amt_paid;
    @BindView(R.id.btn_change)
    Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.calc_0, R.id.calc_1, R.id.calc_2, R.id.calc_3, R.id.calc_4, R.id.calc_5, R.id.calc_6, R.id.calc_7, R.id.calc_8, R.id.calc_9, R.id.calc_period, R.id.calc_clear})
    public void calculatorInput(Button btn) {
        int id = btn.getId();
        if (id == R.id.calc_clear)
            tV_amt_paid.setText("0.00");
        else {
            String existing_amt = tV_amt_paid.getText().toString();
            if (existing_amt.equals("0.00"))
                existing_amt = "";

            int period_pos = existing_amt.indexOf(".");
            if (period_pos != -1) {//a period already exists,so avoid next period or more than two digits after period_pos
                if (id == R.id.calc_period)
                    return;
                else {
                    String digits_after_period = existing_amt.substring(period_pos + 1);
                    if (digits_after_period.length() >= 2)
                        return;//avoid more than two digits after pos_period

                    tV_amt_paid.setText(existing_amt + btn.getText());
                }
            } else
                tV_amt_paid.setText(existing_amt + btn.getText());

        }
    }

    @OnTouch({R.id.btn_card,R.id.btn_change})
    public boolean btn_pressed(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.card_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.card_button_raised);
        }
        return false;
    }
}
