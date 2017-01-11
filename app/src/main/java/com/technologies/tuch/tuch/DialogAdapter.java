package com.technologies.tuch.tuch;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogAdapter extends ArrayAdapter<DialogItem> {

    ArrayList<DialogItem> data;// = new ArrayList<DialogItem>();
    Context context;
    SharedPreferences sharedPreferences;
    String id;

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public DialogAdapter(String my_id ,Context context, ArrayList<DialogItem> arr) {
        super(context, Integer.valueOf(my_id), arr);
        if (arr != null) {
            data = arr;
        }
        this.id = my_id;
        this.context = context;
    }

    /*@Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int num) {
        return data.get(num);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }*/

    @Override
    public View getView(int i, View someView, ViewGroup arg2) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (someView == null) {
            if (data.get(i).author_id.equals(id)){
                someView = inflater.inflate(R.layout.dialog_list_item_output_message, null);
            } else {
                someView = inflater.inflate(R.layout.dialog_list_item_input_message, null);
            }
        }
            TextView textViewText = (TextView)someView.findViewById(R.id.textViewDialogMessageText);
            TextView textViewTime = (TextView)someView.findViewById(R.id.textViewDialogMessageTimeDate);
        textViewText.setText(data.get(i).text);
        textViewTime.setText(data.get(i).time);
        return someView;
    }


}
