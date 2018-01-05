package com.wgt.hotsausagenew.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PaymentActivity extends AppCompatActivity {

    //row 1 and 2
    @BindView(R.id.tV_total_amt)
    TextView tV_total_amt;
    @BindView(R.id.btn_total)
    Button btn_total;
    @BindView(R.id.btn_round_to_next_pound)
    Button btn_round_to_next_pound;
    @BindView(R.id.btn_pound_5)
    Button btn_pound_5;
    @BindView(R.id.btn_pound_10)
    Button btn_pound_10;
    @BindView(R.id.btn_pound_20)
    Button btn_pound_20;

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

    String intent_val_payable_amount = null;//list of buttons to enable and disable when cash/card is pressed
    int deno_20 = 0, deno_10 = 0, deno_5 = 0, deno_round = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        intent_val_payable_amount = getIntent().getStringExtra("tV_payable_amount");
        tV_total_amt.setText(intent_val_payable_amount);
    }

    @OnClick({R.id.calc_0, R.id.calc_1, R.id.calc_2, R.id.calc_3, R.id.calc_4, R.id.calc_5, R.id.calc_6, R.id.calc_7, R.id.calc_8, R.id.calc_9, R.id.calc_period, R.id.calc_clear})
    public void calculatorInput(Button btn) {
        int id = btn.getId();
        if (id == R.id.calc_clear)
            clearData();
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

    @OnTouch({R.id.btn_card, R.id.btn_change})
    public boolean btn_pressed(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.card_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.card_button_raised);

            if (button.getId() == R.id.btn_card) {
                Toast.makeText(this, "Paid via card.Thanks!!", Toast.LENGTH_SHORT).show();
            } else {
                float bill = Float.valueOf(tV_total_amt.getText().toString());
                float paid = Float.valueOf(tV_amt_paid.getText().toString());
                float amount = paid - bill;
                int integral_amt = (int) amount;
                float fractional_amt = amount - integral_amt;

                if (amount < 0) {
                    Toast.makeText(this, "Insufficient Funds", Toast.LENGTH_SHORT).show();
                    return false;
                } else
                    Toast.makeText(this, "Paid via cash.Thanks!!", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    @OnTouch({R.id.btn_total, R.id.btn_round_to_next_pound, R.id.btn_pound_5, R.id.btn_pound_10, R.id.btn_pound_20})
    public boolean denominationInput(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.login_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.login_button);
            String new_total = tV_amt_paid.getText().toString();

            if (button.getId() == R.id.btn_pound_20) {
                new_total = (Double.valueOf(new_total) + 20 + "");
                btn_pound_20.setText("£20:" + ++deno_20);
            } else if (button.getId() == R.id.btn_pound_10) {
                new_total = (Double.valueOf(new_total) + 10 + "");
                btn_pound_10.setText("£10:" + ++deno_10);
            } else if (button.getId() == R.id.btn_pound_5) {
                new_total = (Double.valueOf(new_total) + 5 + "");
                btn_pound_5.setText("£5:" + ++deno_5);
            } else if (button.getId() == R.id.btn_round_to_next_pound) {
                new_total = (Double.valueOf(new_total) + 1 + "");
                btn_round_to_next_pound.setText("Round to next\npound:" + Constant.poundSign + ++deno_round);
            } else//clear all
            {
                clearData();
                return false;
            }

            tV_amt_paid.setText(new_total);
            btn_total.setText("Total:" + Constant.poundSign + new_total);

        }
        return false;
    }

    private void clearData() {
        deno_20 = deno_10 = deno_5 = deno_round = 0;
        tV_amt_paid.setText("0.00");
        btn_total.setText("Total: " + Constant.poundSign + 0);


        btn_round_to_next_pound.setText("Round to next\npound");
        btn_pound_5.setText("£5");
        btn_pound_10.setText("£10");
        btn_pound_20.setText("£20");

    }
}
