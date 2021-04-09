package org.me.gcu.equakestartercode;

import android.util.Log;

import java.io.Serializable;
import java.util.Objects;

public class DateTime implements Serializable
{
    int year;
    public int GetYear()
    {
        return year;
    }

    int month;
    public int GetMonth()
    {
        return month;
    }

    int day;
    public int GetDay()
    {
        return day;
    }

    public DateTime(int y, int m, int d)
    {
        year = y;
        month = m;
        day = d;
    }

    public DateTime(String yearStr, String monthStr, String dayStr)
    {
        String[] months = new String[]
        {
                "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"
        };
        day = Integer.parseInt(dayStr);
        month = Utility.GetIndexOfElement(months, monthStr.toLowerCase()) + 1;
        if (month == 0)
        {
            Log.e("debug","monthStr: "+monthStr);
        }
        year = Integer.parseInt(yearStr);
    }

    public boolean IsAfter(DateTime otherDate)
    {
        if (GetYear() > otherDate.GetYear())
        {
            return  true;
        }
        else if (GetYear() < otherDate.GetYear())
        {
            return false;
        }

        if (GetMonth() > otherDate.GetMonth())
        {
            return true;
        }
        else if (GetMonth() < otherDate.GetMonth())
        {
            return  false;
        }

        if (GetDay() > otherDate.GetDay())
        {
            return true;
        }
        else if (GetDay() < otherDate.GetDay())
        {
            return  false;
        }

        return false;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        DateTime dateTime = (DateTime) o;
        return year == dateTime.year &&
                month == dateTime.month &&
                day == dateTime.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString()
    {
        return "Year: "+String.valueOf(GetYear())+"; Month: "+String.valueOf(GetMonth())+"; Day: "+String.valueOf(GetDay())+";";
    }
}
