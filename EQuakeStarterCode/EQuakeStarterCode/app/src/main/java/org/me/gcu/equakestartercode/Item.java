//Robbie Coyne S1627014

package org.me.gcu.equakestartercode;

public class Item
{
    String title;
    public void SetTitle(String _title)
    {
        title = _title;
    }

    String description;
    public String GetDescription()
    {
        return description;
    }
    public void SetDescription(String _description)
    {
        description = _description;
    }

    String link;
    public void SetLink(String _link)
    {
        link = _link;
    }

    String publicationDateStr;
    public void SetPublicationDateStr(String _publicationDateStr)
    {
        publicationDateStr = _publicationDateStr;
    }

    String category;
    public void SetCategory(String _category)
    {
        category = _category;
    }

    float latitude;
    public void SetLatitude(float _latitude)
    {
        latitude = _latitude;
    }

    float longitude;
    public void SetLongitude(float _longitude)
    {
        longitude = _longitude;
    }

    public Item()
    {

    }
}
