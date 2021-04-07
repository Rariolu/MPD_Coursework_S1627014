//Robbie Coyne S1627014

package org.me.gcu.equakestartercode;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Pattern;

public class RSSXmlParse
{
    enum PrevElementType
    {
        NONE,
        CHANNEL,
        IMAGE,
        ITEM
    }

    public Channel Parse(String data)
    {
        Log.e("Debug","Gets to parse method");
        Channel channel = new Channel();
        ArrayList<Item> items = new ArrayList<Item>();
        try
        {

            Log.e("Debug","Got into try-catch");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            StringReader stringReader = new StringReader(data);

            if (stringReader == null)
            {
                Log.e("debug","WE HAVE A PROBLEM!!!");
            }

            xpp.setInput(stringReader);

            Log.e("debug","definitely not stringreader");

            int eventType = xpp.getEventType();

            Log.e("debug","definitely not eventType");



            Item newestItem = null;

            Stack<PrevElementType> prevTypes = new Stack<PrevElementType>();
            prevTypes.push(PrevElementType.NONE);

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                Log.e("debug",String.valueOf(eventType));

                //Log.e("debug","While loop started");
                PrevElementType prevType = prevTypes.peek();
                //Log.e("debug","peek worked");
                switch(eventType)
                {
                    case  XmlPullParser.START_DOCUMENT:
                    {
                        Log.e("debug","Start Document");
                        break;
                    }
                    case XmlPullParser.START_TAG:
                    {
                        String name = xpp.getName().toLowerCase();
                        Log.e("debug",name);
                        switch (name)
                        {
                            case "rss":
                            {
                                break;
                            }
                            case "channel":
                            {
                                prevTypes.push(PrevElementType.CHANNEL);
                                Log.e("debug","Got to channel open item");
                                break;
                            }
                            case "title":
                            {
                                Log.e("debug","title");
                                String title = xpp.nextText();
                                switch (prevType)
                                {
                                    case CHANNEL:
                                    {
                                        channel.SetTitle(title);
                                        break;
                                    }
                                    case ITEM:
                                    {
                                        if (newestItem != null)
                                        {
                                            newestItem.SetTitle(title);
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                            case "link":
                            {
                                String link = xpp.nextText();
                                switch (prevType)
                                {
                                    case CHANNEL:
                                    {
                                        channel.SetLink(link);
                                        break;
                                    }
                                    case ITEM:
                                    {
                                        if (newestItem != null)
                                        {
                                            newestItem.SetLink(link);
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                            case "description":
                            {
                                String description = xpp.nextText();
                                switch (prevType)
                                {
                                    case CHANNEL:
                                    {
                                        channel.SetDescription(description);
                                        break;
                                    }
                                    case ITEM:
                                    {
                                        if (newestItem != null)
                                        {
                                            newestItem.SetDescription(description);

                                            int lastColon = description.lastIndexOf(':');
                                            String magStr = description.substring(lastColon+1);
                                            float mag = Float.parseFloat(magStr);
                                            newestItem.SetMagnitude(mag);

                                            String locStartStr = "Location: ";
                                            String locEndStr = " ; Lat/long";
                                            int locationStart = description.indexOf(locStartStr)+locStartStr.length();
                                            Log.e("debug","locationStart: "+String.valueOf(locationStart));
                                            int locationEnd = description.indexOf(locEndStr);
                                            Log.e("debug", "locationEnd: "+String.valueOf(locationEnd));
                                            String location = description.substring(locationStart, locationEnd);
                                            Log.e("debug", "locationStart: "+String.valueOf(locationStart)+"; locationEnd: "+String.valueOf(locationEnd)+"; Location: "+location);
                                            newestItem.SetLocation(location);
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                            case "image":
                            {
                                prevTypes.push(PrevElementType.IMAGE);
                                break;
                            }
                            case "item":
                            {
                                prevTypes.push(PrevElementType.ITEM);
                                newestItem = new Item();
                                break;
                            }
                            case "pubdate":
                            {
                                String pubDate = xpp.nextText();
                                if (newestItem != null)
                                {
                                    newestItem.SetPublicationDateStr(pubDate);
                                }
                                break;
                            }
                            case "category":
                            {
                                String category = xpp.nextText();
                                if (newestItem != null)
                                {
                                    newestItem.SetCategory(category);
                                }
                                break;
                            }
                            case "geo:lat":
                            {
                                String strLat = xpp.nextText();
                                float latitude = Float.parseFloat(strLat);
                                //Integer latitude = Integer.parseInt(strLat);
                                if (newestItem != null)
                                {
                                    newestItem.SetLatitude(latitude);
                                }
                            }
                            case "geo:long":
                            {
                                String strLong = xpp.nextText();
                                float longitude = Float.parseFloat(strLong);
                                if (newestItem != null)
                                {
                                    newestItem.SetLongitude(longitude);
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:
                    {
                        String name = xpp.getName().toLowerCase();
                        switch (name)
                        {
                            case "item":
                            {
                                Log.e("debug","item close tag");
                                if (prevType == PrevElementType.ITEM)
                                {
                                    prevTypes.pop();
                                }
                                if (newestItem != null)
                                {
                                    items.add(newestItem);
                                    newestItem = null;
                                }
                                break;
                            }
                            case "image":
                            {
                                if (prevType == PrevElementType.IMAGE)
                                {
                                    prevTypes.pop();
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
                Log.e("debug","pre-eventType");
                eventType = xpp.next();
                Log.e("debug","post-eventType");
            }


        }
        catch (XmlPullParserException err)
        {

            Log.e("error",err.getMessage());
        }
        catch (IOException ae1)
        {
            Log.e("error",ae1.getMessage());
        }
        catch (Exception err)
        {
            Log.e("error", err.getMessage());
        }

        Log.e("debug","got to return line");
        Log.e("debug",String.valueOf(items.size()));
        channel.SetItems(items);
        return channel;
    }
}
