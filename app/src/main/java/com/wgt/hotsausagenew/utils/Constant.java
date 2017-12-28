package com.wgt.hotsausagenew.utils;

import android.content.Context;
import android.widget.Toast;

import com.wgt.hotsausagenew.model.SpecialItemModel;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final String poundSign = "£";
    public static final String multiplySign = "x";
    public static final int buttonPressedAnimTime = 100;//in milli-sec

    public static final String SPECIAL_1 = "SPECIAL_1";
    public static final String SPECIAL_2 = "SPECIAL_2";

    private static List<SpecialItemModel> specialList1 = new ArrayList<>();
    private static List<SpecialItemModel> specialList2 = new ArrayList<>();

    public static List<SpecialItemModel> getSpecialItemList(String specialType) {
        switch (specialType) {
            case SPECIAL_1:
                return specialList1;
            case SPECIAL_2:
                return specialList2;
        }
        return null;
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
}
