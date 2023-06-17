package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavigationForAllActivities {
    public static void SetNavigationForActivities(DrawerLayout drawerLayout,
                                                  NavigationView navigationView,
                                                  Activity activity, Customer currentCustomer) {


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent intent;
                if (itemId == R.id.home_page) {
                    intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("customer",currentCustomer);
                    activity.startActivity(intent);
                } else if (itemId == R.id.create_sport_activity) {
                    intent = new Intent(activity, CreateSportActivity.class);
                    intent.putExtra("customer",currentCustomer);
                    activity.startActivity(intent);
                } else if (itemId == R.id.projectInfo) {
                    intent = new Intent(activity, ProjectInfoActivity.class);
                    intent.putExtra("customer",currentCustomer);
                    activity.startActivity(intent);
                } else if (itemId == R.id.view_sport_activity) {
                    intent = new Intent(activity, MySportingActivitiesActivity.class);
                    intent.putExtra("customer",currentCustomer);
                    activity.startActivity(intent);
                } else if (itemId == R.id.exit_app) {
                    showExitDialog(activity);
                } else if (itemId == R.id.logout) {
                    showLogoutDialog(activity);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private static void showExitDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("אשרו יציאה");
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
         }
        });
        builder.setNegativeButton("ביטול", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void showLogoutDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("אשרו התנתקות?");
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity, WelcomeUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
            }
        }).setNegativeButton("ביטול", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
