package com.wgt.hotsausagenew.utils;

import com.wgt.hotsausagenew.model.SpecialItemModel;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final String poundSign = "£";
    public static final String multiplySign = "x";
    public static final int buttonPressedAnimTime = 100;//in milli-sec

    public static final String SPECIAL_1 = "SPECIAL_1";
    public static final String SPECIAL_2 = "SPECIAL_2";

    public static List<SpecialItemModel> getSpecial1Items() {
        String[] items = {"Special A", "Special B", "Special C", "Special D", "Special E", "Special F"};
        double[]  price = {33.33, 23.33, 23.33, 44.5, 65.43, 35};
        List<SpecialItemModel> list = new ArrayList<>();
        for(int i=0; i<items.length; i++){
            list.add(new SpecialItemModel(items[i], price[i]));
        }
        return list;
    }
    public static List<SpecialItemModel> getSpecial2Items() {
        String[] items = {"Special J", "Special K", "Special L", "Special M", "Special N", "Special O"};
        double[]  price = {33.33, 23.33, 23.33, 44.5, 65.43, 35};
        List<SpecialItemModel> list = new ArrayList<>();
        for(int i=0; i<items.length; i++){
            list.add(new SpecialItemModel(items[i], price[i]));
        }
        return list;
    }
}
