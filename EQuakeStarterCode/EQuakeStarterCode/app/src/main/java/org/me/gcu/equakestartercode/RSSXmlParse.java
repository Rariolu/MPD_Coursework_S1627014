//Robbie Coyne S1627014

package org.me.gcu.equakestartercode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Stack;

public class RSSXmlParse
{
    enum PrevElementType
    {
        CHANNEL,
        IMAGE,
        ITEM
    }

    public Channel Parse(String data)
    {
        Channel channel = new Channel();
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(data));

            int eventType = xpp.getEventType();

            LinkedList<Item> items = new LinkedList<Item>();
            Item newestItem = null;

            Stack<PrevElementType> prevTypes = new Stack<PrevElementType>();

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                PrevElementType prevType = prevTypes.peek();
                switch(eventType)
                {
                    case  XmlPullParser.START_DOCUMENT:
                    {
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
            }

            channel.SetItems(items);
        }
        catch (XmlPullParserException err)
        {

        }
        catch (IOException ae1)
        {

        }
        return channel;
    }
}
