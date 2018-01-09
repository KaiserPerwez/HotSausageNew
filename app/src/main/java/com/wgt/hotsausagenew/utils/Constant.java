package com.wgt.hotsausagenew.utils;

import android.content.Context;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;
import com.wgt.hotsausagenew.model.SpecialItemModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constant {

    public static final String poundSign = "£";
    public static final String multiplySign = "x";
    public static final int buttonPressedAnimTime = 100;//in milli-sec
    public static final String PAYMENT_CASH = "CASH";
    public static final String PAYMENT_CARD = "CARD";
    public static final String KEY_PAYMENT_MODE_INTENT = "payment_mode";
    public static final String KEY_PAYABLE_AMT_INTENT = "payable_amt";
    public static final String KEY_PAID_AMT_INTENT = "paid_amt";
    public static final String KEY_TOTAL_AMT_INTENT = "total_amt";
    public static final String KEY_DISCOUNT_AMT_INTENT = "discount_amt";
    public static final int REQUEST_CODE_PAYMENT_INTENT = 1;
    public static final int REQUEST_CODE_TRANSACTION_INTENT = 2;
    public static final int REQUEST_CODE_LOGOUT_INTENT = 3;
    public static final String SPECIAL_1 = "SPECIAL_1";
    public static final String SPECIAL_2 = "SPECIAL_2";
    public static final int KEY_REGULAR = 1;
    public static final int KEY_LARGE = 2;
    public static final int KEY_FOOTLONG = 3;
    public static final int KEY_REGULAR_CHEESE = 4;
    public static final int KEY_LARGE_CHEESE = 5;
    public static final int KEY_SPECIAL_1_CHEESE = 6;
    public static final int KEY_SPECIAL_2_CHEESE = 7;
    public static final int KEY_FOOTLONG_CHEESE = 8;
    public static final int KEY_DRINK = 9;
    public static final int KEY_EXTRA_CHEESE = 10;
    public static final int KEY_REGULAR_SAUSAGE = 11;
    public static final int KEY_LARGE_SAUSAGE = 12;
    public static final int KEY_FOOTLONG_SAUSAGE = 13;
    private static int loggedInUserId = 0;
    private static HashMap<Integer, Double> productPriceMap = new HashMap<>();
    private static List<Integer> nonDiscountableItemList = new ArrayList<>();
    private static List<SpecialItemModel> specialList1 = new ArrayList<>();
    private static List<SpecialItemModel> specialList2 = new ArrayList<>();

    static // price list of each product
    {
        productPriceMap.put(KEY_REGULAR, 120.00);
        productPriceMap.put(KEY_LARGE, 220.00);
        productPriceMap.put(KEY_FOOTLONG, 65.00);
        productPriceMap.put(KEY_REGULAR_CHEESE, 150.00);
        productPriceMap.put(KEY_LARGE_CHEESE, 250.00);
        productPriceMap.put(KEY_SPECIAL_1_CHEESE, 0.00);   //not actual price.. extra price for CHEESE
        productPriceMap.put(KEY_SPECIAL_2_CHEESE, 0.00);   //not actual price.. extra price for CHEESE
        productPriceMap.put(KEY_FOOTLONG_CHEESE, 160.00);
        productPriceMap.put(KEY_DRINK, 50.00);
        productPriceMap.put(KEY_EXTRA_CHEESE, 70.00);
        productPriceMap.put(KEY_REGULAR_SAUSAGE, 1.50);
        productPriceMap.put(KEY_LARGE_SAUSAGE, 2.00);
        productPriceMap.put(KEY_FOOTLONG_SAUSAGE, 2.50);
    }

    // which items will not be discountable
    static {
        nonDiscountableItemList.add(R.id.btn_extra_cheese);
        nonDiscountableItemList.add(R.id.btn_reg_sausage);
        nonDiscountableItemList.add(R.id.btn_large_sausage);
        nonDiscountableItemList.add(R.id.btn_footlong_sausage);
        nonDiscountableItemList.add(R.id.btn_discount);
        nonDiscountableItemList.add(R.id.btn_gift_sale);
        nonDiscountableItemList.add(R.id.btn_gift_used);
    }

    public static boolean checkNonDiscountability(int itemID) {
        return nonDiscountableItemList.contains(itemID);
    }

    public static double getPriceOfKeyItem(int key) {
        return productPriceMap.get(key);
    }

    public static List<SpecialItemModel> getSpecialItemList(String specialType) {
        switch (specialType) {
            case SPECIAL_1:
                return specialList1;
            case SPECIAL_2:
                return specialList2;
        }
        return null;
    }

    public static SpecialItemModel getItem(int position, String key) {
        switch (key) {
            case SPECIAL_1:
                return specialList1.get(position);
            case SPECIAL_2 :
                return specialList2.get(position);
            default:
                return null;
        }
    }

    public static double getPriceOfItem(String itemName, String specialType) {
        if (specialType.equals(SPECIAL_1)) {
            for (SpecialItemModel s : specialList1) {
                if (s.getProd().equals(itemName))
                    return s.getRate();
            }
        } else if (specialType.equals(SPECIAL_2)) {
            for (SpecialItemModel s : specialList2) {
                if (s.getProd().equals(itemName))
                    return s.getRate();
            }
        }
        return 0;
    }

    public static boolean addSpecialItemToList(SpecialItemModel item, String specialType, Context context) {
        switch (specialType) {
            case SPECIAL_1:
                //checking for duplicate value
                for (SpecialItemModel si : specialList1) {
                    if (si.getProd().equals(item.getProd())) {
                        Toast.makeText(context, "Item : " + item.getProd() + " already exists", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                specialList1.add(item);
                return true;

            case SPECIAL_2:
                //checking for duplicate value
                for (SpecialItemModel si : specialList2) {
                    if (si.getProd().equals(item.getProd())) {
                        Toast.makeText(context, "Item : " + item.getProd() + " already exists", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                specialList2.add(item);
                return true;
            default:
                Toast.makeText(context, "No Special List : " + specialType, Toast.LENGTH_SHORT).show();
                return false;
        }
    }

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static void setLoggedInUserId(int id) {
        loggedInUserId = id;
    }
}
