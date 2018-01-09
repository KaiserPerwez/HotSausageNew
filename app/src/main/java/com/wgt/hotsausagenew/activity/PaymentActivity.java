package com.wgt.hotsausagenew.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.utils.Constant;
import com.wgt.hotsausagenew.utils.LastTransactionPref;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PaymentActivity extends AppCompatActivity {

    //custom interface to hide/show views
    static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override
        public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };
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
    //list of buttons to enable and disable when cash/card is pressed
    @BindViews({R.id.calc_0, R.id.calc_1, R.id.calc_2, R.id.calc_3, R.id.calc_4, R.id.calc_5, R.id.calc_6, R.id.calc_7, R.id.calc_8, R.id.calc_9, R.id.calc_period})
    List<Button> list_calciButtons;
    @BindViews({R.id.btn_round_to_next_pound, R.id.btn_pound_5, R.id.btn_pound_10, R.id.btn_pound_20})
    List<Button> list_denominationButtons;
    Intent prev_intent;
    int deno_20 = 0, deno_10 = 0, deno_5 = 0, deno_round = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        prev_intent = getIntent();
        String intent_val_payable_amount = null;
        intent_val_payable_amount = prev_intent.getStringExtra(Constant.KEY_PAYABLE_AMT_INTENT);
        tV_total_amt.setText(intent_val_payable_amount);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @OnTouch({R.id.btn_total, R.id.btn_round_to_next_pound, R.id.btn_pound_5, R.id.btn_pound_10, R.id.btn_pound_20})
    public boolean denominationInput(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.login_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.login_button);
            ButterKnife.apply(list_calciButtons, ENABLED, false);
            String new_total = btn_total.getText().toString();
            new_total = new_total.substring(new_total.indexOf(Constant.poundSign) + 1);

            if (button.getId() == R.id.btn_pound_20) {
                new_total = (Integer.valueOf(new_total) + 20 + "");
                btn_pound_20.setText("£20 x " + ++deno_20);
            } else if (button.getId() == R.id.btn_pound_10) {
                new_total = (Integer.valueOf(new_total) + 10 + "");
                btn_pound_10.setText("£10 x " + ++deno_10);
            } else if (button.getId() == R.id.btn_pound_5) {
                new_total = (Integer.valueOf(new_total) + 5 + "");
                btn_pound_5.setText("£5 x " + ++deno_5);
            } else if (button.getId() == R.id.btn_round_to_next_pound) {
                new_total = (Integer.valueOf(new_total) + 1 + "");
                btn_round_to_next_pound.setText("Round to next\npound x " + Constant.poundSign + ++deno_round);
            }
//            tV_amt_paid.setText(new_total);
            btn_total.setText("Total:" + Constant.poundSign + new_total);

        }
        return false;
    }

    @OnClick({R.id.calc_0, R.id.calc_1, R.id.calc_2, R.id.calc_3, R.id.calc_4, R.id.calc_5, R.id.calc_6, R.id.calc_7, R.id.calc_8, R.id.calc_9, R.id.calc_period})
    public void calculatorInput(Button btn) {
        ButterKnife.apply(list_denominationButtons, ENABLED, false);
        int id = btn.getId();
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

    @OnClick({R.id.btn_total, R.id.calc_clear})
    public void clearInputData(Button button) {
        if (button.getId() == R.id.btn_total) {
            ButterKnife.apply(list_calciButtons, ENABLED, true);
            deno_20 = deno_10 = deno_5 = deno_round = 0;
            btn_total.setText("Total: " + Constant.poundSign + 0);
            btn_round_to_next_pound.setText("Round to next\npound");
            btn_pound_5.setText("£5");
            btn_pound_10.setText("£10");
            btn_pound_20.setText("£20");
        } else //clear
        {
            tV_amt_paid.setText("0.00");
            ButterKnife.apply(list_denominationButtons, ENABLED, true);
        }
    }

    @OnTouch({R.id.btn_card, R.id.btn_change})
    public boolean payment_btn_pressed(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.card_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.card_button_raised);
            float paid = 0.0f;
            if (button.getId() == R.id.btn_card) {
                {
                    prev_intent.putExtra(Constant.KEY_PAYMENT_MODE_INTENT, Constant.PAYMENT_CARD);
                }
            } else {
                float bill = Float.valueOf(tV_total_amt.getText().toString());
                paid = Float.valueOf(tV_amt_paid.getText().toString());
                if (!(paid > 0.0f)) {
                    String new_total = btn_total.getText().toString();
                    new_total = new_total.substring(new_total.indexOf(Constant.poundSign) + 1);
                    paid = Float.valueOf(new_total);
                }
                float amount = paid - bill;

                if (amount < 0) {
                    Toast.makeText(this, "Insufficient Funds", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    prev_intent.putExtra(Constant.KEY_PAYMENT_MODE_INTENT, Constant.PAYMENT_CASH);
                }

            }

            // get current time and save it as last transaction time
            saveCurrenttime();

            prev_intent.putExtra(Constant.KEY_PAID_AMT_INTENT, paid);
            setResult(Activity.RESULT_OK, prev_intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
        return false;
    }

    private void saveCurrenttime() {
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        new LastTransactionPref(this).saveTime(h + ":" + m);
    }
}
