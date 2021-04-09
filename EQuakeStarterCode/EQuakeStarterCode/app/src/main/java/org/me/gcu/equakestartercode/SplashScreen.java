//Sophie Coyne S1627014

package org.me.gcu.equakestartercode;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity
{
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener((View view) ->
        {
            SwitchActivities();
        });

    }

    private void SwitchActivities()
    {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }
}