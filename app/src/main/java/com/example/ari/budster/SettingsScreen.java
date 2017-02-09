package com.example.ari.budster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by Ari on 5/2/2016.
 */
public class SettingsScreen extends Activity {
    SharedPreferences settings;
    CheckBox checkbox_50;
    CheckBox checkbox_75;
    CheckBox checkbox_100;
    SharedPreferences.Editor editor;
    Button save_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        settings = getSharedPreferences("settings", 0);
        editor = settings.edit();
        checkbox_50 = (CheckBox) findViewById(R.id.checkBox50);
        checkbox_75 = (CheckBox) findViewById(R.id.checkBox75);
        checkbox_100 = (CheckBox) findViewById(R.id.checkBox100);
        save_button = (Button) findViewById(R.id.save_button);

        checkbox_50.setChecked(settings.getBoolean("50",false));
        checkbox_75.setChecked(settings.getBoolean("75",true));
        checkbox_100.setChecked(settings.getBoolean("100",true));


        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if(settings.getBoolean("50", false) != checkbox_50.isChecked()) {
                    editor.putBoolean("50", checkbox_50.isChecked());
                }
                if(settings.getBoolean("75", false) != checkbox_75.isChecked()) {
                    editor.putBoolean("75", checkbox_75.isChecked());
                }
                if(settings.getBoolean("100", false) != checkbox_100.isChecked()) {

                    editor.putBoolean("100", checkbox_100.isChecked());
                }

                editor.apply();

                Intent goingBack = new Intent();
                setResult(RESULT_OK, goingBack);
                finish();


            }
        });




    }

}
