package com.example.mikezurawski.onyourmark.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mikezurawski.onyourmark.R;

import database.BudgetItem;
import database.CostItem;
import database.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler database = null;
    TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ?
        database = new DatabaseHandler(this);
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setDate("today");
        budgetItem.setLocation("Chipotle");
        CostItem costItem = new CostItem();
        costItem.setTitle("Chicken Burrito");
        costItem.setCost(2.00);
        budgetItem.addItem(costItem);

        database.addBudgetItem(budgetItem);

        Button viewBudgetsBtn = (Button) findViewById(R.id.view_budget_btn);
        viewBudgetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAllData();
            }
        });

        textView = (TextView) findViewById(R.id.app_title);

    }

    private void displayAllData() {
        String toPrint = "";
        for (BudgetItem item : database.getAllContacts()) {
            toPrint += item.getLocation() + "\n"
                    + item.getDate() + "\n"
                    + item.getID() + "\n\n";
        }
        textView.setText(toPrint);
    }
}
