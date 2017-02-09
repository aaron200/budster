package com.example.ari.budster;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Ari on 1/21/2016.
 */
public class BudgetStatusScreen extends Activity {

    SharedPreferences budgetValues;
    SharedPreferences spentValues;
    ListView commodityList;
    ArrayAdapter theAdapter;
    ArrayList<String> commodityArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_status);
        commodityList = (ListView) findViewById(R.id.commodity_list);
        budgetValues =  getSharedPreferences("budgets", 0);
        spentValues = getSharedPreferences("spentValues",0);

        populateList();
        listenForClick();


        /*

        ProgressBar moneyProgress = (ProgressBar) findViewById(R.id.spending_progress);
        moneyProgress.setProgress(80);

        SharedPreferences values;
        SharedPreferences.Editor editor;

        values = getSharedPreferences("startDate", 0);

        */

    }

    private void listenForClick() {
        commodityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = String.valueOf(adapterView.getItemAtPosition(position));
                Intent getSelectedBudget = new Intent(BudgetStatusScreen.this, SelectedBudgetScreen.class);
                getSelectedBudget.putExtra("selectedItem", selectedItem);
                startActivity(getSelectedBudget);

            }
        });
        /*
        commodityList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                String selectedItem = String.valueOf(parent.getItemAtPosition(position));
                return true;
            }
        }); */


    }

    private void populateList() {
        Set<String> commodities = budgetValues.getAll().keySet();
        if(!commodities.isEmpty()) {
            TextView selectItem = (TextView) findViewById(R.id.select_item);
            selectItem.setText("Select Item");
            commodityArray = new ArrayList<>();
            commodityArray.addAll(commodities);
            //commodityArray = list.toArray(new String[commodities.size()]);

            theAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,commodityArray);

            commodityList.setAdapter(theAdapter);

            registerForContextMenu(commodityList);
        }
        else {
            TextView prompt = (TextView) findViewById(R.id.prompt_to_create_budget);
            prompt.setText("You currently have no budgets set.");
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_budget, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(item.getItemId()==R.id.delete_budget_item) {
            TextView view = (TextView) info.targetView;
            String budgetItem = view.getText().toString();
            deleteBudget(budgetItem);
            commodityArray.remove(budgetItem);
            theAdapter.notifyDataSetChanged();
        }
        return true;
    }

    private void deleteBudget(String item) {
        SharedPreferences value;
        SharedPreferences.Editor editor;
        String[] fields = {"budgets", "budgetValues","startDate","endDate","startDateDisplay","endDateDisplay","spentValues"};
        for(String s : fields) {
            value = getSharedPreferences(s,0);
            editor = value.edit();
            editor.remove(item);
            editor.apply();
        }


    }

}
