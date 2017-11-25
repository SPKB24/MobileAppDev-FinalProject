package com.example.mikezurawski.onyourmark.views;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

// Key 1 - 22126be1efca48c6bf1f8fc6fe085bc6
// Key 2 - b96d2cf4bc714de9b268660c3d8854b0
// Endpoint - https://westcentralus.api.cognitive.microsoft.com/vision/v1.0

public class MainActivity extends AppCompatActivity {

    final String[] MONTHS = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" };

    DatabaseHandler database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new HamburgerMenuHandler(this, R.id.toolbar, "On Your Mark", 1).init();
        initMonthSwitcher();
        initMonthlySummary();

        database = new DatabaseHandler(this);
        database.resetDB();
//        final Random rand = new Random();
//
//        for (int i = 0; i < 50; i++) {
//            BudgetItem budgetItem = new BudgetItem();
//            final double cost = rand.nextDouble() * (999.50 - 1.50) + 1.50;
//            budgetItem.setCategory(rand.nextInt(5));
//            budgetItem.setCost(round(cost, 2));
//            Calendar cal = Calendar.getInstance();
//            cal.set(2017, rand.nextInt(12), 9);
//            budgetItem.setDate(cal.getTime());
//            database.addBudgetItem(budgetItem);
//        }

        displayAllData();
    }

    // TODO: Remove -- Debugging only
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

    /**
     * Initial the month switcher and return the current month index
     * @return
     */
    private int initMonthSwitcher() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int currMonth = cal.get(Calendar.MONTH);

        TextView monthText = (TextView) findViewById(R.id.month_switcher_text);
        monthText.setText(MONTHS[currMonth]);

        return currMonth;
    }

    private void initMonthlySummary() {

        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(60.5f, ""));
        entries.add(new PieEntry(39.5f, ""));

        final int[] MY_COLORS = {Color.BLUE, Color.RED};
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
        pieChart.setDrawHoleEnabled(false);
        pieChart.invalidate(); // refresh
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
