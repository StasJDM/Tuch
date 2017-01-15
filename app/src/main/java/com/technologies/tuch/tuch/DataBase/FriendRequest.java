package com.technologies.tuch.tuch.DataBase;

import android.os.AsyncTask;
import android.util.Log;

import com.technologies.tuch.tuch.AES;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by StasJDM on 14.01.2017.
 */
public class FriendRequest extends AsyncTask<Void, Void, String> {

    String my_id;
    String friend_id;
    String my_name_surname;
    String friend_name_surname;
    String password = "dsobgigsblsd934n398gdjm349tgwle5dh3ngdfs9g34nirf234342refe";


    public FriendRequest(String my_id, String friend_id, String my_name_surname, String friend_name_surname){
        this.my_id = my_id;
        this.friend_id = friend_id;
        this.my_name_surname = my_name_surname;
        this.friend_name_surname = my_name_surname;
    }

    HttpURLConnection httpURLConnection = null;
    BufferedReader bufferedReader = null;
    String resultJson = "";
    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected String doInBackground(Void... params) {

        try {
            Log.d("MyLogs", "Начало получения информации о пользователе");
            String urlParameters  = "action=new_contacts&contact_1_id=" + my_id + "&contact_2_id=" + friend_id + "&contact_1_name=" + AES.encrypt(my_name_surname, password) + "&contact_2_name=" +AES.encrypt(friend_name_surname, password) + "&type=0";
            String url = "http://sdyusshor1novoch.ru/tuch/friends_request.php";
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
        return resultJson;
    }
}