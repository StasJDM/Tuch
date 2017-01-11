package com.technologies.tuch.tuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.technologies.tuch.tuch.Settings.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DialogActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener {

    private final String USER_AGENT = "Mozilla/5.0";
    ListView listViewDialog;
    TextView textViewNameAndSurnameNavHeader;
    EditText editTextMessageText;
    SharedPreferences sharedPreferences;
    ImageView imageViewSendMessage;
    ImageView imageViewShowStickers;
    String id = "";
    String name = "";
    String surname = "";
    String friend_id = "";
    String friend_name = "";
    String friend_surname = "";
    ArrayList<String> message_author_id = new ArrayList<>();
    ArrayList<String> message_text = new ArrayList<>();
    ArrayList<String> sticker_id = new ArrayList<>();
    ArrayList<String> messageTimeDate = new ArrayList<>();
    ArrayList<DialogItem> data = new ArrayList<DialogItem>();
    String new_message_author_id = "";
    String new_message_client_id = "";
    String new_message_text = "";
    String new_message_sticker_id = "";
    String password = "dsobgigsblsd934n398gdjm349tgwle5dh3ngdfs9g34nirf234342refe";
    DialogItem dialogItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        friend_id = intent.getStringExtra("id");
        Log.d("MyLogs", friend_id);

        friend_name = intent.getStringExtra("name");
        friend_surname = intent.getStringExtra("surname");


        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        surname = sharedPreferences.getString("surname", "");

        new_message_author_id = id;
        new_message_client_id = friend_id;

        editTextMessageText = (EditText)findViewById(R.id.editTextMessageText);
        imageViewSendMessage = (ImageView)findViewById(R.id.imageViewSendMessage);
        imageViewShowStickers = (ImageView)findViewById(R.id.imageViewShowStickers);
        imageViewSendMessage.setOnClickListener(this);
        imageViewShowStickers.setOnClickListener(this);

        new_message_text = "";
        new_message_sticker_id = "";

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        textViewNameAndSurnameNavHeader = (TextView)header.findViewById(R.id.textViewNameAndSurnameNavHeader);
        textViewNameAndSurnameNavHeader.setText(name + " " + surname);

        String titleName = friend_name+" "+friend_surname;
        setTitle(titleName);

        new getMessages().execute();
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
        getMenuInflater().inflate(R.menu.dialog, menu);
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
                intent = new Intent(DialogActivity.this, MessageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contacts:
                intent = new Intent(DialogActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_collection:
                intent = new Intent(DialogActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                intent = new Intent(DialogActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onClickNavHeader(View view) {
        Intent intent = new Intent(DialogActivity.this, ProfileActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imageViewSendMessage:
                new_message_text = editTextMessageText.getText().toString();
                try {
                    Log.d("MyLogs", "Original: "+new_message_text);
                    new_message_text = AES.encrypt(new_message_text, password);
                    Log.d("MyLogs", "Encrypted: "+new_message_text);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (!new_message_text.equals("")) {
                    //new_message_sticker_id = "";
                    new sendMessage().execute();
                    editTextMessageText.setText("");
                }
                break;
            case R.id.imageViewShowStickers:
                Intent intent = new Intent(DialogActivity.this, SelectStickerActivity.class);
                startActivity(intent);
        }
    }

    public class sendMessage extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                String a = name+" "+surname;
                String c = friend_name+" "+friend_surname;
                String aa = AES.encrypt(a, password);
                String cc = AES.encrypt(c, password);
                String urlParameters  = "author_id="+new_message_author_id+"&client_id="+new_message_client_id+"&type=0"+"&text="+new_message_text+"&sticker_id="+new_message_sticker_id+"&author_name="+aa + "&client_name="+cc;
                String url = "http://sdyusshor1novoch.ru/tuch/send_message.php";
                URL obj = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)obj.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);
                httpURLConnection.setRequestProperty( "charset", "UTF-8");
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,ru;q=0.6");
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = httpURLConnection.getResponseCode();
                Log.d("MyLogs", url);
                Log.d("MyLogs", urlParameters);
                Log.d("MyLogs", String.valueOf(responseCode));

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.d("MyLogs", response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            new getMessages().execute();
        }
    }

    public class getMessages extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/get_messages.php?action=select_one_messages&author_id=" + id + "&client_id=" + friend_id);
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
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.d("MyLogs", jsonObject.toString());
                    message_author_id.add(jsonObject.getString("author_id"));
                    message_text.add(AES.decrypt(jsonObject.getString("text"), password));
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(Long.valueOf(jsonObject.getInt("time")));
                    SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm");
                    String formatted = formatDate.format(c.getTime());
                    messageTimeDate.add(formatted);
                    c.clear();
                    sticker_id.add(jsonObject.getString("sticker_id"));
                }
                Log.d("MyLogs", "Выполнен метод OnPostExecute");
                createListDialog();
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

    public void createListDialog() {

        for (int i = 0; i < message_text.size(); i++) {
            data.add(new DialogItem(message_text.get(i), messageTimeDate.get(i), message_author_id.get(i)));
            Log.d("MyLogs", message_text.get(i) + " " + messageTimeDate.get(i));
        }
        message_author_id.clear();
        message_text.clear();
        messageTimeDate.clear();
        sticker_id.clear();

        listViewDialog = (ListView)findViewById(R.id.listViewDialog);
        DialogAdapter dialogAdapter = new DialogAdapter(id, this, data);
        dialogAdapter.setNotifyOnChange(true);
        dialogAdapter.notifyDataSetChanged();
        listViewDialog.setAdapter(dialogAdapter);
        //listViewDialog.setOnClickListener(this);
    }
}
