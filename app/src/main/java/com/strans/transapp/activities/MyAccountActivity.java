package com.strans.transapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.strans.transapp.R;
import com.strans.transapp.adapters.PurchasesAdapter;
import com.strans.transapp.dao.concrete.TripDAO;
import com.strans.transapp.model.City;
import com.strans.transapp.adapters.PurchasesAdapter.GroupItem;
import com.strans.transapp.widget.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {

    private AnimatedExpandableListView listView;
    private PurchasesAdapter adapter;
    private String userID;
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
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userID = (String) getIntent().getExtras().get("userID");
        cities = (ArrayList<City>) getIntent().getExtras().get("cities");
        TripDAO tripDAO = new TripDAO(userID, cities, this);
        tripDAO.execute();
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
     * This function takes the list retrieved from the database
     * and passes it to the adapter so the UI can get updated.
     *
     * @param   items   This is the list of items retrieved from the database via a DAO class.
     */
    public void updateList(List<GroupItem> items)
    {
        int length = items.size();
        if (length > 0) {
            adapter = new PurchasesAdapter(this);
            adapter.setData(items);
            listView = (AnimatedExpandableListView) findViewById(R.id.animatedPurchasesELV);
            listView.setVerticalFadingEdgeEnabled(true);
            listView.setAdapter(adapter);
        }
        else
            openErrorDialog();
    }

    /**
     * This function is called just when no purchases were found for the current user.
     */
    private void openErrorDialog()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("No purchases found");
        alertDialog.setMessage("You have not purchased anything yet.");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
                finish();
            }
        });
        alertDialog.show();
    }
}
