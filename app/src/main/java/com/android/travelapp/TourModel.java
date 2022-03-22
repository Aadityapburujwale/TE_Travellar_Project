package com.android.travelapp;

import java.util.ArrayList;

public class TourModel {
    String tourImg;
    String tourName;
    String tourDesc;
    String tourPrice;
    String tourLoc;

    public TourModel(String tourImg, String tourName, String tourDesc, String tourPrice, String tourLoc) {
        this.tourImg = tourImg;
        this.tourName = tourName;
        this.tourDesc = tourDesc;
        this.tourPrice = tourPrice;
        this.tourLoc = tourLoc;
    }

    public TourModel() {

    }

    public String getTourImg() {
        return tourImg;
    }

    public void setTourImg(String tourImg) {
        this.tourImg = tourImg;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourDesc() {
        return tourDesc;
    }

    public void setTourDesc(String tourDesc) {
        this.tourDesc = tourDesc;
    }

    public String getTourPrice() {
        return tourPrice;
    }

    public void setTourPrice(String tourPrice) {
        this.tourPrice = tourPrice;
    }

    public String getTourLoc() {
        return tourLoc;
    }

    public void setTourLoc(String tourLoc) {
        this.tourLoc = tourLoc;
    }
}