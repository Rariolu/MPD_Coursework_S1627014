import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

public class RSSXmlParse
{
    public void Parse(String data)
    {
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(data));

            int eventType = xpp.getEventType();
            

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch(eventType)
                {
                    case  XmlPullParser.START_DOCUMENT:
                    {
                        break;
                    }
                    case XmlPullParser.START_TAG:
                    {
                        break;;
                    }
                    case XmlPullParser.END_TAG:
                    {
                        break;
                    }
                    case XmlPullParser.TEXT:
                    {
                        break;
                    }
                }
            }

        }
        catch (XmlPullParserException err)
        {

        }

    }
}
