package com.strans.transapp.model;

/**
 * This is the model for the Purchase object, it is used to adapt the data we get from the database.
 * This class also matches the 'table' Purchase from our database hosted on Firebase.
 * For more info take a look at: https://strans.firebaseio.com/Purchase
 */
public class Purchase {

    private String userID;
    private String journeyID;
    private String journeyTime;
    private String journeyDate;
    private int journeyPrice;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getJourneyID() {
        return journeyID;
    }

    public void setJourneyID(String journeyID) {
        this.journeyID = journeyID;
    }

    public String getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(String journeyTime) {
        this.journeyTime = journeyTime;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public int getJourneyPrice() {
        return journeyPrice;
    }

    public void setJourneyPrice(int journeyPrice) {
        this.journeyPrice = journeyPrice;
    }
}
