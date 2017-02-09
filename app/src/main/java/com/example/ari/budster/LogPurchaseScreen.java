package com.example.ari.budster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ari on 1/10/2016.
 */
public class LogPurchaseScreen extends Activity {
    Spinner commoditySpinner;
    SharedPreferences budgetValues;
    SharedPreferences spentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_purchase);
        budgetValues =  getSharedPreferences("budgets", 0);
        spentValues = getSharedPreferences("spentValues",0);
        commoditySpinner =  (Spinner) findViewById(R.id.commodity_spinner);
        populateSpinner();
    }

    private void populateSpinner() {
        Set<String> commodities = budgetValues.getAll().keySet();
        ArrayList<String> list = new ArrayList<>();
        list.addAll(commodities);
        String[] commodityArray = list.toArray(new String[commodities.size()]);
        ArrayAdapter<String> commoditySpinnerAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, commodityArray);
        commoditySpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        commoditySpinner.setAdapter(commoditySpinnerAdapter);
    }

    public void onLogPurchaseButton(View view) {
        EditText amount = (EditText) findViewById(R.id.purchase_amount_text);

        Object selectedItemObject =  commoditySpinner.getSelectedItem();
        String selectedItem;
        if(selectedItemObject!=null)
            selectedItem = selectedItemObject.toString();
        else
            return;
        String amountString = String.valueOf(amount.getText());
        if(amountString.equals(""))
            return;
        float amountFloat = Float.parseFloat(amountString);
        SharedPreferences.Editor editor = spentValues.edit();

        updateValues(selectedItem);

        float prevSpent = spentValues.getFloat(selectedItem, -9999999);
        editor.putFloat(selectedItem, spentValues.getFloat(selectedItem, -99999999) + amountFloat);



        editor.apply();

       // notifyUser(selectedItem);

        Intent goingBack = new Intent();
        goingBack.putExtra("amount",amountString);
        goingBack.putExtra("commodity",selectedItem);
        goingBack.putExtra("prevSpent", prevSpent);
        setResult(RESULT_OK, goingBack);
        finish();
    }

/*    private void notifyUser(String item) {

        SharedPreferences values = getSharedPreferences("spentValues", 0);
        float spent = values.getFloat(item,-9999999);
        values = getSharedPreferences("budgets",0);
        float budget = values.getFloat(item, 9999999);

        values = getSharedPreferences("settings", 0);


        float percent = 100*(spent / budget);

        if(percent>90) {
            if(values.getBoolean("ninety", false)) {
                //notify
                return;

            }
        }
        if(percent>75) {
            if(values.getBoolean("seventyFive",false)) {
                //notify
                return;

            }
        }
        if(percent>50) {
            if(values.getBoolean("fifty",false)) {
                //notify

            }
        }


    } */

    public void updateValues(String item) {
        int startDate;
        int endDate;
        SharedPreferences values;
        SharedPreferences.Editor editor;
        values = getSharedPreferences("startDate", 0);
        startDate = values.getInt(item, -999999);
        values = getSharedPreferences("endDate", 0);
        endDate = values.getInt(item, -999999);

        System.out.println(startDate);
        System.out.println(endDate);

        int total;
        int progress;
        Calendar rightNow = Calendar.getInstance();
        int nowDate = rightNow.get(Calendar.DAY_OF_YEAR);
        if (endDate < startDate) {
            total = endDate + (365 - startDate) + 1;
            if (nowDate >= startDate) {
                progress = nowDate - startDate;
            } else {
                progress = (365 - startDate) + nowDate;
            }
        } else {
            total = endDate - startDate + 1;
            progress = nowDate - startDate;
        }


        if (progress >= total) {
            values = getSharedPreferences("startDate", 0);
            editor = values.edit();
            rightNow.set(Calendar.DAY_OF_YEAR, endDate + 1);
            if (endDate == 365)
                editor.putInt(item, 1);
            else
                editor.putInt(item, endDate + 1);
            editor.apply();

            values = getSharedPreferences("startDateDisplay", 0);
            editor = values.edit();
            editor.putString(item, rightNow.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + rightNow.get(Calendar.DATE));
            if (total == 7)
                rightNow.add(Calendar.DAY_OF_YEAR, 6);
            else
                rightNow.add(Calendar.MONTH, 1);
            editor.apply();

            values = getSharedPreferences("endDate", 0);
            editor = values.edit();
            editor.putInt(item, rightNow.get(Calendar.DAY_OF_YEAR));
            editor.apply();

            values = getSharedPreferences("endDateDisplay", 0);
            editor = values.edit();
            editor.putString(item, rightNow.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + rightNow.get(Calendar.DATE));
            editor.apply();

            values = getSharedPreferences("spentValues", 0);
            editor = values.edit();
            editor.putFloat(item, 0);
            editor.apply();

            updateValues(item);

        }
    }

}
