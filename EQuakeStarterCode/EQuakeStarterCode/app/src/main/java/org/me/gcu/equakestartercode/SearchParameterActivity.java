//Sophie Coyne S1627014

package org.me.gcu.equakestartercode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;

import org.me.gcu.equakestartercode.DateTime;
import org.me.gcu.equakestartercode.SearchParams;

import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

public class SearchParameterActivity extends AppCompatActivity
{
    CheckBox cbSpecificDate;
    DatePicker dpSpecificDate;
    CheckBox cbSetDateRange;
    DatePicker dpStartDate;
    DatePicker dpEndDate;
    CheckBox cbSort;

    Button btnApply;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_parameter);

        cbSpecificDate = (CheckBox)findViewById(R.id.cbUseSpecificDate);
        dpSpecificDate = (DatePicker)findViewById(R.id.dpSpecificDate);
        cbSetDateRange = (CheckBox)findViewById(R.id.cbSetDateRange);
        dpStartDate = (DatePicker)findViewById(R.id.dpStartDate);
        dpEndDate = (DatePicker)findViewById(R.id.dpEndDate);
        btnApply = (Button)findViewById(R.id.btnApply);
        cbSort = (CheckBox)findViewById(R.id.cbSort);

        HashMap<SearchParams.SortType, RadioButton> radioButtons = new HashMap<>();
        radioButtons.put(SearchParams.SortType.MOST_RECENT_TO_LEAST_RECENT, (RadioButton)findViewById(R.id.rbMostRecentToLeast));
        radioButtons.put(SearchParams.SortType.LEAST_RECENT_TO_MOST_RECENT, (RadioButton)findViewById(R.id.rbLeastRecentToMost));
        radioButtons.put(SearchParams.SortType.STRONGEST_TO_WEAKEST, (RadioButton)findViewById(R.id.rbStrongestToWeakest));
        radioButtons.put(SearchParams.SortType.WEAKEST_TO_STRONGEST, (RadioButton)findViewById(R.id.rbWeakestToStrongest));
        radioButtons.put(SearchParams.SortType.NORTHMOST_TO_SOUTHMOST, (RadioButton)findViewById(R.id.rbNorthmostToSouthmost));
        radioButtons.put(SearchParams.SortType.SOUTHMOST_TO_NORTHMOST, (RadioButton)findViewById(R.id.rbSouthmostToNorthmost));
        radioButtons.put(SearchParams.SortType.EASTMOST_TO_WESTMOST, (RadioButton)findViewById(R.id.rbEastmostToWestmost));
        radioButtons.put(SearchParams.SortType.WESTMOST_TO_EASTMOST, (RadioButton)findViewById(R.id.rbWestmostToEastmost));
        radioButtons.put(SearchParams.SortType.DEEPEST_TO_SHALLOWEST, (RadioButton)findViewById(R.id.rbDeepestToShallowest));
        radioButtons.put(SearchParams.SortType.SHALLOWEST_TO_DEEPEST, (RadioButton)findViewById(R.id.rbShallowestToDeepest));

        dpStartDate.setEnabled(false);
        dpEndDate.setEnabled(false);
        dpStartDate.setClickable(false);
        dpEndDate.setClickable(false);

        SearchParams prevSearchParams = (SearchParams)getIntent().getSerializableExtra("searchparams");
        if (prevSearchParams.UsingSpecificDate())
        {
            cbSpecificDate.setChecked(true);
            dpSpecificDate.setEnabled(true);

            DateTime date = prevSearchParams.GetSpecificDate();
            dpSpecificDate.updateDate(date.GetYear(), date.GetMonth()-1, date.GetDay());
        }
        else if (prevSearchParams.UsingDataRange())
        {
            cbSetDateRange.setChecked(true);
            dpStartDate.setEnabled(true);
            dpEndDate.setEnabled(true);

            DateTime startDate = prevSearchParams.GetStartDate();
            dpStartDate.updateDate(startDate.GetYear(), startDate.GetMonth()-1, startDate.GetDay());

            DateTime endDate = prevSearchParams.GetEndDate();
            dpEndDate.updateDate(endDate.GetYear(), endDate.GetMonth()-1, endDate.GetDay());
        }
        else
        {
            dpStartDate.setEnabled(false);
            dpEndDate.setEnabled(false);
            dpSpecificDate.setEnabled(false);
        }

        if (prevSearchParams.Sort())
        {
            cbSort.setChecked(true);
            radioButtons.get(prevSearchParams.GetSortType()).setChecked(true);
        }
        else
        {
            radioButtons.get(SearchParams.SortType.MOST_RECENT_TO_LEAST_RECENT).setChecked(true);
            for(SearchParams.SortType key : radioButtons.keySet())
            {
                radioButtons.get(key).setEnabled(false);
            }
        }

        cbSpecificDate.setOnClickListener((View v) ->
        {
            boolean checked = cbSpecificDate.isChecked();
            dpSpecificDate.setEnabled(checked);
            if (checked)
            {
                dpStartDate.setEnabled(false);
                dpEndDate.setEnabled(false);
                cbSetDateRange.setChecked(false);
            }
        });

        cbSetDateRange.setOnClickListener((View v) ->
        {
            boolean checked = cbSetDateRange.isChecked();
            dpStartDate.setEnabled(checked);
            dpEndDate.setEnabled(checked);

            if (checked)
            {
                dpSpecificDate.setEnabled(false);
                cbSpecificDate.setChecked(false);
            }
        });

        cbSort.setOnClickListener((View v) ->
        {
            boolean checked = cbSort.isChecked();
            for(SearchParams.SortType key : radioButtons.keySet())
            {
                radioButtons.get(key).setEnabled(checked);
            }
        });

        btnApply.setOnClickListener((View v) ->
        {
            Intent intent = new Intent();
            SearchParams searchParams = new SearchParams();
            if (cbSpecificDate.isChecked())
            {
                DateTime date = new DateTime(dpSpecificDate.getYear(), dpSpecificDate.getMonth()+1, dpSpecificDate.getDayOfMonth());
                searchParams.SetSpecificDate(date);
            }
            else if (cbSetDateRange.isChecked())
            {
                DateTime startDate = new DateTime(dpStartDate.getYear(), dpStartDate.getMonth()+1, dpStartDate.getDayOfMonth());

                DateTime endDate = new DateTime(dpEndDate.getYear(), dpEndDate.getMonth()+1, dpEndDate.getDayOfMonth());

                searchParams = new SearchParams(startDate, endDate);

            }
            searchParams.SetSorting(cbSort.isChecked());
            if (cbSort.isChecked())
            {
                for(SearchParams.SortType key : radioButtons.keySet())
                {
                    if (radioButtons.get(key).isChecked())
                    {
                        searchParams.SetSortType(key);
                        break;
                    }
                }
            }
            intent.putExtra("searchparams", searchParams);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to go back without applying search parameters?");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes",(DialogInterface dialog, int id) ->
        {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });
        alert.setNegativeButton("No", null);
        alert.show();
    }
}