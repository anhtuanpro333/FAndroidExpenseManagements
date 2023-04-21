package com.example.fandroidexpensemanagements.Model;

public class Expense {

    private String idExpense;
    private String idUser;
    private String expenseName;
    private int expenseMoney;
    private String days;

    public Expense(String idExpense, String idUser, String expenseName, int expenseMoney) {
        this.idExpense = idExpense;
        this.idUser = idUser;
        this.expenseName = expenseName;
        this.expenseMoney = expenseMoney;
    }

    public Expense(String idUser, String expenseName, int expenseMoney, String days) {
        this.idUser = idUser;
        this.expenseName = expenseName;
        this.expenseMoney = expenseMoney;
        this.days = days;
    }

    public Expense(String expenseName, int expenseMoney) {
        this.expenseName = expenseName;
        this.expenseMoney = expenseMoney;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getIdExpense() {
        return idExpense;
    }

    public void setIdExpense(String idExpense) {
        this.idExpense = idExpense;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public int getExpenseMoney() {
        return expenseMoney;
    }

    public void setExpenseMoney(int expenseMoney) {
        this.expenseMoney = expenseMoney;
    }
}
