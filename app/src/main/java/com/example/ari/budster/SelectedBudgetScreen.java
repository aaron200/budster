package com.example.ari.budster;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ari on 1/21/2016.
 */
public class SelectedBudgetScreen extends Activity {
    String selected;
    SharedPreferences values;
    SharedPreferences.Editor editor;
    float budget;
    float spent;
    int endDate;
    int startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_budget_status);
        selected = getIntent().getStringExtra("selectedItem");
        updateTimeProgress();
        updateMoneyProgress();


    }

    private void updateMoneyProgress() {
        TextView spentOnText = (TextView) findViewById(R.id.money_spent_on);
        TextView upperBound = (TextView) findViewById(R.id.upper_bound);
        ProgressBar moneyProgress = (ProgressBar) findViewById(R.id.spending_progress);
        values = getSharedPreferences("budgets", 0);
        budget = values.getFloat(selected,0);
        upperBound.setText("$"+String.format("%.2f", budget));
        values = getSharedPreferences("spentValues", 0);
        spent = values.getFloat(selected,0);

        moneyProgress.setMax(Math.round(budget));
        moneyProgress.setProgress(Math.round(spent));

        spentOnText.append("$" + String.format("%.2f", spent));
        spentOnText.append(" on " + selected);



    }

    private void updateTimeProgress() {
        values = getSharedPreferences("startDate",0);
        startDate = values.getInt(selected, -999999);
        values = getSharedPreferences("endDate", 0);
        endDate = values.getInt(selected, -999999);

        System.out.println(startDate);
        System.out.println(endDate);


        int total;
        int progress;
        Calendar rightNow = Calendar.getInstance();
        int nowDate = rightNow.get(Calendar.DAY_OF_YEAR);




        ProgressBar timeProgress = (ProgressBar) findViewById(R.id.time_progress);
        if(endDate<startDate) {
            total = endDate + (365 - startDate) + 1;
            if(nowDate >= startDate) {
                progress = nowDate - startDate;
            }
            else {
                progress = (365 - startDate) + nowDate;
            }
        }
        else {
            total = endDate - startDate + 1;
            progress = nowDate - startDate;
        }


        if(progress>=total) {
            values = getSharedPreferences("startDate", 0);
            editor = values.edit();
            rightNow.set(Calendar.DAY_OF_YEAR,endDate+1);
            if(endDate == 365)
                editor.putInt(selected,1);
            else
                editor.putInt(selected,endDate+1);
            editor.apply();
            values = getSharedPreferences("startDateDisplay", 0);
            editor = values.edit();
            editor.putString(selected, rightNow.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + rightNow.get(Calendar.DATE));
            if(total == 7)
                rightNow.add(Calendar.DAY_OF_YEAR, 6);
            else
                rightNow.add(Calendar.MONTH, 1);
            editor.apply();
            values = getSharedPreferences("endDate", 0);
            editor = values.edit();
            editor.putInt(selected, rightNow.get(Calendar.DAY_OF_YEAR));
            editor.apply();

            values = getSharedPreferences("endDateDisplay",0);
            editor = values.edit();
            editor.putString(selected, rightNow.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + rightNow.get(Calendar.DATE));
            editor.apply();

            values = getSharedPreferences("spentValues",0);
            editor = values.edit();
            editor.putFloat(selected,0);
            editor.apply();
            updateTimeProgress();
        }
        else {
            timeProgress.setMax(total);
            timeProgress.setProgress(progress);

            values = getSharedPreferences("startDateDisplay", 0);
            String startDateDisplay = values.getString(selected,"nool");
            TextView startDateDisplayText = (TextView) findViewById(R.id.start_date);
            startDateDisplayText.setText(startDateDisplay);

            values = getSharedPreferences("endDateDisplay",0);
            String endDateDisplay = values.getString(selected,"nool");
            TextView endDateDisplayText = (TextView) findViewById(R.id.target_date);
            endDateDisplayText.setText(endDateDisplay);
        }



    }
}
