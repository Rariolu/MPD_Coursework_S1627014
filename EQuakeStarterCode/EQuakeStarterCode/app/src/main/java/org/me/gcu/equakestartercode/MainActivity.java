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
    private String result="";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
        //rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        lstItems = (ListView)findViewById(R.id.lstItems);

        startButton.setOnClickListener(this);
        Log.e("MyTag","after startButton");
        // More Code goes here
    }

    public void onClick(View aview)
    {
        Log.e("MyTag","in onClick");
        startProgress();
        Log.e("MyTag","after startProgress");
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

        public Task(String aurl)
        {
            url = aurl;
        }

        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
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

                    LinkedList<Item> items = channel.GetItems();

                    String output = "";

                    Log.e("debug","gets to ui thread");

                    ArrayList<String> itemStr = new ArrayList<>();

                    for (Item item : items)
                    {
                        itemStr.add(item.toString());
                    }

                    Log.e("debug", String.valueOf(itemStr.size()));

                    ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, itemStr);

                    lstItems.setAdapter(adapter);

                    Utility.setListViewHeightBasedOnChildren(lstItems);

                    /*for (int i = 0; i <items.size(); i++)
                    {
                        Item item = items.get(i);
                        //output += "Item Category: "+item.GetCategory();
                        output += "Item Description: "+item.GetDescription();
                    }*/

                    //rawDataDisplay.setText(output);
                }
            });
        }

    }
}

