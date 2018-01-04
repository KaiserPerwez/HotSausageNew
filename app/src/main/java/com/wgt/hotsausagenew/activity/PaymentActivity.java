package com.wgt.hotsausagenew.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PaymentActivity extends AppCompatActivity {

    static final ButterKnife.Setter<View, Boolean> ENABLE = new ButterKnife.Setter<View, Boolean>() {
        @Override
        public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };
    //row 1 and 2
    @BindView(R.id.tV_total_amt)
    TextView tV_total_amt;
    @BindView(R.id.tV_total)
    TextView tV_total;
    @BindView(R.id.tV_round_to_next_pound)
    TextView tV_round_to_next_pound;
    @BindView(R.id.tV_pound_1)
    TextView tV_pound_1;
    @BindView(R.id.tV_pound_2)
    TextView tV_pound_2;
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
    @BindViews({R.id.calc_0, R.id.calc_1, R.id.calc_2, R.id.calc_3, R.id.calc_4, R.id.calc_5, R.id.calc_6, R.id.calc_7, R.id.calc_8, R.id.calc_9, R.id.calc_period, R.id.calc_clear})
    List<Button> buttonList_changeBtn;
    String intent_val_payable_amount = null;

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

    @OnTouch({R.id.btn_card, R.id.btn_change})
    public boolean btn_pressed(Button button, MotionEvent motionEvent) {
        btn_change.setEnabled(true);
        btn_card.setEnabled(true);
        ButterKnife.apply(buttonList_changeBtn, ENABLE, true);
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.card_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.card_button_raised);

            if (button.getId() == R.id.btn_card) {
                btn_change.setEnabled(false);
                Toast.makeText(this, "Paid via card.Thanks!!", Toast.LENGTH_SHORT).show();
                ButterKnife.apply(buttonList_changeBtn, ENABLE, false);
            } else {
                btn_card.setEnabled(false);

                float bill = Float.valueOf(tV_total_amt.getText().toString());
                float paid = Float.valueOf(tV_amt_paid.getText().toString());
                float amount = paid - bill;
                int integral_amt = (int) amount;
                float fractional_amt = amount - integral_amt;

                if (amount < 0) {
                    Toast.makeText(this, "Insufficient Funds", Toast.LENGTH_SHORT).show();
                    return false;
                }
                int denomination_20 = 0, denomination_10 = 0, denomination_5 = 0, denomination_2 = 0, denomination_1 = 0, denomination_round = 0;
                if (integral_amt > 20) {
                    denomination_20 = (int) (integral_amt / 20);
                    integral_amt = integral_amt % 20;
                }
                if (integral_amt > 10) {
                    denomination_10 = (int) (integral_amt / 10);
                    integral_amt = integral_amt % 10;
                }
                if (integral_amt > 5) {
                    denomination_5 = (int) (integral_amt / 5);
                    integral_amt = integral_amt % 5;
                }
                if (integral_amt > 2) {
                    denomination_2 = (int) (integral_amt / 2);
                    integral_amt = integral_amt % 2;
                }
                denomination_1 = integral_amt;
                denomination_round = (int) Math.round(fractional_amt);
                int total = 20 * denomination_20 + 10 * denomination_10 + 5 * denomination_5 + 2 * denomination_2 + denomination_1 + denomination_round;

                tV_pound_20.setText(Constant.poundSign + "20=>" + denomination_20);
                tV_pound_10.setText(Constant.poundSign + "10=>" + denomination_10);
                tV_pound_5.setText(Constant.poundSign + "5=>" + denomination_5);
                tV_pound_2.setText(Constant.poundSign + "2=>" + denomination_2);
                tV_pound_1.setText(Constant.poundSign + "1=>" + denomination_1);
                tV_round_to_next_pound.setText("Rounded=> " + Constant.poundSign + denomination_round);
                tV_total.setText("Total=> " + Constant.poundSign + total);
            }
        }
        return false;
    }
}
