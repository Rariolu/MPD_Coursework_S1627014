//Robbie Coyne S1627014

package org.me.gcu.equakestartercode;

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

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    //private TextView rawDataDisplay;
    private Button startButton;
    private ListView lstItems;
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");

        //startButton = (Button)findViewById(R.id.startButton);
        lstItems = (ListView)findViewById(R.id.lstItems);

        //startButton.setOnClickListener(this);
        new Thread(new Task(urlSource)).start();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String _url)
        {
            url = _url;
        }

        @Override
        public void run()
        {
            URL urlInstance;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            String result = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                urlInstance = new URL(url);
                yc = urlInstance.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");

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

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Log.d("UI thread", "I am the UI thread");

                    ArrayList<Item> items = channel.GetItems();

                    String output = "";

                    Log.e("debug","gets to ui thread");

                    ArrayList<String> itemStr = new ArrayList<>();

                    for (Item item : items)
                    {
                        itemStr.add(item.GetLocation());
                    }

                    Log.e("debug", String.valueOf(itemStr.size()));

                    ItemArrayAdapter adapter = new ItemArrayAdapter(MainActivity.this, items);

                    lstItems.setAdapter(adapter);

                    //Set the height of the listview to whatever it needs to be
                    Utility.setListViewHeightBasedOnChildren(lstItems);
                }
            });
        }

    }
}

