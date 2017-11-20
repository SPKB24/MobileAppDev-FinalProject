package com.example.mikezurawski.onyourmark.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mikezurawski.onyourmark.R;

import com.example.mikezurawski.onyourmark.database.BudgetItem;
import com.example.mikezurawski.onyourmark.database.DatabaseHandler;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Date;

// Keys

// Key 1 - 22126be1efca48c6bf1f8fc6fe085bc6
// Key 2 - b96d2cf4bc714de9b268660c3d8854b0
// Endpoint - https://westcentralus.api.cognitive.microsoft.com/vision/v1.0

public class MainActivity extends AppCompatActivity {

    Drawer drawer = null;
    DatabaseHandler database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHamburgerMenu(this);

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

        Button viewAddItemBtn = (Button) findViewById(R.id.add_budget_item_btn);
        viewAddItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
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
                .withHeaderBackground(R.color.md_black_1000)
                .addProfiles(new ProfileDrawerItem()
                        .withIcon(R.mipmap.ic_launcher)
                        .withName("On Your Mark")
                ).withProfileImagesClickable(false)
                .withOnlyMainProfileImageVisible(true)
                .withPaddingBelowHeader(false)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER)
                .withCompactStyle(false)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withHeaderPadding(true)
                .withToolbar((Toolbar) findViewById(R.id.toolbar))
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
                                intent = new Intent(MainActivity.this, SelectImageActivity.class);
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
