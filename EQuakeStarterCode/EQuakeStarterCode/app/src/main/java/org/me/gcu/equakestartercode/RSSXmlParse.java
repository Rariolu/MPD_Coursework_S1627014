//Sophie Coyne S1627014

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
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            StringReader stringReader = new StringReader(data);

            xpp.setInput(stringReader);

            int eventType = xpp.getEventType();

            Item newestItem = null;

            Stack<PrevElementType> prevTypes = new Stack<PrevElementType>();
            prevTypes.push(PrevElementType.NONE);

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                PrevElementType prevType = prevTypes.peek();
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
                        switch (name)
                        {
                            case "rss":
                            {
                                break;
                            }
                            case "channel":
                            {
                                prevTypes.push(PrevElementType.CHANNEL);
                                break;
                            }
                            case "title":
                            {
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
                                            int locationEnd = description.indexOf(locEndStr);
                                            String location = description.substring(locationStart, locationEnd);
                                            newestItem.SetLocation(location);

                                            String depthStartStr = "Depth: ";
                                            String depthEndStr = " km ; Magnitude";
                                            int depthStart = description.indexOf(depthStartStr)+depthStartStr.length();
                                            int depthEnd = description.indexOf(depthEndStr);
                                            String depthStr = description.substring(depthStart, depthEnd);
                                            int depth = Integer.parseInt(depthStr);
                                            newestItem.SetDepth(depth);
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

                                    String dayStr = pubDate.substring(5, 7);

                                    String monthStr = pubDate.substring(8, 11).toLowerCase();

                                    String yearStr = pubDate.substring(12, 16);

                                    DateTime date = new DateTime(yearStr, monthStr, dayStr);

                                    newestItem.SetDate(date);

                                    Log.e("debug","date: "+date.toString());

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
                            case "lat":
                            {
                                Log.e("debug","Latitude parse");
                                String strLat = xpp.nextText();
                                float latitude = Float.parseFloat(strLat);
                                //Integer latitude = Integer.parseInt(strLat);
                                if (newestItem != null)
                                {
                                    newestItem.SetLatitude(latitude);
                                }
                                break;
                            }
                            case "long":
                            {
                                Log.e("debug","Longitude parse");
                                String strLong = xpp.nextText();
                                float longitude = Float.parseFloat(strLong);
                                if (newestItem != null)
                                {
                                    newestItem.SetLongitude(longitude);
                                }
                                break;
                            }
                            default:
                            {
                                Log.e("debug", "Unrecognised tag: "+name+";");
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
                eventType = xpp.next();
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
