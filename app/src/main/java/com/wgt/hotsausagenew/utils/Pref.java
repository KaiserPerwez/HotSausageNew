package com.wgt.hotsausagenew.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wgt.hotsausagenew.model.DiscountModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Pref {

    public static final String DISCOUNT_PREF = "discount_pref"; // file
    public static final String DISCOUNT_KEY = "discount"; // key, (value will be json)

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Pref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(DISCOUNT_PREF, Context.MODE_PRIVATE);
    }

    public List<DiscountModel> getDiscountsPref() {
        List<DiscountModel> list = null;
        String stringList = sharedPreferences.getString(DISCOUNT_KEY, null);
        if (stringList == null){
            return null;
        }
        list = getListFromString(stringList);
        return list;
    }

    private void saveDiscounts(List<DiscountModel> list) {
        editor = sharedPreferences.edit();
        editor.putString(DISCOUNT_KEY, getStringFromList(list));
        editor.apply();
    }

    public boolean saveDiscount(DiscountModel discount) {
        editor = sharedPreferences.edit();
        List<DiscountModel> list = getDiscountsPref();
        if (list == null) {
            list = new ArrayList<DiscountModel>();
        }

        //check for duplicate value
        for (DiscountModel d : list) {
            if(d.getItemName().equals(discount.getItemName())) {
                Toast.makeText(context, "Item : "+discount.getItemName()+" already exists.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        list.add(discount);
        editor.putString(DISCOUNT_KEY, getStringFromList(list));
        editor.apply();
        return true;
    }

    public void deleteItem(String itemName) {
        List<DiscountModel> list = getDiscountsPref();
        List<DiscountModel> temp = new ArrayList<>();
        if (list == null) return;

        for (DiscountModel d :list) {
            if(d.getItemName().equals(itemName))
                continue;
            temp.add(d);
        }
        saveDiscounts(temp);
    }

    private String getStringFromList(List<DiscountModel> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    private List<DiscountModel> getListFromString(String string) {
        Type type = new TypeToken<List<DiscountModel>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(string, type);
    }
}
