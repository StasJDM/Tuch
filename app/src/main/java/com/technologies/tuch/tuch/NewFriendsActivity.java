package com.technologies.tuch.tuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

public class NewFriendsActivity extends AppCompatActivity {

    int avatar = R.drawable.user_avatar;
    ListView listViewNewFriends;
    SharedPreferences sharedPreferences;
    String id = "";
    String name = "";
    String surname = "";
    ArrayList<String> contact_1_id = new ArrayList();
    ArrayList<String> contact_2_id = new ArrayList();
    ArrayList<String> contact_1_name_surname = new ArrayList();
    ArrayList<String> contact_2_name_surname = new ArrayList();
    ArrayList<String> friend_id = new ArrayList();
    ArrayList<String> friend_names = new ArrayList();
    ArrayList<String> type = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        surname = sharedPreferences.getString("surname", "");


        new GetNewFriend().execute();
    }


    public class GetNewFriend extends AsyncTask<Void, Void, String> {

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
                    type.add(jsonObject.getString("type"));
                    contact_1_id.add(jsonObject.getString("contact_1_id"));
                    contact_2_id.add(jsonObject.getString("contact_2_id"));
                    contact_1_name_surname.add(jsonObject.getString("contact_1_name_surname"));
                    contact_2_name_surname.add(jsonObject.getString("contact_2_name_surname"));
                    Log.d("MyLogs", "Contact 1 id : "+contact_1_id.get(i)+" Contact 2 id : "+contact_2_id.get(i));
                    if (jsonObject.getString("type").equals("0")) {
                        if (contact_1_id.get(i).equals(id)){
                        } else if(contact_2_id.get(i).equals(id)){
                            friend_id.add(contact_1_id.get(i));
                            friend_names.add(contact_1_name_surname.get(i));
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

        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(friend_names.size());
        Map<String, Object> map;
        for (int i=0; i < friend_names.size(); i++){
            map = new HashMap<String, Object>();
            map.put("name", friend_names.get(i));
            map.put("contactAvatar", avatar);
            arrayList.add(map);
        }

        String[] from = {"name", "contactAvatar"};
        int[] to = {R.id.textViewNewFriendNameSurname, R.id.imageViewContactAvatar};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.list_item_new_friends, from, to);
        listViewNewFriends = (ListView) findViewById(R.id.listViewNewFriends);
        listViewNewFriends.setAdapter(simpleAdapter);
        listViewNewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MyLogs", "OnClick");
                Intent intent = new Intent(NewFriendsActivity.this, ProfileActivity.class);
                intent.putExtra("id", friend_id.get(position));
                intent.putExtra("name_surname", friend_names.get(position));
                startActivity(intent);
            }
        });
    }
}