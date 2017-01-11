package com.technologies.tuch.tuch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    final String NOT_FOUND = "По запросу ничего не найдено";
    int avatar = R.drawable.user_avatar;
    ArrayList<String> id = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> surname = new ArrayList<String>();
    String word;
    ListView listViewSearch;
    ImageView buttonSearch;
    EditText editTextSearch;
    TextView textViewNotFound;
    String[] words;
    ArrayList<String> wrds = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listViewSearch = (ListView)findViewById(R.id.listViewSearch);
        editTextSearch = (EditText)findViewById(R.id.editTextSearch);
        textViewNotFound = (TextView)findViewById(R.id.textViewNotFound);
        buttonSearch = (ImageView)findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonSearch:
                if (!editTextSearch.getText().toString().equals("")) {
                    word = editTextSearch.getText().toString();
                    words = word.split(" ");
                    for (int i = 0; i<words.length; i++){
                        wrds.add(words[i]);
                    }
                    if (wrds.size() == 1) {
                        wrds.add("");
                    }
                    new Search().execute();
                } else {
                    break;
                }
                break;
        }
    }

    public class Search extends AsyncTask<Void, Void, String> {



        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/search.php?word_1=" + wrds.get(0) + "&word_2=" + wrds.get(1));
                id.clear();
                name.clear();
                surname.clear();
                wrds.clear();
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
                Log.d("MyLogs", "StrJson:"+strJson+"end");
                if (strJson.equals("null ")){
                    notFound();
                } else {
                    JSONArray jsonArray = new JSONArray(strJson);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Log.d("MyLogs", jsonObject.toString());
                        id.add(jsonObject.getString("_id"));
                        name.add(jsonObject.getString("name"));
                        surname.add(jsonObject.getString("surname"));
                    }
                    Log.d("MyLogs", "Выполнен метод OnPostExecute");
                    createListSearch();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createListSearch() {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>(id.size());
        Map<String, Object> map;
        for (int i=0; i < id.size(); i++){
            map = new HashMap<String, Object>();
            map.put("name", name.get(i) + " " + surname.get(i));
            map.put("contactAvatar", avatar);
            arrayList.add(map);
        }

        String[] from = {"name", "contactAvatar"};
        int[] to = {R.id.textViewContactName, R.id.imageViewContactAvatar};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.contacts_list_item, from, to);
        simpleAdapter.notifyDataSetChanged();
        listViewSearch.setAdapter(simpleAdapter);

        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                intent.putExtra("id", id.get(position));
                intent.putExtra("name_surname", name.get(position) + " " + surname.get(position));
                startActivity(intent);
            }
        });
    }

    public void notFound(){
        textViewNotFound.setText(NOT_FOUND);
    }
}
