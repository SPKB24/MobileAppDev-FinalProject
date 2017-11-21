package com.example.mikezurawski.onyourmark.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikezurawski.onyourmark.R;

import com.example.mikezurawski.onyourmark.database.BudgetItem;
import com.example.mikezurawski.onyourmark.database.DatabaseHandler;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Keys

// Key 1 - 22126be1efca48c6bf1f8fc6fe085bc6
// Key 2 - b96d2cf4bc714de9b268660c3d8854b0
// Endpoint - https://westcentralus.api.cognitive.microsoft.com/vision/v1.0

public class MainActivity extends AppCompatActivity {

    final String[] MONTHS = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" };

    Drawer drawer = null;
    DatabaseHandler database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHamburgerMenu(this);
        initMonthSwitcher();
        initMonthlySummary();

        // ?
        database = new DatabaseHandler(this);

        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setCategory("Test");
        budgetItem.setCost(500);
        budgetItem.setDate(new Date());

        database.addBudgetItem(budgetItem);

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

    private void initHamburgerMenu(final Activity activity) {

        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName("Home")
                .withIcon(FontAwesome.Icon.faw_home)
                .withIconTintingEnabled(true)
                .withSelectable(false)
                .withSetSelected(false);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Add New Item")
                .withIcon(FontAwesome.Icon.faw_camera)
                .withIconTintingEnabled(true)
                .withSelectable(false)
                .withSetSelected(false);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Settings")
                .withIcon(FontAwesome.Icon.faw_sliders)
                .withIconTintingEnabled(true)
                .withSelectable(false)
                .withSetSelected(false);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem()
                .withIdentifier(10)
                .withName("About Us")
                .withIcon(R.drawable.avd_hide_password_1)
                .withIconTintingEnabled(true)
                .withSelectable(false)
                .withSetSelected(false);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.colorPrimaryDark)
                .addProfiles(new ProfileDrawerItem()
                        .withIcon(R.mipmap.ic_launcher_round)
                        .withName("On Your Mark")
                ).withProfileImagesClickable(false)
                .withOnlyMainProfileImageVisible(true)
                .withPaddingBelowHeader(false)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER)
                .withCompactStyle(false)
                .build();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("On Your Mark");

        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(headerResult)
                .withCloseOnClick(true)
                .withHeaderPadding(true)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        item3
                ).addStickyDrawerItems(
                        item4
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent;
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                drawer.closeDrawer();
                                break;
                            case 2:
                                intent = new Intent(MainActivity.this, SelectImageActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                Toast.makeText(activity, "Settings", Toast.LENGTH_SHORT).show();
                                intent = new Intent(MainActivity.this, BreakdownActivity.class);
                                startActivity(intent);
                                break;
                            case 10:
                                Toast.makeText(activity, "Yee", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(activity, "You haven't configured an onClick event for this button", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .build();
    }
}
