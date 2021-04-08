package org.me.gcu.equakestartercode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemArrayAdapter extends ArrayAdapter<Item>
{
    private final Context thisContext;
    private final ArrayList<Item> items;
    public ItemArrayAdapter(@NonNull Context context, ArrayList<Item> list)
    {
        super(context, 0, list);
        thisContext = context;
        items = list;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
        {
            listItem = LayoutInflater.from(thisContext).inflate(R.layout.activity_list_item, parent, false);
        }

        TextView textView = (TextView)listItem.findViewById(R.id.text1);



        Item item = items.get(position);

        textView.setText(item.GetLocation());
        float magnitude = item.GetMagnitude();

        float magNorm = magnitude / 10.0f;
        int colorNum = R.color.white;

        if (magnitude  <=  1.0)
        {
            colorNum = Color.parseColor("#FFFF00");
            //colorNum = R.color.Yellow;
        }
        else if (magnitude <= 2.0)
        {
            colorNum = Color.parseColor("#FF0000");
        }
        else
        {
            colorNum = Color.parseColor("#8B0000");
            //colorNum = R.color.DarkRed;
        }

        Log.e("debug","magNorm: "+String.valueOf(magNorm)+"; colorNum: "+String.valueOf(colorNum));

        textView.setBackgroundColor(colorNum);

        //textView.setBackgroundColor();

        textView.setOnClickListener((View v) ->
        {
            Intent intent = new Intent(thisContext, SpecificItemActivity.class);
            intent.putExtra("item", item);
            thisContext.startActivity(intent);
        });

        return textView;
    }
}