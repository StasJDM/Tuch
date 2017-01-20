package com.technologies.tuch.tuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class ContactsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    int avatar = R.drawable.user_avatar;
    TextView textViewNameAndSurnameNavHeader;
    SharedPreferences sharedPreferences;
    LinearLayout linearLayout;
    String id = "";
    String name = "";
    String surname = "";
    ArrayList<Contact> contacts;
    LinearLayout buttonSearchFriends;
    LinearLayout buttonInviteFriends;
    FrameLayout frameLayoutNewFriends;
    TextView textViewNewFriendsQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        surname = sharedPreferences.getString("surname", "");
        Log.d("MyLogs", id);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.create_message_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, CreateMessageActivity.class);
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

        textViewNewFriendsQuantity = (TextView)findViewById(R.id.textViewNewFriendQuantity);

        buttonSearchFriends = (LinearLayout)findViewById(R.id.buttonSearchFriends);
        buttonInviteFriends = (LinearLayout)findViewById(R.id.buttonInviteFriends);
        frameLayoutNewFriends = (FrameLayout)findViewById(R.id.frameLayoutNewFriends);
        buttonSearchFriends.setOnClickListener(this);
        buttonInviteFriends.setOnClickListener(this);
        frameLayoutNewFriends.setOnClickListener(this);

        new GetFriends().execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.buttonInviteFriends:
                break;
            case R.id.buttonSearchFriends:
                intent = new Intent(ContactsActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.frameLayoutNewFriends:
                intent = new Intent(ContactsActivity.this, NewFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_header:
                intent = new Intent(ContactsActivity.this, ProfileActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                break;
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
        getMenuInflater().inflate(R.menu.contacts, menu);
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
                intent = new Intent(ContactsActivity.this, MessageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contacts:
                intent = new Intent(ContactsActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_collection:
                intent = new Intent(ContactsActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                intent = new Intent(ContactsActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class GetNewFriends extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";


        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/contacts.php?action=get_new_contacts&id=" + id);
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
                Log.d("MyLogs", resultJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONArray jsonArray = new JSONArray(strJson);
                JSONObject jsonObject;
                int i = 0;
                while (i < jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.d("MyLogs", jsonObject.toString());
                    String newFriends = jsonObject.getString("new_friends");
                    if (!newFriends.equals(null)) {
                        textViewNewFriendsQuantity.setText(newFriends);
                        textViewNewFriendsQuantity.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        textViewNewFriendsQuantity.setPadding(3, 3, 3, 3);
                    } else {
                        textViewNewFriendsQuantity.setText("");
                        textViewNewFriendsQuantity.setTextColor(null);
                        textViewNewFriendsQuantity.setPadding(0, 0, 0, 0);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class GetFriends extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";


        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/contacts.php?action=get_contacts&id=" + id);
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
                Log.d("MyLogs", resultJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONArray jsonArray = new JSONArray(strJson);
                JSONObject jsonObject;
                int i = 0;
                Contact contact;
                while (i < jsonArray.length()) {
                    contact = new Contact();
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.d("MyLogs", jsonObject.toString());
                    contact.setType(jsonObject.getString("type"));
                    contact.setContact_1_id(jsonObject.getString("contact_1_id"));
                    contact.setContact_2_id(jsonObject.getString("contact_2_id"));
                    contact.setContact_1_name(jsonObject.getString("contact_1_name"));
                    contact.setContact_2_name(jsonObject.getString("contact_2_name"));
                    if (jsonObject.getString("type").equals("1")) {
                        if (contacts.get(i).getContact_1_id().equals(id)){
                            contact.setFriend_id((contacts.get(i).getContact_1_id()));
                            contact.setFriend_name(contacts.get(i).getContact_1_name());
                        } else if(contacts.get(i).getContact_2_id().equals(id)) {
                            contact.setFriend_id((contacts.get(i).getContact_2_id()));
                            contact.setFriend_name(contacts.get(i).getContact_2_name());
                        }
                    }
                    i++;
                }
                createListMessages();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createListMessages(){

        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(contacts.size());
        Map<String, Object> map;
        for (int i=0; i < contacts.size(); i++){
            map = new HashMap<String, Object>();
            map.put("name", contacts.get(i).getFriend_name());
            map.put("contactAvatar", avatar);
            arrayList.add(map);
        }

        String[] from = {"name", "contactAvatar"};
        int[] to = {R.id.textViewContactName, R.id.imageViewContactAvatar};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.contacts_list_item, from, to);
        ListView listViewContacts = (ListView)findViewById(R.id.listViewContacts);
        listViewContacts.setAdapter(simpleAdapter);

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactsActivity.this, ProfileActivity.class);
                intent.putExtra("id", contacts.get(position).getFriend_id());
                intent.putExtra("name_surname", contacts.get(position).getFriend_name());
                startActivity(intent);
            }
        });
    }
}
