package com.strans.transapp.dao.concrete;

import android.os.AsyncTask;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.strans.transapp.activities.SearchActivity;
import com.strans.transapp.model.City;

import java.util.ArrayList;

public class CityDAO extends AsyncTask<Void, Void, Boolean> {

    private final Firebase FIREBASE_URL; // URL to the 'table' relating to City
    private final ArrayList<City> cities;
    private final SearchActivity searchActivity;

    public CityDAO(SearchActivity searchActivity) {
        FIREBASE_URL = new Firebase("https://strans.firebaseio.com/City");
        cities = new ArrayList<>();
        this.searchActivity = searchActivity;
    }

    /**
     * Since we are retrieving data from the database we need to do
     * it asynchronously so it doesn't interfere with the app and
     * with what the user is doing at the moment of retrieving the data.
     *
     * @param   params  Void.
     * @return          Boolean array containing only one element, this is going to be used
     *                  by the onPostExecute(final Boolean success) method.
     */
    @Override
    protected Boolean doInBackground(Void... params) {

        final boolean flag[] = new boolean[1];

        try {
            FIREBASE_URL.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        City newCity = postSnapshot.getValue(City.class);
                        cities.add(newCity);
                    }
                    flag[0] = true;
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    flag[0] = false;
                }
            });
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        return flag[0];
    }

    /**
     * Whether the background process was executed successfully or not, this method is executed.
     * Depending on the argument success different scenarios will be executed.
     *
     * @param   success This parameter comes from the doInBackground(Void... params) method.
     *                  A method from SearchActivity is called so the UI
     *                  can be updated with the data that has been retrieved.
     */
    @Override
    protected void onPostExecute(final Boolean success) {
        if (success)
            searchActivity.populateJourneySpinner(cities);
    }
}