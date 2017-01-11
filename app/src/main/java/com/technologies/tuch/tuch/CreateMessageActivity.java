package com.technologies.tuch.tuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class CreateMessageActivity extends AppCompatActivity implements OnClickListener{

    int avatar = R.drawable.user_avatar;
    SharedPreferences sharedPreferences;
    String id = "";
    String name = "";
    String surname = "";
    LinearLayout btnCreateGroupChat;
    ArrayList<String> contact_1_id = new ArrayList();
    ArrayList<String> contact_2_id = new ArrayList();
    ArrayList<String> contact_1_name_surname = new ArrayList();
    ArrayList<String> contact_2_name_surname = new ArrayList();
    ArrayList<String> friend_id = new ArrayList();
    ArrayList<String> friend_names = new ArrayList();
    ArrayList<String> type = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        surname = sharedPreferences.getString("surname", "");

        new GetContacts().execute();

        btnCreateGroupChat = (LinearLayout)findViewById(R.id.btnCreateGroupChat);
        btnCreateGroupChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateGroupChat:
                Intent intent = new Intent(CreateMessageActivity.this, GroupChatActivity.class);
                startActivity(intent);
        }
    }

    public class GetContacts extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";


        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/get_contacts.php?action=get_contacts&id=" + id);
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
                    /*
                        Тут все идеально работает, не трогай и даже не читай
                        Если все же прочитал, то слушай
                        Тут крч возвращает два имени, мое и другана моего, потом эта штука сама решает какое имя запихать в список друзей, а я дальше делаю другое
                    */
                    type.add(jsonObject.getString("type"));
                    contact_1_id.add(jsonObject.getString("contact_1_id"));
                    contact_2_id.add(jsonObject.getString("contact_2_id"));
                    contact_1_name_surname.add(jsonObject.getString("contact_1_name_surname"));
                    contact_2_name_surname.add(jsonObject.getString("contact_2_name_surname"));
                    Log.d("MyLogs", "Contact 1 id : "+contact_1_id.get(i)+" Contact 2 id : "+contact_2_id.get(i));
                    if (jsonObject.getString("type").equals("1")) {
                        if (contact_1_id.get(i).equals(id)){
                            friend_id.add(contact_2_id.get(i));
                            friend_names.add(contact_2_name_surname.get(i));
                        } else if(contact_2_id.get(i).equals(id)){
                            friend_id.add(contact_1_id.get(i));
                            friend_names.add(contact_1_name_surname.get(i));
                        }
                    }
                    i++;
                }
                createListFriends();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createListFriends() {
        ListView listViewCreateMessage = (ListView)findViewById(R.id.listViewCreateMessage);
        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(friend_names.size());
        Map<String, Object> map;
        for (int i=0; i < friend_names.size(); i++){
            map = new HashMap<String, Object>();
            map.put("name", friend_names.get(i));
            map.put("contactAvatar", avatar);
            arrayList.add(map);
        }

        String[] from = {"name", "contactAvatar"};
        int[] to = {R.id.textViewCreaterMessageContactName, R.id.imageViewCreateMessageContactAvatar};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.create_message_list_item, from, to);
        listViewCreateMessage.setAdapter(simpleAdapter);

        listViewCreateMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        listViewCreateMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CreateMessageActivity.this, DialogActivity.class);
                intent.putExtra("id", friend_id.get(position));
                String[] namesurname = friend_names.get(position).split(" ");
                String intent_name = namesurname[0];
                String intent_surname = namesurname[1];
                intent.putExtra("name", intent_name);
                intent.putExtra("surname", intent_surname);
                startActivity(intent);
            }
        });
    }
}
