package org.me.gcu.equakestartercode;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MapActivity extends AppCompatActivity
{
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Channel channel = (Channel)getIntent().getSerializableExtra("channel");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.FullMap);
        mapFragment.getMapAsync((GoogleMap googleMap) ->
        {
            mMap = googleMap;

            for(Item item : channel.GetItems())
            {
                LatLng itemPos = new LatLng(item.GetLatitude(),item.GetLongitude());
                mMap.addMarker(new MarkerOptions().position(itemPos).title(item.GetLocation()+": "+String.valueOf(item.GetMagnitude())));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(itemPos));
            }

        });
    }
}