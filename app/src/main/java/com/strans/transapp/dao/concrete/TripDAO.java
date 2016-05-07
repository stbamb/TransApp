package com.strans.transapp.dao.concrete;

import android.os.AsyncTask;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.strans.transapp.activities.MyAccountActivity;
import com.strans.transapp.model.City;
import com.strans.transapp.model.Purchase;
import com.strans.transapp.adapters.PurchasesAdapter.GroupItem;
import com.strans.transapp.adapters.PurchasesAdapter.ChildItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban on 29/12/2015.
 */
public class TripDAO extends AsyncTask<Void, Void, Boolean> {

    private final Firebase FIREBASE_URL; // URL to the 'table' relating to Purchase
    private final MyAccountActivity myAccountActivity;
    private final ArrayList<Purchase> purchases;
    private final String userID;
    private final ArrayList<City> cities;

    public TripDAO(String userID, ArrayList<City> cities, MyAccountActivity myAccountActivity)
    {
        FIREBASE_URL = new Firebase("https://strans.firebaseio.com/Purchase");
        purchases = new ArrayList<>();
        this.userID = userID;
        this.cities = cities;
        this.myAccountActivity = myAccountActivity;
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
                        Purchase newPurchase = postSnapshot.getValue(Purchase.class);
                        if (newPurchase.getUserID().equals(userID))
                            purchases.add(newPurchase);
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
            List<GroupItem> list = updateList();
            myAccountActivity.updateList(list);
        }
    }

    /**
     * It gives back a list of the purchases made by an user so it can be shown to the user.
     *
     * @return      List of items corresponding to the purchases of a specific user.
     * @see         List
     */
    private List<GroupItem> updateList()
    {
        List<GroupItem> items = new ArrayList<>();
        int length = purchases.size();
        for (int i = 0; i < length; i++)
        {
            Purchase purchase = purchases.get(i);
            GroupItem item = new GroupItem();
            item.title = "Purchase " + (i + 1);
            ChildItem child = new ChildItem();
            String time = purchase.getJourneyTime();
            String journeyDate = purchase.getJourneyDate();
            String id = purchase.getJourneyID();
            int price = purchase.getJourneyPrice();
            String from = getName(id.substring(0, 3));
            String to = getName(id.substring(4, 7));
            child.from = "From: " + from;
            child.journeyDate = "Date: " + journeyDate;
            child.to = "To: " + to;
            child.time = "Time: " + time;
            child.price = "Price: " + price + " euros";//"€";
            item.items.add(child);
            items.add(item);
        }
        return items;
    }

    /**
     * Basically this function just returns the name of the city.
     *
     * @param id    This is the id of the City, based on that we can get the name of the city.
     * @return      Name of the city that corresponds to the id sent as an argument.
     */
    private String getName(String id)
    {
        int length = cities.size();
        for (int i = 0; i < length; i++) {
            City city = cities.get(i);
            if (city.getId() == Integer.parseInt(id))
                return city.getName();
        }
        return "";
    }
}