package com.strans.transapp.dao.concrete;

import android.os.AsyncTask;

import com.firebase.client.Firebase;
import com.strans.transapp.model.City;
import com.strans.transapp.model.Purchase;

import java.util.ArrayList;

/**
 * Created by Esteban on 13/12/2015.
 */
public class PurchaseDAO extends AsyncTask<Void, Void, Boolean> {

    private final Firebase FIREBASE_URL; // URL to the 'table' relating to Purchase
    private final ArrayList<City> cities;
    private String id;
    private final String date;
    private final String from;
    private final String to;
    private final String time;
    private final String userID;
    private final int price;

    public PurchaseDAO(ArrayList<City> cities, String date, String from, String to,
                       String time, String userID, int price)
    {
        FIREBASE_URL = new Firebase("https://strans.firebaseio.com/");
        this.cities = cities;
        this.date = date;
        this.from = from;
        this.to = to;
        this.time = time;
        this.userID = userID;
        this.price = price;
        int iFrom = getId(from);
        int iTo = getId(to);
        id = "00" + iFrom + "-" + "00" + iTo;
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
            Firebase purchase_url = FIREBASE_URL.child("Purchase");
            Purchase purchase = new Purchase();
            purchase = preparePurchase(purchase);
            purchase_url.push().setValue(purchase);

            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            return false;
        }

        return flag[0];
    }

    /**
     * Basically this function just returns the id of the city,
     * if no city was found the the function will return -1.
     *
     * @param name  This is the name of the City, based on that we can get the id of the city.
     * @return      The id of the city that corresponds to the name sent as an argument.
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
     * This function prepares the instance of the object Purchase so it can be sent
     * to the database hosted on Firebase. It sets all the information needed.
     *
     * @param purchase  This instance is the one that is going to be changed.
     * @return          Purchase object with all corresponding information.
     * @see             Purchase
     */
    private Purchase preparePurchase(Purchase purchase)
    {
        purchase.setUserID(userID);
        purchase.setJourneyID(id);
        purchase.setJourneyTime(time);
        purchase.setJourneyDate(date);
        purchase.setJourneyPrice(price);
        return purchase;
    }
}
