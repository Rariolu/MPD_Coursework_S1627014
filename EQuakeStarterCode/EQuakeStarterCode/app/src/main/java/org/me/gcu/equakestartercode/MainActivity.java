//Sophie Coyne S1627014

package org.me.gcu.equakestartercode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.me.gcu.equakestartercode.SearchParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private Button btnSearchParams;
    private Button btnOpenMap;
    private ListView lstItems;
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private SearchParams prevSearchParams = new SearchParams();

    private Channel prevChannel;

    final static int REQUEST_SEARCHPARAMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");

        btnSearchParams = (Button)findViewById(R.id.btnSearchParams);
        btnSearchParams.setOnClickListener((View v) ->
        {
            Intent intent = new Intent(MainActivity.this, SearchParameterActivity.class);
            intent.putExtra("searchparams", prevSearchParams);
            startActivityForResult(intent, REQUEST_SEARCHPARAMS);
        });

        btnOpenMap = (Button)findViewById(R.id.btnOpenMap);
        btnOpenMap.setOnClickListener((View v) ->
        {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putExtra("channel", prevChannel);
            startActivity(intent);
        });

        lstItems = (ListView)findViewById(R.id.lstItems);

        new Thread(new Task(urlSource, new SearchParams())).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQUEST_SEARCHPARAMS:
            {
                if (resultCode == RESULT_OK)
                {
                    SearchParams searchParams = (SearchParams) data.getSerializableExtra("searchparams");
                    prevSearchParams = searchParams;
                    new Thread(new Task(urlSource, searchParams)).start();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to close the app?");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes",(DialogInterface dialog, int id) ->
        {
            this.finishAffinity();
        });
        alert.setNegativeButton("No", null);
        alert.show();
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;
        private SearchParams thisSearchParams;

        public Task(String _url, SearchParams searchParams)
        {
            url = _url;
            thisSearchParams = searchParams;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run()
        {
            URL urlInstance;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";

            try
            {
                urlInstance = new URL(url);
                yc = urlInstance.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            Channel channel = new RSSXmlParse().Parse(result);
            prevChannel = channel;

            MainActivity.this.runOnUiThread(() ->
            {
                Log.d("UI thread", "I am the UI thread");

                ArrayList<Item> items = channel.GetItems();

                String output = "";

                Log.e("debug", "gets to ui thread");

                ItemArrayAdapter adapter = new ItemArrayAdapter(MainActivity.this, items, thisSearchParams);

                lstItems.setAdapter(adapter);

                //Set the height of the listview to whatever it needs to be
                Utility.setListViewHeightBasedOnChildren(lstItems);
            });
        }
    }
}