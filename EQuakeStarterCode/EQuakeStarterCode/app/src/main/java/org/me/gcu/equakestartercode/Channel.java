//Sophie Coyne S1627014

package org.me.gcu.equakestartercode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;


public class Channel implements Serializable
{
    String title;
    public void SetTitle(String _title)
    {
        title = _title;
    }

    String link;
    public void SetLink(String _link)
    {
        link = _link;
    }

    ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Item> GetItems()
    {
        return items;
    }
    public void SetItems(ArrayList<Item> _items)
    {
        items = _items;
    }

    String description;
    public void SetDescription(String _description)
    {
        description = _description;
    }

    public Channel()
    {

    }
}
