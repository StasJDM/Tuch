package com.technologies.tuch.tuch;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
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

public class GroupChatActivity extends AppCompatActivity{

    int avatar = R.drawable.user_avatar;
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
    CheckBox checkBoxGroupChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        name = sharedPreferences.getString("name", "");
        surname = sharedPreferences.getString("surname", "");


        checkBoxGroupChat = (CheckBox)findViewById(R.id.checkBoxGroupChat);
        new GetFriends().execute();

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
                while (i < jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.d("MyLogs", jsonObject.toString());
                    contact_1_id.add(jsonObject.getString("contact_1_id"));
                    contact_2_id.add(jsonObject.getString("contact_2_id"));
                    contact_1_name_surname.add(jsonObject.getString("contact_1_name"));
                    contact_2_name_surname.add(jsonObject.getString("contact_2_name"));
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
                    Log.d("MyLogs", friend_id.get(i));
                    i++;
                }
                createListGroupChat();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createListGroupChat() {

        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(friend_names.size());
        Map<String, Object> map;
        for (int i=0; i < friend_names.size(); i++){
            map = new HashMap<String, Object>();
            map.put("name", friend_names.get(i));
            map.put("contactAvatar", avatar);
            arrayList.add(map);
        }

        String[] from = {"name", "contactAvatar"};
        int[] to = {R.id.textViewCreaterGroupChatContactName, R.id.imageViewCreateGroupChatContactAvatar};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.create_group_chat_list_item, from, to);
        ListView listViewGroupChat = (ListView)findViewById(R.id.listViewGroupChat);
        listViewGroupChat.setAdapter(simpleAdapter);
    }

}
