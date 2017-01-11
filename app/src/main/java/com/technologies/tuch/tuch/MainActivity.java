package com.technologies.tuch.tuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String name = "";
    String surname = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        surname = sharedPreferences.getString("surname", "");
        //Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
        //startActivity(intent);
        if (!name.equals("") && !surname.equals("")) {
            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

}
