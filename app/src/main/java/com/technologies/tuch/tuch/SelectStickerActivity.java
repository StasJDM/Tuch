package com.technologies.tuch.tuch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class SelectStickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sticker);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        
    }

    private ArrayList<Integer> getDataSet(int c) {
        ArrayList<Integer> arrayListStickers = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            arrayListStickers.add(R.drawable.s101);
        }
        return arrayListStickers;
    }
}
