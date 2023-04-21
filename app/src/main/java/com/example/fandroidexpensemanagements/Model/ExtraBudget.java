package com.example.fandroidexpensemanagements.Model;

import com.example.fandroidexpensemanagements.Interface.UserMoney;

public class ExtraBudget implements UserMoney {
    private String day;
    private int money;
    private String place;
    private String userID;
    private String name;


    public ExtraBudget(String day, int money, String place, String userID, String name) {
        this.day = day;
        this.money = money;
        this.place = place;
        this.userID = userID;
        this.name = name;
    }

    public ExtraBudget(int money, String place, String userID, String name) {
        this.money = money;
        this.place = place;
        this.userID = userID;
        this.name = name;
    }

    public ExtraBudget(int userMoney, String userID) {
        this.money = userMoney;
        this.userID = userID;
    }

    public ExtraBudget() {
    }

    @Override
    public String getUserID() {
        return null;
    }

    @Override
    public int getUserMoney() {
        return 0;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
