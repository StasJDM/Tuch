package com.technologies.tuch.tuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.technologies.tuch.tuch.Settings.AccountSettingsActivity;
import com.technologies.tuch.tuch.Settings.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener{

    SharedPreferences sharedPreferences;
    ListView listViewPosts;
    TextView textViewNameAndSurname;
    TextView textViewNameAndSurnameNavHeader;
    TextView textViewCreateMessage;
    String my_name = "";
    String my_surname = "";
    String my_id = "";
    String id = "";
    String name_surname = "";
    String name = "";
    String surname = "";
    ArrayList<String> author_id = new ArrayList();
    ArrayList<String> author_name = new ArrayList();
    ArrayList<String> author_surname = new ArrayList();
    ArrayList<String> post_id = new ArrayList();
    ArrayList<String> date_create = new ArrayList();
    ArrayList<String> text = new ArrayList();
    ArrayList<String> author_name_surname = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        my_id = sharedPreferences.getString("id", "");
        my_name = sharedPreferences.getString("name", "");
        my_surname = sharedPreferences.getString("surname", "");

        Intent intent =  getIntent();
        id = intent.getStringExtra("id");
        if (id.equals(my_id)){
            name_surname = my_name + " " + my_surname;
        } else {
            name_surname = intent.getStringExtra("name_surname");
            setTitle(name_surname);
        }

        String[] words = name_surname.split(" ");
        name = words[0];
        surname = words[1];

        textViewNameAndSurname = (TextView)findViewById(R.id.textViewNameAndSurname);
        textViewNameAndSurname.setText(name_surname);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.create_message_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CreateMessageActivity.class);
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

        textViewNameAndSurnameNavHeader = (TextView)header.findViewById(R.id.textViewNameAndSurnameNavHeader);
        textViewNameAndSurnameNavHeader.setText(my_name + " " + my_surname);

        textViewCreateMessage = (TextView)findViewById(R.id.textViewCreateMessage);
        textViewCreateMessage.setOnClickListener(this);

        new getPosts().execute();
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
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_profile) {
            Intent intent = new Intent(ProfileActivity.this, AccountSettingsActivity.class);
            startActivity(intent);
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
                intent = new Intent(ProfileActivity.this, MessageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contacts:
                intent = new Intent(ProfileActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_collection:
                intent = new Intent(ProfileActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                intent = new Intent(ProfileActivity.this, SettingsActivity.class);
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
        switch (v.getId()){
            case R.id.textViewCreateMessage:
                intent = new Intent(ProfileActivity.this, DialogActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("surname", surname);
                startActivity(intent);
        }
    }

    public void onClickNavHeader(View view) {
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        intent.putExtra("id", my_id);
        startActivity(intent);
        finish();
    }


    public class getPosts extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/posts.php?action=select&author_id=" + id);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                resultJson = stringBuffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            try {
                Log.d("MyLogs", "Начал выполняться метод onPostExecute");
                JSONArray jsonArray = new JSONArray(strJson);
                JSONObject jsonObject;
                int i = 0;
                while (i < jsonArray.length()){
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.d("MyLogs", jsonObject.toString());
                    author_id.add(jsonObject.getString("author_id"));
                    author_name.add(jsonObject.getString("author_name"));
                    author_surname.add(jsonObject.getString("author_surname"));
                    author_name_surname.add(author_name.get(i) + " " + author_surname.get(i));
                    post_id.add(jsonObject.getString("id"));
                    date_create.add(jsonObject.getString("date_create"));
                    text.add(jsonObject.getString("text"));
                    i++;
                }
                createListPosts();
                Log.d("MyLogs", "Выполнен метод OnPostExecute");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createListPosts(){

        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(post_id.size());
        Map<String, Object> map;
        for (int i = post_id.size(); i!=0; i--) {
            map = new HashMap<String, Object>();
            map.put("author_name_surname", author_name_surname.get(i-1));
            Log.d("MyLogs", author_name_surname.get(i-1));
            map.put("date_create", date_create.get(i-1));
            Log.d("MyLogs", date_create.get(i-1));
            map.put("text", text.get(i-1));
            Log.d("MyLogs", text.get(i-1));
            arrayList.add(map);
        }
        String[] from = {"author_name_surname", "date_create", "text"};
        int[] to = {R.id.textViewOnePoatNameSurname, R.id.textViewOnePostDateTime, R.id.textViewOnePostText};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.one_post_list_item, from, to);
        listViewPosts = (ListView)findViewById(R.id.listViewPosts);
        listViewPosts.setAdapter(simpleAdapter);

    }
}
