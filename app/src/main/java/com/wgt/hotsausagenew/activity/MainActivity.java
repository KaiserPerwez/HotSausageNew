package com.wgt.hotsausagenew.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.adapter.BillAdapter;
import com.wgt.hotsausagenew.dialog.DiscountDialogUtil;
import com.wgt.hotsausagenew.dialog.GiftCardDialogUtils;
import com.wgt.hotsausagenew.dialog.SpecialDialogUtil;
import com.wgt.hotsausagenew.helper.RecyclerItemTouchHelper;
import com.wgt.hotsausagenew.model.BillModel;
import com.wgt.hotsausagenew.model.DiscountModel;
import com.wgt.hotsausagenew.model.SpecialItemModel;
import com.wgt.hotsausagenew.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

public class MainActivity
        extends AppCompatActivity
        implements
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        View.OnTouchListener,
        SpecialDialogUtil.SpecialDialogItemClickListener,
        DiscountDialogUtil.DiscountDialogListener,
        GiftCardDialogUtils.GiftCardSelectedListener,
        BillAdapter.BillAddedListener {

    //column 1
    @BindView(R.id.btn_regular)
    Button btn_regular;
    @BindView(R.id.btn_reg_cheese)
    Button btn_reg_cheese;
    @BindView(R.id.btn_save_50)
    Button btn_save_50;
    @BindView(R.id.btn_reg_sausage)
    Button btn_reg_sausage;

    //column 2
    @BindView(R.id.btn_large)
    Button btn_large;
    @BindView(R.id.btn_large_cheese)
    Button btn_large_cheese;
    @BindView(R.id.btn_save_100)
    Button btn_save_100;
    @BindView(R.id.btn_large_sausage)
    Button btn_large_sausage;

    //column 3
    @BindView(R.id.btn_special_1)
    Button btn_special_1;
    @BindView(R.id.btn_special1_opt)
    Button btn_special1_opt;
    @BindView(R.id.btn_drink)
    Button btn_drink;
    @BindView(R.id.btn_footlong_sausage)
    Button btn_footlong_sausage;

    //column 4
    @BindView(R.id.btn_special_2)
    Button btn_special_2;
    @BindView(R.id.btn_special2_opt)
    Button btn_special2_opt;
    @BindView(R.id.btn_extra_cheese)
    Button btn_extra_cheese;
    @BindView(R.id.btn_gift_sale)
    Button btn_gift_sale;

    //column 5
    @BindView(R.id.btn_footlong)
    Button btn_footlong;
    @BindView(R.id.btn_footlong_cheese)
    Button btn_footlong_cheese;
    @BindView(R.id.btn_discount)
    Button btn_discount;
    @BindView(R.id.btn_gift_used)
    Button btn_gift_used;

    //Billing Pane
    @BindView(R.id.billing_pane_layout)
    ConstraintLayout billing_pane_layout;
    @BindView(R.id.rV_billing_list)
    RecyclerView rV_billing_list;
    @BindView(R.id.tV_lastTransaction)
    TextView tV_lastTransaction;
    @BindView(R.id.iV_clearBill)
    ImageView iV_clearBill;
    @BindView(R.id.tV_total_amount)
    TextView tV_total_amount;
    @BindView(R.id.tV_discount_amount)
    TextView tV_discount_amount;
    @BindView(R.id.tV_payable_amount)
    TextView tV_payable_amount;
    boolean longClicked = false;//to avoid firing click event along with longclick
    private BillAdapter billAdapter;
    private Handler handler;
    private boolean backPressed = false;

    private double discountPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handler = new Handler();
        InitialiseRecyclerViewWithAdapter();
    }

    //onSwipe implementation of RecyclerItemTouchHelperListener ie event on onSwiping of bill items to delete
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BillAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = billAdapter.getItem(viewHolder.getAdapterPosition()).getProduct();

            // backup of removed item for undo purpose
            final BillModel deletedItem = billAdapter.getItem(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            billAdapter.removeItem(viewHolder.getAdapterPosition());

            // update billing Amount
            changePrice(-deletedItem.getRate(), deletedItem.getId());

            if (billAdapter.getItemCount() == 0) {
                discountPercentage = 0;
                toggleSaverButton(true);
            }
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(rV_billing_list, name + " removed from list!", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            billAdapter.restoreDeletedItem(deletedItem, deletedIndex);
                        }
                    })
                    .setActionTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    @OnTouch({R.id.btn_save_50, R.id.btn_save_100})
    public boolean onSaverButtonPressedButton(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            button.setBackgroundResource(R.drawable.pressed_button);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (button.getId() == R.id.btn_save_50) {
                discountPercentage = 0.5;
                toggleSaverButton(false);
            } else if (button.getId() == R.id.btn_save_100) {
                discountPercentage = 1;
                toggleSaverButton(false);
            }
        }

        return false;
    }

    @OnTouch({R.id.btn_regular, R.id.btn_reg_cheese, R.id.btn_reg_sausage,         //touch column1
            R.id.btn_large, R.id.btn_large_cheese, R.id.btn_large_sausage,      //touch column2
            R.id.btn_special_1, R.id.btn_special1_opt, R.id.btn_drink, R.id.btn_footlong_sausage,  //touch column3
            R.id.btn_special_2, R.id.btn_special2_opt, R.id.btn_extra_cheese, R.id.btn_gift_sale,  //touch column4
            R.id.btn_footlong, R.id.btn_footlong_cheese, R.id.btn_discount, R.id.btn_gift_used     //touch column5
    })
    public boolean btn_pressed(Button button, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            button.setBackgroundResource(R.drawable.pressed_button);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            button.setBackgroundResource(R.drawable.raised_button);
            if (!longClicked)//normal click
            {
                if (button.getId() == R.id.btn_regular) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_REGULAR),
                                    R.id.btn_regular
                            )
                    );
                } else if (button.getId() == R.id.btn_large) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_LARGE),
                                    R.id.btn_large
                            )
                    );
                } else if (button.getId()==R.id.btn_footlong) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_FOOTLONG),
                                    R.id.btn_footlong
                            )
                    );
                }else if (button.getId()==R.id.btn_reg_cheese) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_REGULAR_CHEESE),
                                    R.id.btn_reg_cheese
                            )
                    );
                } else if (button.getId()==R.id.btn_large_cheese) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_LARGE_CHEESE),
                                    R.id.btn_large_cheese
                            )
                    );
                } else if (button.getId()==R.id.btn_special1_opt) {
                    if (button.getText().toString().equalsIgnoreCase("Special1 Cheese")) {
                        Toast.makeText(this, "Please select a special item first", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(), //item name
                                    1, //quantity
                                    Constant.getPriceOfItem(
                                            getSpecialOptString(button.getText().toString()),
                                            Constant.SPECIAL_1 //base price
                                    ) + Constant.getPriceOfKeyItem(Constant.KEY_SPECIAL_1_CHEESE), //additional charges for cheese
                                    R.id.btn_special1_opt
                            )
                    );
                }else if (button.getId()==R.id.btn_special2_opt) {
                    if (button.getText().toString().equalsIgnoreCase("Special2 Cheese")) {
                        Toast.makeText(this, "Please select a special item first", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfItem(
                                            getSpecialOptString(button.getText().toString()),
                                            Constant.SPECIAL_2
                                    ) + Constant.getPriceOfKeyItem(Constant.KEY_SPECIAL_2_CHEESE),
                                    R.id.btn_special2_opt
                            )
                    );
                } else if (button.getId() == R.id.btn_save_50) {
                    discountPercentage = .5;
                    Toast.makeText(this, "50% saver on", Toast.LENGTH_SHORT).show();
                } else if (button.getId() == R.id.btn_save_100) {
                    discountPercentage = 1;
                    Toast.makeText(this, "100% saver on", Toast.LENGTH_SHORT).show();
                } else if (button.getId()==R.id.btn_footlong_cheese) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_FOOTLONG_CHEESE),
                                    R.id.btn_footlong_cheese
                            )
                    );
                } else if (button.getId()==R.id.btn_drink) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_DRINK),
                                    R.id.btn_drink
                            )
                    );
                } else if (button.getId()==R.id.btn_extra_cheese) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_EXTRA_CHEESE),
                                    R.id.btn_extra_cheese
                            )
                    );
                } else if (button.getId()==R.id.btn_reg_sausage) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_REGULAR_SAUSAGE),
                                    R.id.btn_reg_sausage
                            )
                    );
                } else if (button.getId()==R.id.btn_large_sausage) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_LARGE_SAUSAGE),
                                    R.id.btn_large_sausage
                            )
                    );
                } else if (button.getId()==R.id.btn_footlong_sausage) {
                    billAdapter.addItem(
                            new BillModel(
                                    button.getText().toString(),
                                    1,
                                    Constant.getPriceOfKeyItem(Constant.KEY_FOOTLONG_SAUSAGE),
                                    R.id.btn_footlong_sausage
                            )
                    );
                } else if (button.getId() == R.id.btn_special_1) {
                    Toast.makeText(this, "Please long hold to get Special 1 Popup", Toast.LENGTH_SHORT).show();
                } else if (button.getId() == R.id.btn_special_2) {
                    Toast.makeText(this, "Please long hold to get Special 2 Popup", Toast.LENGTH_SHORT).show();
                } else if (button.getId() == R.id.btn_gift_sale) {
                    GiftCardDialogUtils giftCardDialogUtils = new GiftCardDialogUtils(this, this);
                    giftCardDialogUtils.showDialog();
                } else if (button.getText().toString().equalsIgnoreCase("Discounts")) {
                    DiscountDialogUtil discountDialogUtil = new DiscountDialogUtil(this);
                    discountDialogUtil.setDiscountDialogListener(this);
                    discountDialogUtil.showDialog();
                }

                //   longClicked = false;
            }
            longClicked = false;
        }
        return false;
    }

    private String getSpecialOptString(String btnString) {
        String returnString = "";
        String arr[] = btnString.split(" ");
        for (int i = 0; i < arr.length - 1; i++) {
            returnString += arr[i];
            returnString += " ";
        }
        return returnString.trim();
    }

    @OnClick(R.id.iV_clearBill)
    public void clearBillPane() {
        billAdapter.removeAll();
        tV_total_amount.setText("" + 0.00);
        tV_discount_amount.setText("" + 0.00);
        tV_payable_amount.setText("" + 0.00);
        discountPercentage = 0; // reset discountPercentage
        if (billAdapter.getItemCount() == 0) {
            toggleSaverButton(true);
        }
        Toast.makeText(this, "Cleared bill pane", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tV_payable_amount)
    public void goToPaymentScreen() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("tV_payable_amount", tV_payable_amount.getText());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_up);
    }

    @OnLongClick({R.id.btn_special_1, R.id.btn_special_2})
    public boolean special_LongClicked(Button button) {
        String key = "";
        if (button.getId() == R.id.btn_special_1) {
            key = Constant.SPECIAL_1;
        } else if (button.getId() == R.id.btn_special_2) {
            key = Constant.SPECIAL_2;
        }

        SpecialDialogUtil sdu = new SpecialDialogUtil(this, key);
        sdu.setSpecialDialogItemClickListener(this);
        sdu.showDialog();

        longClicked = true;
        return false;
    }

    @OnLongClick(R.id.iV_header)
    public boolean showHiddenMenu() {
        final Dialog hiddenDialog = new Dialog(this);// Create custom dialog object
        hiddenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hiddenDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hiddenDialog.setCancelable(true);
        hiddenDialog.setContentView(R.layout.dialog_menu);
        hiddenDialog.show();

        Button btn_sync = hiddenDialog.findViewById(R.id.btn_sync);
        Button btn_transaction = hiddenDialog.findViewById(R.id.btn_transaction);
        Button btn_logout = hiddenDialog.findViewById(R.id.btn_logout);
        btn_sync.setOnTouchListener(this);
        btn_transaction.setOnTouchListener(this);
        btn_logout.setOnTouchListener(this);


        return false;
    }

    //-----------------------------------------------------User defined Functions---------------------------//

    private void InitialiseRecyclerViewWithAdapter() {
        billAdapter = new BillAdapter();
        billAdapter.setBillAddedListener(this);
        rV_billing_list.setLayoutManager(new LinearLayoutManager(this));
        rV_billing_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rV_billing_list.setAdapter(billAdapter);

        //enable swipe-to-delete interaction
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rV_billing_list);

    }

    //hidden dialog buttons touched
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            v.setBackgroundResource(R.drawable.card_button_pressed);
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            v.setBackgroundResource(R.drawable.calculator_total_button);
        }

        int id = v.getId();
        if (id == R.id.btn_sync)
            Toast.makeText(this, "Syncing..", Toast.LENGTH_SHORT).show();
        else if (id == R.id.btn_transaction) {
            startActivity(new Intent(this, TransactionActivity.class));
            finish();
        } else if (id == R.id.btn_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }


        return false;
    }

    @Override
    public void onSpecialItemClicked(String key, SpecialItemModel specialItem) {
        switch (key) {
            case Constant.SPECIAL_1:
                Toast.makeText(this, "SPECIAL 1 : " + specialItem.getProd(), Toast.LENGTH_SHORT).show();
                btn_special1_opt.setText(specialItem.getProd() + " Cheese");
                btn_special_1.setText(specialItem.getProd());
                break;
            case Constant.SPECIAL_2:
                Toast.makeText(this, "SPECIAL 2 : " + specialItem.getProd(), Toast.LENGTH_SHORT).show();
                btn_special2_opt.setText(specialItem.getProd() + " Cheese");
                btn_special_2.setText(specialItem.getProd());
                break;
            default:
                Toast.makeText(this, "Special Type Not found for : " + specialItem.getProd(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDiscountselected(DiscountModel discount) {
        billAdapter.addItem(new BillModel(discount.getProduct(), 1, discount.getRate(), R.id.btn_discount));
    }

    @Override
    public void onGiftCardSelectedListener(BillModel bill) {
        billAdapter.addItem(bill);
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            super.onBackPressed();
        }
        backPressed = true;
        Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
    }


    private void changePrice(double price, int itemKey) {
        try {
            double oldPrice = Double.parseDouble(tV_total_amount.getText().toString());
            double oldDiscount = Double.parseDouble(tV_discount_amount.getText().toString());
            double payable = Double.parseDouble(tV_payable_amount.getText().toString());

            if (discountPercentage > 0) { // discount is applied
                tV_total_amount.setText("" + (oldPrice + price));
                if (Constant.checkNonDiscountability(itemKey)) {
                    // NON-Discountable
                    double currentTotal = oldPrice + price;
                    tV_total_amount.setText("" + (oldPrice + price));
                    tV_payable_amount.setText("" + (payable + price));
                } else {
                    //Discountable
                    double currP = oldPrice + price;
                    double discP = price * discountPercentage;
                    double payP = price - discP;
                    tV_total_amount.setText("" + currP);
                    tV_discount_amount.setText("" + (oldDiscount + discP));
                    tV_payable_amount.setText("" + (payable + payP));
                }
            } else { // discount is not applied, normal charges applied
                tV_total_amount.setText("" + (oldPrice + price));
                tV_payable_amount.setText("" + (payable + price));
            }


        } catch (NumberFormatException e) {
            Toast.makeText(this, "ERROR : billing Pane Data error\nTD : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBillAdded(BillModel billModel) {
        toggleSaverButton(false);
        changePrice(billModel.getRate(), billModel.getId());
    }

    private void toggleSaverButton(boolean enable) {
        btn_save_50.setEnabled(enable);
        btn_save_100.setEnabled(enable);
        if (enable) {
            btn_save_50.setBackgroundResource(R.drawable.raised_button);
            btn_save_100.setBackgroundResource(R.drawable.raised_button);
        }
    }
}
