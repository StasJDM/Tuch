package com.technologies.tuch.tuch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.technologies.tuch.tuch.SQLite.SQLiteCreater;

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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class LoginActivity extends AppCompatActivity implements OnClickListener{

    final String AESpassword = "dsobgigsblsd934n398gdjm349tgwle5dh3ngdfs9g34nirf234342refe";
    SharedPreferences sharedPreferences;
    Button btnLogin;
    TextView btnLoginToRegister;
    EditText etLogin;
    EditText etPassword;
    String login;
    String password;
    String name = "";
    String surname = "";
    String id;
    SQLiteCreater sqLiteCreater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLogin=(EditText)findViewById(R.id.etLogin);
        etPassword=(EditText)findViewById(R.id.etPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLoginToRegister=(TextView)findViewById(R.id.btnLoginToRegister);
        btnLogin.setOnClickListener(this);
        btnLoginToRegister.setOnClickListener(this);

        sqLiteCreater = new SQLiteCreater(this, "Database", null, 1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
                if (!etLogin.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
                    login = etLogin.getText().toString();
                    password = etPassword.getText().toString();
                    new RequestTask().execute();
                } else if (etLogin.getText().toString().equals("")) {
                    etLogin.setHintTextColor(Color.RED);
                } else if (etPassword.getText().toString().equals("")) {
                    etPassword.setHintTextColor(Color.RED);
                }
                Log.d("myLogs","Кнопка работает стабильно");
                break;
            case R.id.btnLoginToRegister:
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    public class RequestTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.d("MyLogs", login+" "+password);
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/login.php?phone="
                        +login
                        +"&password="
                        +password);
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
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String true_password = AES.decrypt(jsonObject.getString("password"), AESpassword);
                Log.d("MyLogs", true_password);

                if (password.equals(true_password)) {
                    name = AES.decrypt(jsonObject.getString("name"), AESpassword);
                    surname = AES.decrypt(jsonObject.getString("surname"), AESpassword);
                    id = jsonObject.getString("_id");
                    sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("surname", surname);
                    editor.putString("id", id);
                    editor.putString("password", password);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MessageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    etPassword.setText("");
                    etPassword.setHintTextColor(Color.RED);
                }
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

}
