package com.technologies.tuch.tuch;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.technologies.tuch.tuch.SQLite.SQLiteCreater;
import com.technologies.tuch.tuch.Settings.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MessageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int friendAvatar = R.drawable.user_avatar;
    TextView textViewNameAndSurnameNavHeader;
    SharedPreferences sharedPreferences;
    String id = "";
    String name = "";
    String surname = "";
    ArrayList<String> message_id = new ArrayList();
    ArrayList<String> message_author_id = new ArrayList();
    ArrayList<String> message_client_id = new ArrayList();
    ArrayList<String> message_type = new ArrayList();
    ArrayList<String> message_name = new ArrayList();
    ArrayList<String> message_text = new ArrayList();
    ArrayList<String> message_date_time = new ArrayList();
    ArrayList<String> messageQuantity = new ArrayList<>();
    ArrayList<String> users_id = new ArrayList();
    //ArrayList<String> user_name = new ArrayList();
    //ArrayList<String> user_surname = new ArrayList();
    ArrayList<String> user_name_surname = new ArrayList<>();
    ArrayList<String> author_name = new ArrayList<>();
    ArrayList<String> client_name = new ArrayList<>();
    int i_gui = 0;
    String password = "dsobgigsblsd934n398gdjm349tgwle5dh3ngdfs9g34nirf234342refe";
    SQLiteCreater sqLiteCreater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.create_message_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, CreateMessageActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        surname = sharedPreferences.getString("surname", "");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        textViewNameAndSurnameNavHeader = (TextView) header.findViewById(R.id.textViewNameAndSurnameNavHeader);
        textViewNameAndSurnameNavHeader.setText(name + " " + surname);

        sqLiteCreater = new SQLiteCreater(this, "Database", null, 1);
        getSQLiteMessages();
        //new GetAllMessages().execute();
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
        getMenuInflater().inflate(R.menu.message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create_message) {
            Intent intent = new Intent(MessageActivity.this, CreateMessageActivity.class);
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
        switch (id) {
            case R.id.nav_messages:
                intent = new Intent(MessageActivity.this, MessageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contacts:
                intent = new Intent(MessageActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_collection:
                intent = new Intent(MessageActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                intent = new Intent(MessageActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickNavHeader(View view) {

        Intent intent = new Intent(MessageActivity.this, ProfileActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

    public class GetAllMessages extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";


        @Override
        protected String doInBackground(Void... params) {

            try {
                i_gui = 0;
                Log.d("MyLogs", "Начало получение сообщений");
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/get_messages.php?action=select_all_messages&author_id=" + id);
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
                Log.d("MyLogs", "Начал выполняться метод onPostExecute");
                JSONArray jsonArray = new JSONArray(strJson);
                JSONObject jsonObject;
                int i = 0;
                users_id.clear();
                //user_name.clear();
                //user_surname.clear();
                user_name_surname.clear();
                message_text.clear();
                messageQuantity.clear();
                message_date_time.clear();
                while (i < jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.d("MyLogs", jsonObject.toString());
                    message_id.add(jsonObject.getString("id"));
                    message_author_id.add(jsonObject.getString("author_id"));
                    message_client_id.add(jsonObject.getString("client_id"));
                    message_type.add(jsonObject.getString("type"));
                    message_name.add(jsonObject.getString("name"));
                    author_name.add(AES.decrypt(jsonObject.getString("author_name_surname"), password));
                    client_name.add(AES.decrypt(jsonObject.getString("client_name_surname"), password));
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(Long.valueOf(jsonObject.getInt("last_message_time")));
                    SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm");
                    String formatted = formatDate.format(c.getTime());

                    message_text.add(AES.decrypt(jsonObject.getString("last_message"), password));

                    message_date_time.add(formatted);
                    if (message_author_id.get(i).equals(id)) {
                        users_id.add(message_client_id.get(i));
                        messageQuantity.add(jsonObject.getString("is_read_1"));
                        user_name_surname.add(client_name.get(i));
                    } else {
                        users_id.add(message_author_id.get(i));
                        messageQuantity.add(jsonObject.getString("is_read_2"));
                        user_name_surname.add(author_name.get(i));
                    }
                    i++;
                    Log.d("MyLogs", String.valueOf(users_id.size()));
                }
                /*while (i_gui < users_id.size()) {
                    new GetUserInfo().execute();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }*/
                createListMessages();
                updateSQLiteMessages();
                Log.d("MyLogs", "Конец цикла получения сообщений");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }

        public class GetUserInfo extends AsyncTask<Void, Void, String> {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String resultJson = "";


            @Override
            protected String doInBackground(Void... params) {

                try {
                    Log.d("MyLogs", "Начало получения информации о пользователе");
                    String b = users_id.get(i_gui);
                    //a = 1;
                    Log.d("MyLogs", "User id : " + b);
                    URL url = new URL("http://sdyusshor1novoch.ru/tuch/get_user_info.php?action=select_from_id&id=" + b);
                    i_gui++;
                    if (i_gui==users_id.size()){
                        createListMessages();
                        updateSQLiteMessages();
                    }
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
                        //user_name.add(jsonObject.getString("name"));
                        //user_surname.add(jsonObject.getString("surname"));
                        //Log.d("MyLogs", users_id + " " + user_name + " " + user_surname);
                        i++;
                    }
                    Log.d("MyLogs", String.valueOf(i_gui) + "  " + users_id.size());
                    if (i_gui > users_id.size()) {
                        createListMessages();
                        updateSQLiteMessages();
                    }else {
                        new GetUserInfo().execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createListMessages() {

        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(users_id.size());
        Map<String, Object> map;
        for (int i = 0; i < users_id.size(); i++) {
            map = new HashMap<String, Object>();
            map.put("name", /*user_name.get(i) + " " + user_surname.get(i)*/ user_name_surname.get(i));
            map.put("message", message_text.get(i));
            map.put("messageTimeDate", message_date_time.get(i));
            map.put("messageQuantity", messageQuantity.get(i));
            map.put("friendAvatar", friendAvatar);
            arrayList.add(map);
        }

        String[] from = {"name", "message", "messageTimeDate", "messageQuantity", "friendAvatar"};
        int[] to = {R.id.textViewMessageAuthor, R.id.textViewMessageText, R.id.textViewMessageTimeDate, R.id.textViewQuantityMessage, R.id.imageViewFriendAvatar};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.message_list_item, from, to);
        ListView listViewMessages = (ListView) findViewById(R.id.listViewMessages);
        listViewMessages.setAdapter(simpleAdapter);

        listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] words = user_name_surname.get(position).split(" ");
                Intent intent = new Intent(MessageActivity.this, DialogActivity.class);
                intent.putExtra("id", users_id.get(position));
                intent.putExtra("name", words[0]);
                intent.putExtra("surname", words[1]);
                startActivity(intent);
            }
        });

        /*users_id.clear();
        user_name.clear();
        user_surname.clear();
        message_text.clear();
        messageQuantity.clear();
        message_date_time.clear();*/
    }

    public void updateSQLiteMessages() {

        SQLiteDatabase db = sqLiteCreater.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < users_id.size(); i++, i++) {
            contentValues.put("id", Integer.valueOf(users_id.get(i)));
            contentValues.put("last_message_text", message_text.get(i));
            contentValues.put("last_message_time", message_date_time.get(i));
            contentValues.put("friend", /*user_name.get(i) + " " + user_surname.get(i)*/ user_name_surname.get(i));
            contentValues.put("quantity", Integer.valueOf(messageQuantity.get(i)));
            long rowID = db.update("Messages", contentValues, "id = ?", new String[] { users_id.get(i) });
        }
        /*users_id.clear();
        user_name.clear();
        user_surname.clear();
        message_text.clear();
        messageQuantity.clear();
        message_date_time.clear();*/
    }

    public void getSQLiteMessages() {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = sqLiteCreater.getWritableDatabase();
        Cursor c = db.query("Messages", null, null, null, null, null, null);
        if (c.moveToFirst() ) {
            int idIndex = c.getColumnIndex("id");
            int last_message_textIndex = c.getColumnIndex("last_message_text");
            int last_message_timeIndex = c.getColumnIndex("last_message_time");
            int friendIndex = c.getColumnIndex("friend");
            int quantityIndex = c.getColumnIndex("quantity");
            do {
                Log.d("MyLogs", "id = " + c.getInt(idIndex) + ", last message text = " + c.getString(last_message_textIndex) + ", last message time = " + c.getString(last_message_timeIndex) + ", friend = " + c.getString(friendIndex) + ", quantity = " + c.getString(quantityIndex));
                users_id.add(String.valueOf(c.getInt(idIndex)));
                user_name_surname.add(c.getString(friendIndex));
                message_text.add(c.getString(last_message_textIndex));
                messageQuantity.add(String.valueOf(c.getInt(quantityIndex)));
                message_date_time.add(c.getString(last_message_timeIndex));
            } while (c.moveToNext());
            createListMessages();
            new GetAllMessages().execute();
        } else {
            Log.d("MyLogs", "0 rows");
            createListMessages();
            new GetAllMessages().execute();
        }

    }
}