package com.technologies.tuch.tuch.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.technologies.tuch.tuch.R;

public class AboutActivity extends AppCompatActivity {

    TextView textViewAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

}
