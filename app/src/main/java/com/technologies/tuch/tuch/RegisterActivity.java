package com.technologies.tuch.tuch;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.technologies.tuch.tuch.SQLite.SQLiteCreater;

import java.net.HttpURLConnection;
import java.net.URL;


public class RegisterActivity extends AppCompatActivity implements OnClickListener{

    final String SERVER_NAME="http://sdyusshor1novoch.ru";
    final String password = "dsobgigsblsd934n398gdjm349tgwle5dh3ngdfs9g34nirf234342refe";
    ImageView imageViewAvatar;
    EditText editTextName;
    EditText editTextSurname;
    EditText editTextPhone;
    EditText editTextPassword1;
    EditText editTextPassword2;
    TextView textViewSex;
    RadioGroup radioGroupSex;
    RadioButton radioMale;
    RadioButton radioFemale;
    Button buttonRegister;
    String[] user_info = {};
    SQLiteCreater sqLiteCreater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageViewAvatar = (ImageView)findViewById(R.id.imageViewAvatar);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextSurname = (EditText)findViewById(R.id.editTextSurname);
        editTextPhone = (EditText)findViewById(R.id.editTextPhone);
        editTextPassword1 = (EditText)findViewById(R.id.editTextPassword1);
        editTextPassword2 = (EditText)findViewById(R.id.editTextPassword2);
        textViewSex = (TextView)findViewById(R.id.textViewSex);
        radioGroupSex = (RadioGroup)findViewById(R.id.radioGroupSex);
        radioMale = (RadioButton)findViewById(R.id.radioMale);
        radioFemale = (RadioButton)findViewById(R.id.radioFemale);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);

        imageViewAvatar.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        sqLiteCreater = new SQLiteCreater(this, "Database", null, 1);
    }

    @Override
        public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.imageViewAvatar:
                break;
            case R.id.buttonRegister:
                registerUser();
                break;
        }
    }

    public void registerUser() {

        final String PASSWORD_ERROR = "Пароли не совпадают";
        final String PASSWORD_ERROR_SMALL = "Пароль слишком короткий";

        if (!editTextPhone.getText().toString().equals("") && /*editTextPhone.getText().toString().length() == 11 &&*/ !editTextName.getText().toString().equals("") && !editTextSurname.getText().toString().equals("") && editTextPassword1.getText().toString().equals(editTextPassword2.getText().toString()) && !(editTextPassword1.getText().length() < 8)) {
            String sex = "M";
            if (radioMale.isChecked()){
                sex = "M";
            } else {
                sex = "W";
            }
            user_info = new String[]{editTextPhone.getText().toString(), editTextPassword1.getText().toString(), editTextName.getText().toString(), editTextSurname.getText().toString(), sex};
            new RegisterUserClass().execute();
            Log.d("MyLog", "Стабильно");
        } else if (editTextName.getText().toString().equals("")) {
            editTextName.setHintTextColor(Color.RED);
        } else if (editTextSurname.getText().toString().equals("")) {
            editTextSurname.setHintTextColor(Color.RED);
        } else if (!editTextPassword1.getText().toString().equals(editTextPassword2.getText().toString())) {
            editTextPassword1.setText("");
            editTextPassword2.setText("");
            editTextPassword1.setHintTextColor(Color.RED);
            editTextPassword1.setHint(PASSWORD_ERROR);
            editTextPassword2.setHintTextColor(Color.RED);
            editTextPassword2.setHint(PASSWORD_ERROR);
        } else if ((editTextPassword1.getText().length() == 0) && (editTextPassword2.getText().length() == 0)) {
            editTextPassword1.setHintTextColor(Color.RED);
            editTextPassword2.setHintTextColor(Color.RED);
            editTextPassword1.setHint("Введите пароль");
            editTextPassword2.setHint("Введите пароль");
        } else if ((editTextPassword1.getText().length() < 8) && (editTextPassword2.getText().length() < 8)) {
            editTextPassword1.setText("");
            editTextPassword2.setText("");
            editTextPassword1.setHintTextColor(Color.RED);
            editTextPassword2.setHintTextColor(Color.RED);
            editTextPassword1.setHint(PASSWORD_ERROR_SMALL);
            editTextPassword2.setHint(PASSWORD_ERROR_SMALL);
        }
    }


    private class RegisterUserClass extends AsyncTask<Void, Void, Integer> {

        HttpURLConnection httpURLConnection = null;

        protected Integer doInBackground(Void... params){

            try{
                Log.d("MyLogs", user_info[0]+" "+user_info[1]+" "+user_info[2]+" "+ user_info[3]+" "+user_info[4]);
                URL url = new URL("http://sdyusshor1novoch.ru/tuch/register.php?phone="
                        +user_info[0]
                        +"&password="
                        +AES.encrypt(user_info[1], password)
                        +"&name="
                        +AES.encrypt(user_info[2], password)
                        +"&surname="
                        +AES.encrypt(user_info[3], password)
                        +"&sex="
                        +user_info[4]);
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
            userLogin();
        }
    }

    public void userLogin() {
        Intent intent = new Intent(RegisterActivity.this, MessageActivity.class);
        startActivity(intent);
    }

}
