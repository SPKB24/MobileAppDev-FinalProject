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

import java.util.Date;

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
        budgetItem.setCategory("Test");
        budgetItem.setCost(500);
        budgetItem.setDate(new Date());

        database.addBudgetItem(budgetItem);

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
}
