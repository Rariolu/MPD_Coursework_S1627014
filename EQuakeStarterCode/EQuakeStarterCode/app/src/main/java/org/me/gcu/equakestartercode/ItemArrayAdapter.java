//Sophie Coyne S1627014

package org.me.gcu.equakestartercode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;

public class ItemArrayAdapter extends ArrayAdapter<Item>
{
    private final Context thisContext;
    private final ArrayList<Item> items;
    private final SearchParams thisSearchParams;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ItemArrayAdapter(@NonNull Context context, ArrayList<Item> list, SearchParams searchParams)
    {
        super(context, 0, list);
        thisContext = context;
        Function<Item, Boolean> itemInRange = (Item item) ->
        {
            DateTime itemDate = item.GetDate();
            if (searchParams.UsingDataRange())
            {
                DateTime startDate = searchParams.GetStartDate();
                DateTime endDate = searchParams.GetEndDate();
                boolean minConstraint = itemDate.IsAfter(startDate) || itemDate.equals(startDate);
                boolean maxConstraint = !itemDate.IsAfter(endDate) || itemDate.equals(endDate);
                return minConstraint && maxConstraint;
            }
            else if (searchParams.UsingSpecificDate())
            {
                return itemDate.equals(searchParams.GetSpecificDate());
            }
            return true;
        };
        if (searchParams.Sort())
        {
            Comparator<Item> itemSort = (Item item1, Item item2) ->
            {
                Boolean item1InRange = itemInRange.apply(item1);
                Boolean item2InRange = itemInRange.apply(item2);
                if (item1InRange && item2InRange)
                {
                    switch (searchParams.GetSortType())
                    {
                        case STRONGEST_TO_WEAKEST:
                        {
                            return item1.GetMagnitude() > item2.GetMagnitude() ? -1 : 1;
                        }
                        case WEAKEST_TO_STRONGEST:
                        {
                            return item1.GetMagnitude() < item2.GetMagnitude() ? -1 : 1;
                        }
                        case MOST_RECENT_TO_LEAST_RECENT:
                        {
                            return item1.GetDate().IsAfter(item2.GetDate()) ? -1 : 1;
                        }
                        case LEAST_RECENT_TO_MOST_RECENT:
                        {
                            return item2.GetDate().IsAfter(item1.GetDate()) ? -1 : 1;
                        }
                        case NORTHMOST_TO_SOUTHMOST:
                        {
                            return item1.GetLatitude() > item2.GetLatitude() ? -1 : 1;
                        }
                        case SOUTHMOST_TO_NORTHMOST:
                        {
                            return item1.GetLatitude() < item2.GetLatitude() ? -1 : 1;
                        }
                        case EASTMOST_TO_WESTMOST:
                        {
                            return item1.GetLongitude() < item2.GetLongitude() ? -1 : 1;
                        }
                        case WESTMOST_TO_EASTMOST:
                        {
                            return item1.GetLongitude() > item2.GetLongitude() ? -1 : 1;
                        }
                        case DEEPEST_TO_SHALLOWEST:
                        {
                            return item1.GetDepth() > item2.GetDepth() ? -1 : 1;
                        }
                        case SHALLOWEST_TO_DEEPEST:
                        {
                            return item1.GetDepth() < item2.GetDepth() ? -1 : 1;
                        }
                        default:
                        {
                            Log.e("debug", "default ussed");
                            return 0;
                        }
                    }
                }
                else if (item1InRange)
                {
                    return -1;
                }
                return 1;
            };

            list.sort(itemSort);
            Log.e("debug","Sorting attempted");
        }
        else if (searchParams.UsingSpecificDate())
        {
            Comparator<Item> itemSort = (Item item1, Item item2) ->
            {
                DateTime specificDate = searchParams.GetSpecificDate();
                boolean equal = item1.GetDate().equals(item2.GetDate());
                if (equal)
                {
                    return 0;
                }
                return item1.GetDate().equals(specificDate) ? -1 : (item2.GetDate().equals(specificDate) ? 1 : 0);
            };
            list.sort(itemSort);
        }
        else if (searchParams.UsingDataRange())
        {
            Comparator<Item> itemSort = (Item item1, Item item2) ->
            {
                Boolean item1InRange = itemInRange.apply(item1);
                Boolean item2InRange = itemInRange.apply(item2);
                if (item1InRange && item2InRange)
                {
                    return 0;
                }
                else if (item1InRange)
                {
                    return -1;
                }
                return 1;
            };
            list.sort(itemSort);
        }

        items = list;
        thisSearchParams = searchParams;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
        {
            listItem = LayoutInflater.from(thisContext).inflate(R.layout.activity_list_item, parent, false);
        }

        TextView textView = (TextView)listItem.findViewById(R.id.text1);



        Item item = items.get(position);

        textView.setText(item.GetLocation()+": "+String.valueOf(item.GetMagnitude()));
        float magnitude = item.GetMagnitude();

        float magNorm = magnitude / 10.0f;
        int colorNum = R.color.white;

        if (magnitude  <=  1.0)
        {
            colorNum = Color.parseColor("#FFFF00");
            //colorNum = R.color.Yellow;
        }
        else if (magnitude <= 2.0)
        {
            colorNum = Color.parseColor("#FF0000");
        }
        else
        {
            colorNum = Color.parseColor("#8B0000");
            //colorNum = R.color.DarkRed;
        }

        textView.setBackgroundColor(colorNum);

        //textView.setBackgroundColor();

        textView.setOnClickListener((View v) ->
        {
            Intent intent = new Intent(thisContext, SpecificItemActivity.class);
            intent.putExtra("item", item);
            thisContext.startActivity(intent);
        });

        DateTime itemDate = item.GetDate();
        if (thisSearchParams.UsingSpecificDate())
        {
            DateTime date = thisSearchParams.GetSpecificDate();
            if (!itemDate.equals(date))
            {
                textView.setVisibility(View.GONE);
            }
        }
        else if (thisSearchParams.UsingDataRange())
        {
            DateTime startDate = thisSearchParams.GetStartDate();
            DateTime endDate = thisSearchParams.GetEndDate();
            boolean minConstraint = itemDate.IsAfter(startDate) || itemDate.equals(startDate);
            boolean maxConstraint = !itemDate.IsAfter(endDate) || itemDate.equals(endDate);
            if (!(minConstraint && maxConstraint))
            {
                textView.setVisibility(View.GONE);
            }
        }
        return textView;
    }
}