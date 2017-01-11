package com.technologies.tuch.tuch.Settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.technologies.tuch.tuch.R;

import java.net.HttpURLConnection;
import java.net.URL;

public class AccountSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName;
    EditText editTextSurname;
    EditText editTextPasswordOld;
    EditText editTextPassword1;
    EditText editTextPassword2;
    RadioGroup radioGroupSex;
    RadioButton radioButtonMale;
    RadioButton radioButtonFemale;
    Button buttonSave;
    String[] user_info;
    SharedPreferences sharedPreferences;
    String id;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        password = sharedPreferences.getString("password", "");

        editTextName = (EditText)findViewById(R.id.editTextEditName);
        editTextSurname = (EditText)findViewById(R.id.editTextEditSurname);
        editTextPasswordOld = (EditText)findViewById(R.id.editTextEditPasswordOld);
        editTextPassword1 = (EditText)findViewById(R.id.editTextEditPassword1);
        editTextPassword2 = (EditText)findViewById(R.id.editTextEditPassword2);
        radioGroupSex = (RadioGroup)findViewById(R.id.radioGroupSexEdit);
        radioButtonMale = (RadioButton)findViewById(R.id.radioMaleEdit);
        radioButtonFemale = (RadioButton)findViewById(R.id.radioFemaleEdit);
        buttonSave = (Button)findViewById(R.id.buttonEditAccount);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonEditAccount:
                if (editTextPasswordOld.equals(password) && editTextPassword1.equals(editTextPassword2)) {
                    String sex = "M";
                    if (radioButtonMale.isChecked()){
                        sex = "M";
                    } else {
                        sex = "W";
                    }
                    user_info = new String[]{editTextPassword1.getText().toString(), editTextName.getText().toString(), editTextSurname.getText().toString(), sex};
                    new UpdateUserInfoClass().execute();
                } else if (!editTextPasswordOld.equals(password)) {
                    editTextPasswordOld.setText("");
                    editTextPasswordOld.setHintTextColor(Color.RED);
                    editTextPasswordOld.setHint("Неверный пароль");
                } else if (!editTextPassword1.equals(editTextPassword2)) {
                    editTextPassword1.setText("");
                    editTextPassword1.setHintTextColor(Color.RED);
                    editTextPassword1.setHint("Пароли не совподают");
                    editTextPassword2.setText("");
                    editTextPassword2.setHintTextColor(Color.RED);
                    editTextPassword2.setHint("Пароли не совподают");
                }
                break;
        }
    }

    private class UpdateUserInfoClass extends AsyncTask<Void, Void, Integer> {

        HttpURLConnection httpURLConnection = null;

        protected Integer doInBackground(Void... params){

            try{
                Log.d("MyLogs", user_info[0] + " " + user_info[1] + " " + user_info[2] + " " + user_info[3]);
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/edit_profile.php?&id=" + id  +
                        "&password="
                        +user_info[0]
                        +"&name="
                        +user_info[1]
                        +"&surname="
                        +user_info[2]
                        +"&sex="
                        +user_info[3]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                Log.d("MyLogs", "Соединение прошло успешно");

                Integer res = httpURLConnection.getResponseCode();
                Log.i("chat", "+ ChatActivity - ответ сервера (200 - все ОК): "
                        + res.toString());

            } catch (Exception e){
                Log.d("MyLogs", "Ошибка соединения " + e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(Integer result) {

            try {

            } catch (Exception e) {
                Log.i("chat", "+ ChatActivity - ошибка передачи сообщения:\n"
                        + e.getMessage());
            }
        }
    }
}
