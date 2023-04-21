package com.example.fandroidexpensemanagements.Model;

public class MonthlyBudget {
    private int money;
    private String place;
    private String userID;
    private String name;

    public MonthlyBudget(int money, String place, String userID, String name) {
        this.money = money;
        this.place = place;
        this.userID = userID;
        this.name = name;
    }

    public MonthlyBudget(int money, String userID) {
        this.money = money;
        this.userID = userID;
    }

    public MonthlyBudget() {
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

    public String getUserID() {
        return userID;
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
