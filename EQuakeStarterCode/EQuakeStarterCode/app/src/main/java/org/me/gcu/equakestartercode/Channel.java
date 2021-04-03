//Robbie Coyne S1627014

package org.me.gcu.equakestartercode;

import java.util.LinkedList;


public class Channel
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

    LinkedList<Item> items = new LinkedList<>();
    public LinkedList<Item> GetItems()
    {
        return items;
    }
    public void SetItems(LinkedList<Item> _items)
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
