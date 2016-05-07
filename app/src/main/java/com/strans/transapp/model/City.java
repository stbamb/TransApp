package com.strans.transapp.model;

import java.io.Serializable;

/**
 * This is the model for the City object, it is used to adapt the data we get from the database.
 * This class also matches the 'table' City from our database hosted on Firebase.
 * For more info take a look at: https://strans.firebaseio.com/City
 */
public class City implements Serializable {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
