package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView sign_in_or_greet;
    private Button aboutButton;
    private Customer currentCustomer;
    private Context context;
    private DrawerLayout drawerLayout;
    private Button sign_or_log_btn;
    private Button view_sport_activities;
    private AppDatabase app_db;
    private Button createSportActivity;
    private CustomerDao customerDao;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
        func_handle_sign_or_log_in();
        func_sign_in_or_greet();
        func_view_sport_activities();
        func_create_sport_activity();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificationBroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Button testingButton = findViewById(R.id.testingButton);
        testingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sendBroadcast(intent);
            }
        });
    }


    public void func_view_sport_activities() {
        view_sport_activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent view_sport_activities = new Intent(context, MySportingActivitiesActivity.class);
                                    view_sport_activities.putExtra("customer", currentCustomer);
                                    startActivity(view_sport_activities);
                                }
                            });
                        }
                    }).start();
            }
        });
    }

    public void func_handle_sign_or_log_in() {
        sign_or_log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WelcomeUserActivity.class);
                if (currentCustomer != null) {
                    intent.putExtra("customer", currentCustomer);
                    Toast.makeText(context, currentCustomer.getName() + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void func_sign_in_or_greet() {
        if (currentCustomer != null) {
            sign_in_or_greet.setText("שלום" + " " + currentCustomer.getName() + " " + " ברוכים הבאים ");
        }
    }

    public void func_create_sport_activity() {
        createSportActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCustomer == null) {
                    Toast.makeText(context, "אתם צריכים להירשם", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent_create_sport_activity = new Intent(context, CreateSportActivity.class);
                    intent_create_sport_activity.putExtra("customer", currentCustomer);
                    startActivity(intent_create_sport_activity);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void init() {
        context = getApplicationContext();
        sign_in_or_greet = findViewById(R.id.sign_in_or_greet);
        app_db = AppDatabase.getDatabase(this);
        customerDao = app_db.customerDAO();
        view_sport_activities = findViewById(R.id.view_sport_activities);
        createSportActivity = findViewById(R.id.create_sport_activity);
        Intent intent = getIntent();
        currentCustomer = (Customer) intent.getSerializableExtra("customer");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                currentCustomer = (Customer) intent.getSerializableExtra("customer");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentCustomer != null) {
                            sign_in_or_greet.setText("שלום " + currentCustomer.getName() + " ברוכים הבאים ");
                        }
                    }
                });
            }
        }).start();
        sign_or_log_btn = findViewById(R.id.sign_in);
        drawer = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.nav_menu);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        context = getApplicationContext();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationForAllActivities.SetNavigationForActivities(drawerLayout,
                navigationView, this, currentCustomer);

    }

}