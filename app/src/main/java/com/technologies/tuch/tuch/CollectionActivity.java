package com.technologies.tuch.tuch;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.technologies.tuch.tuch.Settings.SettingsActivity;

public class CollectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    TextView textViewNameAndSurnameNavHeader;
    SharedPreferences sharedPreferences;
    String id = "";
    String name = "";
    String surname = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        surname = sharedPreferences.getString("surname", "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.create_message_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollectionActivity.this, CreateMessageActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(this);

        textViewNameAndSurnameNavHeader = (TextView)header.findViewById(R.id.textViewNameAndSurnameNavHeader);
        textViewNameAndSurnameNavHeader.setText(name + " " + surname);

        TabHost tabHostCollection = (TabHost)findViewById(R.id.tabHostCollection);
        tabHostCollection.setup();

        TabHost.TabSpec tabSpec = tabHostCollection.newTabSpec("tag1");
        tabSpec.setContent(R.id.linearLayoutTab1);
        tabSpec.setIndicator("Моя коллекция");
        tabHostCollection.addTab(tabSpec);

        tabSpec = tabHostCollection.newTabSpec("tag2");
        tabSpec.setContent(R.id.linearLayoutTab2);
        tabSpec.setIndicator("Магазин");
        tabHostCollection.addTab(tabSpec);

        tabHostCollection.setCurrentTabByTag("tag1");

        TabWidget tabWidget = (TabWidget)findViewById(android.R.id.tabs);
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            ViewGroup tab = (ViewGroup) tabWidget.getChildAt(i);

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.nav_messages:
                intent = new Intent(CollectionActivity.this, MessageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contacts:
                intent = new Intent(CollectionActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_collection:
                intent = new Intent(CollectionActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                intent = new Intent(CollectionActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nav_header:
                intent = new Intent(CollectionActivity.this, ProfileActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                break;
        }
    }
}
