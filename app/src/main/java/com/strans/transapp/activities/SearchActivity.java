package com.strans.transapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.firebase.client.Firebase;
import com.strans.transapp.R;
import com.strans.transapp.dao.concrete.CityDAO;
import com.strans.transapp.model.City;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<City> cities;
    private String id;
    private String date;
    private Spinner toSpinner;
    private Spinner fromSpinner;
    private String from;
    private String to;
    private DatePicker datePicker;
    private String userID;
    private Menu menu;

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
        setContentView(R.layout.activity_search);
        Firebase.setAndroidContext(this);
        CityDAO cityDAO = new CityDAO(this);
        cityDAO.execute();
        userID = (String) getIntent().getExtras().get("userID");
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareInfo();
                toResultsActivity();
            }
        });
    }

    /**
     * Function that fills up the 'Spinner' elements with the existing cities.
     * The cities are retrieved from the database via the CityDAO class.

     * @param cities    ArrayList containing of the information related to the retrieved cities.
     *
     */
    public void populateJourneySpinner(ArrayList<City> cities) {
        this.cities = cities;
        String[] citiesList = createCityList();
        fromSpinner = (Spinner) findViewById(R.id.spinner);
        toSpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, citiesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(adapter);
        fromSpinner.setAdapter(adapter);
        menu.getItem(0).setEnabled(true);
    }

    /**
     * This is the function that takes care of disabling one option in the 'Options menu' so
     * it cannot be used before the info from the database has been retrieved.
     *
     * @param menu  This is the menu itself.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.search_activity_menu, menu);
        this.menu = menu;
        menu.getItem(0).setEnabled(false);
        return (super.onCreateOptionsMenu(menu));
    }

    /**
     * This function handles the different possibilities for the 'Options Menu'.
     *
     * @param item  This is the menu itself.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_account:
                toAnotherActivity("MyAccountActivity");
                return true;

            case R.id.action_log_out:
                toAnotherActivity("LoginActivity");
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function creates a list that contains city names.
     *
     * @return  String array containing the name of the cities only.
     */
    private String[] createCityList()
    {
        int length = cities.size();
        String[] list = new String[length];

        for (int i = 0; i < length; i++)
            list[i] = cities.get(i).getName();

        return list;
    }

    /**
     * Basically this function just returns the id of the city,
     * if no city was found the the function will return -1
     *
     * @param name  this is the name of the City, based on that we can get the id of the city
     * @return      the id of the city that corresponds to the name sent as an argument
     */
    private int getId(String name)
    {
        int length = cities.size();

        for (int i = 0; i < length; i++) {
            City city = cities.get(i);
            if (city.getName().equals(name))
                return city.getId();
        }

        return -1;
    }

    /**
     * This function just takes the name of an activity and it is in charge
     * of making the transition from one activity to another. Sometimes it
     * is necessary to pass some arguments to the next activity and this
     * function handles that as well.
     */
    private void toResultsActivity()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", id);
        bundle.putSerializable("date", date);
        bundle.putSerializable("from", from);
        bundle.putSerializable("to", to);
        bundle.putSerializable("userID", userID);
        bundle.putSerializable("cities", cities);
        Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * This function is in charge of formatting data so it can be displayed properly.
     */
    private void prepareInfo()
    {
        from = fromSpinner.getItemAtPosition(fromSpinner.getSelectedItemPosition()).toString();
        to = toSpinner.getItemAtPosition(toSpinner.getSelectedItemPosition()).toString();
        int iFrom = getId(from);
        int iTo = getId(to);
        id = "00" + iFrom + "-" + "00" + iTo;
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        date = formatDate(day, month, year);
    }

    /**
     * This function is in charge of formatting the date so it matches the format of the database.
     *
     * @param day   Day.
     * @param month Month.
     * @param year  Year.
     * @return      String containing the formatted date.
     * @see         String
     */
    private String formatDate(int day, int month, int year)
    {
        String date = "";

        if (day < 10)
            date = "0" + day + "." + month + "." + year;

        if (month < 10)
            date = day + ".0" + month + "." + year;

        if (day < 10 && month < 10)
            date = "0" + day + ".0" + month + "." + year;
        else if (day > 9 && month > 9)
            date = day + "." + month + "." + year;

        return date;
    }

    /**
     * This function just takes the name of an activity and it is in charge
     * of making the transition from one activity to another. Sometimes it
     * is necessary to pass some arguments to the next activity and this
     * function handles that as well.
     *
     * @param name  The name of the next activity, the one to be initialized.
     */
    private void toAnotherActivity(String name) {
        Intent intent = null;
        String packageName = "com.strans.transapp.activities.";

        try
        {
            intent = new Intent(this.getApplicationContext(), Class.forName(packageName + name));
        }
        catch (ClassNotFoundException e) {}

        Bundle bundle = new Bundle();
        bundle.putSerializable("userID", userID);
        bundle.putSerializable("cities", cities);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
