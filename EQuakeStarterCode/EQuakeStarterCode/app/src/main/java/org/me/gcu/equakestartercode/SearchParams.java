//Sophie Coyne S1627014

package org.me.gcu.equakestartercode;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;



public class SearchParams implements Serializable
{
    public enum SortType
    {
        STRONGEST_TO_WEAKEST,
        WEAKEST_TO_STRONGEST,
        MOST_RECENT_TO_LEAST_RECENT,
        LEAST_RECENT_TO_MOST_RECENT,
        NORTHMOST_TO_SOUTHMOST,
        SOUTHMOST_TO_NORTHMOST,
        EASTMOST_TO_WESTMOST,
        WESTMOST_TO_EASTMOST,
        DEEPEST_TO_SHALLOWEST,
        SHALLOWEST_TO_DEEPEST
    }

    boolean usingDateRange = false;
    public boolean UsingDataRange()
    {
        return usingDateRange;
    }

    boolean usingSpecificDate = false;
    public boolean UsingSpecificDate()
    {
        return usingSpecificDate;
    }

    DateTime specificDate;
    public DateTime GetSpecificDate()
    {
        return specificDate;
    }
    public void SetSpecificDate(DateTime date)
    {
        specificDate = date;
        usingSpecificDate = true;
    }

    DateTime startDate;
    public DateTime GetStartDate()
    {
        return startDate;
    }

    DateTime endDate;
    public DateTime GetEndDate()
    {
        return endDate;
    }

    boolean sort;
    public boolean Sort()
    {
        return sort;
    }
    public void SetSorting(boolean _sort)
    {
        sort = _sort;
    }

    SortType sortType = SortType.STRONGEST_TO_WEAKEST;
    public SortType GetSortType()
    {
        return sortType;
    }
    public void SetSortType(SortType type)
    {
        sortType = type;
    }

    public SearchParams(DateTime start, DateTime end)
    {
        usingDateRange = true;
        startDate = start;
        endDate = end;
    }
    public SearchParams()
    {

    }
}