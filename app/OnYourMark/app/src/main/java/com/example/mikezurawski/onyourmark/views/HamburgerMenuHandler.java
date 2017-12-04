package com.example.mikezurawski.onyourmark.views;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mikezurawski.onyourmark.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by sohit on 11/21/17.
 */

class HamburgerMenuHandler {

    private Drawer drawer;
    private Activity activity;
    private int toolbar_id;
    private String page_title;
    private int page_position;

    HamburgerMenuHandler(final Activity activity, final int toolbar_id, final String page_title, final int page_position) {
        this.activity = activity;
        this.toolbar_id = toolbar_id;
        this.page_title = page_title;
        this.page_position = page_position;
    }

    void init() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName("Home")
                .withIcon(FontAwesome.Icon.faw_home)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Add New Item")
                .withIcon(FontAwesome.Icon.faw_camera)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Settings")
                .withIcon(FontAwesome.Icon.faw_sliders)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem()
                .withIdentifier(10)
                .withName("About Us")
                .withIconTintingEnabled(true);

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

        Toolbar toolbar = (Toolbar) activity.findViewById(toolbar_id);
        toolbar.setTitle(page_title);

        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(headerResult)
                .withCloseOnClick(true)
                .withHeaderPadding(true)
                .withSelectedItem(page_position)
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

                        if (position == page_position) {
                            drawer.closeDrawer();
                            return false;
                        }

                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                intent = new Intent(activity, MainActivity.class);
                                activity.startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(activity, AddItemActivity.class);
                                activity.startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(activity, SettingsActivity.class);
                                activity.startActivity(intent);
                                break;
                            case 10:
                                intent = new Intent(activity, AboutActivity.class);
                                activity.startActivity(intent);
                                break;
                            default:
                                Toast.makeText(activity, "You haven't configured an onClick event for this button", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                }).build();
    }
}
