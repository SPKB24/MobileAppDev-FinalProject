package com.example.mikezurawski.onyourmark.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mikezurawski.onyourmark.R;
import com.example.mikezurawski.onyourmark.database.BudgetItem;
import com.example.mikezurawski.onyourmark.database.DatabaseHandler;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthlyActivity extends AppCompatActivity {

    DatabaseHandler database = null;
    ArrayList<BudgetItem> monthlyItems = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        database = new DatabaseHandler(this);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        monthlyItems = database.getBudgetItems(cal.get(Calendar.MONTH));
        System.out.println("*** START ***");
        for (BudgetItem budgetItem : monthlyItems) {
            System.out.println("id: " + budgetItem.getId());
            System.out.println("cate: " + budgetItem.getCategory());
            System.out.println("cost" + budgetItem.getCost());
        }
        System.out.println("*** END ***");

        Button backMonthButton = (Button) findViewById(R.id.back_month_btn);
        backMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<BudgetItem> list = database.getBudgetItems();
                for (BudgetItem i : list) {
                    System.out.println(i.getCost());
                }
            }
        });
        Button forwardMonthButton = (Button) findViewById(R.id.forward_month_btn);
        forwardMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go forward a month for DB data
            }
        });

        //refreshGraph();

    }

    private void refreshGraph() {
        double[] spentPerCategory = new double[6];
        double totalSpent = 0;
        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        Arrays.fill(spentPerCategory, 0);
        for (BudgetItem budgetItem : monthlyItems) {
            System.out.println("id: " + budgetItem.getId());
            System.out.println("cate: " + budgetItem.getCategory());
            System.out.println("cost" + budgetItem.getCost());
            //spentPerCategory[budgetItem.getCategory()] += budgetItem.getCost();
            totalSpent += budgetItem.getCost();
        }
        return;

        /*
        List<PieEntry> entries = new ArrayList<>();

        for (double spentC : spentPerCategory) {
            double rawPercentage = spentC / totalSpent;
            float percentage = (float) rawPercentage;
            entries.add(new PieEntry(percentage, ""));
        }

        final int[] MY_COLORS = {Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.GREEN};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);


        PieDataSet set = new PieDataSet(entries, "Monthly Money Leftovers");
        set.setColors(colors);
        set.setDrawValues(false);
        PieData data = new PieData(set);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setData(data);
        pieChart.animateY(500);
        pieChart.invalidate(); // refresh
        */
    }
}
