package com.dinosilvestro.mememaker.ui;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.dinosilvestro.mememaker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class NavigationDrawer {

    public NavigationDrawer(final Activity activity, FirebaseAuth auth, Toolbar toolbar) {
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.primary_light)
                .addProfiles(
                        new ProfileDrawerItem().withName(auth.getCurrentUser().getDisplayName())
                                .withEmail(auth.getCurrentUser().getEmail())
                                .withIcon("")
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        Toast.makeText(activity, R.string.signing_out_toast_text, Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        activity.finish();

                        return true;
                    }
                })
                .withTextColor(activity.getResources().getColor(R.color.colorSecondaryText,
                        activity.getTheme()))
                .build();

        new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home")
                                .withIcon(R.drawable.ic_home_black_24dp),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("r/Memes")
                                .withIcon(R.drawable.ic_open_in_browser_black_24dp),
                        new SecondaryDrawerItem().withName("Know Your Meme")
                                .withIcon(R.drawable.ic_open_in_browser_black_24dp),
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position) {
                            case 1:
                                Intent intent = new Intent(activity, MainActivity.class);
                                activity.startActivity(intent);
                                break;
                            case 3:
                                Uri webPage1 = Uri.parse("https://m.reddit.com/r/memes/");
                                Intent webIntent1 = new Intent(Intent.ACTION_VIEW, webPage1);
                                if (webIntent1.resolveActivity(activity.getPackageManager()) != null) {
                                    activity.startActivity(webIntent1);
                                }
                                break;
                            case 4:
                                Uri webPage2 = Uri.parse("http://knowyourmeme.com");
                                Intent webIntent2 = new Intent(Intent.ACTION_VIEW, webPage2);
                                if (webIntent2.resolveActivity(activity.getPackageManager()) != null) {
                                    activity.startActivity(webIntent2);
                                }
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }
}
