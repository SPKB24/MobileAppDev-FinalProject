package com.example.mikezurawski.onyourmark.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikezurawski.onyourmark.R;

import com.example.mikezurawski.onyourmark.database.BudgetItem;
import com.example.mikezurawski.onyourmark.database.DatabaseHandler;
import com.example.mikezurawski.onyourmark.other.SharedPreferenceHandler;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Key 1 - 22126be1efca48c6bf1f8fc6fe085bc6
// Key 2 - b96d2cf4bc714de9b268660c3d8854b0
// Endpoint - https://westcentralus.api.cognitive.microsoft.com/vision/v1.0

public class MainActivity extends AppCompatActivity {

    final String[] MONTHS = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" };

    DatabaseHandler database = null;

    ArrayList<BudgetItem> monthlyItems = null;
    int currentMonth = -1;

    LinearLayout monthly_breakdown_layout;
    LinearLayout monthly_breakdown_categories;
    LinearLayout no_monthly_data_layout;

    PieChart monthly_summary_chart;
    PieChart monthly_breakdown_chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new HamburgerMenuHandler(this, R.id.toolbar, "On Your Mark").init_homepage();

        monthly_breakdown_layout = findViewById(R.id.monthly_breakdown_items);
        monthly_breakdown_categories = findViewById(R.id.monthly_breakdown_categories_layout);
        no_monthly_data_layout = findViewById(R.id.no_information);

        monthly_summary_chart = (PieChart) findViewById(R.id.monthly_summary_chart);
        monthly_breakdown_chart = (PieChart) findViewById(R.id.monthly_breakdown_chart);

        database = new DatabaseHandler(this);
//        database.resetDB();
//        final Random rand = new Random();
//
//        for (int i = 0; i < 50; i++) {
//            BudgetItem budgetItem = new BudgetItem();
//            final double cost = rand.nextDouble() * (100.00 - 1.50) + 1.50;
//            budgetItem.setCategory(rand.nextInt(5));
//            budgetItem.setCost(round(cost, 2));
//            Calendar cal = Calendar.getInstance();
//            cal.set(2017, rand.nextInt(12), 9);
//            budgetItem.setDate(cal.getTime());
//            database.addBudgetItem(budgetItem);
//        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        currentMonth = cal.get(Calendar.MONTH);

        Button backMonthButton = (Button) findViewById(R.id.back_month_btn);
        backMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentMonth-- == 0) {
                    currentMonth = 11;
                }
                refreshInformation();
            }
        });
        Button forwardMonthButton = (Button) findViewById(R.id.forward_month_btn);
        forwardMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonth = (currentMonth + 1) % 12;
                refreshInformation();
            }
        });
        Button addNewItem = (Button) findViewById(R.id.new_item_btn);
        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        refreshInformation();
        debugLogs();
    }

    private void debugLogs() {
        String toPrint = "";
        for (BudgetItem item : database.getBudgetItems()) {
            toPrint += " | category: " + item.getCategory()
                    + " | date: " + item.getDate()
                    + " | cost: " + item.getCost()
                    + " |\n";
        }
        System.out.println(toPrint);
    }

    private void refreshInformation() {
        monthlyItems = database.getBudgetItems(currentMonth);
        updateMonthText();
        refreshMonthlySummaryGraph();

        if (monthlyItems.isEmpty()) {
            monthly_breakdown_layout.setVisibility(View.GONE);
            no_monthly_data_layout.setVisibility(View.VISIBLE);
        } else {
            monthly_breakdown_layout.setVisibility(View.VISIBLE);
            no_monthly_data_layout.setVisibility(View.GONE);
            refreshMonthlyBreakdownGraph();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void updateMonthText() {
        TextView monthText = (TextView) findViewById(R.id.month_switcher_text);
        monthText.setText(MONTHS[currentMonth]);
    }

    private void refreshMonthlySummaryGraph() {

        TextView spentTxt = findViewById(R.id.monthly_summary_spent_txt);
        TextView remainingTxt = findViewById(R.id.monthly_summary_remaining_txt);
        TextView totalTxt = findViewById(R.id.monthly_summary_total_txt);

        // Get monthly spending limit
        SharedPreferenceHandler sharedPreferences = new SharedPreferenceHandler(this);
        Float monthlySpendingLimit = sharedPreferences.getFloat();

        // Get this months spending information
        float totalSpent = 0.0f;
        for (BudgetItem budgetItem : monthlyItems) {
            totalSpent += budgetItem.getCost();
        }

        // Set TextViews text
        Float totalRemaining = monthlySpendingLimit - totalSpent;
        spentTxt.setText(String.format("$%.2f", totalSpent));
        remainingTxt.setText(String.format("$%.2f", totalRemaining));
        totalTxt.setText(String.format("$%.2f", monthlySpendingLimit));

        // Setup chart
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(204,68,75)); // RED
        colors.add(Color.rgb(52,89,149)); // BLUE

        if (totalSpent > monthlySpendingLimit) {
            entries.add(new PieEntry(1, "spent"));
        } else {
            entries.add(new PieEntry((totalSpent / monthlySpendingLimit), "spent"));
            entries.add(new PieEntry((totalRemaining / monthlySpendingLimit), "remaining"));
        }

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);
        set.setDrawValues(false);

        Description description = new Description();
        description.setText("");

        monthly_summary_chart.getLegend().setEnabled(false);
        monthly_summary_chart.setDrawEntryLabels(false);
        monthly_summary_chart.setDescription(description);
        monthly_summary_chart.setData(new PieData(set));
        monthly_summary_chart.animateY(500);
        monthly_summary_chart.setDrawHoleEnabled(false);
        monthly_summary_chart.invalidate(); // refresh
    }

    private void refreshMonthlyBreakdownGraph() {
        clearCategoriesOnCreate();

        HashMap<String, Double> categoryMap = new HashMap<>();
        double totalSpent = 0;

        for (BudgetItem budgetItem: monthlyItems) {
            String category = DatabaseHandler.getCategoryString(budgetItem.getCategory());

            if (!categoryMap.containsKey(category))
                categoryMap.put(category, budgetItem.getCost());
            else {
                categoryMap.put(category, categoryMap.get(category) + budgetItem.getCost());
            }

            totalSpent += budgetItem.getCost();
        }

        Iterator it = categoryMap.entrySet().iterator();
        List<PieEntry> entries = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            addNewCategoryItem((String) pair.getKey(), (Double) pair.getValue());

            double rawPercentage = (Double) pair.getValue() / totalSpent;
            float percentage = (float) rawPercentage;
            entries.add(new PieEntry(percentage, (String) pair.getKey()));
        }

        final int[] MY_COLORS = {Color.BLUE, Color.RED, Color.GRAY, Color.CYAN, Color.GREEN};
        ArrayList<Integer> colors = new ArrayList<>();

        for(int c: MY_COLORS) colors.add(c);

        PieDataSet set = new PieDataSet(entries, "Monthly Money Leftovers");
        set.setColors(colors);
        set.setDrawValues(false);

        Description description = new Description();
        description.setText("");

        monthly_breakdown_chart.getLegend().setEnabled(false);
        monthly_breakdown_chart.setDescription(description);
        monthly_breakdown_chart.setData(new PieData(set));
        monthly_breakdown_chart.animateY(500);
        monthly_breakdown_chart.setDrawHoleEnabled(false);
        monthly_breakdown_chart.invalidate(); // refresh
    }

    private void clearCategoriesOnCreate() {
        monthly_breakdown_categories.removeAllViews();
    }

    private void addNewCategoryItem(final String category, final Double cost) {
        LinearLayout rowToAdd = (LinearLayout) getLayoutInflater().inflate(R.layout.monthly_breakdown_row_item, null);

        TextView categoryTextView = rowToAdd.findViewById(R.id.row_category_text);
        categoryTextView.setText(category);

        TextView costTextView = rowToAdd.findViewById(R.id.row_cost_text);
        costTextView.setText(String.format("$%.2f", cost));

        monthly_breakdown_categories.addView(rowToAdd);
    }
}
