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
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    int friendAvatar = R.drawable.user_avatar;
    TextView textViewNameAndSurnameNavHeader;
    SharedPreferences sharedPreferences;
    String id = "";
    String name = "";
    String surname = "";
    ArrayList<Message> messages = new ArrayList<>();
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
        header.setOnClickListener(this);
        textViewNameAndSurnameNavHeader = (TextView) header.findViewById(R.id.textViewNameAndSurnameNavHeader);
        textViewNameAndSurnameNavHeader.setText(name + " " + surname);

        sqLiteCreater = new SQLiteCreater(this, "Database", null, 1);
        getSQLiteMessages();
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nav_header:
                intent = new Intent(MessageActivity.this, ProfileActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                break;
        }
    }


    public class GetAllMessages extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";


        @Override
        protected String doInBackground(Void... params) {

            try {
                Log.d("MyLogs", "Начало получение сообщений");
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/get_messages.php?action=all_messages&author_id=" + id);
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
                messages.clear();
                Message message;
                while (i < jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(i);
                    message = new Message(jsonObject.getString("id"), jsonObject.getString("author_id"), jsonObject.getString("client_id"), jsonObject.getString("type"), jsonObject.getString("name"), AES.decrypt(jsonObject.getString("last_message"), password), jsonObject.getString("last_message_time"), AES.decrypt(jsonObject.getString("author_name"), password), AES.decrypt(jsonObject.getString("client_name"), password));
                    Log.d("MyLogs", jsonObject.toString());
                    if (message.getAuthorId().equals(id)) {
                        message.setUsersId(message.getClientId());
                        message.setQuantity(jsonObject.getString("is_read_1"));
                        message.setUserNameSurname(message.getClientName());
                    } else {
                        message.setUsersId(message.getAuthorId());
                        message.setQuantity(jsonObject.getString("is_read_2"));
                        message.setUserNameSurname(message.getAuthorName());
                    }
                    messages.add(message);
                    i++;
                    Log.d("MyLogs", String.valueOf(messages.size()));
                }
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

    }

    public void createListMessages() {

        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(messages.size());
        Map<String, Object> map;
        for (int i = 0; i < messages.size(); i++) {
            map = new HashMap<String, Object>();
            map.put("name",  messages.get(i).getUserNameSurname());
            map.put("message", messages.get(i).getText());
            map.put("messageTimeDate", messages.get(i).getDateTime());
            map.put("messageQuantity", messages.get(i).getQuantity());
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
                String[] words = messages.get(position).getUserNameSurname().split(" ");
                Intent intent = new Intent(MessageActivity.this, DialogActivity.class);
                intent.putExtra("id", messages.get(position).getUsersId());
                intent.putExtra("name", words[0]);
                intent.putExtra("surname", words[1]);
                startActivity(intent);
            }
        });

    }

    public void updateSQLiteMessages() {

        SQLiteDatabase db = sqLiteCreater.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < messages.size(); i++, i++) {
            contentValues.put("id", Integer.valueOf(messages.get(i).getUsersId()));
            contentValues.put("last_message_text", messages.get(i).getText());
            contentValues.put("last_message_time", messages.get(i).getDateTime());
            contentValues.put("friend", messages.get(i).getUserNameSurname());
            contentValues.put("quantity", Integer.valueOf(messages.get(i).getQuantity()));
            long rowID = db.update("Messages", contentValues, "id = ?", new String[] { messages.get(i).getUsersId() });
        }
    }

    public void getSQLiteMessages() {

        messages.clear();
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = sqLiteCreater.getWritableDatabase();
        Cursor c = db.query("Messages", null, null, null, null, null, null);
        if (c.moveToFirst() ) {
            int idIndex = c.getColumnIndex("id");
            int last_message_textIndex = c.getColumnIndex("last_message_text");
            int last_message_timeIndex = c.getColumnIndex("last_message_time");
            int friendIndex = c.getColumnIndex("friend");
            int quantityIndex = c.getColumnIndex("quantity");
            Message message;
            do {
                message = new Message(String.valueOf(c.getInt(idIndex)), c.getString(friendIndex), c.getString(last_message_textIndex) , String.valueOf(c.getInt(quantityIndex)), c.getString(last_message_timeIndex));
                messages.add(message);
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