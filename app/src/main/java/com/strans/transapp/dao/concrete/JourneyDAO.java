package com.strans.transapp.dao.concrete;

import android.os.AsyncTask;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.strans.transapp.activities.ResultsActivity;
import com.strans.transapp.model.Journey;

import com.strans.transapp.adapters.JourneyResultAdapter.GroupItem;
import com.strans.transapp.adapters.JourneyResultAdapter.ChildItem;

import java.util.ArrayList;
import java.util.List;

public class JourneyDAO extends AsyncTask<Void, Void, Boolean> {

    private final Firebase FIREBASE_URL; // URL to the 'table' relating to Journey
    private final ArrayList<Journey> journeys;
    private final ResultsActivity resultsActivity;
    private final String id;
    private final String date;
    private final String from;
    private final String to;

    public JourneyDAO(ResultsActivity resultsActivity, String id, String date, String from, String to)
    {
        FIREBASE_URL = new Firebase("https://strans.firebaseio.com/Journey");
        journeys = new ArrayList<>();
        this.resultsActivity = resultsActivity;
        this.id = id;
        this.date = date;
        this.from = from;
        this.to = to;
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
        try
        {
            FIREBASE_URL.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Journey newJourney = postSnapshot.getValue(Journey.class);
                        journeys.add(newJourney);
                    }
                    flag[0] = true;
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    flag[0] = false;
                }
            });
            Thread.sleep(2000);
        }
        catch (InterruptedException e) { flag[0] = false; }

        return flag[0];
    }

    /**
     * Whether the background process was executed successfully or not, this method is executed.
     * Depending on the argument success different scenarios will be executed.
     *
     * @param   success This parameter comes from the doInBackground(Void... params) method.
     */
    @Override
    protected void onPostExecute(final Boolean success) {
        if (success)
        {
            ArrayList<Journey> filteredResults = filterJourneyResults();
            List<GroupItem> list = updateList(filteredResults);
            resultsActivity.updateList(list);
        }
    }

    /**
     * It gives back an ArrayList of the journeys that match the given criteria.
     *
     * @return  ArrayList of journeys that match the given criteria.
     * @see     ArrayList
     */
    private ArrayList<Journey> filterJourneyResults()
    {
        ArrayList<Journey> filteredJourneys = new ArrayList<>();
        int length = journeys.size();

        for (int i = 0; i < length; i++) {
            Journey journey = journeys.get(i);
            if (journey.getId().equals(id) && journey.getDate().equals(date))
                filteredJourneys.add(journey);

        }

        return filteredJourneys;
    }

    /**
     * It gives back a list of the journeys available.
     * This function adapts and shapes the data according to what we need.
     *
     * @param filteredResults   ArrayList of the filtered journeys, this filtering
     *                          is made based on the criteria given by the user.
     * @return                  ArrayList of journeys.
     * @see                     List
     * @see                     ArrayList
     */
    private List<GroupItem> updateList(ArrayList<Journey> filteredResults)
    {
        List<GroupItem> items = new ArrayList<>();
        int length = filteredResults.size();
        for (int i = 0; i < length; i++)
        {
            Journey journey = filteredResults.get(i);
            GroupItem item = new GroupItem();
            item.title = "Option " + (i + 1);
            ChildItem child = new ChildItem();
            String time = journey.getTime();
            String date = journey.getDate();
            int price = journey.getPrice();
            child.from = "From: " + from;
            child.date = "Date: " + date;
            child.to = "To: " + to;
            child.time = "Time: " + time;
            child.price = "Price: " + price + " euros";//"€";
            item.items.add(child);
            items.add(item);
        }
        return items;
    }
}