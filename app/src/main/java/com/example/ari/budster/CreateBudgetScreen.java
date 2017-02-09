package com.example.ari.budster;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ari on 1/11/2016.
 */
public class CreateBudgetScreen extends Activity {
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_budget);
        populateSpinner();
    }

    private void populateSpinner() {
        spinner = (Spinner) findViewById(R.id.week_month_spinner);
        String[] options = {"Week","Month"};
        ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public void onCreateBudgetButton(View view) {
        EditText amount = (EditText) findViewById(R.id.create_budget_amount);
        EditText commodity = (EditText) findViewById(R.id.create_budget_commodity);
        String amountString = String.valueOf(amount.getText());
        String commodityString = String.valueOf(commodity.getText());
        if(amountString.equals("") || commodityString.equals(""))
            return;


        float amountFloat = Float.parseFloat(amountString);


        amountString = String.format("%.2f", amountFloat);

        SharedPreferences values;
        SharedPreferences.Editor editor;
        Calendar rightNow = Calendar.getInstance();
        int startDate = rightNow.get(Calendar.DAY_OF_YEAR);

        values = getSharedPreferences("startDate", 0);
        editor = values.edit();
        editor.putInt(commodityString,startDate);
        editor.apply();

        //displayable
        String currentMonthString = rightNow.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
        int dayOfMonth = rightNow.get(Calendar.DATE);
        String dateString = currentMonthString + " " + dayOfMonth;
        values = getSharedPreferences("startDateDisplay",0);
        editor=values.edit();
        editor.putString(commodityString,dateString);
        editor.apply();

        if(spinner.getSelectedItem().toString() == "Week")
            rightNow.add(Calendar.DATE, 6);
        else
            rightNow.add(Calendar.MONTH,1);
        currentMonthString = rightNow.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
        int endDate = rightNow.get(Calendar.DAY_OF_YEAR);
        dayOfMonth = rightNow.get(Calendar.DATE);
        dateString = currentMonthString + " " + dayOfMonth;
        values = getSharedPreferences("endDateDisplay", 0);
        editor=values.edit();
        editor.putString(commodityString, dateString);
        editor.apply();
        values = getSharedPreferences("endDate",0);
        editor=values.edit();
        editor.putInt(commodityString,endDate);
        editor.apply();

        values = getSharedPreferences("budgets", 0);
        editor = values.edit();
        editor.putFloat(commodityString, amountFloat);
        editor.apply();

        values = getSharedPreferences("spentValues",0);
        editor = values.edit();
        editor.putFloat(commodityString,0);
        editor.apply();

        Intent goingBack = new Intent();
        goingBack.putExtra("amount", amountString);
        goingBack.putExtra("commodity",commodityString);
        setResult(RESULT_OK,goingBack);
        finish();
    }


}
