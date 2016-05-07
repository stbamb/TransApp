package com.strans.transapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.strans.transapp.R;
import com.strans.transapp.adapters.JourneyResultAdapter;
import com.strans.transapp.adapters.JourneyResultAdapter.GroupItem;
import com.strans.transapp.dao.concrete.JourneyDAO;
import com.strans.transapp.model.City;
import com.strans.transapp.widget.AnimatedExpandableListView;

 import java.util.ArrayList;
 import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private String id;
    private String date;
    private String from;
    private String to;
    private String userID;
    private AnimatedExpandableListView listView;
    private JourneyResultAdapter adapter;
    private ArrayList<City> cities;

    /**
     * This function comes with the class Activity itself, it takes care
     * of initializing the variables needed to execute the activity.
     *
     * @param savedInstanceState    Needed by Android itself. It can also be used to retrieved
     *                              the arguments passed to this activity if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = (String) getIntent().getExtras().get("id");
        date = (String) getIntent().getExtras().get("date");
        from = (String) getIntent().getExtras().get("from");
        to = (String) getIntent().getExtras().get("to");
        userID = (String) getIntent().getExtras().get("userID");
        cities = (ArrayList<City>) getIntent().getExtras().get("cities");

        JourneyDAO journeyDAO = new JourneyDAO(this, id, date, from, to);
        journeyDAO.execute();
    }

    /**
     * This function takes the list retrieved from the database
     * and passes it to the adapter so the UI can get updated.
     *
     * @param   items   This is the list of items retrieved from the database via a DAO class.
     */
    public void updateList(List<GroupItem> items)
    {
        int length = items.size();
        if (length > 0) {
            adapter = new JourneyResultAdapter(this, cities, userID, this);
            adapter.setData(items);
            listView = (AnimatedExpandableListView) findViewById(R.id.animatedJourneyResultELV);
            listView.setVerticalFadingEdgeEnabled(true);
            listView.setAdapter(adapter);
        }
        else
            openErrorDialog();
    }

    /**
     * This function handles the different possibilities for the 'Options Menu'.
     *
     * @param item  This is the menu itself.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function is called when no journeys were found for the given criteria.
     */
    private void openErrorDialog()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("No matches found");
        alertDialog.setMessage("No journeys matched the given criteria");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
                finish();
            }
        });
        alertDialog.show();
    }

    /**
     * This function is called once the journey has been successfully purchased.
     */
    public void openSuccessDialog()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Journey successfully purchased");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
}
