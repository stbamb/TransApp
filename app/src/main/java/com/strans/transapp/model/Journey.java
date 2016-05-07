package com.strans.transapp.model;

/**
 * This is the model for the Journey object, it is used to adapt the data we get from the database.
 * This class also matches the 'table' Journey from our database hosted on Firebase.
 * For more info take a look at: https://strans.firebaseio.com/Journey
 */
public class Journey {

    private String id;
    private String date;
    private String time;
    private int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
