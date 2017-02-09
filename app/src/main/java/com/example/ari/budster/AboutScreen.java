package com.example.ari.budster;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Ari on 1/28/2016.
 */
public class AboutScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen);
        TextView aboutText = (TextView) findViewById(R.id.about_description);
        aboutText.setText("Budget Budster is a simple application to keep track of how much you spend on specific costs. With Budget Budster, you can create a weekly or monthly budget for any cost (food, clothes, spaceships, etc) that you input. From there you can log your purchases and keep tabs on how much you are spending on that cost to help you spend responsibly.");
    }


}
