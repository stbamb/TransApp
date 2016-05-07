package com.strans.transapp.dao.concrete;

import android.os.AsyncTask;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.strans.transapp.activities.RegisterActivity;

import java.util.Map;

/**
 * Created by Esteban on 28/12/2015.
 */
public class UserDAO extends AsyncTask<Void, Void, Boolean> {

    private final Firebase FIREBASE_URL; // URL to the database to retrieve the info from the user
    private final RegisterActivity registerActivity;
    private final String email;
    private final String password;

    public UserDAO(RegisterActivity registerActivity, String email, String password)
    {
        FIREBASE_URL = new Firebase("https://strans.firebaseio.com/");
        this.registerActivity = registerActivity;
        this.email = email;
        this.password = password;
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
    protected Boolean doInBackground(Void... params)
    {
        final boolean flag[] = new boolean[1];
        try {
            FIREBASE_URL.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    flag[0] = true;
                }
                @Override
                public void onError(FirebaseError firebaseError) {
                    flag[0] = false;
                }
            });

            Thread.sleep(2000);

        }
        catch (InterruptedException e) {}

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
        registerActivity.showDialog(success);
    }
}
