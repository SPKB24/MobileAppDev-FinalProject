package com.example.mikezurawski.onyourmark.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mikezurawski.onyourmark.R;

import com.example.mikezurawski.onyourmark.database.BudgetItem;
import com.example.mikezurawski.onyourmark.database.DatabaseHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

// Keys

// Key 1 - 22126be1efca48c6bf1f8fc6fe085bc6
// Key 2 - b96d2cf4bc714de9b268660c3d8854b0
// Endpoint - https://westcentralus.api.cognitive.microsoft.com/vision/v1.0

public class MainActivity extends AppCompatActivity {

    DatabaseHandler database = null;
    TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ?
        database = new DatabaseHandler(this);
        database.resetDB();
        final Random rand = new Random();

        for (int i = 0; i < 50; i++) {
            BudgetItem budgetItem = new BudgetItem();
            final double cost = rand.nextDouble() * (999.50 - 1.50) + 1.50;
            budgetItem.setCategory(rand.nextInt() % 5);
            budgetItem.setCost(round(cost, 2));
            Calendar cal = Calendar.getInstance();
            cal.set(2017, 10, 9);
            budgetItem.setDate(cal.getTime());
            database.addBudgetItem(budgetItem);
        }

        displayAllData();

        Button viewBudgetsBtn = (Button) findViewById(R.id.view_budget_btn);
        viewBudgetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAllData();
            }
        });

        Button viewLeftoversBtn = (Button) findViewById(R.id.view_leftovers_btn);
        viewLeftoversBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MonthlyActivity.class);
                startActivity(intent);
            }
        });

        Button viewBreakdownBtn = (Button) findViewById(R.id.view_breakdown_btn);
        viewBreakdownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BreakdownActivity.class);
                startActivity(intent);
            }
        });

        Button viewAddItemBtn = (Button) findViewById(R.id.add_budget_item_btn);
        viewAddItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        textView = (TextView) findViewById(R.id.app_title);
    }

    private void displayAllData() {
        String toPrint = "";
        for (BudgetItem item : database.getBudgetItems()) {
            toPrint += " | id: " + item.getId()
                    + " | category: " + item.getCategory()
                    + " | date: " + item.getDate()
                    + " | cost: " + item.getCost()
                    + " |\n";
        }
        System.out.println(toPrint);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
