package com.technologies.tuch.tuch.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.technologies.tuch.tuch.CollectionActivity;
import com.technologies.tuch.tuch.ContactsActivity;
import com.technologies.tuch.tuch.CreateMessageActivity;
import com.technologies.tuch.tuch.LoginActivity;
import com.technologies.tuch.tuch.MessageActivity;
import com.technologies.tuch.tuch.ProfileActivity;
import com.technologies.tuch.tuch.R;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    String[] nav_settings = {"Редактировать профиль", "Настройки приложения", "Справка", "О приложении", "Выход"};
    TextView textViewNameAndSurnameNavHeader;
    SharedPreferences sharedPreferences;
    String id = "";
    String name = "";
    String surname = "";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
                Intent intent = new Intent(SettingsActivity.this, CreateMessageActivity.class);
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

        ListView listViewSettings = (ListView)findViewById(R.id.listViewSettings);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.settings_list_item, nav_settings);
        listViewSettings.setAdapter(arrayAdapter);

        listViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(SettingsActivity.this, AccountSettingsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(SettingsActivity.this, AppSettingsActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(SettingsActivity.this, HelpActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(SettingsActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        sharedPreferences.edit().remove("id");
                        sharedPreferences.edit().remove("name");
                        sharedPreferences.edit().remove("surname");
                        sharedPreferences.edit().remove("c");
                        sharedPreferences.edit().clear();
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
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
        return false;
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.nav_messages:
                intent = new Intent(SettingsActivity.this, MessageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contacts:
                intent = new Intent(SettingsActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_collection:
                intent = new Intent(SettingsActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                intent = new Intent(SettingsActivity.this, SettingsActivity.class);
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
                intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                break;
        }
    }
}
