package org.me.gcu.equakestartercode;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class SpecificItemActivity extends AppCompatActivity
{
    TextView lblTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_item);

        lblTitle = (TextView)findViewById(R.id.lblTitle);


        Item thisItem = (Item)getIntent().getSerializableExtra("item");
        lblTitle.setText(thisItem.GetTitle());

    }
}