package com.example.ari.budster;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] options = {"Log Purchase","Budget Status","Create Budget","Settings","About"};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView theListView = (ListView) findViewById(R.id.firstList);
        ListAdapter theAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options);
        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                System.out.println(String.valueOf(adapterView.getItemAtPosition(position)));
                switch(String.valueOf(adapterView.getItemAtPosition(position))) {
                    case "Log Purchase":
                        Intent getLogPurchase = new Intent(MainActivity.this, LogPurchaseScreen.class);
                        int result = 1;
                        startActivityForResult(getLogPurchase, result);
                        break;
                    case "Budget Status":
                        Intent getBudgetStatus = new Intent(MainActivity.this,BudgetStatusScreen.class);
                        result = 2;
                        startActivityForResult(getBudgetStatus,result);
                        /*
                        SharedPreferences spentValues = getSharedPreferences("spentValues",0);
                        String map = spentValues.getAll().toString();
                        Toast.makeText(MainActivity.this,map,Toast.LENGTH_LONG).show();
                        */
                        break;
                    case "Create Budget":
                        result = 3;
                        Intent getCreateBudget = new Intent(MainActivity.this,CreateBudgetScreen.class);
                        startActivityForResult(getCreateBudget, result);
                        break;
                    case "About":
                        Intent getAbout = new Intent(MainActivity.this,AboutScreen.class);
                        startActivity(getAbout);
                        break;
                    case "Settings":
                        result = 4;
                        Intent getSettings = new Intent(MainActivity.this, SettingsScreen.class);
                        startActivityForResult(getSettings, result);
                    default: System.out.println("other");
                        break;



                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK ) {
                super.onActivityResult(requestCode, resultCode, data);
                String amount = data.getStringExtra("amount");
                String commodity = data.getStringExtra("commodity");
                float prevSpent = data.getFloatExtra("prevSpent",0);
                Toast.makeText(MainActivity.this,"Logged spending of "+amount+ " on "+commodity,Toast.LENGTH_SHORT).show();
                notify_user(commodity, prevSpent);
            }
        }
        else if(requestCode==3) {
            if(resultCode == Activity.RESULT_OK ) {
                String amount = data.getStringExtra("amount");
                String commodity = data.getStringExtra("commodity");
                Toast.makeText(MainActivity.this, "Set budget for " + commodity + " at " + amount, Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode==4) {
            if(resultCode == Activity.RESULT_OK)
                Toast.makeText(MainActivity.this, "Preferences saved", Toast.LENGTH_SHORT).show();
        }

    }

    public void notify_user(String commodity, float prevSpent) {
        SharedPreferences budgetValues =  getSharedPreferences("budgets", 0);
        SharedPreferences spentValues = getSharedPreferences("spentValues",0);
        SharedPreferences settings = getSharedPreferences("settings",0);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        double threshold;
        Intent resultIntent = new Intent(this, SelectedBudgetScreen.class);
        resultIntent.putExtra("selectedItem", commodity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack
        stackBuilder.addParentStack(SelectedBudgetScreen.class);
// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
// Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);



        if(settings.getBoolean("100", true)) {
             threshold = budgetValues.getFloat(commodity, 0);
            if(spentValues.getFloat(commodity,0) >= threshold && prevSpent < threshold) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Budget Reached")
                                .setContentText("You have reached your budget for "+commodity+"!");
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setDefaults(Notification.DEFAULT_ALL);
                mNotificationManager.notify(1, mBuilder.build());
                builder.build();
                return;
            }
        }
        if(settings.getBoolean("75", true)) {
            threshold = 0.75 * budgetValues.getFloat(commodity, 0);
            if(spentValues.getFloat(commodity,0) >= threshold && prevSpent < threshold) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_stat_name)
                                .setContentTitle("Budget Warning")
                                .setContentText("You have reached 75% of your budget for "+commodity+"!");
                mBuilder.setContentIntent(resultPendingIntent);
                mNotificationManager.notify(1, mBuilder.build());
                return;
            }
        }
        if(settings.getBoolean("50", false)) {
            threshold = 0.50 * budgetValues.getFloat(commodity, 0);
            if(spentValues.getFloat(commodity,0) >= threshold && prevSpent < threshold) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_stat_name)
                                .setContentTitle("Budget Warning")
                                .setContentText("You have reached 50% of your budget for "+commodity+"!");
                mBuilder.setContentIntent(resultPendingIntent);
                mNotificationManager.notify(1, mBuilder.build());
            }
        }

    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */
}
