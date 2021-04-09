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
import android.widget.TextView;

import org.w3c.dom.Text;

public class SpecificItemActivity extends AppCompatActivity
{
    private GoogleMap mMap;
    TextView lblTitle;
    TextView lblCoordinates;
    TextView lblMagnitude;
    TextView lblDepth;
    TextView lblDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_item);

        lblTitle = (TextView)findViewById(R.id.lblTitle);
        lblCoordinates = (TextView)findViewById(R.id.lblCoordinates);
        lblMagnitude = (TextView)findViewById(R.id.lblMagnitude);
        lblDepth = (TextView)findViewById(R.id.lblDepth);
        lblDate = (TextView)findViewById(R.id.lblDate);

        Item thisItem = (Item)getIntent().getSerializableExtra("item");

        lblTitle.setText(thisItem.GetLocation());
        lblCoordinates.setText("Latitude: "+String.valueOf(thisItem.GetLatitude())+"; Longitude: "+String.valueOf(thisItem.GetLongitude()));
        lblMagnitude.setText("Magnitude: "+String.valueOf(thisItem.GetMagnitude()));
        lblDepth.setText("Depth: "+String.valueOf(thisItem.GetDepth())+"km");
        lblDate.setText("Date: "+String.valueOf(thisItem.GetPublicationDate()));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((GoogleMap googleMap) ->
        {
            mMap = googleMap;

            LatLng itemPos = new LatLng(thisItem.GetLatitude(),thisItem.GetLongitude());
            mMap.addMarker(new MarkerOptions().position(itemPos).title(thisItem.GetLocation()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(itemPos));
        });
    }
}